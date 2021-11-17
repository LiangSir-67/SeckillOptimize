package cn.liangyy.seckill.controller;

import cn.liangyy.seckill.controller.viewobject.ItemVO;
import cn.liangyy.seckill.error.BusinessException;
import cn.liangyy.seckill.response.CommonReturnType;
import cn.liangyy.seckill.service.CacheService;
import cn.liangyy.seckill.service.ItemService;
import cn.liangyy.seckill.service.PromoService;
import cn.liangyy.seckill.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商品控制器 @CrossOrigin 该注解无法做到session共享 allowCredentials = "true", allowedHeaders =
 * "*"可以解决这个问题 @Author: 梁歪歪 <1732178815@qq.com> @Description: blog <liangyy.cn> @Create
 * 2021-05-12-15:17
 */
@CrossOrigin(
    origins = {"http://localhost:8888", "http://localhost:63343"},
    allowCredentials = "true",
    allowedHeaders = "*")
@Controller("/item")
@RequestMapping("/item")
public class ItemController extends BaseController {

  @Autowired private ItemService itemService;

  @Autowired private RedisTemplate redisTemplate;

  @Autowired private CacheService cacheService;

  @Autowired private PromoService promoService;

  /**
   * 创建商品
   *
   * @param title
   * @param price
   * @param stock
   * @param description
   * @param imgUrl
   * @return
   * @throws BusinessException
   */
  @RequestMapping(
      value = "/createitem",
      method = {RequestMethod.POST},
      consumes = {CONTENT_TYPE_FORMED})
  @ResponseBody
  public CommonReturnType createItem(
      @RequestParam(name = "title") String title,
      @RequestParam(name = "price") BigDecimal price,
      @RequestParam(name = "stock") Integer stock,
      @RequestParam(name = "description") String description,
      @RequestParam(name = "imgUrl") String imgUrl)
      throws BusinessException {
    // 封装service请求用来创建商品
    ItemModel itemModel = new ItemModel();
    itemModel.setTitle(title);
    itemModel.setPrice(price);
    itemModel.setStock(stock);
    itemModel.setDescription(description);
    itemModel.setImgUrl(imgUrl);
    ItemModel itemModelForReturn = itemService.createItem(itemModel);
    ItemVO itemVO = convertVOFromModel(itemModelForReturn);
    return CommonReturnType.create(itemVO);
  }

  @RequestMapping(
      value = "/publishpromo",
      method = {RequestMethod.GET})
  @ResponseBody
  public CommonReturnType publishPromo(@RequestParam(name = "id") Integer id) {
    promoService.publishPromo(id);
    return CommonReturnType.create(null);
  }

  /**
   * 商品详情页浏览
   *
   * @param id
   * @return
   */
  @RequestMapping(
      value = "/get",
      method = {RequestMethod.GET})
  @ResponseBody
  public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {
    ItemModel itemModel = null;

    // 先取本地缓存
    itemModel = (ItemModel) cacheService.getFromCommonCache("item_" + id);

    if (itemModel == null) {
      // 根据商品的id到redis内获取
      itemModel = (ItemModel) redisTemplate.opsForValue().get("item_" + id);

      // 若redis内不存在对应的itemModel，则访问下游service
      if (itemModel == null) {
        itemModel = itemService.getItemById(id);
        // 设置itemModel到redis内
        redisTemplate.opsForValue().set("item_" + id, itemModel);
        // 设置缓存失效时间 10分钟
        redisTemplate.expire("item_" + id, 10, TimeUnit.MINUTES);
      }
      // 填充本地缓存
      cacheService.setCommonCache("item_" + id, itemModel);
    }

    ItemVO itemVO = convertVOFromModel(itemModel);
    System.out.println("ItemController-Stock : " + itemVO.getStock());
    return CommonReturnType.create(itemVO);
  }

  /**
   * 商品列表页面浏览
   *
   * @return
   */
  @RequestMapping(
      value = "/list",
      method = {RequestMethod.GET})
  @ResponseBody
  public CommonReturnType listItem() {
    List<ItemModel> itemModelList = itemService.listItem();
    // 使用stream api将list内的itemModel转化为ItemVO
    List<ItemVO> itemVOList =
        itemModelList.stream()
            .map(
                itemModel -> {
                  ItemVO itemVO = this.convertVOFromModel(itemModel);
                  return itemVO;
                })
            .collect(Collectors.toList());
    return CommonReturnType.create(itemVOList);
  }

  private ItemVO convertVOFromModel(ItemModel itemModel) {
    if (itemModel == null) {
      return null;
    }
    ItemVO itemVO = new ItemVO();
    BeanUtils.copyProperties(itemModel, itemVO);
    // 说明有正在进行或即将进行的秒杀活动
    if (itemModel.getPromoModel() != null) {
      itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
      itemVO.setPromoId(itemModel.getPromoModel().getId());
      itemVO.setStartDate(
          itemModel
              .getPromoModel()
              .getStartDate()
              .toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
      itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
    } else {
      itemVO.setPromoStatus(0);
    }
    return itemVO;
  }
}
