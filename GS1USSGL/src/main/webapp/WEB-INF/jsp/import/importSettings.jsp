<%@page import="java.util.Collection"%>
<%@page import="java.util.Map"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeType"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.gs1us.sgl.memberservice.User"%>
<%@page import="org.gs1us.sgg.gbservice.api.ImportPrevalidationColumnStatus"%>
<%@page import="java.util.List"%>
<%@page import="org.gs1us.sgg.gbservice.api.ImportPrevalidationColumn"%>
<%@page import="org.gs1us.sgg.gbservice.api.ImportPrevalidationSegment"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>
<%@page import="org.gs1us.sgg.gbservice.api.Import"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>


<%
	Import importRecord = (Import)request.getAttribute("import");
	Map<String,AttributeDesc> attrDescMap = (Map<String,AttributeDesc>)request.getAttribute("attrDescMap");
	Collection<AttributeDesc> attrDescs = attrDescMap.values();
	String actionUrl = (String)request.getAttribute("actionUrl");
	String deleteUrl = (String)request.getAttribute("deleteUrl");
	String laterUrl = (String)request.getAttribute("laterUrl");
    User user = (User) ((Authentication) request.getUserPrincipal()).getPrincipal();
    String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Import product data" />
  <jsp:param name="selectedItem" value="products" />
</jsp:include>


<h1>Review column mappings</h1>
<p><strong>File</strong>: <c:out value="<%= importRecord.getFilename() %>" /></p>
<p><strong>Uploaded</strong>: <%= UserInputUtil.dateToString(importRecord.getUploadDate(), timeZoneId) %>

<form action="<%= actionUrl %>" method="post" role="form">

<c:if test="<%= importRecord.getPrevalidation().getSegments().size() > 1 %>">
	<p>This file has <%=  importRecord.getPrevalidation().getSegments().size() %> sheets.</p>
</c:if>


<% int segmentNumber = 0; boolean fileHasData = false; for (ImportPrevalidationSegment segment : importRecord.getPrevalidation().getSegments()) { 
    int blankRowCount = segment.getRowCount() - segment.getNonblankRowCount();
    fileHasData |= (segment.getNonblankRowCount() > 0);
%>
<c:if test="<%= importRecord.getPrevalidation().getSegments().size() > 1 %>">
	<h3>
	   Sheet <%= segmentNumber+1 %>: &#x201c;<c:out value='<%= segment.getName() == null ? "[unnamed sheet]" : segment.getName() %>' />&#x201d;
	</h3>
</c:if>

<c:choose>
	<c:when test="<%= segment.getNonblankRowCount() == 0 %>">
		<div class="alert alert-warning">This <%= importRecord.getPrevalidation().getSegments().size() > 1 ? "sheet" : "file" %> contains no data.</div>
	</c:when>
	<c:otherwise>

<c:if test="<%= segment.getSegmentErrors() != null && segment.getSegmentErrors().size() > 0 %>">
	<div class="alert alert-danger">
		<% for (String segmentError : segment.getSegmentErrors()) { %>
			<div><c:out value="<%= segmentError %>" /></div>
		<% } %>
	</div>
</c:if>
	
<c:if test="<%= importRecord.getPrevalidation().getSegments().size() > 1 %>">
	<p>
	<c:choose>
		<c:when test="<%= segment.getSettings().isEnabled() %>">
			<input type="checkbox" name='<%= "mapping-" + segmentNumber %>' checked="checked" /> 
		</c:when>
		<c:otherwise>
			<input type="checkbox" name='<%= "mapping-" + segmentNumber %>'  /> 
		</c:otherwise>
	</c:choose>
	Include data from this sheet</p>
</c:if>
<p><%= segment.getNonblankRowCount() %> data <%=  segment.getNonblankRowCount() == 1 ? "row" : "rows" %><c:if test="<%= blankRowCount > 0 %>">, <%=  blankRowCount %> blank <%=  blankRowCount == 1 ? "row" : "rows" %>
</c:if>
</p>
<table class="table">
  <thead>
  	<tr>
    <th>Your column name</th>
    <th>Maps to</th>
    <th>Comment</th>
    </tr>
  </thead>
  <tbody>
     <% int i = 0; for (ImportPrevalidationColumn column : segment.getColumns()) { 
         String mapping = segment.getSettings().getColumnMappings().get(i);
     %>
     	<tr>
     		<td><c:out value='<%= column.getName() == null ? "[unnamed column]" : column.getName() %>'/></td>
     		<td>
     			<select class="form-control" name='<%= String.format("mapping-%d-%d", segmentNumber, i) %>'>
     				<option value="">Choose...</option>
     				<option value="@ignore">[Ignore this column]</option>
     				    <c:choose>
     					<c:when test='<%= "gtin".equals(mapping) %>'>
     						<option selected="selected" value="gtin">GTIN</option>
   						</c:when>
     					<c:otherwise>
     						<option value="gtin">GTIN</option>
   						</c:otherwise>
   						</c:choose>
     				<% for (AttributeDesc attrDesc : attrDescs) { 
    				    String title = (attrDesc.getType() == AttributeType.AFFIRMATION || attrDesc.getType() == AttributeType.BOOLEAN) ? attrDesc.getName() : attrDesc.getTitle();
     				%>
      					<sgl:option test="<%= attrDesc.getTitle().equals(column.getName()) %>" value="<%= attrDesc.getName() %>" display="<%= title %>"/>
						<c:if test="<%= attrDesc.getType() == AttributeType.MEASUREMENT %>">
      						<sgl:option test='<%= (attrDesc.getName() + "_uom").equals(mapping) %>' value='<%= attrDesc.getName() + "_uom" %>' display='<%= title + " (UOM)" %>'/>
						</c:if>
   					<% } %>
     			</select>
     		</td>
     		<td>
     			<c:if test="<%= column.getStatus() == ImportPrevalidationColumnStatus.EMPTY %>">Empty column</c:if>
     		</td>
   		</tr>
     <% i++; } %>
  </tbody>
</table>

	</c:otherwise>
</c:choose>

<% segmentNumber++;} %>

  <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
  <div class="form-group">
  	<c:choose>
  	<c:when test="<%= fileHasData %>">
     <button class="btn-primary btn-margin" type="submit">Validate</button>
    <button class="btn-secondary btn-margin" type="button" onclick='window.location.href="<%= laterUrl %>"'>Finish later</button>
    <button class="btn-secondary btn-margin" type="button" onclick='window.location.href="<%= deleteUrl %>"'>Discard this file</button>
	</c:when>
	<c:otherwise>
    <button class="btn-primary btn-margin" type="button" onclick='window.location.href="<%= deleteUrl %>"'>Discard this file</button>
	</c:otherwise>
	</c:choose>
  </div>
</form>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
