<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();
$sql = "DELETE FROM user_user
        WHERE (user1_id = ".$_POST['user_id']."
        AND user2_id in (
            SElECT id
            FROM user
            WHERE username = '".$_POST['user2_username']."'))
        OR (user2_id = ".$_POST['user_id']."
        AND user1_id in (
            SElECT id
            FROM user
            WHERE username = '".$_POST['user2_username']."'));";
$result = $database->executeQuery($sql);
if($result) {
    $response["code"] = 2;
} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>