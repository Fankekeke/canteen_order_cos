package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MerchantInfo;
import cc.mrbird.febs.cos.entity.StaffInfo;
import cc.mrbird.febs.cos.service.IMerchantInfoService;
import cc.mrbird.febs.cos.service.IStaffInfoService;
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
@RequestMapping("/cos/staff-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaffInfoController {

    private final IStaffInfoService staffInfoService;

    private final IMerchantInfoService merchantInfoService;

    /**
     * 分页获取员工信息
     *
     * @param page      分页对象
     * @param staffInfo 员工信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<StaffInfo> page, StaffInfo staffInfo) {
        return R.ok(staffInfoService.selectStaffPage(page, staffInfo));
    }

    /**
     * 获取ID获取员工详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(staffInfoService.getById(id));
    }

    /**
     * 获取员工信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(staffInfoService.list());
    }

    /**
     * 新增员工信息
     *
     * @param staffInfo 员工信息
     * @return 结果
     */
    @PostMapping
    public R save(StaffInfo staffInfo) {
        // 获取所属商家
        MerchantInfo merchantInfo = merchantInfoService.getOne(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getUserId, staffInfo.getCanteenId()));
        if (merchantInfo != null) {
            staffInfo.setCanteenId(merchantInfo.getId());
        }
        staffInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        staffInfo.setCode("STF-" + System.currentTimeMillis());
        return R.ok(staffInfoService.save(staffInfo));
    }

    /**
     * 根据商家获取员工信息
     *
     * @param merchantUserId 商家ID
     * @return 结果
     */
    @GetMapping("/selectStaffByMerchant/{merchantUserId}")
    public R selectStaffByMerchant(@PathVariable("merchantUserId") Integer merchantUserId) {
        // 获取所属商家
        MerchantInfo merchantInfo = merchantInfoService.getOne(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getUserId, merchantUserId));
        if (merchantInfo == null) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(staffInfoService.list(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getCanteenId, merchantInfo.getId())));
    }

    /**
     * 修改员工信息
     *
     * @param staffInfo 员工信息
     * @return 结果
     */
    @PutMapping
    public R edit(StaffInfo staffInfo) {
        return R.ok(staffInfoService.updateById(staffInfo));
    }

    /**
     * 删除员工信息
     *
     * @param ids ids
     * @return 员工信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(staffInfoService.removeByIds(ids));
    }

}
