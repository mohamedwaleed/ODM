package com.odm.downloader;

import com.github.axet.wget.SpeedInfo;
import com.github.axet.wget.info.DownloadInfo;
import com.odm.gui.ProgressFrame;
import com.odm.utility.Utility;

/**
 * Created by mohamed on 6/11/16.
 */
public class DownloadNotifier implements Runnable {


    private DownloadInfo info;
    private SpeedInfo speedInfo;
    private long last;
    private ProgressFrame progressFrame;
    private long previouseBytes = 0;

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
            case EXTRACTING_DONE:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.extractDone"), 0,1);
                break;
            case DONE:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.done"), 0,1);
                // finish speed calculation by adding remaining bytes speed
                speedInfo.end(info.getCount());
                // print speed
                System.out.println(String.format("%s average speed (%s)", info.getState(), formatSpeed(speedInfo.getAverageSpeed())));
                break;
            case RETRYING:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.retry"), 0,1);
                System.out.println(info.getState() + " " + info.getDelay());
                break;
            case DOWNLOADING:
                progressFrame.setStatusTableRowData(Utility.getLocalString("progress.downloading"), 0,1);
                progressFrame.setStatusTableRowData(formatFileSize(info.getLength()), 1,1);

                speedInfo.step(info.getCount());
                long now = System.currentTimeMillis();
                if (now - 500 > last) {
                    last = now;

                    progressFrame.setPartsTableRowData(info.getParts());

                    float downloadedPercentage = info.getCount() / (float) info.getLength();

                    progressFrame.setProgressBarValue((int)(downloadedPercentage * 100));
                    progressFrame.setFrameTitle(String.format("%.2f%% %s", downloadedPercentage * 100, progressFrame.getSavedFile().getName()));
                    progressFrame.setStatusTableRowData(formatDownloaded(info.getCount(), downloadedPercentage * 100), 2, 1);
                    progressFrame.setStatusTableRowData(formatSpeed(info.getCount() - previouseBytes), 3, 1);
                    progressFrame.setStatusTableRowData(formatTimeLeft(calcTimeLeft(info.getCount(), info.getLength(), previouseBytes)), 4, 1);

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

    public void setUiFrame(ProgressFrame progressFrame) {
        this.progressFrame = progressFrame;
    }
}
