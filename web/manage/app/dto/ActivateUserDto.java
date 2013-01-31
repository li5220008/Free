package dto;

import java.util.Date;

/**
 * 用户信息
 * User: liangbing
 * Date: 12-12-4
 * Time: 上午10:41
 */
public class ActivateUserDto {

    public String id;//用户ID
    public String account;//帐号
    public String name;//姓名
    public String idCard;//身份证号
    public String phone;//联系电话
    public String saleName;//营业部
    public String capitalAccount;//资金帐号
    public Date applyDate;//申请日期
    public Integer status;//用户状态

    public String roleName;//所属角色名称
    public Date startDate;//授权开始日期
    public Date endDate;//到期日期

}
