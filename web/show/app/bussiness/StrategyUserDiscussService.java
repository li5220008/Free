package bussiness;

import models.common.StrategyBaseinfo;
import models.common.UserInfo;
import models.common.UserStrategyDiscuss;

import java.util.List;

/**
 * 策略评论方法
 * User: liangbing
 * Date: 12-11-10
 * Time: 上午11:41
 * To change this template use File | Settings | File Templates.
 */
public class StrategyUserDiscussService {
    /**
     * 查询该策略所有的评论
     * @param id
     * @return
     */
    public static List<UserStrategyDiscuss> userDiscussList(Long id){
        List<UserStrategyDiscuss> usdList = UserStrategyDiscuss.find("byStidOrderByDisTimeDesc",id).fetch(10);
        return usdList;
    }

    /**
     * 保存该用户对该策略的评论
     * @param usd
     * @param uid
     * @param stid
     */
    public static void saveDiscuss(UserStrategyDiscuss usd,Long uid,Long stid){
        if(uid!=null&&stid!=null){
            UserInfo u=UserInfo.findById(uid);

            StrategyBaseinfo sb = StrategyBaseinfo.findById(stid);

            sb.discussCount++;
            sb.discussTotal+=usd.star;
            sb.save();
            usd.user=u;
            usd.strategy=sb;
            usd.save();
        }

    }

    /**
     * 判断该用户是否已经评论的该策略
     * @param stid
     * @param uid
     */
    public static Integer judge(Long stid,Long uid){
        int result=0;
        List<UserStrategyDiscuss> usdList = UserStrategyDiscuss.find("byStidAndUid",stid,uid).fetch(1);
        if(usdList!=null&&usdList.size()>0){
            result=1;
        }
        return result;
    }

}
