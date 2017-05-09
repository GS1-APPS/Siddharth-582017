<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.gs1us.sgl.serviceterms.TermsOfService"%>
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
	TermsOfService termsOfService = (TermsOfService)request.getAttribute("termsOfService");

	String cancelLink = (String)request.getAttribute("cancelLink");
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Sign up" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<c:if test="<%= errorMessage != null %>">
  <h1></h1>
  <div class="alert alert-danger">
     <%= errorMessage %>
  </div>
</c:if>

<h1>Sign up to use the <%= WebappUtil.longProductHtml() %></h1>

<div class="row">
<div class="col-md-8">

<p>Welcome to the <%= WebappUtil.longProductHtml() %>! The <%= WebappUtil.shortProductHtml() %> lets you publish consumer-facing information about your products<%= WebappUtil.valueAddedPhrase() %>.</p>
<p>To get started you must first accept the <%= WebappUtil.productOperator() %> Brand Owner License Agreement.</p>

<form:form commandName="signupCommand" role="form" action="<%= actionUrl %>">
  <form:errors path="*" cssClass="alert alert-danger" element="div" />

    <div class="form-group">
       <div class="checkbox">
          <form:checkbox path="acceptTermsOfService" /><label>By checking this box you are agreeing to the <a href="<%= termsOfService.getUrl() %>" target="_blank"><%= WebappUtil.productOperator() %> Brand Owner License Agreement</a></label>
        </div>
        <form:input type="hidden" path="tosVersion" />
    </div>

  
  <button type="submit" class="btn-primary btn-margin "><c:out value="<%= submitLabel %>" /></button>
  <button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelLink %>"'>Cancel</button>

</form:form>

</div>
</div>
<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
