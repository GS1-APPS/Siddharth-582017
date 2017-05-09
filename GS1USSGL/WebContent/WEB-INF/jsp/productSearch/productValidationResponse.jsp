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

<%@page import="org.gs1us.sgg.gbservice.api.Product"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeSet"%>

<%
	String errorMessage = (String)request.getAttribute("errorMessage");
	Product product = (Product) request.getAttribute("Product");
	AttributeSet attributes = null;	
	String brandName = "";
	String subBrand = "";
	String targetMarket = "N/A";
	String imageUrl = null;
	
	if (product != null)
	{
		attributes = product.getAttributes();
		brandName = attributes.getAttribute("brandName");
		subBrand = attributes.getAttribute("additionalTradeItemDescription");
		if (attributes.getAttribute("targetMarket") != null)
		{
			targetMarket = attributes.getAttribute("targetMarket");	
		}
		
		if (attributes.getAttribute("uriProductImage") != null)
		{
			imageUrl = attributes.getAttribute("uriProductImage");	
		}		
	}
	
	String productSearchUri = MvcUriComponentsBuilder.fromMethodName(SearchController.class, "search", (Object)null).toUriString();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Product Search" />
  <jsp:param name="selectedItem" value="productSearch" />
</jsp:include>

<h1><a href="<%= productSearchUri %>">Search for Products:</a> Product Validation</h1>

<c:if test="<%= errorMessage != null %>">
  <h1></h1>
  <div class="alert alert-danger">
     <%= errorMessage %>
  </div>
</c:if>


<div class="row">
<div class="col-md-6">
<i>
 Enter a Global Trade Item Number (GTIN) to view the available product attributes                    	
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

<c:if test="<%= product != null && attributes != null %>">
<section>
    <div class="row">
    	<h3>SEARCH RESULTS</h3>
    	<table>
	   	<c:if test="<%= imageUrl != null %>">
	    	<tr>
	    		<td><div style="border:1px solid black; margin: 10px"><img src="<%= imageUrl %>" width="150px" height="150px"/></div></td>
	    		<td><%=product.getGtin() %> : <%=brandName %>, <%=subBrand %>, Target Market: <%=targetMarket %></td>
	    	</tr>
	    </c:if>
	   	<c:if test="<%= imageUrl == null %>">
	    	<tr>
	    		<td colspan="2"><%=product.getGtin() %> : <%=brandName %>, <%=subBrand %>, Target Market: <%=targetMarket %></td>
	    	</tr>
	    </c:if>
    	</table>
    </div>
</section>
</c:if>
</form>

</div>
</div>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />