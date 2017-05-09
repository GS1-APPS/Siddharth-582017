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

<%
	Collection<? extends SalesOrder> uninvoiced = (Collection<? extends SalesOrder>)request.getAttribute("uninvoiced");
	Collection<? extends Invoice> invoices = (Collection<? extends Invoice>)request.getAttribute("invoices");
	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Orders" />
  <jsp:param name="selectedItem" value="account" />
</jsp:include>

<c:choose>
	<c:when test='<%= uninvoiced.size() == 0 && invoices.size() == 0 %>'>
		<div class="page-header"></div>
	
		<div class="row">
			<div class="col-md-12">
				<p>You have not yet placed any orders.</p>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-md-12">
				<c:if test="<%= uninvoiced.size() > 0 %>">
					<h1>Orders not yet invoiced</h1>
					<table class="table table-striped">
						<thead>
							<tr>
								<th>Date</th>
								<th>Summary</th>
								<th class="text-right">Amount</th>
							</tr>
						</thead>
						<tbody>
							<% for (SalesOrder order : uninvoiced) { 
							%>
								<tr>
									<td><%= UserInputUtil.dateToString(order.getDate(), timeZoneId) %></td>
									<td><c:out value='<%= order.getSummary() %>'/></td>
									<td class="text-right"><%= order.getTotal() != null ? String.format("%.2f %s", order.getTotal().getValue(), order.getTotal().getCurrency()) : "[]" %></td>
								</tr>
							<% } %>
						</tbody>
					</table>
				</c:if>
				<c:if test="<%= invoices.size() > 0 %>">
					<h1>Invoices</h1>
					<table class="table table-striped">
						<thead>
							<tr>
								<th>Invoice #</th>
								<th>Date</th>
								<th>Summary</th>
								<th>Status</th>
								<th class="text-right">Amount</th>
							</tr>
						</thead>
						<tbody>
							<% for (Invoice invoice : invoices) { 
							%>
								<tr>
									<td><c:out value='<%= invoice.getInvoiceId() %>' /></td>
									<td><%= UserInputUtil.dateOnlyToString(invoice.getDate(), timeZoneId) %></td>
									<td><c:out value='<%= invoice.getSummary() %>'/></td>
									<td>
										<c:choose>
											<c:when test='<%= invoice.getOrderStatus() == OrderStatus.INVOICED %>'>
												<a href="#" data-toggle="tooltip" data-placement="top" title="An invoice has been issued. You will be billed shortly.">Invoiced</a>
											</c:when>
											<c:when test='<%= invoice.getOrderStatus() == OrderStatus.BILLED || invoice.getOrderStatus() == OrderStatus.PAYMENT_COMMITTED %>'>
												<a href="#" data-toggle="tooltip" data-placement="top" title="A bill has been sent to you. We are awaiting your payment.">Invoiced</a>
											</c:when>
											<c:when test='<%= invoice.getOrderStatus() == OrderStatus.PAID || invoice.getOrderStatus() == OrderStatus.SETTLED %>'>
												<a href="#" data-toggle="tooltip" data-placement="top" title="Your payment has been received. Your purchase is complete.">Invoiced</a>
											</c:when>
										</c:choose>
									</td>
									<td class="text-right"><%= invoice.getTotal() != null ? String.format("%.2f %s", invoice.getTotal().getValue(), invoice.getTotal().getCurrency()) : "[]" %></td>
								</tr>
							<% } %>
						</tbody>
					</table>
				</c:if>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
