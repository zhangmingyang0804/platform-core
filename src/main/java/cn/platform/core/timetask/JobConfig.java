package cn.platform.core.timetask;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 定时任务配置bean
 * @Package: cn.platform.core.timetask
 * @ClassName: JobConfig
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/15 14:35
 * @Version: 1.0
 */
public class JobConfig implements Serializable {
    public static final String SCHEDULEJOB_KEY = "scheduleJob";//上下文参数取值键

    private String jobGroup;//任务组名称
    private String jobName;//任务名称
    private String description;//描述
    private String cronExpression;//cron表达式
    private Boolean isConcurrent = Boolean.TRUE;//任务是否有状态(默认为有状态)
    private Boolean currentTimeExecuteStatus = Boolean.TRUE;//当前执行执行状态(true-成功,false-失败)
    private String failedMessage;//失败原因
    private Boolean failedIsStop = Boolean.FALSE;//失败是否停止(true-停止)
    private String springId;//spring bean
    private String recordingBeanId;//记录任务执行状态bean
    private List<String> executeNodes;//允许执行节点

    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public List<String> getExecuteNodes() {
        return executeNodes;
    }

    public void setExecuteNodes(List<String> executeNodes) {
        this.executeNodes = executeNodes;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Boolean getConcurrent() {
        return isConcurrent;
    }

    public void setConcurrent(Boolean concurrent) {
        isConcurrent = concurrent;
    }

    public String getSpringId() {
        return springId;
    }

    public void setSpringId(String springId) {
        this.springId = springId;
    }

    public Boolean getCurrentTimeExecuteStatus() {
        return currentTimeExecuteStatus;
    }

    public void setCurrentTimeExecuteStatus(Boolean currentTimeExecuteStatus) {
        this.currentTimeExecuteStatus = currentTimeExecuteStatus;
    }

    public String getFailedMessage() {
        return failedMessage;
    }

    public void setFailedMessage(String failedMessage) {
        this.failedMessage = failedMessage;
    }

    public Boolean getFailedIsStop() {
        return failedIsStop;
    }

    public void setFailedIsStop(Boolean failedIsStop) {
        this.failedIsStop = failedIsStop;
    }

    public String getRecordingBeanId() {
        return recordingBeanId;
    }

    public void setRecordingBeanId(String recordingBeanId) {
        this.recordingBeanId = recordingBeanId;
    }

    /**
     * 包含组名和任务名的
     *
     * @return
     */
    public String getName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(this.getJobGroup())
                .append(".")
                .append(this.getJobName());
        return stringBuilder.toString();
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
