package dbutils;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.*;
import play.db.DB;
import play.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * qsm数据库操作
 * User: 刘建力
 * Date: 12-12-15
 * Time: 上午11:11
 */
public abstract class QsmDbUtil {
    public static QueryRunner qsmQueryRunner = new QueryRunner();

    //提取数据的数据库在配制中的名称, 对应于application.conf里的 db_名称.url的配制信息. 如果要改动请两个一起改.
    public static final String QSM_DB_CONF_NAME = "qsm";

    //把数据库查询的行处理成 Map
    public static final RowProcessor MAP_ROW_PROCESSOR = new MapRowProcessor();

    //处理一行. 加入处理playframework的model
    public static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor(new BeanProcessorWithModelId());

    /**
     * 返回提取数据的数据库连接
     */
    public static Connection getQsmDBConnection() {
        return DB.getDBConfig(QSM_DB_CONF_NAME, false).getConnection();
    }

    /**
     * 在提取数据的数据库上执行sql. (一般是执行对数据库有更新的那种)
     */
    public static boolean execute4QsmDB(String SQL) {
        return DB.getDBConfig(QSM_DB_CONF_NAME, false).execute(SQL);
    }

    /**
     * 在提取数据的数据库上执行sql语句(查询类)
     */
    public static ResultSet executeQuery4QsmDB(String SQL) {
        return DB.getDBConfig(QSM_DB_CONF_NAME, false).executeQuery(SQL);
    }

    /**
     * 查询sql返回单个bean. 如果没有的话, 返回null
     */
    public static <T> T queryQicoreDBSingleBean(String sql, Class<T> cl, Object... params) {
        Connection conn = getQsmDBConnection();
        ResultSetHandler<T> h = new BeanHandler<T>(cl, ROW_PROCESSOR);
        T t = null;
        try {
            t = qsmQueryRunner.query(conn, sql, h, params);
            return t;
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 查询sql返回Bean list. 如果没有的话, 返回的list长度为0
     */
    public static <T> List<T> queryQsmDBBeanList(String sql, Class<T> cl, Object... params) {
        Connection conn = getQsmDBConnection();
        ResultSetHandler<List<T>> h = new BeanListHandler<T>(cl, ROW_PROCESSOR);
        try {
            return qsmQueryRunner.query(conn, sql, h, params);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 查询单条记录, 转成一个map. 注意, 这里的map的key值为小写的
     */
    public static Map<String, Object> queryQsmDBSingleMap(String sql, Object... params) {
        Connection conn = getQsmDBConnection();
        ResultSetHandler<Map<String, Object>> h = new MapHandler(MAP_ROW_PROCESSOR);
        Map t = null;
        try {
            t = qsmQueryRunner.query(conn, sql, h, params);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
        return t;
    }

    /**
     * 用handler处理查询记录
     *
     * @return
     */
    public static <T> T queryQsmDbWithHandler(String sql, ResultSetHandler<T> rsh, Object... params) {
        Connection conn = getQsmDBConnection();
        try {
            return qsmQueryRunner.query(conn, sql, rsh, params);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 用于count语句的.只查总数
     * @return
     */
    public static Long queryQsmDbCount(String sql, Object... params){
        Connection conn = getQsmDBConnection();
        try{
            return qsmQueryRunner.query(conn, sql, new ScalarHandler<Long>(), params);
        }catch (SQLException ex){
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 查询多条记录, 转成list<map>. 注意, 这里的map的key值为小写的
     * 如果没有,则返回的list长度为0
     */
    public static List<Map<String, Object>> queryQsmDBMapList(String sql, Object... params) {
        Connection conn = getQsmDBConnection();
        ResultSetHandler<List<Map<String, Object>>> h = new MapListHandler(MAP_ROW_PROCESSOR);
        try {
            return qsmQueryRunner.query(conn, sql, h, params);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 批量执行sql语句
     *
     * @param sql
     * @param params 这里是一个二维数组, 第二维记录的是要操作的数据
     * @return 每次受影响的记录条数
     */
    public static int[] batchQsmDB(String sql, Object[][] params) {
        Connection conn = getQsmDBConnection();
        try {
            return qsmQueryRunner.batch(conn, sql, params);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * 可以执行sql语句. insert, update, delete
     *
     * @param sql   sql语句
     * @param param sql语句里的参数
     * @return 受sql语句影响的记录条数
     */
    public static int updateQsmDB(String sql, Object... param) {
        Connection conn = getQsmDBConnection();
        try {
            return qsmQueryRunner.update(conn, sql, param);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
