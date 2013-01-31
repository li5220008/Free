package dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: liangbing
 * Date: 12-11-8
 * Time: 上午8:58
 * 策略超市dto类
 * To change this template use File | Settings | File Templates.
 */
public class StrategyBaseDto extends BaseDtoSupport{

    public String id;
    public String uuid;
    //策略名称
    @SerializedName("sname")
    public String sname;
    //策略星级
    @SerializedName("starLevel")
    public float starLevel;
    //策略类型
    @SerializedName("stype")
    public Integer stype;
    //策略提供者
    @SerializedName("provider")
    public String provider;
    //交易品种
    @SerializedName("tradeVariety")
    public Integer tradeVariety;
    //收藏数量
    @SerializedName("collectCount")
    public int collectCount = 0;
    //交易次数
    @SerializedName("tradeCount")
    public int tradeCount;
    //上架时间
    @SerializedName("upTime")
    public Date upTime;
    //下架时间
    @SerializedName("downTime")
    public Date downTime;
    //收益率
    @SerializedName("yield")
    public float yield;
    //月度收益标准差
    @SerializedName("yomsd")
    public float yomsd;
    //获胜率
    @SerializedName("profitRatio")
    public float profitRatio;
    //策略状态
    public int status;
    //总订阅人数
    public int orderCount;
    //上传时间
    public  Date uploadTime;
    //截止目前还有效的订阅数
    public int validOrderCount;
    //通过时间
    public Date passTime;
    //策略提借人员信息简介
    public String providerDesp;
    //策略简介
    public String desp;
    //回测开始时间
    public String lookbackStime;
    //回测结束时间
    public String lookbackEtime;
    public String getOrderByString(){

        return "";
    }
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

    //策略状态: 1.待审核(也就是上传完成), 2. 沙箱测试  3. 回测中  4. 上架  5 下架 6已回测 7 待下架 -100审核未通过 8 回测失败
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
