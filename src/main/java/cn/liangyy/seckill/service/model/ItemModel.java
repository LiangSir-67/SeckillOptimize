package cn.liangyy.seckill.service.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品领域模型
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-12-9:01
 */
@Data
public class ItemModel implements Serializable {
    private Integer id;

    // 商品名称
    @NotBlank(message = "商品名称不能为空")
    private String title;

    // 商品价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格必须大于0元")
    private BigDecimal price;

    // 商品库存
    @NotNull(message = "库存不能为空")
    private Integer stock;

    // 商品描述
    @NotBlank(message = "商品描述不能为空")
    private String description;

    // 商品销量
    private Integer sales;

    // 商品描述图片的URL
    @NotBlank(message = "商品图片信息不能为空")
    private String imgUrl;

    // 使用聚合模型，如果promoModel不为空，则表示其拥有还未结束的秒杀活动
    private PromoModel promoModel;
}
