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
	String keyAuthenticationUrl = MvcUriComponentsBuilder.fromMethodName(SearchController.class, "searchByAuthentication", (Object)null).toUriString();
	String productValidationUrl = MvcUriComponentsBuilder.fromMethodName(SearchController.class, "searchByValidation", (Object)null).toUriString();
	String searchFunctionUrl = MvcUriComponentsBuilder.fromMethodName(SearchController.class, "searchByProduct", (Object)null).toUriString();
%>
        
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Product Search" />
  <jsp:param name="selectedItem" value="productSearch" />
</jsp:include>

<h1>Search</h1>


<section>
    <div class="row">
        <div class="col-sm-5">
            <div class="content-module-wrapper top-border-orange">
                <div class="content-module content-module-padding">                    
                    <a href="<%= keyAuthenticationUrl %>">Key Authentication:</a> 
                    <i>selecting this will return whether or not the GTIN comes from a GS1 assigned Global Company Prefix (GCP)</i>
                    <br/><br/>                    
                    <a href="<%= productValidationUrl %>">Product Validation:</a>
                    <i>selecting this will return all available attributes for a given Global Trade Number (GTIN)</i>
                    <br/><br/>
                    <a href="<%= searchFunctionUrl %>">Search Function:</a> 
                    <i>selecting this will allow you to search products by the Global Product Classification (GPC) and Target Market</i>                    
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />