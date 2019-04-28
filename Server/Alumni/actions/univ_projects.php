<?php
    include_once '../config/GET_config.php';
    $projects = array();
    $stmt = $dbFunctions->getAllData('univ_projects');
    
    //$projects = $stmt->fetchAll(PDO::FETCH_OBJ);
    while($row=$stmt->fetch()){
        $project = array();
        $project['Title'] = $row['Title'];
        $project['Description'] = $row['Description'];
        $project['Funds'] = (double)$row['Funds'];
        $project['Domain_ID'] = $row['Domain_ID'];

        array_push($projects, $project);
    } 
    $response['univ_projects'] = $projects;
    echo json_encode($response, JSON_PRETTY_PRINT);



?>