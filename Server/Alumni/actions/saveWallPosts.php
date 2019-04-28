<?php
 include_once '../config/POST_config.php';
 if(isset($_POST['content']) && isset($_POST['email'])){
    $dbFunctions->postOnWall($_POST['content'], $_POST['photo'], $_POST['email']);
    $response['error'] = false;
    $response['message'] = "Posted";
}else {
    $response['error'] = true;
    $response['message'] = "Not Posted";
 }

 echo json_encode($response, JSON_PRETTY_PRINT);
?>