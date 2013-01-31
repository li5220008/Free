package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.UserRegisterDto;
import play.libs.Crypto;
import utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 用户操作服务
 * User: weiguili(li5220008@163.com)
 * Date: 12-12-25
 * Time: 下午5:22
 */
public class UsersService {

    /**
     * 添加用户
     * @param userRegisterDto
     * @return 是否成功
     * @throws Exception
     */
    public static boolean addUser(UserRegisterDto userRegisterDto) throws Exception{
        StringBuffer sql = new StringBuffer(SqlLoader.getSqlById("registerAddUser"));
        List<Object> sqlParams = new ArrayList<Object>();
        sqlParams.add(userRegisterDto.name);
        sqlParams.add(userRegisterDto.account);
        sqlParams.add(Crypto.passwordHash(userRegisterDto.pwd)); //加密
        sqlParams.add(userRegisterDto.phone);
        sqlParams.add(userRegisterDto.email);
        sqlParams.add(userRegisterDto.idcard);
        sqlParams.add(userRegisterDto.saleDep);
        sqlParams.add(userRegisterDto.address);
        sqlParams.add(userRegisterDto.postCode);
        sqlParams.add(userRegisterDto.capitalAccount);
        sqlParams.add(Constants.USER_SDATE);
        sqlParams.add(Constants.USER_EDATE);
        sqlParams.add(UserRegisterDto.UserStatus.WITHOUTACTIVITY.value);
        return QicDbUtil.updateQicDB(sql.toString(), sqlParams.toArray())>0;
    }

    /**
     * 根据账号查找用户(账号在系统中是唯一的)
     * @param account
     * @return
     */
    public static UserRegisterDto findUserByAccount(String account){

        String sql = SqlLoader.getSqlById("findUserByAccount");
        UserRegisterDto userRegisterDto = QicDbUtil.queryQicDBSingleBean(sql,UserRegisterDto.class,account);
        return userRegisterDto;
    }

    /**
     * 根据email查找用户(email在系统中是唯一的)
     * @param email
     * @return
     */
    public static UserRegisterDto findUserByEmail(String email){

        String sql = SqlLoader.getSqlById("findUserByEmail");
        UserRegisterDto userRegisterDto = QicDbUtil.queryQicDBSingleBean(sql,UserRegisterDto.class,email);
        return userRegisterDto;
    }
}
