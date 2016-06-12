package com.odm.gui;

import com.odm.Constants;
import com.odm.gui.entities.FrameOptions;
import com.odm.gui.listeners.DownloadButtonListener;
import com.odm.utility.OdmLocal;
import com.odm.utility.Utility;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Created by mohamed on 6/9/16.
 */
@Component
public class MainFrame extends JFrame{
    private JPanel panel = new JPanel();
    private JFrame thisFrame = this;
    private String[] columnNames ;
    private JTable table;

    public MainFrame(){
        super(Constants.APPLIACTION_TITLE);
        Locale locale = new Locale("en", "US");
        OdmLocal.resourceBundle = ResourceBundle.getBundle("strings", locale);
        prepareGui();
    }

    public MainFrame(ResourceBundle resourceBundle){
        super(Constants.APPLIACTION_TITLE);
        OdmLocal.resourceBundle = resourceBundle;
        prepareGui();
    }

    public void prepareGui() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
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
        this.setJMenuBar(jMenuBar);


        JPanel jPanel1 = constructTopPanel();
        JPanel jPanel2 = constructBottomPanel();


        //Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                jPanel1, jPanel2);
        splitPane.setDividerLocation(55);

//Provide minimum sizes for the two components in the split pane

        splitPane.setEnabled(false);


        panel.setSize(this.getWidth(),this.getHeight());
        panel.setLayout(new BorderLayout());
        splitPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        panel.add(splitPane);
        setContentPane(panel);
    }

    private JPanel constructTopPanel() {

        JButton downloadButton = new JButton(Utility.getLocalString("main.button.download"));
        downloadButton.addActionListener(new DownloadButtonListener());
        downloadButton.setBounds(3, 3, 150, 50);
        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(null);
        jPanel1.add(downloadButton);
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

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        tableModel.setColumnIdentifiers(columnNames);

        centerTableCells();

        tableModel.fireTableDataChanged();
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
                Locale locale = new Locale("en", "US");
                OdmLocal.resourceBundle = ResourceBundle.getBundle("strings", locale);
                thisFrame.dispose();
                new MainFrame(OdmLocal.resourceBundle);
            }
        });
        JMenuItem languageArabicItem = new JMenuItem("Arabic");
        languageArabicItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Locale locale = new Locale("ar", "EG");
                OdmLocal.resourceBundle = ResourceBundle.getBundle("strings_ar_eg", locale);
                thisFrame.dispose();
                new MainFrame(OdmLocal.resourceBundle);
            }
        });
        menu.add(languageEnglishItem);
        menu.add(languageArabicItem);
        menuBar.add(menu);
        return menuBar;
    }

}
