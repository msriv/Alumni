package com.avantika.alumni.parameters;

import android.arch.persistence.room.Entity;

@Entity
public class Authentication {
    public boolean error;
    public Profile profile;

    public static class Profile{
        public Personal_Information personal_information;
        public Education[] education;
        public Experience[] experience;
        public Phone_Number[] phone_number;


        public static class Personal_Information{
            public String Email_ID;
            public String F_Name;
            public String L_Name;
            public String DOB;
            public String Postal_Code;
            public String Street_Num;
            public String Street_Name;
            public String House_Num;
            public String Batch;
            public String profile_pics;
        }

        public static class Education{
            public String Qual_Title;
            public String Start_Date;
            public String End_Date;
            public String Domain_ID;
        }

        public static class Experience{
            public String Job_Title;
            public String Start_Date;
            public String End_Date;
            public String Domain_ID;
        }

        public static class Phone_Number{
            public String Phone_Number;
        }

    }
}


