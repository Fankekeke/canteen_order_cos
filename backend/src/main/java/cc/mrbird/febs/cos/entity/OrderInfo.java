package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 所属商家
     */
    private Integer merchantId;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 折扣后价格
     */
    private BigDecimal afterOrderPrice;

    /**
     * 会员折扣
     */
    private BigDecimal discount;

    /**
     * 订单积分
     */
    private BigDecimal integral;

    /**
     * 状态（0.未支付 1.已支付 2.配送中 3.已收货）
     */
    private String status;

    /**
     * 订单类型（0.堂食 1.外送）
     */
    private String type;

    /**
     * 公里数
     */
    private BigDecimal kilometre;

    /**
     * 配送价格
     */
    private BigDecimal distributionPrice;

    /**
     * 下单时间
     */
    private String createDate;

    /**
     * 支付时间
     */
    private String payDate;

    /**
     * 送达时间
     */
    private String serviceDate;

    /**
     * 送货地址
     */
    private Integer addressId;

    /**
     * 员工ID
     */
    private Integer staffId;

    /**
     * 订单菜品
     */
    @TableField(exist = false)
    List<OrderItemInfo> orderItemList;

    /**
     * 是否为会员 0否 1是
     */
    @TableField(exist = false)
    private String isMember;

    @TableField(exist = false)
    private String orderItemListStr;

    @TableField(exist = false)
    private String merchantName;

    @TableField(exist = false)
    private String userName;
}
