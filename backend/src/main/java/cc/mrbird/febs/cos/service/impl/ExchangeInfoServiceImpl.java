package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.ExchangeInfo;
import cc.mrbird.febs.cos.dao.ExchangeInfoMapper;
import cc.mrbird.febs.cos.entity.MaterialInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IExchangeInfoService;
import cc.mrbird.febs.cos.service.IMaterialInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExchangeInfoServiceImpl extends ServiceImpl<ExchangeInfoMapper, ExchangeInfo> implements IExchangeInfoService {

    private final IUserInfoService userInfoService;

    private final IMaterialInfoService materialInfoService;

    /**
     * 分页获取积分兑换信息
     *
     * @param page         分页对象
     * @param exchangeInfo 积分兑换信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectExChangePage(Page<ExchangeInfo> page, ExchangeInfo exchangeInfo) {
        return baseMapper.selectExChangePage(page, exchangeInfo);
    }

    /**
     * 新增积分兑换信息
     *
     * @param exchangeInfo 积分兑换信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addExchange(ExchangeInfo exchangeInfo) throws FebsException {
        // 获取所属用户
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, exchangeInfo.getUserId()));
        // 物品信息
        MaterialInfo materialInfo = materialInfoService.getById(exchangeInfo.getMaterialId());

        if (userInfo == null || materialInfo == null) {
            throw new FebsException("系统异常");
        }
        if (materialInfo.getIntegral().compareTo(userInfo.getIntegral()) > 0) {
            throw new FebsException("用户积分为【"+userInfo.getIntegral()+"】 无法兑换");
        }
        exchangeInfo.setUserId(userInfo.getId());
        exchangeInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        this.save(exchangeInfo);

        // 添加物品销量
        materialInfo.setSaleNum(materialInfo.getSaleNum() + 1);
        materialInfoService.updateById(materialInfo);

        // 更新用户积分
        userInfo.setIntegral(NumberUtil.sub(userInfo.getIntegral(), materialInfo.getIntegral()));
        return userInfoService.updateById(userInfo);
    }

    /**
     * 获取ID获取积分兑换详情
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> exchangeDetail(Integer id) {
        // 获取兑换信息
        ExchangeInfo exchangeInfo = this.getById(id);
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("exchange", null);
                put("user", null);
                put("material", null);
            }
        };
        result.put("exchange", exchangeInfo);

        // 用户信息
        UserInfo userInfo = userInfoService.getById(exchangeInfo.getUserId());
        result.put("user", userInfo);

        // 物品信息
        MaterialInfo materialInfo = materialInfoService.getById(exchangeInfo.getMaterialId());
        result.put("material", materialInfo);
        return result;
    }
}
