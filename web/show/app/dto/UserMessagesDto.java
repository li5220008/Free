package dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panzhiwei
 * Date: 12-12-25
 * Time: 上午11:41
 * To change this template use File | Settings | File Templates.
 */
public class UserMessagesDto {
    public Long id;
    //用户id
    @SerializedName("uid")
    public Long uid;
    //用户通知
    @SerializedName("msg")
    public String msg;
    //通知时间
    @SerializedName("msgTime")
    public Date msgTime;
    //状态 已读/未读
    @SerializedName("status")
    public int status;
    //标题
    @SerializedName("title")
    public String title;


    public enum MessagesStatus{
        UNREAD(1),
        READ(2);

        MessagesStatus(int value){
            this.value = value;
        }
        public int value;
    }

}
