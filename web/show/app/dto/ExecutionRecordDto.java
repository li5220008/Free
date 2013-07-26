package dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 创建成交记录dto
 * User: panzhiwei
 * Date: 12-11-10
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionRecordDto {
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
    //最新成交量
    @SerializedName("lastpx")
    public Float lastpx;
    //成交价格
    @SerializedName("avgpx")
    public Float avgpx;
    //交易所
    @SerializedName("securityexchange")
    public String securityexchange;
    //累计成交量
    public Float cumqty;
    //资金账号
    public String account;
}
