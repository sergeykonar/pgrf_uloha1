package models;

import java.awt.*;

public class Triangle {
    private double x, y, x1, x2, y1, y2;

    private Point point1, point2, point3;

    public Triangle(double x, double y, double x1, double x2, double y1, double y2) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Triangle(Point point1, Point point2, Point point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    public double getX() {
        return x;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public Point getPoint3() {
        return point3;
    }

    public double getY() {
        return y;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getY1() {
        return y1;
    }

    public double getY2() {
        return y2;
    }
}
