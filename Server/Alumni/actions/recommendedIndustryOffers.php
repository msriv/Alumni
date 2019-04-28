<?php
    include_once '../config/GET_config.php';
    $response = $dbFunctions->industryOffers($_GET['email']);
    echo json_encode($response, JSON_PRETTY_PRINT);



?>