package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.CheckInfo;
import com.example.checknut.mapper.CheckInfoMapper;
import com.example.checknut.service.CheckInfoService;
import com.example.checknut.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-08 14:23
 */

@Service
@Slf4j
public class CheckInfoServiceImp implements CheckInfoService {

    @Autowired
    private CheckInfoMapper checkInfoMapper;

    @Override
    public int insertCheckInfo(CheckInfo checkInfo) {
        if (checkInfo != null) {
            checkInfoMapper.insert(checkInfo);
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 根据给定日期，返回CheckInfo列表
     * @param start 开始日期
     * @param end 结束日期，可以与开始日期相同
     * @return checkInfoList
     */
    @Override
    public List<CheckInfo> getCheckInfoByDate(Date start, Date end) {
        if ((null != start) && (null != end)) {
            QueryWrapper<CheckInfo> queryWrapper = new QueryWrapper<>();
            if (start.before(end)){
                start = DateUtils.getEndOfLastDay(start);
                end = DateUtils.getStartOfNextDay(end);
                queryWrapper.between("createtime", start, end);
            }else if (start.after(end)){
                end= DateUtils.getEndOfLastDay(end);
                start = DateUtils.getStartOfNextDay(start);
                queryWrapper.between("createtime", end, start);
            }else if (start.equals(end)){
                start = DateUtils.getEndOfLastDay(start);
                end = DateUtils.getStartOfNextDay(end);
                queryWrapper.between("createtime", start, end);
            }
            List<CheckInfo> checkInfoList = new ArrayList<CheckInfo>();
            checkInfoList = checkInfoMapper.selectList(queryWrapper);
            Set<CheckInfo> checkInfoSet = new LinkedHashSet<>();
            if (checkInfoList != null) {
                return checkInfoList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<CheckInfo> getAllCheckInfo() {
        return null;
    }

    @Override
    public int deleteAllCheckInfo() {
        return 0;
    }

    /**
     * 根据给定日期，获得下月第一天的00:00:00:000时间，并删除之前的数据
     * @param date
     * @return
     */
    @Override
    public int deleteCheckInfoByDate(Date date) {
        if (null != date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int month = c.get(Calendar.MONTH);
            date = DateUtils.getLastDayOfMonth(date);
            QueryWrapper<CheckInfo> checkInfoQueryWrapper = new QueryWrapper<>();
            checkInfoQueryWrapper.lt("createtime", date);
            checkInfoMapper.delete(checkInfoQueryWrapper);
            log.info(month + "月，信息删除完成");
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public int generateExcelByMonth() {
        return 0;
    }

    /**
     * 根据给定日期，生成excel
     * @param start
     * @param end
     * @return
     * @throws IOException
     */
    @Override
    public int generateExcelByDate(Date start, Date end) throws IOException {
        String PATH = "src/excelFile/";  //EXCEL存放路径
        String FONTNAME = "黑体";

        File file0 = new File(PATH);
        if (!file0.exists()) {
            file0.mkdirs();
        }

        Short titleFontSize = 20;//excle标题字号大小
        Short contentFontSize = 11;//excel内容字号大小

        //获取当前时间，用于文件命名
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String shortDate = df1.format(start);
        String fileType = "检验记录";
        String excelPath = PATH + fileType + shortDate + "audit.xlsx";

        List<CheckInfo> checkInfoList = new ArrayList<>();


        if (null !=getCheckInfoByDate(start, end)){
            checkInfoList = getCheckInfoByDate(start, end);
        }

        long beginExcel = System.currentTimeMillis();

        //创建工作簿
        Workbook workbook = new SXSSFWorkbook();

        //创建工作表
        Sheet sheet = workbook.createSheet();

        //设置标题格式
        CellStyle cellStyleTitle = workbook.createCellStyle();
        //标题居中
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        //标题背景色
        cellStyleTitle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //设置表头格式
        CellStyle cellStyleTitle1 = workbook.createCellStyle();
        //表头居中
        cellStyleTitle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle1.setVerticalAlignment(VerticalAlignment.CENTER);
        //表头背景色
        cellStyleTitle1.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        cellStyleTitle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //设置正文格式
        CellStyle cellStyleContent = workbook.createCellStyle();
        //正文居中
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER);
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);

        //自动换行
        cellStyleContent.setWrapText(true);
        //正文背景色
        cellStyleContent.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyleContent.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        //设置标题字体
        Font fontTile = workbook.createFont();
        fontTile.setFontName(FONTNAME);//字体
        fontTile.setFontHeightInPoints(titleFontSize);//字体大小
        cellStyleTitle.setFont(fontTile);

        //设置表头字体
        Font fontTile1 = workbook.createFont();
        fontTile1.setFontName(FONTNAME);//字体颜色
        fontTile1.setFontHeightInPoints(contentFontSize);//字体大小
        cellStyleTitle1.setFont(fontTile1);
        cellStyleContent.setFont(fontTile1);


        //EXCEL标题部分
        Row rowTitle = sheet.createRow(1);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("检验记录");
        //合并单元格
        CellRangeAddress region1 = new CellRangeAddress(1, 1, 0, 8);
        sheet.addMergedRegion(region1);
        cellTitle.setCellStyle(cellStyleTitle);

        //EXCEL表头部分,第4行
        Row rowTitle3 = sheet.createRow(3);
        Cell cellTitle3 = rowTitle3.createCell(0);
        cellTitle3.setCellValue("导出日期");
        cellTitle3.setCellStyle(cellStyleTitle1);

        Cell cellContent3 = rowTitle3.createCell(1);
        cellContent3.setCellValue(shortDate);
        cellContent3.setCellStyle(cellStyleContent);


        //EXCEL表头部分,第6行
        Row rowTitle6 = sheet.createRow(5);
        String[] tempTitleList6 = {"序号", "零件号", "检验类型", "检验数量", "检验日期","检验时间","检验班组", "检验结果", "备注"};
        for (int i = 0; i < tempTitleList6.length; i++) {
            Cell cellTitle6 = rowTitle6.createCell(i);
            cellTitle6.setCellValue(tempTitleList6[i]);
            cellTitle6.setCellStyle(cellStyleTitle1);
        }

        sheet.setColumnWidth(1, 256 * 15);//设置列宽
        sheet.setColumnWidth(4, 256 * 15);
        sheet.setColumnWidth(5, 256 * 15);
        sheet.setColumnWidth(6, 256 * 15);

        if (checkInfoList != null && checkInfoList.size() > 0) {

            String checkTypeTemp = null;
            String checkStatuTemp = null;


            for (int i = 0; i < checkInfoList.size(); i++) {

                //处理检验类型与检验状态
                //检验类型

                if (checkInfoList.get(i).getCheckItem() != null){
                    switch (checkInfoList.get(i).getCheckItem()) {
                        case 1:
                            checkTypeTemp = "螺母检验";
                            break;
                        case 2:
                            checkTypeTemp = "尺寸检验";
                            break;
                        default:
                            checkTypeTemp = "";
                            break;
                    }
                }else {
                    checkTypeTemp = "";
                }


                //判断检验结果
                if (checkInfoList.get(i).getCheckStatus() !=null){
                    switch (checkInfoList.get(i).getCheckStatus()) {
                        case 1:
                            checkStatuTemp = "合格";
                            break;
                        case 2:
                            checkStatuTemp = "不合格";
                            break;
                        default:
                            checkStatuTemp = "";
                            break;
                    }
                }else {
                    checkStatuTemp = "";
                }
                String tempDate = "";

                if (checkInfoList.get(i).getCreateTime() != null){
                    tempDate = df1.format(checkInfoList.get(i).getCreateTime());
                }

                //创建数组，存放每行信息，方便写入
                String[] tempList = {
                        String.valueOf(i + 1),
                        checkInfoList.get(i).getPartNum(),
                        checkTypeTemp,
                        "1",
                        tempDate,
                        checkInfoList.get(i).getCheckTime(),
                        checkInfoList.get(i).getValueUser(),
                        checkStatuTemp,
                        "",
                };

                //创建行
                Row row = sheet.createRow(i + 6);
                row.setHeight((short) (5 * 60));
                for (int j = 0; j < tempList.length; j++) {
                    //根据行，创建单元格
                    Cell cell = row.createCell(j);
                    cell.setCellValue(tempList[j]);
                    cell.setCellStyle(cellStyleContent);
                }

                log.info(shortDate + "检验 Excel写入完成===");

            }
            //数据写入excel
            FileOutputStream fos = new FileOutputStream(excelPath);
            workbook.write(fos);

            if (fos != null) {
                fos.flush();
                fos.getFD().sync();
                fos.close();
            }

            //清除临时文件
            ((SXSSFWorkbook) workbook).dispose();

            long endExcel = System.currentTimeMillis();
            log.info("==每日excel=time===" + (double) (endExcel - beginExcel) / 1000);
            return 1;
        }else {
            return 0;
        }
    }

    /**
     * 根据给定日期，生成excel
     * @param start
     * @param end
     * @return
     * @throws IOException
     */
    @Override
    public int generateExcelByDateFromWeb(Date start, Date end) throws IOException {
        String PATH = "src/excelFile/";  //EXCEL存放路径
        String FONTNAME = "黑体";

        File file0 = new File(PATH);
        if (!file0.exists()) {
            file0.mkdirs();
        }

        Short titleFontSize = 20;//excle标题字号大小
        Short contentFontSize = 11;//excel内容字号大小

        //获取当前时间，用于文件命名
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String shortDate = df1.format(new Date());
        String fileType = "查询检验记录";
        String excelPath = PATH+ shortDate + fileType  + "audit.xlsx";

        List<CheckInfo> checkInfoList = new ArrayList<>();


        if (null !=getCheckInfoByDate(start, end)){
            checkInfoList = getCheckInfoByDate(start, end);
        }

        long beginExcel = System.currentTimeMillis();

        //创建工作簿
        Workbook workbook = new SXSSFWorkbook();

        //创建工作表
        Sheet sheet = workbook.createSheet();

        //设置标题格式
        CellStyle cellStyleTitle = workbook.createCellStyle();
        //标题居中
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        //标题背景色
        cellStyleTitle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //设置表头格式
        CellStyle cellStyleTitle1 = workbook.createCellStyle();
        //表头居中
        cellStyleTitle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle1.setVerticalAlignment(VerticalAlignment.CENTER);
        //表头背景色
        cellStyleTitle1.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        cellStyleTitle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //设置正文格式
        CellStyle cellStyleContent = workbook.createCellStyle();
        //正文居中
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER);
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);

        //自动换行
        cellStyleContent.setWrapText(true);
        //正文背景色
        cellStyleContent.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyleContent.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        //设置标题字体
        Font fontTile = workbook.createFont();
        fontTile.setFontName(FONTNAME);//字体
        fontTile.setFontHeightInPoints(titleFontSize);//字体大小
        cellStyleTitle.setFont(fontTile);

        //设置表头字体
        Font fontTile1 = workbook.createFont();
        fontTile1.setFontName(FONTNAME);//字体颜色
        fontTile1.setFontHeightInPoints(contentFontSize);//字体大小
        cellStyleTitle1.setFont(fontTile1);
        cellStyleContent.setFont(fontTile1);


        //EXCEL标题部分
        Row rowTitle = sheet.createRow(1);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("检验记录");
        //合并单元格
        CellRangeAddress region1 = new CellRangeAddress(1, 1, 0, 8);
        sheet.addMergedRegion(region1);
        cellTitle.setCellStyle(cellStyleTitle);

        //EXCEL表头部分,第4行
        Row rowTitle3 = sheet.createRow(3);
        Cell cellTitle3 = rowTitle3.createCell(0);
        cellTitle3.setCellValue("导出日期");
        cellTitle3.setCellStyle(cellStyleTitle1);

        Cell cellContent3 = rowTitle3.createCell(1);
        cellContent3.setCellValue(shortDate);
        cellContent3.setCellStyle(cellStyleContent);


        //EXCEL表头部分,第6行
        Row rowTitle6 = sheet.createRow(5);
        String[] tempTitleList6 = {"序号", "零件号", "检验类型", "检验数量", "检验日期","检验时间","检验班组", "检验结果", "备注"};
        for (int i = 0; i < tempTitleList6.length; i++) {
            Cell cellTitle6 = rowTitle6.createCell(i);
            cellTitle6.setCellValue(tempTitleList6[i]);
            cellTitle6.setCellStyle(cellStyleTitle1);
        }

        sheet.setColumnWidth(1, 256 * 15);//设置列宽
        sheet.setColumnWidth(4, 256 * 15);
        sheet.setColumnWidth(5, 256 * 15);
        sheet.setColumnWidth(6, 256 * 15);

        if (checkInfoList != null && checkInfoList.size() > 0) {

            String checkTypeTemp = null;
            String checkStatuTemp = null;


            for (int i = 0; i < checkInfoList.size(); i++) {

                //处理检验类型与检验状态
                //检验类型

                if (checkInfoList.get(i).getCheckItem() != null){
                    switch (checkInfoList.get(i).getCheckItem()) {
                        case 1:
                            checkTypeTemp = "螺母检验";
                            break;
                        case 2:
                            checkTypeTemp = "尺寸检验";
                            break;
                        default:
                            checkTypeTemp = "";
                            break;
                    }
                }else {
                    checkTypeTemp = "";
                }


                //判断检验结果
                if (checkInfoList.get(i).getCheckStatus() !=null){
                    switch (checkInfoList.get(i).getCheckStatus()) {
                        case 1:
                            checkStatuTemp = "合格";
                            break;
                        case 2:
                            checkStatuTemp = "不合格";
                            break;
                        default:
                            checkStatuTemp = "";
                            break;
                    }
                }else {
                    checkStatuTemp = "";
                }
                String tempDate = "";

                if (checkInfoList.get(i).getCreateTime() != null){
                    tempDate = df1.format(checkInfoList.get(i).getCreateTime());
                }

                //创建数组，存放每行信息，方便写入
                String[] tempList = {
                        String.valueOf(i + 1),
                        checkInfoList.get(i).getPartNum(),
                        checkTypeTemp,
                        "1",
                        tempDate,
                        checkInfoList.get(i).getCheckTime(),
                        checkInfoList.get(i).getValueUser(),
                        checkStatuTemp,
                        "",
                };

                //创建行
                Row row = sheet.createRow(i + 6);
                row.setHeight((short) (5 * 60));
                for (int j = 0; j < tempList.length; j++) {
                    //根据行，创建单元格
                    Cell cell = row.createCell(j);
                    cell.setCellValue(tempList[j]);
                    cell.setCellStyle(cellStyleContent);
                }

                log.info(shortDate + "检验 Excel写入完成===");

            }
            //数据写入excel
            FileOutputStream fos = new FileOutputStream(excelPath);
            workbook.write(fos);

            if (fos != null) {
                fos.flush();
                fos.getFD().sync();
                fos.close();
            }

            //清除临时文件
            ((SXSSFWorkbook) workbook).dispose();

            long endExcel = System.currentTimeMillis();
            log.info("==每日excel=time===" + (double) (endExcel - beginExcel) / 1000);
            return 1;
        }else {
            return 0;
        }
    }
}
