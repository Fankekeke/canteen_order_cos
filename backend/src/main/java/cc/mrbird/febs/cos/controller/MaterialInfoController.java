package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MaterialInfo;
import cc.mrbird.febs.cos.service.IMaterialInfoService;
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
@RequestMapping("/cos/material-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaterialInfoController {

    private final IMaterialInfoService materialInfoService;

    /**
     * 分页获取物品积分信息
     *
     * @param page        分页对象
     * @param materialInfo 物品积分信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<MaterialInfo> page, MaterialInfo materialInfo) {
        return R.ok(materialInfoService.selectMaterialPage(page, materialInfo));
    }

    /**
     * 获取ID获取物品积分详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(materialInfoService.getById(id));
    }

    /**
     * 获取物品积分信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(materialInfoService.list());
    }

    /**
     * 新增物品积分信息
     *
     * @param materialInfo 物品积分信息
     * @return 结果
     */
    @PostMapping
    public R save(MaterialInfo materialInfo) {
        materialInfo.setCode("MAT-" + System.currentTimeMillis());
        materialInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(materialInfoService.save(materialInfo));
    }

    /**
     * 修改物品积分信息
     *
     * @param materialInfo 物品积分信息
     * @return 结果
     */
    @PutMapping
    public R edit(MaterialInfo materialInfo) {
        return R.ok(materialInfoService.updateById(materialInfo));
    }

    /**
     * 删除物品积分信息
     *
     * @param ids ids
     * @return 物品积分信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(materialInfoService.removeByIds(ids));
    }
    
}
