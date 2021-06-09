package cn.liangyy.seckill.limit.anno;

import java.lang.annotation.*;

/**
 * 限流
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-30-10:00
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RateLimit {
    /**
     * 默认每秒放入桶中的token
     * @return
     */
    double limitNum() default 20.0;
}
