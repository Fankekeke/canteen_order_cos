package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.MerchantMemberInfo;
import cc.mrbird.febs.cos.dao.MerchantMemberInfoMapper;
import cc.mrbird.febs.cos.service.IMerchantMemberInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class MerchantMemberInfoServiceImpl extends ServiceImpl<MerchantMemberInfoMapper, MerchantMemberInfo> implements IMerchantMemberInfoService {

    /**
     * 分页获取商家会员信息
     *
     * @param page        分页对象
     * @param merchantMemberInfo 商家会员信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectMerchantMemberPage(Page<MerchantMemberInfo> page, MerchantMemberInfo merchantMemberInfo) {
        return baseMapper.selectMerchantMemberPage(page, merchantMemberInfo);
    }
}
