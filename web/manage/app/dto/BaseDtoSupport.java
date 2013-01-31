package dto;

import com.google.gson.annotations.SerializedName;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 下午2:06
 * 功能描述:
 */
public abstract class BaseDtoSupport {
    @SerializedName("id")
    public long id;
}
