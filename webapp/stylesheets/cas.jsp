<%
response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
response.setHeader("Location", "welcome.faces?enter=");
%>