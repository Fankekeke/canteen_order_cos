package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.MemberInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IMemberInfoService extends IService<MemberInfo> {

    /**
     * 分页获取会员积分信息
     *
     * @param page        分页对象
     * @param memberInfo 会员积分信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectMemberPage(Page<MemberInfo> page, MemberInfo memberInfo);
}
