package cn.liangyy.seckill.controller.viewobject;

import lombok.Data;

/**
 * 用户展示层模型
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-09-15:54
 */
@Data
public class UserVO {
    private Integer id;
    private String name;
    private Byte gender;
    private Integer age;
    private String Telphone;
}
