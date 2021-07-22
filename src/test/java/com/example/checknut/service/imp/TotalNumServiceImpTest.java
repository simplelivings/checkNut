package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.CheckInfoMonth;
import com.example.checknut.entity.TotalNum;
import com.example.checknut.mapper.TotalNumMapper;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TotalNumServiceImpTest {

    @Autowired
    private TotalNumMapper totalNumMapper;

    @Test
    public void insertOrUpdateTotalNum(){
        TotalNum totalNum = new TotalNum();
        totalNum.setTotalNum(100).setConformNum(50);

        if (null != totalNum) {
            QueryWrapper<TotalNum> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("partNum", totalNum.getPartNum());
            TotalNum totalNum1 = totalNumMapper.selectOne(queryWrapper);
            if (null != totalNum1) {
                totalNumMapper.update(totalNum, queryWrapper);
//                return 1;
            } else {
                totalNumMapper.insert(totalNum);
//                return 2;
            }
        } else {
//            return -1;
        }
    }

    @Test
    public void getTotalNum(){
        String partNum = "T155300360";
        QueryWrapper<TotalNum> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("partNum", partNum);
        TotalNum totalNum = totalNumMapper.selectOne(queryWrapper);
        if (null != totalNum) {
            System.out.println(totalNum);
//                return 1;
        } else {
//                return 2;
        }
    }

    @Test
    public void deleteTotalNumByPartNum(){
        String partNum = "T155300360";
        QueryWrapper<TotalNum> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("partNum", partNum);
        totalNumMapper.delete(queryWrapper);
    }

}
