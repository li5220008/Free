package models.common;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户股票池评论
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 下午12:14
 */
@Entity
@Table(name = "user_stock_pool_discuss")
public class UserStockPoolDiscuss extends Model {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    public UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spid")
    public StockPool stockPool;

    @Column(name = "dis_time")
    public Date disTime; //可以不附值, 插入数据库时会自动生成

    public Integer star;
}
