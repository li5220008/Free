package models.common;

import play.db.jpa.Model;

import javax.persistence.*;

/**
 * 功能点
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午9:59
 */
@Entity
@Table(name = "function_info")
public class FunctionInfo extends Model {
    public String name;

    public String action;

    public String code;

    @ManyToOne
    @JoinColumn(name = "pid")
    //用不上
    public FunctionInfo parent;

    @Transient
    public long fpid;
}
