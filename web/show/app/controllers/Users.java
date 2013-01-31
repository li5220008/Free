package controllers;

import bussiness.UsersService;
import dto.UserRegisterDto;
import models.common.SaleDepartment;
import models.common.UserInfo;
import play.data.validation.Valid;
import play.libs.F;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户操作, 把用户登陆的放在这里进行操作
 * User: wenzhihong
 * Date: 12-11-11
 * Time: 下午2:04
 */
public class Users extends Controller {
    public static void gotoLogin() {
        render();
    }

    public static void login(String name, String pwd) {
        checkAuthenticity();
        F.T2<Long, String> t2 = UserInfo.auth(name, pwd);
        if (t2._1 == null) {
            params.flash("name");
            validation.addError("aa", t2._2);
            renderTemplate("@gotoLogin");
        }else{
            //成功
            session.put(AuthorBaseIntercept.USER_ID_SESSION_KEY, t2._1); //写入session数据
            renderTemplate("@list");
        }
    }

    public static void list() {
        render();
    }

    public static void logout() {
    }

    public static void register(@Valid UserRegisterDto userRegisterDto){
        List<SaleDepartment> saleDepartments = SaleDepartment.findAll();
        if(userRegisterDto == null){
            render(saleDepartments);
        }

        try {
            Map<String,Object> json = new HashMap();
            if(UsersService.addUser(userRegisterDto)){
                //String s = "";
                //renderTemplate("@gotoLogin"); //qicScriptContext.CloseWindow()

                json.put("message","注册成功");
                renderJSON(json);

            }else{
                json.put("message","注册失败");
                renderJSON(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证email
     * @param email
     */
    public static void findUserByEmail(String email){
        if(  UsersService.findUserByEmail(email)==null)    {
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
        if(  UsersService.findUserByAccount(account)==null)    {
            renderText(true);
        }
        else
            renderText(false);
    }

}
