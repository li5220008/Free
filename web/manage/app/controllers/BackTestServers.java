package controllers;

import bussiness.BackTestService;
import bussiness.LogInfosService;
import dto.BackTestServerDto;
import play.mvc.With;
import util.SystemLoggerMessage;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-20
 * Time: 下午4:34
 * 功能描述: 回测服务器管理
 */
@With({AuthorBaseIntercept.class})
public class BackTestServers  extends BasePlayControllerSupport{
    /**
     * 添加回测服务器
     * @param backTestServerDto
     */
  public  static void addServer(BackTestServerDto backTestServerDto,long uid){

      if(backTestServerDto != null){
          BackTestService.addServer(backTestServerDto);
          LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_ADD_BACKTEST_SERVICE, SystemLoggerMessage.ADD_BACKTEST_SERVICE, SystemLoggerMessage.TYPE);//写入系统日志

      }

  }
    /**
     * 更改策略的回测服务器
     * @param
     */
    public  static void updateStratedyServerId(long uid){

        String strategyId = params.get("suuid");
        long serverId = params.get("sid",Long.class);
        BackTestService.updateStratedyServerId(serverId,strategyId);
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_UPDATE_BACKTEST_SERVICEID, SystemLoggerMessage.UPDATE_BACKTEST_SERVICEID, SystemLoggerMessage.TYPE);//写入系统日志


    }

    public static void updateServerStatus(long uid){
        int status = params.get("status",Integer.class);
        long serverId = params.get("sid",Long.class);
        BackTestService.updateServerStatus(status, serverId);
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_UPDATE_BACKTEST_SERVICESTATUS, SystemLoggerMessage.UPDATE_BACKTEST_SERVICESTATUS, SystemLoggerMessage.TYPE);//写入系统日志

    }

    public static void getServers(int type){
        BackTestServerDto.ServerTypeEnum  serverTypeEnum= null;
        for(BackTestServerDto.ServerTypeEnum typeEnums :BackTestServerDto.ServerTypeEnum.values()){
           if(typeEnums.getValue() == type){
               serverTypeEnum = typeEnums;
               break;
           }
        }
        List<BackTestServerDto> list = BackTestService.findServerByStatusAndType(BackTestServerDto.ServerStatusEnum.VALID,serverTypeEnum);
        renderJSON(list);
    }
}
