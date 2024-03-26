package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AlipayBean;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.PayService;
import cn.hutool.core.util.NumberUtil;
import com.alipay.api.AlipayApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cos/pay")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayController {

    private final PayService payService;

    private final IOrderInfoService orderInfoService;

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
