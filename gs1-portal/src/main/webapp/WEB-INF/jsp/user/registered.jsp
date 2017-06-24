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
	StandaloneUser user = (StandaloneUser)request.getAttribute("user");
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Register" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<h2>User <%= user.getUsername() %> is registered to use the <%= WebappUtil.longProductHtml() %></h2>

<div class="row">
<div class="col-md-6">

<p>
An e-mail has been sent to <%= user.getUsername() %> with instructions to set the password. After the user has set the password, the user may log in and use the <%= WebappUtil.longProductHtml() %>.
</p>

</div>
</div>
<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
