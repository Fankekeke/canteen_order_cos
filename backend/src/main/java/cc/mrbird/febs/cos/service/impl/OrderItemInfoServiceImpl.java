package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.OrderItemInfo;
import cc.mrbird.febs.cos.dao.OrderItemInfoMapper;
import cc.mrbird.febs.cos.service.IOrderItemInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author FanK
 */
@Service
public class OrderItemInfoServiceImpl extends ServiceImpl<OrderItemInfoMapper, OrderItemInfo> implements IOrderItemInfoService {

}
