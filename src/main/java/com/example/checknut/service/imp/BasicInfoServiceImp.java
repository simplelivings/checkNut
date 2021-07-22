package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.BasicInfo;
import com.example.checknut.mapper.BasicInfoMapper;
import com.example.checknut.service.BasicInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-08 15:50
 */

@Slf4j
@Service
public class BasicInfoServiceImp implements BasicInfoService {

    @Autowired
    private BasicInfoMapper basicInfoMapper;

    @Override
    public int insertOrUpdateBasicInfo(String partNum, int checkItem) {
        if (partNum != null && checkItem > 0){
            BasicInfo basicInfo = new BasicInfo(partNum, checkItem);
            basicInfo.setId(1);

            QueryWrapper<BasicInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("partNum",partNum);

            BasicInfo basicInfoExist = basicInfoMapper.selectOne(queryWrapper);
            if (basicInfoExist != null){
                basicInfoMapper.update(basicInfo, queryWrapper);
                return 1;
            } else {
                basicInfoMapper.insert(basicInfo);
                return 2;
            }
        }else {
            return -1;
        }
    }

    @Override
    public BasicInfo getBasicInfo() {
        BasicInfo basicInfo = null;
        List<BasicInfo> basicInfoList = basicInfoMapper.selectList(null);
        if (basicInfoList.size() > 0){
            basicInfo = basicInfoList.get(0);
        }
        return basicInfo;
    }

    @Override
    public int deleteBasicInfoByPartNum(String partNum) {
        return 0;
    }

    @Override
    public int deleteAllBasicInfo() {
        basicInfoMapper.delete(null);
        return 1;
    }
}
