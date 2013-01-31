package dto;

import java.util.Date;

/**
 * 股票池基本信息
 * User: liangbing
 * Date: 12-11-19
 * Time: 上午11:34
 * To change this template use File | Settings | File Templates.
 */
public class StockpoolDto {

    public String stockPoolCode;
    public String id;//股票池ID
    public String poolName;//组合名称
    public String source;//来源
    public float annualizedYield;//年化收益率
    public float yearJensenRatio;//夏普比率
    public double starLevel;//评级
    public Date updateDate;//更新日期
    public int stockNum;//组合股票数
    public int collectCount;//收藏人气
    public String orgId;//机构id
}
