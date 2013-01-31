package dto;

/**
 * 策略搜索条件dto
 * User: wenzhihong
 * Date: 12-11-11
 * Time: 上午9:59
 */
public class StrategySearchCnd {
    //交易类型
    public Integer tradeType;

    //交易品种
    public Integer tradeVariety;

    //收益率 小值
    public Float yieldDown;

    //收益率 大值
    public Float yieldUp;

    //获胜率 小值
    public Float profitRatioDown;

    //获胜率 大值
    public Float profitRatioUp;

    //用户评级 小值
    public Integer starDown;

    //用户评级 大值
    public Integer starUp;
}
