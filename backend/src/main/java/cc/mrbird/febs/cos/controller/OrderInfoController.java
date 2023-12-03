package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.service.IOrderInfoService;
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
@RequestMapping("/cos/order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoController {

    private final IOrderInfoService orderInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderInfo> page, OrderInfo orderInfo) {
        return R.ok(orderInfoService.selectOrderPage(page, orderInfo));
    }

    /**
     * 根据ID获取订单详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.orderDetail(id));
    }

    /**
     * 查询可卖商家
     *
     * @return 结果
     */
    @GetMapping("/selectMerchantList")
    public R selectMerchantList(@RequestParam(value = "key", required = false) String key) {
        return R.ok(orderInfoService.selectMerchantList(key));
    }

    /**
     * 根据用户ID获取历史订单
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/selectMerchantMember/{userId}")
    public R selectMerchantMember(@PathVariable("userId") Integer userId) {
        return R.ok(orderInfoService.list(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getUserId, userId)));
    }

    /**
     * 获取订单评价详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/evaluate/{id}")
    public R evaluateDetail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.evaluateDetail(id));
    }

    /**
     * 获取订单信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderInfoService.list());
    }

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderInfo orderInfo) throws FebsException {
        return R.ok(orderInfoService.addOrder(orderInfo));
    }

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/saveOrder")
    public R saveOrder(OrderInfo orderInfo) throws FebsException {
        return R.ok(orderInfoService.saveOrder(orderInfo));
    }

    /**
     * 订单收货
     *
     * @param orderCode 订单编号
     * @param status    状态
     * @return 结果
     */
    @GetMapping("/audit")
    public R audit(@RequestParam("orderCode") String orderCode, @RequestParam("status") String status) {
        return R.ok(orderInfoService.audit(orderCode, status));
    }


    /**
     * 获取订单付款信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/getPriceTotal")
    public R getPriceTotal(OrderInfo orderInfo) {
        return R.ok(orderInfoService.getPriceTotal(orderInfo));
    }

    /**
     * 修改订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderInfo orderInfo) throws FebsException {
        return R.ok(orderInfoService.editOrder(orderInfo));
    }

    /**
     * 订单支付
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @PostMapping("/orderPay")
    public R orderPay(@RequestParam("orderCode") String orderCode) {
        return R.ok(orderInfoService.orderPay(orderCode));
    }

    /**
     * 外送订单选择配送员
     *
     * @param orderCode 订单编号
     * @param staffId   员工ID
     * @return 结果
     */
    @GetMapping("/checkDealer")
    public R checkDealer(@RequestParam("orderCode") String orderCode, @RequestParam("staffId") Integer staffId) {
        return R.ok(orderInfoService.checkDealer(orderCode, staffId));
    }

    /**
     * 删除订单信息
     *
     * @param ids ids
     * @return 订单信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderInfoService.removeByIds(ids));
    }
}
