package cn.liangyy.seckill.service.impl;

import cn.liangyy.seckill.dao.OrderDOMapper;
import cn.liangyy.seckill.dao.SequenceDOMapper;
import cn.liangyy.seckill.dao.StockLogDOMapper;
import cn.liangyy.seckill.dataobject.OrderDO;
import cn.liangyy.seckill.dataobject.SequenceDO;
import cn.liangyy.seckill.dataobject.StockLogDO;
import cn.liangyy.seckill.error.BusinessException;
import cn.liangyy.seckill.error.EmBusinessError;
import cn.liangyy.seckill.service.ItemService;
import cn.liangyy.seckill.service.OrderService;
import cn.liangyy.seckill.service.UserService;
import cn.liangyy.seckill.service.model.ItemModel;
import cn.liangyy.seckill.service.model.OrderModel;
import cn.liangyy.seckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单相关服务实现
 *
 * @Author: 梁歪歪 <1732178815@qq.com>
 * @Description: blog <liangyy.cn>
 * @Create 2021-05-13-10:55
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Autowired
    private StockLogDOMapper stockLogDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount,String stockLogId) throws BusinessException {
        // 1 校验下单状态,下单的商品是否存在,用户是否合法,购买数量是否正确等
        //ItemModel itemModel = itemService.getItemById(itemId);

        ItemModel itemModel = itemService.getItemByIdInCache(itemId);
        //
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        ////UserModel userModel = userService.getUserById(userId);
        //
        //UserModel userModel = userService.getUserIdByInCache(userId);
        //
        //if (userModel == null) {
        //    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        //}

        if (amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不符合");
        }

        // 校验活动信息
        //if (promoId != null) {
        //    // (1) 校验对应活动是否存在这个适用商品
        //    if (promoId.intValue() != itemModel.getPromoModel().getId()) {
        //        throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不符合");
        //    }
        //    // （2）校验活动是否进行中
        //    else if (itemModel.getPromoModel().getStatus().intValue() != 2) {
        //        throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不符合");
        //    }
        //}

        // 2 落单减库存,支付减库存(这里采用前者)
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        // 3 订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        // 订单金额 = 单价 * 数量
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        // 生成交易流水号,订单号
        orderModel.setId(generateOrderNo());

        OrderDO orderDO = convertFromOrderModel(orderModel);
        int i = orderDOMapper.insertSelective(orderDO);

        // 加上商品销量
        itemService.increaseSales(itemId, amount);

        // 设置库存流水状态为成功
        StockLogDO stockLogDO = stockLogDOMapper.selectByPrimaryKey(stockLogId);
        if (stockLogDO == null){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
        stockLogDO.setStatus(2);
        stockLogDOMapper.updateByPrimaryKeySelective(stockLogDO);

        //TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
        //    @Override
        //    public void afterCommit() {
        //        // 异步更新库存
        //        boolean mqResult = itemService.asyncDecreaseStock(itemId, amount);
        //        //if (!mqResult){
        //        //    itemService.increaseStock(itemId, amount);
        //        //    throw new BusinessException(EmBusinessError.MQ_SEND_FAIL);
        //        //}
        //    }
        //});

        // 4 返回前端
        return orderModel;
    }

    // 生成订单编号
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderNo() {
        // 订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        // 前8位为时间信息,年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        // 中间6为为自增序列
        // 获取当前sequence
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        // 最后2位位分库分表位(暂时写死)
        stringBuilder.append("00");
        return stringBuilder.toString();

    }

    private OrderDO convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);

        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
