
<%@page import="org.gs1us.sgg.gbservice.api.ImportStatus"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>
<%@page import="org.gs1us.sgl.webapp.ImportController"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.gs1us.sgl.memberservice.User"%>
<%@page import="org.gs1us.sgg.gbservice.api.Import"%>
<%@page import="java.util.Collection"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%
    Collection<? extends Import> imports = (Collection<? extends Import>)request.getAttribute("imports");
	String importUploadLink = MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importUploadGet", null, null).toUriString();

    User user = (User) ((Authentication) request.getUserPrincipal()).getPrincipal();
    String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Imports" />
  <jsp:param name="selectedItem" value="imports" />
</jsp:include>

<div class="row">
	<div class="col-md-12">
		<h1>Imported files</h1>
		<c:choose>
			<c:when test='<%= imports.size() == 0 %>'>
						<p>You do not have any imported files.  To import a new file of product data, click the button below.</p>
						<p>
							<a class="btn-large btn-primary btn-margin" href="<%=importUploadLink%>" role="button"><span class="icon-upload"></span> Import a file of product data</a>
						</p>
			</c:when>
			<c:otherwise>
				<p>
                    <a class="btn-large btn-primary btn-margin" href="<%= importUploadLink %>" role="button"><span class="icon-upload"></span> Import a file of product data</a>
				</p>
				
				<table class="table table-striped">
					<thead>
						<tr>
							<th>File</th>
							<th>Type</th>
							<th>Uploaded</th>
							<th>Confirmed</th>
							<th>Next Step</th>
							<th>Other Actions</th>
						</tr>
					</thead>
					<tbody>
						<% for (Import importRecord : imports) { 
						 	 String settingsLink = MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importSettingsGet", null, null, importRecord.getId()).toUriString();
						 	 String confirmLink = MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importConfirmGet", null, null, importRecord.getId()).toUriString();
						 	 String deleteLink = MvcUriComponentsBuilder.fromMethodName(ImportController.class, "importDeleteGet", null, null, importRecord.getId()).toUriString();
							 ImportStatus status = importRecord.getStatus();
					 	 %>
							<tr>
								<td><c:out value='<%= importRecord.getFilename() %>' /></td>
								<td>
								<%= "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(importRecord.getFormat()) ? "Excel" : "CSV" %>
								</td>
								<td><c:out value="<%= UserInputUtil.dateToString(importRecord.getUploadDate(), timeZoneId) %>"/></td>
								<td>
									<c:if test="<%= importRecord.getConfirmedDate() != null %>">
										<c:out value="<%= UserInputUtil.dateToString(importRecord.getConfirmedDate(), timeZoneId) %>"/>
									</c:if>
								</td>
								<td>
									<c:choose>
										<c:when test="<%= status == ImportStatus.UPLOADED %>">	
											<a href="<%= settingsLink %>" data-toggle="tooltip" data-placement="top" title="Review and accept column mappings"><span class="icon-cog large" ></span></a>
										</c:when>
										<c:when test="<%= status == ImportStatus.VALIDATED %>">
											<a href="<%= confirmLink %>" data-toggle="tooltip" data-placement="top" title="Review file contents and finish importing"><span class="icon-check_file" ></span></a>
										</c:when>
										<c:when test="<%= status == ImportStatus.PROCESSED %>">
											<span class="icon-check color-orange" data-toggle="tooltip" data-placement="top" title="File has been processed; see Other Actions for things you can do"></span>
										</c:when>
									</c:choose>
								</td>
								<td>
								  <c:if test="<%= status == ImportStatus.VALIDATED  %>">
								  <a href="<%= settingsLink %>" title="Change settings for this file"><span class="icon-cog"></span></a>
								  </c:if>
								  <c:if test="<%= status == ImportStatus.PROCESSED  %>">
								  <a href="<%= settingsLink %>" title="Review settings previously used to import this file"><span class="icon-cog"></span></a>
								  </c:if>
								  <c:if test="<%= status == ImportStatus.PROCESSED  %>">
								  <a href="<%= confirmLink %>" title="Confirm and process this file"><span class="icon-check_file"></span></a>
								  </c:if>
								  <a href="<%= deleteLink %>" title="Delete this file"><span class="icon-trashcan"></span></a>
								</td>
							</tr>
						<% } %>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
