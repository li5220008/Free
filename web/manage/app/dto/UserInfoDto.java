package dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 上午11:28
 * 功能描述: 用于修改用户信息时展示
 */
public class UserInfoDto  extends BaseDtoSupport{
    /**
     * 姓名
     */
    public String name;
    /**
     * 账号
     */
    public String account;
    /**
     * 密码
     */
    public String password;
    /**
     * 联系电话
     */
    public String phone;
    /**
     * 电子邮件
     */
    public String email;
    /**
     * 身份证号码
     */
    public String idCard;
    /**
     * 所属营业部
     */
    public String saleDept;
    /**
     * 资金账号
     */
    public String capitalAccount;
    /**
     * 联系地址
     */
    public String address;
    /**
     * 邮编
     */
    public String postCode;
    /**
     * 所有菜单
     */
    @SerializedName(value = "treeData")
    public FunctionInfoDto functionInfoDto;
    /**
     * 重复密码
     */
    public String rePassword;
    /**
     * 起始时间
     */
    public Date sDate;
    /**
     * 结束时间
     */
    public Date eDate;
    /**
     * 状态
     */
    public UserStatus status;

    public enum UserStatus{
      WITHOUTACTIVITY(2),//未激活
      ACTIVIY(10),//正常
      DISABLED(1),//禁用
      DELETED(-100);//软删除的
      UserStatus(int value){
        this.value = value;
      }
      public int value;
   }

}