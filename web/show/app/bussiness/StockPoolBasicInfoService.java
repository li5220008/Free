package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.StockPoolBasicInfoDto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 股票池基本信息处理类
 * User: liuhongjiang
 * Date: 12-11-19
 * Time: 下午2:13
 */
public class StockPoolBasicInfoService {
    //股票池基本展示
    public static StockPoolBasicInfoDto strategyContrast(String stockPoolCode){
        //根据股票池编码 查询组合股票数，更新频率，组合股票池，组合收益
        String stock_pool_basic_info_sql = SqlLoader.getSqlById("stock_pool_basic_info");

        //根据股票池编码 最近一期的附件下载地址，研报摘要，组合来源
        String stock_pool_download_summary_sql = SqlLoader.getSqlById("stock_pool_download_summary");

        //根据spid 查询股票池组合评级
        String stock_pool_starNum_sql = SqlLoader.getSqlById("stock_pool_starNum");

       //因为股票池基本信息涉及的字段较多且关联复杂，这里分三次查询
        Map<String, Object> basicInfoMap = QicDbUtil.queryQicDBSingleMap(stock_pool_basic_info_sql, stockPoolCode);
        Map<String, Object> downloadSummaryMap = QicDbUtil.queryQicDBSingleMap(stock_pool_download_summary_sql,stockPoolCode);
        Map<String, Object> starNumMap = QicDbUtil.queryQicDBSingleMap(stock_pool_starNum_sql,stockPoolCode);

        StockPoolBasicInfoDto sbi = new StockPoolBasicInfoDto();
        if(basicInfoMap!=null &&basicInfoMap.size()!=0){
            sbi.stockPoolName = (String)basicInfoMap.get("StockPoolName");
            sbi.stockPoolCode =  (Long)basicInfoMap.get("stockPoolCode")==null?0l:(Long)basicInfoMap.get("stockPoolCode");
            sbi.stockNum =  (Long)basicInfoMap.get("stockNum")==null?0l:(Long)basicInfoMap.get("stockNum");
            sbi.updateFrequency = (String)basicInfoMap.get("updateFrequency")==null?"":(String)basicInfoMap.get("updateFrequency");
            sbi.strategy = (String)basicInfoMap.get("strategy")==null?"":(String)basicInfoMap.get("strategy");
            sbi.annualizedYield = (BigDecimal)basicInfoMap.get("AnnualizedYield");
        }

        if(downloadSummaryMap!=null&&downloadSummaryMap.size()!=0){
        sbi.filestoragePath = (String)downloadSummaryMap.get("filestoragePath")==null?"": (String)downloadSummaryMap.get("filestoragePath");
        sbi.summary = (String)downloadSummaryMap.get("summary")==null?"":(String)downloadSummaryMap.get("summary");
        sbi.institutionName = (String)downloadSummaryMap.get("institutionName")==null?"":(String)downloadSummaryMap.get("institutionName");
        }

        if(starNumMap!=null&& starNumMap.size()!=0){
            sbi.starNum = (BigDecimal)starNumMap.get("starNum");
        }
        return sbi;
    }

}
