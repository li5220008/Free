package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.BackTestServerDto;
import dto.BackTestStrategyDto;
import dto.StrategyDto;
import models.common.StrategyBaseinfo;
import util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-20
 * Time: 下午1:49
 * 功能描述:  策略回测同外部api交互相关处理方法
 */
public class BackTestService {
    private static List<BackTestServerDto> serverList_cacehe = new CopyOnWriteArrayList<BackTestServerDto>();
    /**
     * 检查此ip是否有权限进行回测，如果有则返回服务器id 其它则返回-1表示无权限
     * @param ip
     * @return
     */
   public static long findServerIdByAddr(String ip){
       String sql = SqlLoader.getSqlById("findServerIdByAddr");
       BackTestServerDto backTestServerDto = QicDbUtil.queryQicDBSingleBean(sql,BackTestServerDto.class,ip);
       if(backTestServerDto == null || backTestServerDto.status == BackTestServerDto.ServerStatusEnum.DISABLED.getValue()){
           return BackTestServerDto.ServerStatusEnum.DISABLED.getValue();
       }
       return backTestServerDto.id;
   }
    public static String findServerIpById(int serverId){
        List<BackTestServerDto> list = findAllServer();
        for(BackTestServerDto backTestServerDto:list){
            if(backTestServerDto.id == serverId)
            return backTestServerDto.ip;
        }
        return null;
    }

    /**
     * 根据服务器id查询待回测列表
     * @param serverId
     * @return
     */
   public static List<BackTestStrategyDto> findBackTestStrategyByServerId(long serverId){
       String sql = SqlLoader.getSqlById("findBackTestStrategyByServerId");
        List<BackTestStrategyDto> list = QicDbUtil.queryQicDBBeanList(sql,BackTestStrategyDto.class,serverId, StrategyDto.StrategyStatus.SANDBOXTESTING.value, StrategyDto.StrategyStatus.BACKTESTING.value);
       if(list!=null && list.size()>0){
           for(BackTestStrategyDto backTestStrategyDto: list){
               System.out.println(SystemConfigService.get(Constants.OTHERS_LOAD_STRATEGY_BASE_DIR));
                    backTestStrategyDto.filePath = (SystemConfigService.get(Constants.OTHERS_LOAD_STRATEGY_BASE_DIR)+ backTestStrategyDto.filePath);
           }
       }

        return list;
   }

    /**
     *供回测服务器开始回测的时候调用
     * @param serverId
     * @param strategyId
     * @return
     */
   public static boolean  checkBackTestStatus(long serverId,String strategyId){
       String sql = SqlLoader.getSqlById("checkBackTestStatus");
       BackTestServerDto backTestServerDto = QicDbUtil.queryQicDBSingleBean(sql,BackTestServerDto.class,serverId,strategyId);
       return backTestServerDto != null;
   }

    /**
     * 更新策略状态   取走策略后要标识回测中
     * @param strategyId
     * @param status
     * @return
     */
   public  static int[] updateStrategyStatus(String strategyId,StrategyDto.StrategyStatus status){
       BackTestStrategyDto bsd = new BackTestStrategyDto();
       bsd.strategyUUid = strategyId;
       List<BackTestStrategyDto> list = new ArrayList<BackTestStrategyDto>(1);
       list.add(bsd);
       return updateStrategyStatus(list,status);
   }
    /**
     * 更新策略状态   取走策略后要标识回测中
     * @param strategyId
     * @param status
     * @return
     */
    public  static int updateStrategyStatus(String[] strategyId,StrategyDto.StrategyStatus status){
        return 0;
    }
    /**
     * 更新策略状态   取走策略后要标识回测中
     * @param list
     * @param status
     * @return
     */
    public  static int[] updateStrategyStatus(List<BackTestStrategyDto> list,StrategyDto.StrategyStatus status){
        if(list == null ||list.size()==0 ){
            return new int[]{0};
        }
        String sql = SqlLoader.getSqlById("updateStrategyStatus");
        Object[][] params = new Object[list.size()][2];
        for(int i = 0;i<list.size();i++){
            BackTestStrategyDto obj = list.get(i);
            params[i][0] = status.value;
            params[i][1] = obj.strategyUUid;

        }
        return QicDbUtil.batchQicDB(sql,params);

    }

    /**
     * 添加回测服务器
     * @param backTestServerDto
     * @return
     */
    public static int addServer(BackTestServerDto backTestServerDto){
        if(backTestServerDto == null){
            return 0;
        }
        String sql = SqlLoader.getSqlById("addServer");
        return QicDbUtil.updateQicDB(sql,backTestServerDto.ip,backTestServerDto.status,backTestServerDto.memo,backTestServerDto.type);
    }

    /**
     * 修改策略所对应的回测服务器ID
     * @param serverId
     * @param suuid  策略人uuid
     * @return
     */
    public static int updateStratedyServerId(long serverId,String suuid){
        String sql = SqlLoader.getSqlById("updateStratedyServerId");
        return QicDbUtil.updateQicDB(sql,serverId,suuid);
    }
    /**
     * 批量修改策略所对应的回测服务器ID
     * @param serverId
     * @param sid 自增长主键id
     * @return
     */
    public static int[] updateStratedyServerIdByIntId(long serverId,String[] sid){
        if(sid == null || sid.length == 0){
            return new int[]{};
        }
        List<StrategyBaseinfo> list =StrategyService.findStrategysByIds(sid);
        for(StrategyBaseinfo tmp : list){
            if(tmp==null||(tmp.status != StrategyDto.StrategyStatus.CHECKING.value && tmp.status != StrategyDto.StrategyStatus.BACKTESTINGFAILER.value ) ){
                return new int[]{};
            }
        }
        Object[][] params = new Object[sid.length][3];
        for(int i =0 ;i<sid.length;i++){
            params[i][0] = serverId;
            params[i][1] = StrategyDto.StrategyStatus.SANDBOXTESTING.value;
            params[i][2] = sid[i];

        }
        String sql = SqlLoader.getSqlById("updateStratedyServerIdByIntId");
      //  updateStrategyPassTime(sid);
        return QicDbUtil.batchQicDB(sql,params);
    }

    /**
     * 更改回测服务器状态
     * @param status
     * @param serverId
     * @return
     */
    public static int updateServerStatus(int status,long serverId){
        String sql = SqlLoader.getSqlById("updateBackTestServerStatus");
        return QicDbUtil.updateQicDB(sql,serverId,status,serverId);
    }

    /**
     * 查询所有的待回测服务器列表
     * @return
     */
    public static List<BackTestServerDto> findAllServer(){
        if(serverList_cacehe == null || serverList_cacehe.size()==0){
            String sql = SqlLoader.getSqlById("findAllServer");
             serverList_cacehe.addAll(QicDbUtil.queryQicDBBeanList(sql,BackTestServerDto.class));
            return serverList_cacehe;
        }
        return serverList_cacehe;
    }

    /**
     * 查询回测服务器列表
     * @param status
     * @return
     */
    public static List<BackTestServerDto> findBackTestServerByStatus(BackTestServerDto.ServerStatusEnum status){
        return findServerByStatusAndType(status,BackTestServerDto.ServerTypeEnum.BACKTEST);
    }
    /**
     * 查询代理服务器列表
     * @param status
     * @return
     */
    public static List<BackTestServerDto> findAgentServerByStatus(BackTestServerDto.ServerStatusEnum status){
        return findServerByStatusAndType(status,BackTestServerDto.ServerTypeEnum.AGENT);
    }

    /**
     * 按服务器状态和类型查找服务器
     * @param status
     * @return
     */
    public static List<BackTestServerDto> findServerByStatusAndType(BackTestServerDto.ServerStatusEnum status,BackTestServerDto.ServerTypeEnum type){
        int value = status.getValue();
        int serverType = type.getValue();
        List<BackTestServerDto> returnList = new ArrayList<BackTestServerDto>();
        for(BackTestServerDto dto :  findAllServer()){
            if(dto.status==value && dto.type == serverType){
                returnList.add(dto);
            }
        }
        return returnList;
    }
    public  static int updateStrategyStatusByServerId(long serverId,StrategyDto.StrategyStatus oriStatus,StrategyDto.StrategyStatus newStatus){
        String sql = SqlLoader.getSqlById("updateStrategyStatusByServerId");
      return QicDbUtil.updateQicDB(sql,newStatus.value,serverId,oriStatus.value);
    }
    //由于一些不可抗因素导致回测失败需将策略状态进行回滚，以便再次回入回测
    public static int rollBackAfterTestFailure(long serverId){
        return updateStrategyStatusByServerId(serverId,StrategyDto.StrategyStatus.BACKTESTING,StrategyDto.StrategyStatus.SANDBOXTESTING);
    }

    /**
     *更新策略的通过时间
     * @param sid
     */
    public static void updateStrategyPassTime(long[] sid){
        if(sid == null || sid.length == 0){
            return ;
        }
        Object[][] params = new Object[sid.length][1];
        for(int i =0 ;i<sid.length;i++){
            params[i][0] = sid[i];
        }
        String sql = SqlLoader.getSqlById("updateStratedyPassTime");
        QicDbUtil.batchQicDB(sql,params);
    }
   //清空缓存列表
    public static void clearCache(){

        if( serverList_cacehe.size()>0){
            serverList_cacehe.clear();
        }
    }

   //public static
}
