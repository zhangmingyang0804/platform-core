package cn.platform.core.timetask;

/**
 * @Description: 当添加一个定时任务时实现该接口
 * @Package: cn.platform.core.timetask
 * @ClassName: IExecuteJob
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/15 14:53
 * @Version: 1.0
 */
public interface IExecuteJob {
    /**
     * 任务接口
     *
     * @param config
     * @throws Exception
     */
    void execute(JobConfig config) throws Exception;
}
