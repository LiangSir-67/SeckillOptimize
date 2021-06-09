package cn.liangyy.seckill.service;

import cn.liangyy.seckill.error.BusinessException;
import cn.liangyy.seckill.service.model.OrderModel;

/**
 * 订单相关服务
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-13-10:54
 */
public interface OrderService {

    // 1. 通过前端url上传过来秒杀活动id，然后下单接口内校验对应id是否属于对应商品且活动已开始
    // 2. 直接在下单接口内判断对应的商品是否存在秒杀活动，若存在进行中的则以秒杀价格下单
    // 我们使用 第一种方案
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount,String stockLogId) throws BusinessException;
}
