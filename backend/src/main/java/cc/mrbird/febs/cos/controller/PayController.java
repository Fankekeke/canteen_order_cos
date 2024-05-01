package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AlipayBean;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.QuotaRecordInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.IQuotaRecordInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cc.mrbird.febs.cos.service.PayService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/cos/pay")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayController {

    private final PayService payService;

    private final IOrderInfoService orderInfoService;

    private final IQuotaRecordInfoService quotaRecordInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/alipay")
    public R saveOrder(OrderInfo orderInfo) throws AlipayApiException {
        orderInfo.setCode("ORD-" + System.currentTimeMillis());
        orderInfo.setAfterOrderPrice(NumberUtil.round(orderInfo.getAfterOrderPrice(), 2));
        orderInfo.setIntegral(orderInfo.getAfterOrderPrice());
        orderInfoService.saveOrder(orderInfo);
        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(orderInfo.getCode());
        alipayBean.setSubject(orderInfo.getCode());
        alipayBean.setTotal_amount(orderInfo.getAfterOrderPrice().toString());
        alipayBean.setBody("");
        String result = payService.aliPay(alipayBean);
        return R.ok(result);
    }

    /**
     * 新增订单信息【额度支付】
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/alipay/quota")
    @Transactional(rollbackFor = Exception.class)
    public R saveOrderByQuota(OrderInfo orderInfo) throws AlipayApiException {
        // 额度支付不更新积分，额度减少
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfo.getUserId()));

        orderInfo.setCode("ORD-" + System.currentTimeMillis());
        orderInfo.setAfterOrderPrice(NumberUtil.round(orderInfo.getAfterOrderPrice(), 2));
        orderInfo.setIntegral(orderInfo.getAfterOrderPrice());
        orderInfo.setStatus("1");
        orderInfo.setPayDate(DateUtil.formatDateTime(new Date()));
        orderInfoService.saveOrderByQutao(orderInfo);
        userInfo.setQuota(NumberUtil.sub(userInfo.getQuota(), orderInfo.getAfterOrderPrice()));
        return R.ok(userInfoService.updateById(userInfo));
    }

    /**
     * 添加用户额度
     * @param quotaRecordInfo 额度添加信息
     * @return 结果
     */
    @PostMapping("/saveUserQuota")
    @Transactional(rollbackFor = Exception.class)
    public R saveUserQuota(QuotaRecordInfo quotaRecordInfo) throws AlipayApiException {
        quotaRecordInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        quotaRecordInfo.setCode("QR-" + System.currentTimeMillis());
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, quotaRecordInfo.getUserId()));
        quotaRecordInfo.setUserId(userInfo.getId());
        quotaRecordInfoService.save(quotaRecordInfo);

        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(quotaRecordInfo.getCode());
        alipayBean.setSubject("用户额度充值");
        alipayBean.setTotal_amount(quotaRecordInfo.getQuota().toString());
        alipayBean.setBody("");
        String result = payService.aliPay(alipayBean);
        return R.ok(result);
    }

    /**
     * 阿里支付
     * @param subject
     * @param body
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/test")
    public R alipay(String outTradeNo, String subject, String totalAmount, String body) throws AlipayApiException {
        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(outTradeNo);
        alipayBean.setSubject(subject);
        alipayBean.setTotal_amount(totalAmount);
        alipayBean.setBody(body);
        String result = payService.aliPay(alipayBean);
        return R.ok(result);
    }

}
