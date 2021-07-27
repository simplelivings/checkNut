package com.example.checknut.handler;

import com.example.checknut.entity.Circle;
import com.example.checknut.entity.ReturnInfo;
import com.example.checknut.entity.TotalNum;
import com.example.checknut.service.imp.TotalNumServiceImp;
import com.example.checknut.utils.ImageUtils;
import com.example.checknut.utils.OpenCVUtils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-20 13:28
 */

public class T155300360 {

    /**
     * 根据相机照片，判断产品是否合格
     * 由CheckHandler.process方法调用
     * 并将检测结果信息，写入全局对象returnInfo中, 方便FileListener方法调用
     * @return 合格1，不合格-1
     */
    public static Map<String, Integer> checkPart(){
        String path1 = "D:\\testPic1\\Image16.jpg";
        String path2 = "D:\\testPic";
        String path3 = "D:\\testPic";
        Mat mat1 = imread(path1);
        double prop1 = OpenCVUtils.calPropOfMat(mat1);//图片缩放比例

        //浮点型参数，0-圆心差像素值，1-圆心xMin,2-圆心xMax,3-圆心Ymin,4-圆心Ymax
        double[] dp1 = {0.5,0,mat1.cols()*prop1,0,mat1.rows()*prop1};

        //整数参数，0-阈值最小，1-阈值最大，2-最小圆半径，3-最大圆半径
        int[] ip1 = {100,150,10,50};

        //圆心标准坐标
        Point[] cp = {new Point(729,241), new Point(0,0)};

        //圆心标准公差
        int[] ct = {50,50};

        //圆标准半径
        int[] cr = {19,25};

        //圆标准半径公差
        int[] rt = {1,1};

        //在图片中找到的圆的list
        List<Circle> circleList1 = new ArrayList<Circle>();

        //符合条件的map,0-主定位孔
        Map<String,Circle> map1 = new HashMap<>();

        //1 粗过滤符合条件的孔，找到孔的列表
        circleList1 = ImageUtils.findCirclesByRadius(mat1,prop1,ip1[0],ip1[1],ip1[2],ip1[3],dp1[0],dp1[1],dp1[2],dp1[3],dp1[4]);


        //2 精过滤孔，根据孔的圆心/圆心公差/半径/半径公差查找孔，把定位孔与螺母孔放入map中,nut0-定位孔
        for (int i = 0; i < circleList1.size(); i++) {
            Circle circle = circleList1.get(i);
            if (null != circle){
                for (int j = 0; j < cp.length; j++) {
                    if (OpenCVUtils.checkCircleByCenter(circle, cp[j], ct[j], cr[j], rt[j])){
                        map1.put("nut"+j,circle);
                    }
                }
            }
        }



        //3 判断孔的距离是否满足要求
        if (map1.size() > 0){

            //3.1 判断主定位孔是否存在，存在则判断距离；
            if (null != map1.get("nut0")){

            }else {
                //3.2 主定位孔不存在，用其他的孔去查找主定位孔

                //3.3 找到主定位孔，就用主定位孔判断距离

                //3.4 找不到主定位孔，就用螺母孔判断距离，并反馈主定位孔不存在
            }
        }



        Map<String, Integer> map = new HashMap<>();
        map.put("nut1", 1);
        map.put("nut2", 2);
        map.put("nut3", 2);
        map.put("nut4", 2);
        map.put("nut5", 2);
        map.put("nut6", 2);
        map.put("nut7", 2);
        map.put("result", 2);
        map.put("checkResult",1);
        return map;
    };
}
