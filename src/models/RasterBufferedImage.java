package models;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RasterBufferedImage implements Raster {

    private final BufferedImage img;
    private int color;

    private ArrayList<Line> lines = new ArrayList();
    private ArrayList<Triangle> triangles = new ArrayList<>();

    public RasterBufferedImage(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void repaint(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public void draw(RasterBufferedImage raster) {
        Graphics graphics = getGraphics();
        graphics.setColor(new Color(color));
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.drawImage(raster.img, 0, 0, null);
        // pro zájemce - co dělá observer - https://stackoverflow.com/a/1684476
    }

    public void drawLine(Point start, Point end) {
        img.getGraphics().drawLine(start.x, start.y, end.x, end.y);

    }

    public void saveLine(Point start, Point end){
        Line line = new Line(start, end);
        lines.add(line);
    }

    public void drawSavedLines() {
        for (Line line: lines){
            img.getGraphics().drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
        }
    }

    public void drawSavedTriangles() {
        for (Triangle triangle: triangles){
            drawTriangle(triangle.getPoint1(), triangle.getPoint2(), triangle.getPoint3());
        }
    }

    public void saveTriangle(Triangle triangle) {
        triangles.add(triangle);
    }

    public void drawTriangle(double x, double y, double x1, double y1, double x2, double y2) {

        int[] tX = new int[3];
        int[] tY = new int[3];

        tX[0] = (int) x;
        tX[1] = (int) x1;
        tX[2] = (int) x2;

        tY[0] = (int) y;
        tY[1] = (int) y1;
        tY[2] = (int) y2;

        img.getGraphics().drawPolygon(tX, tY, 3);
        drawTriangleHelp();
    }

    void drawTriangleHelp() {
        drawString(getGraphics(), "To draw triangle: \n1) Click on canvas to select start point\n2)Click on the canvas and drag mouse to draw", 5,  10);
    }

    public void printHelp() {
        drawString(getGraphics(), "Use mouse buttons. \nHot keys: C - clear, T - Draw triangle L - draw line, R - draw isosceles triangle", 5,  getHeight() - 60);
    }

    private void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }

    public void drawTriangle(Point point1, Point point2, Point point3) {
        int[] tX = new int[3];
        int[] tY = new int[3];

        tX[0] = (int) point1.getX();
        tX[1] = (int) point2.getX();
        tX[2] = (int) point3.getX();

        tY[0] = (int) point1.getY();
        tY[1] = (int) point2.getY();
        tY[2] = (int) point3.getY();

        Polygon polygon = new Polygon(tX, tY, 3);
        getGraphics().drawPolygon(polygon);
    }

    public boolean containsImg() {
        return !lines.isEmpty();
    }

    public boolean containsTriangles() {
        return !triangles.isEmpty();
    }
    public BufferedImage getImg() {
        return img;
    }

    public Graphics getGraphics() {
        return img.getGraphics();
    }

    @Override
    public int getPixel(int x, int y) {
        return img.getRGB(x, y);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        if(x >= 0 && y >= 0 && x < img.getWidth() - 1 && y < img.getHeight() - 1)
            img.setRGB(x, y, color);
    }

    @Override
    public void clear() {
        Graphics g = img.getGraphics();
        g.setColor(new Color(color));
        g.clearRect(0, 0, img.getWidth(), img.getHeight());
        printHelp();
    }

    public void deleteLines(){
        lines.clear();
    }

    public void deleteTriangles() {
        triangles.clear();
    }

    @Override
    public void setClearColor(int color) {
        this.color = color;
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }


}