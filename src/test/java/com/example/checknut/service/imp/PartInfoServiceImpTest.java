package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.PartInfo;
import com.example.checknut.mapper.PartInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.Query;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PartInfoServiceImpTest {

    @Autowired
    private PartInfoMapper partInfoMapper;


    @Test
    public void getPartInfo(){
        QueryWrapper<PartInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("partNum", "T155300360");

        PartInfo partInfo = partInfoMapper.selectOne(queryWrapper);
        System.out.println("nut1::::::::" + partInfo.getNut1());
    }

    @Test
    public void insertPartInfo(){
        PartInfo partInfo = new PartInfo();
        partInfo.setPhotoNum(5);
        partInfo.setBolt1(5);
        partInfo.setPartNum("T218888888");
        partInfo.setNut1(3);
        partInfo.setSpot1(6);
        partInfo.setLine1(9);
        partInfoMapper.insert(partInfo);
    }

}
