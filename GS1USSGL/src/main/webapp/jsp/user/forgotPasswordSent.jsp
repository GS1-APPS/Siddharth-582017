<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.gs1us.sgl.memberservice.standalone.StandaloneUser"%>
<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Forgot Password Sent" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<h1>Your password reset e-mail is on the way</h1>

<div class="row">
<div class="col-md-6">

<p>
An e-mail with instructions to reset your password has been sent to the e-mail address you supplied (assuming the e-mail address you entered was previously registered as a <%= WebappUtil.longProductHtml() %> account).
After you reset your password, you may log in and use the <%= WebappUtil.shortProductHtml() %>.
</p>


</div>
</div>
<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
