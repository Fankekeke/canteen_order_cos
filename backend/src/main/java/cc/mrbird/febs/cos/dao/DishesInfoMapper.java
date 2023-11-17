package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DishesInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface DishesInfoMapper extends BaseMapper<DishesInfo> {

    /**
     * 分页获取菜品信息
     *
     * @param page       分页对象
     * @param dishesInfo 菜品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDishesPage(Page<DishesInfo> page, @Param("dishesInfo") DishesInfo dishesInfo);
}
