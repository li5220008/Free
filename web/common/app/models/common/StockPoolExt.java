package models.common;

import play.db.jpa.Model;

import javax.persistence.*;

/**
 * 股票池属性扩展表
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午11:53
 */
@Entity
@Table(name = "stock_pool_ext")
public class StockPoolExt extends Model {
    @OneToOne
    @JoinColumn(name = "spid")
    public StockPool main;

    @Column(name = "discuss_total")
    public int discussTotal = 0;

    @Column(name = "discuss_count")
    public int discussCount = 0;

    @Column(name = "collect_count")
    public int collectCount = 0;


}
