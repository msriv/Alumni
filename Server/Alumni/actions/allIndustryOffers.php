<?php
    include_once '../config/GET_config.php';
    $stmt = $dbFunctions->getAllData('Industry_Offers');
    $response = $stmt->fetchAll(PDO::FETCH_OBJ);
    echo json_encode($response, JSON_PRETTY_PRINT);
?>