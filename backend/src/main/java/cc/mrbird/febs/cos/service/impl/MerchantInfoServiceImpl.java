package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.entity.BulletinInfo;
import cc.mrbird.febs.cos.entity.MerchantInfo;
import cc.mrbird.febs.cos.dao.MerchantInfoMapper;
import cc.mrbird.febs.cos.service.IBulletinInfoService;
import cc.mrbird.febs.cos.service.IMerchantInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantInfoServiceImpl extends ServiceImpl<MerchantInfoMapper, MerchantInfo> implements IMerchantInfoService {

    private final OrderInfoMapper orderInfoMapper;

    private final IBulletinInfoService bulletinInfoService;

    private final MerchantInfoMapper merchantInfoMapper;

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

        // 近十天销售订单统计
        result.put("orderNumWithinDays", orderInfoMapper.selectOrderNumWithinDays(merchantInfo.getId()));
        // 近十天销售金额统计
        result.put("orderPriceWithinDays", orderInfoMapper.selectOrderPriceWithinDays(merchantInfo.getId()));
        // 销售菜品统计
        result.put("orderDrugType", orderInfoMapper.selectOrderDishesType(merchantInfo.getId()));
        // 公告信息
        result.put("bulletin", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        return result;
    }
}
