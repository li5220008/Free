package controllers;

import annotation.QicFunction;
import bussiness.UserStrategyCollectService;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略收藏
 * User: panzhiwei
 * Date: 12-11-7
 * Time: 下午3:24
 * To change this template use File | Settings | File Templates.
 */

public class UserStrategyCollects extends BasePlayControllerSupport {
    //加入收藏
    @QicFunction(id=5)
   public static void addstrategycollect(long sid,Long uid){
       try{
           UserStrategyCollectService.addstrategycollect(sid,uid);
           Map<String,Object> json = new HashMap<String, Object>();
           json.put("isSuccess",true);
           json.put("message","策略收藏成功。");
           renderJSON(json);
       } catch (Exception e){
           Map<String,Object> json2 = new HashMap<String, Object>();
           json2.put("isSuccess",false);
           json2.put("message",e.getMessage());
           renderJSON(json2);
           //renderJSON(String.format("{\"isSuccess\": false, \"message\": \"%s\"}", e.getMessage()));
       }
   }

   //取消收藏
   @QicFunction(id=5)
   public static void deletestrategycollect(long sid,Long uid){
       //查询策略收藏列表
       try{
           UserStrategyCollectService.deletestrategycollect(sid,uid);
           Map<String,Object> json = new HashMap<String, Object>();
           json.put("isSuccess",true);
           json.put("message","取消收藏成功。");
           renderJSON(json);
       } catch (Exception e){
           Map<String,Object> json2 = new HashMap<String, Object>();
           json2.put("isSuccess",false);
           json2.put("message",e.getMessage());
           renderJSON(json2);
           //renderJSON(String.format("{\"isSuccess\": false, \"message\": \"%s\"}", e.getMessage()));
       }
   }

}

