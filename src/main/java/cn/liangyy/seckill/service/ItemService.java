package cn.liangyy.seckill.service;

import cn.liangyy.seckill.error.BusinessException;
import cn.liangyy.seckill.service.model.ItemModel;

import java.util.List;

/**
 * 商品相关服务
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-12-14:43
 */
public interface ItemService {

    // 创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    // 商品列表浏览
    List<ItemModel> listItem();

    // 商品详情浏览
    ItemModel getItemById(Integer id);

    // item及promo model缓存模型
    ItemModel getItemByIdInCache(Integer id);

    // 库存扣减
    boolean decreaseStock(Integer itemId, Integer amount);

    // 库存回滚
    boolean increaseStock(Integer itemId, Integer amount);

    // 异步更新库存
    boolean asyncDecreaseStock(Integer itemId, Integer amount);

    // 商品销量增加
    void increaseSales(Integer itemId, Integer amount);

    // 初始化库存流水
    String initStockLog(Integer itemId, Integer amount);
}
