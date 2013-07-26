package controllers;

import annotation.QicFunction;
import bussiness.StrategyDetailService;
import bussiness.StrategyUserDiscussService;
import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.*;
import models.common.StrategyBaseinfo;
import models.common.UserStrategyDiscuss;
import org.apache.commons.lang.time.DateUtils;
import sun.util.resources.LocaleNames_ga;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略详情
 * User: liangbing
 * Date: 12-11-10
 * Time: 上午9:58
 */
public class StrategyDetail  extends BasePlayControllerSupport {
    @QicFunction(id=2)
    public static void detail(long stid,String strategyId,long uid){
        long stime  = System.currentTimeMillis();
        //得到策略基本信息
        StrategyBaseinfo strategyDetail = StrategyDetailService.getStrategyDetail(stid);
        //查询策略是否被收藏
        boolean b = StrategyDetailService.iscollect(stid,uid);
        //策略简单信息展示
        StrategyBaseDto strategyBaseinfo = StrategyDetailService.getstratebasicinfo(stid);
        //绩效指标
        IndicatorDto indicator = StrategyDetailService.getindicator(stid);

        //策略委托信号
        List<AuthorizeRecordDto> ordersignallist = StrategyDetailService.getorder_signal(stid);

        // 收益率走势图
        //周线
        String[] weekDates = StrategyDetailService.strategyDetailForWeekPictrue(strategyId);
        if(weekDates[3]==null){//当series没有数据 避免JS报错
            weekDates[3]="[]";
        }
        //日线
        String[] dayDates = StrategyDetailService.strategyDetailForDayPictrue(strategyId);
        if(dayDates[3]==null){//当series没有数据 避免JS报错
            dayDates[3]="[]";
        }

        //判断句是否订阅
        boolean isorder = StrategyDetailService.isorder(uid,stid);
        //订阅到期时间
        String isorder_sql = SqlLoader.getSqlById("isorder_sql");
        StrategyOrderDto strategy = QicDbUtil.queryQicDBSingleBean(isorder_sql,StrategyOrderDto.class,uid,stid);
        Date strategy_etime = new Date();
        if(strategy != null){
           strategy_etime = strategy.order_etime;
        }
        int discussesBoolean = StrategyUserDiscussService.judge(stid,uid);
        System.out.println("detail=====================cost time :" + (System.currentTimeMillis()-stime));
        render(b,strategyDetail,uid,stid,strategyBaseinfo,indicator,weekDates,dayDates,ordersignallist,isorder,strategy_etime,discussesBoolean);
    }

    //策略持仓
    @QicFunction(id=2)
    public static void holdPosition(long id){
        List<StrategyPositionDto> strategyPositionDtoList = StrategyDetailService.getStrategyPosition(id);
        render(strategyPositionDtoList);
    }
    //委托记录
    @QicFunction(id=2)
    public static void entrustRecord(long id){
        List<AuthorizeRecordDto> authorizeRecordDtoList = StrategyDetailService.getAuthorizeRecord(id);
        render(authorizeRecordDtoList);
    }
    //成交记录
    @QicFunction(id=2)
    public static void bargainRecord(long id){
        List<ExecutionRecordDto> executionRecordDtoList = StrategyDetailService.getExecutionRecord(id);
        render(executionRecordDtoList);
    }
    //我要评论历史数据
    @QicFunction(id=4)
    public static void userComment(long id,long uid){
         List<UserStrategyDiscuss> discussesList =StrategyUserDiscussService.userDiscussList(id);
        int discussesBoolean = StrategyUserDiscussService.judge(id,uid);
        render(discussesBoolean,discussesList);
    }

    /**
     * 保存评论
     * @param uid
     * @param stid
     * @param usd
     */
    @QicFunction(id=4)
    public static void userDiscuss(Long uid,Long stid,UserStrategyDiscuss usd){
        if(uid!=null&&stid!=null){
            usd.disTime=new Date();
            StrategyUserDiscussService.saveDiscuss(usd,uid,stid);
        }
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("op", true);
        json.put("msg", "提交成功");
        json.put("dname",usd.user.name);
        json.put("star",usd.star);
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String time = df.format(usd.disTime);
        // String time = DateUtil.formatDate(usd.disTime);
        json.put("time",time);
        json.put("content",usd.content);
        renderJSON(json);
    }

    /**
     * 加入订阅
     * @param month
     * @param uid
     * @param stid
     */
    @QicFunction(id=4)
    public static void addStrategyOrder(int month,Long uid,Long stid){
        Map<String, Object> json = new HashMap<String, Object>();
        Date edate = DateUtils.addMonths(new Date(), month);
        if(edate.getYear() + 1900 > 2030){
            json.put("message","订阅到期日期不能超过2030年");
            json.put("success",false);
        }
        else{
            Date newDate = StrategyDetailService.addstrategyorder(month,uid,stid);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(newDate != null){
                json.put("message","订阅成功");
                json.put("success",true);
                json.put("date",sdf.format(newDate));
            }
            else {
                String getdowntime_sql = SqlLoader.getSqlById("getdowntime_sql");
                Map<String,Object> map = QicDbUtil.queryQicDBSingleMap(getdowntime_sql,stid);
                String downtime = "";
                if(map.get("down_time") == null){
                    downtime = "2099-12-12";
                }
                else {
                    downtime = map.get("down_time").toString();
                }
                downtime = downtime.substring(0,downtime.length()-2);
                StringBuffer sb = new StringBuffer("该策略将于");
                sb.append(downtime);
                sb.append("下架,请重新选择订阅时间");

                json.put("message",sb);
                json.put("success",false);

            }
        }

        renderJSON(json);
    }

    /**
     * 续订
     * @param month
     * @param uid
     * @param stid
     */
    @QicFunction(id=4)
    public static void delayStrategyOrder(int month,Long uid,Long stid){
        Map<String, Object> json = new HashMap<String, Object>();
        String get_orderetime_sql = SqlLoader.getSqlById("get_orderetime_sql");
        StrategyOrderDto strategyorder = QicDbUtil.queryQicDBSingleBean(get_orderetime_sql, StrategyOrderDto.class, uid, stid);
        Date edate = DateUtils.addMonths(strategyorder.order_etime, month);
        if(edate.getYear() + 1900 > 2030){
            json.put("message","订阅到期日期不能超过2030年");
            json.put("success",false);
        }
        else{
            Date newDate = StrategyDetailService.delaystrategyorder(month,uid,stid);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(newDate != null){
                json.put("message","续订成功");
                json.put("success",true);
                json.put("date",sdf.format(newDate));
            }
            else {
                String getdowntime_sql = SqlLoader.getSqlById("getdowntime_sql");
                Map<String,Object> map = QicDbUtil.queryQicDBSingleMap(getdowntime_sql,stid);
                String downtime = "";
                if(map.get("down_time") == null){
                    downtime = "2099-12-12";
                }
                else {
                    downtime = map.get("down_time").toString();
                }
                downtime = downtime.substring(0,downtime.length()-2);
                StringBuffer sb = new StringBuffer("该策略将于");
                sb.append(downtime);
                sb.append("下架,请重新选择订阅时间");

                json.put("message",sb);
                json.put("success",false);
            }
        }

        renderJSON(json);
    }
}
