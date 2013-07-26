package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.FunctionInfoDto;
import dto.RoleInfoDto;
import dto.UserInfoDto;
import models.common.UserInfo;
import org.apache.commons.lang.time.DateUtils;
import play.Logger;
import play.db.DB;
import play.libs.Crypto;
import util.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 下午1:46
 * 功能描述: 用户信息相关业务逻辑处理
 */
public class UserInfoService {
    /**
     * 根据用户ID查询用户相关信息
     * @param uid 用户ID
     * @return 用户相关信息
     *
     */
    public static UserInfoDto findUserInfoById(long uid){

        String sql = SqlLoader.getSqlById("findUserInfoById");
        //1. 查询用户基本信息
        UserInfoDto userBaseInfo = QicDbUtil.queryQicDBSingleBean(sql,UserInfoDto.class,uid);
      //  userBaseInfo.functionInfoDto = getFuntionInfoTreeByUid(uid);
        return userBaseInfo;
    }

    /**
     * 根据用户ID查询 用户的菜单
     * @param uid
     * @return
     */
    public static List<FunctionInfoDto> findUserFunctionInfoById(long uid){


        String sql = SqlLoader.getSqlById("findUserFunctionInfoById");
        return QicDbUtil.queryQicDBBeanList(sql,FunctionInfoDto.class,uid);
    }

    /**
     * 根据用户ID 查询用户的角色信息
     * @param uid
     * @return
     */
    public  static List<RoleInfoDto> findUserRoleInfoById(long uid){
        String sql = SqlLoader.getSqlById("findUserRoleInfoById");
        return  QicDbUtil.queryQicDBBeanList(sql,RoleInfoDto.class,uid);
    }

    /**
     * 修改用户基本信息
     * @param userInfoDto
     * @return
     */
    public  static boolean updateUserInfo(UserInfoDto userInfoDto){
        //如果用户没有输入密码则查出原来的密码  后期再改成在页面保存原密码
        String sql ;
        Object[] params = null;
        if(null == userInfoDto.password || "".equals(userInfoDto.password.trim())){
            //没修改密码的情况
            sql = SqlLoader.getSqlById("updateUserInfoWithoutPwd");
              params =  new Object[]{
                    userInfoDto.name,
                    userInfoDto.account,
                    userInfoDto.phone,
                    userInfoDto.email,
                    userInfoDto.idCard,
                    userInfoDto.saleDept,
                    userInfoDto.address,
                    userInfoDto.postCode,
                    userInfoDto.capitalAccount,
                    userInfoDto.id
            };
            return QicDbUtil.updateQicDB(sql,params)>0;

        }else{//修改了密码的情况
                params =  new Object[]{
                    userInfoDto.name,
                    userInfoDto.account,
                    Crypto.passwordHash(userInfoDto.password),
                    userInfoDto.phone,
                    userInfoDto.email,
                    userInfoDto.idCard,
                    userInfoDto.saleDept,
                    userInfoDto.address,
                    userInfoDto.postCode,
                    userInfoDto.capitalAccount,
                    userInfoDto.id
            };
            sql = SqlLoader.getSqlById("updateUserInfo");
            return QicDbUtil.updateQicDB(sql,params)>0;

        }



    }

    /**
     * 得到用户的菜单
     * 算法说明:
     * 1. 从数据库中查出用户的权限列表List
     * 2. 将内存中系统菜单复制一份
     * 3. 将List和复制的系统菜单做分析对比，如找到复制的系统菜单ID在用户权限列表中不存在，则将此菜单对像从复制的系统菜单中remove
     * 4. 最终返回上面复制的树就是用户的权限树了
     * @param uid
     * @return
     * @deprecated    算法有问题 不用了  以后有时间再解决
     */
    public static FunctionInfoDto getFuntionInfoTreeByUid(long uid){

         //1. 找出用户拥有的树
        List<FunctionInfoDto> functionInfoDtoList = findUserFunctionInfoById(uid);
        FunctionInfoDto systemTree = FunctionService.getSystemFunctionTree();
        //复制系统树
        FunctionInfoDto copyOfSystemTree = FunctionInfoDto.copy(systemTree);


        filterUserTreeFromSystemTree(copyOfSystemTree.subs,functionInfoDtoList);

         return copyOfSystemTree;
    }

    /**
     * 判断当前节点是否在用户的权限列表中
     * @param list
     * @param id
     * @return
     */
    private static boolean isExist(List<FunctionInfoDto> list,long id){
        if(id == FunctionService.TREE_ROOT_ID){
            return true;
        }else{
            for(FunctionInfoDto tmp : list){
                if(tmp.id == id){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 删除不在用户权限列表中的菜单节点
     * @param subList
     * @param functionInfoDtoList
     * @deprecated  算法有问题 不用了 以后有时间再解决
     */
    private static void filterUserTreeFromSystemTree(List<FunctionInfoDto> subList,List<FunctionInfoDto> functionInfoDtoList )
    {

        for(int i = 0;subList!=null&& i<subList.size();){
            boolean  isExist = isExist(functionInfoDtoList,subList.get(i).id);
            if(!isExist){
                //找到没有权限的节点从树中移掉
                i=0;//之前算法有问题 ，去掉list中的一个元素之后要重新归零 2012-12-10
                subList.remove(i);
            }else{
                //递归判断子节点是否有权限
                filterUserTreeFromSystemTree(subList.get(i).subs, functionInfoDtoList);
                ++i;
            }
        }

    }

    /**
     * 根据账号查找用户(账号在系统中是唯一的)
     * @param account
     * @return
     */
    public static UserInfoDto findUserByAccount(String account){

        String sql = SqlLoader.getSqlById("findUserByAccount");
        UserInfoDto userInfoDto = QicDbUtil.queryQicDBSingleBean(sql,UserInfoDto.class,account);
        return userInfoDto;
    }

    /**
     * 根据email查找用户(email在系统中是唯一的)
     * @param email
     * @return
     */
    public static UserInfoDto findUserByEmail(String email){

        String sql = SqlLoader.getSqlById("findUserByEmail");
        UserInfoDto userInfoDto = QicDbUtil.queryQicDBSingleBean(sql,UserInfoDto.class,email);
        return userInfoDto;
    }


    /**
     * 新建用户
     * @param userInfo
     * @return
     */
    public static List<Long> addUser(UserInfoDto userInfo) {

        List<UserInfoDto> userInfoDtos = new ArrayList<UserInfoDto>();
        userInfoDtos.add(userInfo);
        List<Long> idlist = null;
        try {
            idlist = addUserBatch(userInfoDtos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idlist;
        //QicDbUtil.updateQicDB(addusersql, userInfo.name, userInfo.account, userInfo.password, userInfo.phone, userInfo.email, userInfo.idcard, userInfo.saleDep.id, userInfo.capitalAccount, userInfo.address, userInfo.post, sdate, edate,UserInfoDto.UserStatus.WITHOUTACTIVITY.value);
    }

    /**
     * 删除用户
     * @param ids      删除用户id数组s
     */
    public static void delUser(String[] ids) {
        String delusersql = SqlLoader.getSqlById("delUser");
        Object[][] params = new Object[ids.length][2];
        for(int i = 0; i < ids.length; i ++){
            params[i][0] = UserInfoDto.UserStatus.DELETED.value;
            params[i][1] = ids[i];
        }
        QicDbUtil.batchQicDB(delusersql,params);
    }


    /**
     * 用户状态修改
     * @param ids        用户id数组
     * @param status     修改状态
     */
    public static void userStateModify(String[] ids,int status){
        String sql = SqlLoader.getSqlById("delUser");
        Object[][] params = new Object[ids.length][2];
        for(int i = 0; i < ids.length; i ++){
            params[i][0] = status;
            params[i][1] = ids[i];
        }
        QicDbUtil.batchQicDB(sql,params);
    }


    /**
     * 批量添加用户 insert ignore into tb......
     * @param userInfos
     * @return  新增的用户数量
     */
    public static List<Long> addUserBatch(List<UserInfoDto> userInfos)throws Exception{
        if(null == userInfos || userInfos.size()==0){
            return new ArrayList<Long>(0);
        }
        String sql = SqlLoader.getSqlById("addUserBatch");
        Connection conn = DB.getDBConfig().getConnection();
        String[] columnNames= {"id"} ;
        PreparedStatement pstmt = conn.prepareStatement(sql, columnNames) ;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(date);
        for(UserInfoDto tmp :userInfos){
            pstmt.setString (1,tmp.name);
            pstmt.setString (2,tmp.account);
            pstmt.setString (3, Crypto.passwordHash(tmp.password));
            pstmt.setString (4,tmp.phone);
            pstmt.setString (5, tmp.email);
            pstmt.setString (6,tmp.idCard);
            pstmt.setString (7,tmp.saleDept);
            pstmt.setString (8,tmp.address);
            pstmt.setString (9,tmp.postCode);
            pstmt.setString (10,tmp.capitalAccount);
            pstmt.setString(11,Constants.USER_SDATE);
            pstmt.setString(12,Constants.USER_EDATE);
            pstmt.setInt(13, UserInfoDto.UserStatus.WITHOUTACTIVITY.value);
            pstmt.addBatch();
        }
        pstmt.executeBatch() ;
       // conn.commit();
        List<Long> keys = new ArrayList<Long>(userInfos.size());
        //取得自动生成的主键值的结果集
        ResultSet rs = pstmt.getGeneratedKeys() ;
        while(rs.next()){
            keys.add(rs.getLong(1) );
            Logger.info(rs.getLong(1) + "");
        }
        return keys;
    }


    /**
     * 到期用户延期
     * @param ids
     * @param delaydate
     */
    public static void userdelay(String[] ids,String delaydate){
        String sql = SqlLoader.getSqlById("delayUser");
        Object[][] params = new Object[ids.length][2];
        for (int i = 0; i < ids.length; i ++){
            params[i][0] = delaydate;
            params[i][1] = ids[i];
        }
        QicDbUtil.batchQicDB(sql,params);
    }

   public  static UserInfoDto findUserByEmailExcludeSelf(String newEmail,String selfEmail){
       String sql = SqlLoader.getSqlById("findUserByEmailExcludeSelf");
       UserInfoDto userInfoDto = QicDbUtil.queryQicDBSingleBean(sql,UserInfoDto.class,newEmail,selfEmail);
       return userInfoDto;
   }
    public  static UserInfoDto findUserByAccountExcludeSelf(String newAccount,String selfAccount){
        String sql = SqlLoader.getSqlById("findUserByAccountExcludeSelf");
        UserInfoDto userInfoDto = QicDbUtil.queryQicDBSingleBean(sql,UserInfoDto.class,newAccount,selfAccount);
        return userInfoDto;
    }







}
