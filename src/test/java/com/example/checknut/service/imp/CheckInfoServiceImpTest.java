package com.example.checknut.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.checknut.entity.CheckInfo;
import com.example.checknut.mapper.CheckInfoMapper;
import com.example.checknut.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@Slf4j
class CheckInfoServiceImpTest {

    @Autowired
    private CheckInfoMapper checkInfoMapper;

    @Test
    public void getCheckInfo() {
        QueryWrapper<CheckInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("partNum", "T155300360");
        List<CheckInfo> checkInfoList = checkInfoMapper.selectList(null);
        for (CheckInfo checkInfo : checkInfoList) {
            System.out.println("==========" + checkInfo.getCreateTime());
        }
    }



//    @Test
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


    @Test
    public void generateExcelByDate() throws IOException {
        Date start = new Date();
        Date d = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d);
        c1.add(Calendar.DATE, -5);
        Date end = c1.getTime();

        String PATH = "src/excelFile/";  //EXCEL????????????
        String FONTNAME = "??????";

        File file0 = new File(PATH);
        if (!file0.exists()) {
            file0.mkdirs();
        }

        Short titleFontSize = 20;//excle??????????????????
        Short contentFontSize = 11;//excel??????????????????

        //???????????????????????????????????????
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String shortDate = df1.format(new Date());
        String fileType = "??????";
        String excelPath = PATH + fileType + shortDate + "audit.xlsx";

        List<CheckInfo> checkInfoList = new ArrayList<>();

        //????????????????????????
        Date checkDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(checkDate);
        c.add(Calendar.DATE, -1);
        checkDate = c.getTime();

//        assert getCheckInfoByDate(start, end) != null;
        checkInfoList = getCheckInfoByDate(start, end);

        long beginExcel = System.currentTimeMillis();

        //???????????????
        Workbook workbook = new SXSSFWorkbook();

        //???????????????
        Sheet sheet = workbook.createSheet();

        //??????????????????
        CellStyle cellStyleTitle = workbook.createCellStyle();
        //????????????
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        //???????????????
        cellStyleTitle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //??????????????????
        CellStyle cellStyleTitle1 = workbook.createCellStyle();
        //????????????
        cellStyleTitle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle1.setVerticalAlignment(VerticalAlignment.CENTER);
        //???????????????
        cellStyleTitle1.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        cellStyleTitle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //??????????????????
        CellStyle cellStyleContent = workbook.createCellStyle();
        //????????????
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER);
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);

        //????????????
        cellStyleContent.setWrapText(true);
        //???????????????
        cellStyleContent.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyleContent.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        //??????????????????
        Font fontTile = workbook.createFont();
        fontTile.setFontName(FONTNAME);//??????
        fontTile.setFontHeightInPoints(titleFontSize);//????????????
        cellStyleTitle.setFont(fontTile);

        //??????????????????
        Font fontTile1 = workbook.createFont();
        fontTile1.setFontName(FONTNAME);//????????????
        fontTile1.setFontHeightInPoints(contentFontSize);//????????????
        cellStyleTitle1.setFont(fontTile1);
        cellStyleContent.setFont(fontTile1);


        //EXCEL????????????
        Row rowTitle = sheet.createRow(1);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("????????????");
        //???????????????
        CellRangeAddress region1 = new CellRangeAddress(1, 1, 0, 6);
        sheet.addMergedRegion(region1);
        cellTitle.setCellStyle(cellStyleTitle);

        //EXCEL????????????,???4???
        Row rowTitle3 = sheet.createRow(3);
        Cell cellTitle3 = rowTitle3.createCell(0);
        cellTitle3.setCellValue("????????????");
        cellTitle3.setCellStyle(cellStyleTitle1);

        Cell cellContent3 = rowTitle3.createCell(1);
        cellContent3.setCellValue(shortDate);
        cellContent3.setCellStyle(cellStyleContent);


        //EXCEL????????????,???6???
        Row rowTitle6 = sheet.createRow(5);
        String[] tempTitleList6 = {"??????", "?????????", "????????????", "????????????", "????????????","????????????","????????????", "????????????", "??????"};
        for (int i = 0; i < tempTitleList6.length; i++) {
            Cell cellTitle6 = rowTitle6.createCell(i);
            cellTitle6.setCellValue(tempTitleList6[i]);
            cellTitle6.setCellStyle(cellStyleTitle1);
        }

        sheet.setColumnWidth(1, 256 * 15);//????????????
        sheet.setColumnWidth(4, 256 * 15);

        if (checkInfoList != null && checkInfoList.size() > 0) {

            String checkTypeTemp = null;
            String checkStatuTemp = null;


            for (int i = 0; i < checkInfoList.size(); i++) {

                //?????????????????????????????????
                //????????????

                if (checkInfoList.get(i).getCheckItem() != null){
                    switch (checkInfoList.get(i).getCheckItem()) {
                        case 1:
                            checkTypeTemp = "????????????";
                            break;
                        case 2:
                            checkTypeTemp = "????????????";
                            break;
                        default:
                            checkTypeTemp = "";
                            break;
                    }
                }


                //??????????????????
                if (checkInfoList.get(i).getCheckStatus() !=null){
                    switch (checkInfoList.get(i).getCheckStatus()) {
                        case 1:
                            checkStatuTemp = "??????";
                            break;
                        case 2:
                            checkStatuTemp = "?????????";
                            break;
                        default:
                            checkStatuTemp = "";
                            break;
                    }
                }

                String tempDate = df1.format(checkInfoList.get(i).getCreateTime());

                //????????????????????????????????????????????????
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

                //?????????
                Row row = sheet.createRow(i + 6);
                row.setHeight((short) (5 * 60));
                for (int j = 0; j < tempList.length; j++) {
                    //???????????????????????????
                    Cell cell = row.createCell(j);
                    cell.setCellValue(tempList[j]);
                    cell.setCellStyle(cellStyleContent);
                }

                log.info("===?????? Excel????????????===");

            }
            //????????????excel
            FileOutputStream fos = new FileOutputStream(excelPath);
            workbook.write(fos);

            if (fos != null) {
                fos.flush();
                fos.getFD().sync();
                fos.close();
            }

            //??????????????????
            ((SXSSFWorkbook) workbook).dispose();

            long endExcel = System.currentTimeMillis();
            log.info("===excel=time===" + (double) (endExcel - beginExcel) / 1000);
        }
    }

    @Test
    public void insertCheckInfo() {
        CheckInfo checkInfo = new CheckInfo();
        checkInfo.setPartNum("1111");
        checkInfo.setPartName("???????????????");
        checkInfoMapper.insert(checkInfo);
    }

    @Test
    public void deleteCheckInfoByDate(){
        Date date = new Date();
        if (null != date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH,1);
            date = c.getTime();
            date = DateUtils.getLastDayOfMonth(date);
            System.out.println("date====" + date);
            QueryWrapper<CheckInfo> checkInfoQueryWrapper = new QueryWrapper<>();
            checkInfoQueryWrapper.lt("createtime", date);
            checkInfoMapper.delete(checkInfoQueryWrapper);
        }
    }

}
