package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MerchantMemberInfo;
import cc.mrbird.febs.cos.service.IMerchantMemberInfoService;
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
@RequestMapping("/cos/merchant-member-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantMemberInfoController {

    private final IMerchantMemberInfoService merchantMemberInfoService;

    /**
     * 分页获取商家会员信息
     *
     * @param page               分页对象
     * @param merchantMemberInfo 商家会员信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<MerchantMemberInfo> page, MerchantMemberInfo merchantMemberInfo) {
        return R.ok(merchantMemberInfoService.selectMerchantMemberPage(page, merchantMemberInfo));
    }

    /**
     * 获取ID获取商家会员详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(merchantMemberInfoService.getById(id));
    }

    /**
     * 获取商家会员信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(merchantMemberInfoService.list());
    }

    /**
     * 新增商家会员信息
     *
     * @param merchantMemberInfo 商家会员信息
     * @return 结果
     */
    @PostMapping
    public R save(MerchantMemberInfo merchantMemberInfo) {
        return R.ok(merchantMemberInfoService.save(merchantMemberInfo));
    }

    /**
     * 修改商家会员信息
     *
     * @param merchantMemberInfo 商家会员信息
     * @return 结果
     */
    @PutMapping
    public R edit(MerchantMemberInfo merchantMemberInfo) {
        return R.ok(merchantMemberInfoService.updateById(merchantMemberInfo));
    }

    /**
     * 删除商家会员信息
     *
     * @param ids ids
     * @return 商家会员信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(merchantMemberInfoService.removeByIds(ids));
    }
}
