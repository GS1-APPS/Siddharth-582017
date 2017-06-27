<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
	String errorMessage = (String)request.getAttribute("errorMessage");

	String actionUrl = (String)request.getAttribute("actionUrl");
	String submitLabel = (String)request.getAttribute("submitLabel");

	String heading = (String)request.getAttribute("heading");
	
	String resetPasswordLink = (String)request.getAttribute("resetPasswordLink");
	String cancelLink = (String)request.getAttribute("cancelLink");
	
	String[] timezones = (String[])request.getAttribute("timezones");
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Register" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<c:if test="<%= errorMessage != null %>">
  <h3></h3>
  <div class="alert alert-danger">
     <%= errorMessage %>
  </div>
</c:if>

<h3><c:out value="<%= heading %>" /></h3>

<div class="row">
<div class="col-md-6">

<c:if test="<%= resetPasswordLink != null %>">
  <p>Click <a href="<%= resetPasswordLink %>">here</a> to reset the password for this account.
</c:if>

<form:form commandName="memberCommand" role="form" action="<%= actionUrl %>">
  <form:errors path="*" cssClass="alert alert-danger" element="div" />
  <div class="form-group">
    <label>GLN</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="gln" />
  </div>
  <div class="form-group">
    <label>Company Name</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="companyName" />
  </div>
  <div class="form-group">
    <label>GS1 Company Prefixes</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="gcps" />
  </div>
  <div class="form-group">
    <label>State Sales Tax Percentage for DWCode</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="dmStateSalesTaxPct" />
  </div>
  <div class="form-group">
    <label>Local Sales Tax Percentage for DWCode</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="dmLocalSalesTaxPct" />
  </div>
  <button type="submit" class="btn-primary btn-margin "><c:out value="<%= submitLabel %>" /></button>
  <button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelLink %>"'>Cancel</button>

</form:form>

</div>
</div>
<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
