package com.odm.gui.listeners;

import com.odm.gui.AddUrl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mohamed on 6/11/16.
 */
public class DownloadButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        new AddUrl();
    }
}
