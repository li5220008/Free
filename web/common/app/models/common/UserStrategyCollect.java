package models.common;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户策略收藏
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午10:24
 */
@Entity
@Table(name = "user_strategy_collect")
public class UserStrategyCollect extends Model {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    public UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stid")
    public StrategyBaseinfo strategy;

    @Column(name = "collect_time")
    public Date collectTime; //可以不附值, 插入数据库时会自动生成
}
