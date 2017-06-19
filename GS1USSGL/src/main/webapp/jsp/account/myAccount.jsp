<%@page import="org.gs1us.sgl.memberservice.Member"%>
<%@page import="org.gs1us.sgl.webapp.AccountController"%>
<%@page import="org.gs1us.sgg.gbservice.api.SalesOrder"%>
<%@page import="org.gs1us.sgg.gbservice.api.BillingTransactionType"%>
<%@page import="org.gs1us.sgg.gbservice.api.Amount"%>
<%@page import="org.gs1us.sgg.gbservice.api.BillingTransaction"%>
<%@page import="java.util.Collection"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.gs1us.sgl.memberservice.User"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>

<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	Member member = (Member)request.getAttribute("member");
	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="My Account" />
  <jsp:param name="selectedItem" value="account" />
</jsp:include>

<h1>Your account</h1>
<h3>Company name</h3>
<c:out value="<%= member.getCompanyName() %>" />



<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
