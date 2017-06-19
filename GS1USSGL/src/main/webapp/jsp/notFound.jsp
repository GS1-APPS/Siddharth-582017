<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Not Found" />
  <jsp:param name="selectedItem" value="none" />
</jsp:include>

<h1>No such page</h1>

<p>
Sorry, the page you tried to view does not exist.
</p>


<jsp:include page="/jsp/includes/footer.jsp" flush="true" />