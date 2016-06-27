package com.odm.Application;

import com.odm.downloader.DownloadStarter;
import com.odm.gui.MainFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mohamed on 6/26/16.
 */
@Component
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private DownloadStarter downloadStarter;

    @Autowired
    private MainFrame mainFrame;

    @Override
    public void run(String... args) throws Exception {
        mainFrame.open();
        if(args == null || args.length == 0){
            return ;
        }

        try {
            URL url = new URL(args[0]);
            downloadStarter.start(args[0]);
        }catch (MalformedURLException e){
            JOptionPane.showMessageDialog(null, "Invalid url", "Error", JOptionPane.OK_OPTION);

        }

    }
}
