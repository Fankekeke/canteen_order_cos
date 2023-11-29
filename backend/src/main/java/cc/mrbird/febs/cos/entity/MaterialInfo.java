package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 物品积分
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MaterialInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 物品名称
     */
    private String name;

    private String code;

    /**
     * 物品介绍
     */
    private String content;

    /**
     * 图片
     */
    private String images;

    /**
     * 所需积分
     */
    private BigDecimal integral;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 销量
     */
    private Integer saleNum;


}
