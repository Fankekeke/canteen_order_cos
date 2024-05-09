package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DishesRecordInfo;
import cc.mrbird.febs.cos.service.IDishesRecordInfoService;
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
@RequestMapping("/cos/dishes-record-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DishesRecordInfoController {

    private final IDishesRecordInfoService dishesRecordInfoService;

    /**
     * 分页获取菜品采购信息
     *
     * @param page             分页对象
     * @param dishesRecordInfo 菜品采购信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DishesRecordInfo> page, DishesRecordInfo dishesRecordInfo) {
        return R.ok(dishesRecordInfoService.selectRecordByPage(page, dishesRecordInfo));
    }

    /**
     * 获取ID获取菜品采购详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(dishesRecordInfoService.getById(id));
    }

    /**
     * 获取菜品采购信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(dishesRecordInfoService.list());
    }

    /**
     * 新增菜品采购信息
     *
     * @param dishesRecordInfo 菜品采购信息
     * @return 结果
     */
    @PostMapping
    public R save(DishesRecordInfo dishesRecordInfo) {
        dishesRecordInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(dishesRecordInfoService.save(dishesRecordInfo));
    }

    /**
     * 修改菜品采购信息
     *
     * @param dishesRecordInfo 菜品采购信息
     * @return 结果
     */
    @PutMapping
    public R edit(DishesRecordInfo dishesRecordInfo) {
        return R.ok(dishesRecordInfoService.updateById(dishesRecordInfo));
    }

    /**
     * 删除菜品采购信息
     *
     * @param ids ids
     * @return 菜品采购信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(dishesRecordInfoService.removeByIds(ids));
    }
}
