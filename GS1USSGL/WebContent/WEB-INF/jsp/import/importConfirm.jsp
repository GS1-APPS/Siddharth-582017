<%@page import="org.gs1us.sgg.gbservice.api.OrderLineItem"%>
<%@page import="org.gs1us.sgg.gbservice.api.Amount"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@page import="java.util.Map"%>
<%@page import="org.gs1us.sgg.gbservice.api.ProductValidationError"%>
<%@page import="org.gs1us.sgg.gbservice.api.ProductStatus"%>
<%@page import="org.gs1us.sgg.gbservice.api.ProductState"%>
<%@page import="org.gs1us.sgg.gbservice.api.ImportValidationProduct"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.gs1us.sgl.memberservice.User"%>
<%@page import="org.gs1us.sgg.gbservice.api.ImportPrevalidationColumnStatus"%>
<%@page import="java.util.List"%>
<%@page import="org.gs1us.sgg.gbservice.api.ImportPrevalidationColumn"%>
<%@page import="org.gs1us.sgg.gbservice.api.ImportPrevalidationSegment"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>
<%@page import="org.gs1us.sgg.gbservice.api.Import"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<%
	Import importRecord = (Import)request.getAttribute("import");
	Map<String,AttributeDesc> attrDescMap = (Map<String,AttributeDesc>)request.getAttribute("attrDescMap");
	String actionUrl = (String)request.getAttribute("actionUrl");
	String settingsUrl = (String)request.getAttribute("settingsUrl");
	String deleteUrl = (String)request.getAttribute("deleteUrl");
	String laterUrl = (String)request.getAttribute("laterUrl");
	String dataAccuracyError = (String)request.getAttribute("dataAccuracyError");
	String purchaseError = (String)request.getAttribute("purchaseError");
    User user = (User) ((Authentication) request.getUserPrincipal()).getPrincipal();
    String timeZoneId = user.getTimezone();
    
    int okCount = 0;
    int purchaseCount = 0;
    int errorCount = 0;
    int noGtinCount = 0;
    Amount totalPurchase = Amount.ZERO;
    for (ImportValidationProduct vp : importRecord.getValidation().getValidationProducts())
    {
        if (vp == null || vp.getGtin() == null)
            noGtinCount++;
        else
        {
            if (vp.getStatus().getState() == ProductState.INVALID)
                errorCount++;
            else
            {
                okCount++;
                if (vp.getStatus().getQuotation() != null)
                {
                    purchaseCount++;
                    for (OrderLineItem lineItem : vp.getStatus().getQuotation().getLineItems())
                    {
                        totalPurchase = totalPurchase.add(lineItem.getTotal());
                    }
                }
            }
        }
    }
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Import product data" />
  <jsp:param name="selectedItem" value="products" />
</jsp:include>


<h1>Confirm import</h1>
<table class="table table-striped">
<tbody>
<tr><td class="strong">File</td><td> <c:out value="<%= importRecord.getFilename() %>" /></td></tr>
<tr><td class="strong">Uploaded</td><td><%= UserInputUtil.dateToString(importRecord.getUploadDate(), timeZoneId) %></td></tr>
<c:if test="<%= importRecord.getPrevalidation().getSegments().size() > 1 %>">
	<tr>
		<td class="strong">Sheets selected for import</td>
		<td>
		<% for (ImportPrevalidationSegment segment : importRecord.getPrevalidation().getSegments()) { %>
			<c:if test="<%= segment.getSettings().isEnabled() %>">
				<div><c:out value="<%= segment.getName() %>"/></div>
			</c:if>
		<% } %>
		</td>
	</tr>
</c:if>
<tr><td class="strong">Products ready to import</td><td><%= okCount %></td></tr>
<c:if test="<%= purchaseCount > 0 %>">
  <tr><td class="strong">Products requiring a purchase</td><td><%= purchaseCount %></td></tr>
</c:if>
<c:if test="<%= errorCount > 0 %>">
  <tr><td class="strong">Products with errors</td><td><%= errorCount %></td></tr>
</c:if>
<c:if test="<%= noGtinCount > 0 %>">
  <tr><td class="strong">File lines missing a GTIN</td><td><%= noGtinCount %></td></tr>
</c:if>
</tbody>
</table>

<c:if test="<%= errorCount > 0 %>">
	<h2>Validation errors</h2>
	<table class="table">
	<thead>
	<tr>
	<th>GTIN</th>
	<th>Field</th>
	<th>Error</th>
	</tr>
	</thead>
	<tbody>
	<%  for (ImportValidationProduct vp : importRecord.getValidation().getValidationProducts()) { %>
		<c:if test="<%= vp != null && vp.getStatus().getState() == ProductState.INVALID %>">
			<% boolean first = true; 
			   for (ProductValidationError pve : vp.getStatus().getValidationErrors()) { 
			       String path = pve.getPath();
			       AttributeDesc attrDesc = attrDescMap.get(path);
			       // Hack to avoid displaying ack error when there is some other error
			       //if (attrDesc == null || !attrDesc.getName().equals("dmChargesAck")) {
			%>
			<tr>
				<c:if test="<%= first %>">
					<td rowspan="<%= vp.getStatus().getValidationErrors().size() %>"><%= vp.getGtin() %></td>
				</c:if>
				<td><c:out value="<%= attrDesc == null ? path : attrDesc.getTitle() %>"/></td>
				<td><c:out value="<%= pve.getErrorMessage() %>"/></td>
			</tr>
			<% first = false; } /*}*/ %>
		</c:if>
	<% } %>
	</tbody>
	</table>
</c:if>

<form action="<%= actionUrl %>" method="post" role="form">

<c:choose>
	<c:when test="<%= okCount == 0 %>">
		<p>There is no data <%= errorCount > 0 ? "(without errors)" : "" %> to import.</p>
	</c:when>
	<c:otherwise>
		<c:choose>
		<c:when test="<%= purchaseCount > 0 && !totalPurchase.isZero() %>">
	  			<h3>Purchase acknowledgement</h3>
	   		<div class='<%= (purchaseError != null ? "form-group has-error" : "form-group") %>'>
	      		<div>
	        		<input type="checkbox" name="purchaseAck" /> I acknowledge that importing these products requires a purchase of <%= totalPurchase.toString() %>.
	      		</div>
	      		<c:if test='<%= purchaseError != null  %>'>
	      	 		<div class="form-control-feedback alert-danger"><span class="icon-warning_sign"></span>&nbsp;<c:out value='<%= purchaseError %>'/></div>
	      		</c:if>
	   		</div>
			
		</c:when>
		<c:otherwise>
			<input type="hidden" value="checked" name="purchaseAck" />
		</c:otherwise>
		</c:choose>
  		<h3>Data accuracy (required)</h3>
   		<div class='<%= (dataAccuracyError != null ? "form-group has-error" : "form-group") %>'>
      		<div>
        		<input type="checkbox" name="dataAccuracyAck" /> I certify that I am duly authorized on behalf of the brand owner to provide the data above and represent that all data provided is accurate <%= errorCount > 0 ? "(excluding data containing validation errors as reported above)" : "" %> .
      		</div>
      		<c:if test='<%= dataAccuracyError != null  %>'>
      	 		<div class="form-control-feedback alert-danger"><span class="icon-warning_sign"></span>&nbsp;<c:out value='<%= dataAccuracyError %>'/></div>
      		</c:if>
   		</div>
		<p>Proceed to import the data?
			<c:if test="<%= errorCount > 0 %>">
  				Only products that have no validation errors will be added / updated.
			</c:if>
		</p>
	</c:otherwise>
</c:choose>
  <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
  <div class="form-group">
  	<c:choose>
  		<c:when test="<%= okCount == 0 %>">
     		<button class="btn-primary btn-margin bg-orange-disabled" disabled="disabled" type="submit">Confirm</button>
   		</c:when>
   		<c:otherwise>
     		<button class="btn-primary btn-margin" type="submit">Confirm</button>
   		</c:otherwise>
	</c:choose>
    <button class="btn-secondary btn-margin" type="button" onclick='window.location.href="<%= settingsUrl %>"'>Change column mappings</button>
    <button class="btn-secondary btn-margin" type="button" onclick='window.location.href="<%= laterUrl %>"'>Finish later</button>
    <button class="btn-secondary btn-margin" type="button" onclick='window.location.href="<%= deleteUrl %>"'>Discard this file</button>

  </div>
</form>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
