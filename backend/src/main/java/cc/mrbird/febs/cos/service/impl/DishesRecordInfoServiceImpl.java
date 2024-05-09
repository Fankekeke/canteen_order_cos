package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DishesRecordInfo;
import cc.mrbird.febs.cos.dao.DishesRecordInfoMapper;
import cc.mrbird.febs.cos.service.IDishesRecordInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class DishesRecordInfoServiceImpl extends ServiceImpl<DishesRecordInfoMapper, DishesRecordInfo> implements IDishesRecordInfoService {

    /**
     * 分页获取菜品采购信息
     *
     * @param page             分页对象
     * @param dishesRecordInfo 菜品采购信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectRecordByPage(Page<DishesRecordInfo> page, DishesRecordInfo dishesRecordInfo) {
        return baseMapper.selectRecordByPage(page, dishesRecordInfo);
    }
}
