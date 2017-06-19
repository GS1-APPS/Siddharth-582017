<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String title = (String)request.getAttribute("title");
	String result = (String)request.getAttribute("result");
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="No Such Resource" />
  <jsp:param name="selectedItem" value="none" />
</jsp:include>

<h1><c:out value="<%= title %>" /></h1>

<div class="sgl-test">
<%= result %>
</div>


<jsp:include page="/jsp/includes/footer.jsp" flush="true" />