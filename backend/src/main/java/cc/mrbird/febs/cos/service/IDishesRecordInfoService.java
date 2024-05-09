package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DishesRecordInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IDishesRecordInfoService extends IService<DishesRecordInfo> {

    /**
     * 分页获取菜品采购信息
     *
     * @param page             分页对象
     * @param dishesRecordInfo 菜品采购信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRecordByPage(Page<DishesRecordInfo> page, DishesRecordInfo dishesRecordInfo);
}
