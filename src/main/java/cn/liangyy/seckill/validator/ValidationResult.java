package cn.liangyy.seckill.validator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一校验结果返回
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-11-11:15
 */
@Data
public class ValidationResult {
    // 校验结果是否有错
    private boolean hasErrors = false;

    // 存放错误信息的map
    private Map<String, String> errorMsgMap = new HashMap<>();

    public boolean isHasErrors() {
        return hasErrors;
    }

    // 实现通用的通过格式化字符串信息获取错误结果的msg方法
    public String getErrorMsg() {
        return StringUtils.join(errorMsgMap.values().toArray());
    }
}
