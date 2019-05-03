package com.avantika.alumni.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.avantika.alumni.parameters.Industry_Domains;

@Dao
public interface DomainDao {

    @Insert
    void insertAll(Industry_Domains[] domainList);

    @Query("SELECT * from domainList")
    Industry_Domains[] getDomainList();

    @Query("DELETE FROM domainList")
    void deleteAllDomains();

    @Query("SELECT Domain_ID From domainList WHERE Domain_Name = :name")
    String getDomainID(String name);
}
