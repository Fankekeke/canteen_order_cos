package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.MerchantInfo;
import cc.mrbird.febs.cos.entity.OrderInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo);

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    boolean addOrder(OrderInfo orderInfo) throws FebsException;

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    boolean saveOrder(OrderInfo orderInfo);

    /**
     * 订单收货
     *
     * @param orderCode 订单编号
     * @param status    状态
     * @return 结果
     */
    boolean audit(String orderCode, String status);

    /**
     * 获取订单付款信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    OrderInfo getPriceTotal(OrderInfo orderInfo);

    /**
     * 修改订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    boolean editOrder(OrderInfo orderInfo) throws FebsException;

    /**
     * 获取ID获取订单详情
     *
     * @param id 主键
     * @return 结果
     */
    LinkedHashMap<String, Object> orderDetail(Integer id);

    /**
     * 查询可卖商家
     *
     * @return 结果
     */
    List<MerchantInfo> selectMerchantList(String key);

    /**
     * 获取订单评价详情
     *
     * @param id 主键
     * @return 结果
     */
    LinkedHashMap<String, Object> evaluateDetail(Integer id);

    /**
     * 订单支付
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    boolean orderPay(String orderCode);

    /**
     * 外送订单选择配送员
     *
     * @param orderCode 订单编号
     * @param staffId   员工ID
     * @return 结果
     */
    boolean checkDealer(String orderCode, Integer staffId);
}
