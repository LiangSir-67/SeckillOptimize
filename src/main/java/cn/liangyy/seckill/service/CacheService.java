package cn.liangyy.seckill.service;

/**
 * 封装本地缓存操作类
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-25-18:00
 */
public interface CacheService {

    // 存方法
    void setCommonCache(String key, Object value);

    // 取方法
    Object getFromCommonCache(String key);
}
