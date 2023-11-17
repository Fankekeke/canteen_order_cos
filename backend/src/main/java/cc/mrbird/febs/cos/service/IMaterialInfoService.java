package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.MaterialInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IMaterialInfoService extends IService<MaterialInfo> {

    /**
     * 分页获取物品积分信息
     *
     * @param page        分页对象
     * @param materialInfo 物品积分信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectMaterialPage(Page<MaterialInfo> page, MaterialInfo materialInfo);
}
