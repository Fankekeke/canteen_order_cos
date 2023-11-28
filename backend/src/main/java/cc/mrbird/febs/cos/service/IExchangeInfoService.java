package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.ExchangeInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IExchangeInfoService extends IService<ExchangeInfo> {

    /**
     * 分页获取积分兑换信息
     *
     * @param page         分页对象
     * @param exchangeInfo 积分兑换信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectExChangePage(Page<ExchangeInfo> page, ExchangeInfo exchangeInfo);

    /**
     * 新增积分兑换信息
     *
     * @param exchangeInfo 积分兑换信息
     * @return 结果
     */
    boolean addExchange(ExchangeInfo exchangeInfo) throws FebsException;

    /**
     * 获取ID获取积分兑换详情
     *
     * @param id 主键
     * @return 结果
     */
    LinkedHashMap<String, Object> exchangeDetail(Integer id);
}
