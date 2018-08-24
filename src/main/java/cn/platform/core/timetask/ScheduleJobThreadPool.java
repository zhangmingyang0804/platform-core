package cn.platform.core.timetask;

import org.quartz.simpl.SimpleThreadPool;


/**
 * @Description:
 * @Package: cn.inbs.blockchain.common.schedulejob
 * @ClassName: ScheduleJobThreadPool.java
 * @Date: 2018/6/20 14:08
 * @author: create by zhangmingyang
 **/
public class ScheduleJobThreadPool extends SimpleThreadPool {

    @Override
    public void setThreadCount(int count) {
        super.setThreadCount(20);
    }
}
