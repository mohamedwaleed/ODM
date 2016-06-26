package com.odm.downloader;

import com.odm.gui.MainFrame;
import com.odm.gui.ProgressFrame;
import com.odm.persistence.entities.Download;
import com.odm.persistence.services.DownloadService;
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

    public  void start(String url) throws MalformedURLException {

        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileDialog.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File currentDirectory = fileDialog.getSelectedFile();
            String urlFileName = FilenameUtils.getName(url);
            File target = new File(currentDirectory.getAbsolutePath() + File.separator + urlFileName);


            DownloadFileProcess downloader = new DownloadFileProcess();
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


            Vector<String>vector= new Vector<>();
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
        }



    }
}
