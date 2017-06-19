<%@page import="org.gs1us.sgl.memberservice.standalone.StandaloneUser"%>
<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
	StandaloneUser user = (StandaloneUser)request.getAttribute("user");
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Change Password" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<h1>Change password for <c:out value="<%= user.getUsername() %>" /></h1>

<div class="row">
<div class="col-md-6">

<p>
To reset the password for user <c:out value="<%= user.getUsername() %>" />, click "Send". 
An e-mail will be sent to <c:out value="<%= user.getUsername() %>" /> 
with instructions to reset the password. 
</p>

<form method="post">
    <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
	<button type="submit" class="btn-primary btn-margin ">Send</button>
    <button type="button" class="btn-secondary btn-margin ">Cancel</button>
</form>

</div>
</div>
<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
