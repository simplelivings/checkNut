package com.example.checknut.service;

import com.example.checknut.entity.TotalNum;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-22 13:08
 */

public interface TotalNumService {

    public int insertOrUpdateTotalNum(TotalNum totalNum);
    public TotalNum getTotalNum(String partNum);
    public int deleteTotalNumByPartNum(String partNum);
}
