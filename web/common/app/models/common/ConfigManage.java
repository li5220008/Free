package models.common;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 配置管理
 * User: liangbing
 * Date: 12-12-12
 * Time: 下午1:50
 */
@Entity
@Table(name = "config_manage")
public class ConfigManage extends Model {

    @Column(name = "key_name")
    public String keyName;
    @Column(name = "key_value")
    public String keyValue;

    public String remark;

}
