package cn.liangyy.seckill.service;

import cn.liangyy.seckill.service.model.PromoModel;

/**
 * 秒杀相关服务
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-13-13:49
 */
public interface PromoService {

    // 根据item获取即将进行的或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);

    // 活动发布
    void publishPromo(Integer promoId);

    // 生成秒杀用的令牌
    String generateSecondKillToken(Integer promoId, Integer itemId, Integer userId);
}
