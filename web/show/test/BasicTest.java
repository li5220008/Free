import dbutils.QicoreDbUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import play.db.DB;
import play.db.DBConfig;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);

        String sql = "SELECT * FROM qic_db.strategy_baseinfo a WHERE a.name LIKE ? AND a.type = 1";
        List<Map<String, Object>> maps = QicoreDbUtil.queryQicoreDBMapList(sql, "%策略%");
        assertEquals(2, maps.size());
    }

}
