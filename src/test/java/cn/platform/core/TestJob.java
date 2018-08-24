package cn.platform.core;

import cn.platform.core.timetask.JobConfig;
import cn.platform.core.timetask.center.IJobCenter;
import cn.platform.core.timetask.center.JobCenter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJob {
    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("platformcore-context.xml");

            IJobCenter jobCenter = (IJobCenter)context.getBean("jobCenter");

            JobConfig config = new JobConfig();
            config.setJobGroup("group1");
            config.setJobName("test1");
            config.setCronExpression("*/2 * * * * ?");
            config.setSpringId("demoJob");

            JobConfig config2 = new JobConfig();
            config2.setJobGroup("group1");
            config2.setJobName("test2");
            config2.setCronExpression("*/2 * * * * ?");
            config2.setSpringId("demo2Job");

            jobCenter.startJob(config);
            jobCenter.startJob(config2);

            Thread.sleep(20000);

            config.setCronExpression("*/4 * * * * ?");
            jobCenter.refreshJobTriggerCron(config);

            config2.setCronExpression("*/3 * * * * ?");
            jobCenter.refreshJobTriggerCron(config2);

            Thread.sleep(20000);

            jobCenter.stopJob(config);
            jobCenter.stopJob(config2);

            Thread.sleep(5000);

            jobCenter.stopJob(config);
//            jobCenter.stopJob(config2);

            System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
