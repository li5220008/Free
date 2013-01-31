package controllers;

import bussiness.BackTestService;
import play.mvc.Controller;
import util.QicConfigProperties;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-31
 * Time: 下午4:05
 * 功能描述:
 */
public class Caches extends Controller {
    /**
     * 更新策略服务器列表
     */
    public static void reloadServers(){
        BackTestService.clearCache();
        renderText("success");
    }
    //更新系统配置 config_manage表
    public static void reloadConfig(){
        QicConfigProperties.getInstance().load();
        renderText("success");
    }
}
