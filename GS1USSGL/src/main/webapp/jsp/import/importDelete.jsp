<%@page import="org.gs1us.sgl.webapp.ImportController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.gs1us.sgg.gbservice.api.ImportStatus"%>
<%@page import="org.gs1us.sgg.gbservice.api.Import"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<%
	Import importRecord = (Import)request.getAttribute("import");
	String actionUrl = (String)request.getAttribute("actionUrl");
	String showImportsUrl = MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importShowAllGet", null, null).toUriString();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Delete uploaded file" />
  <jsp:param name="selectedItem" value="products" />
</jsp:include>


<h1>Delete imported file &#x201c;<c:out value="<%= importRecord.getFilename() %>"/>&#x201d;</h1>
<p>
<c:choose>
  <c:when test="<%= importRecord.getStatus() == ImportStatus.PROCESSED %>">
  	File &#x201c;<c:out value="<%= importRecord.getFilename() %>"/>&#x201d; has been fully processed.
  	Deleting this file will free up space, but will <strong>not</strong> affect the product data that was loaded from this file.
  </c:when>
  <c:otherwise>
  	File &#x201c;<c:out value="<%= importRecord.getFilename() %>"/>&#x201d; has not been fully processed.
  	If you delete this file now, the data in the file will be discarded.
  </c:otherwise>
</c:choose>
</p>

<form action="<%= actionUrl %>" method="post" role="form">
  <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>

  <div class="form-group">
     <button class="btn-primary btn-margin" type="submit">Delete</button>
   <button class="btn-secondary btn-margin" type="button" onclick='window.location.href="<%= showImportsUrl %>"'>Cancel</button>
  </div>
</form>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
