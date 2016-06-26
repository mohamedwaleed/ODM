package com.odm.gui;

import com.odm.Constants;
import com.odm.gui.entities.FrameOptions;
import com.odm.gui.listeners.DownloadButtonListener;
import com.odm.persistence.entities.Download;
import com.odm.persistence.repositories.DownloadRepository;
import com.odm.persistence.services.DownloadService;
import com.odm.utility.OdmLocal;
import com.odm.utility.PropertyFileUtility;
import com.odm.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


/**
 * Created by mohamed on 6/9/16.
 */
@Component
public class MainFrame extends JFrame{

    @Value("${app.language}")
    private String language;
    @Autowired
    private PropertyFileUtility propertyFileUtility ;

    @Autowired
    private DownloadButtonListener downloadButtonListener;

    @Autowired
    private DownloadService downloadService;

    private JPanel panel = new JPanel();

    private JFrame thisFrame = this;

    private String[] columnNames ;

    private JTable table;

    private DefaultTableModel tableModel;

    @PostConstruct
    public void init(){
        setTitle(Constants.APPLIACTION_TITLE);
        try {
            Image image = ImageIO.read(new File("images/ic_trending_down_black_24dp_2x.png"));
            setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Locale locale = null;
        switch (language){
            case "Arabic":
                locale = new Locale("ar", "EG");
                OdmLocal.resourceBundle = ResourceBundle.getBundle("strings_ar_eg", locale);
                break;
            default:
                locale = new Locale("en", "US");
                OdmLocal.resourceBundle = ResourceBundle.getBundle("strings", locale);
        }
        prepareGui();

    }

    public void prepareGui() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        columnNames = new String[]{
                Utility.getLocalString("main.downloadList.first"),
                Utility.getLocalString("main.downloadList.second"),
                Utility.getLocalString("main.downloadList.third"),
                Utility.getLocalString("main.downloadList.forth"),
                Utility.getLocalString("main.downloadList.fifth"),
                Utility.getLocalString("main.downloadList.six"),
                Utility.getLocalString("main.downloadList.seventh")};

        FrameOptions frameOptions = new FrameOptions();
        frameOptions.setDefaultCloseOperation(true);
        frameOptions.setDimension(new Dimension(900, 500));
        frameOptions.setIsCentered(true);
        frameOptions.setIsResizable(true);
        frameOptions.setIsVisible(true);
        FrameConfigurator.configure(this, frameOptions);
        JMenuBar jMenuBar = buildMenuBar();

        JPanel jPanel1 = constructTopPanel();
        JPanel jPanel2 = constructBottomPanel();


        //Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                jPanel1, jPanel2);
        splitPane.setDividerLocation(85);


        splitPane.setEnabled(false);


        panel.setSize(this.getWidth(), this.getHeight());
        panel.setLayout(new BorderLayout());
        splitPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        panel.add(splitPane);
        setContentPane(panel);
        setJMenuBar(jMenuBar);

        getDownloadList();

    }

    private JPanel constructTopPanel() {

        JButton downloadButton = new JButton(Utility.getLocalString("main.button.download"));
        ImageIcon imageIcon = null;
            imageIcon = new ImageIcon("images/ic_get_app_black_24dp_2x.png");

        downloadButton.setIcon(imageIcon);
        downloadButton.addActionListener(downloadButtonListener);
        downloadButton.setBounds(3, 3, 100, 80);
        downloadButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        downloadButton.setHorizontalTextPosition(SwingConstants.CENTER);
        Font font = downloadButton.getFont();
        downloadButton.setFont(font.deriveFont(Font.BOLD));


        JButton donationButton = new JButton(Utility.getLocalString("main.button.donation"));
        ImageIcon donationButtonImageIcon = null;
            donationButtonImageIcon = new ImageIcon("images/ic_attach_money_black_24dp_2x.png");

        donationButton.setIcon(donationButtonImageIcon);
        donationButton.setBounds(108, 3, 100, 80);
        Font donationButtonFont = downloadButton.getFont();
        donationButton.setFont(donationButtonFont.deriveFont(Font.BOLD));
        donationButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        donationButton.setHorizontalTextPosition(SwingConstants.CENTER);

        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(null);
        jPanel1.add(downloadButton);
        jPanel1.add(donationButton);
        Dimension minimumSize = new Dimension(panel.getWidth(), 10);
        jPanel1.setSize(minimumSize);
        return jPanel1;
    }
    private JPanel constructBottomPanel() {

        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        Dimension minimumSize1 = new Dimension(panel.getWidth(), panel.getHeight() - 50);
        jPanel2.setSize(minimumSize1);

        constructDownloadList(jPanel2);
        return jPanel2;
    }
    private void constructDownloadList(JPanel panel) {

        table = new JTable();
        table.setBounds(0, 0, panel.getWidth(), panel.getHeight());
        table.setRowHeight(25);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(3, 3, 860, 300);
        // Add the scroll pane to this panel.
        panel.add(scrollPane);

        tableModel = (DefaultTableModel) table.getModel();

        tableModel.setColumnIdentifiers(columnNames);

        centerTableCells();

        tableModel.fireTableDataChanged();
    }
    public void getDownloadList(){

        java.util.List<Download> savedDownloadList = downloadService.findDownloads();
        for(Download download:savedDownloadList){
            Vector<String> downloadRow = new Vector<>();
            downloadRow.add(download.getFileName());
            downloadRow.add(download.getSize());
            downloadRow.add(download.getStatus());
            downloadRow.add(download.getTimeLeft());
            downloadRow.add(download.getTransferRate());
            downloadRow.add(download.getProgress());
            downloadRow.add(download.getDownloaded());
            tableModel.addRow(downloadRow);
        }
        tableModel.fireTableDataChanged();
    }

    public synchronized int addDownloadListRecord(Vector<String>record) {
        int insertRow = tableModel.getRowCount();
        tableModel.addRow(record);
        tableModel.fireTableDataChanged();
        return insertRow;
    }
    private void centerTableCells() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columnNames.length; i++)
            table.getColumnModel().getColumn(i)
                    .setCellRenderer(centerRenderer);
    }
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(Utility.getLocalString("menuBar.language"));
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        JMenuItem languageEnglishItem = new JMenuItem("English");
        languageEnglishItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Properties properties = new Properties();
                properties.setProperty("app.language","English");
                try {
                    propertyFileUtility.saveToDefaultPropertyFile(properties,"Settings");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Locale locale = new Locale("en", "US");
                OdmLocal.resourceBundle = ResourceBundle.getBundle("strings", locale);
                JOptionPane.showMessageDialog(null, Utility.getLocalString("language.confirm"));
//                thisFrame.dispose();
                // new MainFrame(OdmLocal.resourceBundle);
            }
        });
        JMenuItem languageArabicItem = new JMenuItem("Arabic");
        languageArabicItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Properties properties = new Properties();
                properties.setProperty("app.language","Arabic");
                try {
                    propertyFileUtility.saveToDefaultPropertyFile(properties,"Settings");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Locale locale = new Locale("ar", "EG");
                OdmLocal.resourceBundle = ResourceBundle.getBundle("strings_ar_eg", locale);
                JOptionPane.showMessageDialog(null, Utility.getLocalString("language.confirm"));
//                thisFrame.dispose();
                // new MainFrame(OdmLocal.resourceBundle);
            }
        });
        menu.add(languageEnglishItem);
        menu.add(languageArabicItem);
        menuBar.add(menu);
        return menuBar;
    }
    public void updatePersistence(Download download){
        downloadService.saveDownload(download);
    }
    public void setStatusTableRowData(String data, int rowNumber, int colomnNumber) {
        tableModel.setValueAt(data, rowNumber, colomnNumber);
    }
}
