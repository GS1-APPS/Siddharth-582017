<%@page import="org.gs1us.sgl.webapp.ImportController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<%
	String importUrl = MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importShowAllGet", null, null).toUriString();

%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Upload product data" />
  <jsp:param name="selectedItem" value="products" />
</jsp:include>


<h1>Upload a file of product data</h1>
<p>Use the form below to upload a file of product data. The file should be an Excel spreadsheet with one or more sheets, or a CSV
file. Each sheet should have one row of column headings followed by one row for each product. After uploading,
you will be able to configure the mapping of columns and validate the data before it is finally accepted into
the portal.</p>

<form action="./upload?${_csrf.parameterName}=${_csrf.token}" method="post" enctype="multipart/form-data" role="form">
  <div class="form-group">
     <label>File</label>
     <input type="file" name="file" size="75"/>
  </div>
  <div class="form-group">
     <button class="btn-primary btn-margin" type="submit">Upload</button>
    <button class="btn-secondary btn-margin" type="button" onclick='window.location.href="<%= importUrl %>"'>Cancel</button>
  </div>
</form>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
