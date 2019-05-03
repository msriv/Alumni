package com.avantika.alumni.parameters;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class News {

    @NonNull
    @PrimaryKey
    public String News_ID;

    public String Title;
    public String Date_Time;
    public String Description;
    public String Author;
    public String Thumbnail;
}
