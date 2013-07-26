package models.common;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 营业部信息
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午9:35
 */
@Entity
@Table(name = "sale_department")
public class SaleDepartment extends Model{
    public String name;
}
