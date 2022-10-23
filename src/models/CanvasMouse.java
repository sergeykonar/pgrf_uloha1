package models;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class CanvasMouse {

    private JPanel panel;
    private RasterBufferedImage img;

    private Point start, end, startTriangle, endTriangle;


    private boolean isLineDragged, isTriangleDragged = false;
    private boolean drawTriangle, drawEqual = false;

    public CanvasMouse(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("Paint : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.requestFocus();
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        img = new RasterBufferedImage(800, 600);

        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_T:
                        drawTriangle = true;
                        startTriangle = null;
                        endTriangle = null;
                        img.drawTriangleHelp();
						drawEqual = false;
                        break;
                    case KeyEvent.VK_C:
                        img.clear();
                        img.deleteLines();
                        img.deleteTriangles();
                        panel.repaint();
                        startTriangle = null;
                        endTriangle = null;
                        break;
                    case KeyEvent.VK_L:
                        drawTriangle = false;
                        startTriangle = null;
                        endTriangle = null;
                        break;
                    case KeyEvent.VK_R:
                        drawTriangle = true;
                        drawEqual = true;
                        startTriangle = null;
                        endTriangle = null;
                        img.drawTriangleHelp();
                        break;
                }

                panel.repaint();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                start = new Point(e.getX(), e.getY());
                img.setPixel(e.getX(), e.getY(), Color.RED.getRGB());
                panel.repaint();

                if (drawTriangle) {
                    if (startTriangle == null) {
                        startTriangle = start;
                    } else if (endTriangle == null && startTriangle != null) {
                        endTriangle = start;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isLineDragged) {
                    img.saveLine(start, end);
                    isLineDragged = false;
                }
                if (isTriangleDragged && startTriangle != null && endTriangle != null ) {
                    Point current = new Point(e.getX(), e.getY());
                    Triangle triangle = new Triangle(startTriangle, endTriangle, current);
                    img.saveTriangle(triangle);
                    isTriangleDragged = false;
                    startTriangle = null;
                    endTriangle = null;
                }
                super.mouseReleased(e);
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (drawTriangle) {
                    isTriangleDragged = true;
                    if ((img.containsTriangles()) && !drawEqual && startTriangle != null && endTriangle != null) {
                        img.clear();
                        img.drawSavedLines();
                        img.drawSavedTriangles();

                        img.drawTriangle(startTriangle.getX(), startTriangle.getY(), endTriangle.getX(), endTriangle.getY(), e.getX(), e.getY());

                        panel.repaint();
                    } else {
                        if (startTriangle != null && endTriangle != null) {
                            img.clear();
                            img.drawSavedLines();
                            img.deleteTriangles();
                            img.drawTriangle(startTriangle.getX(), startTriangle.getY(), endTriangle.getX(), endTriangle.getY(), e.getX(), e.getY());
                            panel.repaint();
                        }
                    }

                    if (drawEqual) {
                        if (img.containsTriangles() && startTriangle != null && endTriangle != null) {
                            img.clear();
                            img.drawSavedLines();
                            img.drawSavedTriangles();
                            Point point2 = new Point((int)(startTriangle.x + 0.5 * (endTriangle.x - startTriangle.x)), (int)(endTriangle.y + 0.5 * (startTriangle.y - endTriangle.y)));
                            double p1 = point2.x - (point2.y - startTriangle.y);
                            Point p = new Point((int) p1,
                                    (int)(p1 + (point2.x - startTriangle.x)));

							img.drawTriangle(startTriangle.getX(), startTriangle.getY(), endTriangle.getX(), endTriangle.getY(), p.getX(),  p.getY());
                            panel.repaint();

                        }else {
                            if (startTriangle != null && endTriangle != null) {

                                img.clear();
                                img.drawSavedLines();
                                Point point2 = new Point((int)(startTriangle.x + 0.5 * (endTriangle.x - startTriangle.x)), (int)(endTriangle.y + 0.5 * (startTriangle.y - endTriangle.y)));
                                double p1 = point2.x - (point2.y - startTriangle.y);
                                Point p = new Point((int) p1,
                                        (int)(p1 + (point2.x - startTriangle.x)));

                                img.drawTriangle(startTriangle, endTriangle, new Point((int) p.getX(), (int) p.getY()));
                                panel.repaint();
                            }
                        }
                    }

                } else {
                    isLineDragged = true;
                    if (img.containsImg()) {
                        img.clear();
                        end = new Point(e.getX(), e.getY());
                        img.drawSavedLines();
                        img.drawSavedTriangles();
                        img.drawLine(start, end);
                        panel.repaint();
                    } else {
                        img.clear();
                        img.deleteLines();
                        img.drawSavedTriangles();
                        end = new Point(e.getX(), e.getY());
                        img.drawLine(start, end);
                        panel.repaint();
                    }
                }

                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });
    }

    public void clear() {
        Graphics gr = img.getGraphics();
        gr.setColor(new Color(0x2f2f2f));
        gr.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public void present(Graphics graphics) {
        graphics.drawImage(img.getImg(), 0, 0, null);
    }

    public void start() {
        clear();
        panel.repaint();
    }
}