package com.example.checknut.service;

import com.example.checknut.entity.CheckInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-08 14:22
 */

public interface CheckInfoService {
    public int insertCheckInfo(CheckInfo checkInfo);
    public List<CheckInfo> getCheckInfoByDate(Date start, Date end);
    public List<CheckInfo> getAllCheckInfo();
    public int deleteAllCheckInfo();
    public int deleteCheckInfoByDate(Date end);
    public int generateExcelByMonth();
    public int generateExcelByDate(Date start, Date end) throws IOException;
    public int generateExcelByDateFromWeb(Date start, Date end) throws IOException;
}
