package models.common;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户策略订阅
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午10:38
 */
@Entity
@Table(name = "user_strategy_order")
public class UserStrategyOrder extends Model {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    public UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stid")
    public StrategyBaseinfo strategy;

    @Column(name = "order_stime")
    public Date orderSTime; //可以不附值, 插入数据库时会自动生成

    @Column(name = "order_etime")
    public Date orderETime;

}
