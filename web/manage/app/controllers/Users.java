package controllers;

import models.common.UserInfo;
import play.libs.F;
import play.mvc.Controller;

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

}
