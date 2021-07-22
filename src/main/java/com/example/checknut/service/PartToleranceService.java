package com.example.checknut.service;

import com.example.checknut.entity.PartTolerance;

public interface PartToleranceService {
    public int insertOrUpdatePartTolerance(PartTolerance partTolerance);
    public PartTolerance getPartToleranceByPartNum(String partNum, String itemNum);
    public int deleteAllPartTolerance();
    public int deletePartToleranceByPartNum(String partNum, String itemNum);
}
