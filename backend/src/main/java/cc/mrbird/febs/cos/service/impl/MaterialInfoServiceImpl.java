package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.MaterialInfo;
import cc.mrbird.febs.cos.dao.MaterialInfoMapper;
import cc.mrbird.febs.cos.service.IMaterialInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class MaterialInfoServiceImpl extends ServiceImpl<MaterialInfoMapper, MaterialInfo> implements IMaterialInfoService {

    /**
     * 分页获取物品积分信息
     *
     * @param page        分页对象
     * @param materialInfo 物品积分信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectMaterialPage(Page<MaterialInfo> page, MaterialInfo materialInfo) {
        return baseMapper.selectMaterialPage(page, materialInfo);
    }
}
