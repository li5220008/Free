package controllers;

import job.StrategyYieldSmallPicJob;
import play.data.binding.As;
import play.mvc.Controller;

import java.util.List;

/**
 * 生成指定策略ID数据和图片
 * User: liangbing
 * Date: 13-1-16
 * Time: 上午10:10
 */
public class CreateOneStrategyPic extends Controller {

    /**
     * 生成图片和画图数据
     *
     * @param strategyId 策略ID
     */
    public static void doRun(@As(",") List<String> strategyId) {
        if (strategyId != null && strategyId.size() > 0) {
            try {
                StrategyYieldSmallPicJob.calcYieldDataAndDrawPic(strategyId);
                renderText(true);
            } catch (Exception e) {
                e.printStackTrace();
                renderText(false);
            }
        }
        render(false);

    }
}
