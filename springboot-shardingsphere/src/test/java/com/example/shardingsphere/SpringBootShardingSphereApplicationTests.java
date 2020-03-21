package com.example.shardingsphere;

import com.example.shardingsphere.domain.TbOrder;
import com.example.shardingsphere.mapper.TbOrderItemMapper;
import com.example.shardingsphere.mapper.TbOrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class SpringBootShardingSphereApplicationTests {
    @Resource
    private TbOrderMapper tbOrderMapper;
    @Resource
    private TbOrderItemMapper tbOrderItemMapper;

    @Test
    public void testTborder() {
        TbOrder tbOrder = new TbOrder();
        tbOrder.setOrderId(1L);
        tbOrder.setUserId(1L);
        tbOrderMapper.insert(tbOrder);
    }
    @Test
    public void testTborder1() {
        TbOrder tbOrder = new TbOrder();
        tbOrder.setOrderId(2L);
        tbOrder.setUserId(2L);
        tbOrderMapper.insert(tbOrder);
    }
    @Test
    public void testSelectAll(){
        List<TbOrder> lists = tbOrderMapper.selectAll();
        lists.forEach(v-> System.out.println(v));
    }
}
