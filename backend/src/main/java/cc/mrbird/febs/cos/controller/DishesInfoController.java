package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DishesInfo;
import cc.mrbird.febs.cos.entity.MerchantInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IDishesInfoService;
import cc.mrbird.febs.cos.service.IMerchantInfoService;
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
@RequestMapping("/cos/dishes-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DishesInfoController {

    private final IDishesInfoService dishesInfoService;

    private final IMerchantInfoService merchantInfoService;

    /**
     * 分页获取菜品信息
     *
     * @param page       分页对象
     * @param dishesInfo 菜品信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DishesInfo> page, DishesInfo dishesInfo) {
        return R.ok(dishesInfoService.selectDishesPage(page, dishesInfo));
    }

    /**
     * 根据商家获取菜品信息
     *
     * @param merchantUserId 商家ID
     * @return 结果
     */
    @GetMapping("/selectDishesByMerchant/{merchantUserId}")
    public R selectDishesByMerchant(@PathVariable("merchantUserId") Integer merchantUserId) {
        // 获取所属商家
        MerchantInfo merchantInfo = merchantInfoService.getOne(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getId, merchantUserId));
        if (merchantInfo == null) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(dishesInfoService.list(Wrappers.<DishesInfo>lambdaQuery().eq(DishesInfo::getMerchantId, merchantInfo.getId()).eq(DishesInfo::getStatus, "1")));
    }

    /**
     * 获取ID获取菜品详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(dishesInfoService.getById(id));
    }

    /**
     * 获取菜品信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(dishesInfoService.list());
    }

    /**
     * 新增菜品信息
     *
     * @param dishesInfo 菜品信息
     * @return 结果
     */
    @PostMapping
    public R save(DishesInfo dishesInfo) {
        // 获取所属商家
        MerchantInfo merchantInfo = merchantInfoService.getOne(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getUserId, dishesInfo.getMerchantId()));
        if (merchantInfo != null) {
            dishesInfo.setMerchantId(merchantInfo.getId());
        }
        dishesInfo.setCode("DIS-" + System.currentTimeMillis());
        dishesInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(dishesInfoService.save(dishesInfo));
    }

    /**
     * 修改菜品信息
     *
     * @param dishesInfo 菜品信息
     * @return 结果
     */
    @PutMapping
    public R edit(DishesInfo dishesInfo) {
        return R.ok(dishesInfoService.updateById(dishesInfo));
    }

    /**
     * 删除菜品信息
     *
     * @param ids ids
     * @return 菜品信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(dishesInfoService.removeByIds(ids));
    }
}
