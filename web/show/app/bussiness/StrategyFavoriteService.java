package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.StrategyBaseDto;
import dto.StrategySearchCnd;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.libs.F;
import util.CommonUtils;
import util.Page;

import java.util.List;

/**
 * 我的收藏展示
 * User: liangbing
 * Date: 12-11-12   策略基本信息
 * Time: 下午2:13
 */
public class StrategyFavoriteService {

    /**
     * @param myselect 分类搜索
     * @param content  关键字
     * @param pageNo   当前页数
     * @return _1 为结果集, _2为 分页page信息,
     */
    public static F.T2<List<StrategyBaseDto>, Page> strategyList(int myselect, String content, int pageNo,Long uid) {
        String sqlList = SqlLoader.getSqlById("StrategyFavoriteSql");
        sqlList+=" AND ui.`id`="+uid;
        List<StrategyBaseDto> sbdList = null;
        String condition = "";

        //content 关键字查询内容
        if (StringUtils.isNotBlank(content)) {
            condition = "  and  (sb.`name` like ? OR sb.`provider` like ? )";
        }
        if (myselect == 0) {//分类查询,默认按收益率排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY yield DESC ";

        } else if (myselect == 1) {//myselect = 1按收益率排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY yield DESC";

        } else if (myselect == 2) {//myselect =2 按人气排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY collectCount DESC";

        } else {//按最新 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY upTime DESC";

        }
        Long total;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable ");
        if (StringUtils.isNotBlank(content)) {
            total = QicDbUtil.queryQicDbCount(coutSql.toString(), "%" + content + "%", "%" + content + "%");
        } else {
            total =QicDbUtil.queryQicDbCount(coutSql.toString());
        }
        Page page = new Page(total.intValue(), pageNo);
        if(total>0){
            sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
            if (StringUtils.isNotBlank(content)) {
                sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StrategyBaseDto.class, "%" + content + "%", "%" + content + "%");
            } else {
                sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StrategyBaseDto.class);
            }
        }
        return F.T2(sbdList, page);

    }


    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public static F.T2<List<StrategyBaseDto>, Page> advanceSearch(StrategySearchCnd cnd,int myselect,int pageNo,Long uid) {
        String sql = SqlLoader.getSqlById("StrategyFavoriteSql");
        StringBuilder listSelectSql = new StringBuilder("select * from (\n" + sql + "\n) distTable where 1=1 and uid="+uid+" \n");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable where 1=1 and uid="+uid+" \n");
        StringBuilder where = new StringBuilder();
        if (cnd.tradeType != null && cnd.tradeType != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and stype = " + cnd.tradeType.intValue());
        }

        if (cnd.tradeVariety != null && cnd.tradeVariety != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and tradeVariety = " + cnd.tradeVariety.intValue()).append('\n');
        }

        if (cnd.yieldDown != null) {
            where.append(" and yield >= " + cnd.yieldDown).append('\n');
        }

        if (cnd.yieldUp != null) {
            where.append(" and yield <= " + cnd.yieldUp).append('\n');
        }

        if (cnd.profitRatioDown != null) {
            where.append(" and profitRatio >= " + cnd.profitRatioDown).append('\n');
        }

        if (cnd.profitRatioUp != null) {
            where.append(" and profitRatio <= " + cnd.profitRatioUp).append('\n');
        }

        if (cnd.starDown != null) {
            where.append(" and starLevel >= " + cnd.starDown).append('\n');
        }

        if (cnd.starUp != null) {
            where.append(" and starLevel <= " + cnd.starUp).append('\n');
        }

        if(myselect==3){
            where.append(" ORDER BY upTime DESC ");
        }else if(myselect==2){
            where.append(" ORDER BY collectCount DESC ");
        }else{
            where.append(" ORDER BY yield DESC ");
        }

        if (Logger.isDebugEnabled()) {
            Logger.debug("sql where ==" + where.toString());
        }

        listSelectSql.append(where);
        coutSql.append(where);

        Long total = QicDbUtil.queryQicDbWithHandler(coutSql.toString(), new ScalarHandler<Long>());

        Page page = new Page(total.intValue(), pageNo);
        listSelectSql.append("\n limit " + page.beginIndex + "," + page.pageSize + "\n");
        List<StrategyBaseDto> dtoList = QicDbUtil.queryQicDBBeanList(listSelectSql.toString(), StrategyBaseDto.class);

        return F.T2(dtoList, page);
    }
}
