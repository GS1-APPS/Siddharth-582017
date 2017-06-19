<%@page import="org.gs1us.sgl.memberservice.Member"%>
<%@page import="org.gs1us.sgl.webapp.AccountController"%>
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
	Collection<? extends SalesOrder> uninvoiced = (Collection<? extends SalesOrder>)request.getAttribute("uninvoiced");
	Collection<? extends BillingTransaction> xactions = (Collection<? extends BillingTransaction>)request.getAttribute("xactions");
	Member forMember = (Member)request.getAttribute("forMember");
	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Transactions" />
  <jsp:param name="selectedItem" value="account" />
</jsp:include>

<c:choose>
	<c:when test='<%= xactions.size() == 0 && uninvoiced.size() == 0 %>'>
		<div class="page-header"></div>
	
		<div class="row">
			<div class="col-md-12">
				<c:choose>
					<c:when test="<%= forMember == null %>">
						<p>You have not yet incurred any charges.</p>
					</c:when>
					<c:otherwise>
						<p>Member <c:out value="<%= forMember.getCompanyName() %>"/> has not yet incurred any charges.</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-md-12">
				<h1>Transaction history</h1>
				<c:if test="<%= forMember != null %>">
					<sgl:memberNameAndAddressBlock member="<%= forMember %>"/>
				</c:if>
				<c:if test="<%= !uninvoiced.isEmpty() %>">
					<p>Amounts shown for pending orders do not include sales tax. If sales tax is due, it will be applied when pending orders
					are consolidated into your monthly bill.</p>
				</c:if>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Date</th>
							<th>Description</th>
							<th class="text-right">Amount</th>
							<th class="text-right">Balance</th>
						</tr>
					</thead>
					<tbody>
						<% for (SalesOrder salesOrder : uninvoiced) { %>
							<tr>
								<td><%= UserInputUtil.dateToString(salesOrder.getDate(), timeZoneId) %></td>
								<td>
									<div>Pending Order</div>
									<div><c:out value="<%= salesOrder.getSummary() %>"/></div>
								</td>
								<td class="text-right"><%= salesOrder.getTotal() != null ? String.format("%.2f %s", salesOrder.getTotal().getValue(), salesOrder.getTotal().getCurrency()) : "[]" %></td>
								<td class="text-right"></td>
							</tr>
						<% } %>
						<% for (BillingTransaction xaction : xactions) { 
						%>
							<tr>
								<td><%= UserInputUtil.dateToString(xaction.getDate(), timeZoneId) %></td>
								<td>
									<c:choose>
										<c:when test="<%= xaction.getType() == BillingTransactionType.INVOICE %>">
											<div><a href='<%= MvcUriComponentsBuilder.fromMethodName(AccountController.class, "showInvoice", null, null, null, xaction.getReference()).toUriString() %>'>Invoice #<c:out value="<%= xaction.getReference() %>"/></a></div>
										</c:when>
										<c:when test="<%= xaction.getType() == BillingTransactionType.PAYMENT %>">
											<div>Payment #<c:out value="<%= xaction.getReference() %>"/></div>
										</c:when>
									</c:choose>
									<div><c:out value="<%= xaction.getDescription() %>"/></div>
								</td>
								<td class="text-right"><%= xaction.getAmount() != null ? String.format("%.2f %s", xaction.getAmount().getValue(), xaction.getAmount().getCurrency()) : "[]" %></td>
								<td class="text-right"><%= String.format("%.2f %s", xaction.getBalance().getValue(), xaction.getBalance().getCurrency()) %></td>
							</tr>
						<% } %>
					</tbody>
				</table>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
