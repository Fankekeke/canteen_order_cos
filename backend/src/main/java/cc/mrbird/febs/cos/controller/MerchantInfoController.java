package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MerchantInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IMerchantInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/merchant-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantInfoController {

    private final IMerchantInfoService merchantInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取商家信息
     *
     * @param page         分页对象
     * @param merchantInfo 商家信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<MerchantInfo> page, MerchantInfo merchantInfo) {
        return R.ok(merchantInfoService.selectMerchantPage(page, merchantInfo));
    }

    /**
     * 根据用户ID获取商家信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/getMerchantByUser")
    public R getMerchantByUser(@RequestParam("userId") Integer userId) {
        MerchantInfo merchantInfo = merchantInfoService.getOne(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getUserId, userId));
        if (merchantInfo == null) {
            return R.ok();
        }
        return R.ok(merchantInfo);
    }

    /**
     * 根据商家获取订单评价信息
     *
     * @param merchantId 商家ID
     * @return 结果
     */
    @GetMapping("/selectEvaluateByMerchant")
    public R selectEvaluateByMerchant(@RequestParam("merchantId") Integer merchantId, @RequestParam(value = "dishesId", required = false) Integer dishesId) {
        return R.ok(merchantInfoService.selectEvaluateByMerchant(merchantId, dishesId));
    }

    /**
     * 商家获取统计信息
     *
     * @param userId 商家用户ID
     * @return 结果
     */
    @GetMapping("/homeData")
    public R selectHomeDataByMerchant(@RequestParam("userId") Integer userId) {
        return R.ok(merchantInfoService.selectHomeDataByMerchant(userId));
    }

    /**
     * 获取ID获取商家详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(merchantInfoService.getById(id));
    }

    /**
     * 获取商家信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(merchantInfoService.list());
    }

    /**
     * 新增商家信息
     *
     * @param merchantInfo 商家信息
     * @return 结果
     */
    @PostMapping
    public R save(MerchantInfo merchantInfo) {
        merchantInfo.setCode("MER-" + System.currentTimeMillis());
        merchantInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(merchantInfoService.save(merchantInfo));
    }

    /**
     * 修改商家信息
     *
     * @param merchantInfo 商家信息
     * @return 结果
     */
    @PutMapping
    public R edit(MerchantInfo merchantInfo) {
        return R.ok(merchantInfoService.updateById(merchantInfo));
    }

    /**
     * 删除商家信息
     *
     * @param ids ids
     * @return 商家信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(merchantInfoService.removeByIds(ids));
    }
}
