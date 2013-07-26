package bussiness;

import dbutils.QicDbUtil;
import dbutils.QicoreDbUtil;
import dbutils.SqlLoader;
import dto.StrategyContrastDto;
import dto.StrategyDailyYieldDto;
import play.Logger;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 处理 策略对比表格展示
 * User: liuhongjiang
 * Date: 12-11-9
 * Time: 下午4:54
 */
public class StrategyContrastService {

        //策略对比 表格展示  涉及到三张表（high_freq_hist_indicator，strategy_baseinfo，low_freq_hist_indicator）做成分库查询
        public static List<StrategyContrastDto> strategyContrast(String idArray[]){
            if(idArray==null||idArray.length==0){
                return null;
            }
            //策略ID
            List<StrategyContrastDto> strategyContrastlist = new ArrayList<StrategyContrastDto>();
            //根据策略ID查 策略（qic_db.strategy_high_low)的相关信息
            String highFreqStrategyContrastSql = SqlLoader.getSqlById("high_freq_strategy_contrast");
            //查询策略名(qic_db.strategy_baseinfo)
            String queryStrategyNameSql = SqlLoader.getSqlById("queryStrategyName");
             //为页面策略名称添加颜色样式
            String[] colors = new String[]{"#059af8","#a67ec7","#2bb431","#fb3118","#ef9c00"};

            for (int i=0;i<idArray.length;i++){
                Map<String, Object> highMap = QicDbUtil.queryQicDBSingleMap(highFreqStrategyContrastSql, idArray[i]);
                //Map<String, Object> lowMap = QicoreDbUtil.queryQicoreDBSingleMap(lowFreqStrategyContrastSql, idArray[i]);
                StrategyContrastDto sci = QicDbUtil.queryQicDBSingleBean(queryStrategyNameSql,StrategyContrastDto.class,idArray[i]);
                if(sci ==null){
                    sci = new StrategyContrastDto();
                }
                sci.color = colors[i];
                if(highMap!=null && highMap.size()!=0){
                    sci.maxSingleDeficit = (BigDecimal) highMap.get("maxsingledeficit");
                    sci.maxSingleProfit = (BigDecimal) highMap.get("maxsingleprofit");
                    sci.profitCount = (Integer) highMap.get("profitcount");
                    sci.profitRatio = (BigDecimal) highMap.get("profitratio");
                    sci.yield = (BigDecimal) highMap.get("yield");
                    sci.yieldOfMonth = (BigDecimal) highMap.get("yieldofmonth");
                    sci.yieldOfYear = (BigDecimal) highMap.get("yieldofyear");
                    sci.tradeDays = (Integer) highMap.get("tradedays");
                    sci.sharpeIndex = (BigDecimal) highMap.get("sharpeindex");
                    sci.yieldOfMonthSD = (BigDecimal) highMap.get("ymsd");

                }
                //将组合的数据放入list
                strategyContrastlist.add(sci) ;
            }
                 return strategyContrastlist;
        }


    /**
     *     策略对比 图形展示
     *     这个方法的主要作用是组装数据
     *     需要的数据：
     *     1.格式化近三个月的起始时间 maxDate minDate
     *     2.列出最近三个月收益率的最大值 最小值  取绝对值最大的modYield
     *     3.把数据组装成[{name=“策略名1” date:[ Date.UTC(2010, 0, 1), 9.05],...},
     *                  {name=“策略名2” date:[ Date.UTC(2010, 1, 1), 9.05],...}]类型
     *     @param  idArray 策略ID数组
     *     @retrun  arr_strategys[]数组  放入的数据依次为： 最大日期（对应X轴最大值），
     *                                                   最小日期（对应X轴最小值），
     *                                                   最大收益率（对应Y轴最大值），
     *                                                   按highcharts格式组装好的一个或多个策略的单天收益率和时间，
     *                                                   Y轴最小间距，X轴最小间距
     *
     */

    public static  String[] strategyContrastForPictrue(String idArray[]){

        String[]   arr_strategys = new String[5];
        //默认策略名
          String sname="--";
        //组装多个策略数据
        String strategys="";
        //取当前日期作为最大时间
        Date maxDate=new Date() ;
        String minDate=null ;
        float maxYield = 0f;
        String pictrue_sql = SqlLoader.getSqlById("strategy_contrast_for_pictrue_sql");
        String getName_sql = SqlLoader.getSqlById("queryStrategyName");
        if(idArray==null){
            return null;
        }

        for (int i=0;i<idArray.length;i++){  //  根据策略ID查询 单位时间内的收益率
            String strategy = "";
            //取当前策略数据库最大（最近）时间
            //maxDate  = getlatelyDate(idArray[i]);
            //取89天以前的时间
            minDate = getTime(maxDate,5,-89);
             //取最近三个月的收益率
            List<StrategyDailyYieldDto> yield_list = QicDbUtil.queryQicDBBeanList(pictrue_sql, StrategyDailyYieldDto.class, idArray[i],minDate);
            StrategyContrastDto strategyContrast = QicDbUtil.queryQicDBSingleBean(getName_sql,StrategyContrastDto.class,idArray[i]);
            if(strategyContrast!=null){
                sname = strategyContrast.name;
            }
            Iterator<StrategyDailyYieldDto> it =  yield_list.iterator();
             while (it.hasNext()){
                 StrategyDailyYieldDto sdy = it.next();
                 String s =  combinationData(sdy) ;
                 strategy +=(s+"\n");
             }
            //剔除字符串中最后一个“,”
             int flag = strategy.lastIndexOf(",");
            if(flag == -1){//数据库没有数据 给一个初始化值 页面图形就能正确展示
                strategy = "{name:'"+sname+"', data:[[Date.UTC(2012,01,01),1.00]]}";
            }else{
                //数据拼接完成
                strategy = "{name:'"+sname+"', data:["+strategy.substring(0,flag)+"]}";
            }
            strategys += (strategy+",\n");
           float Yield ;
            //取当前策略绝对值最大的yield
            Yield =  getmaxModYield(idArray[i],minDate);
            //取所有对比的策略中最大的  yield
            if (maxYield<Yield){
                maxYield = Yield  ;
            }

        }
        //剔除字符串中最后一个“,”
        int flag1 = strategys.lastIndexOf(",");
        if(flag1==-1){
            return arr_strategys;
        }
        //数据拼接完成
        strategys = "["+strategys.substring(0,flag1)+"]";


        //取最大时间
        String str_maxDate ="";
        if(maxDate!=null){
            str_maxDate = getFormatMaxorMinDate(maxDate);
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
            str_minDate = getFormatMaxorMinDate(d_minDate);

        }

         String str_maxYield = String.format("%.2f",maxYield*100);
        //（最大收益率/3）来表示图中Y轴坐标间隔 保留小数点后两位 无论小数点后第三位是否大于5都入位
        String str_averYield = new BigDecimal((maxYield/3)*100).setScale(2, BigDecimal.ROUND_CEILING).toString();
        //最大时间，最小时间，最大收益率，组装的series，收益率均值
        arr_strategys = new String[]{str_maxDate,str_minDate,str_maxYield,strategys,str_averYield};

        if (Logger.isDebugEnabled()) {
            Logger.debug("strategys============"+strategys);
            Logger.debug("maxYield============"+maxYield);
            Logger.debug("str_maxDate============"+str_maxDate);
            Logger.debug("str_minDate============"+str_minDate);
            Logger.debug("str_averYield====================================="+str_averYield);
        }
        return  arr_strategys;
     }


    /*
    *java中对日期的加减操作
    *gc.add(1,-1)表示年份减一.
    *gc.add(2,-1)表示月份减一.
    *gc.add(3.-1)表示周减一.
    *gc.add(5,-1)表示天减一.
    *GregorianCalendar类的add(int field,int amount)方法表示年月日加减.
    *field参数表示年,月.日等.
    *amount参数表示要加减的数量.
    *
    */
    public static String  getTime(Date date ,int d ,int t){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gc =new GregorianCalendar();
        gc.setTime(date);
        gc.add(d,t);
        gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
         return  sdf.format(gc.getTime());

    }

    /**
     * 根据指定格式获取时间 (用户格式化 绘图数据)
     * @param     date 给定时间
     * @return     指定格式的时间
     */
    public static  String[] getFormatDate(Date date){
        String[] formatDate;
        if(date==null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =  sdf.format(date);
        // Logger.debug("year=========="+year);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
        String month =sdf1.format(date);
        int m=0;
        try {
            m = Integer.parseInt(month);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //在highcharts中 0表示1月  需要把实际月份-1 才能在绘图工具中正确显示
        m = m-1;
        //Logger.debug("M=========="+m);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        String day =sdf2.format(date);
        formatDate=new String[]{year,m+"",day};
        return   formatDate;
    }

    //格式化最大或者最小时间 maxDate minDate
    public static  String getFormatMaxorMinDate(Date maxDate){
        String[] formatDate = getFormatDate(maxDate)  ;
        if(formatDate.length!=3){
            return null;
        }
        String year =   formatDate[0];
        String month =  formatDate[1];
        String day =   formatDate[2];
        String s = "Date.UTC("+year+","+month+","+day+")";
        return s;
    }

    //取该策略最近三个月 绝对值最大的收益率
    public static float getmaxModYield(String strategyId, String minDate){
        String max_and_min_yield_sql = SqlLoader.getSqlById("max_and_min_yield_sql");
        Map<String, Object> max_and_min_yield_map = QicDbUtil.queryQicDBSingleMap(max_and_min_yield_sql, strategyId,minDate);
        float minYield =  max_and_min_yield_map.get("minYield") == null ? 0 : ((BigDecimal) max_and_min_yield_map.get("minYield")).floatValue();
        float maxYield = max_and_min_yield_map.get("maxYield") == null ? 0 : ((BigDecimal) max_and_min_yield_map.get("maxYield")).floatValue();
        if(Math.abs(minYield)<Math.abs(maxYield)){
            return maxYield;
        }
        return Math.abs(minYield);
    }

    //组合数据
    public static  String combinationData(StrategyDailyYieldDto sdy){
        //得到已经格式化的时间数组
        if(sdy==null||sdy.updateDate==null){
            return "";
        }
        String[] formatDate = getFormatDate(sdy.updateDate);
        if(formatDate==null||formatDate.length!=3){
            return null;
        }
        String year =   formatDate[0];
        String month =  formatDate[1];
        String day =   formatDate[2];
       // Logger.debug("day=========="+day);

        //拼接，sdy.yield（收益率）*100 取小数点后两位四舍五入
        String s = "[Date.UTC("+year+","+month+","+day+"),"+ String.format("%.2f",sdy.yield*100)+"],";
       // Logger.debug("s=========="+s);
        return s;
    }


   /* //取当前策略数据库最大（最近）时间
    public static  Date getlatelyDate(String strategyId){
        String minDate=null;
        String get_maxDate_sql = SqlLoader.getSqlById("max_updatedate_sql");
        Map<String, Object> max_date_map = QicDbUtil.queryQicDBSingleMap(get_maxDate_sql, strategyId);
        Date maxDate =  (Date)max_date_map.get("maxDate");
        //  Logger.debug("maxDate=========="+maxDate);

        return maxDate;
    }*/

}
