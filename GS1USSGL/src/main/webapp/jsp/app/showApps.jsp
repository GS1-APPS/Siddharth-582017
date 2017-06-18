<%@page import="java.util.Collection"%>
<%@page import="org.gs1us.sgl.webapp.AppController"%>
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

<%
	Collection<AppDesc> unsubscribedApps = (Collection<AppDesc>)request.getAttribute("unsubscribedApps");
	Collection<AppSubscription> subscriptions = (Collection<AppSubscription>)request.getAttribute("subscriptions");
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Apps" />
  <jsp:param name="selectedItem" value="apps" />
</jsp:include>

<h3>GTIN Applications</h3>
<p>Enhance your products with these GTIN applications.</p>
<h4>Your Apps</h4>
<table class="sgl-app-table">
<tbody>
<tr>
<% { int i = 0;
     for (AppSubscription subscription : subscriptions) {
		AppDesc appDesc = subscription.getAppDesc();
%>

<td>
<div class="sgl-app-table-icon"><a href="#"><img src='<%= String.format("%s/images/%s-icon-96.png", request.getContextPath(), appDesc.getIconName()) %>'/></a></div>
<div class="sgl-app-table-title"><c:out value="<%= appDesc.getTitle() %>" /></div>
</td>
<c:if test="<%= (i % 5) == 4 %>">
</tr><tr>
</c:if>
<%
 i++;}}
%>
</tr>
</tbody>
</table>
<h4>Other Apps Available for Sign-Up</h4>
<table class="sgl-app-table">
<tbody>
<tr>
<% { int i = 0; 
	for (AppDesc appDesc : unsubscribedApps) {
	String subscribeUrl = MvcUriComponentsBuilder.fromMethodName(AppController.class, "subscribeGet", (Object)null, (Object)null, appDesc.getName()).toUriString();
%>

<td>
<div class="sgl-app-table-icon"><a href="<%= subscribeUrl %>"><img src='<%= String.format("%s/images/%s-icon-96.png", request.getContextPath(), appDesc.getIconName()) %>'/></a></div>
<div class="sgl-app-table-title"><c:out value="<%= appDesc.getTitle() %>" /></div>
</td>
<c:if test="<%= (i % 5) == 4 %>">
</tr><tr>
</c:if>
<%
  i++;}}
%>
</tr>
</tbody>
</table>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
