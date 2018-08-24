package cn.platform.core.timetask.demo;

import cn.platform.core.timetask.IExecuteJob;
import cn.platform.core.timetask.JobConfig;
import cn.platform.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("demoJob")
public class DemoJob implements IExecuteJob {
    private static Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Override
    public void execute(JobConfig config) throws Exception {
        logger.info("demoJob:[{}]", DateUtils.formatDate(new Date(), DateUtils.DateFormat.DATE_FORMAT_4));
    }
}
