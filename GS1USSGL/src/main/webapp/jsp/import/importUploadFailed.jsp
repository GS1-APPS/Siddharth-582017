<%@page import="org.gs1us.sgl.webapp.ImportController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<%
	String reason = (String)request.getAttribute("reason");
	String importUploadUrl = MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importUploadGet", null, null).toUriString();
	String importUrl = MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importShowAllGet", null, null).toUriString();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Import upload failed" />
  <jsp:param name="selectedItem" value="products" />
</jsp:include>


<h1>Upload failed</h1>

<p><c:out value="<%= reason %>"/></p>

<p>
    <button class="btn-secondary btn-margin" type="button" onclick='window.location.href="<%= importUploadUrl %>"'>Try again</button>
    <button class="btn-secondary btn-margin" type="button" onclick='window.location.href="<%= importUrl %>"'>Cancel</button>
</p>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
