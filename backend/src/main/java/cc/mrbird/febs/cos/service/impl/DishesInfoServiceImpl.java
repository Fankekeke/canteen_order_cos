package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DishesInfo;
import cc.mrbird.febs.cos.dao.DishesInfoMapper;
import cc.mrbird.febs.cos.service.IDishesInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class DishesInfoServiceImpl extends ServiceImpl<DishesInfoMapper, DishesInfo> implements IDishesInfoService {

    /**
     * 分页获取菜品信息
     *
     * @param page       分页对象
     * @param dishesInfo 菜品信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDishesPage(Page<DishesInfo> page, DishesInfo dishesInfo) {
        return baseMapper.selectDishesPage(page, dishesInfo);
    }
}
