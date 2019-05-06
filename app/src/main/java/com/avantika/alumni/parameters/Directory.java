package com.avantika.alumni.parameters;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Directory {

    @NonNull
    @PrimaryKey
    public String Email_ID;

    public String Name;

    public String Batch;
    public String Program;
    public String profile_pics;

    @Ignore
    public UserEducation[] education;
    @Ignore
    public UserExperience[] experience;


    public static class UserEducation {
        public String Qual_Title;
        public String Start_Date;
        public String End_Date;
    }

    public static class UserExperience {
        public String Job_Title;
        public String Start_Date;
        public String End_Date;
    }


}
