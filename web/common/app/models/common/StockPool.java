package models.common;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 股票池基本信息. (注:这张表来源于外部数据库, 对我们来说只能是读.)
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午11:46
 */
//TODO 这里特别要注意的是 schema 的附值, 这里的值是别的数据库的名称
@Entity
@Table(name = "stock_pool", schema = "STP_StockPool")
public class StockPool extends Model {
    public String StockPoolName;
}
