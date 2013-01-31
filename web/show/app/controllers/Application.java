package controllers;

import job.StrategyYieldSmallPicJob;
import play.mvc.*;

public class Application extends Controller {

    public static void index() {
        Users.gotoLogin();

        //new StrategyYieldSmallPicJob().now();

        render();
    }

}