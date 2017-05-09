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
	String errorMessage = (String)request.getAttribute("errorMessage");
	String displayMessage = (String) request.getAttribute("ProductName");
	String productSearchUri = MvcUriComponentsBuilder.fromMethodName(SearchController.class, "search", (Object)null).toUriString();
%>
    
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Product Search" />
  <jsp:param name="selectedItem" value="productSearch" />
</jsp:include>

<h1><a href="<%= productSearchUri %>">Search for Products:</a> Key Authentication</h1>

<c:if test="<%= errorMessage != null %>">
  <h1></h1>
  <div class="alert alert-danger">
     <%= errorMessage %>
  </div>
</c:if>


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

<c:if test="<%=displayMessage != null %>">
<section>
    <div class="row">
    		<h3>SEARCH RESULTS</h3>
            <div class="content-module-wrapper top-border-orange">
                <div class="content-module content-module-padding">
                	     <%=displayMessage %>              
                </div>
            </div>
    </div>
</section>
</c:if>

</form>



</div>
</div>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />