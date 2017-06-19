<%@page import="org.gs1us.sgg.gbservice.api.AppSubscription"%>
<%@page import="org.gs1us.sgg.gbservice.api.AppDesc"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.gs1us.sgl.memberservice.User"%>
<%@page import="org.gs1us.sgl.webapp.ProductController"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>
<%@page import="org.gs1us.sgl.webapp.standalone.UserController"%>
<%@page import="org.gs1us.sgg.gbservice.api.Product" %>

<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
	String actionUrl = (String)request.getAttribute("actionUrl");
	String cancelUrl = (String)request.getAttribute("cancelUrl");
	AppDesc appDesc = (AppDesc)request.getAttribute("appDesc");
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Apps" />
  <jsp:param name="selectedItem" value="apps" />
</jsp:include>

<h3>Subscribe to <c:out value="<%= appDesc.getTitle() %>"/></h3>

<p><c:out value="<%= appDesc.getDescription() %>"/></p>
<p>To get started you must first accept the terms of service.</p>

<form:form commandName="subscribeCommand" role="form" action="<%= actionUrl %>">
  <form:errors path="*" cssClass="alert alert-danger" element="div" />

    <div class="form-group">
       <div class="checkbox">
          <form:checkbox path="acceptTermsOfService" /><label>By checking this box you are agreeing to the <a href="#" target="_blank"><c:out value="<%= appDesc.getTitle() %>"/> Terms of Service</a></label>
        </div>
        <form:input type="hidden" path="tosVersion" />
    </div>

  
  <button type="submit" class="btn-primary btn-margin "><c:out value="Subscribe" /></button>
  <button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelUrl %>"'>Cancel</button>

</form:form>




<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
