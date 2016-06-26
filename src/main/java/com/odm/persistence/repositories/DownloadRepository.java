package com.odm.persistence.repositories;

import com.odm.persistence.entities.Download;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mohamed on 6/25/16.
 */
public interface DownloadRepository extends JpaRepository<Download,Integer> {
}
