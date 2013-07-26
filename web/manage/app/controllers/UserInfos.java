package controllers;

import annotation.QicFunction;
import bussiness.LogInfosService;
import bussiness.SaleDepartmentService;
import bussiness.UserInfoService;
import dto.RoleInfoDto;
import dto.SaleDepartMentDto;
import dto.UserInfoDto;
import util.SystemLoggerMessage;
import util.SystemResponseMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 下午1:21
 * 功能描述:  用户信息控制器
 */
public class UserInfos  extends BasePlayControllerSupport {
    /**
     * 查询用户信息
     * @param id
     */
    @QicFunction(id=21)
    public  static  void  show(long id){

        UserInfoDto udto = UserInfoService.findUserInfoById(id);
        List<SaleDepartMentDto> saleDepartments  = SaleDepartmentService.findAll();
        List<RoleInfoDto> roleInfoDtos = UserInfoService.findUserRoleInfoById(id);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userInfo",udto);
        map.put("saleDepartmentsInfo",saleDepartments);
        map.put("roleInfo",roleInfoDtos);
        map.put("functionInfo",UserInfoService.findUserFunctionInfoById(id));
        renderJSON(map);
    }

    /**
     * 修改用户信息
     * @param userInfoDto
     */
    @QicFunction(id=21)
    public static void update(UserInfoDto userInfoDto,long uid){
       boolean isSuccess =  UserInfoService.updateUserInfo(userInfoDto);
        //后期类似的成功返回json串,放到公共文件
        setMessage(SystemResponseMessage.UPDATE_USER_SUCESS_RSP);
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_UPDATE_USERINFO, SystemLoggerMessage.UPDATE_USERINFO, SystemLoggerMessage.TYPE);//写入操作日志
        renderJSON(getSampleResponseMap());
    }

    /**
     * 新建用户
     * @param userInfo
     */
    @QicFunction(id=21)
    public static void adduser(UserInfoDto userInfo,Long uid) {
        String depId = params.get("depId", String.class);
        userInfo.saleDept = depId;
        List<Long> keys = UserInfoService.addUser(userInfo);
        setExtraData("uids", keys);
        setMessage(SystemResponseMessage.ADD_USER_SUCCESS_RSP);
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_ADD_USER,SystemLoggerMessage.ADD_USER,SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
        //render(idlist);
    }

    /**
     * 删除用户
     * @param ids
     */
    @QicFunction(id=21)
    public static void deluser(String[] ids,Long uid){
        UserInfoService.delUser(ids);
        setMessage(SystemResponseMessage.DEL_USER_SUCCESS_RSP);
        LogInfosService.writeSystemLog(uid,SystemLoggerMessage.DO_DELETE_USER,SystemLoggerMessage.DELETE_USER,SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }

    /**
     * 用户状态修改
     * @param ids
     * @param status
     */
    @QicFunction(id=21)
    public static void userStateModify(String[] ids,int status,Long uid){
        UserInfoService.userStateModify(ids,status);
        setMessage(SystemResponseMessage.MODIFY_USER_RSP);
        LogInfosService.writeSystemLog(uid,SystemLoggerMessage.DO_UPDATE_USERSTATUS,SystemLoggerMessage.UPDATE_USERSTATUS,SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }

    /**
     * 验证email
     * @param email
     */
    public static void findUserByEmail(String email){
        if(  UserInfoService.findUserByEmail(email)==null)    {
            renderText(true);
        }
        else
            renderText(false);
    }

    /**
     * 验证账户
     * @param account
     */
    public static void findUserByAccount(String account){
        if(  UserInfoService.findUserByAccount(account)==null)    {
            renderText(true);
        }
        else
            renderText(false);
    }

    /**
     * 到期用户延期
     * @param ids
     * @param delaydate
     */
    @QicFunction(id=21)
    public static void userDelay(String[] ids,String delaydate,long uid){
        UserInfoService.userdelay(ids,delaydate);
        setMessage(SystemResponseMessage.USER_DELAY);
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_USER_DELAY, SystemLoggerMessage.USER_DELAY, SystemLoggerMessage.TYPE);//写入操作日志
        renderJSON(getSampleResponseMap());

    }

    /**
     * 验证email
     * @param email
     */
    public static void findUserByEmailExcludeSelf(String email){
        String[] emails = email.split(",");
        if(  UserInfoService.findUserByEmailExcludeSelf(emails[0],emails[1])==null)    {
            renderText(true);
        }
        else
            renderText(false);
    }

    /**
     * 验证账户
     * @param account
     */
    public static void findUserByAccountExcludeSelf(String account){
        String[] accounts = account.split(",");
        if(  UserInfoService.findUserByAccountExcludeSelf(accounts[0],accounts[1])==null)    {
            renderText(true);
        }
        else
            renderText(false);
    }

    public static void newUser(){
        List<SaleDepartMentDto> saleDepartments = SaleDepartmentService.findAll();
        render(saleDepartments);
    }



}
