package com.odm.gui.listeners;

import com.github.axet.wget.info.DownloadInfo;
import com.odm.downloader.DownloadFileProcess;
import com.odm.downloader.DownloadNotifier;
import com.odm.gui.MainFrame;
import com.odm.gui.ProgressFrame;
import com.odm.persistence.entities.Download;
import com.odm.utility.Utility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mohamed on 6/14/16.
 */
public class StopButtonListener implements ActionListener {
    private DownloadFileProcess downloadFileProcess;
    private ProgressFrame progressFrame;
    public StopButtonListener(DownloadFileProcess downloadFileProcess,ProgressFrame progressFrame){
        this.downloadFileProcess = downloadFileProcess;
        this.progressFrame = progressFrame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton stopButton = (JButton) e.getSource();
        AtomicBoolean stop = downloadFileProcess.getStop();
        if(!stop.get()) {
            stopButton.setText(Utility.getLocalString("progress.button.resume"));
            downloadFileProcess.getStop().set(true);
        }else {
            DownloadInfo oldInfo = downloadFileProcess.getInfo();
            MainFrame mainFrame = downloadFileProcess.getMainFrame();
            Download download = downloadFileProcess.getDownload();
            Integer mainFramRow = downloadFileProcess.getMainFrameRow();
            DownloadNotifier oldDownloadNotifier = downloadFileProcess.getNotify();
            downloadFileProcess = new DownloadFileProcess();
            downloadFileProcess.setNotify(oldDownloadNotifier);
            downloadFileProcess.setInfo(oldInfo);
            downloadFileProcess.setProgressFrame(progressFrame);
            downloadFileProcess.setMainFrame(mainFrame);
            downloadFileProcess.setMainFrameRow(mainFramRow);
            downloadFileProcess.setDownload(download);
            downloadFileProcess.setTargetDirectory(progressFrame.getSavedFile());
            try {
                downloadFileProcess.setUrl(new URL(progressFrame.getUrl()));
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            downloadFileProcess.start();
            stopButton.setText(Utility.getLocalString("progress.button.pause"));
        }
    }
}
