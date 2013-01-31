package models.common;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户股票池收藏
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 下午12:11
 */
@Entity
@Table(name = "user_stock_pool_collect")
public class UserStockPoolCollect extends Model {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    public UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spid")
    public StockPool stockPool;

    @Column(name = "collect_time")
    public Date collectTime; //可以不附值, 插入数据库时会自动生成
}