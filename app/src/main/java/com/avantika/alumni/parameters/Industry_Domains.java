package com.avantika.alumni.parameters;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "domainList")
public class Industry_Domains {
    @NonNull
    @PrimaryKey
    public String Domain_ID;
    @NonNull
    public String Domain_Name;

    @NonNull
    @Override
    public String toString() {
        return Domain_Name;
    }
}
