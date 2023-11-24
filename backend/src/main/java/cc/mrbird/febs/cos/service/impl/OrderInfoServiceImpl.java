package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.LocationUtils;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private final IUserInfoService userInfoService;

    private final IMerchantInfoService merchantInfoService;

    private final IAddressInfoService addressInfoService;

    private final IMerchantMemberInfoService merchantMemberInfoService;

    private final IOrderItemInfoService orderItemInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo) {
        return baseMapper.selectOrderPage(page, orderInfo);
    }

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public boolean addOrder(OrderInfo orderInfo) throws FebsException {

        List<OrderItemInfo> orderItemList = orderInfo.getOrderItemList();
        if (CollectionUtil.isEmpty(orderItemList)) {
            throw new FebsException("订单详情不能为空");
        }

        orderInfo.setCode("ORD-" + System.currentTimeMillis());
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfo.getUserId()));

        orderInfo.setUserId(userInfo.getId());
        // 如果为外送 计算配送费用
        if ("1".equals(orderInfo.getType())) {
            // 获取送货地址
            AddressInfo addressInfo = addressInfoService.getById(orderInfo.getAddressId());
            // 获取商家地址
            MerchantInfo merchantInfo = merchantInfoService.getById(orderInfo.getMerchantId());

            // 计算公里数与配送费用
            double distance = LocationUtils.getDistance(merchantInfo.getLongitude().doubleValue(), merchantInfo.getLatitude().doubleValue(), addressInfo.getLongitude().doubleValue(), addressInfo.getLatitude().doubleValue());
            orderInfo.setDiscount(NumberUtil.div(new BigDecimal(distance), 1000));


            // 每公里两米
            orderInfo.setDistributionPrice(NumberUtil.mul(orderInfo.getDistributionPrice(), 2));
            orderInfo.setOrderPrice(NumberUtil.add(orderInfo.getOrderPrice(), orderInfo.getDistributionPrice()));
        }


        // 判断用户是否为此店会员
        int count = merchantMemberInfoService.count(Wrappers.<MerchantMemberInfo>lambdaQuery().eq(MerchantMemberInfo::getMerchantId, orderInfo.getMerchantId()).eq(MerchantMemberInfo::getUserId, orderInfo.getUserId()));
        if (count > 0) {
            BigDecimal discount = NumberUtil.sub(orderInfo.getOrderPrice(), NumberUtil.mul(orderInfo.getOrderPrice(), 0.8));
            orderInfo.setDiscount(discount);
            orderInfo.setAfterOrderPrice(NumberUtil.mul(orderInfo.getOrderPrice(), 0.8));
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItemInfo orderItem : orderItemList) {
            orderItem.setTotalPrice(NumberUtil.mul(orderItem.getTotalPrice(), orderItem.getAmount()));
            totalPrice = NumberUtil.add(totalPrice, orderItem.getTotalPrice());
        }
        // 添加订单
        this.save(orderInfo);

        // 添加订单详情
        for (OrderItemInfo orderItem : orderItemList) {
            orderItem.setOrderId(orderInfo.getId());
        }
        return orderItemInfoService.saveBatch(orderItemList);
    }
}
