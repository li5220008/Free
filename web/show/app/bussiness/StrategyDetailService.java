package bussiness;

import controllers.AuthorBaseIntercept;
import dbutils.QicDbUtil;
import dbutils.QicoreDbUtil;
import dbutils.SqlLoader;
import dto.*;
import models.common.StrategyBaseinfo;
import models.common.UserStrategyCollect;
import org.apache.commons.lang.time.DateUtils;
import play.Logger;
import play.mvc.With;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 策略详情
 * User: panzhiwei
 * Date: 12-11-10
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */
@With({AuthorBaseIntercept.class})
public class StrategyDetailService {
    //策略基本信息
    public static StrategyBaseinfo getStrategyDetail(long stid) {

        StrategyBaseinfo strategyDetail = StrategyBaseinfo.findById(stid);
        if (strategyDetail == null) {
            StrategyBaseinfo strategyBaseinfo = new StrategyBaseinfo();
            strategyDetail = strategyBaseinfo;
        }
        return strategyDetail;
    }

    //策略持仓
    public static List<StrategyPositionDto> getStrategyPosition(long stid) {
        String strategy_baseinfo_sql = SqlLoader.getSqlById("strategy_baseinfo");
        StrategyBaseinfo strategyBaseinfo = QicDbUtil.queryQicDBSingleBean(strategy_baseinfo_sql, StrategyBaseinfo.class, stid);

        String sql = SqlLoader.getSqlById("StrategyPosition");
        List<StrategyPositionDto> strategyPositionDtoList = QicoreDbUtil.queryQicoreDBBeanList(sql, StrategyPositionDto.class, strategyBaseinfo.stUuid);
        if (strategyPositionDtoList != null) {
            for (StrategyPositionDto sp : strategyPositionDtoList) {
                sp.name = strategyBaseinfo.name;
                sp.trade_variety = strategyBaseinfo.tradeVariety;
            }
        } else {
            List<StrategyPositionDto> strategyPositionDtoList1 = new ArrayList<StrategyPositionDto>();
            strategyPositionDtoList = strategyPositionDtoList1;
        }
        return strategyPositionDtoList;
    }

    //委托记录
    public static List<AuthorizeRecordDto> getAuthorizeRecord(long stid) {
        String strategy_baseinfo_sql = SqlLoader.getSqlById("strategy_baseinfo");
        StrategyBaseinfo strategyBaseinfo = QicDbUtil.queryQicDBSingleBean(strategy_baseinfo_sql, StrategyBaseinfo.class, stid);

        String sql = SqlLoader.getSqlById("AuthorizeRecord");
        List<AuthorizeRecordDto> authorizeRecordDtoList = QicoreDbUtil.queryQicoreDBBeanList(sql, AuthorizeRecordDto.class, strategyBaseinfo.stUuid);
        if (authorizeRecordDtoList != null) {
            for (AuthorizeRecordDto ar : authorizeRecordDtoList) {
                ar.name = strategyBaseinfo.name;
                ar.trade_variety = strategyBaseinfo.tradeVariety;
            }
        } else {
            List<AuthorizeRecordDto> authorizeRecordDtoList1 = new ArrayList<AuthorizeRecordDto>();
            authorizeRecordDtoList = authorizeRecordDtoList1;
        }
        return authorizeRecordDtoList;
    }

    //成交记录
    public static List<ExecutionRecordDto> getExecutionRecord(Long stid) {
        String strategy_baseinfo_sql = SqlLoader.getSqlById("strategy_baseinfo");
        StrategyBaseinfo strategyBaseinfo = QicDbUtil.queryQicDBSingleBean(strategy_baseinfo_sql, StrategyBaseinfo.class, stid);

        String sql = SqlLoader.getSqlById("ExecutionRecord");
        List<ExecutionRecordDto> executionRecordDtoList = QicoreDbUtil.queryQicoreDBBeanList(sql, ExecutionRecordDto.class, strategyBaseinfo.stUuid);
        if (executionRecordDtoList != null) {
            for (ExecutionRecordDto er : executionRecordDtoList) {
                er.name = strategyBaseinfo.name;
                er.trade_variety = strategyBaseinfo.tradeVariety;
            }
        } else {
            List<ExecutionRecordDto> executionRecordDtoList1 = new ArrayList<ExecutionRecordDto>();
            executionRecordDtoList = executionRecordDtoList1;
        }
        return executionRecordDtoList;
    }

    //根据策略id查询是否被收藏
    public static boolean iscollect(long stid, long uid) {
        UserStrategyCollect userStrategyCollect = UserStrategyCollect.find("byStidAndUid", stid, uid).first();
        if (userStrategyCollect == null)
            return false;
        else
            return true;

    }

    //策略交易简单信息展示
    public static StrategyBaseDto getstratebasicinfo(long stid) {
        StrategyBaseinfo strategy = StrategyBaseinfo.findById(stid);
        if(strategy == null){
            StrategyBaseinfo strategy1 = new StrategyBaseinfo();
            strategy = strategy1;
        }
        StrategyBaseDto strategyBaseDto = new StrategyBaseDto();
        strategyBaseDto.sname = strategy.name;
        strategyBaseDto.stype = strategy.tradeType;
        strategyBaseDto.upTime = strategy.upTime;
        strategyBaseDto.collectCount = strategy.collectCount;
        if(strategy.discussCount == 0)
            strategyBaseDto.starLevel = 0;
        else
            strategyBaseDto.starLevel = (float)strategy.discussTotal / strategy.discussCount;
        return strategyBaseDto;
    }

    //绩效指标
    public static IndicatorDto getindicator(long stid) {
        String sql = SqlLoader.getSqlById("Indicator");
        IndicatorDto indicator = QicDbUtil.queryQicDBSingleBean(sql, IndicatorDto.class, stid);
        if(indicator == null){
            IndicatorDto indicatorDto = new IndicatorDto();
            indicator = indicatorDto;
        }
        return indicator;
    }

    //策略委托信号
    public static List<AuthorizeRecordDto> getorder_signal(long stid) {
        String strategy_baseinfo_sql = SqlLoader.getSqlById("strategy_baseinfo");
        StrategyBaseinfo strategyBaseinfo = QicDbUtil.queryQicDBSingleBean(strategy_baseinfo_sql,StrategyBaseinfo.class,stid);
        String sql = SqlLoader.getSqlById("order_signal");
        List<AuthorizeRecordDto> ordersignallist = QicoreDbUtil.queryQicoreDBBeanList(sql, AuthorizeRecordDto.class, strategyBaseinfo.stUuid);
        if(ordersignallist == null){
            List<AuthorizeRecordDto> authorizeRecordDtoList = new ArrayList<AuthorizeRecordDto>();
            ordersignallist = authorizeRecordDtoList;
        }
        return ordersignallist;
    }



    /**
     *     单个策略 图形展示（日线）
     *     这个方法的主要作用是组装数据
     *     需要的数据：
     *     1.格式化近三个月的起始时间 maxDate minDate
     *     2.列出最近三个月收益率的最大值 最小值  取绝对值最大的数modYield
     *     3.把数据组装成[{name=“策略名1” date:[ Date.UTC(2010, 0, 1), 9.05],...},
     *     {name=“策略名2” date:[ Date.UTC(2010, 1, 1), 9.05],...}]类型
     *     @param  strategyId 策略ID
     *     @retrun  arr_strategys[]数组  按顺序依次为： 最大日期，
     *                                                最小日期，
     *                                                最大收益率，
     *                                                按绘图格式组装好的单天或者单周收益率和时间，
     *                                                收益率均值，
     *                                                初始资金
     */

    public static  String[] strategyDetailForDayPictrue(String strategyId){
        String[] arr_strategys = new String[5];
        if (strategyId==null){
            return arr_strategys;
        }
        //默认策略名
        String sname="--";
        //取当前日期
        Date maxDate=new Date() ;
        String minDate;
        float maxYield;
        String strategy = "";//组装好的数据放在这里
        String pictrue_sql_for_day = SqlLoader.getSqlById("pictrue_sql_for_day");
        //取当前策略数据库最大（最近）时间
        // maxDate  =  StrategyContrastService.getlatelyDate(strategyId);
        //取89天以前的时间
        minDate = StrategyContrastService.getTime(maxDate,5,-89);
        //取最近三个月的收益率
        List<StrategyDailyYieldDto>  yield_list_for_day = QicDbUtil.queryQicDBBeanList(pictrue_sql_for_day, StrategyDailyYieldDto.class,strategyId,minDate);
        Iterator<StrategyDailyYieldDto> it =  yield_list_for_day.iterator();
        while (it.hasNext()){
            StrategyDailyYieldDto sdy = it.next();
            sname =sdy.sname;
            String s = StrategyContrastService.combinationData(sdy) ;
            strategy +=(s+"\n");
        }
        //剔除字符串中最后一个“,”
        int flag = strategy.lastIndexOf(",");
        if(flag == -1){
            strategy = "[{name:'"+sname+"', data:[[Date.UTC(2012,01,01),1.00]]}]";
        }else{
            //数据拼接完成
            strategy = "[{name:'"+sname+"', data:["+strategy.substring(0,flag)+"]}]";
        }

        //取当前策略绝对值最大的yield
        maxYield =  StrategyContrastService.getmaxModYield(strategyId,minDate);
        //取最大时间
        String str_maxDate = "";
        if(maxDate!=null){
            str_maxDate = StrategyContrastService.getFormatMaxorMinDate(maxDate);
        }

        //取最小时间
        String str_minDate ="";
        Date d_minDate=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d_minDate =  sdf.parse(minDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(d_minDate!=null){
            str_minDate = StrategyContrastService.getFormatMaxorMinDate(d_minDate);

        }
        //拿到初始资金
        BigDecimal initialCapitalObject = getInitialCapital(strategyId);
        float initialCapital = 0;
        if(initialCapitalObject != null){
            initialCapital = initialCapitalObject.floatValue();
        }
        String str_maxYield = String.format("%.2f",maxYield*100);
        //（最大收益率/3）来表示图中收益率Y轴坐标间隔 保留小数点后两位 无论小数点后第三位是否大于5都入位
        String str_average_Yield = new BigDecimal((maxYield/3)*100).setScale(2, BigDecimal.ROUND_CEILING).toString();
        /**    图中资金Y轴坐标间隔 保留小数点后两位 无论小数点后第三位是否大于5都入位
         *     最大策略回测资金 = （最大收益率（maxYield）+1）*初始资金
         *     最小策略回测资金 = （1-最小收益率（maxYield））*初始资金
         *      Y轴坐标间隔 =   （最大策略回测资金-  最小策略回测资金）/6   (这里假设初始资金为1000)
         */
       // String str_average_fund = new BigDecimal((2*maxYield/6)*initialCapital).setScale(2, BigDecimal.ROUND_CEILING).toString();
        if(Logger.isDebugEnabled()){
            Logger.debug("-------------日线-------------------");
            Logger.debug("minDate=========="+minDate);
            Logger.debug("strategys============" + strategy);
            Logger.debug("maxYield============"+maxYield);
            Logger.debug("str_maxDate============"+str_maxDate);
            Logger.debug("str_minDate============"+str_minDate);
            Logger.debug("str_preYield==============="+str_average_Yield);
          //  Logger.debug("str_average_fund_day==============="+str_average_fund);
        }
        //最大日期，最小日期，最大收益率，按绘图格式组装好的单天或者单周收益率和时间(series)， 收益率均值，回测资金均值，初始资金
         arr_strategys = new String[]{str_maxDate,str_minDate,str_maxYield,strategy,str_average_Yield,initialCapital+""};

           return  arr_strategys;
    }


    /**
     *     单个策略 图形展示（周线）
     *     这个方法的主要作用是组装数据
     *     需要的数据：
     *     1.格式化近三个月的起始时间 maxDate minDate
     *     2.列出最近三个月收益率的最大值 最小值  取绝对值最大的数modYield
     *     3.把数据组装成[{name=“策略名1” date:[ Date.UTC(2010, 0, 1), 9.05],...},
     *     {name=“策略名2” date:[ Date.UTC(2010, 1, 1), 9.05],...}]类型
     *     @param  strategyId 策略ID
     *     @retrun  arr_strategys[]数组  按顺序依次为： 最大日期，
     *                                                最小日期，
     *                                                最大收益率，
     *                                                按绘图格式组装好的单天或者单周收益率和时间(series)，
     *                                                收益率均值，
     *                                                初始资金
     */

    public static  String[] strategyDetailForWeekPictrue(String strategyId){
        String[] arr_strategys = new String[5] ;
        if (strategyId==null){
            return arr_strategys;
        }
        //设置图片X轴坐标点的间隔距离 日线间隔20天，周线间隔7天
        String interval_days ="20";
        //默认策略名
        String sname="--";
        Date maxDate=new Date() ;
        String minDate ;
        float maxYield ;
        String strategy = "";//组装好的数据放在这里
        String pictrue_sql_for_week = SqlLoader.getSqlById("pictrue_sql_for_week");

        //取当前策略数据库最大（最近）时间
       //maxDate  =  StrategyContrastService.getlatelyDate(strategyId);
        //取89天以前的时间
        minDate = StrategyContrastService.getTime(maxDate,5,-89);
        //取最近三个月的收益率
        List<StrategyDailyYieldDto> yield_list_for_week = QicDbUtil.queryQicDBBeanList(pictrue_sql_for_week, StrategyDailyYieldDto.class, strategyId,minDate);
        Iterator<StrategyDailyYieldDto> it =  yield_list_for_week.iterator();
        while (it.hasNext()){
            StrategyDailyYieldDto sdy = it.next();
            sname = sdy.sname;
            String s = StrategyContrastService.combinationData(sdy) ;
            strategy +=(s+"\n");
        }
        //剔除字符串中最后一个“,”
        int flag = strategy.lastIndexOf(",");
        if(flag == -1){//数据库没有数据 给一个初始化值 页面图形就能正确展示
            strategy = "[{name:'"+sname+"', data:[[Date.UTC(2012,01,01),1.00]]}]";
        }else{
            //数据拼接完成
            strategy = "[{name:'"+sname+"', data:["+strategy.substring(0,flag)+"]}]";
        }

        //取当前策略绝对值最大的yield
        maxYield = StrategyContrastService.getmaxModYield(strategyId,minDate);
        //取最大时间
        String str_maxDate = "";
        if(maxDate!=null){
            str_maxDate = StrategyContrastService.getFormatMaxorMinDate(maxDate);
        }
        //取最小时间
        String str_minDate ="";
        Date d_minDate=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d_minDate =  sdf.parse(minDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(d_minDate!=null){
            str_minDate = StrategyContrastService.getFormatMaxorMinDate(d_minDate);

        }
        //拿到初始资金
        BigDecimal initialCapitalObject = getInitialCapital(strategyId);
        float initialCapital = 0;
        if(initialCapitalObject != null){
            initialCapital = initialCapitalObject.floatValue();
        }
        String str_maxYield = String.format("%.2f",maxYield*100);
        //（最大收益率/3）来表示图中Y轴坐标间隔 保留小数点后两位 无论小数点后第三位是否大于5都入位
        String str_average_Yield = new BigDecimal((maxYield/3)*100).setScale(2, BigDecimal.ROUND_CEILING).toString();
        /**    图中资金Y轴坐标间隔 保留小数点后两位 无论小数点后第三位是否大于5都入位
         *     最大策略回测资金 = （最大收益率（maxYield）+1）*初始资金
         *     最小策略回测资金 = （1-最小收益率（maxYield））*初始资金
         *      Y轴坐标间隔 =   （最大策略回测资金-  最小策略回测资金）/6   (这里假设初始资金为1000)
         */
      //  String str_average_fund = new BigDecimal((2*maxYield/6)*initialCapital).setScale(2, BigDecimal.ROUND_CEILING).toString();
        if(Logger.isDebugEnabled()){
            Logger.debug("-------------周线-------------------");
            Logger.debug("minDate=========="+minDate);
            Logger.debug("strategys============" + strategy);
            Logger.debug("maxYield============"+maxYield);
            Logger.debug("str_maxDate============"+str_maxDate);
            Logger.debug("str_minDate============"+str_minDate);
            Logger.debug("str_preYield==============="+str_average_Yield);
          //  Logger.debug("str_average_fund==============="+str_average_fund);
        }
          arr_strategys = new String[]{str_maxDate,str_minDate,str_maxYield,strategy,str_average_Yield,initialCapital+""};

        return  arr_strategys;
    }
     //拿到初始资金
    public static BigDecimal getInitialCapital(String strategyId){
        String get_initial_capital_sql = SqlLoader.getSqlById("get_initial_capital");
        Map<String, Object> initialCapitalMap = QicoreDbUtil.queryQicoreDBSingleMap(get_initial_capital_sql, strategyId);
        if(initialCapitalMap==null){
            return null;
        }
        return  (BigDecimal)initialCapitalMap.get("inicapital");

    }

    /**
     * 加入订阅
     * @param month
     * @param uid
     * @param stid
     */
    public static Date addstrategyorder(int month, Long uid, Long stid) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到下架时间
        String getdowntime_sql = SqlLoader.getSqlById("getdowntime_sql");
        Map<String,Object> map = QicDbUtil.queryQicDBSingleMap(getdowntime_sql,stid);
        String downtime = "";
        if(map.get("down_time") == null){
            downtime = "2099-12-12";
        }
        else {
            downtime = map.get("down_time").toString();
        }

        Date downtime2 = new Date();
        try {
            downtime2  = sdf.parse(downtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //订阅结束时间
        Date date = new Date();
        Date newDate = DateUtils.addMonths(date, month);

        long l = newDate.getTime() - downtime2.getTime();
        long day = l/(24*60*60*1000);
        long hour = (l/(60*60*1000)-24*day);
        long min = (l/(60*1000)-day*24*60-hour*60);

        //插入订阅时间
        if (min <=0 ){
            String insert_orderetime_sql = SqlLoader.getSqlById("insert_downtime");
            QicDbUtil.updateQicDB(insert_orderetime_sql, uid, stid,newDate);
            String add_sumOrderCount_sql = SqlLoader.getSqlById("add_sumOrderCount_sql");
            QicDbUtil.updateQicDB(add_sumOrderCount_sql,stid);
            return newDate;
        }
        else {
            return null;
        }
    }

    /**
     * 续订
     * @param month
     * @param uid
     * @param stid
     * @return
     */
    public static Date delaystrategyorder(int month, Long uid, Long stid){
        //得到下架时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getdowntime_sql = SqlLoader.getSqlById("getdowntime_sql");
        Map<String,Object> map = QicDbUtil.queryQicDBSingleMap(getdowntime_sql,stid);
        String downtime = "";
        if(map.get("down_time") == null){
            downtime = "2099-12-12";
        }
        else {
            downtime = map.get("down_time").toString();
        }

        Date downtime2 = new Date();
        try {
            downtime2  = sdf.parse(downtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String get_orderetime_sql = SqlLoader.getSqlById("get_orderetime_sql");
        StrategyOrderDto strategyorder = QicDbUtil.queryQicDBSingleBean(get_orderetime_sql, StrategyOrderDto.class, uid, stid);
        //订阅结束时间
        Date newDate = DateUtils.addMonths(strategyorder.order_etime, month);

        long l =  newDate.getTime() - downtime2.getTime();
        long day = l/(24*60*60*1000);
        long hour = (l/(60*60*1000)-24*day);
        long min = (l/(60*1000)-day*24*60-hour*60);
        //更新订阅时间
        if(min <=0 ){
            String update_orderetime_sql = SqlLoader.getSqlById("update_downtime");
            QicDbUtil.updateQicDB(update_orderetime_sql, newDate, uid, stid);
            return newDate;
        }
        else {
            return null;
        }

    }

    /**
     * 判断是否订阅
     * @param uid
     * @param stid
     * @return
     */
    public static boolean isorder(Long uid, Long stid){
        String isorder_sql = SqlLoader.getSqlById("isorder_sql");
        StrategyOrderDto strategy = QicDbUtil.queryQicDBSingleBean(isorder_sql,StrategyOrderDto.class,uid,stid);
        if(strategy != null){
           return true;
        }
        else {
            return false;
        }

    }




}
