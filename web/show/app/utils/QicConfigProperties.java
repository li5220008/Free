package utils;

import bussiness.SystemConfigService;
import dto.ConfigDto;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-11
 * Time: 下午1:41
 * 功能描述: 初始化一些配置,比如文件上传的路径，数据始化在load里面做,启动时用@OnApplicationStart标识下
 */
public class QicConfigProperties extends AbstractConfigProperties {
    private static QicConfigProperties config = new QicConfigProperties();

    private QicConfigProperties(){
    }
    //初始化一些配置,
    public   void load(){
        //这里的key可能是放在数据库配置表的
        System.out.println("start load system config=============");
        List<ConfigDto> list = SystemConfigService.loadConfig();
        for(ConfigDto configer: list){
            map.put(configer.key,configer.value);
        }
     /*  map.put(Constants.USER_EXCEL_TEMPLATE_KEY,"public/attachment/template.xls");
       map.put(Constants.USER_EXCEL_UPLOAD_TEMP_DIR,"/data/excel/tmp/");
       map.put(Constants.USER_EXCEL_UPLOAD_OFFICIAL_DIR,"/data/excel/official/");
       map.put(Constants.STRATEGY_UPLOAD_TEMP_DIR,"/data/strategy/tmp/");
       map.put(Constants.STRATEGY_UPLOAD_OFFICIAL_DIR,"/data/strategy/official/");*/
        System.out.println("finsh load system config=============");
    }
    public static QicConfigProperties getInstance(){
        return config;
    }
}
