package controllers;

import annotation.QicFunction;
import bussiness.StrategyContrastService;
import bussiness.StrategyFavoriteService;
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
 * 我的收藏 策略展示
 * User: liangbing   StrategyBaseInfoSql
 * Date: 12-11-12
 * Time: 下午1:24
 */
public class StrategyFavorite extends BasePlayControllerSupport {


    /**
     * 策略展示
     */
    @QicFunction(id=5)
    public static void strategyList(int myselect, String content, int pageNo, Long uid) {
        if(pageNo <= 0){
            pageNo = 1;
        }
        F.T2<List<StrategyBaseDto>, Page> t2 = StrategyFavoriteService.strategyList(myselect, content, pageNo,uid);
        List<StrategyBaseDto> sbdList = t2._1;
        Page page = t2._2;

        Set<Integer> collectSet = UserStrategyCollectService.queryUserCollectSet(sbdList, uid);

        //用户的自定义搜索条件
        List<UserTemplate> utList = UserTemplate.fetchUserSearchCond(uid, 1);

        render(sbdList, myselect, content, collectSet, utList, page);
    }

    /**
     * 策略对比
     * @Auther 刘泓江
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
    @QicFunction(id=5)
    public static void advanceSearch(StrategySearchCnd cnd,int myselect, int pageNo, Long uid) {
        F.T2<List<StrategyBaseDto>, Page> t2 = StrategyFavoriteService.advanceSearch(cnd,myselect,pageNo,uid);
        List<StrategyBaseDto> sbdList = t2._1;
        Page page = t2._2;

        Set<Integer> collectSet = UserStrategyCollectService.queryUserCollectSet(sbdList, uid);

        //用户的自定义搜索条件
        List<UserTemplate> utList = UserTemplate.fetchUserSearchCond(uid, 1);

        boolean advanceSearch = true;

        render("@strategyList" ,sbdList, pageNo, collectSet, utList, page, advanceSearch,myselect,cnd);
    }
}
