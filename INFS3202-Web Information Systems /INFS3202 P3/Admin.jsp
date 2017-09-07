<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	if (session.getAttribute("username")==null || session.getAttribute("password")==null) 
	{
		response.sendRedirect("loginform.jsp");
	}
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="description" content="Gallery of place">
	<meta name="keywords" content="INFS3202 Prac1">
	<meta name="author" content="Jiefeng Hou">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<title>Admin Page</title>
	<script type="text/javascript">
		function showform(n) {
			if(n==1) {
				if(document.getElementById("form1").style.display == "block")
				{
					document.getElementById("form1").style.display = "none";
				}
				else document.getElementById("form1").style.display = "block";
			}
			if(n==2) {
				if(document.getElementById("form2").style.display == "block")
				{
					document.getElementById("form2").style.display = "none";
				}
				else document.getElementById("form2").style.display = "block";
			}			
		}	
		
		function formValidation() {
			
			if(document.getElementById("name").value == "")
			{
				alert("Name cannot be empty!");
				return false;		
			}
			
			if(document.getElementById("name2").value == "")
			{
				alert("Name cannot be empty!");
				return false;		
			}
		}
	</script>
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
	<form method="post" action="" onSubmit="return formValidation()">
		<table class="file">
			<tr bgcolor="#FAEBD7">
				<td width="200">Graham Ian</td>
				<td width="50"><a href="#" onclick="showform(1)">Edit</a></td>
			</tr>
		</table>
		<table id="form1" style="display: none">
			<tr>
				<td>Name:</td>
				<td><input id="name" type="text" name="name" value="<%=request.getParameter("name")==null?name:request.getParameter("name")%>"></td>
			</tr>
			<tr>
				<td>Price:</td>
				<td><input type="text" name="price" value="<%=request.getParameter("price")==null?price:request.getParameter("price")%>"></td>
			</tr>
			<tr>
				<td>Due Time:</td>
				<td><input type="text" name="dueTime" value="<%=request.getParameter("dueTime")==null?dueTime:request.getParameter("dueTime")%>"></td>
			</tr>			
			<tr>
				<td>Location:</td>
				<td><input type="text" name="location" value="<%=request.getParameter("location")==null?location:request.getParameter("location")%>"></td>
			</tr>
			<tr>
				<td>Photo url:</td>
				<td><input type="text" name="photoURL" value="<%=request.getParameter("photoURL")==null?photoURL:request.getParameter("photoURL")%>"></td>
			</tr>
			<tr>
				<td>Description:</td>
				<td><input type="text" name="description" value="<%=request.getParameter("description")==null?description:request.getParameter("description")%>"></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Edit"></td>
			</tr>
		</table>
	</form>

	<form method="post" action="" onSubmit="return formValidation()">
		<table class="file">
			<tr bgcolor="#00FFFF">
				<td width="200">Schulze Hans Herbert</td>
				<td width="50"><a href="#" onclick="showform(2)">Edit</a></td>
			</tr>
		</table>
		<table id="form2" style="display: none">
			<tr>
				<td>Name:</td>
				<td><input id="name2" type="text" name="name2" value="<%=request.getParameter("name2")==null?name2:request.getParameter("name2")%>"></td>
			</tr>
			<tr>
				<td>Price:</td>
				<td><input type="text" name="price2" value="<%=request.getParameter("price2")==null?price2:request.getParameter("price2")%>"></td>
			</tr>
			<tr>
				<td>Due Time:</td>
				<td><input type="text" name="dueTime2" value="<%=request.getParameter("dueTime2")==null?dueTime2:request.getParameter("dueTime2")%>"></td>
			</tr>			
			<tr>
				<td>Location:</td>
				<td><input type="text" name="location2" value="<%=request.getParameter("location2")==null?location2:request.getParameter("location2")%>"></td>
			</tr>
			<tr>
				<td>Photo url:</td>
				<td><input type="text" name="photoURL2" value="<%=request.getParameter("photoURL2")==null?photoURL2:request.getParameter("photoURL2")%>"></td>
			</tr>
			<tr>
				<td>Description:</td>
				<td><input type="text" name="description2" value="<%=request.getParameter("description2")==null?description2:request.getParameter("description2")%>"></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Edit"></td>
			</tr>
		</table>
	</form>
</body>
</html>

<%
	if(request.getParameter("name") != null)
	{
		name=request.getParameter("name");
		price=request.getParameter("price");
		dueTime=request.getParameter("dueTime");
		location=request.getParameter("location");
		photoURL=request.getParameter("photoURL");
		description=request.getParameter("description");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(request.getRealPath("/")+"photo1.txt"));
		writer.write(name+"\n"+price+"\n"+dueTime+"\n"+location+"\n"+photoURL+"\n"+description+"\n");
		writer.close();		
	}
%>

<%
	if(request.getParameter("name2") != null)
	{
		name2=request.getParameter("name2");
		price2=request.getParameter("price2");
		dueTime2=request.getParameter("dueTime2");
		location2=request.getParameter("location2");
		photoURL2=request.getParameter("photoURL2");
		description2=request.getParameter("description2");
		
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(request.getRealPath("/")+"photo2.txt"));
		writer2.write(name2+"\n"+price2+"\n"+dueTime2+"\n"+location2+"\n"+photoURL2+"\n"+description2+"\n");
		writer2.close();		
	}
%>
