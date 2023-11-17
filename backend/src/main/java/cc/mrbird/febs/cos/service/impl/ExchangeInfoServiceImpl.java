package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ExchangeInfo;
import cc.mrbird.febs.cos.dao.ExchangeInfoMapper;
import cc.mrbird.febs.cos.service.IExchangeInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class ExchangeInfoServiceImpl extends ServiceImpl<ExchangeInfoMapper, ExchangeInfo> implements IExchangeInfoService {

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
}
