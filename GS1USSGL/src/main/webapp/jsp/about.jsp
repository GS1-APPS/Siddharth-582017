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
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    

<%
	Map<String,String> properties = (Map<String,String>)request.getAttribute("properties");
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="About" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<h1>About the <%= WebappUtil.longProductHtml() %></h1>

<table class="table table-striped">
	<tbody>
		<% for (Map.Entry<String,String> entry : properties.entrySet()) { %>
			<tr><td><c:out value="<%= entry.getKey() %>"/></td><td><c:out value="<%= entry.getValue() %>"/></td></tr>
		<% } %>
	</tbody>
</table>



<jsp:include page="/jsp/includes/footer.jsp" flush="true" />