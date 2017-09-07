<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>photo1</title>
</head>
<%
	String name = "",price = "",dueTime = "",location = "",photoURL = "",description = "";
	String line;
    int i = 1;
	BufferedReader reader = new BufferedReader(new FileReader(request.getRealPath("/")+"photo2.txt"));
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
<body>
	<h2><%=name + "&nbsp&nbsp&nbsp" + price + "&nbsp&nbsp&nbsp" + dueTime%></h2>
	<img src="<%=photoURL%>">
	<h2>Location: <%=location %></h2>
	<h3>Description:</h3>
	<p><%=description%></p>
</body>
</html>