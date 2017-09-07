<?php 
	session_start(); 
	if(isset($_SESSION['username']) && isset($_SESSION['password']) )
	{
		$file = fopen("/tmp/log.txt", "a");
		fwrite($file, date("Y-m-d H:i:s", time()) ." ".$_SESSION['username'] ." Logout user" ."\r\n");
		fclose($file);
		session_unset();
	} 
?>

<html>
	<head>
		<title>Login Form</title>	
	</head>
	<body>
		<form method="POST" action="login.php">
			<p>Login Information</p>
			Username:<input type="text" name="username" size="20"><br>
			Password:<input type="password" name="password" size="20"><br>
			<p>Stay Logged in for</p>
			<select name="select">
				<option value="30">30 Sec</option>
				<option value="86400">1 Day</option>
			</select>
			<input type="submit" value="Login" name="login" size="20">
		</form>	
	</body>
</html>