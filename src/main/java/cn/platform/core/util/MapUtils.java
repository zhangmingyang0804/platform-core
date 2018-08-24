package cn.platform.core.util;

import java.util.Map;

/**
 * @Description: 工具类
 * @Package: cn.platform.core.util
 * @ClassName: MapUtils
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:52
 * @Version: 1.0
 */
public class MapUtils {
    /**
     * map空判断
     *
     * @param val
     * @return
     */
    public static boolean isEmpty(Map val) {
        return null == val || val.size() == 0;
    }

    /**
     * map非空判断
     *
     * @param val
     * @return
     */
    public static boolean isNotEmpty(Map val) {
        return !isEmpty(val);
    }
}
