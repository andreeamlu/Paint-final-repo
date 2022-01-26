package com.company;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//this class represents the area where the user can draw
public class DrawArea extends JComponent implements ChangeListener {
    Color colorChoose = Color.BLACK;
    JButton changeColor = new JButton();
    JSlider slider = new JSlider(0, 100, 25);
    Style style = Style.LINE;
    Shape currentShape = Shape.NONE;
    Image image;
    Graphics2D g2;
    int w = slider.getValue();
    int h = slider.getValue();
    // coordonatele mouse-ului
    int currentX, currentY, oldX, oldY;


    public DrawArea() {
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //salveaza coordonatele lui x si y cand se apasa mouse ul
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        changeColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorChoose = JColorChooser.showDialog(null, "Change the color", Color.black);
                g2.setPaint(colorChoose);
            }
        });
        changeColor.setPreferredSize(new Dimension(100, 70));
        changeColor.setFocusable(false);
        changeColor.setIcon(new ImageIcon("C:\\Users\\Andre\\IdeaProjects\\UCreate\\res\\3.png"));

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                // coord x,y when drag mouse
                currentX = e.getX();
                currentY = e.getY();
                //setStroke- seteaza grosimea marginilor
                if (g2 != null) {
                    if (currentShape == Shape.NONE) {
                        switch (style) {
                            case LINE: {
                                g2.drawLine(oldX, oldY, currentX, currentY);
                                g2.setStroke(new BasicStroke(slider.getValue() / 10));
                                g2.setPaint(colorChoose);
                                repaint();
                                oldX = currentX;
                                oldY = currentY;
                            }
                            break;
                            case BRUSH: {
                                g2.fillOval(currentX, currentY, w, h);
                                g2.setPaint(colorChoose);
                                repaint();
                            }
                            break;
                            case ERASER: {
                                g2.fillOval(currentX, currentY, w, h);
                                g2.setPaint(Color.white);
                                repaint();
                            }
                            break;
                        }
                        //daca o forma este selectata:
                    } else switch (currentShape) {
                        case OVAL: {
                            g2.drawOval(currentX, currentY, w, h);
                            g2.setStroke(new BasicStroke(5));
                            g2.setPaint(colorChoose);
                            repaint();
                        }
                        break;
                        case RECTANGLE: {
                            g2.drawRect(currentX, currentY, h * 2, h);
                            g2.setStroke(new BasicStroke(5));
                            g2.setPaint(colorChoose);
                            repaint();
                        }
                        break;
                        case SQUARE: {
                            g2.drawRect(currentX, currentY, w, h);
                            g2.setStroke(new BasicStroke(5));
                            g2.setPaint(colorChoose);
                            repaint();
                        }
                        break;
                        case TRIANGLE: {
                            g2.drawPolygon(new int[]{currentX, currentX - slider.getValue(), currentX + slider.getValue()}, new int[]{currentY, currentY + slider.getValue() * 2, currentY + slider.getValue() * 2}, 3);
                            g2.setStroke(new BasicStroke(5));
                            g2.setPaint(colorChoose);
                            repaint();
                        }
                        break;
                    }
                }
            }
        });

        slider.setPreferredSize(new Dimension(100, 100));
        slider.setOrientation(SwingConstants.VERTICAL);
        slider.setFont(new Font("MV Boli", Font.BOLD, 10));
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(25);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.addChangeListener(this);

    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            //imaginea pe care urmeaza sa se deseneze este goala- se creeaza una
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            // antialiasing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // clear draw area
            clear();
        }

        g.drawImage(image, 0, 0, null);
    }

    public void clear() {
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        w = slider.getValue();
        h = slider.getValue();
    }
}
