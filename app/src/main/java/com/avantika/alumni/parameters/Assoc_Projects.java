package com.avantika.alumni.parameters;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Assoc_Projects {
    @NonNull
    @PrimaryKey
    public String A_Proj_ID;

    public String Title;
    public String Description;
    public double Funds;
    public String Domain_ID;
}


