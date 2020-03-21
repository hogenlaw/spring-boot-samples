package com.example.shardingsphere.service.impl;

import com.example.shardingsphere.mapper.TbOrderMapper;
import com.example.shardingsphere.service.TbOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TbOrderServiceImpl implements TbOrderService {

    @Resource
    private TbOrderMapper tbOrderMapper;

}