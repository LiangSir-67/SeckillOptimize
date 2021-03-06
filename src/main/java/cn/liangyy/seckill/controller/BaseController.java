package cn.liangyy.seckill.controller;

import cn.liangyy.seckill.error.BusinessException;
import cn.liangyy.seckill.error.EmBusinessError;
import cn.liangyy.seckill.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义exceptionhandle解决未被controller层吸收的exception
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-10-9:21
 */
public class BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    //@ExceptionHandler(Exception.class)
    //@ResponseStatus(HttpStatus.OK)
    //@ResponseBody
    //public Object handleException(HttpServletRequest request, Exception exception) {
    //    Map<String, Object> responseData = new HashMap<>();
    //    if (exception instanceof BusinessException) {
    //        BusinessException businessException = (BusinessException) exception;
    //        responseData.put("errCode", businessException.getErrCode());
    //        responseData.put("errMsg", businessException.getErrMsg());
    //    } else {
    //        responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
    //        responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
    //    }
    //    return CommonReturnType.create(responseData, "fail");
    //
    //}
}
