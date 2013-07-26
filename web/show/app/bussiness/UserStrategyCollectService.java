package bussiness;


import dbutils.QicDbUtil;
import dto.StrategyBaseDto;
import models.common.StrategyBaseinfo;
import models.common.UserStrategyCollect;
import play.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 策略收藏
 * User: panzhiwei
 * Date: 12-11-9
 * Time: 上午9:18
 * To change this template use File | Settings | File Templates.
 */
public class UserStrategyCollectService {
    //得到已收藏的策略,放入set中
    public static Set<Integer> queryUserCollectSet(List<StrategyBaseDto> list, Long uid){
        String sql = "SELECT uid AS uid,stid AS stid FROM qic_db.user_strategy_collect WHERE uid=" + uid + " AND stid in (";
        Set<Integer> result = new HashSet<Integer>();
        if(list!=null&&list.size()>0){
            for(StrategyBaseDto item : list){
                sql += item.id+",";
            }
            sql = sql.substring(0,sql.length()-1);
            sql += ")";
            Logger.debug(sql);
            List<Map<String, Object>> queryList = QicDbUtil.queryQicDBMapList(sql);

            for(Map<String, Object> item : queryList) {
                result.add(Integer.parseInt(item.get("stid").toString()));
            }
        }

        return result;
    }
    //加入收藏
    public static void addstrategycollect(long stid,Long uid) throws Exception{
        String sql = "SELECT \n" +
                "  * \n" +
                "FROM\n" +
                "  qic_db.`user_strategy_collect` a \n" +
                "WHERE a.`stid` = ? \n" +
                "  AND a.`uid` = ?";
        UserStrategyCollect userStrategyCollect =QicDbUtil.queryQicDBSingleBean(sql,UserStrategyCollect.class,stid,uid);
        if(userStrategyCollect == null){
            userStrategyCollect = new UserStrategyCollect();
            StrategyBaseinfo strategy = StrategyBaseinfo.findById(stid);
            userStrategyCollect.strategy = strategy;
            if(userStrategyCollect.strategy == null){
                throw new Exception("不存在的策略.");
            }
            String sql2 = "INSERT INTO qic_db.`user_strategy_collect`(uid,stid) VALUES(?,?)";
            QicDbUtil.updateQicDB(sql2,uid,stid);
            String sql3 = "UPDATE qic_db.`strategy_baseinfo` a SET a.`collect_count` = a.`collect_count` + 1 WHERE a.`id` = ?";
            QicDbUtil.updateQicDB(sql3,stid);
        }
        else{
            throw  new Exception("已经收藏策略.");
        }
    }

    //取消收藏
    public static void deletestrategycollect(long stid,Long uid) throws Exception{
        //查询策略收藏列表
        String sql = "SELECT \n" +
                "  * \n" +
                "FROM\n" +
                "  qic_db.`user_strategy_collect` a \n" +
                "WHERE a.`stid` = ? \n" +
                "  AND a.`uid` = ?";
        UserStrategyCollect userStrategyCollect =QicDbUtil.queryQicDBSingleBean(sql,UserStrategyCollect.class,stid,uid);
        if(userStrategyCollect != null){
            StrategyBaseinfo strategy = StrategyBaseinfo.findById(stid);
            String sql2 = "DELETE FROM qic_db.`user_strategy_collect` WHERE qic_db.`user_strategy_collect`.`stid`=? AND qic_db.`user_strategy_collect`.`uid`=?";
            QicDbUtil.updateQicDB(sql2,stid,uid);
            String sql3 = "UPDATE qic_db.`strategy_baseinfo` a SET a.`collect_count` = a.`collect_count` - 1 WHERE a.`id` = ?";
            QicDbUtil.updateQicDB(sql3,stid);

        }

        else{
            throw new Exception("已经取消收藏.");
        }
    }
}
