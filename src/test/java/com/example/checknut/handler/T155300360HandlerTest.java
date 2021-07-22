package com.example.checknut.handler;

import com.example.checknut.utils.OpenCVUtils;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

@SpringBootTest
class T155300360HandlerTest {

    @Test
    public void Demo01(){
        String path = "D:\\testPic\\nut3.jpg";
        String path1 = "H:\\testPic1\\nut1.jpg";

        System.setProperty("java.awt.headless", "false");

        double prop = 0.2;
        int height = 0, width = 0, rows = 0, cols = 0; //resize后的图片尺寸
        int thresh = 135, maxVal = 145; //二值化阈值
        int eHeight = 3, eWidth = 3; //膨胀卷积核尺寸
        int dIteration = 1, eIteration = 1;//膨胀和腐蚀的迭代次数
        int cThreshold1 = 3, cThreshold2 = 3, apertureSize = 5; //边缘阈值与间隙尺寸
        int minRadius = 30, maxRadius = 50; //查找圆直径范围
        Scalar contoursColor = new Scalar(150, 5, 50, 50);//轮廓线颜色
        Scalar circleColor = new Scalar(0, 0, 220); //圆边线颜色；
        int contoursThick = 2, circleThick = 2; //轮廓及圆的厚度, 圆的厚度负值为实心圆
        Mat image, resizeImage, grayImage, bImage, dilateMat, erodeMat, cannyMat, hImage;
        int centerTS = 20; //基准圆，半径范围公差
        Point pCenter = new Point(215, 378); //基准圆坐标
        long start = System.currentTimeMillis();


        //1 连接openCV库
        OpenCVUtils.loadOpenCV();

        //2 读取图片
        image = imread(path, 1);
        resizeImage = new Mat();

        if (image != null) {
            //3 调整图片尺寸
            Imgproc.resize(
                    image, resizeImage, new Size(image.cols() * prop, image.rows() * prop),
                    0, 0, Imgproc.INTER_AREA);
            imshow("s", resizeImage);
            System.out.println("dddddddddd");
        }
        long end = System.currentTimeMillis();
        System.out.println("zzzzzzzzzzz");
//        printl("time=== " + (end-start));
        imwrite(path1,resizeImage);
        waitKey();
        //按任何键，关闭图片窗口
//        while (true) {
//            if (waitKey(0) == 27) {
//                break;
//            }
//        }
    }
}
