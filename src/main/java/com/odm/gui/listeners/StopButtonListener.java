package com.odm.gui.listeners;

import com.odm.downloader.DownloadFileProcess;
import com.odm.utility.Utility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mohamed on 6/14/16.
 */
public class StopButtonListener implements ActionListener {
    private DownloadFileProcess downloadFileProcess;
    public StopButtonListener(DownloadFileProcess downloadFileProcess){
        this.downloadFileProcess = downloadFileProcess;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton stopButton = (JButton) e.getSource();
        AtomicBoolean stop = downloadFileProcess.getStop();
        if(!stop.get()) {
            stopButton.setText(Utility.getLocalString("progress.button.resume"));
            downloadFileProcess.getStop().set(true);
        }else {
            downloadFileProcess.getStop().set(false);
            downloadFileProcess.start();
            stopButton.setText(Utility.getLocalString("progress.button.pause"));
        }
    }
}
