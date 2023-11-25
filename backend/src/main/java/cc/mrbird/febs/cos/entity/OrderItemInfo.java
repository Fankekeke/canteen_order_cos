package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单详情
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderItemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 菜品ID
     */
    private Integer dishesId;

    /**
     * 购买数量
     */
    private Integer amount;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;

    /**
     * 所属订单
     */
    private Integer orderId;

    /**
     * 菜品名称
     */
    @TableField(exist = false)
    private String dishesName;

    /**
     * 菜品图片
     */
    @TableField(exist = false)
    private String images;
}
