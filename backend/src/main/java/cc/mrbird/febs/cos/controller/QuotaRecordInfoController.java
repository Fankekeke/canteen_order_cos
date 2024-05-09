package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.QuotaRecordInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IQuotaRecordInfoService;
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
@RequestMapping("/cos/quota-record-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuotaRecordInfoController {

    private final IQuotaRecordInfoService quotaRecordInfoService;
    private final IUserInfoService userInfoService;

    /**
     * 分页获取用户额度信息
     *
     * @param page            分页对象
     * @param quotaRecordInfo 用户额度信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<QuotaRecordInfo> page, QuotaRecordInfo quotaRecordInfo) {
        return R.ok(quotaRecordInfoService.selectQuotaPage(page, quotaRecordInfo));
    }

    /**
     * 获取ID获取用户额度详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(quotaRecordInfoService.getById(id));
    }

    /**
     * 获取用户额度信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(quotaRecordInfoService.list());
    }

    /**
     * 新增用户额度信息
     *
     * @param quotaRecordInfo 用户额度信息
     * @return 结果
     */
    @PostMapping
    public R save(QuotaRecordInfo quotaRecordInfo) {
        quotaRecordInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(quotaRecordInfoService.save(quotaRecordInfo));
    }

    /**
     * 新增用户额度信息
     *
     * @param quotaRecordInfo 用户额度信息
     * @return 结果
     */
    @PostMapping("byUser")
    public R saveByUser(QuotaRecordInfo quotaRecordInfo) {
        quotaRecordInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        quotaRecordInfo.setCode("QR-" + System.currentTimeMillis());
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, quotaRecordInfo.getUserId()));
        quotaRecordInfo.setUserId(userInfo.getId());
        return R.ok(quotaRecordInfoService.save(quotaRecordInfo));
    }

    /**
     * 修改用户额度信息
     *
     * @param quotaRecordInfo 用户额度信息
     * @return 结果
     */
    @PutMapping
    public R edit(QuotaRecordInfo quotaRecordInfo) {
        return R.ok(quotaRecordInfoService.updateById(quotaRecordInfo));
    }

    /**
     * 删除用户额度信息
     *
     * @param ids ids
     * @return 用户额度信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(quotaRecordInfoService.removeByIds(ids));
    }
}
