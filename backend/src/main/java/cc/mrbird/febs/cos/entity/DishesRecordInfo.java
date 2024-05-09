package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 菜品采购记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DishesRecordInfo implements Serializable {

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
     * 添加数量
     */
    private Integer num;

    /**
     * 添加时间
     */
    private String createDate;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String code;

    @TableField(exist = false)
    private String merchantName;

    @TableField(exist = false)
    private Integer merchantId;
}
