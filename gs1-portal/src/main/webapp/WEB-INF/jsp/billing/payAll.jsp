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
	Member member = (Member)request.getAttribute("member");
	String memberId = member.getMemberId();
    Collection<? extends Invoice> invoices = (Collection<? extends Invoice>)request.getAttribute("invoices");
    Amount total = (Amount)request.getAttribute("total");
    
    String actionUrl = (String)request.getAttribute("actionUrl");
    String cancelUrl = (String)request.getAttribute("cancelUrl");

	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value='<%= "Pay all invoices for " + member.getCompanyName() %>'/>
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>

<c:choose>
	<c:when test='<%=invoices.size() == 0%>'>
		<div class="page-header"></div>
	
		<div class="row">
			<div class="col-md-12">
				<p>There are no unpaid invoices for <c:out value="<%= member.getCompanyName() %>"/><c:if test='<%= memberId != null %>'> (Member ID <c:out value="<%= memberId %>"/>)</c:if>.</p>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-md-12">
				<h1>Pay all invoices for <c:out value="<%= member.getCompanyName() %>"/><c:if test='<%= memberId != null %>'> (Member ID <c:out value="<%= memberId %>"/>)</c:if></h1>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Invoice #</th>
							<th>Invoiced</th>
							<th>Billing Reference</th>
							<th class="text-right">Amount</th>
						</tr>
					</thead>
					<tbody>
						<%
						    for (Invoice invoice : invoices) {
						%>
							<tr>
								<td>
									<c:if test="<%= invoice.getInvoiceId() != null %>"><c:out value="<%= invoice.getInvoiceId() %>"/></c:if>
								</td>
								<td>
									<c:if test="<%= invoice.getDate() != null %>"><c:out value="<%= UserInputUtil.dateToString(invoice.getDate(), timeZoneId) %>"/></c:if>
								</td>
								<td><c:out value='<%= invoice.getBillingReference() %>' /></td>
								<td class="text-right"><%= invoice.getTotal() != null ? String.format("%.2f %s", invoice.getTotal().getValue(), invoice.getTotal().getCurrency()) : "[]" %></td>
							</tr>
						<% } %>
							<tr>
								<td>Total</td>
								<td>
								</td>
								<td>
								</td>
								<td class="text-right"><%= String.format("%.2f %s", total.getValue(), total.getCurrency()) %></td>
							</tr>
					</tbody>
				</table>
			</div>
			<div class="col-md-4">
				<form:form commandName="paymentCommand" role="form" action="<%= actionUrl %>">
  					<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<div class="form-group">
	    				<label>Payment receipt ID</label>
				    	<form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="paymentId"/>
					</div>
					<div class="form-group">
	    				<label>Amount (<%= total.getCurrency() %>)</label>
				    	<form:input type="text" class="form-control" cssErrorClass="form-control alert-danger" path="paymentAmount" />
					</div>
				    <form:input type="hidden" class="form-control" cssErrorClass="form-control alert-danger" path="currency" />
					<button type="submit" class="btn-primary btn-margin ">Enter Payment</button>
  					<button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelUrl %>"'>Cancel</button>
  					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				</form:form>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
