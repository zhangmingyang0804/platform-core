package cn.platform.core.timetask.job;

import cn.platform.core.timetask.IExecuteJob;
import cn.platform.core.timetask.IRecordingJob;
import cn.platform.core.timetask.JobConfig;
import cn.platform.core.timetask.center.JobCenter;
import cn.platform.core.util.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.HashMap;

/**
 * @Description:
 * @Package: cn.platform.core.timetask
 * @ClassName: AbstractJob
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/15 14:51
 * @Version: 1.0
 */
abstract class AbstractJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(AbstractJob.class);

    /**
     * 调度具体定时任务
     *
     * @param context
     */
    void executeJob(JobExecutionContext context) {
        Object object = context.getMergedJobDataMap().get(JobConfig.SCHEDULEJOB_KEY);
        if (object instanceof HashMap) {
            //获取上下文参数
            HashMap contextParam = (HashMap) object;
            JobConfig config = (JobConfig) contextParam.get(JobCenter.CONTEXT_JOBCONFIG);
            ApplicationContext applicationContext = (ApplicationContext) contextParam.get(JobCenter.CONTEXT_APPLICATIONCONTEXT);

            IExecuteJob job = (IExecuteJob) applicationContext.getBean(config.getSpringId());
            Exception executeException = null;//记录当前次执行异常
            if (null != job) {
                //调度定时任务
                try {
                    job.execute(config);
                } catch (Exception e) {
                    executeException = e;
                    if (logger.isWarnEnabled()) {
                        logger.warn("Execute job : [{}] exception , cause by :", config.getName(), e);
                    }
                    config.setCurrentTimeExecuteStatus(Boolean.FALSE);//失败
                    config.setFailedMessage(e.getMessage());//失败原因
                }

                //记录定时任务执行情况
                if (StringUtils.isNotEmpty(config.getRecordingBeanId())) {
                    IRecordingJob recordingJob = (IRecordingJob) applicationContext.getBean(config.getRecordingBeanId());
                    if (null != recordingJob) {
                        try {
                            recordingJob.recordingJob(config);
                        } catch (Exception e) {
                            //记录任务信息失败时,不做任何操作,输出警告日志
                            if (logger.isWarnEnabled()) {
                                logger.warn("Recording job : [{}] exception , cause by :", config.getName(), e);
                            }
                        }
                    }
                }

                //失败是否继续
                try {
                    if (config.getFailedIsStop() && null != executeException) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Job :[{}] config failedIsStop is true , will stop job .", config.getName());
                        }
                        SchedulerFactoryBean schedulerFactoryBean = (SchedulerFactoryBean) applicationContext.getBean("schedulerFactoryBean");
                        if (null != schedulerFactoryBean) {
                            Scheduler scheduler = schedulerFactoryBean.getScheduler();

                            //获取定时任务身份
                            JobKey jobKey = JobKey.jobKey(config.getJobName(), config.getJobGroup());

                            //停止定时任务
                            scheduler.pauseJob(jobKey);

                            //从任务工厂删除该定时任务
                            scheduler.deleteJob(jobKey);
                            if (logger.isInfoEnabled()) {
                                logger.info("Job : [{}] execute failed , stop job successed .", config.getName());
                            }
                        }
                    }
                } catch (Exception e) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("Job : [{}] execute failed , stop job failed .", config.getName(), e);
                    }
                }
            }
        }
    }
}
