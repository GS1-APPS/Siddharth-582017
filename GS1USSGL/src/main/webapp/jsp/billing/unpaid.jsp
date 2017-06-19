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
    Collection<? extends Payment> unpaid = (Collection<? extends Payment>)request.getAttribute("unpaid");
	Map<String,Member> memberMap = (Map<String,Member>)request.getAttribute("memberMap");
	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Committed payments not yet received" />
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>


<div class="row">
	<div class="col-md-12">
		<h2>Payments recorded but whose funds have not yet been received or cleared</h2>
		<c:choose>
		<c:when test="<%= unpaid.size() == 0 %>">
			<p>There are currently no payments awaiting the receipt of funds.</p>
			<p>&gt;&nbsp;&nbsp;<a href="<%= MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingSummary", null, null).toUriString() %>">Go to billing action center</a></p>
		</c:when>
		<c:otherwise>
			<p class="small">Each of the payments below has been committed by the customer but the funds have not yet been received and cleared.
			   After funds are received, select the payment below to record the receipt of funds.</p>
					<table class="table table-striped">
						<thead>
							<tr>
								<th>Payment #</th>
								<th>Date</th>
								<sgl:memberTH/>
								<th>Payment Reference</th>
								<th class="text-right">Amount</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
							<%
							    for (Payment payment : unpaid) {
							        String paidUri = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "paidPaymentGet", null, null, payment.getPaymentId()).toUriString();
									Member member = memberMap.get(payment.getGBAccountGln());
							%>
								<tr>
									<td><c:out value="<%= payment.getPaymentId() %>"/></td>
									<td><c:out value="<%= UserInputUtil.dateToString(payment.getDate(), timeZoneId) %>"/></td>
									<sgl:memberTD member="<%= member %>"></sgl:memberTD>
									<td><c:out value="<%= payment.getPaymentReceiptId() %>"/></td>
									<td class="text-right"><%= payment.getAmount() != null ? String.format("%.2f %s", payment.getAmount().getValue(), payment.getAmount().getCurrency()) : "[]" %></td>
									<td><a href="<%= paidUri %>" title="Record funds received"><span class="icon-shopping_cart"></span></a></td>
								</tr>
							<% } %>
						</tbody>
					</table>
		</c:otherwise>
		</c:choose>

	</div>
</div>

<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
