package cn.liangyy.seckill.limit.aspect;

import cn.liangyy.seckill.error.EmBusinessError;
import cn.liangyy.seckill.limit.anno.RateLimit;
import cn.liangyy.seckill.response.CommonReturnType;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-30-10:03
 */
@Component
@Scope
@Aspect
public class RateLimitAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    /* 用来存放不同接口的RateLimiter(key为接口名称，value为RateLimiter) */
    private ConcurrentHashMap<String, RateLimiter> map = new ConcurrentHashMap<>();

    private RateLimiter rateLimiter;

    @Pointcut("@annotation(cn.liangyy.seckill.limit.anno.RateLimit)")
    public void serviceLimit() {
    }

    @Around("serviceLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Object obj = null;
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = joinPoint.getTarget();
        //为了获取注解信息
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解信息
        RateLimit annotation = currentMethod.getAnnotation(RateLimit.class);
        //获取注解每秒加入桶中的token
        double limitNum = annotation.limitNum();
        //注解所在方法名区分不同的限流策略
        String functionName = msig.getName();

        //获取rateLimiter
        if(map.containsKey(functionName)){
            rateLimiter = map.get(functionName);
        }else {
            map.put(functionName, RateLimiter.create(limitNum));
            rateLimiter = map.get(functionName);
        }

        try {
            if (rateLimiter.tryAcquire()) {
                //执行方法
                obj = joinPoint.proceed();
            } else {
                //拒绝了请求（服务降级）
                log.info("拒绝了请求---------" );
                return CommonReturnType.create(EmBusinessError.RATELIMIT.getErrMsg(),"fail");

            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }
}
