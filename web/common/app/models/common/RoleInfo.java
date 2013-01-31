package models.common;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Set;

/**
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午9:44
 */
@Entity
@Table(name = "role_info")
public class RoleInfo extends Model {
    public String name;
    public String desp;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_function",
        joinColumns = @JoinColumn(name = "rid"),
        inverseJoinColumns = @JoinColumn(name = "fid")
    )
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<FunctionInfo> functions;
}
