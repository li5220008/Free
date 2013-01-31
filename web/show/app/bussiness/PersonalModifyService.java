package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.UserInfoDto;
import play.libs.Crypto;

/**
 * Created with IntelliJ IDEA.
 * User: panzhiwei
 * Date: 12-12-31
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
public class PersonalModifyService {
    public static void updateUserInfo(UserInfoDto userInfoDto,Long uid) {
        String sql = SqlLoader.getSqlById("updateUserInfoById");
        if(userInfoDto.rePassword.equals("")){
            QicDbUtil.updateQicDB(sql,userInfoDto.name,userInfoDto.email,userInfoDto.phone,userInfoDto.postCode,userInfoDto.address,Crypto.passwordHash(userInfoDto.password),uid);
        }
        else{
            QicDbUtil.updateQicDB(sql,userInfoDto.name,userInfoDto.email,userInfoDto.phone,userInfoDto.postCode,userInfoDto.address,Crypto.passwordHash(userInfoDto.rePassword),uid);
        }

    }

    public static boolean findPwdById(String password, Long uid) {
        String sql = SqlLoader.getSqlById("findUserInfoDtoById");
        UserInfoDto userInfoDto = QicDbUtil.queryQicDBSingleBean(sql, UserInfoDto.class, uid);
        String oldPwd = userInfoDto.password;
        if ( Crypto.passwordHash(password).trim().equals(oldPwd.trim())) {
            return true;
        } else {
            return false;
        }

    }
}
