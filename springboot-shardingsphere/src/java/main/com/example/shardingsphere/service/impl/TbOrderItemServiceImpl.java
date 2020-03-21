package com.example.shardingsphere.service.impl;

import com.example.shardingsphere.mapper.TbOrderItemMapper;
import com.example.shardingsphere.service.TbOrderItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TbOrderItemServiceImpl implements TbOrderItemService {

    @Resource
    private TbOrderItemMapper tbOrderItemMapper;

}
