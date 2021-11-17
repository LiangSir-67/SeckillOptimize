package cn.liangyy.seckill.controller;

import cn.liangyy.seckill.limit.anno.RateLimit;
import cn.liangyy.seckill.response.CommonReturnType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用来测试限流（使用RateLimit AOP方式实现防黄牛技术） @Author: 梁歪歪 <1732178815@qq.com> @Description: blog
 * <liangyy.cn> @Create 2021-05-30-10:30
 */
@CrossOrigin(
    origins = {"http://localhost:8888", "http://localhost:63343"},
    allowCredentials = "true",
    allowedHeaders = "*")
@Controller("test")
@RequestMapping("/test")
public class TestController {

  @RequestMapping(
      value = "/limittest",
      method = {RequestMethod.GET})
  @ResponseBody
  @RateLimit(limitNum = 2.0)
  public CommonReturnType limitTest() {
    return CommonReturnType.create("成功调用了方法");
  }
}
