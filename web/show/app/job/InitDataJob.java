package job;

import dbutils.SqlLoader;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.QicConfigProperties;

/**
 * Created with IntelliJ IDEA.
 * User: liangbing
 * Date: 12-11-7
 * Time: 下午5:10
 * To change this template use File | Settings | File Templates.
 */
@OnApplicationStart
public class InitDataJob extends Job {
    @Override
    public void doJob() throws Exception {
        SqlLoader.init();
        QicConfigProperties.getInstance().load();
    }
}
