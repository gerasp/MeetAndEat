<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$sql = "SELECT * FROM user WHERE username='" . $_POST['username'] . "'";
$result = $database->executeQuery($sql);

if ($result) {
    $row = $result->fetch_assoc();
    require 'vendor/autoload.php';

    $request_body = json_decode('{
      "personalizations": [
        {
          "to": [
            {
              "email": "'.$row['email'].'"
            }
          ],
          "subject": "Hello World from the SendGrid PHP Library!"
        }
      ],
      "from": {
        "email": "support@meetandeat.com"
      },
      "content": [
        {
          "type": "text/plain",
          "value": "Hello, Email!"
        }
      ]
    }');
    $apiKey = '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';
    $sg = new \SendGrid($apiKey);
    $sg->client->mail()->send()->post($request_body);

}
$response = array();
$response["code"] = 2;
echo json_encode($response);
$database->closeConnection();

