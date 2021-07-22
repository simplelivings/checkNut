package com.example.checknut.utils;

import com.example.checknut.entity.Circle;
import org.opencv.core.Point;

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

}
