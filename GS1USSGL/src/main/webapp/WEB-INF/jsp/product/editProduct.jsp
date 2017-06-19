<%@page import="org.gs1us.sgg.gbservice.api.AttributeSet"%>
<%@page import="org.gs1us.sgg.gbservice.api.Action"%>
<%@page import="org.gs1us.sgl.webapp.ProductController"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeType"%>
<%@page import="org.gs1us.sgg.gbservice.api.ProductValidationError"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Map"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@page import="org.gs1us.sgg.gbservice.api.AppDesc"%>
<%@page import="org.gs1us.sgg.gbservice.api.AppSubscription"%>
<%@page import="java.util.List"%>
<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%
	Collection<? extends AppSubscription> subs = (Collection<? extends AppSubscription>)request.getAttribute("subs");
	//AttributeSet attributes = (AttributeSet)request.getAttribute("attributes");
	Map<String,String[]> parameters = (Map<String,String[]>)request.getAttribute("parameters");
	boolean editGtin = (Boolean)request.getAttribute("editGtin");
	Action action = (Action)request.getAttribute("action");
	List<String> previouslySelectedApps = (List<String>)request.getAttribute("previouslySelectedApps");
	String gtin = (String)request.getAttribute("gtin");
	String heading = (String)request.getAttribute("heading");
	String actionUrl = (String)request.getAttribute("actionUrl");
	String submitLabel = (String)request.getAttribute("submitLabel");
	String cancelUrl = (String)request.getAttribute("cancelUrl");
	Map<String,List<String>> validationErrors = (Map<String,List<String>>)request.getAttribute("validationErrors");
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Edit Product" />
  <jsp:param name="selectedItem" value="products" />
</jsp:include>

<c:if test="<%=validationErrors != null && validationErrors.size() > 0 %>">
  <section>
    <div class="alert alert-danger">Please correct the errors below and submit again.</div>
  </section>
</c:if>

<h1><c:out value="<%=heading%>" /></h1>

<div class="row">
<div class="col-md-6">

<form method="post" action="<%=actionUrl%>">
	<c:if test="<%=editGtin%>">
		<sgl:formControlText validationErrors="<%= validationErrors %>" label="GTIN (required)" name="gtin" value="<%= gtin %>"></sgl:formControlText>
  	</c:if>
  <%
      for (AppSubscription sub : subs) {
      AppDesc appDesc = sub.getAppDesc();
      String appName = appDesc.getName();
      AttributeDesc selectionAttributeDesc = appDesc.getProductModuleDesc().getSelectionAttribute();
      boolean isSelectable = selectionAttributeDesc != null;
      boolean isSelected = !isSelectable || (parameters != null && parameters.get(selectionAttributeDesc.getName()) != null);
      String collapseClass = isSelected ? "collapse in" : "collapse";
      String collapseIdRef = "#" + appName;
      boolean previouslySelected = previouslySelectedApps != null && previouslySelectedApps.contains(appName);
      Action appAction = (action == Action.UPDATE && !previouslySelected ? Action.CREATE : action);
  %>
    <c:choose>
    	<c:when test="<%=isSelectable%>">
    		<c:choose>
    			<c:when test='<%=isSelected%>'>
  					<h3 class="color-orange"><input type="checkbox" name="<%=selectionAttributeDesc.getName()%>" checked="checked" data-toggle="collapse" data-target="<%=collapseIdRef%>"  />&nbsp;&nbsp;Enable <c:out value="<%=appDesc.getTitle()%>"/> for this product&nbsp;<c:if test="<%= appDesc.getDescription() != null %>"><a href="#" data-toggle="tooltip" data-placement="top" title="<%= appDesc.getDescription() %>"><span class="icon-info_sign"></span></a></c:if></h3>
 				</c:when>
 				<c:otherwise>
  					<h3 class="color-orange"><input type="checkbox" name="<%=selectionAttributeDesc.getName()%>"   data-toggle="collapse" data-target="<%=collapseIdRef%>" />&nbsp;&nbsp;Enable <c:out value="<%=appDesc.getTitle()%>"/> for this product&nbsp;<c:if test="<%= appDesc.getDescription() != null %>"><a href="#" data-toggle="tooltip" data-placement="top" title="<%= appDesc.getDescription() %>"><span class="icon-info_sign"></span></a></c:if></h3>
 				</c:otherwise>
			</c:choose>
  		</c:when>

 	</c:choose>
 	<div id="<%=appName%>" class="<%=collapseClass%>"> 	
 	<sgl:attributeGroupFormControls 
		validationErrors="<%= validationErrors %>" 
		parameters="<%= parameters %>" 
		appAction="<%= appAction %>" 
		moduleDesc="<%= appDesc.getProductModuleDesc() %>" />
    </div>
  <% } %>
  
  <% {
      List<String> errors = validationErrors.get("dataAccuracyAck");
      boolean hasError = errors != null;
  %>
  <h3 class="color-orange">Data accuracy (required)</h3>
    <div class='<%= (hasError ? "form-group has-error" : "form-group") %>'>
      <div>
        <input type="checkbox" name="dataAccuracyAck" /> I certify that I am duly authorized on behalf of the brand owner to provide the data above.
      </div>
      <c:if test='<%= hasError  %>'>
      	 <div class="form-control-feedback alert-danger"><span class="icon-warning_sign"></span>&nbsp;<c:out value='<%= errors.get(0) %>'/></div>
      </c:if>
    </div>
   <% } %>
  
  <button type="submit" class="btn-primary btn-margin "><c:out value="<%= submitLabel %>" /></button>
  <button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelUrl %>"'>Cancel</button>
  <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
</form>

</div>
</div>
<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
