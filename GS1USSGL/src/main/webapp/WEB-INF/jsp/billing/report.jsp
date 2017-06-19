<%@page import="org.gs1us.sgl.webapp.BillingReportController.PendingReport"%>
<%@page import="org.gs1us.sgl.webapp.BillingReportController.PaidReport"%>
<%@page import="java.util.Date"%>
<%@page import="org.gs1us.sgl.webapp.BillingReportController"%>
<%@page import="org.gs1us.sgg.gbservice.api.Payment"%>
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
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%

	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();

    String actionUrl = MvcUriComponentsBuilder.fromMethodName(BillingReportController.class, "billingReportGet", null, null, null, null).toUriString();
    
    Date fromDate = (Date)request.getAttribute("fromDate");
    Date toDate   = (Date)request.getAttribute("toDate");
    
    String fromDateString = fromDate == null ? null : UserInputUtil.dateOnlyToString(fromDate, timeZoneId);
    String toDateString = toDate == null ? null: UserInputUtil.dateOnlyToString(toDate, timeZoneId);
    
    PaidReport paidReport = (PaidReport)request.getAttribute("paidReport");
    PendingReport pendingReport = (PendingReport)request.getAttribute("pendingReport");
    
    String exportPaidUrl = paidReport == null ? null : MvcUriComponentsBuilder.fromMethodName(BillingReportController.class, "exportPaidReport", null, null, null, fromDateString, toDateString).toUriString();
    String exportPendingUrl = pendingReport == null ? null : MvcUriComponentsBuilder.fromMethodName(BillingReportController.class, "exportPendingReport", null, null, null, fromDateString, toDateString).toUriString();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value='<%= "Billing reports" %>'/>
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>
		<div class="row">
			<div class="col-md-12">
				<h1>Billing Reports</h1>
				<p>
					<form:form commandName="reportCommand" role="form"  method="get" action="<%= actionUrl %>">
						<form:errors path="*" cssClass="alert alert-danger" element="div" />
						<span >Date range:</span>&nbsp;<form:select id="dateRangeSelector" path="dateRangeSelection" >
							<form:option value="MONTH_TO_DATE" label="Month to date" />
							<form:option value="YEAR_TO_DATE" label="Year to date" />
							<form:option value="LAST_MONTH" label="Last month" />
							<form:option value="LAST_YEAR" label="Last year" />
							<form:option value="CUSTOM"  label="Custom..." />
						</form:select>
						<span id="customDates">
							<span>&nbsp;&nbsp;&nbsp;From date:</span>&nbsp;<form:input type="text"  path="fromDate" size="10" maxlength="10" placeholder="MM/DD/YYYY"/>
							<span>&nbsp;&nbsp;&nbsp;To date:</span>&nbsp;<form:input type="text" path="toDate" size="10" maxlength="10" placeholder="MM/DD/YYYY"/>
						</span>
						&nbsp;&nbsp;&nbsp;<button type="submit" class="btn-primary">Show Report</button>
					</form:form>
				</p>
				<c:if test="<%= paidReport != null %>">
					<h2>Invoices for which payment was received between <%= fromDateString %> and <%= toDateString %></h2>
					<p><a href="<%= exportPaidUrl %>">Export this report &gt;</a></p>
					<sgl:invoiceOrderReport report="<%= paidReport %>" asOfDate="<%= toDate %>" timeZoneId="<%= timeZoneId %>"></sgl:invoiceOrderReport>
				</c:if>
				<c:if test="<%= pendingReport != null %>">
					<h2>Pending orders as of <%= toDateString %></h2>
					<p><a href="<%= exportPendingUrl %>">Export this report &gt;</a></p>
					<sgl:invoiceOrderReport report="<%= pendingReport %>" asOfDate="<%= toDate %>" timeZoneId="<%= timeZoneId %>"></sgl:invoiceOrderReport>
				</c:if>
			</div>

		</div>

<script>
$("#dateRangeSelector").on("change", function(){
	var value = this.value;
	if (value == "CUSTOM")
		$("#customDates").show();
	else
		$("#customDates").hide();
});
$(document).ready(function(){
	if ($("#dateRangeSelector").val() == "CUSTOM")
		$("#customDates").show();
	else
		$("#customDates").hide();	
});
</script>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />