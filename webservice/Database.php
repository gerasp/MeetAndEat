<?php

class Database
{
    private $server = "eu-cdbr-west-01.cleardb.com:3306";
    private $username = "b4bb93f7820d8c";
    private $password = "2b085a86";
    private $db = "heroku_7ff54ac22b69462";
    private $conn;

    public function __construct($key)
    {
        if ($key != "Df5f5z7e6W5pR2D2yEMK7Vkb77cp23nP") $this->openConnection();
    }

    public function openConnection()
    {
        $this->conn = new mysqli($this->server, $this->username, $this->password, $this->db);
        if ($this->conn->connect_error) {
            die("Connection failed: " . $this->conn->connect_error);
        }
        if ($this->conn === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }
    }

    public function closeConnection()
    {
        mysqli_close($this->conn);
    }

    public function executeQuery($sql)
    {
        return mysqli_query($this->conn, $sql);
    }
    public function getLastId()
    {
        return mysqli_insert_id($this->conn);
    }
}
