package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MerchantInfo;
import cc.mrbird.febs.cos.service.IMerchantInfoService;
import cn.hutool.core.date.DateUtil;
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
@RequestMapping("/cos/merchant-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantInfoController {

    private final IMerchantInfoService merchantInfoService;

    /**
     * 分页获取商家信息
     *
     * @param page        分页对象
     * @param merchantInfo 商家信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<MerchantInfo> page, MerchantInfo merchantInfo) {
        return R.ok(merchantInfoService.selectMerchantPage(page, merchantInfo));
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
