<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.gs1us.sgg.gbservice.api.InvoiceExtra"%>
<%@page import="org.gs1us.sgl.webapp.BillingController"%>
<%@page import="org.gs1us.sgl.memberservice.Member"%>
<%@page import="org.gs1us.sgl.webapp.ByMemberGrouper"%>
<%@page import="org.gs1us.sgg.gbservice.api.OrderStatus"%>
<%@page import="org.gs1us.sgg.gbservice.api.Invoice"%>
<%@page import="org.gs1us.sgg.gbservice.api.SalesOrder"%>
<%@page import="org.gs1us.sgg.gbservice.api.BillingTransactionType"%>
<%@page import="org.gs1us.sgg.gbservice.api.Amount"%>
<%@page import="org.gs1us.sgg.gbservice.api.BillingTransaction"%>
<%@page import="java.util.Collection"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.gs1us.sgl.memberservice.User"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>

<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%
	Invoice invoice = (Invoice)request.getAttribute("invoice");
	Collection<SalesOrder> orders = (Collection<SalesOrder>)request.getAttribute("orders");
	Member forMember = (Member)request.getAttribute("forMember");	
	String invoiceTitle = "Invoice #" + invoice.getInvoiceId();


	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="<%= invoiceTitle %>" />
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>


		<div class="row">
			<div class="col-md-12">
				<h1><%= invoiceTitle %></h1>
				<c:if test="<%= forMember != null %>">
					<sgl:memberNameAndAddressBlock member="<%= forMember %>"/>
				</c:if>
				
				<p><strong>Invoice date: </strong><%= UserInputUtil.dateOnlyToString(invoice.getDate(), timeZoneId) %></p>
				<p><strong>Status: </strong>
					<c:choose>
						<c:when test="<%= invoice.getOrderStatus() == OrderStatus.INVOICED %>">
							This invoice has not yet been billed to you.
						</c:when>
						<c:when test="<%= invoice.getOrderStatus() == OrderStatus.BILLED %>">
							This invoice has been billed to you, and <%= WebappUtil.productOperator() %> is awaiting the receipt of your payment.
						</c:when>
						<c:when test="<%= invoice.getOrderStatus() == OrderStatus.PAYMENT_COMMITTED %>">
							Your payment has been received, and <%= WebappUtil.productOperator() %> is waiting for the funds to clear.
						</c:when>
						<c:when test="<%= invoice.getOrderStatus() == OrderStatus.PAID || invoice.getOrderStatus() == OrderStatus.SETTLED %>">
							This invoice has been fully paid.
						</c:when>
					</c:choose>
					<table class="table">
						<thead>
						<tr>
							<th>Order ID</th>
							<th>Date</th>
							<th>Description</th>
							<th class="text-right">Amount</th>
						</tr>
						</thead>
						<tbody>
					<% Amount subTotal = Amount.ZERO;
						for (SalesOrder order : orders) {
						    subTotal = subTotal.add(order.getTotal());
					%>
							<tr>
								<td><%= order.getOrderId() %></td>
								<td><%= UserInputUtil.dateToString(order.getDate(), timeZoneId) %></td>
								<td><c:out value="<%= order.getSummary() %>" /></td>
								<td class="text-right"><%= order.getTotal() %></td>
							</tr>
					<% } %>
					<c:if test="<%= invoice.getExtras() != null && invoice.getExtras().size() > 0  %>">
					<tr class="bg-light-gray">
						<td colspan="3"><strong>Subtotal</strong></td>
						<td id="subtotal" class="text-right"><%= subTotal %></td>
					</tr>
					<% for (InvoiceExtra extra : invoice.getExtras())  { 
					    subTotal = subTotal.add(extra.getTotal());
					%>
							<tr>
								<td></td>
								<td colspan="2"><%= extra.getItemDescription() %></td>
								<td class="text-right"><%= extra.getTotal() %></td>
							</tr>
					<% } %>
					</c:if>
					<tr class="bg-light-gray">
						<td colspan="3"><strong>Total</strong></td>
						<td id="total" class="text-right"><%= subTotal %></td>
					</tr>
					
					</tbody>
					</table>

			</div>
		</div>




<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
