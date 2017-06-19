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
	String cancelLink = (String)request.getAttribute("cancelLink");
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Forgot Password" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<h1>Forgot your password?</h1>

<div class="row">
<div class="col-md-6">

<p>
To reset your password, enter the e-mail address for your <%= WebappUtil.longProductHtml() %> account below and click "Send". 
If this e-mail address was previously registered as an <%= WebappUtil.shortProductHtml() %> account, an e-mail will be sent to it 
with instructions to reset your password. After you reset your password, you may log in and use the <%= WebappUtil.shortProductHtml() %>.
</p>

<form method="post">
  <div class="form-group">
    <label>E-mail Address</label>
    <input name="email" type="text" class="form-control" />
  </div>
    <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
	<button type="submit" class="btn-primary btn-margin ">Send</button>
    <button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelLink %>"'>Cancel</button>
</form>

</div>
</div>
<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
