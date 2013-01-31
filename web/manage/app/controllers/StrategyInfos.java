package controllers;

import annotation.QicFunction;
import bussiness.*;
import dto.StrategyDto;
import dto.StrategyPar;
import dto.UserInfoDto;
import models.common.StrategyBaseinfo;
import play.Logger;
import play.libs.F;
import play.libs.WS;
import play.mvc.With;
import util.Constants;
import util.Page;
import util.SystemLoggerMessage;
import util.SystemResponseMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略列表
 * User: liangbing
 * Date: 12-12-10
 * Time: 下午1:44
 * 策略列表展示和搜索
 */
@With({AuthorBaseIntercept.class})
public class StrategyInfos extends BasePlayControllerSupport {

    private static final int gflag = 1;//已上架策略tab
    private static final int wflag = 2;//待上架策略tab
    private static final int rflag = 3;//回收站tab


    /**
     * 已上架策略列表
     * @param sp     参数类
     * @param pageNo 当前页
     */
    @QicFunction(id=23)
    public static void list(StrategyPar sp, int pageNo) {
        if (sp == null) {
            sp = new StrategyPar();
        }
        sp.flag = gflag;
        F.T2<List<StrategyDto>, Page> t2 = StrategyService.groundingList(sp, pageNo);
        List<StrategyDto> gdList = t2._1;
        Page page = t2._2;
        render( gdList, page, sp);
    }

    /**
     * 查询待上架策略
     * @param sp     参数类
     * @param pageNo 当前页
     */
    @QicFunction(id=23)
    public static void grounding(StrategyPar sp, int pageNo) {
        if (sp == null) {
            sp = new StrategyPar();
        }
        sp.flag = wflag;
        F.T2<List<StrategyDto>, Page> t2 = StrategyService.waitList(sp, pageNo);
        List<StrategyDto> waitList = t2._1;
        Page page = t2._2;
        //查当前登入用户  (add by liujl 2012-12-20,用于策略上传的时候将当前用户显示为策略提供者)
        UserInfoDto userInfoDto = UserInfoService.findUserInfoById(params.get("uid",Long.class));
        render("@list", waitList, page, sp,userInfoDto);

    }

    /**
     * 策略回收站
     * @param sp     参数类
     * @param pageNo 当前页
     */
    @QicFunction(id=23)
    public static void retrieve(StrategyPar sp, int pageNo) {
        if (sp == null) {
            sp = new StrategyPar();
        }
        sp.flag = rflag;
        F.T2<List<StrategyDto>, Page> t2 = StrategyService.retrieveList(sp, pageNo);
        List<StrategyDto> retrieveList = t2._1;
        Page page = t2._2;
        render("@list", retrieveList, page, sp);
    }


    /**
     * 加入回测
     * @Author 潘志威
     *  2012-12-21 modify liujl
     * @param ids
     */
    @QicFunction(id=23)
    public static void addLookback(String ids[], int sid,long uid) {
       //StrategyService.addLookback(ids, status);
        int[] effectRows = BackTestService.updateStratedyServerIdByIntId(sid,ids);
        if(effectRows.length == 0){
            setSuccessFlag(false);
            setMessage("加入失败，只有回测失败或审核中的策略才能回测");
        }
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_BACK_TEST, SystemLoggerMessage.BACK_TEST, SystemLoggerMessage.TYPE);//写入系统日志
        renderJSON(getSampleResponseMap());
    }

    /**
     * 删除策略
     * @Author 潘志威
     *
     * @param ids
     */
    @QicFunction(id=23)
    public static void delStrategy(String ids[],long uid) {
        StrategyService.delstrategy(ids);
        LogInfosService.writeSystemLog(uid,SystemLoggerMessage.DO_DELETE_STRATEGY,SystemLoggerMessage.DELETE_STRATEGY,SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }

    /**
     * 策略清空
     * @Author 潘志威
     */
    @QicFunction(id=23)
    public static void emptyStrategy(long uid) {
        StrategyService.emptystrategy();
        LogInfosService.writeSystemLog(uid,SystemLoggerMessage.DO_EMPTY_STRATEGY,SystemLoggerMessage.EMPTY_STRATEGY,SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }


    /**
     * 上传完策略文件后-----添加策略
     *
     * @param strategyDto
     * @Author 刘建力
     */
    @QicFunction(id=23)
    public static void addStrategy(StrategyDto strategyDto) {
        //先拷备文件到正式目录,再添加

        String filePath = params.get("files");
        long uid = params.get("uid",Long.class);
        try {
            setSuccessFlag(StrategyService.insertStrategy(strategyDto, filePath.split(","),uid));
            setMessage(SystemResponseMessage.STRATEGY_UPLOAD_SUCCESS_RSP);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            setSuccessFlag(false);
        }
        renderJSON(getSampleResponseMap());

    }

    public static void findStrategyByName(String sname) {
        if (StrategyService.findStrategyByName(sname) != null) {
            renderText(false);
        } else {
            renderText(true);
        }
    }


    /**
     * 策略上架
     * @Author 潘志威
     *
     * @param ids
     */
    @QicFunction(id=23)
    public static void upStrategy(String ids[],Long uid,int serverId) {
        String params = "";
        List<StrategyBaseinfo> strategyBaseinfos=  StrategyService.findStrategysByIds(ids);
        for(StrategyBaseinfo strategyBaseinfo :strategyBaseinfos){
            params+=strategyBaseinfo.stUuid+",";
        }
        params = params.substring(0,params.length()-1);
        String url= SystemConfigService.get(Constants.show_createOneStrategyPic_path);
        String result =  WS.url(url,params).get().getString();
        
        if("true".equalsIgnoreCase(result)){//如果图片生成成功,执行上架操作
            boolean flag = StrategyService.upstrategy(ids,serverId);
            if (flag) {
                setMessage(SystemResponseMessage.STRATEGY_UP_RSP);
                setSuccessFlag(true);
            } else {
                setMessage(SystemResponseMessage.STRATEGY_UP_FAILURE_RSP);
                setSuccessFlag(false);
            }
        }else{//图片生成失败,提示用户
            setMessage(SystemResponseMessage.STRATEGY_PIC_FAILURE);
            setSuccessFlag(false);
        }
        LogInfosService.writeSystemLog(uid,SystemLoggerMessage.DO_UP_STRATEGY, SystemLoggerMessage.UP_STRATEGY,SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }

    /**
     * 策略下架
     * @author 刘泓江
     * @param stId 用户选择的策略ID
     * @param setTime 用户设定时间
     * @param textValue 下架消息
     */
    @QicFunction(id=23)
    public static void strategyDownInfo(String stId, Date setTime, String textValue,long uid){
        Map<String,Object> json = new HashMap<String, Object>();
         int iFlag = StrategyService.strategyDown(stId,setTime,textValue,uid);
        if(iFlag==1){
            json.put("flag",true);
            json.put("msg","下架成功");
        }else if(iFlag==2){
            json.put("flag",false);
            json.put("msg","该策略当前有用户订阅，不能下架");
        }else if(iFlag==4){
            json.put("flag",false);
            json.put("msg","请按提示填写下架消息！");
        }else if(iFlag==5){
            json.put("flag",false);
            json.put("msg","待下架状态不允许修改！");
        }else{
            json.put("flag",false);
            json.put("msg","非法操作");
        }
            renderJSON(json);
    }

    /**
     * @author 刘泓江
     * 策略下架通知模板
     */
    @QicFunction(id=23)
    public static void strategyDown() {
        String content = SystemConfigService.get(Constants.STRATEGY_DOWN_TEMPLATE_KEY);
        render(content);
    }

}
