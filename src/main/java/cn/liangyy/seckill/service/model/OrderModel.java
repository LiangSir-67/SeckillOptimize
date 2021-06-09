package cn.liangyy.seckill.service.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户下单的交易模型
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-13-10:15
 */
@Data
public class OrderModel implements Serializable {
    // 2021051300012828
    private String id;

    // 购买的用户id
    private Integer userId;

    // 购买的商品id
    private Integer itemId;

    // 若非空，则表示是以秒杀商品方式下单
    private Integer promoId;

    // 购买商品时的单价，若promoId非空，则表示秒杀商品价格
    private BigDecimal itemPrice;

    // 购买数量
    private Integer amount;

    // 购买金额，若promoId非空，则表示秒杀商品价格
    private BigDecimal orderPrice;
}
