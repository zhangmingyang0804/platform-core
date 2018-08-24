package cn.platform.core.property;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Package: cn.platform.core.property
 * @ClassName: PropertyUtils
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:50
 * @Version: 1.0
 */
public class PropertyUtils {
    private static Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

    /**
     * 获取String
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getStringValue(String key, String defaultValue) {
        String val;
        try {
            val = PropertyLoader.getConfigValue(key, defaultValue);
        } catch (Exception e) {
            printWarnLogger(key, defaultValue, e);
            val = defaultValue;
        }
        return val;
    }

    /**
     * 获取int
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntValue(String key, int defaultValue) {
        int val;
        try {
            val = Integer.parseInt(PropertyLoader.getConfigValue(key, null));
        } catch (Exception e) {
            printWarnLogger(key, String.valueOf(defaultValue), e);
            val = defaultValue;
        }
        return val;
    }

    /**
     * 获取long
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLongValue(String key, long defaultValue) {
        long val;
        try {
            val = Long.parseLong(PropertyLoader.getConfigValue(key, null));
        } catch (Exception e) {
            printWarnLogger(key, String.valueOf(defaultValue), e);
            val = defaultValue;
        }
        return val;
    }

    /**
     * 获取Boolean
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanValue(String key, boolean defaultValue) {
        boolean val;
        try {
            val = Boolean.parseBoolean(PropertyLoader.getConfigValue(key, null));
        } catch (Exception e) {
            printWarnLogger(key, String.valueOf(defaultValue), e);
            val = defaultValue;
        }
        return val;
    }

    /**
     * 记录日志
     *
     * @param key
     * @param defaultValue
     * @param throwable
     */
    private static void printWarnLogger(String key, String defaultValue, Throwable throwable) {
        if (logger.isWarnEnabled()) {
            logger.warn("get config value:[{}] failed,return defaultValue:[{}],cause by:", key, defaultValue, throwable);
        }
    }
}
