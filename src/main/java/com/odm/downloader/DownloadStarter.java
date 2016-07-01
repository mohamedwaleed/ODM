package com.odm.downloader;

import com.github.axet.wget.info.DownloadInfo;
import com.odm.gui.DownloadInfoFrame;
import com.odm.gui.MainFrame;
import com.odm.gui.ProgressFrame;
import com.odm.persistence.entities.Download;
import com.odm.persistence.services.DownloadService;
import com.odm.utility.Utility;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * Created by mohamed on 6/12/16.
 */
@Component
public class DownloadStarter {

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private MainFrame mainFrame;

    @Autowired
    private DownloadInfoFrame downloadInfoFrame;

    public  void start(String url) throws MalformedURLException {


          DownloadInfo info = new DownloadInfo(new URL(url));
          info.extract();

          String formatedFileSize = Utility.getLocalString("progress.info.fileSize.unknown");
          Long fileSize = info.getLength();
          if (fileSize != null) {
              formatedFileSize = formatFileSize(info.getLength());
          }
          String urlFileName = info.getContentFilename();
          downloadInfoFrame.open(url,new File(urlFileName),formatedFileSize,info);


    }
    public static String formatFileSize(long s) {
        if (s / 1024f / 1024f / 1024f >= 1) {
            float f = s / 1024f / 1024f / 1024f;
            return String.format("%.1f ", f) + Utility.getLocalString("progress.gb");
        } else if (s / 1024f / 1024f >= 1) {
            float f = s / 1024f / 1024f;
            return String.format("%.1f ", f) + Utility.getLocalString("progress.mb");
        } else {
            float f = s / 1024f;
            return String.format("%.1f ", f) + Utility.getLocalString("progress.kb");
        }
    }
}
