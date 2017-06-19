<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="No Such Resource" />
  <jsp:param name="selectedItem" value="none" />
</jsp:include>

<h1><%= WebappUtil.shortProductHtml() %> error</h1>

<p>
An error has prevented the <%= WebappUtil.shortProductHtml() %> from fulfilling your request. Please try again later, or contact customer support.
</p>


<jsp:include page="/jsp/includes/footer.jsp" flush="true" />