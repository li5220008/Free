package dto;

import play.data.binding.As;

/**
 * 股票池搜索条件dto
 * User: wenzhihong
 * Date: 12-11-11
 * Time: 下午3:53
 */
public class StockPoolSearchCnd {
    //推荐机构, 这样用逗号分割开来,就可以转成数组了
    @As(",")
    public String[] recommendOrgs;

    //研报更新周期
    public Integer reportUpdatePeriod;

    //用户评级 小值
    public Integer starDown;

    //用户评级 大值
    public Integer starUp;

    //年化收益率 小值
    public Float yearYieldDown;

    //年化收益率 大值
    public Float yearYieldUp;

    //sharp比率 小值
    public Float sharpRateDown;

    //sharp比率 大值
    public Float sharpRateUp;

}
