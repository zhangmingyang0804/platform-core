package cn.platform.core.util;

import java.util.Collection;

/**
 * @Description: 集合工具类
 * @Package: cn.platform.core.util
 * @ClassName: CollectionUtils
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:51
 * @Version: 1.0
 */
public class CollectionUtils {
    /**
     * 集合空判断
     *
     * @param collection 待判断集合
     * @return
     */
    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.size() == 0;
    }

    /**
     * 集合非空判断
     *
     * @param collection 待判断集合
     * @return
     */
    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }
}
