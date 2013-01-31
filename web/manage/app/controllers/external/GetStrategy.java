package controllers.external;

import bussiness.BackTestService;
import bussiness.StrategyService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.BackTestServerDto;
import dto.BackTestStrategyDto;
import dto.StrategyDto;
import util.SystemResponseMessage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-20
 * Time: 下午1:32
 * 功能描述:  提供外部调用接口 1. 查询回测策略
 */
public class GetStrategy extends IpInterceptor {
    public static void getBackTestStrategy() {
        //得到客户端ip

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        //  取策略，返回json数据

        List<BackTestStrategyDto> list = BackTestService.findBackTestStrategyByServerId(getServerId());
        jsonMap.put("data", list);
        jsonMap.put("status", BackTestServerDto.ServerStatusEnum.VALID.getValue());
        jsonMap.put("message", SystemResponseMessage.SYSTEM_DEFAULT_MSG_RSP);
        //修改状态回测中
        //BackTestService.updateStrategyStatus(list, StrategyDto.StrategyStatus.BACKTESTING);

        renderJSON(jsonMap);
        //  记录此次拿到的最大id(待分析)
    }

    /**
     * 检查是否仍是“我” 回测
     */
    public static void checkBackTestStatus() {
        String struuid = params.get("sid");
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (struuid == null) {
            jsonMap.put("status", BackTestServerDto.ServerStatusEnum.DISABLED.getValue());
            jsonMap.put("message", "策略ID不能为空");
        } else {
            boolean ret = BackTestService.checkBackTestStatus(getServerId(), struuid);
            //改状态
            BackTestService.updateStrategyStatus(struuid, StrategyDto.StrategyStatus.BACKTESTING);
            jsonMap.put("status", ret ? BackTestServerDto.ServerStatusEnum.VALID.getValue() : BackTestServerDto.ServerStatusEnum.DISABLED.getValue());
            jsonMap.put("message", ret ? SystemResponseMessage.SYSTEM_DEFAULT_MSG_RSP : SystemResponseMessage.SERVER_DISABLED_RSP + ",可能已被其它服务器回测完毕!");
        }
        renderJSON(jsonMap);


    }

    /**
     * 同步回测结果数据,只接收当前状态为回测中的策略
     * {status:0成功 1失败  data:{key1:value1,key2:value2}}
     */
    public static void syncBackTestResult() {

        InputStream inputStream = request.body;
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("status", -1);

        if (inputStream == null) {
            json.put("message", "请求内容为空");
        } else {
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(reader);
            String line = null;
            StringBuffer sb = new StringBuffer();
            try {

                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(br);
                JsonObject jo = jsonElement.getAsJsonObject();

                JsonElement status = jo.get("status");
                JsonElement strategyData = jo.get("data");
                Gson deSerializer = new Gson();
                StrategyDto result = deSerializer.fromJson(strategyData, StrategyDto.class);
                //插入高频低频中间表
                if (result != null && result.strategyId != null) {
                    //检查 如果当前同步来的strategyId状态是否处于回测中，并且策略存在
                    StrategyDto historyDto = StrategyService.findStrategyByUUID(result.strategyId);
                    if (historyDto == null || historyDto.status != StrategyDto.StrategyStatus.BACKTESTING.value) {
                        json.put("status", -1);
                        json.put("message", "同步失败，该策略不存在或已被其它服务器回测");
                    } else {
                        //出现重复的先删除
                        StrategyService.deleteStrategyFromHighLow(result.strategyId);

                        StrategyService.syncBackTestResult(result);
                        //将strategy_baseinfo中的状态改为已回测
                        StrategyDto.StrategyStatus status2 = status.getAsInt() == 0 ? StrategyDto.StrategyStatus.FINISHTEST :
                                StrategyDto.StrategyStatus.BACKTESTINGFAILER;
                        StrategyService.updateStategyStatus(status2, result.strategyId);
                        //插入数据库
                        json.put("status", 0);
                        json.put("message", "同步成功");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                json.put("message", "同步失败,读取出错");
            }
        }
        renderJSON(json);

    }

    public static void noticeTestFailure() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("status", -1);
        json.put("message","通知失败");
        try {
            int effectRow = BackTestService.rollBackAfterTestFailure(getServerId());
            json.put("status",0);
            if (effectRow > 0) {
                json.put("message","通知成功");
            }else{
                json.put("message","通知失败，没有符合条件的策略");
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
        renderJSON(json);
    }
}
