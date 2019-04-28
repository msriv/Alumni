<?php
    include_once '../config/GET_config.php';
    $news = array();
    $stmt = $dbFunctions->getAllData('alumni_directory');

    $news = $stmt->fetchAll(PDO::FETCH_OBJ);

   /*  while($row=$stmt->fetch()){
        $newsItem = array();
        $newsItem['Title'] = $row['Title'];
        $newsItem['Description'] = $row['Description'];
        $newsItem['Timestamp'] = $row['Date_Time'];

        array_push($news, $newsItem);
    } */
    
    echo json_encode($news, JSON_PRETTY_PRINT);



?>