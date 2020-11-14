<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();
$sql = "SELECT username
        FROM user
        WHERE id NOT IN 
            (SELECT uu.user2_id id
            FROM user_user uu
            WHERE uu.user1_id = " . $_POST['user_id'] . ")
        AND id NOT IN 
            (SELECT uu.user1_id id
            FROM user_user uu
            WHERE uu.user2_id = " . $_POST['user_id'] . ")
        AND SOUNDEX('" . $_POST['username'] . "') = SOUNDEX(username)
        AND id != " . $_POST['user_id'] . "
        ORDER BY username ASC;";

$result = $database->executeQuery($sql);
if ($result->num_rows > 0) {
    $response["code"] = 2;
    $response["results"] = array();
    while ($row = $result->fetch_assoc()) {
        array_push($response["results"], $row);
    }
} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>