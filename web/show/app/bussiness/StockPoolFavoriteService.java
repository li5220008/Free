package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.StockPoolSearchCnd;
import dto.StockPoolsPar;
import dto.StockpoolDto;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import play.libs.F;
import util.CommonUtils;
import util.Page;

import java.util.Date;
import java.util.List;

/**
 * 股票池组合浏览基本业务类 我的收藏
 * User: liangbing
 * Date: 12-11-23
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class StockPoolFavoriteService {


    /**
     * @param spp 参数对象类
     * @param pageNo  当前页
     * @return_1 为结果集, _2为分页对象
     */
    public static F.T2<List<StockpoolDto>, Page> stockPoolyList(StockPoolsPar spp,int pageNo,Long uid) {

        String sqlList = SqlLoader.getSqlById("stockpoolFavoriteSql");
        sqlList+=" WHERE uspc.`uid`="+uid ;
        List<StockpoolDto> sbdList = null;
        String condition = "";
        Long total;
        StringBuilder coutSql = new StringBuilder();
        //content 关键字查询内容
        if (StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if(spp.strategyName.equals("I05")){
                condition = " AND  (a.`StockPoolName` like ? OR source like ?) and ss.`StrategyCode`in (?,?,?)";
            }else{
                condition = " AND  (a.`StockPoolName` like ? OR source like ?) and ss.`StrategyCode`in (?)";
            }
            sqlList += condition;
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            if(spp.strategyName.equals("I05")){
                total = QicDbUtil.queryQicDbCount(coutSql.toString(), "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName,"I0501","I0502");
            }else{
                total = QicDbUtil.queryQicDbCount(coutSql.toString(), "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName);
            }
        } else if (StringUtils.isNotBlank(spp.content) && !StringUtils.isNotBlank(spp.strategyName)) {
            condition = " AND  a.`StockPoolName` like ? OR source like ? ";
            sqlList += condition;
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            total = QicDbUtil.queryQicDbCount(coutSql.toString(), "%" + spp.content + "%", "%" + spp.content + "%");
        } else if (!StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if(spp.strategyName.equals("I05")){
                condition = " AND  ss.`StrategyCode`  in(?,?,?) ";
            }else{
                condition = " AND  ss.`StrategyCode`  in(?) ";
            }
            sqlList += condition;
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            if(spp.strategyName.equals("I05")){
                total = QicDbUtil.queryQicDbCount(coutSql.toString(), spp.strategyName,"I0501","I0502");
            }else{
                total = QicDbUtil.queryQicDbCount(coutSql.toString(), spp.strategyName);
            }
        } else {
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            total = QicDbUtil.queryQicDbCount(coutSql.toString());
        }

        if (StringUtils.isNotBlank(spp.orderSort)) {
            if (spp.flag == 0)
                sqlList += " ORDER BY " + spp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + spp.orderSort + " desc ";

        }

        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";


        if (StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if(spp.strategyName.equals("I05")){
                sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName);
            }else{
                sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName,"I0501","I0502");
            }
        } else if (StringUtils.isNotBlank(spp.content) && !StringUtils.isNotBlank(spp.strategyName)) {
            sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%");
        } else if (!StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if(spp.strategyName.equals("I05")){
                sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StockpoolDto.class, spp.strategyName,"I0501","I0502");
            }else{
                sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StockpoolDto.class, spp.strategyName);
            }
        } else {
            sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StockpoolDto.class);
        }

        return F.T2(sbdList, page);

    }

    /**
     * 高级搜索
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2 page分页对象
     */
    public static F.T2<List<StockpoolDto>, Page>  advanceSearch(StockPoolSearchCnd cnd, int pageNo,Long uid) {
        String sql = SqlLoader.getSqlById("stockpoolFavoriteSql");
        StringBuilder listSelectSql = new StringBuilder("select * from (\n" + sql + "\n) distTable where 1=1 and distTable.uid ="+uid+" \n");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql +"\n) distTable where 1=1 and distTable.uid ="+uid+" \n");
        StringBuilder where = new StringBuilder();
        if(cnd.recommendOrgs != null && cnd.recommendOrgs.length > 0){ //机构代码
            StringBuilder orgwhere = new StringBuilder();
            orgwhere.append(" and orgId in (");
            boolean hasOrg = false;
            for (String org : cnd.recommendOrgs) {
                String[] item = org.split("\\|\\|"); //多个代码, 用 ||分开
                for (String s : item) {
                    if(StringUtils.isNotBlank(s)){
                        orgwhere.append(s.trim() + ",");
                        hasOrg = true;
                    }

                }
            }
            if(hasOrg){
                orgwhere.deleteCharAt(orgwhere.length()-1);
            }
            orgwhere.append(") \n");

            if (hasOrg) {
                where.append(orgwhere.toString());
            }
        }

        if(cnd.reportUpdatePeriod != null && cnd.reportUpdatePeriod != CommonUtils.SELECT_ALL_OPTION_VALUE){
            Date curDate = new Date();
            Date startDate;
            switch (cnd.reportUpdatePeriod.intValue()){
                case 1:
                    startDate = DateUtils.addDays(curDate, -1);
                    break;

                case 2:
                    startDate = DateUtils.addDays(curDate, -7);
                    break;

                case 3:
                    startDate = DateUtils.addMonths(curDate, -1);
                    break;

                case 4:
                    startDate = DateUtils.addMonths(curDate, -3);
                    break;

                case 5:
                    startDate = DateUtils.addMonths(curDate, -6);
                    break;

                case 6:
                    startDate = DateUtils.addYears(curDate, -1);
                    break;
                default:
                    startDate = DateUtils.addDays(curDate, -1); //默认为1天前
                    break;
            }
            where.append(" and updateDate >= '" + DateFormatUtils.format(startDate, "yyyy-MM-dd") + "'").append('\n');
        }

        if(cnd.starDown != null){
            where.append(" and starLevel >= " + cnd.starDown).append('\n');
        }

        if (cnd.starUp != null) {
            where.append(" and starLevel <= " + cnd.starUp).append('\n');
        }

        if (cnd.yearYieldDown != null) {
            where.append(" and annualizedYield >= " + cnd.yearYieldDown).append('\n');
        }

        if (cnd.yearYieldUp != null) {
            where.append(" and annualizedYield <= " + cnd.yearYieldUp).append('\n');
        }

        if (cnd.sharpRateDown != null) {
            where.append(" and yearJensenRatio >= " + cnd.sharpRateDown).append('\n');
        }

        if (cnd.sharpRateUp != null) {
            where.append(" and yearJensenRatio <= " + cnd.sharpRateUp).append('\n');
        }

        listSelectSql.append(where);
        coutSql.append(where);

        Long total = QicDbUtil.queryQicDbCount(coutSql.toString());

        Page page = new Page(total.intValue(), pageNo);
        listSelectSql.append("\n limit " + page.beginIndex + "," + page.pageSize + "\n");
        List<StockpoolDto> dtoList = QicDbUtil.queryQicDBBeanList(listSelectSql.toString(), StockpoolDto.class);

        return F.T2(dtoList, page);
    }

}
