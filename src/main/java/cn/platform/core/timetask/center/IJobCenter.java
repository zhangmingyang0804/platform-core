package cn.platform.core.timetask.center;

import cn.platform.core.timetask.JobConfig;

/**
 * @Description:
 * @Package: cn.platform.core.timetask
 * @ClassName: IJobCenter
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/15 14:39
 * @Version: 1.0
 */
public interface IJobCenter {
    /**
     * 添加一个定时任务到定时任务工厂,并为该任务添加触发器,执行触发器(发起定时任务)
     *
     * @param config
     * @throws Exception
     */
    void startJob(JobConfig config) throws Exception;

    /**
     * 停止一个定时任务并将该定时任务从定时任务工厂删除
     *
     * @param config
     * @throws Exception
     */
    void stopJob(JobConfig config) throws Exception;

    /**
     * 修改一个触发器表达式
     *
     * @param config
     * @throws Exception
     */
    void refreshJobTriggerCron(JobConfig config) throws Exception;


}
