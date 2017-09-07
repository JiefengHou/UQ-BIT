<%
	if (session.getAttribute("username")!=null && session.getAttribute("password")!=null) 
	{
		response.sendRedirect("Admin.jsp");
	}

%>

<html>
	<head>
		<title>Login Form</title>	
		<script type="text/javascript">
			function check()
			{
				if(document.getElementById("user").value != "admin" || document.getElementById("password").value != "password" )
				{
					alert("Incorrect username or password");
					return false;
				}
			}
		</script>
	</head>
	<body>
		<form method="POST" action="login.jsp" onSubmit="return check()">
			<p>Login Information</p>
			Username:<input id="user" type="text" name="username" size="20"><br>
			Password:<input id="password" type="password" name="password" size="20"><br>
			<input type="submit" value="Login" name="login" size="20">
		</form>	
	</body>
</html>