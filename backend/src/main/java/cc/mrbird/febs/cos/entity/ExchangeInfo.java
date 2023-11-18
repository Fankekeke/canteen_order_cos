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
 * 积分兑换
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExchangeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 物品ID
     */
    private Integer materialId;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 消耗积分
     */
    private BigDecimal integral;

    /**
     * 创建时间
     */
    private String createDate;

    @TableField(exist = false)
    private String code;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String materialName;
}
