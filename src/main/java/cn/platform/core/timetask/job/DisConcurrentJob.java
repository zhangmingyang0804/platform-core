package cn.platform.core.timetask.job;

import cn.platform.core.timetask.JobConfig;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * @Description: 无状态任务, 终止正在执行的任务
 * @Package: cn.platform.core.timetask.job
 * @ClassName: DisConcurrentJob
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/15 15:46
 * @Version: 1.0
 */
@DisallowConcurrentExecution
public class DisConcurrentJob extends AbstractJob {
    @Override
    public void execute(JobExecutionContext context) {
        JobConfig config = (JobConfig) context.getMergedJobDataMap().get(JobConfig.SCHEDULEJOB_KEY);
        executeJob(config);
    }
}
