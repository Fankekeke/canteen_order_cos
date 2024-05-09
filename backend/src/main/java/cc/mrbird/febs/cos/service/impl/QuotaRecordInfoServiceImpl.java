package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.QuotaRecordInfo;
import cc.mrbird.febs.cos.dao.QuotaRecordInfoMapper;
import cc.mrbird.febs.cos.service.IQuotaRecordInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class QuotaRecordInfoServiceImpl extends ServiceImpl<QuotaRecordInfoMapper, QuotaRecordInfo> implements IQuotaRecordInfoService {

    /**
     * 分页获取用户额度信息
     *
     * @param page            分页对象
     * @param quotaRecordInfo 用户额度信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectQuotaPage(Page<QuotaRecordInfo> page, QuotaRecordInfo quotaRecordInfo) {
        return baseMapper.selectQuotaPage(page, quotaRecordInfo);
    }
}
