<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="org.gs1us.sgg.gbservice.api.Action"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeSet"%>
<%@page import="org.gs1us.sgg.gbservice.api.ProductValidationError"%>
<%@page import="java.util.Collection"%>
<%@page import="org.gs1us.sgg.gbservice.api.ModuleDesc"%>
<%@page import="org.gs1us.sgg.gbservice.api.AppDesc"%>
<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%
	String errorMessage = (String)request.getAttribute("errorMessage");

	String actionUrl = (String)request.getAttribute("actionUrl");
	String submitLabel = (String)request.getAttribute("submitLabel");

	String heading = (String)request.getAttribute("heading");
	
	String resetPasswordLink = (String)request.getAttribute("resetPasswordLink");
	String cancelLink = (String)request.getAttribute("cancelLink");
	
	String[] timezones = (String[])request.getAttribute("timezones");
	
	Collection<? extends AppDesc> appDescs = (Collection<? extends AppDesc>)request.getAttribute("appDescs");
	AppDesc.Scope scope = (AppDesc.Scope)request.getAttribute("scope");
	Map<String,List<String>> validationErrors = (Map<String,List<String>>)request.getAttribute("validationErrors");
	//AttributeSet attributes = (AttributeSet)request.getAttribute("attributes");
	Map<String,String[]> parameters = (Map<String,String[]>)request.getAttribute("parameters");
	Action action = (Action)request.getAttribute("action");
	

%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Edit Member" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<c:if test="<%= errorMessage != null %>">
  <section>
  <div class="alert alert-danger">
     <%= errorMessage %>
  </div>
  </section>
</c:if>
<c:if test="<%=validationErrors != null && validationErrors.size() > 0 %>">
  <section>
    <div class="alert alert-danger">Please correct the errors below and submit again.</div>
  </section>
</c:if>

<h1><c:out value="<%= heading %>" /></h1>

<div class="row">
<div class="col-md-6">

<c:if test="<%= resetPasswordLink != null %>">
  <p>Click <a href="<%= resetPasswordLink %>">here</a> to reset the password for this account.
</c:if>

<form:form commandName="memberCommand" role="form" action="<%= actionUrl %>">
  <form:errors path="*" cssClass="alert alert-danger" element="div" />
  <div class="form-group">
    <label>GLN</label>
    <form:input disabled="true" type="text" class="form-control" cssErrorClass="form-control alert-danger" path="gln" />
  </div>
  <div class="form-group">
    <label>Company Name</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="companyName" />
  </div>
  <div class="form-group">
    <label>Address 1</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="address1" />
  </div>
  <div class="form-group">
    <label>Address 2</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="address2" />
  </div>
  <div class="form-group">
    <label>City</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="city" />
  </div>
  <div class="form-group">
    <label>State</label>
    <form:select class="form-control" cssErrorClass="form-control alert-danger" path="state" >
    	<form:option value="-" label="Select" />
	<form:option value="AL" label="Alabama" />
	<form:option value="AK" label="Alaska" />
	<form:option value="AZ" label="Arizona" />
	<form:option value="AR" label="Arkansas" />
	<form:option value="CA" label="California" />
	<form:option value="CO" label="Colorado" />
	<form:option value="CT" label="Connecticut" />
	<form:option value="DC" label="District of Columbia" />
	<form:option value="DE" label="Delaware" />
	<form:option value="FL" label="Florida" />
	<form:option value="GA" label="Georgia" />
	<form:option value="HI" label="Hawaii" />
	<form:option value="ID" label="Idaho" />
	<form:option value="IL" label="Illinois" />
	<form:option value="IN" label="Indiana" />
	<form:option value="IA" label="Iowa" />
	<form:option value="KS" label="Kansas" />
	<form:option value="KY" label="Kentucky" />
	<form:option value="LA" label="Louisiana" />
	<form:option value="ME" label="Maine" />
	<form:option value="MD" label="Maryland" />
	<form:option value="MA" label="Massachusetts" />
	<form:option value="MI" label="Michigan" />
	<form:option value="MN" label="Minnesota" />
	<form:option value="MS" label="Mississippi" />
	<form:option value="MO" label="Missouri" />
	<form:option value="MT" label="Montana" />
	<form:option value="NE" label="Nebraska" />
	<form:option value="NV" label="Nevada" />
	<form:option value="NH" label="New Hampshire" />
	<form:option value="NJ" label="New Jersey" />
	<form:option value="NM" label="New Mexico" />
	<form:option value="NY" label="New York" />
	<form:option value="NC" label="North Carolina" />
	<form:option value="ND" label="North Dakota" />
	<form:option value="OH" label="Ohio" />
	<form:option value="OK" label="Oklahoma" />
	<form:option value="OR" label="Oregon" />
	<form:option value="PA" label="Pennsylvania" />
	<form:option value="RI" label="Rhode Island" />
	<form:option value="SC" label="South Carolina" />
	<form:option value="SD" label="South Dakota" />
	<form:option value="TN" label="Tennessee" />
	<form:option value="TX" label="Texas" />
	<form:option value="UT" label="Utah" />
	<form:option value="VT" label="Vermont" />
	<form:option value="VA" label="Virginia" />
	<form:option value="WA" label="Washington" />
	<form:option value="WV" label="West Virginia" />
	<form:option value="WI" label="Wisconsin" />
	<form:option value="WY" label="Wyoming" />
    </form:select>
  </div>
  <div class="form-group">
    <label>Postal Code</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="postalCode" />
  </div>
  <div class="form-group">
    <label>Member ID (from IMIS)</label>
    <form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="memberId" />
  </div>
  <div class="form-group">
    <label>GS1 Company Prefixes</label>
    <form:textarea rows="3" type="text" class="form-control" cssErrorClass="form-control alert-danger" path="gcps" />
  </div>
  <% for (AppDesc appDesc : appDescs) {
      ModuleDesc moduleDesc = appDesc.getModuleDesc(scope);
  %>
  <sgl:attributeGroupFormControls 
  	validationErrors="<%= validationErrors %>" 
  	parameters="<%= parameters %>" 
  	appAction="<%= action %>" 
  	moduleDesc="<%= moduleDesc %>" />
  <% } %>
  <button type="submit" class="btn-primary btn-margin "><c:out value="<%= submitLabel %>" /></button>
  <button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelLink %>"'>Cancel</button>

</form:form>

</div>
</div>
<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
