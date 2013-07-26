package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.ActivateUserDto;
import dto.FunctionInfoDto;
import dto.RoleInfoDto;
import dto.UserRoleDto;
import models.common.UserInfo;
import play.libs.F;
import util.Page;
import util.SystemLoggerMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取对应角色的用户列表
 * User: liangbing
 * Date: 12-12-7
 * Time: 上午11:40
 */
public class RoleInfoService {

    /**
     * 根据roleId 获取用户信息
     * @param roleId 角色ID
     * @param pageNo 当前页
     * @return _1.用户集合 _2.page对象
     */
    public static F.T2<List<ActivateUserDto>, Page> roleList(Long roleId, int pageNo) {
        String sqlList = SqlLoader.getSqlById("roleInfoSql");
        List<ActivateUserDto> audList = null;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total = QicDbUtil.queryQicDbCount(coutSql.toString(), roleId);
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            audList = QicDbUtil.queryQicDBBeanList(sqlList, ActivateUserDto.class,roleId);
        }
        return F.T2(audList, page);
    }

    /**
     * 根据角色ID查找权限
     * @param rid
     * @return
     */
    public static  List<FunctionInfoDto> findFunctionInfoByRoleId(long rid){
        String sql = SqlLoader.getSqlById("findFunctionInfoByRoleId");
        List<FunctionInfoDto> functionInfoDtoList = QicDbUtil.queryQicDBBeanList(sql, FunctionInfoDto.class, rid);
        return functionInfoDtoList;
    }

    /**
     * 删除角色的权限
     * @param rid
     * @return true 删除成功 false 删除失败或没有需要删除的对像
     */
    public static boolean deleteFunctionInfoByRoleId(long rid){
        String sql = SqlLoader.getSqlById("deleteFunctionInfoByRoleId");
        return QicDbUtil.updateQicDB(sql,rid)>0;
    }

    /**
     * 批量添加角色权限
     * @param role 角色  带授权信息
     */
    public  static void addFunctionInfoByRoleId(RoleInfoDto role){
        if(role == null){
            return;
        }
        //删除角色的权限
        deleteFunctionInfoByRoleId(role.id);
        //重新赋权限
        if(role.functions ==  null || role.functions.size() == 0){
            //没用权限
            return ;
        }
        String sql = SqlLoader.getSqlById("addFunctionInfoByRoleId");
        int size = role.functions.size();
        Object[][] params = new Object[size][2];
        //填充二维数组
        for(int i = 0;i< size;i++){
            params[i][0] = role.id;
            params[i][1] = role.functions.get(i).id;
        }
        QicDbUtil.batchQicDB(sql,params);
    }


    /**
     * 角色基本信息修改
     * @param txtarea
     * @param id
     */

    public static void saveRoleBasicInfo(String txtarea,long id){
        String sql = SqlLoader.getSqlById("saverolebasicinfo");
        QicDbUtil.updateQicDB(sql,txtarea,id);
    }

    /**
     * 角色基本信息查询
     * @param id
     */
    public static RoleInfoDto getRoleBasicInfo(long id){
        String sql = SqlLoader.getSqlById("getrolebasicinfo");
        RoleInfoDto roleInfoDto = QicDbUtil.queryQicDBSingleBean(sql,RoleInfoDto.class,id);
        return roleInfoDto;
    }

    /**
     * 查询最近20个已授权用户信息
     * @return
     */
    public static List<UserInfo> queryLastTwentyUser(Long rid){
        String queryLastTwentyUserSql =SqlLoader.getSqlById("queryLastTwentyUser");
        return QicDbUtil.queryQicDBBeanList(queryLastTwentyUserSql,UserInfo.class,rid);
    }

    /**
     * 查询最近20个当前角色用户信息
     * @param rid 角色ID
     * @return
     */
    public static List<UserInfo> queryLastTwentyRoleUser(Long rid){
        String queryLastTwentyRoleUserSql = SqlLoader.getSqlById("queryLastTwentyRoleUser");
        return QicDbUtil.queryQicDBBeanList(queryLastTwentyRoleUserSql,UserInfo.class,rid);
    }


    /**
     * 给定账号或姓名 查询已授权用户
     * @param condition 用户名/账号
     * @return
     */
    public static List<UserInfo> queryUserByCondition(String condition){
        String queryLastTwentyRoleUserSql = SqlLoader.getSqlById("queryUserByName");
        return QicDbUtil.queryQicDBBeanList(queryLastTwentyRoleUserSql,UserInfo.class,condition,condition);
    }

    /**
     * 给定账号或姓名 查询当前角色用户
     * @param condition 用户名/账号
     * @param roleId 用户名/账号
     * @return
     */
    public static List<UserInfo> queryRoleUserByCondition(String condition,Long roleId){
        String queryLastTwentyRoleUserSql = SqlLoader.getSqlById("queryRoleUser");
        return QicDbUtil.queryQicDBBeanList(queryLastTwentyRoleUserSql,UserInfo.class,condition,condition,roleId);
    }

    /**
     * 更换用户角色
     * @param uid 用户列表ID数组
     * @param urid 角色用户列表ID数组
     * @param roleId 用户名/账号
     * @return
     */
    public static boolean changeRole(Long[] urid,Long[] uid, Long roleId,long sysUid){
        String queryUserRoleByIdSql = SqlLoader.getSqlById("queryUserRoleById");
        String insertUserRolebyIdSql = SqlLoader.getSqlById("insertUserRolebyId");
        String deleteUserRolebyIdSql = SqlLoader.getSqlById("deleteUserRolebyId");
        if(urid!=null){
            for(int i=1;i<urid.length;i++){
                if(QicDbUtil.queryQicDBBeanList(queryUserRoleByIdSql, UserRoleDto.class,urid[i],roleId).size()==0){//如果不存在就插入
                      QicDbUtil.updateQicDB(insertUserRolebyIdSql,urid[i],roleId);
                }
            }
        }
        if(uid!=null){
            for(int i=1;i<uid.length;i++){
                QicDbUtil.updateQicDB(deleteUserRolebyIdSql,uid[i],roleId);
            }
        }
        LogInfosService.writeSystemLog(sysUid, SystemLoggerMessage.DO_CHANGE_ROLE,SystemLoggerMessage.CHANGE_ROLE,SystemLoggerMessage.TYPE);//写入系统日志
        return true;
    }

    /**
     * 删除角色 以及级联关系
     * @Author liuhongjiang
     * @param id 角色id
     */
    public static void deleteRole(Long id,long uid){
        String sql = SqlLoader.getSqlById("deleteRoleName");//删除角色名
        String userRoleSql = SqlLoader.getSqlById("deleteUserRoleByRoleId");//删除用户角色
        String functionRoleSql = SqlLoader.getSqlById("deleteFunctionInfoByRoleId");//删除角色权限
        String findUserByRoleIdSql = SqlLoader.getSqlById("findUserByRoleId");
        String findRoleByUserIdSql = SqlLoader.getSqlById("findRoleByUserId");
        String updateUserStatusSql = SqlLoader.getSqlById("updateUserStatus");
            //查询拥有该角色的用户
            List<UserRoleDto> list = QicDbUtil.queryQicDBBeanList(findUserByRoleIdSql, UserRoleDto.class,id);
            //删除角色名称 删除相关级联
            QicDbUtil.updateQicDB(userRoleSql, id);
            QicDbUtil.updateQicDB(functionRoleSql, id);
            QicDbUtil.updateQicDB(sql, id);
            if(list!=null&&list.size()>0){
                int status = 2; //用户待激活状态
                for(UserRoleDto userRole :list){
                    //如果该用户处于无权限状态，修改用户状态为待激活
                    if(QicDbUtil.queryQicDBBeanList(findRoleByUserIdSql,UserRoleDto.class,userRole.uid).size()==0){
                        QicDbUtil.updateQicDB(updateUserStatusSql,status,userRole.uid);
                    }
                }
            }
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_DELETE_ROLE,SystemLoggerMessage.DELETE_ROLE,SystemLoggerMessage.TYPE);//写入系统日志

    }
}
