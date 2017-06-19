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
	Collection<? extends BillingTransaction> xactions = (Collection<? extends BillingTransaction>)request.getAttribute("xactions");
	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Transactions" />
  <jsp:param name="selectedItem" value="account" />
</jsp:include>

<c:choose>
	<c:when test='<%= xactions.size() == 0 %>'>
		<div class="page-header"></div>
	
		<div class="row">
			<div class="col-md-12">
				<p>You have not yet made any transactions.</p>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-md-12">
				<h1>Transaction history</h1>
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
						<% for (BillingTransaction xaction : xactions) { 
						%>
							<tr>
								<td><%= UserInputUtil.dateToString(xaction.getDate(), timeZoneId) %></td>
								<td>
									<c:choose>
										<c:when test="<%= xaction.getType() == BillingTransactionType.INVOICE %>">
											<div>Invoice #<c:out value="<%= xaction.getReference() %>"/></div>
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
