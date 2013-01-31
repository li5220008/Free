package controllers;

import annotation.QicFunction;
import bussiness.*;
import dto.ActivateUserDto;
import dto.AvtivatePar;
import dto.SaleDepartMentDto;
import models.common.RoleInfo;
import play.data.binding.As;
import play.libs.F;
import util.Constants;
import util.Page;
import util.SystemLoggerMessage;

import java.util.*;

/**
 * 用户管理控制类.
 * User: liangbing
 * Date: 12-12-4
 * Time: 上午10:54
 */
public class ActivateUser extends BasePlayControllerSupport {

    /**
     * 待激活用户列表
     * @param ap     参数对象类
     * @param pageNo 页码
     */
    @QicFunction(id=21)
    public static void list(AvtivatePar ap, int pageNo) {

        if (ap == null) {
            ap = new AvtivatePar();
        }
        if (ap.saleId == null) {
            ap.saleId = 0l;//初始化营业部Id ="";
        }
        ap.flag = 1;
        //_1.待激活用户列表 _2 Page 对象
        F.T2<List<ActivateUserDto>, Page> t2 = ActivateUserService.userList(ap, pageNo);
        List<ActivateUserDto> audList = t2._1;
        Page page = t2._2;
        //得到所有营业部门
        List<SaleDepartMentDto> saleDepartments = SaleDepartmentService.findAll();
        List<RoleInfo> rollList = UserAuthorizationService.findAllRole();
       //String hostName = getHostName();
        String hostName = SystemConfigService.get(Constants.USER_EXCEL_TEMPLATE_KEY);
        render(audList, ap, saleDepartments, page,rollList,hostName);
    }

    /**
     * 用户列表
     *
     * @param ap
     * @param pageNo
     */
    @QicFunction(id=21)
    public static void users(AvtivatePar ap, int pageNo) {
        if (ap == null) {
            ap = new AvtivatePar();
        }
        if (ap.saleId == null) {
            ap.saleId = 0l;//初始化营业部Id =0l;
        }
        if(ap.roleId ==null){
            ap.roleId = 0l;//初始化角色Id =0l;
        }
        ap.flag = 2;
        //_1.用户列表 _2 Page 对象
        F.T2<List<ActivateUserDto>, Page> t2 = ActivateUserService.users(ap, pageNo);
        List<ActivateUserDto> usersList = t2._1;
        Page page = t2._2;
        //得到所有营业部门
        List<SaleDepartMentDto> saleDepartments = SaleDepartmentService.findAll();
        //得到所有角色信息
        List<RoleInfo> roleInfoList = UserAuthorizationService.findAllRole();

        render("@list", usersList, page, saleDepartments, ap, roleInfoList);
    }


    /**
     * 到期用户
     * @param ap
     * @param pageNo
     */
    @QicFunction(id=21)
    public static void dueUsers(AvtivatePar ap, int pageNo) {
        if (ap == null) {
            ap = new AvtivatePar();
        }
        if (ap.saleId == null) {
            ap.saleId = 0l;//初始化营业部Id ="";
        }
        if(ap.roleId ==null){
            ap.roleId = 0l;//初始化营业部Id ="";
        }
        ap.flag = 3;
        //_1.用户列表 _2 Page 对象
        F.T2<List<ActivateUserDto>, Page> t2 = ActivateUserService.dueUsers(ap, pageNo);
        List<ActivateUserDto> dueUsersList = t2._1;
        Page page = t2._2;
        //得到所有营业部门
        List<SaleDepartMentDto> saleDepartments = SaleDepartmentService.findAll();
        //得到所有角色信息
        List<RoleInfo> roleInfoList = UserAuthorizationService.findAllRole();

        render("@list", dueUsersList, page, saleDepartments, ap, roleInfoList);
    }

    /**
     * 用户授权
     * @param uids
     * @param rid
     * @param edate
     */
    @QicFunction(id=21)
    public static void insertRoleForUser( @As(",") int[] uids ,@As(",") int[] rid,Date edate){
        long uid = params.get("uid",Long.class);
        Map<String, Object> json = new HashMap<String, Object>();
        Boolean flag =  UserAuthorizationService.insertUserRole(uids, rid,edate);
        json.put("returnBoolean", flag);
        if(flag){
            json.put("msg", "授权成功");
         LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_USER_ROLE, SystemLoggerMessage.USER_ROLE, SystemLoggerMessage.TYPE);//写入系统日志
        }else{
            json.put("msg", "授权失败");
        }
        renderJSON(json);

    }

    /**
     * 根据用户组ID 查询用户 姓名 账号 所属营业部名称信息
     * @param idArray
     * @param startId
     * @param endId
     */
    @QicFunction(id=21)
    public static void userAuthorization(@As(",") int[] idArray, int startId, int endId) {
        List<ActivateUserDto> userList;
        if(idArray != null && idArray.length > 0) {

            userList = UserAuthorizationService.getUserList(idArray);
        } else if(startId > 0 && ((endId > 0 && endId > startId) || endId == 0)) {
            userList = UserAuthorizationService.getUserList(startId, endId);
        } else {
            userList = new ArrayList<ActivateUserDto>();
        }
        List<RoleInfo> roleList = UserAuthorizationService.findAllRole();
        render(userList, roleList);
    }
}
