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
 * 菜品管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DishesInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 菜品编号
     */
    private String code;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品描述
     */
    private String content;

    /**
     * 原料
     */
    private String rawMaterial;

    /**
     * 份量
     */
    private Integer portion;

    /**
     * 口味
     */
    private String taste;

    /**
     * 价格
     */
    private BigDecimal unitPrice;

    /**
     * 销量
     */
    private Integer saleNum;

    /**
     * 状态（0.下架 1.上架）
     */
    private String status;

    /**
     * 所属商家
     */
    private Integer merchantId;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 图片
     */
    private String images;

    /**
     * 热量
     */
    private BigDecimal heat;

    /**
     * 蛋白质
     */
    private BigDecimal protein;

    /**
     * 脂肪
     */
    private BigDecimal fat;

    @TableField(exist = false)
    private String merchantName;

}
