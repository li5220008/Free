package dto;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 下午1:52
 * 功能描述: 角色信息
 */
public class RoleInfoDto extends BaseDtoSupport{

    public String name;
    public String desp;


    public List<FunctionInfoDto> functions;
}
