package controllers;

import com.google.gson.Gson;
import models.common.SearchCnd;
import models.common.UserInfo;
import models.common.UserTemplate;
import play.mvc.Controller;

import java.util.Date;

/**
 * 用户自定义模板
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 下午1:48
 */
public class UserTemplates extends Controller {
    public static void add(UserTemplate template) {
        //TODO 加入逻辑判断
        template.save();
    }

    public static void show() {
        UserTemplate ut = UserTemplate.findById(1l);
        renderJSON(ut);
    }

    public static void add2() {
        SearchCnd cnd = new SearchCnd();
        cnd.sdate = "2012-12-12";
        cnd.edate = "2012-12-31";

        UserInfo u = new UserInfo();
        u.name = "wenzhi";
        u.account = "wenh";
        u.sdate = new Date();
        u.edate = new Date();
        u.email = "wen@126.com";
        u.status = 1;
        u.save();

        UserTemplate ut = new UserTemplate();
        ut.user = u;
        ut.name = "我的模板1";
        ut.content = new Gson().toJson(cnd);
        ut.type = 1;
        ut.save();
    }
}
