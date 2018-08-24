package cn.platform.core.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description:
 * @Package: cn.platform.core.property
 * @ClassName: PropertyLoader
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:50
 * @Version: 1.0
 */
public class PropertyLoader {
    private static Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    private static Properties properties;//配置存储类
    private static Resource[] resources;//配置文件资源

    /**
     * spring注入提供构造方法
     *
     * @param resources 文件资源
     */
    private PropertyLoader(Resource[] resources) {
        PropertyLoader.resources = resources;
        loadProperty();
    }

    /**
     * 提供过去单例方法
     *
     * @return
     */
    public static synchronized PropertyLoader getInstance() {
        return new PropertyLoader(resources);
    }

    /**
     * 配置加载方法
     */
    private void loadProperty() {
        properties = new Properties();
        for (Resource resource : resources) {
            InputStream inputStream = null;
            try {
                inputStream = resource.getInputStream();
                properties.load(inputStream);
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Could not load property file from path:[{}],cause by:", resource.getFilename(), e);
                }
                throw new IllegalArgumentException(e);
            } finally {
                try {
                    if (null != inputStream) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("close InputStream failed , cause by :", e);
                    }
                }
            }
        }
    }

    /**
     * 获取配置值
     *
     * @param configKey    取值键
     * @param defaultValue 默认值
     * @return
     */
    public static String getConfigValue(String configKey, String defaultValue) {
        getInstance().loadProperty();
        return properties.getProperty(configKey, defaultValue).trim();
    }
}
