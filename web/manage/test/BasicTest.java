import models.common.UserInfo;
import org.junit.*;

import java.util.*;

import play.test.*;

public class BasicTest extends UnitTest {

    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void userTemplateTest() throws Exception {
        UserInfo.deleteAll();
        UserInfo u = new UserInfo();
        u.name = "wenzhi";
        u.account = "wenh";
        u.sdate = new Date();
        u.edate = new Date();
        u.email = "wen@126.com";
        u.status = 1;
        u.save();
        UserInfo u2 = UserInfo.findById(u.id);
        assertNotNull(u2);
    }
}
