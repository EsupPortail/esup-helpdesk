<html>
<head>
</head>
<body>
<h1>LOGOUT</h1>
<%
	HttpSession sessionHelpdesk = request.getSession();
	session.invalidate();
	response.sendRedirect("index.jsp");
%>
</body>
</html>