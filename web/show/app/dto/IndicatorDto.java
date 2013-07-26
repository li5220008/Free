package dto;

import com.google.gson.annotations.SerializedName;

/**
 * 绩效指标
 * User: panzhiwei
 * Date: 12-11-13
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
public class IndicatorDto {
    public String id;
    public String uuid;
    //净利润
    @SerializedName("retainedProfits")
    public float retainedProfits;
    //收益率
    @SerializedName("yield")
    public float yield;
    //月化收益率
    @SerializedName("yieldOfMonth")
    public float yieldOfMonth;
    //月度收益标准差
    @SerializedName("yieldOfMonthStandardDeviation")
    public float yieldOfMonthStandardDeviation;
    //总盈利
    @SerializedName("overallProfitability")
    public float overallProfitability;
    //总亏损
    @SerializedName("overallDeficit")
    public float overallDeficit;
    //总交易天数
    @SerializedName("tradeDays")
    public int tradeDays;
    //最大空仓时间
    @SerializedName("mAXShortPositionTime")
    public int mAXShortPositionTime;
    //交易次数
    @SerializedName("tradeCount")
    public int tradeCount;
    //多头交易次数
    @SerializedName("longPositionTradeCount")
    public int longPositionTradeCount;
    //空头交易次数
    @SerializedName("shortPositionTradeCount")
    public int shortPositionTradeCount;
    //盈利次数
    @SerializedName("profitCount")
    public int profitCount;
    //亏损次数
    @SerializedName("deficitCount")
    public int deficitCount;
    //持平次数
    @SerializedName("positionCloseCount")
    public int positionCloseCount;
    //最大连续盈利次数
    @SerializedName("mAXSequentialProfitCount")
    public int mAXSequentialProfitCount;
    //最大连续亏损次数
    @SerializedName("mAXSequentialDeficitCount")
    public int mAXSequentialDeficitCount;
    //盈利比率
    @SerializedName("profitRatio")
    public float profitRatio;
    //总盈利/总亏损
    @SerializedName("canhsiedRatio")
    public float canhsiedRatio;
    //单词最大盈利
    @SerializedName("mAXSingleProfit")
    public float mAXSingleProfit;
    //单词最大亏损
    @SerializedName("mAXSingleDeficit")
    public float mAXSingleDeficit;
    //最大盈利/总盈利
    @SerializedName("mAXSingleProfitRatio")
    public float mAXSingleProfitRatio;
    //最大亏损/总亏损
    @SerializedName("mAXSingleDeficitRatio")
    public float mAXSingleDeficitRatio;
    //最大连续亏损额
    @SerializedName("mAXSequentialDeficitCapital")
    public float mAXSequentialDeficitCapital;
    //手续费合计
    @SerializedName("sumOfCommission")
    public float sumOfCommission;
    //毛利润
    @SerializedName("grossProfit")
    public float grossProfit;
    //净利润/单次最大亏损
    @SerializedName("profitLossRatio")
    public float profitLossRatio;
    //月度平均盈利
    @SerializedName("avgProfitOfMonth")
    public float avgProfitOfMonth;
    //年化收益率
    @SerializedName("yieldOfYear")
    public float yieldOfYear;
    //夏普指数
    @SerializedName("sharpeIndex")
    public float sharpeIndex;
    //浮动盈亏
    @SerializedName("floatingProfitAndLoss")
    public float floatingProfitAndLoss;
    //滑价成本
    @SerializedName("movingCost")
    public float movingCost;

}
