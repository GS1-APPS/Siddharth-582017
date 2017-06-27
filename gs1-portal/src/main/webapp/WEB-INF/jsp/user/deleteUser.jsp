<%@page import="org.gs1us.sgl.memberservice.standalone.StandaloneUser"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
	StandaloneUser user = (StandaloneUser)request.getAttribute("user");


%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Delete User" />
  <jsp:param name="selectedItem" value="user" />
</jsp:include>



<h1>Delete User <c:out value="<%= user.getUsername() %>" /></h1>



<form method="post">

  <button type="submit" class="btn-primary btn-margin ">Delete</button>
  <button type="button" class="btn-secondary btn-margin " onclick='window.location.href="/ui/user"'>Cancel</button>
  <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
</form>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
