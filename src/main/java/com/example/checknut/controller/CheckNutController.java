package com.example.checknut.controller;

import com.example.checknut.entity.BasicInfo;
import com.example.checknut.entity.PartInfo;
import com.example.checknut.entity.ReturnInfo;
import com.example.checknut.service.BasicInfoService;
import com.example.checknut.service.imp.BasicInfoServiceImp;
import com.example.checknut.service.imp.PartInfoServiceImp;
import com.example.checknut.service.imp.TotalNumServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 * 处理前端登录请求；
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-05 14:04
 */

@RestController
@RequestMapping("/basicinfo")
@CrossOrigin  //解决跨域
@Slf4j
public class CheckNutController {

    @Autowired
    private BasicInfoServiceImp basicInfoServiceImp;

    @Autowired
    private TotalNumServiceImp totalNumServiceImp;

    @Autowired
    private PartInfoServiceImp partInfoServiceImp;


    /**
     * 处理登录请求,将零件名称返回至前端；并清空basicInfo与totalNum数据库，
     * 将新数据从新写入上述数据库中；
     * @param partNum 零件号
     * @param checkItem 检验项目--1、焊接检验   2、尺寸检验
     * @return
     */

    @GetMapping("/login")
    public String checkPartNum(@RequestParam("partNum") String partNum, @RequestParam("checkItem") int checkItem,@RequestParam("valueUser") String valueUser){
        if ((null != partNum) && (checkItem >= 0)){
            //1 清空数据库数据
            basicInfoServiceImp.deleteAllBasicInfo();
            totalNumServiceImp.deleteTotalNumByPartNum(partNum);

            //2 将检验信息，写入数据库；方便后序获取partNum和checkItem;
            basicInfoServiceImp.insertOrUpdateBasicInfo(partNum, checkItem,valueUser);

            //3 根据零件号，从数据库中获取零件名称
            PartInfo partInfo = partInfoServiceImp.getPartInfoByPartNum(partNum);
            String partName = "";
            if (null != partInfo){
                partName = partInfo.getPartName();
            }
            return partName;
        }else {
            return "";
        }
    }
}
