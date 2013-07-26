package dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 创建策略持仓dto
 * User: panzhiwei
 * Date: 12-11-10
 * Time: 上午11:48
 * To change this template use File | Settings | File Templates.
 */
public class StrategyPositionDto {

    //标的名称//策略id
    @SerializedName("id")
    public long id;
    //时间
    @SerializedName("updatetime")
    public Date updatetime;
    //策略名称
    @SerializedName("name")
    public String name;
    //方向
    @SerializedName("side")
    public int side;
    //交易品种
    @SerializedName("trade_variety")
    public int trade_variety;
    //标的代码
    @SerializedName("securityid")
    public String securityid;
    //标的名称
    @SerializedName("symbol")
    public String symbol;
    //交易所
    @SerializedName("securityexchange")
    public String securityexchange;
    //持仓量
    @SerializedName("positionqty")
    public int positionqty;
}
