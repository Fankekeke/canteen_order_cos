package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AddressInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IAddressInfoService;
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
@RequestMapping("/cos/address-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressInfoController {

    private final IAddressInfoService addressInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取收货地址信息
     *
     * @param page        分页对象
     * @param addressInfo 收货地址信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<AddressInfo> page, AddressInfo addressInfo) {
        return R.ok(addressInfoService.selectAddressPage(page, addressInfo));
    }

    /**
     * 获取ID获取收货地址详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(addressInfoService.getById(id));
    }

    /**
     * 获取收货地址信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(addressInfoService.list());
    }

    /**
     * 根据用户获取收货地址
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/listByUserId/{userId}")
    public R listByUserId(@PathVariable("userId") Integer userId) {
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(addressInfoService.list(Wrappers.<AddressInfo>lambdaQuery().eq(AddressInfo::getUserId, userInfo.getId())));
    }

    /**
     * 新增收货地址信息
     *
     * @param addressInfo 收货地址信息
     * @return 结果
     */
    @PostMapping
    public R save(AddressInfo addressInfo) {
        // 获取所属用户
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, addressInfo.getUserId()));
        if (userInfo != null) {
            addressInfo.setUserId(userInfo.getId());
        }
        addressInfo.setCode("ADS-" + System.currentTimeMillis());
        addressInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(addressInfoService.save(addressInfo));
    }

    /**
     * 修改收货地址信息
     *
     * @param addressInfo 收货地址信息
     * @return 结果
     */
    @PutMapping
    public R edit(AddressInfo addressInfo) {
        return R.ok(addressInfoService.updateById(addressInfo));
    }

    /**
     * 删除收货地址信息
     *
     * @param ids ids
     * @return 收货地址信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(addressInfoService.removeByIds(ids));
    }
}
