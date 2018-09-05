package cn.platform.core.timetask.center;

import cn.platform.core.exception.PlatformException;
import cn.platform.core.timetask.IExecuteJob;
import cn.platform.core.timetask.IRecordingJob;
import cn.platform.core.timetask.JobConfig;
import cn.platform.core.timetask.ScheduleJobUtils;
import cn.platform.core.timetask.job.ConcurrentJob;
import cn.platform.core.timetask.job.DisConcurrentJob;
import cn.platform.core.util.CollectionUtils;
import cn.platform.core.util.HttpClientUtil;
import cn.platform.core.util.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Package: cn.platform.core.timetask
 * @ClassName: JobCenter
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/15 14:40
 * @Version: 1.0
 */
public class JobCenter implements IJobCenter, ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobCenter.class);

    public static final String CONTEXT_JOBCONFIG = "context_jobConfig";
    public static final String CONTEXT_APPLICATIONCONTEXT = "context_applicationContext";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取定时任务调度器
     *
     * @return
     */
    private Scheduler getScheduler() {
        return (StdScheduler) applicationContext.getBean("schedulerFactoryBean");
    }

    /**
     * 启动定时任务
     *
     * @param config
     * @throws Exception
     */
    @Override
    public void startJob(JobConfig config) throws Exception {
        //配置校验
        startCheckJobConfig(config);

        //IP校验
        executeIpCheck(config);

        //检查定时任务是否启动
        String jobStatus = getJobStatus(config);
        if (null != jobStatus) {
            throw new PlatformException("Start job :[" + config.getName() + "] failed , job always exist , status is " + jobStatus);
        }

        //创建并启动定时任务
        Class<? extends Job> clazz = config.getConcurrent() ? ConcurrentJob.class : DisConcurrentJob.class;
        JobBuilder jobBuilder = JobBuilder.newJob(clazz);
        JobDetail jobDetail = jobBuilder
                .withIdentity(config.getJobName(), config.getJobGroup())
                .build();
        jobDetail.getJobDataMap().put(JobConfig.SCHEDULEJOB_KEY, initContextParam(config));

        //创建触发器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(config.getCronExpression());
        CronTrigger cronTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity(config.getJobName(), config.getJobGroup())
                .withSchedule(scheduleBuilder)
                .build();

        //启动定时任务
        getScheduler().scheduleJob(jobDetail, cronTrigger);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Started job : [{}] successed", config.getName());
        }
    }

    @Override
    public void stopJob(JobConfig config) throws Exception {
        Scheduler scheduler = getScheduler();

        //获取定时任务身份
        JobKey jobKey = JobKey.jobKey(config.getJobName(), config.getJobGroup());

        //停止定时任务
        scheduler.pauseJob(jobKey);

        //从任务工厂删除该定时任务
        scheduler.deleteJob(jobKey);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("stop and delete job:[{}] in factory successed . ", config.getName());
        }
    }

    @Override
    public void refreshJobTriggerCron(JobConfig config) throws Exception {
        //获取定时任务调度器
        Scheduler scheduler = getScheduler();

        //获取当前定时任务触发器
        TriggerKey triggerKey = new TriggerKey(config.getJobName(), config.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        //修改定时任务表达式
        if (null == cronTrigger) {
            throw new PlatformException(config.getName() + " do not exist");
        } else {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(config.getCronExpression());
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, cronTrigger);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("refresh job trigger cron:[{}] successed", config.getName());
            }
        }
    }

    /**
     * 定时任务指定IP拦截
     *
     * @param config
     */
    private void executeIpCheck(JobConfig config) {
        List<String> executeNodes = config.getExecuteNodes();
        boolean isBan = true;
        StringBuilder ipBuilder = new StringBuilder();
        String systemLocalIp = null;
        if (CollectionUtils.isNotEmpty(executeNodes)) {
            systemLocalIp = HttpClientUtil.getSystemLocalAddress();
            if (StringUtils.isEmpty(systemLocalIp)) {
                isBan = true;
            } else {
                for (int i = 0; i < executeNodes.size(); i++) {
                    String node = executeNodes.get(i);
                    ipBuilder.append(node);
                    if (i != executeNodes.size() - 1) {
                        ipBuilder.append(" , ");
                    }
                    if (node.equals(systemLocalIp)) {
                        isBan = false;
                        break;
                    }
                }
            }
        } else {
            isBan = false;
        }

        if (isBan) {
            throw new PlatformException("System local address : [" + systemLocalIp + "] , job: [" + config.getName() + "] allow execute ips : [" + ipBuilder.toString() + "]");
        }
    }

    /**
     * 初始化上下文参数
     *
     * @param config
     * @return
     */
    private HashMap<String, Object> initContextParam(JobConfig config) {
        HashMap<String, Object> contextParam = new HashMap<>();
        contextParam.put(CONTEXT_JOBCONFIG, config);
        contextParam.put(CONTEXT_APPLICATIONCONTEXT, applicationContext);
        return contextParam;
    }

    /**
     * 检查任务配置
     *
     * @param config
     */
    private void startCheckJobConfig(JobConfig config) {
        String baseErrorMessage = "Started job failed , ";

        //非空校验
        if (null == config) {
            throw new PlatformException(baseErrorMessage + "jobConfig is null");
        }

        if (StringUtils.isEmpty(config.getJobGroup())) {
            throw new PlatformException(baseErrorMessage + "jobGroup is null");
        }

        if (StringUtils.isEmpty(config.getJobName())) {
            throw new PlatformException(baseErrorMessage + "jobName is null");
        }

        if (StringUtils.isEmpty(config.getCronExpression())) {
            throw new PlatformException(baseErrorMessage + "cronExpression is null");
        }

        if (StringUtils.isEmpty(config.getSpringId())) {
            throw new PlatformException(baseErrorMessage + "SpringId is null");
        }

        //bean校验
        Object bean = applicationContext.getBean(config.getSpringId());
        if (null == bean || !(bean instanceof IExecuteJob)) {
            throw new PlatformException(baseErrorMessage + config.getSpringId() + " is not springBean or unImplement IExecuteJob interface");
        }

        if (StringUtils.isNotEmpty(config.getRecordingBeanId())) {
            Object recordingBean = applicationContext.getBean(config.getSpringId());
            if (null == recordingBean || !(recordingBean instanceof IRecordingJob)) {
                throw new PlatformException(baseErrorMessage + config.getRecordingBeanId() + " is not springBean or unImplement IRecordingJob interface");
            }
        }
    }

    /**
     * 获取任务状态
     *
     * @param config
     * @return
     * @throws SchedulerException
     */
    private String getJobStatus(JobConfig config) throws SchedulerException {
        //获取调度器
        Scheduler scheduler = getScheduler();
        //获取当前任务控制触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(config.getJobName(), config.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (cronTrigger == null) {
            return null;
        } else {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
            return ScheduleJobUtils.getScheduleJobStatus(triggerState);
        }
    }
}
