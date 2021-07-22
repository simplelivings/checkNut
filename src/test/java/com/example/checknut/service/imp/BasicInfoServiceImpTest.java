package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.BasicInfo;
import com.example.checknut.mapper.BasicInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BasicInfoServiceImpTest {

    @Autowired
    private BasicInfoMapper basicInfoMapper;

    @Test
    public void getBasicInfo(){
//        QueryWrapper<BasicInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("partNum", "T155300360");
//        BasicInfo basicInfo = basicInfoMapper.selectOne(queryWrapper);

        BasicInfo basicInfo = null;
        List<BasicInfo> basicInfoList = basicInfoMapper.selectList(null);
        if (basicInfoList.size() > 0){
            basicInfo = basicInfoList.get(0);
        }


        System.out.println("ItemNum ====" + basicInfo.getCheckItem());
    }

    @Test
    public void insertBasicInfo(){
        basicInfoMapper.delete(null);
        BasicInfo b = new BasicInfo();
        b.setPartNum("cccccddd");
        b.setId(1);
        b.setCheckItem(2);
        basicInfoMapper.insert(b);
    }
}
