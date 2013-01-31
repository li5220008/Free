package job;

import dbutils.QicDbUtil;
import dbutils.QicoreDbUtil;
import dbutils.SqlLoader;
import dto.StrategyDto;
import play.jobs.Job;
import play.jobs.On;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 绩效扩展数据初始化
 * User: liangbing
 * Date: 12-12-20
 * Time: 上午10:27
 */
@On("0 10 0 * * ?")
public class StrategyHighLowJob extends Job {

    @Override
    public void doJob() {
        initData();
    }

    private void initData() {
        System.out.println("--------StrategyHighLowJob 定时任务执行 开始--------"+ new Date());
        String delSql = SqlLoader.getSqlById("delHighLowSql");
        String sql = SqlLoader.getSqlById("strategyHighLowSql");
        List<StrategyDto> sdlist =selectHighLow();
        if(sdlist!=null && sdlist.size()>0){
            Object[][] params = new Object[sdlist.size()][32];
            for(int row = 0;row<sdlist.size();row++){
                StrategyDto strategyDto = sdlist.get(row);
                        params[row][0] =strategyDto.strategyId;
                        params[row][1] = strategyDto.retainedProfits;
                        params[row][2] = strategyDto.yield;
                        params[row][3] = strategyDto.yieldOfMonth;
                        params[row][4] = strategyDto.yieldOfYear;
                        params[row][5] =strategyDto.yieldOfMonthStandardDeviation;
                        params[row][6] =strategyDto.overallProfitability;
                        params[row][7] = strategyDto.overallDeficit;
                        params[row][8] =strategyDto.tradeCount;
                        params[row][9] = strategyDto.profitCount;
                        params[row][10] = strategyDto. deficitCount;
                        params[row][11] = strategyDto.maxSingleProfit;
                        params[row][12] = strategyDto.maxSingleDeficit;
                        params[row][13] =strategyDto.tradeDays;
                        params[row][14] = strategyDto.maxShortPositionTime;
                        params[row][15] = strategyDto.longPositionTradeCount;
                        params[row][16] =strategyDto.shortPositionTradeCount;
                        params[row][17] = strategyDto.positionCloseCount;
                        params[row][18] =strategyDto.maxSequentialProfitCount;
                        params[row][19] =strategyDto. maxSequentialDeficitCount;
                        params[row][20] =strategyDto.profitRatio;
                        params[row][21] =strategyDto.canhsiedRatio;
                        params[row][22] = strategyDto.maxSingleProfitRatio;
                        params[row][23] = strategyDto.maxSingleDeficitRatio;
                        params[row][24] = strategyDto.maxSequentialDeficitCapital;
                        params[row][25] =  strategyDto.sumOfCommission;
                        params[row][26] = strategyDto.grossProfit;
                        params[row][27] = strategyDto.profitLossRatio;
                        params[row][28] =strategyDto.avgProfitOfMonth;
                        params[row][29] = strategyDto. sharpeIndex;
                        params[row][30] =strategyDto. floatingProfitAndLoss;
                        params[row][31] = strategyDto.movingCost;
            }
            //删除里面的数据 按条件批量删除数据
            del(sdlist);

            //  QicDbUtil.execute4QicDB(delSql);
            //插入最新的数据
            QicDbUtil.batchQicDB(sql,params);
            System.out.println("--------StrategyHighLowJob 定时任务执行 结束--------"+ new Date());
        }
    }

    /**
     * 删除已经更新的数据,保存没有更新的数据
     * @param sdlist
     */
    private static void del(List<StrategyDto> sdlist){
        int pageNo = 1;
        int index = 0;
        if(sdlist.size()<30){
            doDel(sdlist.subList(0, sdlist.size()));
        }else{
            for (int i = 0; i <=sdlist.size(); ) {
                doDel(sdlist.subList(i, pageNo * 30));
                index = pageNo * 30;
                pageNo++;
                i += 30;
                if (sdlist.size() - index < 30) {
                    doDel(sdlist.subList(i, sdlist.size()));
                    break;
                }
            }
        }


    }
    private static void doDel(List<StrategyDto> sublist){

        Object[][] params = new Object[sublist.size()][1];
        for(int i = 0;i<sublist.size();i++){
            StrategyDto dto  = sublist.get(i);
            params[i][0] =dto.strategyId;
        }
        String delSql = SqlLoader.getSqlById("delHighLowSql");
        QicDbUtil.batchQicDB(delSql,params);
    }

    /**
     * 查询高低频表数据 合并
     * @return
     */
    public List<StrategyDto> selectHighLow(){
        String highSql = SqlLoader.getSqlById("selecthighSql");
        String lowSql = SqlLoader.getSqlById("selectlowSql");
        List<StrategyDto> highlist = QicoreDbUtil.queryQicoreDBBeanList(highSql,StrategyDto.class);
        List<StrategyDto> lowlist = QicoreDbUtil.queryQicoreDBBeanList(lowSql,StrategyDto.class);
        Map<String,StrategyDto> lowMap = covertListToMap(lowlist);
        for(int i=0;i<highlist.size();i++){
            StrategyDto highDto = highlist.get(i);
            StrategyDto lowDto =lowMap.get(highDto.strategyId);
            if(lowDto != null){
                highDto.yieldOfMonthStandardDeviation=lowDto.yieldOfMonthStandardDeviation;
                highDto.tradeDays=lowDto.tradeDays;
                highDto.maxShortPositionTime=lowDto.maxShortPositionTime;
                highDto.sharpeIndex=lowDto.sharpeIndex;
                highDto.movingCost=lowDto.movingCost;
                lowMap.remove(highDto.strategyId);
            }
        }
        for(StrategyDto dto :lowMap.values()){
            highlist.add(dto);
        }
        return highlist;
    }
    private Map<String,StrategyDto> covertListToMap(List<StrategyDto> lowlist){
        Map<String,StrategyDto> map = new HashMap<String,StrategyDto>();
        for(StrategyDto dto : lowlist){
            map.put(dto.strategyId,dto);
        }
        return  map;
    }

}