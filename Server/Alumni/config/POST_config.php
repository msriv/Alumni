<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');

include_once 'database.php';
include_once '../assets/functions.php';

$database = new Database();
$db = $database->getConnection();
$dbFunctions = new DatabaseFunctions($db);