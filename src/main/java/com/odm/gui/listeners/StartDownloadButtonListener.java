package com.odm.gui.listeners;

import com.odm.downloader.DownloadFileProcess;
import com.odm.gui.DownloadInfoFrame;
import com.odm.gui.MainFrame;
import com.odm.gui.ProgressFrame;
import com.odm.persistence.entities.Download;
import com.odm.persistence.services.DownloadService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * Created by mohamed on 6/27/16.
 */
public class StartDownloadButtonListener implements ActionListener {


    private String url;
    private File target;
    private DownloadService downloadService;
    private MainFrame mainFrame;
    private DownloadInfoFrame downloadInfoFrame;

    public StartDownloadButtonListener(String url,File target,DownloadService downloadService,MainFrame mainFrame,DownloadInfoFrame downloadInfoFrame){
        this.url = url;
        this.target = target;
        this.downloadService = downloadService;
        this.mainFrame = mainFrame;
        this.downloadInfoFrame = downloadInfoFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        downloadInfoFrame.dispose();
        DownloadFileProcess downloader = new DownloadFileProcess();
        try {
            downloader.setUrl(new URL(url));
            downloader.setTargetDirectory(target);

            ProgressFrame progressFrame = new ProgressFrame();
            progressFrame.open(downloader);
            progressFrame.setUrl(url);
            progressFrame.setSavedFile(target);




            Download download = new Download();
            download.setFileName(target.getName());
            download.setDownloaded("");
            download.setProgress("");
            download.setStatus("");
            download.setSize("");
            download.setTimeLeft("");
            download.setTransferRate("");
            download = downloadService.saveDownload(download);


            Vector<String> vector= new Vector<>();
            vector.add(target.getName());
            vector.add("");
            vector.add("");
            vector.add("");
            vector.add("");
            vector.add("");
            vector.add("");
            int row = mainFrame.addDownloadListRecord(vector);

            downloader.setProgressFrame(progressFrame);
            downloader.setMainFrame(mainFrame);
            downloader.setMainFrameRow(row);
            downloader.setDownload(download);
            downloader.start();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getTarget() {
        return target;
    }

    public void setTarget(File target) {
        this.target = target;
    }

    public DownloadService getDownloadService() {
        return downloadService;
    }

    public void setDownloadService(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public DownloadInfoFrame getDownloadInfoFrame() {
        return downloadInfoFrame;
    }

    public void setDownloadInfoFrame(DownloadInfoFrame downloadInfoFrame) {
        this.downloadInfoFrame = downloadInfoFrame;
    }

}
