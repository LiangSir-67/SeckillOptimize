package cn.liangyy.seckill.service.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 秒杀商品模型
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-13-13:16
 */
@Data
public class PromoModel implements Serializable {
    private Integer id;

    // 秒杀活动状态 1-未开始，2-进行中，3-已结束
    private Integer status;

    // 秒杀活动名称
    private String promoName;

    // 秒杀活动的开始时间
    private DateTime startDate;

    // 秒杀活动的结束时间
    private DateTime endDate;

    // 秒杀活动的适用商品
    private Integer itemId;

    // 秒杀活动的商品价格
    private BigDecimal promoItemPrice;
}
