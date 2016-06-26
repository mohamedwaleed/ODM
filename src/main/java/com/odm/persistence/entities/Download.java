package com.odm.persistence.entities;

import javax.persistence.*;

/**
 * Created by mohamed on 6/25/16.
 */
@Entity
@Table
public class Download {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column
    private String fileName;

    @Column
    private String size;

    @Column
    private String status;

    @Column
    private String timeLeft;

    @Column
    private String transferRate;

    @Column
    private String progress;

    @Column
    private String downloaded;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransferRate() {
        return transferRate;
    }

    public void setTransferRate(String transferRate) {
        this.transferRate = transferRate;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }

}
