package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AddressInfo;
import cc.mrbird.febs.cos.service.IAddressInfoService;
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
@RequestMapping("/cos/address-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressInfoController {

    private final IAddressInfoService addressInfoService;

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
     * 新增收货地址信息
     *
     * @param addressInfo 收货地址信息
     * @return 结果
     */
    @PostMapping
    public R save(AddressInfo addressInfo) {
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
