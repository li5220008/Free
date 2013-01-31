package job;

import ChartDirector.*;
import dbutils.QicDbUtil;
import dbutils.QicoreDbUtil;
import dbutils.SqlLoader;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.AbstractKeyedHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import play.Play;
import play.exceptions.DatabaseException;
import play.jobs.Job;
import play.jobs.On;
import play.templates.JavaExtensions;
import util.CommonUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 每日收益率数据计算及策略收益率小图生生成
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 下午5:29
 */
@On("0 20 0 * * ?")
public class StrategyYieldSmallPicJob extends Job {
    @Override
    public void doJob() throws Exception {
        //calcYieldData();
        calcYieldDataAllCategory();
        generateYieldSmallPic();
    }

    //生成指定策略的数据跟画图
    public static void calcYieldDataAndDrawPic(List<String> drawStrategyList){
        calcYieldDataAllWithUUIDs(drawStrategyList);
        generateStrategysYieldSmallPic(drawStrategyList);
    }

    /**
     *从资金变化表计算收益率 (所有的策略)
     */
    private static void calcYieldDataAllCategory(){
        //策略初始资金
        String initCapitalStrategySql = SqlLoader.getSqlById("hasInitCapitalStrategy");
        //策略id做为key值, 初始资金为value
        Map<String, Double> initCapitalMap = QicoreDbUtil.queryQicoreDbWithHandler(initCapitalStrategySql, new AbstractKeyedHandler<String, Double>() {
            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("sid");
            }

            @Override
            protected Double createRow(ResultSet rs) throws SQLException {
                return rs.getBigDecimal("initCapital").doubleValue();
            }
        });

        //得到每个策略的收益率最大日期
        String maxDateSql = SqlLoader.getSqlById("max_yield_date");
        Map<String, String> maxDateMap = QicDbUtil.queryQicDbWithHandler(maxDateSql, new AbstractKeyedHandler<String, String>() {
            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("strategyID");
            }

            @Override
            protected String createRow(ResultSet rs) throws SQLException {
                return rs.getString("maxDate");
            }
        });

        calcYieldDataFromCapitalChangeWithUUID(initCapitalMap, maxDateMap);
    }

    //根据指定的uuid进行计算
    private static void calcYieldDataAllWithUUIDs(List<String> uuidList){
        StringBuilder ids = new StringBuilder();
        for (String s : uuidList) {
            ids.append(',').append("'" + s + "'");
        }
        String idList = null;
        if(ids.length() > 0){
            idList = ids.substring(1);
        }else{
            idList = "'hoho-no-exist'"; //不存在啊, 所以设置一个这个的值. 哈哈
        }

        //策略初始资金
        String initCapitalStrategySql = SqlLoader.getSqlById("hasInitCapitalStrategyWithUUIDs").replaceAll("#idList#", idList);
        //策略id做为key值, 初始资金为value
        Map<String, Double> initCapitalMap = QicoreDbUtil.queryQicoreDbWithHandler(initCapitalStrategySql, new AbstractKeyedHandler<String, Double>() {
            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("sid");
            }

            @Override
            protected Double createRow(ResultSet rs) throws SQLException {
                return rs.getBigDecimal("initCapital").doubleValue();
            }
        });


        //得到每个策略的收益率最大日期
        String maxDateSql = SqlLoader.getSqlById("max_yield_date_WithUUIDs").replaceAll("#idList#", idList);
        Map<String, String> maxDateMap = QicDbUtil.queryQicDbWithHandler(maxDateSql, new AbstractKeyedHandler<String, String>() {
            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("strategyID");
            }

            @Override
            protected String createRow(ResultSet rs) throws SQLException {
                return rs.getString("maxDate");
            }
        });

        calcYieldDataFromCapitalChangeWithUUID(initCapitalMap, maxDateMap);
    }


    private static void calcYieldDataFromCapitalChangeWithUUID(Map<String, Double> initCapitalMap, Map<String, String> maxDateMap) {
        final String before3MonthDate = DateFormatUtils.format(DateUtils.addMonths(new Date(), -3), "yyyy-MM-dd"); //三个月前
        String maxDateStr = before3MonthDate;

        //插入数据的sql
        String insertSql = SqlLoader.getSqlById("insert_strategy_daily_yield");
        //计算收益率的sql模板
        String yieldCalcSqlTmp = SqlLoader.getSqlById("yieldCalcFromCapital");
        for (String sid : initCapitalMap.keySet()) {
            final String fsid = sid;
            double initCapital = initCapitalMap.get(sid); //初始资金
            String initCapitalStr = JavaExtensions.format(initCapital, "#.000000");
            String strategyMaxDate = maxDateMap.get(sid);
            if (strategyMaxDate == null) { //没有的话, 取三个月前的日期
                maxDateStr = before3MonthDate;
            }else{
                maxDateStr = DateFormatUtils.format(DateUtils.addDays(CommonUtils.parseDate(strategyMaxDate), 1), "yyyy-MM-dd"); //最大日期 + 1
            }

            String sql =  yieldCalcSqlTmp.replaceAll("#initCapital#", initCapitalStr);
            Object[][] dailyFieldRows = QicoreDbUtil.queryQicoreDbWithHandler(sql, new ResultSetHandler<Object[][]>() {

                @Override
                public Object[][] handle(ResultSet rs) throws SQLException {
                    List<Object[]> strategyDailyFiledList = new ArrayList<Object[]>(100);
                    while (rs.next()) {
                        Object[] item = new Object[3];
                        item[0] = fsid;
                        item[1] = rs.getFloat("yield");
                        item[2] = rs.getString("transactDate");
                        strategyDailyFiledList.add(item);
                    }
                    return strategyDailyFiledList.toArray(new Object[strategyDailyFiledList.size()][]);
                }
            }, sid, maxDateStr);

            try {
                QicDbUtil.qicQueryRunner.batch(QicDbUtil.getQicDBConnection(), insertSql, dailyFieldRows);
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }


    private void calcYieldData() {
        String sql = SqlLoader.getSqlById("need_calc_yield_strategy_uuid");
        //得到所有要计算收益率的策略id
        List<String> strategyUUIDList = QicoreDbUtil.queryQicoreDbWithHandler(sql, new ColumnListHandler<String>());

        if(strategyUUIDList.size() > 0){
            //得到每个策略的收益率最大日期
            String maxDateSql = SqlLoader.getSqlById("max_yield_date");
            Map<String, String> maxDateMap = QicDbUtil.queryQicDbWithHandler(maxDateSql, new AbstractKeyedHandler<String, String>() {
                @Override
                protected String createKey(ResultSet rs) throws SQLException {
                    return rs.getString("strategyID");
                }

                @Override
                protected String createRow(ResultSet rs) throws SQLException {
                    return rs.getString("maxDate");
                }
            });

            final String before3MonthDate = DateFormatUtils.format(DateUtils.addMonths(new Date(), -3), "yyyy-MM-dd"); //三个月前
            String maxDateStr = before3MonthDate;
            String insertSql = SqlLoader.getSqlById("insert_strategy_daily_yield");
            for (String uuid : strategyUUIDList) {
                final String stUUID = uuid;
                String strategyMaxDate = maxDateMap.get(uuid);
                if (strategyMaxDate == null) { //没有的话, 取三个月前的日期
                    maxDateStr = before3MonthDate;
                }else{
                    maxDateStr = DateFormatUtils.format(DateUtils.addDays(CommonUtils.parseDate(strategyMaxDate), 1), "yyyy-MM-dd"); //最大日期 + 1
                }

                String hightYieldList = SqlLoader.getSqlById("strategy_yield_high_list");
                Object[][] dailyFieldRows = QicoreDbUtil.queryQicoreDbWithHandler(hightYieldList, new ResultSetHandler<Object[][]>() {
                    String date = "";
                    String oldDate = "";

                    @Override
                    public Object[][] handle(ResultSet rs) throws SQLException {
                        List<Object[]> strategyDailyFiledList = new ArrayList<Object[]>(100);
                        while (rs.next()) {
                            date = rs.getString("updateDate");
                            if (date.equals(oldDate) == false) { //不相等, 说明是新的
                                Object[] item = new Object[3];
                                item[0] = stUUID;
                                item[1] = rs.getFloat("yield");
                                item[2] = date;
                                strategyDailyFiledList.add(item);
                            }

                            oldDate = date;
                        }

                        return strategyDailyFiledList.toArray(new Object[strategyDailyFiledList.size()][]);
                    }
                }, uuid, maxDateStr);

                try {
                    QicDbUtil.qicQueryRunner.batch(QicDbUtil.getQicDBConnection(), insertSql, dailyFieldRows);
                } catch (SQLException e) {
                    throw new DatabaseException(e.getMessage(), e);
                }
            }

        }
    }


    private static void generateYieldSmallPic() {
        //要画图的策略Id
        String needDrawStrategySql = SqlLoader.getSqlById("need_draw_pic_strategy");
        List<String> drawStrategyList = QicDbUtil.queryQicDbWithHandler(needDrawStrategySql, new ColumnListHandler<String>("strategyID"));
        generateStrategysYieldSmallPic(drawStrategyList);
    }

    /**
     * 画策略的绩效图
     * @param drawStrategyList  策略id List
     */
    private static void generateStrategysYieldSmallPic(List<String> drawStrategyList) {
        String strategyInitCapitalSql = SqlLoader.getSqlById("strategy_initCapital");
        //初始资金map. 策略id做为key, 资金值做为value
        Map<String, Double> strategyInitCapitalMap = QicoreDbUtil.queryQicoreDbWithHandler(strategyInitCapitalSql, new AbstractKeyedHandler<String, Double>() {

            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("strategyID");
            }

            @Override
            protected Double createRow(ResultSet rs) throws SQLException {
                return rs.getDouble("initCapital");
            }
        });


        String yieldDataSql = SqlLoader.getSqlById("strategy_daily_yied_3month");
        for (String sid : drawStrategyList) {
            String stUUID = sid; //策略id

            if (!strategyInitCapitalMap.containsKey(sid)) { //不存在初始资金,则跳过
                continue;
            }

            //取数据
            final List<Date> dateList = new ArrayList<Date>(100);
            final List<Double> yieldList = new ArrayList<Double>(100);
            final AtomicInteger firstIndex = new AtomicInteger(0);
            QicDbUtil.queryQicDbWithHandler(yieldDataSql, new ResultSetHandler<Object>() {
                @Override
                public Object handle(ResultSet rs) throws SQLException {
                    Date sdate = null;
                    int index = 0;
                    boolean isFind = false;
                    double preValue = 0;
                    while (rs.next()) {
                        Date ud = rs.getDate("updateDate");
                        double yieldV = rs.getDouble("yield");
                        if (index == 0) {
                            sdate = ud; //起始日期
                            dateList.add(ud);
                            yieldList.add(yieldV);
                        } else {
                            int compareResult = DateUtils.truncatedCompareTo(sdate, ud, Calendar.DATE);
                            while (compareResult <= 0) {
                                if (DateUtils.toCalendar(sdate).get(Calendar.DAY_OF_MONTH) == 1 && (isFind == false)) { //第一个月的第一天
                                    firstIndex.set(index);
                                    isFind = true;
                                }
                                if (compareResult == 0) {
                                    dateList.add(ud);
                                    yieldList.add(yieldV); //同一天的话,把值加上
                                } else {
                                    dateList.add(sdate);
                                    yieldList.add(preValue);
                                }
                                index++;
                                sdate = DateUtils.addDays(sdate, 1); //增加一天
                                compareResult = DateUtils.truncatedCompareTo(sdate, ud, Calendar.DATE);
                            }

                        }
                        preValue = yieldV; //前值
                    }
                    return null;
                }
            }, stUUID);

            int picWith = 281; //图片宽度
            int picHeiht = 207; //图片高度
            int picGridColor = 0x9D9D9D; //风格线的颜色
            int picColor = 0xffffff; //整张图片的背景色
            int plotLeft = 45; //开始画图的左点位置
            int plotRight = 18; //图到右边的距离
            int plotTop = 10;  //开始画图的高的位置
            int plotBottom = 19; //图到下边的距离
            int plotHeight = picHeiht - plotTop - plotBottom; //画图的高度
            int plotColor = 0xF9FBFC; //画图区域的背景色
            int lineColor = 0x002a80c9; //曲线颜色
            int borderColor = 0x002a80c9; //边框颜色
            int gradientStart =  0x608ac6e9;//渐变色开始
            int gradientEnd =  0x60e3f3fa;//渐变色开始
            int yAxisHalfGap = 3; //y轴上一半几等分
            String yAxisFormat = "##0.00"; //y轴的格式化
            float fontSize = 7.5f;
            String fontName = "宋体"; //字体名字, 为空的话, 是Arial字体

            if (yieldList.size() == 0) { //没有数据,画空图
                //XYChart c = new XYChart(picWith, picHeiht, Chart.brushedMetalColor(picColor, 45));
                XYChart c = new XYChart(picWith, picHeiht, Chart.brushedMetalColor(picColor, 1, 90), borderColor);
                TTFText testTTF = c.getDrawArea().text3("0000", fontName, fontSize);
                plotLeft = testTTF.getWidth() + 5; //计算出最宽的
                int plotWith = picWith - plotLeft - plotRight; //画图的宽度
                plotTop = testTTF.getHeight();
                plotHeight = picHeiht - plotTop - plotBottom; //画图的高度
                //c.setPlotArea(plotLeft, plotTop, plotWith, plotHeight, plotColor, -1, -1, c.dashLineColor(picGridColor), c.dashLineColor(picGridColor));
                c.setPlotArea(plotLeft, plotTop, plotWith, plotHeight, plotColor, -1, -1, c.dashLineColor(picGridColor), Chart.Transparent);
                String fileName = Play.applicationPath + "/public/smallImages/st_" + stUUID + ".png";
                c.makeChart(fileName);

                continue;
            }

            //画图
            //y轴
            //取出收益率的绝对值最大
            double[] yiedlArr = new double[yieldList.size()];
            double[] yieldAbsArr = new double[yieldList.size()];
            int count = 0;
            for (Double f : yieldList) {
                float fv = f != null ? f.floatValue() * 100 : 0;
                yiedlArr[count] = fv;
                yieldAbsArr[count] = Math.abs(fv);
                count++;
            }
            float yieldAbsMax = CommonUtils.minMax(yieldAbsArr)._2.floatValue();
            yieldAbsMax = yieldAbsMax * 1.1f;

            //格式化y轴值
            float avg = yieldAbsMax / yAxisHalfGap;
            double[] ylabelVal = new double[yAxisHalfGap * 2 + 1];
            //处理0以下的值
            for (int i = 0; i < yAxisHalfGap; i++) {
                ylabelVal[i] = -yieldAbsMax + i * avg;
            }
            ylabelVal[yAxisHalfGap] = 0;
            //处理0上的值
            for (int i = 0; i < yAxisHalfGap; i++) {
                ylabelVal[yAxisHalfGap + 1 + i] = yieldAbsMax - (yAxisHalfGap - 1 - i) * avg;
            }
            String[] ylabelStr = new String[ylabelVal.length];
            DecimalFormat df = new DecimalFormat(yAxisFormat);
            for (int i = 0; i < ylabelVal.length; i++) {
                ylabelStr[i] = df.format(ylabelVal[i]);
            }

            //以下for循环是为了修复在chartDirector中, 要有第一个 - (减号) 不显示的问题. 好像是被忽略了. 加多一个即可显示
            for (int i = 0; i < yAxisHalfGap; i++) {
                ylabelStr[i] = "-" + ylabelStr[i];
            }

            XYChart c = new XYChart(picWith, picHeiht, Chart.brushedMetalColor(picColor, 1, 90), borderColor);

            TTFText testTTF = c.getDrawArea().text3(ylabelStr[0], fontName, fontSize);
            plotLeft = testTTF.getWidth() + 5; //计算出最宽的
            int plotWith = picWith - plotLeft - plotRight; //画图的宽度
            plotTop = testTTF.getHeight();
            plotHeight = picHeiht - plotTop - plotBottom; //画图的高度

            //c.setPlotArea(plotLeft, plotTop, plotWith, plotHeight, plotColor, -1, -1, c.dashLineColor(picGridColor), c.dashLineColor(picGridColor));
            c.setPlotArea(plotLeft, plotTop, plotWith, plotHeight, plotColor, -1, -1, c.dashLineColor(picGridColor), Chart.Transparent);
            c.yAxis().setLabelStyle(fontName, fontSize); //y轴使用7.5大小的Arial字体.
            c.yAxis().setLinearScale(-yieldAbsMax, yieldAbsMax, ylabelStr);

            //x轴
            /*
            String[] xdateArr = new String[dateList.size()];
            for (int i = 0; i < dateList.size(); i++) {
                xdateArr[i] = DateFormatUtils.format(dateList.get(i), "yyyy-MM");
            }
            c.xAxis().setLabels(xdateArr);
            c.xAxis().setLabelStep(31, 0, firstIndex.get());
            */

            //另外一种算法
            Set<String> xdateSet = new LinkedHashSet<String>();
            for (Date d : dateList) {
                xdateSet.add(DateFormatUtils.format(d, "yyyy-MM"));
            }
            String[] xdateArr = xdateSet.toArray(new String[xdateSet.size()]);
            c.xAxis().setLinearScale(0, yieldAbsArr.length - 1, xdateArr); //设置x轴

            c.xAxis().setLabelStyle(fontName, fontSize);
            //c.xAxis().setMargin(8, 8);

            //LineLayer lineLayer = c.addLineLayer(yiedlArr, lineColor);
            //lineLayer.setGapColor(Chart.SameAsMainColor); //无值的颜色.这里用平滑过渡色. 也就是跟主颜色一样.

            //这个是线图加上区间颜色的
            SplineLayer splineLayer = c.addSplineLayer(yiedlArr, lineColor);
            splineLayer.setGapColor(Chart.SameAsMainColor);
            Mark mark = c.yAxis().addMark(ylabelVal[0], -1, "");
            mark.setLineWidth(0);
            mark.setMarkColor(Chart.Transparent, Chart.Transparent);
            c.addInterLineLayer(splineLayer.getLine(), mark.getLine(), c.linearGradientColor(0, plotTop, 0, picHeiht - plotBottom, gradientStart, gradientEnd), Chart.Transparent);


            //以下是面积图的画法
            /*
            AreaLayer areaLayer = c.addAreaLayer(yiedlArr, c.linearGradientColor(0, plotTop, 0, picHeiht - plotBottom, gradientStart, gradientEnd));
            areaLayer.setBorderColor(lineColor);
            areaLayer.setGapColor(Chart.SameAsMainColor);
            areaLayer.setBaseLine(ylabelVal[0]);
            */


            String fileName = Play.applicationPath + "/public/smallImages/st_" + stUUID + ".png";
            c.makeChart(fileName);
        }
    }

}
