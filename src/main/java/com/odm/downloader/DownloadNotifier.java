package com.odm.downloader;

import com.github.axet.wget.SpeedInfo;
import com.github.axet.wget.info.DownloadInfo;
import com.odm.gui.MainFrame;
import com.odm.gui.ProgressFrame;
import com.odm.persistence.entities.Download;
import com.odm.utility.Utility;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mohamed on 6/11/16.
 */
public class DownloadNotifier implements Runnable {


    private DownloadInfo info;

    private AtomicBoolean stop;

    private SpeedInfo speedInfo;

    private long last;

    private ProgressFrame progressFrame;

    private Integer mainFrameRow;

    private Download download;

    private MainFrame mainFrame;

    private long previouseBytes = 0;

    float downloadedPercentage ;

    private boolean isDownloading = false;
    public static String formatSpeed(long s) {
        if (s / 1024f / 1024f / 1024f >= 1) {
            float f = s / 1024f / 1024f / 1024f;
            return String.format("%.1f ", f) + Utility.getLocalString("progress.gbPerSec");
        } else if (s / 1024f / 1024f >= 1) {
            float f = s / 1024f / 1024f;
            return String.format("%.1f ", f)+ Utility.getLocalString("progress.mbPerSec");
        } else {
            float f = s / 1024f;
            return String.format("%.1f ", f)+ Utility.getLocalString("progress.kbPerSec");
        }
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
    public static String formatDownloaded(long s,float percentage) {
        String ret = "";
        if (s / 1024f / 1024f / 1024f >= 1) {
            float f = s / 1024f / 1024f / 1024f;
            ret =  String.format("%.1f ", f) + Utility.getLocalString("progress.gb");
        } else if (s / 1024f / 1024f >= 1) {
            float f = s / 1024f / 1024f;
            ret = String.format("%.1f ", f) + Utility.getLocalString("progress.mb");
        } else {
            float f = s / 1024f;
            ret =  String.format("%.1f ", f) + Utility.getLocalString("progress.kb");
        }
        String perc = String.format("( %.2f%% )",percentage);
        ret += perc;
        return ret;
    }
    public  static double calcTimeLeft(long numOfBytesDownloadedBytes,
                                      long fileLength, long previouseBytes) {
        double transfareRateOfKiloBytes = (double) (numOfBytesDownloadedBytes - previouseBytes)  ;
        double remainedSizeOfKiloBytes = (double) ((double) (fileLength - numOfBytesDownloadedBytes));
        double timeLeft = (remainedSizeOfKiloBytes / transfareRateOfKiloBytes) ;
        return timeLeft;
    }

    public static String formatTimeLeft(double timeLeft){


        int hours = (int)timeLeft / 60 / 60 ;
        int minuts = (int)timeLeft / 60 % 60;
        int seconds = (int)timeLeft % 60;

        String formatedTimeLeft = "";

        if(hours >= 1){
            formatedTimeLeft += Integer.toString(hours) + " " + Utility.getLocalString("progress.hours") + " ";
        }
        if(minuts >= 1){
            formatedTimeLeft += Integer.toString(minuts) + " " + Utility.getLocalString("progress.minutes") + " ";
        }
        if(seconds >= 1){
            formatedTimeLeft += Integer.toString(seconds) + " " + Utility.getLocalString("progress.seconds") + " ";
        }
        return formatedTimeLeft;
    }
    @Override
    public void run() {
        // notify app or save download state
        // you can extract information from DownloadInfo info;
        switch (info.getState()) {
            case EXTRACTING:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.extract"), 0,1);
                mainFrame.setStatusTableRowData(Utility.getLocalString("progress.extract"), mainFrameRow,2);
                download.setStatus(Utility.getLocalString("progress.extract"));
            case EXTRACTING_DONE:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.extractDone"), 0,1);
                mainFrame.setStatusTableRowData(Utility.getLocalString("progress.extractDone"), mainFrameRow,2);
                download.setStatus(Utility.getLocalString("progress.extractDone"));
                break;
            case DONE:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.done"), 0,1);
                mainFrame.setStatusTableRowData(Utility.getLocalString("progress.done"), mainFrameRow,2);
                download.setStatus(Utility.getLocalString("progress.done"));

                // finish speed calculation by adding remaining bytes speed
                speedInfo.end(info.getCount());
                progressFrame.setProgressBarValue(100);
                progressFrame.setFrameTitle(String.format("%.2f%% %s", 100.0, progressFrame.getSavedFile().getName()));
                progressFrame.completeDownload(formatFileSize(info.getCount()));

                mainFrame.updatePersistence(download);
                break;
            case RETRYING:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.retry"), 0,1);
                mainFrame.setStatusTableRowData(Utility.getLocalString("progress.retry"), mainFrameRow,2);
                download.setStatus(Utility.getLocalString("progress.retry"));
                mainFrame.updatePersistence(download);
                System.out.println(info.getState() + " " + info.getDelay());
                break;
            case DOWNLOADING:
                isDownloading = true;

                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.downloading"), 0,1);
                mainFrame.setStatusTableRowData(Utility.getLocalString("progress.downloading"), mainFrameRow,2);
                download.setStatus(Utility.getLocalString("progress.downloading"));

                if(info.getLength() == null){
                    progressFrame.setStatusTableRowData(Utility.getLocalString("progress.info.fileSize.unknown"), 1, 1);
                }
                else {
                    progressFrame.setStatusTableRowData(formatFileSize(info.getLength()), 1, 1);
                }

                mainFrame.setStatusTableRowData(formatFileSize(info.getLength()), mainFrameRow,1);
                download.setSize(formatFileSize(info.getLength()));
                speedInfo.step(info.getCount());
                long now = System.currentTimeMillis();
                if (now - 500 > last) {
                    last = now;

                    if (info.multipart()){
                        progressFrame.setPartsTableRowData(info.getParts());
                    }else{
                        // create single part download
                        DownloadInfo.Part part = new DownloadInfo.Part();
                        part.setStart(0);
                        part.setEnd(info.getLength());
                        part.setCount(0);
                        part.setState(DownloadInfo.Part.States.DOWNLOADING);
                        part.setCount(info.getCount());
                        ArrayList<DownloadInfo.Part> parts = new ArrayList<>();
                        parts.add(part);
                        progressFrame.setPartsTableRowData(parts);
                    }

                    float downloadedPercentage = info.getCount() / (float) info.getLength();

                    progressFrame.setProgressBarValue((int)(downloadedPercentage * 100));
                    progressFrame.setFrameTitle(String.format("%.2f%% %s", downloadedPercentage * 100, progressFrame.getSavedFile().getName()));
                    progressFrame.setStatusTableRowData(formatDownloaded(info.getCount(), downloadedPercentage * 100), 2, 1);
                    progressFrame.setStatusTableRowData(formatSpeed(speedInfo.getCurrentSpeed()), 3, 1);
                    progressFrame.setStatusTableRowData(formatTimeLeft(calcTimeLeft(info.getCount(), info.getLength(), previouseBytes)), 4, 1);

                    mainFrame.setStatusTableRowData(formatDownloaded(info.getCount(), downloadedPercentage * 100), mainFrameRow, 6);
                    mainFrame.setStatusTableRowData(formatSpeed(speedInfo.getCurrentSpeed()), mainFrameRow,4);
                    mainFrame.setStatusTableRowData(formatTimeLeft(calcTimeLeft(info.getCount(), info.getLength(), previouseBytes)), mainFrameRow,3);
                    mainFrame.setStatusTableRowData(String.format("%.2f%%", downloadedPercentage * 100),mainFrameRow,5);

                    download.setDownloaded(formatDownloaded(info.getCount(), downloadedPercentage * 100));
                    download.setTransferRate(formatSpeed(speedInfo.getCurrentSpeed()));
                    download.setTimeLeft(formatTimeLeft(calcTimeLeft(info.getCount(), info.getLength(), previouseBytes)));
                    download.setProgress(String.format("%.2f%%", downloadedPercentage * 100));

                    String resumeCap = "";
                    boolean resume = info.resume(info);
                    if(resume){
                        resumeCap = Utility.getLocalString("progress.resumeYes");
                    }else {
                        resumeCap = Utility.getLocalString("progress.resumeNo");
                    }
                    progressFrame.setStatusTableRowData(resumeCap, 5, 1);


                    previouseBytes = info.getCount();

                }
                if(!isDownloading){
                    mainFrame.updatePersistence(download);
                }
                break;
            case STOP:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.stop"), 0,1);
                mainFrame.setStatusTableRowData(Utility.getLocalString("progress.stop"), mainFrameRow,2);
                download.setStatus(Utility.getLocalString("progress.stop"));
                download.setDownloaded(formatDownloaded(info.getCount(), downloadedPercentage * 100));
                download.setProgress(String.format("%.2f%%", downloadedPercentage * 100));

                mainFrame.updatePersistence(download);
                break;
            case ERROR:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.error"), 0,1);
                mainFrame.setStatusTableRowData(Utility.getLocalString("progress.error"), mainFrameRow,2);
                download.setStatus(Utility.getLocalString("progress.error"));
                mainFrame.updatePersistence(download);
                break;
            default:
                break;
        }

    }

    public void setInfo(DownloadInfo info) {
        this.info = info;
    }

    public void setSpeedInfo(SpeedInfo speedInfo) {
        this.speedInfo = speedInfo;
    }

    public void setProgressFrame(ProgressFrame progressFrame) {
        this.progressFrame = progressFrame;
    }

    public AtomicBoolean getStop() {
        return stop;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setMainFrameRow(Integer mainFrameRow) {
        this.mainFrameRow = mainFrameRow;
    }

    public void setDownload(Download download) {
        this.download = download;
    }
    public void setStop(AtomicBoolean stop) {
        this.stop = stop;
    }
}
