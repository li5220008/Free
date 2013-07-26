package models.common;

import dbutils.QicDbUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.db.jpa.Model;
import play.libs.Crypto;
import play.libs.F;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户信息
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午9:29
 */
@Entity
@Table(name = "user_info")
public class UserInfo extends Model {

    public String name;

    public String account; //帐号

    public String pwd;

    /**
     * 存放的是加密的
     */
    public void setPwd(String pwd) {
        this.pwd = Crypto.passwordHash(pwd);
    }

    public String phone;

    public String email;

    public String idcard;

    @Column(name = "capital_account")
    public String capitalAccount;

    public String address;

    public String post;

    public Date sdate; //启用时间

    public Date edate; //结束时间

    @Column(name = "apply_date")
    public Date applyDate; //申请时间

    public Integer status; //状态

    @Enumerated(EnumType.ORDINAL)
    public Integer utype; //用户类型. 1. 营业部用户, 2. 系统用户

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_dep")
    public SaleDepartment saleDep; //营业部


    @ManyToMany
    @JoinTable(name = "user_role", //关联表名
            joinColumns = @JoinColumn(name = "uid"), //关系维护端的外键
            inverseJoinColumns = @JoinColumn(name = "rid") //关系被维护端的外键
    )
    @Fetch(FetchMode.SUBSELECT)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<RoleInfo> roles;

    /**
     * 认证, _1 为 用户id. _2为操作信息
     * 如果
     */
    public static F.T2 auth(String name, String pwd) {
        UserInfo u = find("byAccount", name).first();
        if(u == null || u.status ==UserStatus.DELETED.value ){
            return F.T2(null, "没有找到用户或用户已被删除");
        }else{
            String pwdCry = Crypto.passwordHash(pwd);
            String message = "true";
            if(u.status <= UserStatus.WITHOUTACTIVITY.value){
                return F.T2(null,u.status ==UserStatus.WITHOUTACTIVITY.value? "用户未激活":"该用户已被禁用");
            }
            if(!pwdCry.equals(u.pwd)){//验证密码
                return F.T2(null, "密码不对");
            }
            if(u.edate.before(new Date())){//验证日期
                //过期了
                return F.T2(null,"账号已过期");
            }
            List list = getUserFunctionIds(u.id);
            if(list == null || list.size()==0){//验证是否授权
                return F.T2(null,"账号未授权");
            }
           //验证通过了
            return F.T2(u.id, "true");

        }
    }
    //缓存 后期优化重点
    public static List<FunctionInfo> getUserFunctionIds(long uid){
        String sql = " SELECT\n" +
                "                          distinct fi.id AS id,\n" +
                "                          fi.pid AS fpid\n" +
                "                    FROM\n" +
                "                          user_role ur\n" +
                "                    LEFT JOIN\n" +
                "                          role_function rf\n" +
                "                    ON\n" +
                "                          ur.rid=rf.rid\n" +
                "                    LEFT JOIN\n" +
                "                          function_info fi\n" +
                "                    ON\n" +
                "                          rf.fid=fi.id\n" +
                "                    WHERE\n" +
                "                          ur.uid=? and fi.name<>''";
        return QicDbUtil.queryQicDBBeanList(sql, FunctionInfo.class, uid);
    }
    public enum UserStatus{
        WITHOUTACTIVITY(2),//未激活
        ACTIVIY(10),//正常
        DISABLED(1),//禁用
        DELETED(-100);//软删除的
        UserStatus(int value){
            this.value = value;
        }
        public int value;
    }
}
