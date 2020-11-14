<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();

$sql = "INSERT INTO user VALUES (null,'".$_POST['username']."',
'".$_POST['email']."',
'".password_hash($_POST['password'], PASSWORD_BCRYPT)."');";
$result = $database->executeQuery($sql);

if ($result) {
    $database->closeConnection();
    require_once("LoginUser.php");
} else {
    $response["code"] = 0;
    echo json_encode($response);
}

$database->closeConnection();
?>