package com.odm.gui.listeners;

import com.odm.downloader.DownloadFileProcess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mohamed on 6/14/16.
 */
public class CancelButtonListener implements ActionListener{
    private DownloadFileProcess downloadFileProcess;
    public CancelButtonListener(DownloadFileProcess downloadFileProcess){
        this.downloadFileProcess = downloadFileProcess;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        downloadFileProcess.interrupt();
    }
}
