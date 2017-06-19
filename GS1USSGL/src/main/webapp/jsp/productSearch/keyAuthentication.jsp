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
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%	
	String productSearchUri = MvcUriComponentsBuilder.fromMethodName(SearchController.class, "search", (Object)null).toUriString();
%>
    
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Product Search" />
  <jsp:param name="selectedItem" value="productSearch" />
</jsp:include>

<h1><a href="<%= productSearchUri %>">Search:</a> Key Authentication</h1>

<div class="row">
<div class="col-md-6">
<i>
 Enter the Global Trade Item Number to find out whether or not the GTIN comes from a GS1 assigned Global Company Prefix (GCP)                	
</i>               
<br/><br/>
<form method="post">
  <div class="form-group">
    <label>Global Trade Item Number</label>
    <input name="itemNumber" type="text" class="form-control" />
  </div>
	<button type="submit" class="btn-primary btn-margin ">Search</button>
    <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>	
</form>

</div>
</div>


<jsp:include page="/jsp/includes/footer.jsp" flush="true" />