package controllers;

import bussiness.LookPwdService;
import models.common.UserInfo;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 找回密码功能
 * User: liangbing
 * Date: 12-12-26
 * Time: 上午9:52
 * 用户可以通过  用户名,帐号,邮箱 找回密码
 */
public class LookPwd extends Controller {

    public static void list() {
        render();
    }

    public static void lookPwd(UserInfo ui) {
        UserInfo userInfo = UserInfo.find("byNameAndAccountAndEmail", ui.name, ui.account, ui.email).first();
        String newPwd = String.valueOf(new Double(Math.random() * 1000000).intValue());
        Map<String, Object> json = new HashMap<String, Object>();
        if (userInfo != null) {
            userInfo.setPwd(newPwd);
            userInfo.save();
            LookPwdService.sendMsg(userInfo,newPwd); //发邮箱方法, email 和 新密码
            json.put("msg", "密码已通过邮箱发送给你,请查收!");
            json.put("flag", true);
        } else {
            json.put("msg", "用户名或帐号或邮箱不正确,请重试!");
            json.put("flag", false);
        }
        renderJSON(json);

}
}
