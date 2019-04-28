<?php
    include_once '../config/GET_config.php';
    $events = array();
    $stmt = $dbFunctions->getAllData('univ_events');

    //$events = $stmt->fetchAll(PDO::FETCH_OBJ);
    while($row=$stmt->fetch()){
        $event = array();
        $event['Event_ID'] = $row['Event_ID'];
        $event['Title'] = $row['Title'];
        $event['Description'] = $row['Description'];
        $event['Timestamp'] = $row['Date_Time'];
        $event['Domain_ID'] = $row['Domain_ID'];
        array_push($events, $event);
    } 
    $response['events'] = $events;
    echo json_encode($response, JSON_PRETTY_PRINT);

?>