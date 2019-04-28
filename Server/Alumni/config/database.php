<?php
    class Database{
        private $HOST = "localhost";
        private $DB_NAME = "alumni";
        private $USER = "root";
        private $PASSWORD = "mihir@1198";

        public function getConnection(){
            $this->conn = null;
            try{
                $this->conn = new PDO("mysql:host=" . $this->HOST . ";dbname=" . $this->DB_NAME, $this->USER, $this->PASSWORD);
                $this->conn->exec("set names utf8");
            }catch(PDOException $exception){
                echo "Connection error: " . $exception->getMessage();
        }
 
        return $this->conn;
    }
}

?>