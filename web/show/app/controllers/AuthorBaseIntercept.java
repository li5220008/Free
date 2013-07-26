package controllers;

import play.mvc.Before;
import play.mvc.Controller;

/**
 * 基本 拦截器, 提供一些公用的功能.
 * before, after, finally, catch 的 priority 0 -- 29 做为系统保留使用
 * 用这个拦截器控制所有要登陆的功能的使用. 在controller方法里只要定义 Long uid 这个一个参数, 框架就会自动把当前用户的id给这个变量.
 * User: wenzhihong
 * Date: 12-11-11
 * Time: 下午3:40
 */
public class AuthorBaseIntercept extends Controller {
    public static final String USER_ID_SESSION_KEY = "uid";

    public static final String NO_PERMISSION_RESOURCE = "不是你的资源,不能操作";

    /**
     * 获取用户id
     */
    @Before(priority = 1)
    static void fetchUserId() {
        String uidFromSession = session.get(USER_ID_SESSION_KEY);
        if(uidFromSession == null){
            forbidden("没有登陆,不能使用");
        }
        String uid = params.get("uid");
        if (uid == null) {
            params.put("uid", uidFromSession);
        }
    }
}
