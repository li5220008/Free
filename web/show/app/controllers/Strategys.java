package controllers;

import annotation.QicFunction;
import bussiness.StrategyContrastService;
import bussiness.StrategyService;
import bussiness.UserStrategyCollectService;
import dto.StrategyBaseDto;
import dto.StrategyContrastDto;
import dto.StrategySearchCnd;
import models.common.UserTemplate;
import play.libs.F;
import util.Page;

import java.util.List;
import java.util.Set;

/**
 * 策略展示与对比
 * User: liangbing
 * Date: 12-11-8
 * Time: 上午10:32
 * To change this template use File | Settings | File Templates.
 */
public class Strategys extends BasePlayControllerSupport {

    /**
     * 策略展示
     */
    @QicFunction(id=3,dependencies = {4,5})
    public static void strategyList(int myselect, String content, int pageNo, Long uid) {
        F.T2<List<StrategyBaseDto>, Page> t2 = StrategyService.strategyList(myselect,content, pageNo);
        List<StrategyBaseDto> sbdList = t2._1;
        Page page = t2._2;

        Set<Integer> collectSet = UserStrategyCollectService.queryUserCollectSet(sbdList, uid);

        //用户的自定义搜索条件
        List<UserTemplate> utList = UserTemplate.fetchUserSearchCond(uid, 1);

        render(sbdList, myselect, content, collectSet, utList, page);
    }

    /**
     *
     * 策略对比
     * @Auther liuhongjiang
     * @param idArray 策略ID
     */
    @QicFunction(id=3)
    public static  void  strategyContrast(String idArray[]){
        List<StrategyContrastDto> list = StrategyContrastService.strategyContrast(idArray);
        String[] dates = StrategyContrastService.strategyContrastForPictrue(idArray);
        if(dates[3]==null){
            dates[3]="[]";
        }
        render(list, dates);
    }

    /**
     * 高级搜索
     * @param pageNo 第几页. 从1开始
     */
    @QicFunction(id=3)
    public static void advanceSearch(StrategySearchCnd cnd,int myselect,int pageNo, Long uid) {

        F.T2<List<StrategyBaseDto>, Page> t2 = StrategyService.advanceSearch(cnd,myselect,pageNo);
        List<StrategyBaseDto> sbdList = t2._1;
        Page page = t2._2;

        Set<Integer> collectSet = UserStrategyCollectService.queryUserCollectSet(sbdList, uid);

        //用户的自定义搜索条件
        List<UserTemplate> utList = UserTemplate.fetchUserSearchCond(uid, 1);

        boolean advanceSearch = true;

        render("@strategyList" ,sbdList, pageNo, collectSet, utList, page, advanceSearch, cnd,myselect);
    }

}
