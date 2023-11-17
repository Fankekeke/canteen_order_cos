package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.ExchangeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ExchangeInfoMapper extends BaseMapper<ExchangeInfo> {

    /**
     * 分页获取积分兑换信息
     *
     * @param page         分页对象
     * @param exchangeInfo 积分兑换信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectExChangePage(Page<ExchangeInfo> page, @Param("exchangeInfo") ExchangeInfo exchangeInfo);
}
