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
	Collection<? extends IsoCountryRef> countryList = (Collection<? extends IsoCountryRef>) request.getAttribute("countryList");
	String productSearchUri = MvcUriComponentsBuilder.fromMethodName(SearchController.class, "search", (Object)null).toUriString();
%>
    
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Product Search" />
  <jsp:param name="selectedItem" value="productSearch" />
</jsp:include>

<h1><a href="<%= productSearchUri %>">Search:</a> Search Function</h1>

<div class="row">
<div class="col-md-6">
<i>
 Choose the Global Product Classification (GPC) and Target Market to see a list of available products                	
</i>               
<br/><br/>
<form method="post">
<table>
<tr>
<td>
    <label>Global Product Classification (GPC)</label>
    <input name="gpcNumber" type="text" class="form-control" />
    <a href="http://www.gs1.org/gpc/browser" target="_new">Browse available codes</a>
</td>
</tr>
<tr><td>&nbsp;</td></tr>
<tr>
<td>
	<label>Target Market</label>
    <select name="targetMarket" class="form-control">
    <% for (IsoCountryRef country : countryList) { %> 
    	<option value="<%= country.getId()%>"><%= country.getCountryName()%></option>
    <% } %>    	
    </select>
</td>
</tr>
</table>

<button type="submit" class="btn-primary btn-margin ">Search</button>	
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>	
</form>
</div>
</div>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />