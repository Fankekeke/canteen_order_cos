package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.MerchantMemberInfoMapper;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.dao.StaffInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.MerchantInfoMapper;
import cc.mrbird.febs.cos.service.IBulletinInfoService;
import cc.mrbird.febs.cos.service.IMerchantInfoService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantInfoServiceImpl extends ServiceImpl<MerchantInfoMapper, MerchantInfo> implements IMerchantInfoService {

    private final OrderInfoMapper orderInfoMapper;

    private final IBulletinInfoService bulletinInfoService;

    private final MerchantInfoMapper merchantInfoMapper;

    private final StaffInfoMapper staffInfoMapper;

    private final MerchantMemberInfoMapper merchantMemberInfoMapper;

    /**
     * 分页获取商家信息
     *
     * @param page         分页对象
     * @param merchantInfo 商家信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectMerchantPage(Page<MerchantInfo> page, MerchantInfo merchantInfo) {
        return baseMapper.selectMerchantPage(page, merchantInfo);
    }

    /**
     * 根据商家获取订单评价信息
     *
     * @param merchantId 商家ID
     * @param dishesId   菜品ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectEvaluateByMerchant(Integer merchantId, Integer dishesId) {
        return baseMapper.selectEvaluateByMerchant(merchantId, dishesId);
    }

    /**
     * 商家获取统计信息
     *
     * @param userId 商家用户ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectHomeDataByMerchant(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("orderNum", 0);
                put("orderPrice", 0);
                put("staffNum", 0);
                put("memberNum", 0);
            }
        };

        // 商家信息
        MerchantInfo merchantInfo = merchantInfoMapper.selectOne(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getUserId, userId));

        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getMerchantId, merchantInfo.getId()).ne(OrderInfo::getStatus, "0"));

        BigDecimal totalPrice = orderInfoList.stream().map(OrderInfo::getAfterOrderPrice).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("orderNum", orderInfoList.size());
        result.put("orderPrice", totalPrice);
        result.put("staffNum", staffInfoMapper.selectCount(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getCanteenId, merchantInfo.getId()).eq(StaffInfo::getStatus, "1")));
        result.put("memberNum", merchantMemberInfoMapper.selectCount(Wrappers.<MerchantMemberInfo>lambdaQuery().eq(MerchantMemberInfo::getMerchantId, merchantInfo.getId())));

        // 本月订单数量
        List<OrderInfo> orderMonthList = orderInfoMapper.selectOrderByMonth(merchantInfo.getId());
        result.put("monthOrderNum", CollectionUtil.isEmpty(orderMonthList) ? 0 : orderMonthList.size());
        BigDecimal orderPrice = orderMonthList.stream().map(OrderInfo::getAfterOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取本月收益
        result.put("monthOrderTotal", orderPrice);

        // 本年订单数量
        List<OrderInfo> orderYearList = orderInfoMapper.selectOrderByYear(merchantInfo.getId());
        result.put("yearOrderNum", CollectionUtil.isEmpty(orderYearList) ? 0 : orderYearList.size());
        // 本年总收益
        BigDecimal orderYearPrice = orderYearList.stream().map(OrderInfo::getAfterOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("yearOrderTotal", orderYearPrice);

        // 近十天销售订单统计
        result.put("orderNumDayList", orderInfoMapper.selectOrderNumWithinDays(merchantInfo.getId()));
        // 近十天销售金额统计
        result.put("priceDayList", orderInfoMapper.selectOrderPriceWithinDays(merchantInfo.getId()));
        // 销售菜品统计
        result.put("orderDrugType", orderInfoMapper.selectOrderDishesType(merchantInfo.getId()));
        // 公告信息
        result.put("bulletinInfoList", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        return result;
    }

    /**
     * 管理员获取统计信息
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectHomeDataByAdmin() {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("orderNum", 0);
                put("orderPrice", 0);
                put("staffNum", 0);
                put("merchantNum", 0);
            }
        };

        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(Wrappers.<OrderInfo>lambdaQuery().ne(OrderInfo::getStatus, "0"));

        BigDecimal totalPrice = orderInfoList.stream().map(OrderInfo::getAfterOrderPrice).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("orderNum", orderInfoList.size());
        result.put("orderPrice", totalPrice);
        result.put("staffNum", staffInfoMapper.selectCount(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getStatus, "1")));
        result.put("merchantNum", merchantInfoMapper.selectCount(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getStatus, "1")));

        // 本月订单数量
        List<OrderInfo> orderMonthList = orderInfoMapper.selectOrderByMonth(null);
        result.put("monthOrderNum", CollectionUtil.isEmpty(orderMonthList) ? 0 : orderMonthList.size());
        BigDecimal orderPrice = orderMonthList.stream().map(OrderInfo::getAfterOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取本月收益
        result.put("monthOrderTotal", orderPrice);

        // 本年订单数量
        List<OrderInfo> orderYearList = orderInfoMapper.selectOrderByYear(null);
        result.put("yearOrderNum", CollectionUtil.isEmpty(orderYearList) ? 0 : orderYearList.size());
        // 本年总收益
        BigDecimal orderYearPrice = orderYearList.stream().map(OrderInfo::getAfterOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("yearOrderTotal", orderYearPrice);

        // 近十天销售订单统计
        result.put("orderNumDayList", orderInfoMapper.selectOrderNumWithinDays(null));
        // 近十天销售金额统计
        result.put("priceDayList", orderInfoMapper.selectOrderPriceWithinDays(null));
        // 销售菜品统计
        result.put("orderDrugType", orderInfoMapper.selectOrderDishesType(null));
        // 公告信息
        result.put("bulletinInfoList", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        return result;
    }
}
