package cn.platform.core.timetask.job;

import org.quartz.JobExecutionContext;

/**
 * @Description: 有状态任务(停止时, 将正在执行的任务执行完成)
 * @Package: cn.platform.core.timetask.job
 * @ClassName: ConcurrentJob
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/15 15:44
 * @Version: 1.0
 */
public class ConcurrentJob extends AbstractJob {
    @Override
    public void execute(JobExecutionContext context) {
            executeJob(context);
    }
}
