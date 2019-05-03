package com.avantika.alumni.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.avantika.alumni.parameters.Assoc_Projects;

@Dao
public interface AssocProjectsDao {
    @Insert
    void insertAll(Assoc_Projects[] projects);

    @Query("SELECT * from Assoc_Projects")
    Assoc_Projects[] getProjects();

    @Query("DELETE FROM Assoc_Projects")
    void deleteAllProjects();
}
