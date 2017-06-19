<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.gs1us.sgl.webapp.standalone.UserController"%>
<%@page import="org.gs1us.sgl.serviceterms.TermsOfService"%>
<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String errorMessage = (String)request.getAttribute("errorMessage");
	TermsOfService tos = (TermsOfService)request.getAttribute("termsOfService");
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Login" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<c:if test="<%= errorMessage != null %>">
  <h1></h1>
  <div class="alert alert-danger">
     <%= errorMessage %>
  </div>
</c:if>

<h1>Please log in</h1>

<div class="row">
<div class="col-md-4">


<form role="form" method="post" action="<%= MvcUriComponentsBuilder.fromMethodName(LoginController.class, "login", (Object)null).toUriString() %>">
  <div class="form-group">
    <label>Email Address</label>
    <input name="j_username" type="text" class="form-control" placeholder="Text in field">
  </div>
  <div class="form-group">
    <label>Password</label>
    <input name="j_password" type="password" class="form-control" placeholder="Text in field">
  </div>
  <c:if test="<%= tos != null %>">
    <p>By logging in you agree to the <a href="<%= tos.getUrl() %>" target="_blank"><%= WebappUtil.longProductHtml() %> Terms of Service</a></p>
  </c:if>
  <div class="form-group">
    <button type="submit" class="btn-primary btn-margin btn-lg">Log in</button>
  </div>
  <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
</form>
  <p><a href="<%= MvcUriComponentsBuilder.fromMethodName(UserController.class, "forgotPasswordGet", (Object)null).toUriString() %>">Forgot your password?</a></p>
  <p>No account? Contact your <%= WebappUtil.productOperator() %> customer service representative to find out how to sign up.<p>
</div>
</div>
<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
