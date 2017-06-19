<%@page import="org.gs1us.sgl.webapp.BillingController"%>
<%@page import="org.gs1us.sgg.gbservice.api.Payment"%>
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

<%
	int uninvoicedCount = (Integer)request.getAttribute("uninvoicedCount");
    int invoicedCount = (Integer)request.getAttribute("invoicedCount");
	int billedCount = (Integer)request.getAttribute("billedCount");
	int unpaidCount = (Integer)request.getAttribute("unpaidCount");
	
    String ordersUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "invoiceAndBillGet", null, null).toUriString();
    String invoicedUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingInvoiced", null, null).toUriString();
    String billedUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingBilled", null, null).toUriString();
    String unpaidUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingUnpaid", null, null).toUriString();

	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Billing" />
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>


<div class="row">
	<div class="col-md-12">
		<h1>Billing action list</h1>
		<p>There <%= uninvoicedCount == 1 ? "is" : "are" %> <%= uninvoicedCount %> pending <%= uninvoicedCount == 1 ? "order" : "orders" %> that need to be invoiced.</p>
		<c:if test="<%= uninvoicedCount > 0 %>">
			<p>&gt;&nbsp;&nbsp;<a href="<%= ordersUrl %>">Review pending orders and create invoices</a></p>
		</c:if>
		
		<h5>&nbsp;</h5>
		<p>There <%= invoicedCount == 1 ? "is" : "are" %> <%= invoicedCount %> new <%= invoicedCount == 1 ? "invoice" : "invoices" %> that need to be billed to the customer.</p>
		<c:if test="<%= invoicedCount > 0 %>">
			<p>&gt;&nbsp;&nbsp;<a href="<%= invoicedUrl %>">Review unbilled invoices and send bill to customer</a></p>
		</c:if>
		
		<h5>&nbsp;</h5>
		<p>There <%= billedCount == 1 ? "is" : "are" %> <%= billedCount %> <%= billedCount == 1 ? "invoice" : "invoices" %> awaiting confirmation of payment from the customer.</p>
		<c:if test="<%= billedCount > 0 %>">
			<p>&gt;&nbsp;&nbsp;<a href="<%= billedUrl %>">Review billed invoices and note the commitment of a payment</a></p>
		</c:if>
		
		<h5>&nbsp;</h5>
		<p>There <%= unpaidCount == 1 ? "is" : "are" %> <%= unpaidCount %> committed <%= unpaidCount == 1 ? "payment" : "payments" %> whose funds are not yet received.</p>
		<c:if test="<%= unpaidCount > 0 %>">
			<p>&gt;&nbsp;&nbsp;<a href="<%= unpaidUrl %>">Review committed payment and note the receipt of funds</a></p>
		</c:if>
	</div>
</div>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
