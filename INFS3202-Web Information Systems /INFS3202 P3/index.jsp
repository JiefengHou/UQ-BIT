<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>3202 p1</title>
		<meta name="description" content="Gallery of place">
		<meta name="keywords" content="INFS3202 Prac1">
		<meta name="author" content="Jiefeng Hou">
		<link rel="stylesheet" type="text/css" href="css/style.css">
		
	</head>
<%
	String name = "",price = "",dueTime = "",location = "",photoURL = "",description = "";
	String line;
    int i = 1;
	BufferedReader reader = new BufferedReader(new FileReader(request.getRealPath("/")+"photo1.txt"));
	while ((line = reader.readLine()) != null) {
		if(i==1)
		{
			 name = line;
		}
		if(i==2)
		{
			 price = line;
		}
		if(i==3)
		{
			 dueTime = line;
		}
		if(i==4)
		{
			 location = line;
		}
		if(i==5)
		{
			 photoURL = line;
		}
		else description = line;
		i++;		
	}
	reader.close();
%>

<%
	String name2 = "",price2 = "",dueTime2 = "",location2 = "",photoURL2 = "",description2 = "";
	String line2;
    int ii = 1;
	BufferedReader reader2 = new BufferedReader(new FileReader(request.getRealPath("/")+"photo2.txt"));
	while ((line = reader2.readLine()) != null) {
		if(ii==1)
		{
			 name2 = line;
		}
		if(ii==2)
		{
			 price2 = line;
		}
		if(ii==3)
		{
			 dueTime2 = line;
		}
		if(ii==4)
		{
			 location2 = line;
		}
		if(ii==5)
		{
			 photoURL2 = line;
		}
		else description2 = line;
		ii++;		
	}
%>
	<body>
		<div id="g1">
			<a href="loginform.jsp">Admin Page</a>
			<h1>Restaurants</h1>
			<ul>
				<p><%=name + "&nbsp&nbsp&nbsp" + price + "&nbsp&nbsp&nbsp" + dueTime%></p>
				<li><img src="images/photo1.jpg" width="150px" height="150px" alt="photo1">
				<a href="photo1.jsp" target="_blank">Read More</a></li>

				<p><%=name2 + "&nbsp&nbsp&nbsp" + price2 + "&nbsp&nbsp&nbsp" + dueTime2%></p>
				<li><img src="images/photo2.jpg" width="150px" height="150px" alt="photo2">
				<a href="photo2.jsp" target="_blank">Read More</a></li>
			</ul>
		</div>	
	</body>
</html>