package cn.liangyy.seckill.controller.viewobject;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品及活动商品展示模型
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-12-15:17
 */
@Data
public class ItemVO {
    private Integer id;

    // 商品名称
    private String title;

    // 商品价格
    private BigDecimal price;

    // 商品库存
    private Integer stock;

    // 商品描述
    private String description;

    // 商品销量
    private Integer sales;

    // 商品描述图片的URL
    private String imgUrl;

    // -----------------

    // 商品秒杀活动的状态 0-没有秒杀活动，1-表示秒杀活动待开始，2-表示秒杀活动进行中
    private Integer promoStatus;

    // 秒杀活动价格
    private BigDecimal promoPrice;

    // 秒杀活动ID
    private Integer promoId;

    // 秒杀活动开始时间
    private String startDate;
}
