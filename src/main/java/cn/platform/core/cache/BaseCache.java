package cn.platform.core.cache;

/**
 * @Description: redis缓存借口
 * @Package: cn.platform.core.cache
 * @ClassName: BaseCache
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/31 17:17
 * @Version: 1.0
 */
public interface BaseCache<T> {

    /**
     * 创建缓存
     *
     * @param key
     * @param val
     */
    void put(String key, T val);

    /**
     * 创建缓存(可设置超时时间)
     *
     * @param key
     * @param val
     * @param expire
     */
    void put(String key, T val, long expire);

    /**
     * 修改缓存
     *
     * @param key
     * @param val
     */
    void update(String key, T val);

    /**
     * 修改缓存(可设置超时时间)
     *
     * @param key
     * @param val
     * @param expire
     */
    void update(String key, T val, final long expire);

    /**
     * 获取缓存值
     *
     * @param key
     * @return
     */
    T get(String key);

    /**
     * 是否存在缓存
     *
     * @param key
     * @return
     */
    boolean has(String key);

    /**
     * 删除指定缓存
     *
     * @param key
     */
    void del(String key);


    /**
     * 清除缓存
     */
    void clear();
}
