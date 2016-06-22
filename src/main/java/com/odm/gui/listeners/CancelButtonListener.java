package com.odm.gui.listeners;

import com.odm.downloader.DownloadFileProcess;
import com.odm.gui.ProgressFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mohamed on 6/14/16.
 */
public class CancelButtonListener implements ActionListener{
    private DownloadFileProcess downloadFileProcess;
    private ProgressFrame progressFrame;

    public CancelButtonListener(DownloadFileProcess downloadFileProcess,ProgressFrame progressFrame){
        this.downloadFileProcess = downloadFileProcess;
        this.progressFrame = progressFrame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        downloadFileProcess.getStop().set(true);
        progressFrame.dispose();
    }
}
