package com.example.checknut.service;

import com.example.checknut.entity.BasicInfo;

public interface BasicInfoService {

    public int insertOrUpdateBasicInfo(String partNum, int checkItem, String valueUser);
    public BasicInfo getBasicInfo();
    public int deleteBasicInfoByPartNum(String partNum);
    public int deleteAllBasicInfo();
}
