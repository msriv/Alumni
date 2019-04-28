<?php
    include_once '../config/GET_config.php';
    $recognisedAlumni = array();
    $stmt = $dbFunctions->recognisedAlumni();

    $recognisedAlumni = $stmt->fetchAll(PDO::FETCH_OBJ);

   /*  while($row=$stmt->fetch()){
        $recognisedAlumniItem = array();
        $recognisedAlumniItem['Title'] = $row['Title'];
        $recognisedAlumniItem['Description'] = $row['Description'];
        $recognisedAlumniItem['Timestamp'] = $row['Date_Time'];

        array_push($recognisedAlumni, $recognisedAlumniItem);
    } */
    $response['recognisedAlumni'] = $recognisedAlumni;
    echo json_encode($response, JSON_PRETTY_PRINT);



?>