package controllers;

import annotation.QicFunction;
import bussiness.StrategyService;
import dto.StrategyBaseDto;
import models.common.UserInfo;
import play.Logger;
import play.libs.F;
import util.Page;
import utils.SystemResponseMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-18
 * Time: 上午11:09
 * 功能描述: 用户策略管理相关类
 */

public class UserStrategyManage extends BasePlayControllerSupport {
    /**
     * 我上传的策略列表
     */
    @QicFunction(id=7)
     public static void myStrategyList(Map<String,String> map){
         if(map == null){
             map = new HashMap<String,String>();
         }

         map.put("uid",params.get("uid"));//得到当前用户的uid
         map.put("orderType",params.get("orderType")==null?"0":params.get("orderType"));
        map.put("orderCol",params.get("orderCol")==null?"1":params.get("orderCol"));
         map.put("pageNo",params.get("pageNo")==null?"1":params.get("pageNo"));
         map.put("status",params.get("status")==null?"-1":params.get("status"));
         map.put("keyword",params.get("keyword")==null?"":params.get("keyword"));

         F.T2<List<StrategyBaseDto>, Page> list = StrategyService.findStrategysByUser(map);
         List<StrategyBaseDto> strategyBaseDtos = list._1;
         Page page = list._2;
         map.remove("pageNo");
         UserInfo curLoginUser = UserInfo.findById(params.get("uid",Long.class));
         render(strategyBaseDtos,page,map,curLoginUser);
     }

    /**
     * 上传完策略文件后-----添加策略
     *
     * @param strategyDto
     * @Author 刘建力
     */
    @QicFunction(id=7)
    public static void addStrategy(StrategyBaseDto strategyDto) {
        //先拷备文件到正式目录,再添加

        String filePath = params.get("files");
        try {
            long uid = params.get("uid",Long.class);
            setSuccessFlag(StrategyService.insertStrategy(strategyDto, filePath.split(","),uid));
            setMessage(SystemResponseMessage.STRATEGY_UPLOAD_SUCCESS_RSP);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            setSuccessFlag(false);
        }
        renderJSON(getSampleResponseMap());

    }
    @QicFunction(id=7)
    public static void findStrategyByName(String sname) {
        if (StrategyService.findStrategyByName(sname) != null) {
            renderText(false);
        } else {
            renderText(true);
        }
    }



}
