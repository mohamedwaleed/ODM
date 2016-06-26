package com.odm.gui;

import com.odm.gui.entities.FrameOptions;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mohamed on 6/10/16.
 */
public class FrameConfigurator {
    public static void configure(JFrame frame,FrameOptions frameOptions) {
        if (frameOptions.isDefaultCloseOperation()) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        frame.setSize(frameOptions.getDimension());
        frame.setResizable(frameOptions.isResizable());
        frame.setVisible(frameOptions.isVisible());
        if(frameOptions.isCentered()) {
            setWindowPosition(frame);
        }
    }
    private static void setWindowPosition(JFrame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = frame.getWidth();
        int height = frame.getHeight();
        int x = (int) ((dimension.getWidth() - width) / 2);
        int y = (int) ((dimension.getHeight() - height) / 2);
        frame.setLocation(x, y);
    }
}
