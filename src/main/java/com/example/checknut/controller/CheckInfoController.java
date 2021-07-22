package com.example.checknut.controller;

import com.example.checknut.entity.CheckInfo;
import com.example.checknut.entity.CheckInfoReturn;
import com.example.checknut.entity.TempCheckInfo;
import com.example.checknut.service.imp.CheckInfoServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-20 15:35
 */
@RestController
@RequestMapping("/checkInfo")
@CrossOrigin  //解决跨域
@Slf4j
public class CheckInfoController {

    @Autowired
    private CheckInfoServiceImp checkInfoServiceImp;


    @GetMapping("/writeData")
    public int generateExcel(@RequestParam("startDate") String start, @RequestParam("endDate") String end) throws ParseException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (null != start && null != end){
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            if (checkInfoServiceImp.generateExcelByDateFromWeb(startDate,endDate) > 0){
                return 1;
            }else {
                return -1;
            }
        }else {
            return -1;
        }
    }

    @GetMapping("/getData")
    public CheckInfoReturn sendData(@RequestParam("startDate") String start, @RequestParam("endDate") String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (null != start && null != end){
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            CheckInfoReturn checkInfoReturn = new CheckInfoReturn();
            List<CheckInfo> checkInfoList = new ArrayList<>();
            List<TempCheckInfo> tempCheckInfoList = new ArrayList<TempCheckInfo>();
            checkInfoList = checkInfoServiceImp.getCheckInfoByDate(startDate,endDate);
            for (int i = 0; i < checkInfoList.size(); i++) {
                CheckInfo checkInfo = checkInfoList.get(i);
                String checkItem = "";
                String checkResult = "";
                if (null != checkInfo){
                    switch (checkInfo.getCheckItem()){
                        case 1:
                            checkItem = "螺母检验";
                            break;
                        case 2:
                            checkItem = "尺寸检验";
                            break;
                        default:
                            break;
                    }

                    switch (checkInfo.getCheckStatus()){
                        case 1:
                            checkResult = "合格";
                            break;
                        case 2:
                            checkResult = "不合格";
                            break;
                        default:
                            break;
                    }

                    TempCheckInfo tempCheckInfo = new TempCheckInfo();
                    tempCheckInfo.setPartNum(checkInfo.getPartNum())
                            .setCheckItem(checkItem)
                            .setCheckDate(checkInfo.getCheckDate())
                            .setCheckNum(1)
                            .setCheckResult(checkResult)
                            .setCheckNote("");
                    tempCheckInfoList.add(tempCheckInfo);
                }
            }
            if (null != checkInfoList){
                checkInfoReturn.setCheckInfoList(tempCheckInfoList);
                return checkInfoReturn;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

}
