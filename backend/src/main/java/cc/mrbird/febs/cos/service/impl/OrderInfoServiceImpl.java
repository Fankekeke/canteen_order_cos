package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.LocationUtils;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private final IUserInfoService userInfoService;

    private final IMerchantInfoService merchantInfoService;

    private final IAddressInfoService addressInfoService;

    private final IMerchantMemberInfoService merchantMemberInfoService;

    private final IOrderItemInfoService orderItemInfoService;

    private final IMemberInfoService memberInfoService;

    private final IDishesInfoService dishesInfoService;

    private final IStaffInfoService staffInfoService;

    private final IEvaluateInfoService evaluateInfoService;

    private final TemplateEngine templateEngine;

    private final IMailService mailService;


    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo) {
        return baseMapper.selectOrderPage(page, orderInfo);
    }

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrder(OrderInfo orderInfo) throws FebsException {

        List<OrderItemInfo> orderItemList = orderInfo.getOrderItemList();
        if (CollectionUtil.isEmpty(orderItemList)) {
            throw new FebsException("订单详情不能为空");
        }

        orderInfo.setCode("ORD-" + System.currentTimeMillis());
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfo.getUserId()));

        orderInfo.setUserId(userInfo.getId());
        // 如果为外送 计算配送费用
        if ("1".equals(orderInfo.getType())) {
            // 获取送货地址
            AddressInfo addressInfo = addressInfoService.getById(orderInfo.getAddressId());
            // 获取商家地址
            MerchantInfo merchantInfo = merchantInfoService.getById(orderInfo.getMerchantId());

            // 计算公里数与配送费用
            double distance = LocationUtils.getDistance(merchantInfo.getLongitude().doubleValue(), merchantInfo.getLatitude().doubleValue(), addressInfo.getLongitude().doubleValue(), addressInfo.getLatitude().doubleValue());
            orderInfo.setDiscount(NumberUtil.div(new BigDecimal(distance), 1000));

            // 每公里两米
            orderInfo.setDistributionPrice(NumberUtil.mul(orderInfo.getDistributionPrice(), 2));
            orderInfo.setOrderPrice(NumberUtil.add(orderInfo.getOrderPrice(), orderInfo.getDistributionPrice()));
        }

        // 判断用户是否为此店会员
        int count = merchantMemberInfoService.count(Wrappers.<MerchantMemberInfo>lambdaQuery().eq(MerchantMemberInfo::getMerchantId, orderInfo.getMerchantId()).eq(MerchantMemberInfo::getUserId, orderInfo.getUserId()));
        if (count > 0) {
            BigDecimal discount = NumberUtil.sub(orderInfo.getOrderPrice(), NumberUtil.mul(orderInfo.getOrderPrice(), 0.8));
            orderInfo.setDiscount(discount);
            orderInfo.setAfterOrderPrice(NumberUtil.mul(orderInfo.getOrderPrice(), 0.8));
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItemInfo orderItem : orderItemList) {
            orderItem.setTotalPrice(NumberUtil.mul(orderItem.getTotalPrice(), orderItem.getAmount()));
            totalPrice = NumberUtil.add(totalPrice, orderItem.getTotalPrice());
        }
        // 添加订单
        this.save(orderInfo);

        // 添加订单详情
        for (OrderItemInfo orderItem : orderItemList) {
            orderItem.setOrderId(orderInfo.getId());
        }
        return orderItemInfoService.saveBatch(orderItemList);
    }

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public boolean saveOrder(OrderInfo orderInfo) {
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfo.getUserId()));

        orderInfo.setUserId(userInfo.getId());

        // 添加订单
        orderInfo.setStatus("0");
        this.save(orderInfo);

        List<OrderItemInfo> orderItemList = JSONUtil.toList(orderInfo.getOrderItemListStr(), OrderItemInfo.class);

        // 添加订单详情
        for (OrderItemInfo orderItem : orderItemList) {
            orderItem.setOrderId(orderInfo.getId());
        }
        return orderItemInfoService.saveBatch(orderItemList);
    }

    /**
     * 订单收货
     *
     * @param orderCode 订单编号
     * @param status    状态
     * @return 结果
     */
    @Override
    public boolean audit(String orderCode, String status) {
        OrderInfo order = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));

        UserInfo userInfo = userInfoService.getById(order.getUserId());

        // 邮箱通知
        if (StrUtil.isNotEmpty(userInfo.getMail())) {
            MerchantInfo merchantInfo = merchantInfoService.getById(order.getMerchantId());
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + "，您好，在 " + merchantInfo.getName() + " 消费订单 " + order.getCode() + " 已完成，可进行评价");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getMail(), DateUtil.formatDate(new Date()) + "订单完成", emailContent);
        }
        return this.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getStatus, status).eq(OrderInfo::getCode, orderCode));
    }

    /**
     * 获取订单付款信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public OrderInfo getPriceTotal(OrderInfo orderInfo) {
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfo.getUserId()));

        // 如果为外送 计算配送费用
        if ("1".equals(orderInfo.getType())) {
            // 获取送货地址
            AddressInfo addressInfo = addressInfoService.getById(orderInfo.getAddressId());
            // 获取商家地址
            MerchantInfo merchantInfo = merchantInfoService.getById(orderInfo.getMerchantId());

            // 计算公里数与配送费用
            double distance = LocationUtils.getDistance(merchantInfo.getLongitude().doubleValue(), merchantInfo.getLatitude().doubleValue(), addressInfo.getLongitude().doubleValue(), addressInfo.getLatitude().doubleValue());
            orderInfo.setKilometre(NumberUtil.round(NumberUtil.div(new BigDecimal(distance), 1000), 2));

            // 每公里两米
            orderInfo.setDistributionPrice(NumberUtil.mul(orderInfo.getKilometre(), 2));
            orderInfo.setOrderPrice(NumberUtil.add(orderInfo.getOrderPrice(), orderInfo.getDistributionPrice()));
        }

        // 判断用户是否为此店会员
        int count = merchantMemberInfoService.count(Wrappers.<MerchantMemberInfo>lambdaQuery().eq(MerchantMemberInfo::getMerchantId, orderInfo.getMerchantId()).eq(MerchantMemberInfo::getUserId, userInfo.getId()));
        if (count > 0) {
            BigDecimal discount = NumberUtil.sub(orderInfo.getOrderPrice(), NumberUtil.mul(orderInfo.getOrderPrice(), 0.8));
            orderInfo.setDiscount(NumberUtil.round(discount,2));
            orderInfo.setAfterOrderPrice(NumberUtil.mul(orderInfo.getOrderPrice(), 0.8));
            orderInfo.setIsMember("1");
        } else {
            orderInfo.setAfterOrderPrice(orderInfo.getOrderPrice());
            orderInfo.setIsMember("0");
        }
        return orderInfo;
    }

    /**
     * 修改订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public boolean editOrder(OrderInfo orderInfo) throws FebsException {
        List<OrderItemInfo> orderItemList = orderInfo.getOrderItemList();
        if (CollectionUtil.isEmpty(orderItemList)) {
            throw new FebsException("订单详情不能为空");
        }

        // 如果为外送 计算配送费用
        if ("1".equals(orderInfo.getType())) {
            // 获取送货地址
            AddressInfo addressInfo = addressInfoService.getById(orderInfo.getAddressId());
            // 获取商家地址
            MerchantInfo merchantInfo = merchantInfoService.getById(orderInfo.getMerchantId());

            // 计算公里数与配送费用
            double distance = LocationUtils.getDistance(merchantInfo.getLongitude().doubleValue(), merchantInfo.getLatitude().doubleValue(), addressInfo.getLongitude().doubleValue(), addressInfo.getLatitude().doubleValue());
            orderInfo.setDiscount(NumberUtil.div(new BigDecimal(distance), 1000));


            // 每公里两米
            orderInfo.setDistributionPrice(NumberUtil.mul(orderInfo.getDistributionPrice(), 2));
            orderInfo.setOrderPrice(NumberUtil.add(orderInfo.getOrderPrice(), orderInfo.getDistributionPrice()));
        }

        // 判断用户是否为此店会员
        int count = merchantMemberInfoService.count(Wrappers.<MerchantMemberInfo>lambdaQuery().eq(MerchantMemberInfo::getMerchantId, orderInfo.getMerchantId()).eq(MerchantMemberInfo::getUserId, orderInfo.getUserId()));
        if (count > 0) {
            BigDecimal discount = NumberUtil.sub(orderInfo.getOrderPrice(), NumberUtil.mul(orderInfo.getOrderPrice(), 0.8));
            orderInfo.setDiscount(discount);
            orderInfo.setAfterOrderPrice(NumberUtil.mul(orderInfo.getOrderPrice(), 0.8));
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItemInfo orderItem : orderItemList) {
            orderItem.setTotalPrice(NumberUtil.mul(orderItem.getTotalPrice(), orderItem.getAmount()));
            totalPrice = NumberUtil.add(totalPrice, orderItem.getTotalPrice());
        }
        // 添加订单
        return this.updateById(orderInfo);
    }

    /**
     * 获取ID获取订单详情
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> orderDetail(Integer id) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("order", null);
                put("user", null);
                put("merchant", null);
                put("orderItem", Collections.emptyList());
                put("address", null);
                put("staff", null);
                put("evaluate", null);
            }
        };
        // 获取订单信息
        OrderInfo orderInfo = this.getById(id);
        result.put("order", orderInfo);

        // 评价信息
        EvaluateInfo evaluateInfo = evaluateInfoService.getOne(Wrappers.<EvaluateInfo>lambdaQuery().eq(EvaluateInfo::getOrderId, orderInfo.getId()));
        result.put("evaluate", evaluateInfo);

        // 订单详情
        List<OrderItemInfo> orderItemInfoList = orderItemInfoService.list(Wrappers.<OrderItemInfo>lambdaQuery().eq(OrderItemInfo::getOrderId, orderInfo.getId()));
        // 获取菜品信息
        List<Integer> dishesIds = orderItemInfoList.stream().map(OrderItemInfo::getDishesId).distinct().collect(Collectors.toList());
        List<DishesInfo> dishesInfoList = dishesInfoService.list(Wrappers.<DishesInfo>lambdaQuery().in(DishesInfo::getId, dishesIds));
        Map<Integer, DishesInfo> dishesMap = dishesInfoList.stream().collect(Collectors.toMap(DishesInfo::getId, e -> e));

        for (OrderItemInfo orderItemInfo : orderItemInfoList) {
            if (CollectionUtil.isNotEmpty(dishesMap) && dishesMap.get(orderItemInfo.getDishesId()) != null) {
                DishesInfo dishesInfo = dishesMap.get(orderItemInfo.getDishesId());
                orderItemInfo.setDishesName(dishesInfo.getName());
                orderItemInfo.setImages(dishesInfo.getImages());
            }
        }
        result.put("orderItem", orderItemInfoList);

        // 用户信息
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        result.put("user", userInfo);

        // 所属商家
        MerchantInfo merchantInfo = merchantInfoService.getById(orderInfo.getMerchantId());
        result.put("merchant", merchantInfo);

        if ("1".equals(orderInfo.getType())) {
            // 用户收货地址
            if (orderInfo.getAddressId() != null) {
                AddressInfo addressInfo = addressInfoService.getById(orderInfo.getAddressId());
                result.put("address", addressInfo);
            }

            // 配送员工
            if (orderInfo.getStaffId() != null) {
                StaffInfo staffInfo = staffInfoService.getById(orderInfo.getStaffId());
                result.put("staff", staffInfo);
            }
        }
        return result;
    }

    /**
     * 查询可卖商家
     *
     * @return 结果
     */
    @Override
    public List<MerchantInfo> selectMerchantList(String key) {
        // 获取所有商家
        List<MerchantInfo> merchantList = merchantInfoService.list(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getStatus, "1")
                .like(StrUtil.isNotEmpty(key), MerchantInfo::getName, key).or()
                .like(StrUtil.isNotEmpty(key), MerchantInfo::getDishes, key).or()
                .like(StrUtil.isNotEmpty(key), MerchantInfo::getContent, key).or()
                .like(StrUtil.isNotEmpty(key), MerchantInfo::getAddress, key));
        if (CollectionUtil.isEmpty(merchantList)) {
            return merchantList;
        }

        List<Integer> merchantIds = merchantList.stream().map(MerchantInfo::getId).collect(Collectors.toList());
        // 根据商家获取菜品
        List<DishesInfo> dishesList = dishesInfoService.list(Wrappers.<DishesInfo>lambdaQuery().in(DishesInfo::getMerchantId, merchantIds));
        Map<Integer, List<DishesInfo>> dishesMap = dishesList.stream().collect(Collectors.groupingBy(DishesInfo::getMerchantId));

        for (MerchantInfo merchantInfo : merchantList) {
            // 校验营业时间是否填写
            if (StrUtil.isEmpty(merchantInfo.getOperateDay()) || StrUtil.isEmpty(merchantInfo.getOperateStartTime()) || StrUtil.isEmpty(merchantInfo.getOperateEndTime())) {
                merchantInfo.setCurrentStatus("0");
                continue;
            }

            // 根据商家获取菜品
            List<DishesInfo> dishesInfoList = dishesMap.get(merchantInfo.getId());
            if (CollectionUtil.isEmpty(dishesInfoList)) {
                merchantInfo.setCurrentStatus("0");
                continue;
            }

            // 判断是否在营业日期内
            String week = cc.mrbird.febs.common.utils.DateUtil.getWeekOfDate(new Date());

            Map<String, String> weekMap = new HashMap<String, String>() {
                {
                    put("1", "周一");
                    put("2", "周二");
                    put("3", "周三");
                    put("4", "周四");
                    put("5", "周五");
                    put("6", "周六");
                    put("7", "周日");
                }
            };
            if (StrUtil.isNotEmpty(merchantInfo.getOperateDay())) {
                List<String> operateDayList = StrUtil.split(merchantInfo.getOperateDay(), ",");
                List<String> operateDayResult = new ArrayList<>();
                for (String s : operateDayList) {
                    operateDayResult.add(weekMap.get(s));
                }
                merchantInfo.setOperateDayList(operateDayResult);
            }
            if (!merchantInfo.getOperateDayList().contains(week)) {
                merchantInfo.setCurrentStatus("0");
                continue;
            }

            DateTime startTime = DateUtil.parseDateTime(DateUtil.formatDate(new Date()) + " " + merchantInfo.getOperateStartTime());
            DateTime endTime = DateUtil.parseDateTime(DateUtil.formatDate(new Date()) + " " + merchantInfo.getOperateEndTime());
            if (!DateUtil.isIn(new Date(), startTime, endTime)) {
                merchantInfo.setCurrentStatus("0");
                continue;
            }
            merchantInfo.setCurrentStatus("1");
        }
        return merchantList;
    }

    /**
     * 获取订单评价详情
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> evaluateDetail(Integer id) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("order", null);
                put("user", null);
                put("merchant", null);
                put("orderItem", Collections.emptyList());
                put("address", null);
                put("staff", null);
                put("evaluate", null);
            }
        };
        // 获取评价信息
        EvaluateInfo evaluateInfo = evaluateInfoService.getById(id);
        result.put("evaluate", evaluateInfo);

        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getId, evaluateInfo.getOrderId()));
        result.put("order", orderInfo);

        // 订单详情
        List<OrderItemInfo> orderItemInfoList = orderItemInfoService.list(Wrappers.<OrderItemInfo>lambdaQuery().eq(OrderItemInfo::getOrderId, orderInfo.getId()));
        // 获取菜品信息
        List<Integer> dishesIds = orderItemInfoList.stream().map(OrderItemInfo::getDishesId).distinct().collect(Collectors.toList());
        List<DishesInfo> dishesInfoList = dishesInfoService.list(Wrappers.<DishesInfo>lambdaQuery().in(DishesInfo::getId, dishesIds));
        Map<Integer, DishesInfo> dishesMap = dishesInfoList.stream().collect(Collectors.toMap(DishesInfo::getId, e -> e));

        for (OrderItemInfo orderItemInfo : orderItemInfoList) {
            if (CollectionUtil.isNotEmpty(dishesMap) && dishesMap.get(orderItemInfo.getDishesId()) != null) {
                DishesInfo dishesInfo = dishesMap.get(orderItemInfo.getDishesId());
                orderItemInfo.setDishesName(dishesInfo.getName());
                orderItemInfo.setImages(dishesInfo.getImages());
            }
        }
        result.put("orderItem", orderItemInfoList);

        // 用户信息
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        result.put("user", userInfo);

        // 所属商家
        MerchantInfo merchantInfo = merchantInfoService.getById(orderInfo.getMerchantId());
        result.put("merchant", merchantInfo);

        if ("1".equals(orderInfo.getType())) {
            // 用户收货地址
            if (orderInfo.getAddressId() != null) {
                AddressInfo addressInfo = addressInfoService.getById(orderInfo.getAddressId());
                result.put("address", addressInfo);
            }

            // 配送员工
            if (orderInfo.getStaffId() != null) {
                StaffInfo staffInfo = staffInfoService.getById(orderInfo.getStaffId());
                result.put("staff", staffInfo);
            }
        }
        return result;
    }

    /**
     * 订单支付
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderPay(String orderCode) {
        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
        orderInfo.setStatus("1");
        orderInfo.setPayDate(DateUtil.formatDateTime(new Date()));

        // 用户添加积分
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        userInfo.setIntegral(NumberUtil.add(userInfo.getIntegral(), orderInfo.getIntegral()));

        // 判断用户是否为此商家会员
        MerchantMemberInfo merchantMember = merchantMemberInfoService.getOne(Wrappers.<MerchantMemberInfo>lambdaQuery().eq(MerchantMemberInfo::getMerchantId, orderInfo.getMerchantId()).eq(MerchantMemberInfo::getUserId, orderInfo.getUserId()));
        if (merchantMember == null) {
            // 统计用户积分是否达到此商家会员
            List<OrderInfo> orderInfoList = this.list(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getUserId, orderInfo.getUserId()).eq(OrderInfo::getMerchantId, orderInfo.getMerchantId()));
            BigDecimal totalIntegral = orderInfoList.stream().map(OrderInfo::getIntegral).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 查询此商家会员积分要求
            MemberInfo memberInfo = memberInfoService.getOne(Wrappers.<MemberInfo>lambdaQuery().eq(MemberInfo::getMerchantId, orderInfo.getMerchantId()));
            if (memberInfo != null && memberInfo.getIntegral().compareTo((totalIntegral.add(orderInfo.getIntegral()))) <= 0) {
                // 设置用户为此商家会员
                MerchantMemberInfo merchantMemberInfo = new MerchantMemberInfo();
                merchantMemberInfo.setMerchantId(orderInfo.getMerchantId());
                merchantMemberInfo.setUserId(orderInfo.getUserId());
                // 总消费
                merchantMemberInfo.setConsumption(totalIntegral.add(orderInfo.getIntegral()));
                merchantMemberInfo.setLastDate(DateUtil.formatDateTime(new Date()));
                merchantMemberInfoService.save(merchantMemberInfo);
                // 邮箱通知
                if (StrUtil.isNotEmpty(userInfo.getMail())) {
                    MerchantInfo merchantInfo = merchantInfoService.getById(orderInfo.getMerchantId());
                    Context context = new Context();
                    context.setVariable("today", DateUtil.formatDate(new Date()));
                    context.setVariable("custom", userInfo.getName() + "，您好，在 " + merchantInfo.getName() + " 您已消费 " + memberInfo.getIntegral() + "元。获得此店会员资格（所有餐品消费八折）");
                    String emailContent = templateEngine.process("registerEmail", context);
                    mailService.sendHtmlMail(userInfo.getMail(), DateUtil.formatDate(new Date()) + "会员通知", emailContent);
                }
            }
        } else {
            // 更新消费记录
            merchantMember.setConsumption(merchantMember.getConsumption().add(orderInfo.getIntegral()));
            merchantMember.setLastDate(DateUtil.formatDateTime(new Date()));
            merchantMemberInfoService.updateById(merchantMember);
        }
        // 用户下单发送邮件
        if (StrUtil.isNotEmpty(userInfo.getMail())) {
            MerchantInfo merchantInfo = merchantInfoService.getById(orderInfo.getMerchantId());
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + "，您好，在 " + merchantInfo.getName() + " 消费订单 " + orderCode + "，已支付" + orderInfo.getAfterOrderPrice() + "元。");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getMail(), DateUtil.formatDate(new Date()) + "支付通知", emailContent);
        }

        // 更新用户积分
        userInfoService.updateById(userInfo);
        // 更新订单状态
        return this.updateById(orderInfo);
    }

    /**
     * 外送订单选择配送员
     *
     * @param orderCode 订单编号
     * @param staffId   员工ID
     * @return 结果
     */
    @Override
    public boolean checkDealer(String orderCode, Integer staffId) {
        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
        orderInfo.setStatus("2");
        orderInfo.setStaffId(staffId);

        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        // 配送发送邮件
        if (StrUtil.isNotEmpty(userInfo.getMail())) {
            MerchantInfo merchantInfo = merchantInfoService.getById(orderInfo.getMerchantId());
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + "，您好，在 " + merchantInfo.getName() + " 消费订单 " + orderCode + "，正在配送，请耐心等待");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getMail(), DateUtil.formatDate(new Date()) + "配送通知", emailContent);
        }
        // 更新订单状态
        return this.updateById(orderInfo);
    }
}
