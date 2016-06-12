package com.odm.gui;


import com.github.axet.wget.info.DownloadInfo;
import com.odm.utility.Utility;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class ProgressFrame extends JFrame {

	private static final long serialVersionUID = -3915239322761791027L;
	private JTable table;
	private DefaultTableModel tableModel;
	private JTable partsTable;
	private DefaultTableModel partsTableModel;
	private JPanel window;
	private JProgressBar progressBar = new JProgressBar();
	private JButton cancel ;
	private JButton pause ;
	private JLabel urlLable;
    private String url;
	private String[] downloadInformation ;
	private String[] columnNames ;
    private File savedFile;

	public ProgressFrame() {
		constructGui();
		configurFrame();
        configureUrlLable();
	}

	private void constructGui() {

		downloadInformation = new String[]{
                Utility.getLocalString("progress.info.status"),
                Utility.getLocalString("progress.info.fileSize"),
				Utility.getLocalString("progress.info.downloaded"),
				Utility.getLocalString("progress.info.tranferRate"),
				Utility.getLocalString("progress.info.timeLeft"),
				Utility.getLocalString("progress.info.resume")
		};
		cancel = new JButton(Utility.getLocalString("progress.button.cancel"));
		pause = new JButton(Utility.getLocalString("progress.button.pause"));

		columnNames = new String[]{"N.", Utility.getLocalString("progress.column.downloaded"), Utility.getLocalString("progress.column.info")};

		setTitle("100%");

		window = new JPanel();
		window.setLayout(null);

		configureDownloadStatusTable();

		configureProgressBar();

		configureButtons();

		configureDownloadPartsTable();

		setContentPane(window);
	}

	private void configureDownloadPartsTable() {
		partsTable = new JTable();
		partsTable.setRowHeight(25);
		partsTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		partsTable.setFillsViewportHeight(true);
		partsTable.setEnabled(false);
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(partsTable);
		scrollPane.setBounds(20, 290, 560, 100);
		// Add the scroll pane to this panel.
		window.add(scrollPane);

		partsTableModel = (DefaultTableModel) partsTable.getModel();

		partsTableModel.fireTableDataChanged();

		partsTableModel.setColumnIdentifiers(columnNames);
	}

	private void configureButtons() {
		cancel.setBounds(370, 250, 100, 25);
		pause.setBounds(480, 250, 100, 25);
		window.add(pause);
		window.add(cancel);
	}

	private void configureUrlLable() {
		urlLable = new JLabel(url);
		urlLable.setBounds(20, 20, 560, 15);
		window.add(urlLable);
	}

	private void configureProgressBar() {
		progressBar.setIndeterminate(true);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setIndeterminate(false);
		progressBar.setBounds(20, 213, 560, 20);
		window.add(progressBar);
	}

	private void configureDownloadStatusTable() {
		table = new JTable();
		table.setRowHeight(25);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		table.setTableHeader(null);
		table.setEnabled(false);
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 40, 560, 153);
		// Add the scroll pane to this panel.
		window.add(scrollPane);

		tableModel = (DefaultTableModel) table.getModel();

		tableModel.fireTableDataChanged();

		tableModel.addColumn("", downloadInformation);
		String[] emptyArr = new String[downloadInformation.length];
		tableModel.addColumn("", emptyArr);
	}

	private void configurFrame() {
		setResizable(false);
		getContentPane().setBackground(new Color(240, 240, 240));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 450);
		setVisible(true);
		setWindowPosition();

	}

	private void setWindowPosition() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int width = getWidth();
		int height = getHeight();
		int x = (int) ((dimension.getWidth() - width) / 2);
		int y = (int) ((dimension.getHeight() - height) / 2);
		setLocation(x, y);
	}

	public void setFrameTitle(String lable) {
		setTitle(lable);
	}

	public void setProgressBarValue(int value) {
		progressBar.setIndeterminate(true);
		progressBar.setValue(value);
		progressBar.setIndeterminate(false);
	}

	public void setUrl(String url) {
		this.url = url;
		urlLable.setText(url);
	}

	public void setStatusTableRowData(String data, int rowNumber,
			int colomnNumber) {
		tableModel.setValueAt(data, rowNumber, colomnNumber);
	}

	public void setPartsTableRowData(java.util.List<DownloadInfo.Part> parts) {

		for (int i = 0; i < parts.size(); i++) {
			if (parts.get(i).getState().equals(DownloadInfo.Part.States.DOWNLOADING)) {
				if (i > partsTableModel.getRowCount() - 1) {
//					Vector<String> rowData = new Vector<String>();
//					rowData.add(Integer.toString(i + 1));
//					rowData.add(String.format("%.2f", DownloadInfoCalculator
//							.calcDownloadedSize(parts.get(i).getCount())) + " MB");
//					rowData.add(parts.get(i).getState().toString());
//					partsTableModel.addRow(rowData);
				} else {
//					partsTableModel.setValueAt(Integer.toString(i + 1), i, 0);
//					partsTableModel.setValueAt(String.format("%.2f",
//							DownloadInfoCalculator.calcDownloadedSize(parts
//									.get(i).getCount())) + " MB", i, 1);
//					partsTableModel.setValueAt(parts.get(i).getState()
//							.toString(), i, 2);
				}
			}
		}
		partsTableModel.fireTableDataChanged();

	}

    public File getSavedFile() {
        return savedFile;
    }

    public void setSavedFile(File savedFile) {
        this.savedFile = savedFile;
    }
}
