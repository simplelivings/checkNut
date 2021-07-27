package com.example.checknut.controller;

import com.example.checknut.entity.ReturnInfo;
import com.example.checknut.service.imp.BasicInfoServiceImp;
import com.example.checknut.service.imp.CheckInfoMonthImp;
import com.example.checknut.service.imp.CheckInfoServiceImp;
import com.example.checknut.utils.SelfFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * TODO
 * 定时处理
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-17 13:00
 */

@RestController
@Slf4j
public class ScheduleController {

    @Autowired
    private ReturnInfo returnInfo;

    @Autowired
    private CheckInfoServiceImp checkInfoServiceImp;

    @Autowired
    private BasicInfoServiceImp basicInfoServiceImp;

    @Autowired
    private CheckInfoMonthImp checkInfoMonthImp;

    //检验产品图片存放路径，与application.yml中的数据绑定
    @Value("${selfDefinition.imagePath}")
    private String imagePath;

//    @Scheduled(cron = "${dap.testschedules}")
    private void testSchedule(){
        System.out.println("Schedule---" + returnInfo.getId());
    }

    /**
     * 每天的07:45生成，前一天的检验信息的excel表格，并清空basicInfo数据库与图片文件夹；
     */
    @Scheduled(cron = "${dap.checkSchedules}")
    private void generateExcelOfDay() throws IOException {
        //1 获取前一天日期
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-1);
        date = c.getTime();

        //2 生成每天检验信息excel表
        int n = checkInfoServiceImp.generateExcelByDate(date,date);

        //3 清空basicInfo数据库与图片文件夹
        if (n > 0){
            basicInfoServiceImp.deleteAllBasicInfo();
            File file = new File(imagePath);
            SelfFileUtils.delAllFile(imagePath);
        }
    }

    /**
     * 每月的1号和15号09:00，分别将上月之前的检验信息，汇总入checkInfoMonth数据库，并生成excel表；
     * （防止假期关机，数据误删）
     */
    @Scheduled(cron = "${dap.monthSchedules}")
    private void generateExcelOfMonth() throws IOException {
        //1 获取上个月日期
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        date = c.getTime();

        //2 数据汇总入checkInfoMonth
        checkInfoMonthImp.generateMonthData(date);

        //3 生成月度excel表
        checkInfoMonthImp.generateExcelByMonthEachYear(date);
    }

    /**
     * 每月15号10:00，删除4月之前的检验信息，删除前首先判断checkInfoMonth是否有数据
     * 有数据，则删除；否则不删除
     */
    @Scheduled(cron = "${dap.monthDeleteSchedules}")
    private void deleteCheckInfoByMonth(){
        //1 获取上个月日期
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -4);
        date = c.getTime();

        //2 查看checkInfoMonth数据库，是否有数据
        if (null != checkInfoMonthImp.getCheckInfoMonthByDate(date)){
            checkInfoServiceImp.deleteCheckInfoByDate(date);
        }
    }
}
