package dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 策略信息Dto
 * User: liangbing
 * Date: 12-12-10
 * Time: 下午2:21
 * 页面展示数据
 */
public class StrategyDto extends BaseDtoSupport {

    //策略id
    public String strategyId;
    //状态
    @SerializedName("status")
    public int status;

    //策略名称
    @SerializedName("sname")
    public String sname;

    //策略提供者
    @SerializedName("provider")
    public String provider;

    //交易品种
    @SerializedName("tradeVariety")
    public Integer tradeVariety;

    //策略类型
    @SerializedName("stype")
    public Integer stype;

    //净利润
    @SerializedName("retainedProfits")
    public float retainedProfits;

    //收益率
    @SerializedName("yield")
    public float yield;

    //年化收益率
    @SerializedName("yieldOfYear")
    public float yieldOfYear;

    //月化收益率标准差
    @SerializedName("yomsd")
    public float yomsd;

    //总盈利
    @SerializedName("overallProfitability")
    public float overallProfitability;

    //总亏损
    @SerializedName("overallDeficit")
    public float overallDeficit;

    //月化收益率
    @SerializedName("yieldOfMonth")
    public float yieldOfMonth;

    //夏普指数
    @SerializedName("sharpeIndex")
    public float sharpeIndex;

    //交易次数
    @SerializedName("tradeCount")
    public int tradeCount;

    //盈利次数
    @SerializedName("profitCount")
    public int profitCount;

    //收藏次数
    @SerializedName("collectCount")
    public int collectCount;

    //当前订阅总数
    @SerializedName("subscriber")
    public int subscriber;

    //订阅最迟到期时间
    @SerializedName("endDate")
    public Date endDate;

    //总订阅数
    @SerializedName("allSubscriber")
    public int allSubscriber;
    //策略提供者信息简介
    public String providerDesp;
    //策略简介
    public String desp;
    //下架时间
    public Date downTime;
    //上传时间
    public Date uploadTime;
    //回测开始时间
    public String lookbackStime;
    //回测结束时间
    public String lookbackEtime;


    public int deficitCount;//亏损次数
    public float maxSingleProfit;//单次最大盈利
    public float maxSingleDeficit;//单次最大亏损
    public int tradeDays;//总交易天数
    public int maxShortPositionTime;//最大空仓时间
    public int longPositionTradeCount;//多头交易次数
    public int shortPositionTradeCount;//空头交易次数
    public int positionCloseCount;//持平次数
    public int maxSequentialProfitCount;//最大连续盈利次数
    public int maxSequentialDeficitCount;//最大连续亏损次数
    public float profitRatio;//盈利比率(胜率)
    public float canhsiedRatio;//盈亏比
    public float maxSingleProfitRatio;//最大盈利/总盈利
    public float maxSingleDeficitRatio;//最大亏损/总亏损
    public float maxSequentialDeficitCapital;//最大连续亏损额
    public float sumOfCommission;//手续费合计
    public float grossProfit;//毛利润
    public float profitLossRatio;//净利润/单次最大亏损
    public float avgProfitOfMonth;//月度平均盈利
    public float floatingProfitAndLoss;//浮动盈亏
    public float movingCost;//滑动成本
    public float lastSequentialDeficitCapital;//不知道什么意思,先加上
    public float lastSequentialProfitCount;//不知道什么意思,先加上
    public float lastSequentialDeficitCount;//不知道什么意思,先加上
    public float yieldOfMonthStandardDeviation;//..一个很复杂的计算
    public float totalAsset;//不知道什么意思,先加上


    /**
     * 交易类型
     */
    public enum TradeType {
        STOCK_CHOICE(1),//择股型
        TIME_CHOICE(2),//择时型
        TRADE_CHOICE(3),//交易型
        OTHER(4);//其它

        TradeType(int value) {
            this.value = value;
        }

        public int value;
    }

    /**
     * 交易品种
     */
    public enum TradeVariety {
        STOCK(1),
        FUTURES(2),
        MIXED(3);

        TradeVariety(int value) {
            this.value = value;
        }

        public int value;
    }

    //策略状态: 1.待审核(也就是上传完成), 2. 沙箱测试  3. 回测中  4. 上架  5 下架  6. 已回测 7 8回测失败 待下架-100审核未通过
    public enum StrategyStatus {
        CHECKING(1),
        SANDBOXTESTING(2),
        BACKTESTING(3),
        UPSHELF(4),
        DOWNSHELF(5),
        FINISHTEST(6),
        WAITINGUPSHELF(7),
        BACKTESTINGFAILER(8),
        DELETED(-100);


        StrategyStatus(int value) {
            this.value = value;
        }

        public int value;
    }
}
