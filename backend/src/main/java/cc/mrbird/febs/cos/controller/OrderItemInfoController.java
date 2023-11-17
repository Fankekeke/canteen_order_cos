package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderItemInfo;
import cc.mrbird.febs.cos.service.IOrderItemInfoService;
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
@RequestMapping("/cos/order-item-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderItemInfoController {

    private final IOrderItemInfoService orderItemInfoService;

    /**
     * 分页获取收货地址信息
     *
     * @param page          分页对象
     * @param orderItemInfo 收货地址信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderItemInfo> page, OrderItemInfo orderItemInfo) {
        return R.ok();
    }

    /**
     * 获取ID获取收货地址详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderItemInfoService.getById(id));
    }

    /**
     * 获取收货地址信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderItemInfoService.list());
    }

    /**
     * 新增收货地址信息
     *
     * @param orderItemInfo 收货地址信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderItemInfo orderItemInfo) {
        return R.ok(orderItemInfoService.save(orderItemInfo));
    }

    /**
     * 修改收货地址信息
     *
     * @param orderItemInfo 收货地址信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderItemInfo orderItemInfo) {
        return R.ok(orderItemInfoService.updateById(orderItemInfo));
    }

    /**
     * 删除收货地址信息
     *
     * @param ids ids
     * @return 收货地址信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderItemInfoService.removeByIds(ids));
    }
}
