package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ExchangeInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IExchangeInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/exchange-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExchangeInfoController {

    private final IExchangeInfoService exchangeInfoService;

    /**
     * 分页获取积分兑换信息
     *
     * @param page         分页对象
     * @param exchangeInfo 积分兑换信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ExchangeInfo> page, ExchangeInfo exchangeInfo) {
        return R.ok(exchangeInfoService.selectExChangePage(page, exchangeInfo));
    }

    /**
     * 获取ID获取积分兑换详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(exchangeInfoService.exchangeDetail(id));
    }

    /**
     * 获取积分兑换信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(exchangeInfoService.list());
    }

    /**
     * 新增积分兑换信息
     *
     * @param exchangeInfo 积分兑换信息
     * @return 结果
     */
    @PostMapping
    public R save(ExchangeInfo exchangeInfo) throws FebsException {
        return R.ok(exchangeInfoService.addExchange(exchangeInfo));
    }

    /**
     * 修改积分兑换信息
     *
     * @param exchangeInfo 积分兑换信息
     * @return 结果
     */
    @PutMapping
    public R edit(ExchangeInfo exchangeInfo) {
        return R.ok(exchangeInfoService.updateById(exchangeInfo));
    }

    /**
     * 删除积分兑换信息
     *
     * @param ids ids
     * @return 积分兑换信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(exchangeInfoService.removeByIds(ids));
    }
}
