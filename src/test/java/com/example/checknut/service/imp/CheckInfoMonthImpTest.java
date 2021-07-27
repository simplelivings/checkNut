package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.CheckInfo;
import com.example.checknut.entity.CheckInfoMonth;
import com.example.checknut.mapper.CheckInfoMonthMapper;
import com.example.checknut.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CheckInfoMonthImpTest {

    @Autowired
    private CheckInfoServiceImp checkInfoService;

    @Autowired
    private CheckInfoMonthMapper checkInfoMonthMapper;

    @Test
    public void generateMonthData() {
        Date date = new Date();
        Date firstDay = DateUtils.getFirstDayOfMonth(date);
        Date lastDay = DateUtils.getLastDayOfMonth(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        String valueUser = "";
        List<CheckInfo> checkInfoList = checkInfoService.getCheckInfoByDate(firstDay, lastDay);

        CheckInfoMonth checkInfoMonth = new CheckInfoMonth();
        CheckInfoMonth checkInfoMonth1 = new CheckInfoMonth();
        checkInfoMonth.setYear(year).setMonth(month);
        checkInfoMonth1.setYear(year).setMonth(month);

        //存放零件号，用于合并当月同类零件用；
        Set<String> partNumSet = new LinkedHashSet<>();

        //存放班组名，用于合并当月同班组零件用；
        Set<String> valueUerSet = new LinkedHashSet<>();

        if (null != checkInfoList && checkInfoList.size() > 0) {
            for (int i = 0; i < checkInfoList.size(); i++) {
                CheckInfo checkInfo = checkInfoList.get(i);
                if (null != checkInfo) {
                    partNumSet.add(checkInfo.getPartNum());
                }
            }
        }
        for (String s : partNumSet) {
            checkInfoMonth.setPartNum(s);
            checkInfoMonth1.setPartNum(s);
            for (int i = 0; i < checkInfoList.size(); i++) {
                CheckInfo checkInfo = checkInfoList.get(i);
                if (s.equals(checkInfo.getPartNum())) {
                    valueUerSet.add(checkInfo.getValueUser());
                }
            }

            for (String user : valueUerSet) {
                checkInfoMonth.setValueUser(user);
                checkInfoMonth1.setValueUser(user);
                int conform = 0, unConform = 0, sConform = 0, sUnconform = 0;
                for (int i = 0; i < checkInfoList.size(); i++) {
                    CheckInfo checkInfo = checkInfoList.get(i);
                    if (null != checkInfo) {
                        if (s.equals(checkInfo.getPartNum()) && user.equals(checkInfo.getValueUser())) {
                            //检验结果
                            int n = checkInfo.getCheckStatus();

                            //检验项目
                            int m = checkInfo.getCheckItem();
                            if (n > 0 && 1 == m) {
                                switch (n) {
                                    case 1:
                                        conform++;
                                        break;
                                    case 2:
                                        unConform++;
                                        break;
                                    default:
                                        break;
                                }
                            } else if (n > 0 && 2 == m) {
                                switch (n) {
                                    case 1:
                                        sConform++;
                                        break;
                                    case 2:
                                        sUnconform++;
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }
                    }
                }
                checkInfoMonth.setCheckItem(1)
                        .setConformNum(conform)
                        .setUnConformNum(unConform);
                insertOrUpdateCheckInfoMonth(checkInfoMonth);

                if (sConform > 0 || sUnconform > 0) {
                    checkInfoMonth1.setCheckItem(2)
                            .setConformNum(sConform)
                            .setUnConformNum(sUnconform);
                    insertOrUpdateCheckInfoMonth(checkInfoMonth1);
                }
            }
            valueUerSet.clear();
        }

    }

    public void insertOrUpdateCheckInfoMonth(CheckInfoMonth checkInfoMonth) {
        if (null != checkInfoMonth) {
            QueryWrapper<CheckInfoMonth> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("partNum", checkInfoMonth.getPartNum())
                    .eq("month", checkInfoMonth.getMonth())
                    .eq("valueUser", checkInfoMonth.getValueUser())
                    .eq("year", checkInfoMonth.getYear())
                    .eq("checkItem",checkInfoMonth.getCheckItem());
            CheckInfoMonth checkInfoMonthTemp = checkInfoMonthMapper.selectOne(queryWrapper);
            if (null != checkInfoMonthTemp) {
                checkInfoMonthMapper.update(checkInfoMonth, queryWrapper);
            } else {
                checkInfoMonthMapper.insert(checkInfoMonth);
            }
        }
    }

    @Test
    public void generateExcelByMonthEachYear() throws IOException {
        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        List<CheckInfoMonth> checkInfoMonthList = new ArrayList<>();

        String PATH = "src/excelFile/";  //EXCEL存放路径
        String FONTNAME = "黑体";

        File file0 = new File(PATH);
        if (!file0.exists()) {
            file0.mkdirs();
        }

        Short titleFontSize = 20;//excle标题字号大小
        Short contentFontSize = 11;//excel内容字号大小

        if (null != getCheckInfoMonthByDate(date)) {
            checkInfoMonthList = getCheckInfoMonthByDate(date);
        }

        //获取当前时间，用于文件命名
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String shortDate = df1.format(new Date());
        String fileType = "检验记录";
        String excelPath = "";
        if (month < 10) {
            excelPath = PATH + fileType + year + "0" + month + "audit.xlsx";
        } else {
            excelPath = PATH + fileType + year + month + "audit.xlsx";
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
        CellRangeAddress region1 = new CellRangeAddress(1, 1, 0, 9);
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
        String[] tempTitleList6 = {"序号", "零件号", "检验年份", "检验月份", "检验班组", "检验类型", "合格数量", "不合格数量", "合计", "备注"};
        for (int i = 0; i < tempTitleList6.length; i++) {
            Cell cellTitle6 = rowTitle6.createCell(i);
            cellTitle6.setCellValue(tempTitleList6[i]);
            cellTitle6.setCellStyle(cellStyleTitle1);
        }

        sheet.setColumnWidth(1, 256 * 15);//设置列宽
        sheet.setColumnWidth(4, 256 * 15);
        sheet.setColumnWidth(6, 256 * 15);

        if (checkInfoMonthList != null && checkInfoMonthList.size() > 0) {

            String checkTypeTemp = null;
            String checkStatuTemp = null;


            for (int i = 0; i < checkInfoMonthList.size(); i++) {

                //处理检验类型与检验状态
                //检验类


                String tempDate = df1.format(checkInfoMonthList.get(i).getCreateTime());

                CheckInfoMonth checkInfoMonth = checkInfoMonthList.get(i);
                if (null != checkInfoMonth) {

                    int checkItem = 0;
                    if (null != checkInfoMonth.getCheckItem()) {
                        checkItem = checkInfoMonth.getCheckItem();
                    }
                    if (checkItem >= 0) {
                        switch (checkItem) {
                            case 1:
                                checkTypeTemp = "螺母检验";
                                break;
                            case 2:
                                checkTypeTemp = "尺寸检验";
                            default:
                                break;
                        }
                    }

                    int totalNum = checkInfoMonth.getConformNum() + checkInfoMonth.getUnConformNum();
                    //创建数组，存放每行信息，方便写入
                    String[] tempList = {
                            String.valueOf(i + 1),
                            checkInfoMonth.getPartNum(),
                            checkInfoMonth.getYear().toString(),
                            checkInfoMonth.getMonth().toString(),
                            checkInfoMonth.getValueUser(),
                            checkTypeTemp,
                            "",
                            "",
                            "",
                            "",
                    };

                    int[] tempNum = {checkInfoMonth.getConformNum(), checkInfoMonth.getUnConformNum(), totalNum};

                    //创建行
                    Row row = sheet.createRow(i + 6);
                    row.setHeight((short) (5 * 60));
                    for (int j = 0; j < tempList.length; j++) {
                        //根据行，创建单元格
                        Cell cell = row.createCell(j);
                        cell.setCellValue(tempList[j]);
                        cell.setCellStyle(cellStyleContent);
                    }
                    for (int j = 0; j < tempNum.length; j++) {
                        Cell cell = row.createCell(j + 6);
                        cell.setCellValue(tempNum[j]);
                        cell.setCellStyle(cellStyleContent);
                    }

                    log.info(year + "年" + month + "月" + "检验 Excel写入完成===");
                }

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
            log.info("==每月excel=time===" + (double) (endExcel - beginExcel) / 1000);
        }
    }

    public List<CheckInfoMonth> getCheckInfoMonthByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        System.out.println("month===" + month);
        QueryWrapper<CheckInfoMonth> checkInfoMonthQueryWrapper = new QueryWrapper<>();
        checkInfoMonthQueryWrapper.eq("year", year).eq("month", month);

        List<CheckInfoMonth> checkInfoMonthList = new ArrayList<>();
        checkInfoMonthList = checkInfoMonthMapper.selectList(checkInfoMonthQueryWrapper);
        System.out.println("checkInfoMonthList====" + checkInfoMonthList);
        return checkInfoMonthList;
    }

}
