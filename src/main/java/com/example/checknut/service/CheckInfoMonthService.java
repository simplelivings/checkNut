package com.example.checknut.service;

import com.example.checknut.entity.CheckInfoMonth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface CheckInfoMonthService {
    public int generateMonthData(Date date);
    public int generateExcelByMonthEachYear(Date date) throws IOException;
    public int insertOrUpdateMonthDate(CheckInfoMonth checkInfoMonth);
    public List<CheckInfoMonth> getCheckInfoMonthByDate(Date date);
}
