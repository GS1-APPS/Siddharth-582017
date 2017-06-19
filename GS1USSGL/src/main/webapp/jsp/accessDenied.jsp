<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Access Denied" />
  <jsp:param name="selectedItem" value="none" />
</jsp:include>

<% if (request.getUserPrincipal() != null) { %>

<h3>Access Denied</h3>

<p>
Sorry, you are not allowed to access this page.
</p>

<% } else { %>

<h3>Please log in</h3>

<p>
Sorry, your previous session expired. Please <a href="/ui/login">log in</a> again.
</p>

<% } %>

<jsp:include page="/jsp/includes/footer.jsp" flush="true" />