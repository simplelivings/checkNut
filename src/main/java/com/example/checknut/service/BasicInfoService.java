package com.example.checknut.service;

import com.example.checknut.entity.BasicInfo;

public interface BasicInfoService {

    public int insertOrUpdateBasicInfo(String partNum, int checkItem);
    public BasicInfo getBasicInfo();
    public int deleteBasicInfoByPartNum(String partNum);
    public int deleteAllBasicInfo();
}
