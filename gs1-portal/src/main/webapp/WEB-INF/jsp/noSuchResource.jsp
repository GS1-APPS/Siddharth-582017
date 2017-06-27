<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="No Such Resource" />
  <jsp:param name="selectedItem" value="none" />
</jsp:include>

<h1>No such resource</h1>

<p>
The resource you tried to access does not exist.
</p>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />