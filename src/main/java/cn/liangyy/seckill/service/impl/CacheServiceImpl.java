package cn.liangyy.seckill.service.impl;

import cn.liangyy.seckill.service.CacheService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-25-18:01
 */
@Service
public class CacheServiceImpl implements CacheService {

    private Cache<String,Object> commonCache = null;

    /**
     * 在bean加载的时候，优先加载init方法
     */
    @PostConstruct
    public void init(){
        commonCache = CacheBuilder.newBuilder()
                // 设置缓存容器的初始容量为10
                .initialCapacity(10)
                // 设置缓存中最大可以存储100个key，超过100个之后按照LRU的策略移除缓存项
                .maximumSize(100)
                // 设置写缓存后 多少秒过期 1分钟
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key, value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        // 如果存在返回，不存在返回null
        return commonCache.getIfPresent(key);
    }
}
