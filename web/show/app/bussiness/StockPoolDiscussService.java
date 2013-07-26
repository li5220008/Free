package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import play.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: liuhongjiang
 * Date: 12-11-20
 * Time: 上午9:47
 */
public class StockPoolDiscussService {

    /**
     * 保存该用户对该策略的评论
     * @param star
     * @param uid
     * @param spid
     */
    public static boolean saveDiscuss(int star,Long uid,Long spid){

        if(star==0||uid==0||spid==0){
            return false;
        }
        String is_stock_pool_comment_sql = SqlLoader.getSqlById("is_stock_pool_comment");
        String is_user_comment_sql = SqlLoader.getSqlById("is_user_comment");
        String updateStockPoolCommentSql = SqlLoader.getSqlById("update_stock_pool_comment1");
        String insertStockPoolCommentSql = SqlLoader.getSqlById("insert_stock_pool_comment");
        String insertUserCommentSql = SqlLoader.getSqlById("insert_user_comment");

        Map<String, Object>  user_map = QicDbUtil.queryQicDBSingleMap(is_user_comment_sql, uid, spid);
        if(user_map==null||user_map.size()==0){
        Map<String, Object>  stock_pool_map = QicDbUtil.queryQicDBSingleMap(is_stock_pool_comment_sql, spid);
            if(stock_pool_map==null||stock_pool_map.size()==0){
                QicDbUtil.updateQicDB(insertStockPoolCommentSql, spid, star, 1);
            } else{
                QicDbUtil.updateQicDB(updateStockPoolCommentSql, star, 1, spid);
            }

            if(Logger.isDebugEnabled()){
                Logger.debug(is_user_comment_sql);
            }
                QicDbUtil.updateQicDB(insertUserCommentSql, uid, spid, star);
                return true;

        } else{
            return false;
        }


    }


    /**
     * 判断该用户是否已经评论的该股票池
     * @param spid
     * @param uid
     */
    public static Integer judge(String spid,Long uid){
        int result=0;
        String is_user_comment_sql = SqlLoader.getSqlById("is_user_comment");
        Map<String, Object>  user_map = QicDbUtil.queryQicDBSingleMap(is_user_comment_sql, uid, spid);
        if(user_map!=null&&user_map.size()>0){
            result=1;
        }
        return result;
    }

    /**
     * 从股票池列表中查询已评论的股票池
     *
     * @param uid  用户id
     * @return List<Map<String,Object>>
     */
    public static Set<Integer> queryStockPoolDiscussMap( Long uid) {
        String sql = "SELECT spid as spid FROM user_stock_pool_discuss  WHERE uid =" + uid;
        List<Map<String, Object>> stockmapList = QicDbUtil.queryQicDBMapList(sql);
        Set<Integer> result = new HashSet<Integer>();
        if(stockmapList!=null){
            for (Map<String, Object> map : stockmapList) {
                result.add(Integer.parseInt(map.get("spid").toString()));
            }
        }
        return result;
    }

}
