package cn.liangyy.seckill.service;

import cn.liangyy.seckill.error.BusinessException;
import cn.liangyy.seckill.service.model.UserModel;

/**
 * 用户相关服务
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-09-13:18
 */
public interface UserService {

    // 通过用户ID获取用户对象的方法
    UserModel getUserById(Integer id);

    // 通过缓存获取用户对象模型
    UserModel getUserIdByInCache(Integer id);

    // 用户注册
    void register(UserModel userModel) throws BusinessException;

    /**
     * 用户登录
     * telphone:用户注册手机
     * password:用户加密后的密码
     */

    UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException;
}
