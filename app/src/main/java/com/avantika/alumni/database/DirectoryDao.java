package com.avantika.alumni.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.avantika.alumni.parameters.Directory;

@Dao
public interface DirectoryDao {

    @Insert
    void insertAll(Directory[] directory);

    @Query("SELECT * FROM Directory WHERE Name LIKE :name")
    Directory[] getDirectory(String name);

    @Query("SELECT * FROM Directory")
    Directory[] getAllUsers();

    @Query("DELETE FROM Directory")
    void deleteAllUsers();
}
