package cn.liangyy.seckill.service.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户模型
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-09-13:21
 */
@Data
public class UserModel implements Serializable {
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    private String name;

    @NotNull(message = "性别不能不填写")
    private Byte gender;

    @NotNull(message = "性别不能不填写")
    @Min(value = 0, message = "年龄必须大于0岁")
    @Max(value = 150, message = "年龄不能大于150岁")
    private Integer age;

    @NotBlank(message = "手机号不能为空")
    private String telphone;

    private String registerMode;
    private String thirdPartyId;

    @NotBlank(message = "密码不能为空")
    private String encrptPassword;
}
