package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.PartInfo;
import com.example.checknut.mapper.PartInfoMapper;
import com.example.checknut.service.PartInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-08 14:58
 */
@Slf4j
@Service
public class PartInfoServiceImp implements PartInfoService {

    @Autowired
    private PartInfoMapper partInfoMapper;

    @Override
    public int insertOrUpdatePartInfo(PartInfo partInfo) {
        return 0;
    }

    @Override
    public PartInfo getPartInfoByPartNum(String partNum) {
        if (partNum != null){
            QueryWrapper<PartInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("partNum", partNum);
            PartInfo partInfo = partInfoMapper.selectOne(queryWrapper);
            if (partInfo != null){
                return partInfo;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    @Override
    public int deleteAllPartInfo() {
        return 0;
    }

    @Override
    public int deletePartInfoByPartNum(String partNum) {
        return 0;
    }
}
