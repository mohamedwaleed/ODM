package com.odm.gui;


import com.github.axet.wget.info.DownloadInfo;
import com.odm.downloader.DownloadFileProcess;
import com.odm.downloader.DownloadNotifier;
import com.odm.gui.listeners.CancelButtonListener;
import com.odm.gui.listeners.StopButtonListener;
import com.odm.utility.Utility;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;

@Component
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
	private JTextField address;
    private String url;
	private String[] downloadInformation ;
	private String[] columnNames ;
    private File savedFile;
	private DownloadFileProcess downloadFileProcess;


	public void open(DownloadFileProcess downloadFileProcess){
        this.downloadFileProcess = downloadFileProcess;
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

		setTitle("0%");

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
        cancel.addActionListener(new CancelButtonListener(downloadFileProcess, this));
        pause.addActionListener(new StopButtonListener(downloadFileProcess,this));
		window.add(pause);
		window.add(cancel);
	}

	private void configureUrlLable() {
        address = new JTextField(url);
        address.setBounds(20, 5, 560, 30);
        address.setEditable(false);

		window.add(address);
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

		WindowAdapter windowAdapter = new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
                downloadFileProcess.getStop().set(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                System.out.println("closed");
            }
		};

		addWindowListener(windowAdapter);
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
        address.setText(url);
	}
	public String getUrl() {
		return this.url ;
	}

	public void setStatusTableRowData(String data, int rowNumber,
			int colomnNumber) {
		tableModel.setValueAt(data, rowNumber, colomnNumber);
	}

	public void setPartsTableRowData(java.util.List<DownloadInfo.Part> parts) {

		for (int i = 0; i < parts.size(); i++) {
            DownloadInfo.Part.States state = parts.get(i).getState();
			if (state.equals(DownloadInfo.Part.States.DOWNLOADING)) {
				if (i > partsTableModel.getRowCount() - 1) {
					Vector<String> rowData = new Vector<>();
					rowData.add(Integer.toString(i + 1));
					rowData.add(DownloadNotifier.formatDownloaded(parts.get(i).getCount(), parts.get(i).getCount() / (float) parts.get(i).getLength()));
                    if(state == DownloadInfo.Part.States.DOWNLOADING){
                        rowData.add(Utility.getLocalString("progress.downloading"));
                    }else if(state == DownloadInfo.Part.States.DONE){
                        rowData.add(Utility.getLocalString("progress.done"));
                    }else if(state == DownloadInfo.Part.States.RETRYING){
                        rowData.add(Utility.getLocalString("progress.retry"));
                    }else if(state == DownloadInfo.Part.States.QUEUED){
                        rowData.add(Utility.getLocalString("progress.queued"));
                    }else if(state == DownloadInfo.Part.States.ERROR){
                        rowData.add(Utility.getLocalString("progress.error"));
                    }else if(state == DownloadInfo.Part.States.STOP){
                        rowData.add(Utility.getLocalString("progress.stop"));
                    }
					partsTableModel.addRow(rowData);
				} else {
					partsTableModel.setValueAt(Integer.toString(i + 1), i, 0);
					partsTableModel.setValueAt(DownloadNotifier.formatDownloaded(parts.get(i).getCount(), parts.get(i).getCount() / (float) parts.get(i).getLength()), i, 1);
					if(state == DownloadInfo.Part.States.DOWNLOADING){
                        partsTableModel.setValueAt(Utility.getLocalString("progress.downloading"), i, 2);
                    }else if(state == DownloadInfo.Part.States.DONE){
                        partsTableModel.setValueAt(Utility.getLocalString("progress.done"), i, 2);
                    }else if(state == DownloadInfo.Part.States.RETRYING){
                        partsTableModel.setValueAt(Utility.getLocalString("progress.retry"), i, 2);
                    }else if(state == DownloadInfo.Part.States.QUEUED){
                        partsTableModel.setValueAt(Utility.getLocalString("progress.queued"), i, 2);
                    }else if(state == DownloadInfo.Part.States.ERROR){
                        partsTableModel.setValueAt(Utility.getLocalString("progress.error"), i, 2);
                    }else if(state == DownloadInfo.Part.States.STOP){
                        partsTableModel.setValueAt(Utility.getLocalString("progress.stop"), i, 2);
                    }

				}
			}
		}
		partsTableModel.fireTableDataChanged();

	}
	public void completeDownload(String downloadedSize){
		DownloadCompleteFrame downloadCompleteFrame = new DownloadCompleteFrame();
        downloadCompleteFrame.open(url,savedFile,downloadedSize);
        this.dispose();
	}
    public File getSavedFile() {
        return savedFile;
    }

    public void setSavedFile(File savedFile) {
        this.savedFile = savedFile;
    }

	public DownloadFileProcess getDownloadFileProcess() {
		return downloadFileProcess;
	}

	public void setDownloadFileProcess(DownloadFileProcess downloadFileProcess) {
		this.downloadFileProcess = downloadFileProcess;
	}
}
