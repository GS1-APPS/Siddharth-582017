<%@page import="org.gs1us.sgl.memberservice.standalone.StandaloneUser"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String errorMessage = (String)request.getAttribute("errorMessage");
	StandaloneUser user = (StandaloneUser)request.getAttribute("user");
%>

<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Login" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<c:if test="<%= errorMessage != null %>">
  <h3></h3>
  <div class="alert alert-danger">
     <%= errorMessage %>
  </div>
</c:if>

<h1>Reset your password</h1>

<div class="row">
<div class="col-md-4">


<form role="form" method="post" >
  <div class="form-group">
    <label>New password</label>
    <input name="pw1" type="password" class="form-control" />
  </div>
  <div class="form-group">
    <label>Re-enter new password</label>
    <input name="pw2" type="password" class="form-control" />
  </div>
  <div class="form-group">
    <button type="submit" class="btn-primary btn-margin btn-lg">Reset</button>
  </div>
  <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
</form>
</div>
</div>
<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
