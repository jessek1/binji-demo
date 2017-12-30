<html>
<body>
<h2>Hello World!</h2>
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%	Properties prop = System.getProperties(); 
%>
Java class path: <%=prop.getProperty("java.class.path")%>
</body>
</html>
