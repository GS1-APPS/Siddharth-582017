<%@page import="org.gs1us.sgg.gbservice.api.OrderLineItem"%>
<%@page import="org.gs1us.sgg.gbservice.api.SalesOrder"%>
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
    Invoice invoice = (Invoice)request.getAttribute("invoice");
	String invoiceId = invoice.getInvoiceId() ;
	
	Collection<? extends SalesOrder> orders = (Collection<? extends SalesOrder>)request.getAttribute("orders");
    
    String actionUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billInvoicePost", null, null, invoiceId).toUriString();
    String cancelUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingInvoiced", null, null).toUriString();

	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value='<%= "Pay invoice " + invoiceId %>'/>
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>

		<div class="row">
			<div class="col-md-12">
				<h1>Bill the customer for invoice <c:out value="<%= invoiceId %>"/></h1>
			</div>
			<div class="col-md-12">
				<table class="table">
				<% for (SalesOrder order : orders) { %>
					<tr class="bg-light-gray">
						<td colspan="3">Order <%= order.getOrderId() %> placed <%= UserInputUtil.dateToString(order.getDate(), timeZoneId) %></td>
						<td class="text-right"><%= order.getTotal() %></td>
					</tr>
					<% for (OrderLineItem lineItem : order.getLineItems()) { %>
						<tr>
							<td class="text-right"><%= lineItem.getQuantity() %></td>
							<td><c:out value="<%= lineItem.getItemDescription() %>" /></td>
							<td class="text-right"><%= lineItem.getPrice() %></td>
							<td class="text-right"><%= lineItem.getTotal() %></td>
						</tr>
					<% } %>
				<% } %>
				<tr class="bg-light-gray">
					<td colspan="3">TOTAL</td>
					<td class="text-right"><%= invoice.getTotal() %></td>
				</tr>
				</table>
			</div>
			<div class="col-md-4">
				<form method="post" action="<%= actionUrl %>">
					<div class="form-group">
	    				<label>Billing reference (optional)</label>
				    	<input type="text" class="form-control" name="billingReference"/>
					</div>
					<button type="submit" class="btn-primary btn-margin ">Bill</button>
  					<button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelUrl %>"'>Cancel</button>
  					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				</form>
			</div>
		</div>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
