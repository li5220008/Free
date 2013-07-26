package util;

import models.common.FunctionInfo;
import models.common.UserInfo;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-24
 * Time: 下午4:26
 * 功能描述:
 */
public class SimpleFunctionAuth implements FunctionAuth {
    private static SimpleFunctionAuth self = new SimpleFunctionAuth();

    public static FunctionAuth getInstance() {
        return self;
    }

    private SimpleFunctionAuth() {

    }

    @Override
    public boolean hasPrivilege(long preId,long uid) {
        //查出用户所有的权限
        List<FunctionInfo> fids = UserInfo.getUserFunctionIds(uid);
        for(FunctionInfo fif :fids){
            //当参数的权限id等于当前菜单的id 或当前
            if(preId == fif.id || preId == fif.fpid){
                return true;
            }
        }
        return false;

    }
}
