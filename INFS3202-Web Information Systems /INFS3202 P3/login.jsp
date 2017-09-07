
<% 
	String username = request.getParameter("username");
	String password = request.getParameter("password"); 

	
	if (username.equals("admin") && password.equals("password")) 
	{
		session.setAttribute("username", username);
		session.setAttribute("password", password);
		response.sendRedirect("Admin.jsp");
	}
	
	else response.sendRedirect("loginform.jsp");
%>