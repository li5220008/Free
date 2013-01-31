package dto;

/**
 * 策略管理查询 参数类
 * User: liangbing
 * Date: 12-12-10
 * Time: 下午5:46
 * To change this template use File | Settings | File Templates.
 */
public class StrategyPar {

    //关键字
    public String keyWords;

    //正方序排序
    public int orderFlag;

    //按对应字段排序
    public String orderSort;

    //表示是已上架,待上架,回收站
    public int flag;
}
