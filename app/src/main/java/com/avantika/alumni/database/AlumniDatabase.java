package com.avantika.alumni.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.avantika.alumni.parameters.Assoc_Projects;
import com.avantika.alumni.parameters.Directory;
import com.avantika.alumni.parameters.Events;
import com.avantika.alumni.parameters.Industry_Domains;
import com.avantika.alumni.parameters.News;
import com.avantika.alumni.parameters.Univ_Projects;

@Database(
        entities = {
                Industry_Domains.class,
                News.class,
                Events.class,
                Univ_Projects.class,
                Assoc_Projects.class,
                Directory.class
        },
        version = 6,
        exportSchema = false
)
public abstract class AlumniDatabase extends RoomDatabase {
    public abstract DomainDao domainDao();

    public abstract NewsDao newsDao();

    public abstract EventsDao eventsDao();

    public abstract AssocProjectsDao assocProjectsDao();

    public abstract UnivProjectsDao univProjectsDao();

    public abstract DirectoryDao directoryDao();

}
