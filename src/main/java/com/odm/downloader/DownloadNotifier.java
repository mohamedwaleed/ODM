package com.odm.downloader;

import com.github.axet.wget.SpeedInfo;
import com.github.axet.wget.info.DownloadInfo;
import com.odm.gui.ProgressFrame;

/**
 * Created by mohamed on 6/11/16.
 */
public class DownloadNotifier implements Runnable {


    private DownloadInfo info;
    private SpeedInfo speedInfo;
    private long last;
    private ProgressFrame progressFrame;
    private long previouseBytes = 0;

    public String formatSpeed(long s) {
        if (s > 0.1 * 1024 * 1024 * 1024) {
            float f = s / 1024f / 1024f / 1024f;
            return String.format("%.1f GB/s", f);
        } else if (s > 0.1 * 1024 * 1024) {
            float f = s / 1024f / 1024f;
            return String.format("%.1f MB/s", f);
        } else {
            float f = s / 1024f;
            return String.format("%.1f kb/s", f);
        }
    }
    public String formatFileSize(long s) {
        if (s > 0.1 * 1024 * 1024 * 1024) {
            float f = s / 1024f / 1024f / 1024f;
            return String.format("%.1f GB", f);
        } else if (s > 0.1 * 1024 * 1024) {
            float f = s / 1024f / 1024f;
            return String.format("%.1f MB", f);
        } else {
            float f = s / 1024f;
            return String.format("%.1f kb", f);
        }
    }
    public String formatDownloaded(long s,float percentage) {
        String ret = "";
        if (s > 0.1 * 1024 * 1024 * 1024) {
            float f = s / 1024f / 1024f / 1024f;
            ret =  String.format("%.1f GB", f);
        } else if (s > 0.1 * 1024 * 1024) {
            float f = s / 1024f / 1024f;
            ret = String.format("%.1f MB", f);
        } else {
            float f = s / 1024f;
            ret =  String.format("%.1f kb", f);
        }
        String perc = String.format("( %.2f%% )",percentage);
        ret += perc;
        return ret;
    }
    public  double calcTimeLeft(long numOfBytesDownloadedBytes,
                                      long fileLength, long previouseBytes) {
        double transfareRateOfKiloBytes = (double) (numOfBytesDownloadedBytes - previouseBytes) / 1024.0;
        double remainedSizeOfKiloBytes = (double) ((double) (fileLength - numOfBytesDownloadedBytes) / (double) 1024.0);
        double timeLeft = (remainedSizeOfKiloBytes / transfareRateOfKiloBytes) / 60;
        return timeLeft;
    }

    public String formatTimeLeft(double timeLeft){

        int minuts = (int)timeLeft / 60;
        int hours = minuts / 60;
        int days = hours / 24;

        String formatedTimeLeft = "";

        if(days >= 1){
            formatedTimeLeft += Integer.toString(days) + " days ";
        }
        if(hours >= 1){
            formatedTimeLeft += Integer.toString(hours) + " hours ";
        }
        if(minuts >= 1){
            formatedTimeLeft += Integer.toString(minuts) + " minutes ";
        }
        formatedTimeLeft += Integer.toString((int)timeLeft % 60) + " sec ";
        return formatedTimeLeft;
    }
    @Override
    public void run() {
        // notify app or save download state
        // you can extract information from DownloadInfo info;
        progressFrame.setStatusTableRowData(info.getState().name(), 0,1);
        switch (info.getState()) {
            case EXTRACTING:
            case EXTRACTING_DONE:
                break;
            case DONE:
                // finish speed calculation by adding remaining bytes speed
                speedInfo.end(info.getCount());
                // print speed
                System.out.println(String.format("%s average speed (%s)", info.getState(), formatSpeed(speedInfo.getAverageSpeed())));
                break;
            case RETRYING:
                System.out.println(info.getState() + " " + info.getDelay());
                break;
            case DOWNLOADING:
                progressFrame.setStatusTableRowData(formatFileSize(info.getLength()), 1,1);

                speedInfo.step(info.getCount());
                long now = System.currentTimeMillis();
                if (now - 1000 > last) {
                    last = now;

                    String parts = "";

                    for (DownloadInfo.Part p : info.getParts()) {
                        if (p.getState().equals(DownloadInfo.Part.States.DOWNLOADING)) {
                            parts += String.format("Part#%d(%.2f) ", p.getNumber(),
                                    p.getCount() / (float) p.getLength());
                        }
                    }

                    float downloadedPercentage = info.getCount() / (float) info.getLength();
                    progressFrame.setFrameTitle(String.format("%.2f%% %s", downloadedPercentage * 100, progressFrame.getSavedFile().getName()));
                    progressFrame.setStatusTableRowData(formatDownloaded(info.getCount(), downloadedPercentage * 100), 2, 1);
                    progressFrame.setStatusTableRowData(formatSpeed(info.getCount() - previouseBytes), 3, 1);
                    progressFrame.setStatusTableRowData(formatTimeLeft(calcTimeLeft(info.getCount(),info.getLength(),previouseBytes)), 4, 1);

                    System.out.println(String.format("%.2f %s (%s / %s)", downloadedPercentage, parts,
                            formatSpeed(speedInfo.getCurrentSpeed()),
                            formatSpeed(speedInfo.getAverageSpeed())));
                }
                previouseBytes = info.getCount();
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
