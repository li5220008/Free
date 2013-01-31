package models.common;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * 策略基本信息
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午10:15
 */
@Entity
@Table(name = "strategy_baseinfo")
public class StrategyBaseinfo extends Model {
    @Column(name = "st_uuid")
    public String stUuid; //策略的uuid,用java代码生成, qicore要用到这个值,通过这个值进行关联. 用 play.libs.UUID() 生成 36位的

    public String name;

    //1. 选股型 2. 择时型 3. 交易型 4. 其他
    @Column(name = "trade_type")
    public Integer tradeType;

    @Column(name = "trade_variety")
    public Integer tradeVariety; //投资品种

    @Column(name = "up_time")
    public Date upTime; //上架时间

    @Column(name = "down_time")
    public Date downTime; //下架时间

    public String provider; //策略提供者

    @Column(name = "provider_desp")
    public String providerDesp; //策略提供者的简单描述

    public String desp; //策略简介

    @Column(name = "lookback_stime")
    public Date lookbackStime; //策略回测开始时间

    @Column(name = "lookback_etime")
    public Date lookbackEtime; //策略回测结束时间

    public Integer status;  //策略状态: 1.待审核(也就是上传完成), 2. 沙箱测试  3. 回测中  4. 上架  5 下架

    @Column(name = "discuss_total")
    public int discussTotal = 0; //总评论分(也就是所有的评论总分)

    @Column(name = "discuss_count")
    public int discussCount = 0; //评论人数

    @Column(name = "collect_count")
    public int collectCount = 0; //收藏人数

    @Column(name = "order_count")
    public int orderCount = 0;//订阅人数

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stup_uid")
    public UserInfo upUser;  //策略上传者

}
