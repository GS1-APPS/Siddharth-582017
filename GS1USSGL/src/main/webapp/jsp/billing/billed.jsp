<%@page import="org.gs1us.sgl.memberservice.Member"%>
<%@page import="java.util.Map"%>
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
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%
    Collection<? extends Invoice> billed = (Collection<? extends Invoice>)request.getAttribute("billed");
	Map<String,Member> memberMap = (Map<String,Member>)request.getAttribute("memberMap");
	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Billed invoices not yet paid" />
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>


<div class="row">
	<div class="col-md-12">
		<h2>Invoices billed to the customer but no payment yet recorded or received</h2>
		<c:choose>
		<c:when test="<%= billed.size() == 0 %>">
			<p>There are currently no invoices awaiting commitment of payment from the customer.</p>
			<p>&gt;&nbsp;&nbsp;<a href="<%= MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingSummary", null, null).toUriString() %>">Go to billing action center</a></p>
		</c:when>
		<c:otherwise>
			<p class="small">Each of the invoices below has been billed to the customer but no commitment to pay has been received.
			   After receiving a payment or a commitment to pay, select it below to record the payment or commmitment of payment.</p>
					<table class="table table-striped">
						<thead>
							<tr>
								<th>Invoice #</th>
								<th>Date</th>
								<sgl:memberTH></sgl:memberTH>
								<th>Billing Reference</th>
								<th class="text-right">Amount</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
							<%
							    for (Invoice invoice : billed) {
							        String payUri = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "payInvoiceGet", null, null, invoice.getInvoiceId()).toUriString();
									Member member = memberMap.get(invoice.getGBAccountGln());

							%>
								<tr>
									<td><c:out value="<%= invoice.getInvoiceId() %>"/></td>
									<td><c:out value="<%= UserInputUtil.dateToString(invoice.getDate(), timeZoneId) %>"/></td>
									<sgl:memberTD member="<%= member %>"></sgl:memberTD>
									<td><c:out value="<%= invoice.getBillingReference() %>"/></td>
									<td class="text-right"><%= invoice.getTotal() != null ? String.format("%.2f %s", invoice.getTotal().getValue(), invoice.getTotal().getCurrency()) : "[]" %></td>
									<td><a href="<%= payUri %>" title="Record a payment for this invoice"><span class="icon-shopping_cart"></span></a></td>
								</tr>
							<% } %>
						</tbody>
					</table>

		</c:otherwise>
		</c:choose>

	</div>
</div>

<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
