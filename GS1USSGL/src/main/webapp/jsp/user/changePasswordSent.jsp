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
  <jsp:param name="pageTitle" value="Forgot Password Sent" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<h1>The password reset e-mail is on the way</h1>

<div class="row">
<div class="col-md-6">

<p>
An e-mail with instructions to reset the password for user <c:out value="<%= user.getUsername() %>" /> has been sent.
</p>


</div>
</div>
<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
