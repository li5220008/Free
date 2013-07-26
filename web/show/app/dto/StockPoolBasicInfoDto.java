package dto;

import java.math.BigDecimal;

/**
 * 股票池基本信息
 * User: liuhongjiang
 * Date: 12-11-19
 * Time: 下午2:02
 */
public class StockPoolBasicInfoDto {
    //股票池编号
    public Long stockPoolCode;
    //股票池名称
    public String stockPoolName;
   //组合股票数
    public Long stockNum;
    //更新频率
    public String updateFrequency;
    //股票策略（组合说明）
    public String strategy;
    //组合收益
    public BigDecimal annualizedYield;
    //附件下载
    public String filestoragePath;
    //研报摘要
    public String summary;
    //组合来源
    public String institutionName;
    //组合评级
    public BigDecimal starNum;

}
