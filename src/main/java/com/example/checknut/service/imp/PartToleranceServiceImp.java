package com.example.checknut.service.imp;

import com.example.checknut.entity.PartTolerance;
import com.example.checknut.service.PartToleranceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-08 15:11
 */
@Slf4j
@Service
public class PartToleranceServiceImp implements PartToleranceService {
    @Override
    public int insertOrUpdatePartTolerance(PartTolerance partTolerance) {
        return 0;
    }

    @Override
    public PartTolerance getPartToleranceByPartNum(String partNum, String itemNum) {
        return null;
    }

    @Override
    public int deleteAllPartTolerance() {
        return 0;
    }

    @Override
    public int deletePartToleranceByPartNum(String partNum, String itemNum) {
        return 0;
    }
}
