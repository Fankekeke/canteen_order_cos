package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.QuotaRecordInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IQuotaRecordInfoService extends IService<QuotaRecordInfo> {

    /**
     * 分页获取用户额度信息
     *
     * @param page            分页对象
     * @param quotaRecordInfo 用户额度信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectQuotaPage(Page<QuotaRecordInfo> page, QuotaRecordInfo quotaRecordInfo);
}
