package controllers;

import bussiness.PersonalModifyService;
import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.UserInfoDto;
import models.common.UserInfo;
import play.mvc.Controller;
import play.mvc.With;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息修改
 * User: panzhiwei
 * Date: 12-11-21
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@With({BasePlayControllerSupport.class})
public class PersonalModify extends BasePlayControllerSupport{
    public static void  personalmodify(Long uid){
        String sql = SqlLoader.getSqlById("findUserInfoDtoById");
        UserInfoDto userInfo = QicDbUtil.queryQicDBSingleBean(sql,UserInfoDto.class,uid);
        render(userInfo);
    }
    public static void modifySuccess(UserInfoDto userInfoDto,Long uid){
        PersonalModifyService.updateUserInfo(userInfoDto,uid);
        Map<String,Object> json = new HashMap<String, Object>();
        json.put("success",true);
        json.put("message","个人信息修改成功");
        renderJSON(json);
    }

    public static void checkPassword(String password,Long uid){
        boolean  flag = PersonalModifyService.findPwdById(password,uid);
        if(flag){
           renderText(true);
        }
        else {
            renderText(false);
        }

    }
}
