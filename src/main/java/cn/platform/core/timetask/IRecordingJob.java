package cn.platform.core.timetask;

/**
 * @Description: 当记录定时任务每次执行情况时, 实现该接口
 * @Package: cn.platform.core.timetask
 * @ClassName: IRecordingJob
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/15 15:07
 * @Version: 1.0
 */
public interface IRecordingJob {
    /**
     * 记录任务执行信息
     *
     * @param config
     * @throws Exception
     */
    void recordingJob(JobConfig config) throws Exception;
}
