package com.avantika.alumni.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.avantika.alumni.parameters.Univ_Projects;

@Dao
public interface UnivProjectsDao {
    @Insert
    void insertAll(Univ_Projects[] projects);

    @Query("SELECT * from Univ_Projects")
    Univ_Projects[] getProjects();

    @Query("DELETE FROM Univ_Projects")
    void deleteAllProjects();
}
