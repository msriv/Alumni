<?php
include_once '../config/GET_config.php';

$response = array();
    if($dbFunctions->authenticate($_GET['email'])){
        $message['error']=false;
        $message['message']="User Exists";
        $response['message']= $message;
        $response['profile']=$dbFunctions->getProfile($_GET['email']);
    }else {
        $message['error']=true;
        $message['message']="No Such User";
        $response['message'] = $message;
        $response['profile'] = null;
    }
    
    echo json_encode($response,JSON_PRETTY_PRINT);


?>