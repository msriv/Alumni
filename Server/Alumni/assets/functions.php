<?php

class DatabaseFunctions{
    private $conn;

    public function __construct($db){
        $this->conn = $db;
    }

    public function authenticate($email){
        $query = "SELECT Email_ID FROM alumni_directory WHERE Email_ID= ? ";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1,$email);
        $stmt->execute();
        $count = $stmt->rowCount();
        return $count > 0;
    }

    public function getProfile($email){
        $personal_information = $this->conn->query("SELECT Email_ID, F_Name, L_Name, DOB, Postal_Code, Street_Num, Street_Name, House_Num, Batch FROM alumni_directory WHERE Email_ID='".$email."';")->fetchAll(PDO::FETCH_OBJ);
        $qual_history = $this->conn->query("SELECT Qual_Title, Start_Date, End_Date, Domain_ID FROM qual_history WHERE Email_ID='".$email."'")->fetchAll(PDO::FETCH_OBJ);
        $job_history = $this->conn->query("SELECT Job_Title, Start_Date, End_Date, Domain_ID FROM job_history WHERE Email_ID='".$email."'")->fetchAll(PDO::FETCH_OBJ);
        $phone_number = $this->conn->query("SELECT Phone_Number FROM alumni_directory_Phone_Number WHERE Email_ID = '".$email."'")->fetchAll(PDO::FETCH_OBJ);

        $profile['personal_information'] = $personal_information[0];
        $profile['education'] = $qual_history;
        $profile['experience'] = $job_history;
        $profile['phone_number'] = $phone_number;
        
        return $profile;
    }

    public function getAllData($table){
        $query = "SELECT * FROM ".$table.";";
        $stmt = $this->conn->query($query);
        return $stmt;
    }

    public function recognisedAlumni(){
        $query="CALL getRecognisedAlumni()";
        $stmt = $this->conn->query($query);
        return $stmt;
    }

    public function industryOffers($email){
        $query="CALL listOffersForAlumni('".$email."')";
        $stmt = $this->conn->query($query);
        $offers=array();
        while($row=$stmt->fetch()){
            $offersItem = array();
            $offersItem['Offer_ID'] = $row['Offer_ID'];
            $offersItem['Offer_Title'] = $row['Offer_title'];
            $offersItem['Domain_ID'] = $row['Domain_ID'];
            $offersItem['Description'] = $row['Description'];
            $offersItem['Total_Involvement'] = (int)$row['Total_Involvement'];
            array_push($offers, $offersItem);
        } 
        return $offers;
    }

    public function postOnWall($content, $postPhotos, $email){
        $query = "INSERT INTO wall_posts(Content, postPhotos, Email_ID) VALUES('".$content."', '".$postPhotos."', '".$email."');";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
    }

}

?>