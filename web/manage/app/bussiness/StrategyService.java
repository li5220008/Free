package bussiness;

import com.google.gson.Gson;
import dbutils.QicDbUtil;
import dbutils.QsmDbUtil;
import dbutils.SqlLoader;
import dto.StrategyDownTaskContextDto;
import dto.StrategyDto;
import dto.StrategyPar;
import dto.UserStrategyOrderDto;
import models.common.StrategyBaseinfo;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.db.DB;
import play.libs.F;
import util.*;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 策略列表的业务方法.
 * User: liangbing
 * Date: 12-12-10
 * Time: 下午2:19
 * 根据条件查询策略列表
 */
public class StrategyService {

    /**
     * 查询待上架策略
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 待上架策略对象集合, _2 Page 分页对象
     */
    public static F.T2<List<StrategyDto>, Page> waitList(StrategyPar sp, int pageNo) {
        String sqlList = SqlLoader.getSqlById("StrategySql");
        sqlList +=" and status in (1,2,3,6,8) ";
        if (StringUtils.isNotBlank(sp.keyWords)) {
            sqlList += " and (sb.`name` LIKE '%" + sp.keyWords + "%' OR sb.`provider` LIKE '%" + sp.keyWords + "%')  ";
        }
        sqlList+=" GROUP BY id ";
        List<StrategyDto> audList = null;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total = QicDbUtil.queryQicDbCount(coutSql.toString());
        if (StringUtils.isNotBlank(sp.orderSort)) {
            if (sp.orderFlag == 0)
                sqlList += " ORDER BY " + sp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + sp.orderSort + " desc ";

        }
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            audList = QicDbUtil.queryQicDBBeanList(sqlList, StrategyDto.class);
        }
        return F.T2(audList, page);
    }

    /**
     * 策略回收站列表
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 策略回收站对象集合, _2 Page 分页对象
     */
    public static F.T2<List<StrategyDto>, Page> retrieveList(StrategyPar sp, int pageNo) {
        String sqlList = SqlLoader.getSqlById("StrategySql");
        sqlList +=" and status in(?,?) ";
        if (StringUtils.isNotBlank(sp.keyWords)) {
            sqlList += " and (sb.`name` LIKE '%" + sp.keyWords + "%' OR sb.`provider` LIKE '%" + sp.keyWords + "%')  ";
        }
        sqlList+=" GROUP BY id ";
        List<StrategyDto> audList = null;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total = QicDbUtil.queryQicDbCount(coutSql.toString(),StrategyDto.StrategyStatus.DOWNSHELF.value,StrategyDto.StrategyStatus.DELETED.value);
        if (StringUtils.isNotBlank(sp.orderSort)) {
            if (sp.orderFlag == 0)
                sqlList += " ORDER BY " + sp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + sp.orderSort + " desc ";

        }
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            audList = QicDbUtil.queryQicDBBeanList(sqlList, StrategyDto.class,StrategyDto.StrategyStatus.DOWNSHELF.value,StrategyDto.StrategyStatus.DELETED.value);
        }
        return F.T2(audList, page);
    }

    /**
     * 上架策略列表
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 策略列表对象集合, _2 Page 分页对象
     */
    public static F.T2<List<StrategyDto>, Page> groundingList(StrategyPar sp, int pageNo) {
        String sqlList = SqlLoader.getSqlById("StrategySql");
        sqlList +=" and status in (?,?) ";
        if (StringUtils.isNotBlank(sp.keyWords)) {
            sqlList += " and (sb.`name` LIKE '%" + sp.keyWords + "%' OR sb.`provider` LIKE '%" + sp.keyWords + "%')  ";
        }
        sqlList+=" GROUP BY id ";
        List<StrategyDto> audList = null;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total = QicDbUtil.queryQicDbCount(coutSql.toString(),StrategyDto.StrategyStatus.UPSHELF.value,StrategyDto.StrategyStatus.WAITINGUPSHELF.value);
        if (StringUtils.isNotBlank(sp.orderSort)) {
            if (sp.orderFlag == 0)
                sqlList += " ORDER BY " + sp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + sp.orderSort + " desc ";

        }
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            audList = QicDbUtil.queryQicDBBeanList(sqlList, StrategyDto.class,StrategyDto.StrategyStatus.UPSHELF.value,StrategyDto.StrategyStatus.WAITINGUPSHELF.value);
        }
        return F.T2(audList, page);
    }

    /**
     * author 潘志威
     * 加入回测
     * @param ids
     */
    @Deprecated
    public static void addLookback(String ids[],int status){

        String sql = "UPDATE qic_db.`strategy_baseinfo` a SET a.`status` = ?, a.`pass_time` = NOW() WHERE a.`id` IN (";
        for(int i = 0; i < ids.length; i ++){
            if(i == ids.length - 1){
                sql += ids[i];
            }
            else{
                sql += ids[i] + ",";
            }
        }
        sql += ")";
        QicDbUtil.updateQicDB(sql,status);

    }

    /**
     * author 潘志威
     * 删除策略
     * @param ids
     */
    public static void delstrategy(String ids[]){
        String sql = SqlLoader.getSqlById("delStrategy");
        Object[][] params = new Object[ids.length][2];
        for (int i = 0; i < ids.length; i ++) {
            params [i][0] = StrategyDto.StrategyStatus.DELETED.value;
            params [i][1] = ids[i];
        }
        QicDbUtil.batchQicDB(sql,params);
    }

    /**
     * author 潘志威
     * 策略清空
     */
    public static void emptystrategy(){
        String sql = SqlLoader.getSqlById("emptyStrategy");
        QicDbUtil.updateQicDB(sql);
    }


    /**
     * 上传策略
     */
    public static boolean insertStrategy(StrategyDto strategyDto,String[] files,long uid) throws SQLException {
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
            statement.setInt(8,StrategyDto.StrategyStatus.CHECKING.value);
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
            LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_UPLOAD_STRATEGY,SystemLoggerMessage.UPLOAD_STRATEGY,SystemLoggerMessage.TYPE);//写入系统日志
            return true;
        }catch (Exception e){
            Logger.error(e.getMessage(),e);
            //出现异常,删除临时文件

            conn.rollback();//只要一方面出异常则删除文件 ，数据库回滚
            return false;
            //throw new DatabaseException(e.getMessage(),e);

        }


    }
     public static StrategyDto findStrategyByName(String sname){
         String sql = SqlLoader.getSqlById("findStrategyByName");
         return QicDbUtil.queryQicDBSingleBean(sql,StrategyDto.class,sname);
     }
     public static StrategyBaseinfo findStrategyById(Long id){
         String sql = SqlLoader.getSqlById("findStrategyById");
         return QicDbUtil.queryQicDBSingleBean(sql,StrategyBaseinfo.class,id);
     }

    /**
     *
     * 处理 立即下架/延时下架
     *
     * 立即下架包括两个步骤
     * 1.如果当前时间大于最大收藏时间--》策略下架
     * 2.把每条下架信息保存到user_message表
     *
     * 延时下架 把相关信息存入任务调度表（scheduling_task）由定时任务类处理
     * @author 刘泓江
     * @param stId  策略id数组
     * @param setTime 用户设置的 延时下架时间
     * @return 5.策略为待下架状态不允许修改，4.模板内容非法 3.非法操作 2.策略当前有用户订阅，不能下架 1.下架成功
     */
    public static int strategyDown(String stId,Date setTime,String message,long uid){
        Date currentTime = new Date();
        currentTime.setHours(24);
        currentTime.setMinutes(0);
        currentTime.setSeconds(0);

        /* final String KEY_REGEX = "\\[\\$(\\w+)\\.(\\w+)]";
        Pattern pattern = Pattern.compile(KEY_REGEX);
        Matcher matcher = pattern.matcher(message);
        StrategyDownTaskContextDto tastContext = new StrategyDownTaskContextDto();
        if (!matcher.find()){
            return 4;//4.模板内容非法
        }*/
        StrategyDownTaskContextDto tastContext = new StrategyDownTaskContextDto();
        if (setTime == null) {
            setTime = currentTime;
        }else{
            setTime.setHours(24);
        }
        if (setTime.before(currentTime)||stId ==null) {
            return 3;//3.非法操作
        }

        String[] stIds = stId.split(",");
        tastContext.strategyIdArray = stIds;
        tastContext.messageTemplate = message;

        String StrategyDownSql = SqlLoader.getSqlById("StrDown");
        String maxOrderTimeSql = SqlLoader.getSqlById("maxOrderTime");
        String getOrderedUserSql = SqlLoader.getSqlById("getOrderedUser");
        String sendUserMessageSql = SqlLoader.getSqlById("sendUserMassage");
        String queryStraStatusSql = SqlLoader.getSqlById("queryStraStatus");

        //2.策略当前有用户订阅
        Gson gson = new Gson();
        String strIdGroup = gson.toJson(stIds).replace("[", "").replace("]", "");
        String sql = maxOrderTimeSql.replaceAll("#strIdGroup#", strIdGroup);
        UserStrategyOrderDto userStrategyOrder = QicDbUtil.queryQicDBSingleBean(sql, UserStrategyOrderDto.class);
        if (setTime.before(userStrategyOrder.orderTime)) {//设定时间 比 所有已订阅用户最大收藏时间 小。
             return 2;
        }

        //5.策略为待下架状态不允许修改
        for( String id :stIds){
            StrategyDto strategyDto = QicDbUtil.queryQicDBSingleBean(queryStraStatusSql,StrategyDto.class,id);
            if(strategyDto==null||strategyDto.status==StrategyDto.StrategyStatus.WAITINGUPSHELF.value){
                return 5;
            }
        }

        if(setTime.after(currentTime)){//处理延时下架
            StraDownDelayed(stIds,tastContext,StrategyDownSql,setTime,currentTime,getOrderedUserSql,sendUserMessageSql,message);
            LogInfosService.writeSystemLog(uid,SystemLoggerMessage.DO_STR_DOWN,SystemLoggerMessage.STR_DOWN_DELAY,SystemLoggerMessage.TYPE);//写入系统日志
        }else{ //处理立即下架
            for (int i = 0; i < stIds.length; i++) {
                //设置策略状态为已下架
                QicDbUtil.updateQicDB(StrategyDownSql, setTime, StrategyDto.StrategyStatus.DOWNSHELF.value,Long.parseLong(stIds[i]));
                deleteStrategyFromQsm(stIds);
            }
           /* // 下架成功以后 把每条下架信息保存到user_message表
            if (stId != null && stIds.length != 0) {
                for (int i = 0; i < stIds.length; i++) {
                    try {
                        List<UserStrategyOrderDto> list = QicDbUtil.queryQicDBBeanList(getOrderedUserSql, UserStrategyOrderDto.class,Long.parseLong(stIds[i]));
                        //组装用户消息
                        StrategyBaseinfo strategy = StrategyService.findStrategyById(Long.parseLong(stIds[i]));
                        MessageBuilder messageBuilder = new MessageBuilder(message);
                        messageBuilder.addParameter("strategy", strategy);//下架通知模板 【$strategy.name】
                        String  inputVal = messageBuilder.execute();
                        for (UserStrategyOrderDto userStrategyOrderDto : list) {
                            QicDbUtil.updateQicDB(sendUserMessageSql, userStrategyOrderDto.uid, inputVal);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                }*/
                LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_STR_DOWN,SystemLoggerMessage.STR_DOWN_PROMPTLY,SystemLoggerMessage.TYPE);//写入系统日志

        }
        return 1;//1.下架成功
    }

    /**
     * author 潘志威
     * 策略上架
     * @param ids
     * @param serverId qsm中的agentIP
     */
    public static boolean upstrategy(String ids[],int serverId){
        List<StrategyBaseinfo> strategyList = findStrategysByIds(ids);
        for(StrategyBaseinfo strategyBaseinfo : strategyList){
            if(strategyBaseinfo.status != StrategyDto.StrategyStatus.FINISHTEST.value){
                return false;
            }
        }
        String sql = SqlLoader.getSqlById("upShelfStrategy");
        Object[][] params = new Object[ids.length][2];
        for (int i = 0; i < ids.length; i ++){
            params [i][0] = StrategyDto.StrategyStatus.UPSHELF.value;
            params [i][1] = ids[i];
        }
        QicDbUtil.batchQicDB(sql,params);
        synStrateToQsm(ids,serverId);
        //同步数据到qsm个
        return true;
    }
    private static boolean synStrateToQsm(String[] ids,int serverId){
        List<StrategyBaseinfo> list =findStrategysByIds(ids);
        String ip = BackTestService.findServerIpById(serverId);
        if(list == null || list.size() == 0){
            return false;
        }else{
            String basePath = SystemConfigService.get(Constants.OTHERS_LOAD_STRATEGY_BASE_DIR);
            String qsmSql = SqlLoader.getSqlById("syncStrategyToQsm");
            Object[][] params = new Object[list.size()][4];
            for(int row = 0;row<list.size();row++){
                params[row][0] = list.get(row).stUuid;
                params[row][1] = list.get(row).name;
                params[row][2] = basePath + list.get(row).stUuid;//是否要拼要基地址?
                params[row][3] = ip;//是否要拼要基地址?
            }
            QsmDbUtil.batchQsmDB(qsmSql,params);
            return true;
        }

    }
    public static boolean deleteStrategyFromQsm(String[] ids){
        List<StrategyBaseinfo> strategyBaseinfoList =findStrategysByIds(ids);
        if(strategyBaseinfoList== null ||strategyBaseinfoList.size() == 0){
            return false;
        }else {
            String qsmSql = SqlLoader.getSqlById("deleteStrategyFromQsm");
            Object[][] params = new Object[strategyBaseinfoList.size()][1];
            for(int row = 0;row<strategyBaseinfoList.size();row++){
                params[row][0] = strategyBaseinfoList.get(row).stUuid;
            }
            QsmDbUtil.batchQsmDB(qsmSql,params);
        }
        return true;
    }

    /**
     * 查询一批策略
     * @param ids
     * @return
     */
    public static List<StrategyBaseinfo> findStrategysByIds(String ids[]){
        if(ids ==  null || ids.length == 0){
            return new ArrayList<StrategyBaseinfo>();
        }

       Long[]  list = new Long[ids.length];
        for(int i = 0;i<ids.length;i++ ){

            list[i]=Long.valueOf(ids[i]);
        }
       return findStrategysByIds(list);
    }
    public static List<StrategyBaseinfo> findStrategysByIds(Long ids[]){
        StringBuffer  sql =  new StringBuffer(SqlLoader.getSqlById("findStrategysByIds"));
        sql.append(" and id IN (");
        for(int i = 0;i<ids.length;i++ ){
            sql.append("?");
            if(i<ids.length-1){
                sql.append(",");
            }
        }
        sql.append(")");
        return QicDbUtil.queryQicDBBeanList(sql.toString(),StrategyBaseinfo.class,ids);
    }
   public static int  syncBackTestResult(StrategyDto strategyDto){
       String sql = SqlLoader.getSqlById("syncBackTestResult");
       return QicDbUtil.updateQicDB(sql,
                                   strategyDto.strategyId,
                                   strategyDto.retainedProfits,
                                   strategyDto.grossProfit,
                                   strategyDto.overallProfitability,
                                   strategyDto. overallDeficit,
                                   strategyDto.canhsiedRatio,
                                   strategyDto.tradeCount,
                                   strategyDto.longPositionTradeCount,
                                   strategyDto.shortPositionTradeCount,
                                   strategyDto.profitRatio,
                                   strategyDto.profitCount,
                                   strategyDto.deficitCount,
                                   strategyDto.positionCloseCount,
                                   strategyDto.maxSingleProfit,
                                   strategyDto.maxSingleDeficit,
                                   strategyDto.maxSingleProfitRatio,
                                   strategyDto.maxSingleDeficitRatio,
                                   strategyDto.profitLossRatio,
                                   strategyDto.sumOfCommission,
                                   strategyDto.yield,
                                   strategyDto.avgProfitOfMonth,
                                   strategyDto.floatingProfitAndLoss,
                                   strategyDto.totalAsset,
                                   strategyDto.yieldOfMonth,
                                   strategyDto.yieldOfYear,
                                   strategyDto.maxSequentialDeficitCapital,
                                   strategyDto.lastSequentialDeficitCapital,
                                   strategyDto.maxSequentialProfitCount,
                                   strategyDto.lastSequentialProfitCount,
                                   strategyDto.maxSequentialDeficitCount,
                                   strategyDto.lastSequentialDeficitCount,
                                   strategyDto.tradeDays,
                                   strategyDto.maxShortPositionTime,
                                   strategyDto.yieldOfMonthStandardDeviation,
                                   strategyDto.sharpeIndex,
                                   strategyDto.movingCost);
   }
    public static int updateStategyStatus(StrategyDto.StrategyStatus status,String suuid){
        String sql = SqlLoader.getSqlById("updateStategyStatus");
       return  QicDbUtil.updateQicDB(sql,status.value,suuid);
    }
    public static int updateStategyStatusbyId(StrategyDto.StrategyStatus status,String id){
        String sql = SqlLoader.getSqlById("updateStategyStatusbyId");
       return  QicDbUtil.updateQicDB(sql,status.value,id);
    }
    // 策略延时下架
    public static int StraDownDelayed(String[] stIds, StrategyDownTaskContextDto tastContext, String StrategyDownSql,
                        Date setTime,Date currentTime,String getOrderedUserSql,String sendUserMessageSql,
                        String message){
        final Long SPACING_INTERVAL =7*24*60*60*1000L;//间隔时间
        String setScdulingTaskSql = SqlLoader.getSqlById("setScdulingTaskInfo");
        for (int i = 0; i < stIds.length; i++) {
            //设置策略状态为待下架
            QicDbUtil.updateQicDB(StrategyDownSql, setTime, StrategyDto.StrategyStatus.WAITINGUPSHELF.value, Long.parseLong(stIds[i]));
        }

        Gson gson = new Gson();
        String contextJsonString = gson.toJson(tastContext);
        if(setTime.getTime()-currentTime.getTime()<=SPACING_INTERVAL){//设置时间-当前时间小于7天 马上发下架通知给用户表
            Date orderDate = new Date(setTime.getTime()-SPACING_INTERVAL);
                for (int i = 0; i < stIds.length; i++) {
                    try {
                        List<UserStrategyOrderDto> list = QicDbUtil.queryQicDBBeanList(getOrderedUserSql, UserStrategyOrderDto.class, Long.parseLong(stIds[i]),orderDate);
                        StrategyBaseinfo strategy = StrategyService.findStrategyById(Long.parseLong(stIds[i]));//组装用户消息
                        MessageBuilder messageBuilder = new MessageBuilder(message);
                        messageBuilder.addParameter("strategy", strategy);//下架通知模板 【$strategy.name】
                        String  inputVal = messageBuilder.execute();
                        for (UserStrategyOrderDto userStrategyOrderDto : list) {
                            QicDbUtil.updateQicDB(sendUserMessageSql, userStrategyOrderDto.uid, inputVal);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            QicDbUtil.updateQicDB(setScdulingTaskSql, contextJsonString, setTime, "StrategyDownTaskRunner");
        }else{//设置时间-当前时间大于7天
            Date beforeDate = new Date(setTime.getTime()-SPACING_INTERVAL);
            QicDbUtil.updateQicDB(setScdulingTaskSql,contextJsonString,beforeDate,"StrategyDownTaskRunner");//提前7天通知用户
            QicDbUtil.updateQicDB(setScdulingTaskSql,contextJsonString,setTime,"StrategyDownTaskRunner");//在设定日期再次通知用户
        }
        return 1;
    }
    public static StrategyDto findStrategyByUUID(String strUUID){
        String sql = SqlLoader.getSqlById("findStrategyByUUID");
        return QicDbUtil.queryQicDBSingleBean(sql,StrategyDto.class,strUUID);
    }
   public static int  deleteStrategyFromHighLow(String strUUID){
       String sql = SqlLoader.getSqlById("deleteStrategyFromHighLow");
       return QicDbUtil.updateQicDB(sql,strUUID);
   }
}
