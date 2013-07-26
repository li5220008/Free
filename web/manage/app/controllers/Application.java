package controllers;

import models.common.FunctionInfo;
import models.common.StrategyBaseinfo;
import models.common.UserInfo;
import models.common.UserStrategyCollect;
import play.mvc.Controller;
import play.mvc.Util;
import play.test.Fixtures;

import java.util.Date;


public class Application extends Controller {

    public static void index() {
        Users.gotoLogin();
    }

    //加入@Util表示这个方法不是控制器方法, 不做为web入口, 只是一个工具方法
    @Util
    static void functionTest() {
        FunctionInfo f1 = new FunctionInfo();
        f1.name = "第一级";
        f1.action = "aa1";
        f1.code = "01";

        f1.save();

        FunctionInfo f2 = new FunctionInfo();
        f2.name = "第二级1";
        f2.action = "aa21";
        f2.code = "0101";
        f2.parent = f1;
        f2.save();

        FunctionInfo f3 = new FunctionInfo();
        f3.name = "第二级2";
        f3.action = "aa22";
        f3.code = "0102";
        f3.parent = f1;
        f3.save();

        FunctionInfo f4 = new FunctionInfo();
        f4.name = "第三级1";
        f4.action = "aa31";
        f4.code = "010101";
        f4.parent = f2;
        f4.save();
    }

    @Util
    static void userStrategyCollectTest() {
        Fixtures.deleteAllModels(); //这个方法是把所有的model都删除掉

        UserInfo u = new UserInfo();
        u.name = "wenzhihong";
        u.account = "wenzhihong";
        u.pwd = "23";
        u.address = "3223";
        u.email = "wen66@126.com";
        u.sdate = new Date();
        u.edate = new Date();
        u.status = 1;
        u.save();

        StrategyBaseinfo strategy = new StrategyBaseinfo();
        strategy.name = "策略1";
        strategy.tradeType = 1;
        strategy.status = 1;
        strategy.tradeVariety = 1;
        strategy.save();

        UserStrategyCollect usc = new UserStrategyCollect();
        usc.user = u;
        usc.strategy = strategy;
        usc.save();
    }

}