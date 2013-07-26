package bussiness;


import dbutils.QicDbUtil;
import dto.StockpoolDto;
import models.common.UserStockPoolCollect;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 股票池收藏
 * User: panzhiwei
 * Date: 12-11-19
 * Time: 上午11:43
 * To change this template use File | Settings | File Templates.
 */
public class StockPoolCollectService {
    /**
     * 从股票池列表中查询已收藏的股票池
     *
     * @param list 股票池列表
     * @param uid  用户id
     * @return List<Map<String,Object>>
     */
    public static Set<Integer> queryStockPoolCollectMap(List<StockpoolDto> list, Long uid) {
        String sql = "SELECT spid as spid FROM qic_db.`user_stock_pool_collect`  WHERE uid =" + uid + " AND spid in ( ";
        Set<Integer> result = new HashSet<Integer>();
        if(list!=null&&list.size()>0){
        for (StockpoolDto stockPool : list) {
            sql += stockPool.id+",";
        }
        sql = sql.substring(0,sql.length()-1);
        sql += ")";
        List<Map<String, Object>> stockmapList = QicDbUtil.queryQicDBMapList(sql);

        for (Map<String, Object> map : stockmapList) {
            result.add(Integer.parseInt(map.get("spid").toString()));
        }
        }
        return result;
    }

    /**
     * 股票池收藏
     *
     * @param uid  用户id
     * @param spid 股票池id
     */
    public static void stockpooladdcollect(long uid, long spid) throws Exception {
        String sql = "SELECT * FROM qic_db.`user_stock_pool_collect` a WHERE a.`uid`=? AND a.`spid`=?";
        UserStockPoolCollect userStockPoolCollect = QicDbUtil.queryQicDBSingleBean(sql, UserStockPoolCollect.class, uid, spid);
        if (userStockPoolCollect == null) {
            String sql2 = "INSERT INTO qic_db.`user_stock_pool_collect`(uid,spid) VALUES(?,?)";
            QicDbUtil.updateQicDB(sql2, uid, spid);

            String sql4 = "SELECT * FROM stock_pool_ext a WHERE spid = ?";//查看stock_pool_ext是否有这个股票池的数据
            Map<String,Object> map = QicDbUtil.queryQicDBSingleMap(sql4,spid);
            if(map == null){
                String sql5 = "INSERT INTO qic_db.`stock_pool_ext` (collect_count, spid) VALUES (?, ?)";
                QicDbUtil.updateQicDB(sql5,1,spid);
            }
            else{
                String sql3 = "UPDATE qic_db.`stock_pool_ext` a SET a.`collect_count` = a.`collect_count` + 1 WHERE a.`spid`=?";
                QicDbUtil.updateQicDB(sql3, spid);
            }
        } else {
            throw new Exception("已经收藏股票池.");
        }

    }

    /**
     * 股票池取消收藏
     *
     * @param uid        用户id
     * @param spid       股票池id
     */
    public static void stockpooldeletecollect(long uid, long spid) throws Exception {
        String sql = "SELECT * FROM qic_db.`user_stock_pool_collect` a WHERE a.`uid`=? AND a.`spid`=?";
        UserStockPoolCollect userStockPoolCollect = QicDbUtil.queryQicDBSingleBean(sql, UserStockPoolCollect.class, uid, spid);
        if (userStockPoolCollect != null) {
            String sql2 = "DELETE  FROM qic_db.`user_stock_pool_collect` WHERE uid=? AND spid=?";
            QicDbUtil.updateQicDB(sql2, uid, spid);

            String sql3 = "UPDATE qic_db.`stock_pool_ext` a SET a.`collect_count` = a.`collect_count` - 1 WHERE a.`spid`=?";
            QicDbUtil.updateQicDB(sql3, spid);
        } else {
            throw new Exception("已经取消收藏.");
        }


    }

}
