package com.odm.downloader;

import com.github.axet.wget.SpeedInfo;
import com.github.axet.wget.WGet;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.ex.DownloadInterruptedError;
import com.github.axet.wget.info.ex.DownloadMultipartError;
import com.odm.gui.ProgressFrame;

import java.io.File;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mohamed on 6/11/16.
 */
public class DownloadFileProcess extends Thread{



    private AtomicBoolean stop = new AtomicBoolean(false);

    public DownloadInfo getInfo() {
        return info;
    }

    public void setInfo(DownloadInfo info) {
        this.info = info;
    }

    private DownloadInfo info;
    private SpeedInfo speedInfo = new SpeedInfo();
    private URL url;
    private File targetDirectory;
    private ProgressFrame progressFrame;
    private DownloadNotifier notify;
    @Override
    public void run() {
        download();
    }

    public void download() {


        // initialize url information object with or without proxy
        // info = new DownloadInfo(url, new ProxyInfo("proxy_addr", 8080, "login", "password"));

        if(info == null) {
            notify = new DownloadNotifier();
            notify.setSpeedInfo(speedInfo);
            notify.setUiFrame(progressFrame);
            info = new DownloadInfo(url);
            notify.setInfo(info);
            notify.setStop(stop);
            // extract information from the web
            info.extract(stop, notify);
        }

        try {
            downloadMultipart(notify);
        } catch (DownloadMultipartError e) {
            downloadSinglePart(notify);
        } catch (RuntimeException e) {
            if(!(e instanceof DownloadInterruptedError)) {
                // initialize wget object
                downloadSinglePart(notify);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void downloadSinglePart(DownloadNotifier notify) {
        // initialize wget object
        WGet w = new WGet(info, targetDirectory);
        w.download(stop, notify);
    }

    private void downloadMultipart(DownloadNotifier notify) {

        if(info.getCount() == 0) {
            // enable multipart download
            info.enableMultipart();
            // init speedinfo
            speedInfo.start(0);
        }
        // create wget downloader
        WGet w = new WGet(info, targetDirectory);

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
    public AtomicBoolean getStop() {
        return stop;
    }
    public void setStop(AtomicBoolean stop) {
        this.stop = stop;
    }

    public DownloadNotifier getNotify() {
        return notify;
    }

    public void setNotify(DownloadNotifier notify) {
        this.notify = notify;
    }
}
