package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.QuotaRecordInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface QuotaRecordInfoMapper extends BaseMapper<QuotaRecordInfo> {

    /**
     * 分页获取用户额度信息
     *
     * @param page            分页对象
     * @param quotaRecordInfo 用户额度信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectQuotaPage(Page<QuotaRecordInfo> page, @Param("quotaRecordInfo") QuotaRecordInfo quotaRecordInfo);
}
