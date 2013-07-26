package dto;

/**
 * 查询参数对象类.
 * User: liangbing
 * Date: 12-12-6
 * Time: 上午11:02
 */
public class AvtivatePar{


    /**
     * @param name 姓名
     * @param account 帐号
     * @param saleId 营业部门ID
     * @param orderSort 按对应字段排序
     * @param orderFlag 排序标识 ep:正序 反序
     */
    public String name;
    public String account;
    public Long saleId;
    public String orderSort;
    public int orderFlag;
    //角色ID
    public Long roleId;
    //状态
    public int status;
    //表示是待激活列表,用户列表,到期用户
    public int flag;
}
