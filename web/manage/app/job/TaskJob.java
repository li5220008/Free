package job;

import dbutils.QicDbUtil;
import dbutils.SqlLoader;
import dto.SchedulingTask;
import job.task.TaskRunner;
import play.jobs.Job;
import play.jobs.On;

import java.util.List;


/**
 * User: 刘志，刘泓江
 * Date: 12-12-14
 * Time: 下午2:09
 */
@On("0 0 0 * * ?")
public class TaskJob extends Job {
    @Override
    public void doJob() throws Exception {
        List<SchedulingTask> list = getCanExecuteSchedulingTaskList();
        for(SchedulingTask item : list) {
            executeSchedulingTask(item);
        }
    }

    private List<SchedulingTask> getCanExecuteSchedulingTaskList() {
        String schedulingTaskSql = SqlLoader.getSqlById("getScdulingTaskInfo");
        return QicDbUtil.queryQicDBBeanList(schedulingTaskSql, SchedulingTask.class);
    }

    private void executeSchedulingTask(SchedulingTask task) {
        String changeTaskStatusSql = SqlLoader.getSqlById("changeTaskStatus");
        if(task == null)
            return;
        String safeModuleName = "job.task." + task.module.replace(".", "_");
        TaskRunner taskRunner;
        try{
            taskRunner = (TaskRunner)Class.forName(safeModuleName).newInstance();
        } catch (Exception e) {
            task.status = 4;
            // 异常 将调度任务状态改为执行失败
            QicDbUtil.updateQicDB(changeTaskStatusSql,task.status,task.id);
            return;
        }
        try{
            taskRunner.processParameter(task.parameter);
            taskRunner.execute();
            //将调度任务状态改为已执行
            task.status = 2;
            QicDbUtil.updateQicDB(changeTaskStatusSql, task.status,task.id);
        } catch (Exception e) {
            task.status = 4;
            // 异常 将调度任务状态改为执行失败
            QicDbUtil.updateQicDB(changeTaskStatusSql,task.status,task.id);
            return;
        }
    }
}
