package controllers;

import annotation.QicFunction;
import bussiness.StrategyContrastService;
import bussiness.StrategySubscriptionService;
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
 * description: 我的订阅 策略阅展示
 * User: weiguili(li5220008@163.com)
 * Date: 12-12-18
 * Time: 上午10:58
 */
public class StrategySubscription extends BasePlayControllerSupport {

    /**
     * 订阅策略列表
     * @param myselect 排序
     * @param content 关键字
     * @param pageNo  页码
     * @param uid 用户id
     */
    @QicFunction(id=2)
    public static void strategyList(int myselect, String content, int pageNo, Long uid) {
        if(pageNo<1){
            pageNo = 1;
        }
        F.T2<List<StrategyBaseDto>, Page> t2 = StrategySubscriptionService.strategyList(myselect, content, pageNo, uid);
        List<StrategyBaseDto> sbdList = t2._1;
        Page page = t2._2;

        Set<Integer> collectSet = UserStrategyCollectService.queryUserCollectSet(sbdList, uid);

        //用户的自定义搜索条件
        List<UserTemplate> utList = UserTemplate.fetchUserSearchCond(uid, 1);

        render(sbdList, myselect, content, collectSet, utList, page);
    }

    /**
     * 策略对比
     * @param idArray
     */
    @QicFunction(id=2)
    public static  void  strategyContrast(String idArray[]){
        List<StrategyContrastDto> list = StrategyContrastService.strategyContrast(idArray);
        String[] dates = StrategyContrastService.strategyContrastForPictrue(idArray);
        if(dates[3]==null){
            dates[3]="[]";
        }
        render(list,dates);
    }

    /**
     * 高级搜索
     * @param pageNo 第几页. 从1开始
     */
    @QicFunction(id=2)
    public static void advanceSearch(StrategySearchCnd cnd, int pageNo, Long uid) {
        if(pageNo <= 0){
            pageNo = 1;
        }
        F.T2<List<StrategyBaseDto>, Page> t2 = StrategySubscriptionService.advanceSearch(cnd, pageNo,uid);
        List<StrategyBaseDto> sbdList = t2._1;
        Page page = t2._2;

        Set<Integer> collectSet = UserStrategyCollectService.queryUserCollectSet(sbdList, uid);

        //用户的自定义搜索条件
        List<UserTemplate> utList = UserTemplate.fetchUserSearchCond(uid, 1);

        boolean advanceSearch = true;

        render("@strategyList" ,sbdList, pageNo, collectSet, utList, page, advanceSearch, cnd);
    }
}
