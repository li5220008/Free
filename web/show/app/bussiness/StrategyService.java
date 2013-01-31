package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.StrategyBaseDto;
import dto.StrategySearchCnd;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.db.DB;
import play.libs.F;
import util.CommonUtils;
import util.Page;
import utils.Constants;
import utils.QicFileUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 策略超市基本信息展示业务方法
 * User: liangbing
 * Date: 12-11-9
 * Time: 上午9:10
 * To change this template use File | Settings | File Templates.
 */
public class StrategyService {

    /**
     * @param myselect 分类搜索
     * @param content  关键字
     * @param pageNo   当前页数
     * @return  _1 为结果集, _2为 分页page信息,
     */
    public static F.T2<List<StrategyBaseDto>, Page> strategyList(int myselect, String content, int pageNo) {
        String sqlList = SqlLoader.getSqlById("StrategyBaseInfoSql");

        List<StrategyBaseDto> sbdList = null;
        String condition = "";

        //content 关键字查询内容
        if (StringUtils.isNotBlank(content)) {
            condition = " AND  (sb.`name` like ? OR sb.`provider` like ?) ";
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
            sqlList += " ORDER BY yield DESC ";

        } else if (myselect == 2) {//myselect =2 按人气排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY collectCount DESC ";

        } else {//按最新 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY upTime DESC ";

        }
        Long total;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");

        if (StringUtils.isNotBlank(content)) {
              total = QicDbUtil.queryQicDbCount(coutSql.toString(),"%" + content + "%", "%" + content + "%");
        } else {
            total =QicDbUtil.queryQicDbCount(coutSql.toString());
        }
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (StringUtils.isNotBlank(content)) {
            sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StrategyBaseDto.class, "%" + content + "%", "%" + content + "%");
        } else {
            sbdList = QicDbUtil.queryQicDBBeanList(sqlList, StrategyBaseDto.class);
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
    public static F.T2<List<StrategyBaseDto>, Page> advanceSearch(StrategySearchCnd cnd,int myselect, int pageNo) {
        String sql = SqlLoader.getSqlById("StrategyBaseInfoSql");
        StringBuilder listSelectSql = new StringBuilder("select * from (\n" + sql + "\n) distTable where 1=1 \n");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable where 1=1 \n");
        StringBuilder where = new StringBuilder();
        if (cnd.tradeType != null && cnd.tradeType != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and stype = " + cnd.tradeType.intValue());
        }

        if (cnd.tradeVariety != null && cnd.tradeVariety != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and tradeVariety = " + cnd.tradeVariety.intValue()).append('\n');
        }

        if (cnd.yieldDown != null) {
            where.append(" and yield >= " + cnd.yieldDown / 100).append('\n');
        }

        if (cnd.yieldUp != null) {
            where.append(" and yield <= " + cnd.yieldUp / 100).append('\n');
        }

        if (cnd.profitRatioDown != null) {
            where.append(" and profitRatio >= " + cnd.profitRatioDown / 100).append('\n');
        }

        if (cnd.profitRatioUp != null) {
            where.append(" and profitRatio <= " + cnd.profitRatioUp / 100).append('\n');
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

        Long total=QicDbUtil.queryQicDbWithHandler(coutSql.toString(), new ScalarHandler<Long>());
        Page page = new Page(total.intValue(), pageNo);
        listSelectSql.append("\n limit " + page.beginIndex + "," + page.pageSize + "\n");
        List<StrategyBaseDto> dtoList = QicDbUtil.queryQicDBBeanList(listSelectSql.toString(), StrategyBaseDto.class);

        return F.T2(dtoList, page);
    }
    public  static  F.T2<List<StrategyBaseDto>, Page> findStrategysByUser(Map<String,String> queryParams){
            //查询sql
            StringBuffer querySql = new StringBuffer(SqlLoader.getSqlById("findStrategysByUser"));
            //计算总数sql
            StringBuffer countSql = new StringBuffer();
            countSql.append("SELECT \n").append("COUNT(1)\n").append("FROM (\n");
             countSql.append(SqlLoader.getSqlById("countOfStrategysByUser"));
            String keyWord = queryParams.get("keyword");
            String orderCol = queryParams.get("orderCol") == null?"1":queryParams.get("orderCol");
            int pageNo = Integer.valueOf(queryParams.get("pageNo"));
            int status = Integer.valueOf(queryParams.get("status"));
            int uid = Integer.valueOf(queryParams.get("uid"));
            int orderByType = queryParams.get("orderType")==null?0:Integer.valueOf(queryParams.get("orderType"));
            List<Object> queryList = new ArrayList<Object>(4);
            List<Object> countList = new ArrayList<Object>(2);
            queryList.add(uid);
            countList.add(uid);
            Long totalSize = 0L;

           if(status==StrategyBaseDto.StrategyStatus.DOWNSHELF.value || status==StrategyBaseDto.StrategyStatus.DELETED.value){//状态查询
                querySql.append(" and status=?\n");
                countSql.append(" and status=?\n");
                queryList.add(status);
                countList.add(status);
            }else if(status == StrategyBaseDto.StrategyStatus.UPSHELF.value){
               querySql.append(" and status=?  or status=?\n");
               countSql.append(" and status=?  or status=?\n");
               queryList.add(StrategyBaseDto.StrategyStatus.UPSHELF.value);
               countList.add(StrategyBaseDto.StrategyStatus.UPSHELF.value);
               queryList.add(StrategyBaseDto.StrategyStatus.WAITINGUPSHELF.value);
               countList.add(StrategyBaseDto.StrategyStatus.WAITINGUPSHELF.value);
           }
           else if(status==-2){//查询审核中的 此处的审核中包含 沙箱测试(2) 回测中(3) 回测试失败(8) 审核中(1) 四个状态
                querySql.append(" and (status >0 and status<? or status=? or status=?)\n");
                countSql.append(" and (status >0 and status<? or status=? or status=?)\n");
                queryList.add(StrategyBaseDto.StrategyStatus.UPSHELF.value);
                countList.add(StrategyBaseDto.StrategyStatus.UPSHELF.value);
                queryList.add(StrategyBaseDto.StrategyStatus.FINISHTEST.value);
                countList.add(StrategyBaseDto.StrategyStatus.FINISHTEST.value);
                queryList.add(StrategyBaseDto.StrategyStatus.BACKTESTINGFAILER);
                countList.add(StrategyBaseDto.StrategyStatus.BACKTESTINGFAILER);
            }
            if(keyWord !=null &&!"".equals(keyWord)){//关键查询
                //这里注意 like需要使用预编译的时候 不用写成:querySql.append(" and (provider like ? or name like '%?%' )\n");
                //%号必需在set的时候进行拼接
                querySql.append(" and (provider like ? or name like ? )\n");
                countSql.append(" and (provider like ?  or name like  ? )\n");
                queryList.add("%"+keyWord+"%");
                queryList.add("%"+keyWord+"%");
                countList.add("%"+keyWord+"%");
                countList.add("%"+keyWord+"%");
            }
            //排序,把order by 的字段出来
            querySql.append(" order by "+getColNameByIndex(orderCol)+ (orderByType == 0?" ASC":" DESC")+" \n");
            countSql.append("\n) tmp");
            totalSize = QicDbUtil.queryQicDbCount(countSql.toString(),countList.toArray());
            Page page = new Page(totalSize.intValue(), pageNo);
            querySql.append(" limit ?,? ");
            queryList.add(page.beginIndex);
            queryList.add(page.pageSize);

            List<StrategyBaseDto> strategyBaseDtoList = QicDbUtil.queryQicDBBeanList(querySql.toString(),StrategyBaseDto.class,queryList.toArray());
            return F.T2(strategyBaseDtoList,page);
    }
    private static String getColNameByIndex(String index){
        switch (Integer.valueOf(index)){
            case 1:
                return "sb.name";
            case 2:
                return "sb.provider";
            case 3:
                return "sb.status";
            case 4:
                return "sb.upload_time";
            case 5:
                return "sb.pass_time";
            case 6:
                return "sb.up_time";
            case 7:
                return "sb.down_time";
            case 8:
                return "sb.order_count";
            case 9:
                return "uso.validCount";
            case 10:
                return "sb.collect_count";
            default:
                return "sb.status";




        }

    }

    /**
     * 上传策略
     */
    public static boolean insertStrategy(StrategyBaseDto strategyDto,String[] files,long uid) throws SQLException {
        Connection conn = DB.getDBConfig().getConnection();
        String sql = SqlLoader.getSqlById("insertStrategy");
        //conn.setTransactionIsolation(TransactionIsolation.);设置事务的隔离级别
        try{

            PreparedStatement statement = conn.prepareStatement(sql);
            //uuid
            String strategyId = UUID.randomUUID().toString().replaceAll("-", "");
            statement.setString(1,strategyId);
            //策略名
            statement.setString(2,strategyDto.sname);
            //策略类型
            statement.setInt(3,strategyDto.stype);
            //交易品种
            statement.setInt(4,strategyDto.tradeVariety);
            //提供者
            statement.setString(5,strategyDto.provider);
            //提供者信息
            statement.setString(6,strategyDto.providerDesp);
            //提供简介
            statement.setString(7,strategyDto.desp);
            //策略状态
            statement.setInt(8,StrategyBaseDto.StrategyStatus.CHECKING.value);
            //上传用户的uid,暂时不填
            statement.setLong(9,uid);
            statement.setString(10,strategyDto.lookbackStime);
            statement.setString(11,strategyDto.lookbackEtime);
            //
            //赋值
            statement.executeUpdate();
            for(String tmpFile :files){
                File file = new File(tmpFile);
                File newFile ;
                if(file.getName().endsWith(".xml")){
                    newFile = new File(file.getParentFile().getAbsolutePath(),strategyId + ".xml");
                }else if(file.getName().endsWith(".dll")){
                    newFile = new File(file.getParentFile().getAbsolutePath(),strategyId + ".dll");
                }else {
                    continue;
                }
                file.renameTo(newFile);
                QicFileUtil.saveStrategyToOfficai(newFile, strategyId);//移动正式文件库中,用uuid作文件 防止文件名重复
                QicFileUtil.deleteFile(newFile);//将临时文件库中文件删除
            }
            String qsmSql = SqlLoader.getSqlById("syncStrategyToQsm");
            String loadBaseDir = SystemConfigService.get(Constants.OTHERS_LOAD_STRATEGY_BASE_DIR);//这里的baseDir一定要/结尾
           // QicDbUtil.updateQicDB(qsmSql, strategyId, strategyDto.sname, loadBaseDir + strategyId);
            QicDbUtil.updateQicDB(qsmSql, strategyId, strategyDto.sname, strategyId,"127.0.0.1");//2012-12-24改的
            return true;
        }catch (Exception e){
            Logger.error(e.getMessage(),e);
            //出现异常,删除临时文件

            conn.rollback();//只要一方面出异常则删除文件 ，数据库回滚
            return false;
            //throw new DatabaseException(e.getMessage(),e);
        }


    }
    public static StrategyBaseDto findStrategyByName(String sname){
        String sql = SqlLoader.getSqlById("findStrategyByName");
        return QicDbUtil.queryQicDBSingleBean(sql,StrategyBaseDto.class,sname);
    }
}
