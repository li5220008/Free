package bussiness;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.UserMessagesDto;
import play.libs.F;
import util.Page;

import java.util.List;

/**
 * 用户消息
 * User: panzhiwei
 * Date: 12-12-25
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public class MessagesInfosService {
    /**
     * 消息列表
     * @param pageNo
     * @return
     */
    public static F.T2<List<UserMessagesDto>,Page> MsgList(int pageNo,int orderFlag,Long uid){
        String sql = SqlLoader.getSqlById("userMsg");
        List<UserMessagesDto> userMsgList = null;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable  \n");
        Long total = QicDbUtil.queryQicDbCount(coutSql.toString(),uid);
            if(orderFlag == 1){
                sql+= " order by msgTime desc ";
            }else{
                sql+= " order by msgTime asc ";
            }

        Page page = new Page(total.intValue(), pageNo);
        sql += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if(total > 0){
            userMsgList = QicDbUtil.queryQicDBBeanList(sql,UserMessagesDto.class,uid);
        }
        return F.T2(userMsgList,page);
    }

    /**
     * 更新消息状态
     * @param msgId
     */
    public static void updateMsgStatus(Long msgId){
        String sql = SqlLoader.getSqlById("get_msgStatus_byId");
        UserMessagesDto userMessagesDto = QicDbUtil.queryQicDBSingleBean(sql,UserMessagesDto.class,msgId);
        if(userMessagesDto.status == UserMessagesDto.MessagesStatus.UNREAD.value){
            userMessagesDto.status = UserMessagesDto.MessagesStatus.READ.value;
        }
        String sql2 = SqlLoader.getSqlById("update_msgStatus_byId");
        QicDbUtil.updateQicDB(sql2,userMessagesDto.status,msgId);
    }

    /**
     * 删除消息
     * @param ids
     */
     public static void delMsg(String[] ids){
         String sql = SqlLoader.getSqlById("delMsg");
         Object[][] params = new Object[ids.length][1];
         for(int i = 0; i<ids.length; i ++){
             params[i][0] = ids[i];
         }
         QicDbUtil.batchQicDB(sql,params);


     }
}
