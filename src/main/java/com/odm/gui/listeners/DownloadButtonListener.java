package com.odm.gui.listeners;

import com.odm.gui.AddUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mohamed on 6/11/16.
 */
@Component
public class DownloadButtonListener implements ActionListener {

    @Autowired
    private AddUrl addUrl;

    @Override
    public void actionPerformed(ActionEvent e) {
        addUrl.open();
    }
}
