package controllers;


import bussiness.MessagesInfosService;
import dto.UserMessagesDto;
import play.libs.F;
import util.Page;
import utils.SystemResponseMessage;

import java.util.List;

/**
 * 用户消息
 * User: panzhiwei
 * Date: 12-12-25
 * Time: 下午1:38
 * To change this template use File | Settings | File Templates.
 */
public class MessagesInfos extends BasePlayControllerSupport {
    /**
     * 用户消息列表
     * @param pageNo
     */
    public static void msgList(int pageNo,int orderFlag,Long uid){
        F.T2<List<UserMessagesDto>,Page> t2 = MessagesInfosService.MsgList(pageNo,orderFlag,uid);
        List<UserMessagesDto> userMsgList= t2._1;
        Page page = t2._2;
        render(userMsgList,orderFlag, page);
    }

    /**
     * 更新消息状态
     * @param msgId
     */
    public static void updateMsgStatus(Long msgId){
        MessagesInfosService.updateMsgStatus(msgId);
    }

    /**
     * 删除消息
     * @param ids
     * @param uid
     */
    public static void delMsg(String[] ids,Long uid){
        MessagesInfosService.delMsg(ids);
        setMessage(SystemResponseMessage.DEL_MESSAGE_SUCCESS_RSP);
        //LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_DELETE_MESSAGES, SystemLoggerMessage.DELETE_MESSAGES, SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }
}
