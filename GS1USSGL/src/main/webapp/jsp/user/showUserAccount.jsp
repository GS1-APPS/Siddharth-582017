<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.gs1us.sgl.webapp.AccountController"%>
<%@page import="org.gs1us.sgl.webapp.standalone.UserController"%>
<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="java.util.Collection"%>
<%@page import="org.gs1us.sgl.webapp.ProductController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.gs1us.sgl.webapp.SignupController"%>
<%@page import="org.gs1us.sgg.gbservice.api.Product"%>
<%@page import="java.util.List"%>
<%@page import="org.gs1us.sgg.gbservice.api.GBAccount"%>
<%@page import="java.security.Principal"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@page import="org.gs1us.sgl.webapp.SearchController"%>
<%@page import="org.gs1us.sgg.gbservice.api.IsoCountryRef"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String firstName = (String) request.getAttribute("firstName");
	String lastName = (String) request.getAttribute("lastName");	
	String email = (String) request.getAttribute("email");
	String apiKey = (String) request.getAttribute("apiKey");
	String timeZone = (String) request.getAttribute("timeZone");	
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="User Account Details" />
  <jsp:param name="selectedItem" value="userAccount" />
</jsp:include>

<h1>Account</h1>

<table>
<tr>
<td>
    <label>First Name</label> <%=firstName %>
</td>
</tr>

<tr>
<td>
    <label>Last Name</label> <%=lastName %>
</td>
</tr>

<tr>
<td>
    <label>Email</label> <%=email %>
</td>
</tr>
<% if (apiKey != null && !apiKey.equals("")) { %>
<tr>
<td>
    <label>API Key</label> <%=apiKey %>
</td>
</tr>
<% } else { %>
<tr>
<td>
    <label>API Key</label> Not Generated
</td>
</tr>
<% } %>
<tr>
<td>
    <label>Time Zone</label> <%=timeZone %>
</td>
</tr>

</table>

<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>	

<jsp:include page="/jsp/includes/footer.jsp" flush="true" />