<?php
	$username = $_REQUEST['username'];
	$password = $_REQUEST['password'];
	session_start(); 
	if (($username == "infs" || $username == "INFS") && ($password == "3202")){
		$_SESSION['username'] = "INFS"; 
		$_SESSION['password'] = "3202";
		$_SESSION['timeout'] = time() + $_REQUEST['select'];
		$_SESSION['state'] = "login";
		header("Location: index.php");	
	}

	else echo "Incorrect username/password";
?>