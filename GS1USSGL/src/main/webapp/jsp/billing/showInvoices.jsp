<%@page import="org.gs1us.sgg.gbservice.api.Invoice"%>
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
    Collection<? extends Invoice> invoices = (Collection<? extends Invoice>)request.getAttribute("invoices");
	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Invoices" />
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>

<c:choose>
	<c:when test='<%=invoices.size() == 0%>'>
		<div class="page-header"></div>
	
		<div class="row">
			<div class="col-md-12">
				<p>There are no invoices.</p>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-md-12">
				<h1>All invoices</h1>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Invoice #</th>
							<th class="text-right">Amount</th>
							<th>Member</th>
							<th>Invoiced</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<%
						    for (Invoice invoice : invoices) {
						%>
							<tr>
								<td><c:out value="<%= invoice.getInvoiceId() %>"/></td>
								<td class="text-right"><%= invoice.getTotal() != null ? String.format("%.2f %s", invoice.getTotal().getValue(), invoice.getTotal().getCurrency()) : "[]" %></td>
								<td><c:out value="<%= invoice.getGBAccountGln() %>"/></td>
								<td>
									<c:if test="<%= invoice.getDate() != null %>"><c:out value="<%= UserInputUtil.dateToString(invoice.getDate(), timeZoneId) %>"/></c:if>
								</td>
								<td>
								</td>
							</tr>
						<% } %>
					</tbody>
				</table>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<jsp:include page="/jsp/includes/footer.jsp" flush="true" />
