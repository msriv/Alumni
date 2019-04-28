<?php
    include_once '../config/GET_config.php';
    $news = array();
    $stmt = $dbFunctions->getAllData('univ_news');

    //$news = $stmt->fetchAll(PDO::FETCH_OBJ);

     while($row=$stmt->fetch()){
        $newsItem = array();
        $newsItem['News_ID'] = $row['News_ID'];
        $newsItem['Title'] = $row['Title'];
        $newsItem['Description'] = $row['Description'];
        $newsItem['Timestamp'] = $row['Date_Time'];
        array_push($news, $newsItem);
    }
    $response['news'] = $news;
    echo json_encode($response, JSON_PRETTY_PRINT);



?>