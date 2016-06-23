package com.odm.downloader;

import com.odm.gui.ProgressFrame;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mohamed on 6/12/16.
 */
public class DownloadStarter {
    public static void start(String url) throws MalformedURLException {

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

            ProgressFrame progressFrame = new ProgressFrame(downloader);
            progressFrame.setUrl(url);
            progressFrame.setSavedFile(target);


            downloader.setUiFrame(progressFrame);
            downloader.start();

        }



    }
}
