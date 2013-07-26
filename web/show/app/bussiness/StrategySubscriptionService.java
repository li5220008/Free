package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.StrategyBaseDto;
import dto.StrategySearchCnd;
import org.apache.commons.lang.StringUtils;
import play.libs.F;
import util.CommonUtils;
import util.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * User: weiguili(li5220008@163.com)
 * Date: 12-12-18
 * Time: 上午11:21
 */
public class StrategySubscriptionService {
    /**
     * 订阅策略列表
     * @param myselect 分类
     * @param content 关键字
     * @param pageNo 页码
     * @param uid 用户ID
     * @return
     */
    public static F.T2<List<StrategyBaseDto>,Page> strategyList(int myselect, String content, int pageNo,Long uid){
        StringBuilder querySql = new StringBuilder(SqlLoader.getSqlById("StrategyOrderSql"));
        StringBuilder countSql = new StringBuilder(SqlLoader.getSqlById("CountStrategyOrderSql"));
        List<Object> queryList = new ArrayList<Object>();
        List<Object> queryCount = new ArrayList<Object>();

        //必须属于当前用户
        querySql.append(" AND ui.`id`=?\n");
        countSql.append(" AND ui.`id`=?\n");
        queryList.add(uid);
        queryCount.add(uid);

        //关键字过滤
        if(StringUtils.isNotBlank(content)){
            querySql.append(" AND (sb.`name` like ? OR sb.`provider` like ? )\n");
            countSql.append(" AND (sb.`name` like ? OR sb.`provider` like ? )\n");
            queryList.add("%"+content+"%");
            queryList.add("%"+content+"%");
            queryCount.add("%"+content+"%");
            queryCount.add("%"+content+"%");
        }

        //排序
        if(myselect ==2){
            querySql.append(" ORDER BY collectCount DESC\n");
        }else if(myselect == 3){
            querySql.append(" ORDER BY upTime DESC\n");
        }else{
            querySql.append(" ORDER BY yield DESC\n");//默认排序
        }
        //获得总记录
        Long total = QicDbUtil.queryQicDbCount(countSql.toString(), queryCount.toArray());
        //实例分页类
        Page page = new Page(total.intValue(),pageNo);
        querySql.append(" limit ?,?\n");
        queryList.add(page.beginIndex);
        queryList.add(page.pageSize);
        List<StrategyBaseDto> sbList = QicDbUtil.queryQicDBBeanList(querySql.toString(), StrategyBaseDto.class, queryList.toArray());

        return F.T2(sbList,page);
    }

    /**
     * 高级搜索
     *
     * @param cnd 搜索DTO
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public static F.T2<List<StrategyBaseDto>, Page> advanceSearch(StrategySearchCnd cnd, int pageNo,Long uid) {
        String sql = SqlLoader.getSqlById("StrategyOrderSql");
        StringBuilder querySql = new StringBuilder("select * from (\n" + sql + "\n) distTable where 1=1\n");
        StringBuilder countSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable where 1=1\n");

        List<Object> queryList = new ArrayList<Object>();
        List<Object> queryCount = new ArrayList<Object>();

        //必须属于当前用户
        querySql.append(" AND uid=?\n");
        countSql.append(" AND uid=?\n");
        queryList.add(uid);
        queryCount.add(uid);

        if (cnd.tradeType != null && cnd.tradeType != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            querySql.append("AND stype = ?\n");
            countSql.append("AND stype = ?\n");
            queryList.add(cnd.tradeType.intValue());
            queryCount.add(cnd.tradeType.intValue());
        }

        if(cnd.tradeVariety != null && cnd.tradeVariety != CommonUtils.SELECT_ALL_OPTION_VALUE){
            querySql.append("AND tradeVariety = ?\n");
            countSql.append("AND tradeVariety = ?\n");
            queryList.add(cnd.tradeVariety.intValue());
            queryCount.add(cnd.tradeVariety.intValue());
        }

        if (cnd.yieldDown != null) {
            querySql.append("AND yield >= ?\n");
            countSql.append("AND yield >= ?\n");
            queryList.add(cnd.yieldDown/100);
            queryCount.add(cnd.yieldDown/100);
        }

        if (cnd.yieldUp != null) {
            querySql.append("AND yield <= ?\n");
            countSql.append("AND yield <= ?\n");
            //该处除以100是因为按照百分比来所搜
            queryList.add(cnd.yieldUp/100);
            queryCount.add(cnd.yieldUp/100);
        }

        if (cnd.profitRatioDown != null) {
            querySql.append("AND profitRatio >= ?\n");
            countSql.append("AND profitRatio >= ?\n");
            queryList.add(cnd.profitRatioDown/100);
            queryCount.add(cnd.profitRatioDown/100);
        }

        if (cnd.profitRatioUp != null) {
            querySql.append("AND profitRatio <= ?\n");
            countSql.append("AND profitRatio <= ?\n");
            queryList.add(cnd.profitRatioUp/100);
            queryCount.add(cnd.profitRatioUp/100);
        }

        if (cnd.starDown != null) {
            querySql.append("AND starLevel >= ?\n");
            countSql.append("AND starLevel >= ?\n");
            queryList.add(cnd.starDown);
            queryCount.add(cnd.starDown);
        }

        if (cnd.starUp != null) {
            querySql.append("AND starLevel <= ?\n");
            countSql.append("AND starLevel <= ?\n");
            queryList.add(cnd.starUp);
            queryCount.add(cnd.starUp);
        }

        //System.out.print(querySql);
        Long total = QicDbUtil.queryQicDbCount(countSql.toString(),queryCount.toArray());
        Page page = new Page(total.intValue(),pageNo);
        querySql.append("limit ?,?\n");
        queryList.add(page.beginIndex);
        queryList.add(page.pageSize);
        List<StrategyBaseDto> dtoList = QicDbUtil.queryQicDBBeanList(querySql.toString(),StrategyBaseDto.class,queryList.toArray());
        return F.T2(dtoList, page);
    }
}
