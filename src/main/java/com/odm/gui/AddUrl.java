package com.odm.gui;

import com.odm.downloader.DownloadStarter;
import com.odm.utility.Utility;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sngv on 27/02/15.
 */
public class AddUrl extends JFrame{

    public AddUrl(){
        String url = JOptionPane.showInputDialog(this, Utility.getLocalString("main.fileUrl"),Utility.getLocalString("main.button.download"),JOptionPane.INFORMATION_MESSAGE);

        try {
            if(url != null && !url.isEmpty()) {
                new URL(url);
                DownloadStarter.start(url);
            }
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(this, "Invalid url", "Error", JOptionPane.OK_OPTION);
        }

    }

}

