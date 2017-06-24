
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeSet"%>
<%@page import="org.gs1us.sgg.gbservice.api.Product"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<body>
<form method="post" action="/GS1USSGG/demo/deleteimports">
<p>Delete products created within past <input name="minutes" /> minutes.</p>
<input type="submit" value="Delete"></input>
</form>
</body>
</html>