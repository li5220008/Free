package dto;

import models.common.SaleDepartment;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;

/**
 * description: 用户注册DTO
 * User: weiguili(li5220008@163.com)
 * Date: 12-12-25
 * Time: 上午10:28
 */
public class UserRegisterDto {

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

    public String pwd;

    /**
     * 重复密码
     */

    public String rePwd;

    /**
     * 联系电话
     */
    public String phone;

    /**
     * 邮箱
     */

    public String email;

    /**
     * 身份号
     */

    public String idcard;

    /**
     * 营业部门
      */

    public int saleDep;

    /**
     * 资金账号
     */

    public String capitalAccount;

    /**
     * 地址
     */

    public String address;

    /**
     * 邮编
     */

    public String postCode;

    /**
     * 所属营业部门
     */
    public SaleDepartment saleDepartment;

    /**
     * 状态
     */
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
