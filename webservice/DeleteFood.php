<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();
$sql = "DELETE FROM food
        WHERE id = ".$_POST['food_id'].";";
$result = $database->executeQuery($sql);
if($result) {
    $response["code"] = 2;
} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>