package com.avantika.alumni.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.avantika.alumni.parameters.Events;

@Dao
public interface EventsDao {
    @Insert
    void insertAll(Events[] events);

    @Query("SELECT * from Events")
    Events[] getEvents();

    @Query("DELETE FROM Events")
    void deleteAllEvents();

}
