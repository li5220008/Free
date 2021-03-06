package controllers;

import play.mvc.*;
import util.SystemResponseMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-6
 * Time: 下午3:33
 * 功能描述: Controller基类,放一些公共的方法
 */
@With({AuthorBaseIntercept.class,FunctionInterceptor.class})
public abstract   class BasePlayControllerSupport extends Controller {

    private static ThreadLocal<Map<String,Object>> current = new ThreadLocal<Map<String,Object>>();

    @Before(priority = 3)// invoke before the real controller method execute
    public  static void init(){
        //初始化
        if(current.get() == null){
            current.set(new HashMap<String,Object>());
        }else{
            current.get().clear();
            current.get().put("success",true);
            current.get().put("message", SystemResponseMessage.SYSTEM_DEFAULT_MSG_RSP);
        }
    }
    @Util
    public static void setMessage(String message) {
        put("message",message);
    }
    @Util
    public static void setSuccessFlag(boolean isSuccess){
        put("success",isSuccess);
    }
    @Util
    public static void setExtraData(String key,Object extraData){
        put(key,extraData);
    }
    @Util
    public static Map getSampleResponseMap(){
        return current.get();
    }

    @Util
    private static void put(String key, Object obj) {
        current.get().put(key, obj);
    }
    @After
    public static void doSomethingAfter(){

      //记操作日志什么的,暂时没什么用
    }
    @Util
    protected static String getHostName(){

        String hostName = request.host;
        if(!hostName.startsWith("http")){//https情况暂未考虑
            hostName = "http://" +  hostName;
        }
        return hostName;
    }
    @Util
    public static boolean auth(int id){
        return true;
    }

}
