package cn.platform.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Description:
 * @Package: cn.platform.core.util
 * @ClassName: SpringContextUtil
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:52
 * @Version: 1.0
 */
public class SpringContextUtil implements ApplicationContextAware {
    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法。设置上下文环境
     *
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name
     * @return Object
     * @throws BeansException
     */
    public static Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (BeansException e) {
            return null;
        }
    }

    public SpringContextUtil() {
    }
}
