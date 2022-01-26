package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;


public class Paint {
    //GUI components
    private JFrame frame = new JFrame();
    private DrawArea drawArea = new DrawArea();
    private JMenuBar menuBar = new JMenuBar();
    //buttons
    JRadioButton line = new JRadioButton("Line");
    JRadioButton brush = new JRadioButton("Brush");
    JRadioButton eraser = new JRadioButton("Eraser");

    JButton uploadImage = new JButton();
    JButton saveImage = new JButton();

    //labels
    JLabel clearLabel = new JLabel("Clear all");
    JLabel changeLabel = new JLabel("Change the color");
    JLabel uploadLabel = new JLabel("Upload an image");
    JLabel saveLabel = new JLabel("Save this paint");
    JLabel shapelabel = new JLabel("Shapes:");

    //shapes:
    JRadioButton triangle = new JRadioButton("Triangle");
    JRadioButton ovalShape = new JRadioButton("Oval");
    JRadioButton square = new JRadioButton("Square");
    JRadioButton rectangle = new JRadioButton("Rectangle");
    JRadioButton none = new JRadioButton("None");


    ButtonGroup group1 = new ButtonGroup();
    JRadioButton shapes[] = {none, ovalShape, triangle, square, rectangle};

    //saving & uploading
    JFileChooser chooser = new JFileChooser();
    BufferedImage bi;

    //timer care sa dea refresh la stilul de desen si shape ul selectat
    Timer timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //style
            if (brush.isSelected()) drawArea.style = Style.BRUSH;
            else if (line.isSelected()) drawArea.style = Style.LINE;
            else if (eraser.isSelected()) drawArea.style = Style.ERASER;
            //shape
            if(none.isSelected())drawArea.currentShape=Shape.NONE;
            else if(ovalShape.isSelected())drawArea.currentShape=Shape.OVAL;
            else if(triangle.isSelected())drawArea.currentShape=Shape.TRIANGLE;
            else if(rectangle.isSelected())drawArea.currentShape=Shape.RECTANGLE;
            else if(square.isSelected())drawArea.currentShape=Shape.SQUARE;
        }
    });

    Paint() {
        //frame
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Painting app");
        frame.setLayout(new BorderLayout());//stilul in care se vor aseza elementele in frame
        frame.setSize(new Dimension(1920, 1080));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);//frame-ul se va deschide by default fullscreen
        frame.setIconImage(new ImageIcon("C:\\Users\\Andre\\IdeaProjects\\UCreate\\res\\1.jpg").getImage());

        //menu bar
        menuBar.setPreferredSize(new Dimension(100, 20));
        menuBar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

        //clear all button
        JButton clear = new JButton();
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawArea.clear();
            }
        });
        clear.setPreferredSize(new Dimension(100, 70));
        clear.setFocusable(false);
        clear.setIcon(new ImageIcon("C:\\Users\\Andre\\IdeaProjects\\UCreate\\res\\2.png"));
        menuBar.add(clearLabel);
        menuBar.add(clear);

        //change color button
        menuBar.add(changeLabel);
        menuBar.add(drawArea.changeColor);

        //upload image
        uploadImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = chooser.showDialog(frame, "Select");
                chooser.getIcon(new File("C:\\Users\\Andre\\IdeaProjects\\UCreate\\res\\1.jpg"));

                if (response == JFileChooser.APPROVE_OPTION) {
                    try {
                        bi = ImageIO.read(chooser.getSelectedFile());
                        drawArea.g2.drawImage(bi, 0, 0, null);
                        drawArea.repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        uploadImage.setPreferredSize(new Dimension(100, 70));
        uploadImage.setFocusable(false);
        uploadImage.setIcon(new ImageIcon("C:\\Users\\Andre\\IdeaProjects\\UCreate\\res\\4.png"));
        menuBar.add(uploadLabel);
        menuBar.add(uploadImage);

        //save the image
        saveImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = chooser.showSaveDialog(frame);

                if (response == JFileChooser.APPROVE_OPTION) {
                    File file = new File(String.valueOf(chooser.getSelectedFile()) + ".jpg");
                    try {
                        ImageIO.write((RenderedImage) drawArea.image, "jpg", file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        saveImage.setPreferredSize(new Dimension(100, 70));
        saveImage.setFocusable(false);
        saveImage.setIcon(new ImageIcon("C:\\Users\\Andre\\IdeaProjects\\UCreate\\res\\5.png"));
        menuBar.add(saveLabel);
        menuBar.add(saveImage);


        //butoane care verifica cu ce stil sa deseneze userul
        ButtonGroup group = new ButtonGroup();
        group.add(line);
        group.add(brush);
        group.add(eraser);

        line.setFocusable(false);
        brush.setFocusable(false);
        eraser.setFocusable(false);
        line.setSelected(true);

        timer.start();

        JLabel label = new JLabel("Current tool:");
        menuBar.add(label);
        menuBar.add(line);
        menuBar.add(brush);
        menuBar.add(eraser);

        menuBar.add(new JLabel("Change the size"));

        menuBar.add(drawArea.slider);
        //shapes:
        group1.add(none);
        group1.add(ovalShape);
        group1.add(triangle);
        group1.add(square);
        group1.add(rectangle);

        shapelabel.setPreferredSize(new Dimension(80, 20));
        menuBar.add(shapelabel);
        shapes[0].setSelected(true);
        for (JRadioButton b: shapes) {
            b.setPreferredSize(new Dimension(90,15));
            b.setFocusable(false);
            menuBar.add(b);
        }

        //adds
        frame.add(menuBar, BorderLayout.WEST);
        frame.add(drawArea, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
