<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();
$sql = "INSERT INTO user_user 
        VALUES (".$_POST['user_id'].",
        (SELECT id FROM user WHERE username = '".$_POST['user2_username']."'),0)";

$result = $database->executeQuery($sql);
if($result) {
    $response["code"] = 2;
} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>