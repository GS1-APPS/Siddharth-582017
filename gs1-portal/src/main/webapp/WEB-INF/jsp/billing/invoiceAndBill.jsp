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
	ByMemberGrouper<SalesOrder> uninvoicedGroups = (ByMemberGrouper<SalesOrder>)request.getAttribute("uninvoicedOrderGroups");

	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Pending Orders" />
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>

<div class="row">
	<div class="col-md-12">
		<h1>Invoice pending orders</h1>
		<c:choose>
			<c:when test='<%= uninvoicedGroups.size() == 0  %>'>
		
							<p>There are no uninvoiced orders.</p>
							<p>&gt;&nbsp;&nbsp;<a href="<%= MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingSummary", null, null).toUriString() %>">Go to billing action center</a></p>
		
			</c:when>
			<c:otherwise>
			<p>Select a member to create and bill an invoice for all of that member's pending orders.</p>
					<% for (ByMemberGrouper.Entry<SalesOrder> entry : uninvoicedGroups.getGroups()) { 
					    Amount memberTotal = Amount.ZERO;
					    for (SalesOrder order : entry.getElts())
					    {
					        memberTotal = memberTotal.add(order.getTotal());
					    }
						String actionUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "invoiceAndBillMemberGet", null, null, entry.getMember().getGln()).toUriString();
					%>
					<table class="table">
						<tr class="bg-light-gray">
						<td colspan="2" >
							<sgl:memberSpan member="<%= entry.getMember() %>" />
						</td>
						<td class="text-right"><%= memberTotal %></td>
						<td><a href="<%= actionUrl %>" title="Create an invoice for this member"><span class="icon-shopping_cart" /></a></td>
						</tr>
						<% for (SalesOrder order : entry.getElts()) { %>
							<tr>
								<td><%= UserInputUtil.dateToString(order.getDate(), timeZoneId) %></td>
								<td><c:out value="<%= order.getSummary() %>" /></td>
								<td class="text-right"><%= order.getTotal() %></td>
								<td></td>
							</tr>
						<% } %>
					</table>
					<% } %>

			</c:otherwise>
		</c:choose>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
