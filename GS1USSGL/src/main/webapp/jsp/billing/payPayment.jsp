<%@page import="org.gs1us.sgg.gbservice.api.Payment"%>
<%@page import="org.gs1us.sgl.webapp.BillingController"%>
<%@page import="org.gs1us.sgl.memberservice.Member"%>
<%@page import="org.gs1us.sgg.gbservice.api.Invoice"%>
<%@page import="org.gs1us.sgg.gbservice.api.Amount"%>
<%@page import="java.util.Collection"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.gs1us.sgl.memberservice.User"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>

<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
    Payment payment = (Payment)request.getAttribute("payment");
	String paymentId = payment.getPaymentId() ;
    
    String actionUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "paidPaymentPost", null, null, paymentId, null).toUriString();
    String cancelUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingUnpaid", null, null).toUriString();

	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value='<%= "Payment " + paymentId  + " paid" %>'/>
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>

		<div class="row">
			<div class="col-md-12">
				<h1>Record funds received for payment <c:out value="<%= paymentId %>"/> <c:if test='<%= paymentId != null %>'>(reference <c:out value="<%= paymentId %>"/>)</c:if></h1>
			</div>
			<div class="col-md-4">
				<form method="post" action="<%= actionUrl %>">
					<div class="form-group">
	    				<label>Reference number</label>
				    	<input type="text" class="form-control" name="paidReference"/>
					</div>
					<button type="submit" class="btn-primary btn-margin ">Paid</button>
  					<button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelUrl %>"'>Cancel</button>
  					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				</form>
			</div>
		</div>


<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
