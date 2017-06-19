<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String errorMessage = (String)request.getAttribute("errorMessage");
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="No Such Resource" />
  <jsp:param name="selectedItem" value="none" />
</jsp:include>

<h1><%= WebappUtil.shortProductHtml() %> error</h1>

<p>
<c:out value="<%= errorMessage %>" />
</p>


<jsp:include page="/jsp/includes/footer.jsp" flush="true" />