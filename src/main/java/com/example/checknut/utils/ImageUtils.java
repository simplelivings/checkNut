package com.example.checknut.utils;

import com.example.checknut.entity.Circle;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.RETR_LIST;
import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-06-15 15:49
 */

public class ImageUtils {

    /**
     * 与基准圆位置比较，判断目标圆位置是否超差
     * @param baseCircle 基准圆
     * @param targetCircle 目标圆
     * @param sDistance 圆心标准距离（换算后的实际距离）
     * @param ratio 换算比例（实际距离/软件距离）
     * @param t 圆心距离公差
     * @return 正值，表示圆心在标准范围内；负值，表示圆心超差；
     */
    public static int compareCircle(Circle baseCircle, Circle targetCircle, double sDistance, double ratio, double t) {
        double z = calCircleDis(baseCircle,targetCircle);
        if (Math.abs(z*ratio - sDistance) > t) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * 计算给定2个圆的圆心距离
     * @param circle1 给定圆1
     * @param circle2 给定圆2
     * @return z 返回的距离
     */
    public static double calCircleDis(Circle circle1, Circle circle2){
        if (circle1 !=null && circle2 !=null){
            Point circle1Center = circle1.getCenter();
            Point circle2Center = circle2.getCenter();
            double xPow = Math.pow(circle1Center.x - circle2Center.x, 2);
            double yPow = Math.pow(circle1Center.y - circle2Center.y, 2);
            double z = Math.sqrt(xPow + yPow);
            return z;
        }else {
            return -1;
        }
    }

    /**
     * 给定Mat,找到符合参数的圆List
     * @param image 原始图片的Mat
     * @param prop 缩放比例
     * @param thresh 二值化处理，阈值小
     * @param maxVal 二值化处理，阈值大
     * @param minRadius 最小半径
     * @param maxRadius 最大半径
     * @param xMin 圆心的x坐标下限
     * @param xMax 圆心的x坐标上限
     * @param yMin 圆心的y坐标下限
     * @param yMax 圆心的y坐标上限
     * @return circleList
     */
    public static List<Circle> findCirclesByRadius(Mat image, double prop, int thresh, int maxVal,int minRadius, int maxRadius, double centerMin, double xMin, double xMax, double yMin, double yMax){

        int height = 0, width = 0, rows = 0, cols = 0; //resize后的图片尺寸
        int eHeight = 3, eWidth = 3; //膨胀卷积核尺寸
        int dIteration = 1, eIteration = 1;//膨胀和腐蚀的迭代次数
        int cThreshold1 = 3, cThreshold2 = 3, apertureSize = 5; //边缘阈值与间隙尺寸
        Scalar contoursColor = new Scalar(150, 5, 50, 50);//轮廓线颜色
        Scalar circleColor = new Scalar(0, 0, 220); //圆边线颜色；
        int contoursThick = 2, circleThick = 2; //轮廓及圆的厚度, 圆的厚度负值为实心圆
        Mat  resizeImage, grayImage, bImage, dilateMat, erodeMat, cannyMat, hImage;
        long start = System.currentTimeMillis();

        List<Circle> circleList = new ArrayList<Circle>();//返回圆的List

        //1 连接openCV库,在程序入口已完成

        //2 读取图片
        if (image != null) {
            //3 调整图片尺寸
            resizeImage = new Mat();
            Imgproc.resize(
                    image, resizeImage, new Size(image.cols() * prop, image.rows() * prop),
                    0, 0, Imgproc.INTER_AREA);

            cols = resizeImage.cols();
            rows = resizeImage.cols();

            //4 灰度处理
            grayImage = new Mat();
            cvtColor(resizeImage, grayImage, Imgproc.COLOR_BGR2GRAY);

            //5 二值处理
            bImage = new Mat(height, width, CvType.CV_8UC1);
            Imgproc.threshold(grayImage, bImage, thresh, maxVal, Imgproc.THRESH_BINARY);
            imshow("bImage", bImage);


            //6 获取图像边缘-可选
            cannyMat = new Mat();
            Imgproc.Canny(bImage, cannyMat, cThreshold1, cThreshold2, apertureSize, true);

            //7 获取图像轮廓
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            hImage = new Mat();
            Imgproc.findContours(cannyMat, contours, hImage, RETR_LIST,
                    Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
            for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(resizeImage, contours, i, contoursColor, contoursThick);
            }
            // 找到圆的轮廓
            circleList = OpenCVUtils.findCircles(resizeImage, contours, centerMin, minRadius, maxRadius, xMin, xMax, yMin, yMax);
        }

        return circleList;
    }

    /**
     * 根据给定圆的数量，计算二值图像的阈值
     * @param image
     * @param n 圆的数量
     * @return int[] 0-thresh 1-maxVal
     */
    public static int[] calThreshold(Mat image, int n){
        double prop = OpenCVUtils.calPropOfMat(image);
        boolean flag = true;
        int thresh = 0, maxVal = 50, ns = 100;
        int[] ts = {0,0};
        List<Circle> circleList = new ArrayList<>();
        while (flag){
            circleList = findCirclesByRadius(image,prop,thresh,maxVal,5,100,0.5,0,image.cols(),0,image.rows());
            if (circleList.size() < n){
                if (maxVal <= 255){
                    thresh += ns;
                    maxVal += ns;
                }else if(ns > 0){
                    ns -= 10;
                    thresh = 0;
                    maxVal = 50;
                }else {
                    return null;
                }
            }else {
                ts[0] = thresh;
                ts[1] = maxVal;
                return ts;
            }
        }
        return null;
    }

}
