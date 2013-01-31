package dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 创建委托记录dto
 * User: panzhiwei
 * Date: 12-11-10
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class AuthorizeRecordDto {
    //策略id
    @SerializedName("id")
    public long id;
    //时间
    @SerializedName("sendingtime")
    public Date sendingtime;
    //策略名称
    @SerializedName("name")
    public String name;
    //方向
    @SerializedName("side")
    public int side;
    //交易品种
    @SerializedName("trade_variety")
    public Integer trade_variety;
    //标的代码
    @SerializedName("securityid")
    public String securityid;
    //标的名称
    @SerializedName("symbol")
    public String symbol;
    //委托量
    @SerializedName("orderqty")
    public Float orderqty;
    //委托价格
    @SerializedName("price")
    public Float price;
    //交易所
    @SerializedName("securityexchange")
    public String securityexchange;
    //资金账号
    @SerializedName("account")
    public String account;
    //订单流水号
    @SerializedName("ordersnid")
    public String ordersnid;
}
