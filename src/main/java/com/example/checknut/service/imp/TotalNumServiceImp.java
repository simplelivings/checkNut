package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.TotalNum;
import com.example.checknut.mapper.TotalNumMapper;
import com.example.checknut.service.TotalNumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-22 13:10
 */
@Service
public class TotalNumServiceImp implements TotalNumService {

    @Autowired
    private TotalNumMapper totalNumMapper;

    @Override
    public int insertOrUpdateTotalNum(TotalNum totalNum) {
        if (null != totalNum) {
            QueryWrapper<TotalNum> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("partNum", totalNum.getPartNum());
            TotalNum totalNum1 = totalNumMapper.selectOne(queryWrapper);
            if (null != totalNum1) {
                totalNumMapper.update(totalNum, queryWrapper);
                return 1;
            } else {
                totalNumMapper.insert(totalNum);
                return 2;
            }
        } else {
            return -1;
        }
    }

    @Override
    public TotalNum getTotalNum(String partNum) {
        if (null != partNum){
            QueryWrapper<TotalNum> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("partNum", partNum);
            TotalNum totalNum = totalNumMapper.selectOne(queryWrapper);
            if (null != totalNum) {
                return totalNum;
            } else {
                return null;
            }
        }else {
            return null;
        }
    }

    @Override
    public int deleteTotalNumByPartNum(String partNum) {
        return 0;
    }

}
