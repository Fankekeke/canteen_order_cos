package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商家会员管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantMemberInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属商家
     */
    private Integer merchantId;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 总消费
     */
    private BigDecimal consumption;

    /**
     * 上次消费时间
     */
    private String lastDate;

    @TableField(exist = false)
    private String merchantName;

    @TableField(exist = false)
    private String userName;

}
