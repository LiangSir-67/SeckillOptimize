package cn.liangyy.seckill.service.impl;

import cn.liangyy.seckill.dao.PromoDOMapper;
import cn.liangyy.seckill.dataobject.PromoDO;
import cn.liangyy.seckill.error.BusinessException;
import cn.liangyy.seckill.error.EmBusinessError;
import cn.liangyy.seckill.service.ItemService;
import cn.liangyy.seckill.service.PromoService;
import cn.liangyy.seckill.service.UserService;
import cn.liangyy.seckill.service.model.ItemModel;
import cn.liangyy.seckill.service.model.PromoModel;
import cn.liangyy.seckill.service.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀相关服务实现
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-13-13:58
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDOMapper promoDOMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        // 获取对应商品的秒杀活动信息
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        // dataobject -> model
        PromoModel promoModel = this.convertFromDataObject(promoDO);
        if (promoModel == null) {
            return null;
        }

        // 拿当前时间去判断秒杀活动状态
        if (promoModel.getStartDate().isAfterNow()) {
            // 如果秒杀开始时间比现在时间还晚，说明未开始
            promoModel.setStatus(1);
        } else if (promoModel.getEndDate().isBeforeNow()) {
            // 如果秒杀结束时间比现在时间还早，说明已结束
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }


        return promoModel;
    }

    @Override
    public void publishPromo(Integer promoId) {

        // 通过活动id获取活动
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        if (promoDO.getItemId() == null || promoDO.getItemId().intValue() == 0){
            return;
        }
        ItemModel itemModel = itemService.getItemById(promoDO.getItemId());
        // 将库存同步到redisnei
        redisTemplate.opsForValue().set("promo_item_stock_"+itemModel.getId(), itemModel.getStock());

        // 将大闸的限制数字设到redis内
        redisTemplate.opsForValue().set("promo_door_count_"+promoId, itemModel.getStock().intValue() * 5);
    }

    @Override
    public String generateSecondKillToken(Integer promoId, Integer itemId, Integer userId) {
        // 判断库存是否已售罄，若对应的售罄key存在，则直接返回下单失败
        Boolean hasKeyIsExist = redisTemplate.hasKey("promo_item_stock_invalid_" + itemId);
        if (hasKeyIsExist){
            return null;
        }

        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        PromoModel promoModel = convertFromDataObject(promoDO);
        if (promoModel == null){
            return null;
        }

        // 拿当前时间去判断秒杀活动状态
        if (promoModel.getStartDate().isAfterNow()) {
            // 如果秒杀开始时间比现在时间还晚，说明未开始
            promoModel.setStatus(1);
        } else if (promoModel.getEndDate().isBeforeNow()) {
            // 如果秒杀结束时间比现在时间还早，说明已结束
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        // 判断活动是否正在进行
        if (promoModel.getStatus().intValue() != 2){
            return null;
        }

        // 判断item信息是否存在
        ItemModel itemModel = itemService.getItemByIdInCache(itemId);

        if (itemModel == null) {
            return null;
        }

        // 判断用户是否存在
        UserModel userModel = userService.getUserIdByInCache(userId);

        if (userModel == null) {
            return null;
        }

        // 获取秒杀大闸的count数量
        Long result = redisTemplate.opsForValue().increment("promo_door_count_" + promoId, -1);
        if (result < 0){
            return null;
        }

        // 生成token并且存入redis内并给一个5分钟的有效期
        String token = UUID.randomUUID().toString().replace("-","");
        redisTemplate.opsForValue().set("promo_token_"+promoId+"_userid_"+userId+"_itemid_"+itemId, token);
        redisTemplate.expire("promo_token_"+promoId+"_userid_"+userId+"_itemid_"+itemId,5, TimeUnit.MINUTES);
        return token;
    }

    private PromoModel convertFromDataObject(PromoDO promoDO) {
        if (promoDO == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
