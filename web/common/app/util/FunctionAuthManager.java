package util;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-24
 * Time: 下午4:27
 * 功能描述:
 */
public class FunctionAuthManager {
    public static  boolean auth(long  fid,long uid){
        return SimpleFunctionAuth.getInstance().hasPrivilege(fid,uid);
    }
}
