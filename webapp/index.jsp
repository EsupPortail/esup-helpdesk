<%@page import="org.springframework.util.StringUtils"%>
<html>
<head>
</head>
<body>
<%
String newLocation = "stylesheets/welcome.faces";
if (StringUtils.hasText(request.getQueryString())) {
	newLocation = newLocation + "?" + request.getQueryString();
}
response.sendRedirect(newLocation);
%>
</body>
</html>
