package com.example.checknut.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.example.checknut.controller.WebSocketServer;
import com.example.checknut.entity.*;
import com.example.checknut.handler.CheckHandler;
import com.example.checknut.service.PartInfoService;
import com.example.checknut.service.imp.BasicInfoServiceImp;
import com.example.checknut.service.imp.CheckInfoServiceImp;
import com.example.checknut.service.imp.PartInfoServiceImp;
import com.example.checknut.service.imp.TotalNumServiceImp;
import com.example.checknut.utils.DateUtils;
import com.example.checknut.utils.SelfFileUtils;
import com.example.checknut.utils.SerialUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * TODO
 * 监听文件夹图片变化；
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-06 11:22
 */
@Slf4j
@Component
public class FileListener implements FileAlterationListener {

    //检验产品图片存放路径，与application.yml中的数据绑定
    @Value("${selfDefinition.imagePath}")
    private String imagePath;

    @Autowired
    private TotalNumServiceImp totalNumServiceImp;

    @Autowired
    private BasicInfoServiceImp basicInfoServiceImp;

    @Autowired
    private CheckInfoServiceImp checkInfoServiceImp;

    @Autowired
    private PartInfoServiceImp partInfoServiceImp;

    @SneakyThrows
    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {
    }

    @Override
    public void onDirectoryCreate(File file) {
    }

    @Override
    public void onDirectoryChange(File file) {
    }

    @Override
    public void onDirectoryDelete(File file) {
    }


    /**
     * 监听文件夹文件数量，达到数据库中零件图片数量时，对产品信息进行视觉处理；
     *
     * @param file
     */
    @SneakyThrows
    @Override
    public void onFileCreate(File file) {
        System.out.println("==onFileCreate==");
        File file1 = new File(imagePath);
        int n = 0, checkItem = 0, photoNum = 0, checkResult = 0;
        String partNum = "", partName = "", valueUser = "";
        if (file1.exists()) {
            n = SelfFileUtils.getDirectoriesNum(file1);
            //1 获取文件夹图片数量
            System.out.println("=========n==========" + n);

            //2 从BasicInfo数据库，取出当前零件号与检验内容
            BasicInfo basicInfo = basicInfoServiceImp.getBasicInfo();
            if (basicInfo != null) {
                partNum = basicInfo.getPartNum();
                checkItem = basicInfo.getCheckItem();
                valueUser = basicInfo.getValueUser();

                //3 根据零件号与检验内容，从PartInfo数据库中，取出零件图片数量
                if (null != partNum) {
                    PartInfo partInfo = partInfoServiceImp.getPartInfoByPartNum(partNum);
                    if (partInfo != null) {
                        photoNum = partInfo.getPhotoNum();
                        partName = partInfo.getPartName();
                        Map<String, Integer> map = new HashMap<>();

                        //4 判断文件夹图片数量是否满足要求
                        if (n == photoNum) {
                            //5 如果满足要求，传入openCV，检查图片
                            map = CheckHandler.process(partNum);
                            Mat mat = imread("D:\\testPic1\\nutp1.jpg", 1);
                            System.out.println("mat" + mat);

                            if (null != map) {
                                ReturnInfo returnInfo = new ReturnInfo();
                                checkResult = map.get("checkResult");

                                int totalNum = 0, conformNum = 0, unConformNum, riskOfConfirm = 0;
                                TotalNum totalNum1 = totalNumServiceImp.getTotalNum(partNum);
                                if (totalNum1 != null) {
                                    totalNum = totalNum1.getTotalNum() + 1;
                                    conformNum = totalNum1.getConformNum() + 1;
                                    totalNum1.setTotalNum(totalNum);
                                    totalNum1.setConformNum(conformNum);
                                    totalNumServiceImp.insertOrUpdateTotalNum(totalNum1);
                                    map.put("totalNum", totalNum);
                                    map.put("conformNum", conformNum);
                                    map.put("unConformNum", (totalNum - conformNum));
                                }

                                returnInfo.setNutMap(map);

                                CheckInfo checkInfo = new CheckInfo();
                                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                                String shortDate = df1.format(new Date());
                                Calendar c = Calendar.getInstance();
                                checkInfo.setCheckDate(shortDate)
                                        .setPartNum(partNum)
                                        .setPartName(partName)
                                        .setCheckItem(checkItem)
                                        .setValueUser(valueUser)
                                        .setCheckTime(DateUtils.getHMS(c));

                                //6 检查结果OK，向串口发出信号，将数据存入CheckInfo数据库
                                if (checkResult > 0) {
                                    checkInfo.setCheckStatus(1);
                                    //向串口输出数据
//                                SerialUtils.sendDataToPort();

                                    //信息传送至前端
                                    WebSocketServer.sendObjectTo(returnInfo.getNutMap());

                                    log.info("=OK======检验信息写入完成=======");
                                } else {
                                    checkInfo.setCheckStatus(2);
                                    log.info("=NO======检验信息写入完成=======");
                                }
                                checkInfoServiceImp.insertCheckInfo(checkInfo);
                                //7 清空文件夹内容
                                SelfFileUtils.delAllFile(imagePath);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onFileChange(File file) {
    }

    @Override
    public void onFileDelete(File file) {
    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {
    }
}
