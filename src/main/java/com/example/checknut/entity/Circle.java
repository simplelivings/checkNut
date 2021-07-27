package com.example.checknut.entity;

import org.opencv.core.Point;

/**
 * TODO
 * 圆实体
 * @version: 1.0
 * @author: faraway
 * @date: 2021-05-21 15:18
 */

public class Circle {
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    private int radius;
    private Point center;
}
