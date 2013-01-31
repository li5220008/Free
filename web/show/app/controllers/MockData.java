package controllers;

import dbutils.QicDbUtil;
import dbutils.QicoreDbUtil;
import job.StockpoolExtInfo;
import job.StrategyHighLowJob;
import job.StrategyYieldSmallPicJob;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import play.Logger;
import play.exceptions.DatabaseException;
import play.mvc.Controller;
import play.mvc.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: wenzhihong
 * Date: 12-11-10
 * Time: 上午11:10
 */
public class MockData extends Controller {
    static String[] strategyUUIDs = {"20121101Test",
            "222E0CD6AE5342B596670834CEE6FA4F",
            "23339CB2018143E5918F8216C7BEC554",
            "54156134E0824DDB8E3F26AA3529A136",
            "5FA68737E1404709BB1F638309985366",
            "647481911801428F95D3AC7EDCA73918",
            "72ED1B2B829C49E9A9B581A30FCDF057",
            "C781810735904308800B0E55F1BD2680",
            "K0000005",
            "K0000024"
    };

    /**
     * 模拟生成收益率数据
     */
    public static void mockYieldData()  {
        String delSql = "DELETE FROM qicore.`high_freq_hist_indicator` WHERE RetainedProfits IS NULL";
        try {
            QicoreDbUtil.qicoreQueryRunner.update(QicoreDbUtil.getQicoreDBConnection() ,delSql);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }

        String sql = "INSERT INTO `qicore`.`high_freq_hist_indicator` (`StrategyID`,`Yield`,`UpdateTime`)VALUES (?,?,?)";
        List<Date> datelist = makeDate();

        for (String uuid : strategyUUIDs) {
            Logger.info("处理策略:%s", uuid);
            Object[][] parms = new Object[datelist.size()][3];
            for (int i = 0; i < parms.length; i++) {
                parms[i][0] = uuid;
                parms[i][1] = RandomUtils.nextFloat();
                parms[i][2] = datelist.get(i);
            }
            try {
                QicoreDbUtil.qicoreQueryRunner.batch(QicoreDbUtil.getQicoreDBConnection(), sql, parms);
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage(), e);
            }

        }

        String truncateSql = "TRUNCATE qic_db.`strategy_daily_yield`";
        try {
            QicDbUtil.qicQueryRunner.update(QicDbUtil.getQicDBConnection(), truncateSql);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }

        new StrategyYieldSmallPicJob().now();
    }

    /**
     * 策略超市扩展表 和 股票池扩展表 绩效数据指标 手动更新数据调用方法
     */
    public static void createSmallPic() {
        new StrategyYieldSmallPicJob().now();//生成小图片
       // new StrategyExtInfo().now(); //生成策略超市数据
        new StockpoolExtInfo().now();//生成股票池数据
        new StrategyHighLowJob().now();//生成绩效指标数据
        renderText("生成数据成功!");
    }

    @Util
    static List<Date> makeDate(){
        List<Date> dateList = new ArrayList<Date>(540);
        Date curDate = DateUtils.addDays(new Date(), -10);
        Date sDate = DateUtils.addMonths(new Date(), -3); //三个月前
        while(sDate.before(curDate)){
            dateList.add(sDate);
            sDate = DateUtils.addHours(sDate, 4);

            if(dateList.size() > 1000){
                break;
            }
        }
        return dateList;
    }
}
