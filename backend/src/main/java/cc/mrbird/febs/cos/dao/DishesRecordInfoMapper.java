package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DishesRecordInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface DishesRecordInfoMapper extends BaseMapper<DishesRecordInfo> {

    /**
     * 分页获取菜品采购信息
     *
     * @param page             分页对象
     * @param dishesRecordInfo 菜品采购信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRecordByPage(Page<DishesRecordInfo> page, @Param("dishesRecordInfo") DishesRecordInfo dishesRecordInfo);
}
