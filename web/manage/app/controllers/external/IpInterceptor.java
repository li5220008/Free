package controllers.external;

import bussiness.BackTestService;
import dto.BackTestServerDto;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Util;
import util.SystemResponseMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-24
 * Time: 上午8:53
 * 功能描述: ip 分配和过虑
 */
public class IpInterceptor extends Controller {
    private static ThreadLocal<Long>  current = new ThreadLocal<Long>();
    @Before(priority = 1)
    public static void doFilter(){
        String ip = request.remoteAddress;
        // ip鉴权 如果ip有权限  则返回服务器id 否则返回-1 表示无权限
        long sid = BackTestService.findServerIdByAddr(ip);
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        if (sid < 0 ) {
            jsonMap.put("status", BackTestServerDto.ServerStatusEnum.DISABLED.getValue());
            jsonMap.put("message", SystemResponseMessage.SERVER_DISABLED_RSP);
           // jsonMap.put("message", "来自过滤器的响应");
            renderJSON(jsonMap);
        }else{
            current.set(sid);
        }
    }
    @Util
    public static long getServerId(){
        return current.get() == null ? -1 :  current.get();
    }
}
