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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Reports" />
  <jsp:param name="selectedItem" value="reports" />
</jsp:include>

<%
	String count = (String) request.getAttribute("TotalCount");
	String olderCount = (String) request.getAttribute("OldDataCount");
%>

<h1>Admin Reports</h1>
<br/>
<strong>Current Stats of System</strong>

<h3># of GTIN records: <%=count%> </h3>
<h3># of GTIN records 60 days or older: <%=olderCount%> </h3>
<h3># of requests for data: "Still developing" </h3>
<h3># of requests for data where no product was found: "Still developing" </h3>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />