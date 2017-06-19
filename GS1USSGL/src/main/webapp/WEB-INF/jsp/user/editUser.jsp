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
	
	boolean editUsername = (Boolean)request.getAttribute("editUsername");
	
	String resetPasswordLink = (String)request.getAttribute("resetPasswordLink");
	String cancelLink = (String)request.getAttribute("cancelLink");
	
	String[] timezones = (String[])request.getAttribute("timezones");
	String[] userTypeNames = (String[])request.getAttribute("userTypeNames");
	String[] userStateNames = (String[])request.getAttribute("userStateNames");
	
	String apiKey = (String)request.getAttribute("apiKey");
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Edit user" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<c:if test="<%= errorMessage != null %>">
  <h1></h1>
  <div class="alert alert-danger">
     <%= errorMessage %>
  </div>
</c:if>

<h1><c:out value="<%= heading %>" /></h1>

<div class="row">
<div class="col-md-6">

<c:if test="<%= resetPasswordLink != null %>">
  <p>Click <a href="<%= resetPasswordLink %>">here</a> to reset the password for this account.
</c:if>

<form:form commandName="userCommand" role="form" action="<%= actionUrl %>">
  <form:errors path="*" cssClass="alert alert-danger" element="div" />
  <c:if test="<%= editUsername %>">
  <div class="form-group">
    <label>E-mail Address</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="email" />
  </div>
  </c:if>
  <c:if test="<%= userTypeNames != null %>">
    <div class="form-group">
      <label>User Type</label>
      <form:select class="form-control" cssErrorClass="form-control alert-danger" path="userTypeName" items="<%= userTypeNames %>"/>
    </div>
  </c:if>
  <c:if test="<%= userStateNames != null %>">
    <div class="form-group">
      <label>Status</label>
      <form:select class="form-control" cssErrorClass="form-control alert-danger" path="userStateName" items="<%= userStateNames %>"/>
    </div>
  </c:if>
  <div class="form-group">
    <label>First Name</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="firstName" />
  </div>
  <div class="form-group">
    <label>Last Name</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="lastName" />
  </div>

   <div class="form-group">
    <label>Time Zone</label>
    <form:select class="form-control" cssErrorClass="form-control alert-danger" path="timezone" items="<%= timezones %>"/>
  </div>

  <% if (apiKey != null && !apiKey.equals("")) { %>
  <div class="form-group">
    <label>API Key</label>
    <%=apiKey %>    
  </div>
  <% } %>
  	
	<form:hidden path="apiKey"/>

  <button type="submit" class="btn-primary btn-margin "><c:out value="<%= submitLabel %>" /></button>
  <button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelLink %>"'>Cancel</button>

</form:form>

</div>
</div>
<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
