package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.QuotaRecordInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IQuotaRecordInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
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
@RequestMapping("/cos/user-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoController {

    private final IUserInfoService userInfoService;

    private final IQuotaRecordInfoService quotaRecordService;

    /**
     * 分页获取用户信息
     *
     * @param page     分页对象
     * @param userInfo 用户信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<UserInfo> page, UserInfo userInfo) {
        return R.ok(userInfoService.selectUserPage(page, userInfo));
    }

    /**
     * 管理员分配额度
     *
     * @param quotaRecordInfo 额度分配
     * @return 结果
     */
    @PostMapping("/auditQuota")
    public R auditQuota(QuotaRecordInfo quotaRecordInfo) {
        quotaRecordInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        quotaRecordInfo.setType("0");
        // 添加额度记录
        quotaRecordService.save(quotaRecordInfo);
        // 更新用户额度
        UserInfo userInfo = userInfoService.getById(quotaRecordInfo.getUserId());
        userInfo.setQuota(NumberUtil.add(userInfo.getQuota(), quotaRecordInfo.getQuota()));
        return R.ok(userInfoService.updateById(userInfo));
    }

    /**
     * 获取ID获取用户详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(userInfoService.getById(id));
    }

    /**
     * 获取用户ID获取用户详情
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/detailByUserId/{userId}")
    public R detailByUserId(@PathVariable("userId") Integer userId) {
        return R.ok(userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId)));
    }

    /**
     * 根据用户ID用户信息及订单
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/user/{userId}")
    public R selectUserByUserId(@PathVariable("userId") Integer userId) {
        return R.ok(userInfoService.selectUserByUserId(userId));
    }

    /**
     * 获取用户信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(userInfoService.list());
    }

    /**
     * 新增用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    @PostMapping
    public R save(UserInfo userInfo) {
        userInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        userInfo.setCode("UR-" + System.currentTimeMillis());
        return R.ok(userInfoService.save(userInfo));
    }

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    @PutMapping
    public R edit(UserInfo userInfo) {
        return R.ok(userInfoService.updateById(userInfo));
    }

    /**
     * 删除用户信息
     *
     * @param ids ids
     * @return 用户信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(userInfoService.removeByIds(ids));
    }

}
