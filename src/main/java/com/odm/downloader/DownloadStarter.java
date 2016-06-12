package com.odm.downloader;

import com.odm.gui.ProgressFrame;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mohamed on 6/12/16.
 */
public class DownloadStarter {
    public static void start(String url,File target) throws MalformedURLException {
        ProgressFrame progressFrame = new ProgressFrame();
        progressFrame.setUrl(url);
        progressFrame.setSavedFile(target);

        DownloadFileProcess downloader = new DownloadFileProcess();
        downloader.setUrl(new URL(url));
        downloader.setTargetDirectory(target);
        downloader.setUiFrame(progressFrame);
        downloader.start();
    }
}
