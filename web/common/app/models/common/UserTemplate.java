package models.common;

import play.db.jpa.Model;
import util.JPAHibernateUtil;

import javax.persistence.*;
import java.util.List;

/**
 * 用户自定义模板
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午10:44
 */
@Entity
@Table(name = "user_template")
public class UserTemplate extends Model {
    public String name;

    public Integer type; //1. 自定义策略查询 2. 自定义股票池查询

    public String content; //保存的内容,只提供存储,里面的内容自定义

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    public UserInfo user;


    /**
     * 是否有重名. 有的话返回true, 没有则返回false
     * @param uid  用户id
     * @param type 1. 策略 2. 股票池
     * @return
     */
    public static boolean hasSameName(Long uid, int type, String name){
        UserInfo user = JPAHibernateUtil.getReference(UserInfo.class, uid);
        long count = UserTemplate.count("byUserAndTypeAndName", user, type, name);
        return count > 0;
    }

    /**
     * 返回用户已定义的模板
     * @param uid
     * @param type 1. 策略 2. 股票池
     * @return  返回的UserTemplate包含属性: id, name, type, content
     */
    public static List<UserTemplate> fetchUserSearchCond(Long uid, int type){
        UserInfo user = JPAHibernateUtil.getReference(UserInfo.class, uid);
        List<UserTemplate> templateList = UserTemplate.find("byUserAndTypeOrderByNameDesc", user, type).fetch(20);
        return templateList;
    }

    @Override
    public String toString() {
        return "UserTemplate{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
