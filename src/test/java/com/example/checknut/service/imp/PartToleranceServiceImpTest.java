package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.PartTolerance;
import com.example.checknut.mapper.PartToleranceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PartToleranceServiceImpTest {

    @Autowired
    private PartToleranceMapper partToleranceMapper;


    @Test
    public void getPartTolerance(){
        QueryWrapper<PartTolerance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("partNum","T155300360");
        queryWrapper.eq("itemNum","nut2");
        PartTolerance partTolerance = partToleranceMapper.selectOne(queryWrapper);
        System.out.println("tolerance: upper = " + partTolerance.getUpper());
    }

    @Test
    public void insertPartTolerance(){
        PartTolerance p = new PartTolerance();
        p.setPartNum("cccccccc");
        p.setItemNum("nut1");
        p.setUpper(5.0);
        p.setLower(4.0);
        partToleranceMapper.insert(p);
    }
}
