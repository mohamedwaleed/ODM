package com.odm.downloader;

import com.github.axet.wget.SpeedInfo;
import com.github.axet.wget.WGet;
import com.github.axet.wget.info.DownloadInfo;
import com.odm.gui.ProgressFrame;

import java.io.File;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mohamed on 6/11/16.
 */
public class DownloadFileProcess extends Thread{

    private AtomicBoolean stop = new AtomicBoolean(false);
    private DownloadInfo info;
    private SpeedInfo speedInfo = new SpeedInfo();
    private URL url;
    private File targetDirectory;
    private ProgressFrame progressFrame;

    @Override
    public void run() {
        download();
    }

    public void download() {
            DownloadNotifier notify = new DownloadNotifier();
            notify.setSpeedInfo(speedInfo);
            notify.setUiFrame(progressFrame);

            // initialize url information object with or without proxy
            // info = new DownloadInfo(url, new ProxyInfo("proxy_addr", 8080, "login", "password"));
            info = new DownloadInfo(url);
             notify.setInfo(info);

        // extract information from the web
            info.extract(stop, notify);
            // enable multipart download
            info.enableMultipart();
            // create wget downloader
            WGet w = new WGet(info, targetDirectory);
            // init speedinfo
            speedInfo.start(0);

            // will blocks until download finishes
            w.download(stop, notify);
    }


    public void setUrl(URL url) {
        this.url = url;
    }

    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public void setUiFrame(ProgressFrame progressFrame) {
        this.progressFrame = progressFrame;
    }
}
