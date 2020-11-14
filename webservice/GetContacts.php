<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();
$sql = "SELECT username
        FROM user
        WHERE id in 
            (SELECT uu.user2_id id
            FROM user_user uu
            WHERE uu.user1_id = ".$_POST['user_id']."
            AND uu.isAccepted = 1)
        OR id in 
            (SELECT uu.user1_id id
            FROM user_user uu
            WHERE uu.user2_id = ".$_POST['user_id']."
            AND uu.isAccepted = 1)
        ORDER BY username ASC;";
$result = $database->executeQuery($sql);
if($result) {
    $response["code"] = 2;
    $response["results"] = array();
    while($row = $result->fetch_assoc()) {
        array_push($response["results"],$row);
    }
} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>