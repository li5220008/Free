package controllers;

import play.mvc.Controller;
import utils.QicConfigProperties;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-31
 * Time: 下午4:01
 * 功能描述:  更新一些缓存数据
 */
public class Caches extends Controller {
    //更新系统配置
    public static void relodConfig(){
            QicConfigProperties.getInstance().load();
            renderText("success");
    }
}
