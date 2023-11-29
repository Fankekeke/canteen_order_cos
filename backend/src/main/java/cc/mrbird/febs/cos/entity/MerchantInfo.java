package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalTime;
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
 * 商家管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商铺名称
     */
    private String name;

    /**
     * 商铺编号
     */
    private String code;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 负责人
     */
    private String principal;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 介绍
     */
    private String content;

    /**
     * 营业星期
     */
    private String operateDay;

    /**
     * 开始营业时间
     */
    private String operateStartTime;

    /**
     * 营业结束时间
     */
    private String operateEndTime;

    /**
     * 图片
     */
    private String images;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 菜系
     */
    private String dishes;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 商家管理员ID
     */
    private Integer userId;

    /**
     * 状态（0.下架 1.上架）
     */
    private String status;

    /**
     * 营业星期
     */
    @TableField(exist = false)
    private List<String> operateDayList;

    /**
     * 当时状态（0.未营业 1.营业中）
     */
    @TableField(exist = false)
    private String currentStatus;

}
