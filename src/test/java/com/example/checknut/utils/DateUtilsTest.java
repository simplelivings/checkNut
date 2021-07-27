package com.example.checknut.utils;

import com.example.checknut.entity.Circle;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.circle;

class DateUtilsTest {

    @Test
    public void cc(){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH,1);
        c.set(Calendar.DATE,1);
        date = c.getTime();
        date = DateUtils.setHMSOfDayToZero(date);
        System.out.println("kkkkkkkkkk" + date);
    }

    @Test
    public void dd() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            Calendar c = Calendar.getInstance();
            System.out.println("=="+DateUtils.getHMS(c));
            Thread.sleep(5000);
        }
    }

    @Test
    public void fileTest(){
        String path = "D:\\testPic";
        File file = new File(path);
        int m = 0;
        if (null != file){
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (file1.isDirectory()){
                    m++;
                }
            }
        }
        System.out.println("m=" + m);
    }

    @Test
    public void propTest(){
        OpenCVUtils.loadOpenCV();
        double prop  = 1;

        Mat mat = imread("D:\\testPic1\\nutp1.jpg",1);

        prop = OpenCVUtils.calPropOfMat(mat);
        System.out.println("prop ==" + prop);
    }

    @Test
    public void imageUtils(){
        OpenCVUtils.loadOpenCV();
        double prop  = 1;

        Mat mat = imread("D:\\testPic1\\nutp1.jpg",1);
        List<Circle> circleList = new ArrayList<>();
//        circleList = ImageUtils.findCirclesByRadius(mat,0.5,100,150,10,100,0.5,100,300,50,200);
        circleList = ImageUtils.findCirclesByRadius(mat,prop,100,200,10,100,0.5,100,700,100,600);



        Mat reMat = new Mat();
        Imgproc.resize(
                mat, reMat, new Size(mat.cols() * prop, mat.rows() * prop),
                0, 0, Imgproc.INTER_AREA);

        for (int i = 0; i < circleList.size(); i++) {
            Circle circle = circleList.get(i);
            circle(reMat, circle.getCenter(), circle.getRadius(), new Scalar(70,240,60), 1);

        }
        OpenCVUtils.findReferencePoints(reMat, 5);

        imshow("mat",reMat);

        //按任何键，关闭图片窗口
        while (true) {
            if (waitKey(0) == 27) {
                break;
            }
        }

    }


}
