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
	Collection<SalesOrder> uninvoiced = (Collection<SalesOrder>)request.getAttribute("uninvoiced");
	Member member = (Member)request.getAttribute("member");
	String paymentMethod = (String)request.getAttribute("paymentMethod");

	String actionUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "invoiceAndBillMemberPost", null, null, member.getGln()).toUriString();
	String cancelUrl = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "invoiceAndBillGet", null, null).toUriString();


	User user = (User)((Authentication)request.getUserPrincipal()).getPrincipal();
	String timeZoneId = user.getTimezone();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Pending Orders" />
  <jsp:param name="selectedItem" value="billing" />
</jsp:include>

<c:choose>
	<c:when test='<%= uninvoiced == null || uninvoiced.size() == 0  %>'>
		<div class="page-header"></div>
	
		<div class="row">
			<div class="col-md-12">
				<p>There are no uninvoiced orders for <sgl:memberSpan member="<%= member %>" />.</p>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-md-12">
				<h1>Create and bill an invoice</h1>
				<p>Create an invoice for <sgl:memberSpan member="<%= member %>"/> and bill it</p>
				<sgl:memberNameAndAddressBlock member="<%= member %>"/>
				<c:if test="<%= paymentMethod != null %>">
					<p><c:out value="<%= paymentMethod %>"/></p>
				</c:if>
				<form method="post" action="<%= actionUrl %>">
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
						for (SalesOrder order : uninvoiced) {
						    subTotal = subTotal.add(order.getTotal());
					%>
							<tr>
								<td><%= order.getOrderId() %>
								<td><%= UserInputUtil.dateToString(order.getDate(), timeZoneId) %></td>
								<td><c:out value="<%= order.getSummary() %>" /></td>
								<td class="text-right"><%= order.getTotal() %></td>
								<td><input type="hidden" name='<%= "order-" + order.getOrderId() %>' value="checked" /></td>
							</tr>
					<% } %>
					<tr class="bg-light-gray">
						<td colspan="3"><strong>Subtotal</strong></td>
						<td id="subtotal" class="text-right"><%= subTotal %></td>
					</tr>
					<% { String currencyPrompt = "Amount in " + subTotal.getCurrency(); %>
					<tr>
						<td></td>
						<td>State sales tax</td>
						<td>
							<select name="stateSalesTaxJurisdiction" class="form-control" >
								<option value="">Choose jurisdiction...</option>
								<option value="AL">Alabama</option>
								<option value="AK">Alaska</option>
								<option value="AZ">Arizona</option>
								<option value="AR">Arkansas</option>
								<option value="CA">California</option>
								<option value="CO">Colorado</option>
								<option value="CT">Connecticut</option>
								<option value="DE">Delaware</option>
								<option value="FL">Florida</option>
								<option value="GA">Georgia</option>
								<option value="HI">Hawaii</option>
								<option value="ID">Idaho</option>
								<option value="IL">Illinois</option>
								<option value="IN">Indiana</option>
								<option value="IA">Iowa</option>
								<option value="KS">Kansas</option>
								<option value="KY">Kentucky</option>
								<option value="LA">Louisiana</option>
								<option value="ME">Maine</option>
								<option value="MD">Maryland</option>
								<option value="MA">Massachusetts</option>
								<option value="MI">Michigan</option>
								<option value="MN">Minnesota</option>
								<option value="MS">Mississippi</option>
								<option value="MO">Missouri</option>
								<option value="MT">Montana</option>
								<option value="NE">Nebraska</option>
								<option value="NV">Nevada</option>
								<option value="NH">New Hampshire</option>
								<option value="NJ">New Jersey</option>
								<option value="NM">New Mexico</option>
								<option value="NY">New York</option>
								<option value="NC">North Carolina</option>
								<option value="ND">North Dakota</option>
								<option value="OH">Ohio</option>
								<option value="OK">Oklahoma</option>
								<option value="OR">Oregon</option>
								<option value="PA">Pennsylvania</option>
								<option value="RI">Rhode Island</option>
								<option value="SC">South Carolina</option>
								<option value="SD">South Dakota</option>
								<option value="TN">Tennessee</option>
								<option value="TX">Texas</option>
								<option value="UT">Utah</option>
								<option value="VT">Vermont</option>
								<option value="VA">Virginia</option>
								<option value="WA">Washington</option>
								<option value="WV">West Virginia</option>
								<option value="WI">Wisconsin</option>
								<option value="WY">Wyoming</option> 
							</select>
						</td>
						<td><input type="text" class="form-control calc-subtotal text-right"  name="stateSalesTaxAmount"  placeholder="<%= currencyPrompt %>"/></td>
					</tr>
					<tr>
						<td></td>
						<td>Local sales tax</td>
						<td>
						  <input type="text" class="form-control"  name="localSalesTaxJurisdiction" placeholder="Enter local jurisdiction name"/>
						</td>
						<td><input type="text" class="form-control calc-subtotal text-right"  name="localSalesTaxAmount" placeholder="<%= currencyPrompt %>"/></td>
					</tr>
					<tr class="bg-light-gray">
						<td colspan="3"><strong>Total</strong></td>
						<td id="total" class="text-right"><%= subTotal %></td>
					</tr>
					<% } %>
					</tbody>
					</table>
					<div class="col-md-4">
					<div class="form-group">
	    				<label>Billing reference (optional)</label>
				    	<input type="text" class="form-control" name="billingReference"/>
					</div>
					<button type="submit" class="btn-primary btn-margin ">Bill</button>
  					<button type="button" class="btn-secondary btn-margin " onclick='window.location.href="<%= cancelUrl %>"'>Cancel</button>
  					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  					</div>
				</form>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<script>
$(".calc-subtotal").change(function(){
	var numberPat = /^(([0-9]+(\.[0-9]+)?)|\.[0-9]+)$/;
	
	var subtotal = $("#subtotal").text();
	var spos = subtotal.indexOf(" ");
	var suffix = "";
	if (spos > 0)
	{
		suffix = subtotal.substring(spos, subtotal.length);
		subtotal = subtotal.substring(0, spos);
	}
	
	var total = parseFloat(subtotal);
	$(".calc-subtotal").each(function(){
		var s = this.value.trim();
		var pos = s.indexOf(" ");
		if (pos > 0)
			s = s.substring(0, pos);

		if (numberPat.test(s))
		{
			var parsed = parseFloat(s);
			total += parsed;
		}
	});
	$("#total").text(total.toFixed(2) + suffix);
});
</script>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
