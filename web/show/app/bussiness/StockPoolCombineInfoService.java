package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.StockPoolCombineInfoDto;
import models.common.UserStockPoolCollect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 股票池组合列表信息查看
 * User: panzhiwei
 * Date: 12-11-21
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 */
public class StockPoolCombineInfoService {
    /**
     * 得到股票池组合列表信息
     * @param stockpoolcode
     * @return
     */
    public static List<StockPoolCombineInfoDto> queryCombineInfo(String stockpoolcode){
        String sql = SqlLoader.getSqlById("stockPoolList1");
        List<Map<String,Object>> inlist = QicDbUtil.queryQicDBMapList(sql,stockpoolcode); //股票池所有的股票list
        String sql2 = SqlLoader.getSqlById("stockPoolList2");
        List<Map<String,Object>> outlist = QicDbUtil.queryQicDBMapList(sql2,stockpoolcode); //选出最后一次调整状态是调出的list
        for(int i = 0;i<outlist.size();i++){
            String a = (String)outlist.get(i).get("scode");
            for(int j = 0;j<inlist.size();j++){
                String b = (String)inlist.get(j).get("scode");
                if(a.equals(b)){
                    inlist.remove(j);
                }
            }
        }
        List<StockPoolCombineInfoDto> stockPoolCombineInfoDtoList = new ArrayList<StockPoolCombineInfoDto>();

        for(int k = 0;k<inlist.size();k++){
            StockPoolCombineInfoDto s = new StockPoolCombineInfoDto();
            s.scode = (String)inlist.get(k).get("scode");
            s.exchangeCode = (String) inlist.get(k).get("exchangeCode");
            s.shortName = (String) inlist.get(k).get("shortName");
            stockPoolCombineInfoDtoList.add(s);
        }
        return stockPoolCombineInfoDtoList;
    }

    /**
     * 此方法用于根据spid和uid判断该股票池是否被收藏
     * @param spid
     * @param uid
     * @return
     */
    public static boolean iscollect(String spid, Long uid) {
        String sql = "SELECT * FROM qic_db.`user_stock_pool_collect` WHERE uid=? AND spid=?";
        UserStockPoolCollect userStockPoolCollect = QicDbUtil.queryQicDBSingleBean(sql,UserStockPoolCollect.class,uid,spid);
        if (userStockPoolCollect == null)
            return false;
        else
            return true;
    }
}
