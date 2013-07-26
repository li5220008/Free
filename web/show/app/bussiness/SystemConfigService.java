package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.ConfigDto;
import utils.QicConfigProperties;

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
    public static String get(String key){
        return QicConfigProperties.getString(key);
    }
}
