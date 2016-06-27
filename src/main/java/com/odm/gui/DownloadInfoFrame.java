package com.odm.gui;

import com.odm.gui.entities.FrameOptions;
import com.odm.gui.listeners.StartDownloadButtonListener;
import com.odm.persistence.services.DownloadService;
import com.odm.utility.Utility;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by mohamed on 6/27/16.
 */
@Component
public class DownloadInfoFrame extends JFrame{

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private MainFrame mainFrame;

    private String url;

    private String fileSize;

    private File targetFile;

    private JFrame thisFrame = this;

    private JPanel panel;

    public void open(String url,File targetFile,String fileSize){
        this.url = url;
        this.targetFile = targetFile;
        this.fileSize = fileSize;
        prepareGui();
    }
    private void prepareGui() {
        setTitle(Utility.getLocalString("downloadStart.info"));



        panel = new JPanel();
        panel.setLayout(null);


        JLabel urlLable = new JLabel(Utility.getLocalString("downloadStart.url"));
        urlLable.setBounds(10, 10, 150, 20);

        JTextField address = new JTextField(url);
        address.setBounds(10, 30, 480, 30);
        address.setEditable(false);


        JLabel fileSizeLable = new JLabel(Utility.getLocalString("downloadStart.fileSize") + ": " + fileSize);
        fileSizeLable.setBounds(500, 35, 150, 20);

        JLabel fileLable = new JLabel(Utility.getLocalString("downloadStart.savedAs"));
        fileLable.setBounds(10, 70, 150, 20);

        JTextField fileAddress = new JTextField(targetFile.getAbsolutePath());
        fileAddress.setBounds(10, 90, 610, 30);
        fileAddress.setEditable(false);


        JButton startDownload = new JButton(Utility.getLocalString("downloadStart.start"));
        startDownload.setBounds(400, 130, 150, 30);
        StartDownloadButtonListener startDownloadButtonListener = new StartDownloadButtonListener(url,targetFile,downloadService,mainFrame,this);
        startDownload.addActionListener(startDownloadButtonListener);



        JButton browesLocation = new JButton("...");
        browesLocation.setBounds(620, 90, 30, 30);
        browesLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileDialog = new JFileChooser();
                fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int returnVal = fileDialog.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File currentDirectory = fileDialog.getSelectedFile();
                    String urlFileName = FilenameUtils.getName(url);
                    File target = new File(currentDirectory.getAbsolutePath() + File.separator + urlFileName);
                    targetFile = target;
                    fileAddress.setText(target.getAbsolutePath());
                    startDownloadButtonListener.setTarget(target);
                }

            }
        });


        JButton closeButton = new JButton(Utility.getLocalString("downloadStart.cancel"));
        closeButton.setBounds(550, 130, 100, 30);
        closeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
            }
        });


        panel.add(urlLable);
        panel.add(address);
        panel.add(fileSizeLable);
        panel.add(fileLable);
        panel.add(fileAddress);
        panel.add(browesLocation);
        panel.add(startDownload);
        panel.add(closeButton);


        setContentPane(panel);


        FrameOptions frameOptions = new FrameOptions();
        frameOptions.setDefaultCloseOperation(false);
        frameOptions.setDimension(new Dimension(700, 210));
        frameOptions.setIsCentered(true);
        frameOptions.setIsResizable(false);
        frameOptions.setIsVisible(true);
        FrameConfigurator.configure(this, frameOptions);

    }


}
