package com.odm.persistence.services;

import com.odm.persistence.entities.Download;
import com.odm.persistence.repositories.DownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mohamed on 6/25/16.
 */
@Service
public class DownloadService {
    @Autowired
    private DownloadRepository downloadRepository;

    public Download saveDownload(Download download){
        return downloadRepository.save(download);
    }
    public List<Download> findDownloads(){
        List<Download> downloadList = downloadRepository.findAll();
        return downloadList;
    }
}
