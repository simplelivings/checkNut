package com.example.checknut.service;

import com.example.checknut.entity.PartInfo;

public interface PartInfoService {

    public int insertOrUpdatePartInfo(PartInfo partInfo);
    public PartInfo getPartInfoByPartNum(String partNum);
    public int deleteAllPartInfo();
    public int deletePartInfoByPartNum(String partNum);
}
