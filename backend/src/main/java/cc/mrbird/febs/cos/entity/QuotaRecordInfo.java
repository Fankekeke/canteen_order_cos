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
 * 额度充值记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QuotaRecordInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 金额
     */
    private BigDecimal quota;

    /**
     * 0.管理员添加 1.充值
     */
    private String type;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 创建时间
     */
    private String createDate;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String phone;
}
