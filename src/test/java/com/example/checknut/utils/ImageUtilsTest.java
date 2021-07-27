package com.example.checknut.utils;

import com.example.checknut.entity.Circle;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.circle;

class ImageUtilsTest {

    @Test
    public void partCheck(){


        OpenCVUtils.loadOpenCV();

        double start = System.currentTimeMillis();
        String path1 = "D:\\testPic1\\Image16.jpg";
        Mat mat = imread(path1,1);
        double prop = OpenCVUtils.calPropOfMat(mat);
        //浮点型参数，0-圆心差像素值，1-圆心xMin,2-圆心xMax,3-圆心Ymin,4-圆心Ymax
        double[] dp1 = {0.5,50,mat.cols(),50,mat.rows()};

        //整数参数，0-阈值最小，1-阈值最大，2-最小圆半径，3-最大圆半径
        int[] ip1 = {200,250,10,50};

        //圆心标准坐标
        org.opencv.core.Point[] cp = {new org.opencv.core.Point(729.0,241.0), new org.opencv.core.Point()};

        //圆心标准公差
        int[] ct = {50,50};

        //圆标准半径
        int[] cr = {19,25};

        //圆标准半径公差
        int[] rt = {1,1};
        List<Circle> circleList = new ArrayList<Circle>();
        circleList = ImageUtils.findCirclesByRadius(mat,prop,ip1[0],ip1[1],ip1[2],ip1[3],dp1[0],dp1[1],dp1[2],dp1[3],dp1[4]);

        Mat reMat = new Mat();
        Imgproc.resize(
                mat, reMat, new Size(mat.cols() * prop, mat.rows() * prop),
                0, 0, Imgproc.INTER_AREA);

        Map<String,Circle> map = new HashMap<>();
        //把List中的圆，放入map中；
        for (int i = 0; i < circleList.size(); i++) {
            Circle circle = circleList.get(i);
            if (null != circle){
                for (int j = 0; j < cp.length; j++) {
                    if (OpenCVUtils.checkCircleByCenter(circle, cp[j], ct[j], cr[j], rt[j])){
                        map.put("nut"+j,circle);
                    }
                }
            }
            OpenCVUtils.drawCircle(reMat,circle);
            System.out.println("--c=" + circle.getCenter() + "--r=" + circle.getRadius());
        }

//        System.out.println("map==" + map.get("nut0").getCenter());
        double end = System.currentTimeMillis();
        System.out.println("time ==" + (end-start)/1000);

        OpenCVUtils.findReferencePoints(reMat,5);

        imshow("reMat", reMat);

//
        //按任何键，关闭图片窗口
        while (true) {
            if (waitKey(0) == 27) {
                break;
            }
        }
        System.out.println("size ==" + circleList.size());
    }

    @Test
    public void threshold(){
        OpenCVUtils.loadOpenCV();

        double start = System.currentTimeMillis();
        String path1 = "D:\\testPic1\\Image16.jpg";
        Mat mat = imread(path1,1);
        double prop = OpenCVUtils.calPropOfMat(mat);

        int[] cc= ImageUtils.calThreshold(mat,20);
        double end = System.currentTimeMillis();
        System.out.println("time ==" + (end-start)/1000);
        System.out.println("cc============" + cc[0] + "==" + cc[1]);
    }

}
