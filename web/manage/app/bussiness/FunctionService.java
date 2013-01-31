package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.FunctionInfoDto;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-5
 * Time: 上午9:39
 * 功能描述:  获取系统树
 *
 */
public class FunctionService {
     private static FunctionInfoDto root = null;
     private static List<FunctionInfoDto> list = null;
     public static int TREE_ROOT_ID = 1;
     static {
          reload();
     }

    /**
     * 得到系统的权限菜单
     * @return @Deprecated
     */
    public static FunctionInfoDto getSystemFunctionTree(){
        return root;
    }
    public static List<FunctionInfoDto> getAllSystemFunctions(){
        reload();//暂时先每次都加载....
        return list;
    }
    private static void buildTree(){

        findSubFunctionInfoByPid(root);

    }
    @Deprecated //没用了 ligerUI Tree实现了通过pid构造数的方法，服务器就再构造树的数据结构了 2012-12-10（刘建力）
    private static void findSubFunctionInfoByPid(FunctionInfoDto root){
        String sql = SqlLoader.getSqlById("findSubFunctionInfoByPid");
        List<FunctionInfoDto> functionInfoDtoList = QicDbUtil.queryQicDBBeanList(sql, FunctionInfoDto.class, root.id);
        if(null != functionInfoDtoList || functionInfoDtoList.size()> 0){
                 root.subs = functionInfoDtoList;
        }else{
            return;
        }
        for(FunctionInfoDto functionInfoDto : functionInfoDtoList){
            //递归查找子节点
            findSubFunctionInfoByPid(functionInfoDto);
        }

    }
    private static void findAll(){
        String sql = SqlLoader.getSqlById("findAll");
        list = QicDbUtil.queryQicDBBeanList(sql,FunctionInfoDto.class);
}
    public static  List<FunctionInfoDto>  reload(){
        findAll();
        return list;
    }

}
