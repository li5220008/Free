package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.ConfigDto;
import util.QicConfigProperties;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-13
 * Time: 下午5:59
 * 功能描述:读取config_manage中的配置
 */
public class SystemConfigService {
    /**
     * 加载配置表
     */
    public static List<ConfigDto> loadConfig(){
        String sql = SqlLoader.getSqlById("loadConfig");
        List<ConfigDto> list = QicDbUtil.queryQicDBBeanList(sql, ConfigDto.class);
        return list;

    }
    public  static boolean updateValueByKey(String key,String value){
        String sql = SqlLoader.getSqlById("updateValueByKey");
        int row = QicDbUtil.updateQicDB(sql,value,key);
        if(row>0){//更新缓存
            QicConfigProperties.set(key,value);
        }
        return QicDbUtil.updateQicDB(sql,value,key)>0;
    }
    public static String get(String key){
        return QicConfigProperties.getString(key);
    }
}
