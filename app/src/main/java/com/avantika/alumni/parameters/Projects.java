package com.avantika.alumni.parameters;

public class Projects {
    public Assoc_Project[] assoc_projects;
    public Univ_Project[] univ_projects;
    public static class Assoc_Project{
        public String Title;
        public String Description;
        public double Funds;
        public String Domain_ID;
    }

    public static class Univ_Project{
        public String Title;
        public String Description;
        public double Funds;
        public String Domain_ID;
    }
}
