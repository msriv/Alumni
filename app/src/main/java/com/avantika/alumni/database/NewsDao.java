package com.avantika.alumni.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.avantika.alumni.parameters.News;

@Dao
public interface NewsDao {
    @Insert
    void insertAll(News[] news);

    @Query("SELECT * from News")
    News[] getNews();

    @Query("DELETE FROM News")
    void deleteAllNews();

}
