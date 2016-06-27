package com.odm.gui;

import com.odm.gui.entities.FrameOptions;
import com.odm.utility.Utility;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by mohamed on 6/23/16.
 */
@Component
public class DownloadCompleteFrame extends JFrame{

    private String url;
    private File targetFile;
    private JFrame thisFrame = this;
    private String downloadedSize;
    private JPanel panel;

    public void open(String url,File targetFile,String downloadedSize){
        this.url = url;
        this.targetFile = targetFile;
        this.downloadedSize = downloadedSize;
        prepareGui();
    }
    private void prepareGui() {
        setTitle(Utility.getLocalString("downloadComplete.complete"));


        BufferedImage img = null;
        try {
            img = ImageIO.read(new ClassPathResource("ic_folder_open_black_24dp_2x.png").getURL());
        } catch (IOException e) {
            e.printStackTrace();
        }

        panel = new JPanel();
        panel.setLayout(null);

        ImageIcon icon = new ImageIcon(img);
        JLabel label = new JLabel(icon);
        label.setBounds(10, 10, 50, 50);

        JLabel completLable = new JLabel(Utility.getLocalString("downloadComplete.complete"));
        completLable.setBounds(61, 15, 150, 20);

        JLabel downloaded = new JLabel(Utility.getLocalString("downloadComplete.downloaded") + " " + downloadedSize);
        downloaded.setBounds(61, 30, 150, 20);

        JLabel urlLable = new JLabel(Utility.getLocalString("downloadComplete.url"));
        urlLable.setBounds(10, 60, 150, 20);

        JTextField address = new JTextField(url);
        address.setBounds(10, 80, 480, 30);
        address.setEditable(false);


        JLabel fileLable = new JLabel(Utility.getLocalString("downloadComplete.file"));
        fileLable.setBounds(10, 120, 150, 20);

        JTextField fileAddress = new JTextField(targetFile.getAbsolutePath());
        fileAddress.setBounds(10, 140, 480, 30);
        fileAddress.setEditable(false);


        JButton openFolder = new JButton(Utility.getLocalString("downloadComplete.openFolder"));
        openFolder.setBounds(290, 180, 100, 30);
        openFolder.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(targetFile.getParentFile().getAbsoluteFile());
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        });

        JButton closeButton = new JButton(Utility.getLocalString("downloadComplete.close"));
        closeButton.setBounds(390,180,100,30);
        closeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
            }
        });

        panel.add(label);
        panel.add(completLable);
        panel.add(downloaded);
        panel.add(urlLable);
        panel.add(address);
        panel.add(fileLable);
        panel.add(fileAddress);
        panel.add(openFolder);
        panel.add(closeButton);


        setContentPane(panel);


        FrameOptions frameOptions = new FrameOptions();
        frameOptions.setDefaultCloseOperation(false);
        frameOptions.setDimension(new Dimension(500, 250));
        frameOptions.setIsCentered(true);
        frameOptions.setIsResizable(false);
        frameOptions.setIsVisible(true);
        FrameConfigurator.configure(this, frameOptions);

    }

}
