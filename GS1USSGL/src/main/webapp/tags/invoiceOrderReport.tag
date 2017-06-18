<%@tag import="org.gs1us.sgl.webapp.AccountController"%>
<%@tag import="org.gs1us.sgg.gbservice.api.InvoiceExtra"%>
<%@tag import="java.util.Map"%>
<%@tag import="java.util.Collection"%>
<%@tag import="org.gs1us.sgg.gbservice.api.SalesOrder"%>
<%@tag import="org.gs1us.sgl.webapp.BillingReportController.PaidReport"%>
<%@tag import="org.gs1us.sgl.webapp.BillingReportController"%>
<%@tag import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@tag import="org.gs1us.sgl.memberservice.Member"%>
<%@tag import="org.gs1us.sgg.gbservice.api.Invoice"%>
<%@tag import="org.gs1us.sgg.util.UserInputUtil"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeType"%>
<%@tag import="java.util.List"%>
<%@tag import="org.gs1us.sgl.webapp.SortPageManager"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%@ attribute name="report" required="true" type="PaidReport" %>
<%@ attribute name="asOfDate" required="true" type="java.util.Date" %>
<%@ attribute name="timeZoneId" required="true" type="java.lang.String" %>

					<table class="table">
						<thead>
							<tr>
							<th>Invoice ID</th>
							<th>Member / Order</th>
							<th class="text-right">Amount</th>
							<th class="text-right">Order Date</th>
							<th class="text-right">Invoice Date</th>
							<th class="text-right">Payment Committed</th>
							<th class="text-right">Paid</th>
							</tr>
						</thead>
						<tbody>
						<% for (Invoice invoice : report.getInvoices()) { 
						    String gln = invoice.getGBAccountGln();
						    Member member = report.getMember(gln);
						    Collection<? extends SalesOrder> orders = report.getOrders(invoice);
						    int orderCount = orders == null ? 0 : orders.size();
						    Collection<? extends InvoiceExtra> extras = invoice.getExtras();
						    int extraCount = orders == null ? 0 : extras.size();
						    String invoiceDetailUrl = MvcUriComponentsBuilder.fromMethodName(AccountController.class, "showInvoice", null, null, null, invoice.getInvoiceId()).toUriString();
						%>
							<tr>
								<td rowspan=<%= orderCount+extraCount+1 %>"><a href="<%= invoiceDetailUrl %>"><%= invoice.getInvoiceId() %></a></td>
								<td>
									<c:if test="<%= member == null %>">
										<div>GLN&nbsp;<%= gln %></div>
									</c:if>
									<c:if test="<%= member != null %>">
										<sgl:memberNameAndAddressBlock member="<%= member %>" />
									</c:if>
								</td>
								<td class="text-right"><strong><c:out value="<%= invoice.getTotal().toString() %>"/></strong></td>	
								<td></td>							
								<td class="text-right"><sgl:pastOrFutureDate timeZoneId="<%= timeZoneId %>" date="<%= invoice.getDate() %>" asOfDate="<%= asOfDate %>"></sgl:pastOrFutureDate></td>
								<td class="text-right"><sgl:pastOrFutureDate timeZoneId="<%= timeZoneId %>" date="<%= invoice.getPaymentCommittedDate() %>" asOfDate="<%= asOfDate %>"></sgl:pastOrFutureDate></td>
								<td class="text-right"><sgl:pastOrFutureDate timeZoneId="<%= timeZoneId %>" date="<%= invoice.getPaidDate() %>" asOfDate="<%= asOfDate %>"></sgl:pastOrFutureDate></td>
							</tr>
							<c:if test="<%= orders != null %>">
								<% for (SalesOrder order : orders) { %>
									<tr>
										<td><c:out value="<%= order.getSummary() %>"/></td>
										<td class="text-right"><c:out value="<%= order.getTotal().toString() %>"/></td>
										<td class="text-right"><sgl:pastOrFutureDate timeZoneId="<%= timeZoneId %>" date="<%= order.getDate() %>" asOfDate="<%= asOfDate %>"></sgl:pastOrFutureDate></td>
									</tr>
								<% } %>
							</c:if>
							<c:if test="<%= extras != null %>">
								<% for (InvoiceExtra extra : extras) { %>
									<tr>
										<td><c:out value="<%= extra.getItemDescription() %>"/></td>
										<td class="text-right"><c:out value="<%= extra.getTotal().toString() %>"/></td>
										<td></td>
									</tr>
								<% } %>
							</c:if>
						<% } %>
						<% for (Map.Entry<String,Collection<? extends SalesOrder>> entry : report.getUninvoicedOrders().entrySet()) {
						    String gln = entry.getKey();
						    Collection<? extends SalesOrder> uninvoicedOrders = entry.getValue();
						    Member member = report.getMember(gln);
						%>
							<tr>
								<td rowspan=<%= uninvoicedOrders.size()+1 %>">(uninvoiced)</td>
								<td>
									<c:if test="<%= member == null %>">
										<div>GLN&nbsp;<%= gln %></div>
									</c:if>
									<c:if test="<%= member != null %>">
										<div><c:out value="<%= member.getCompanyName() %>"/></div>
										<c:if test="<%= member.getAddress1() != null %>" >
											<div class="small color-dark-medium-gray"><c:out value="<%= member.getAddress1() %>"/></div>
										</c:if>
										<c:if test="<%= member.getAddress2() != null %>" >
											<div class="small color-dark-medium-gray"><c:out value="<%= member.getAddress2() %>"/></div>
										</c:if>
										<c:if test="<%= member.getCity() != null || member.getState() != null || member.getPostalCode() != null %>" >
										<div class="small color-dark-medium-gray">
											<c:if test="<%= member.getCity() != null %>">
												<c:out value="<%= member.getCity() %>"/>
											</c:if>
											<c:if test="<%= member.getState() != null %>">
												<c:out value="<%= member.getState() %>"/>
											</c:if>
											<c:if test="<%= member.getPostalCode() != null %>">
												<c:out value="<%= member.getPostalCode() %>"/>
											</c:if>
										</div>
										</c:if>
										<div class="small color-dark-medium-gray">GLN:&nbsp;<%= gln %></div>
										<c:if test="<%= member.getMemberId() != null %>">
											<div class="small color-dark-medium-gray">Member&nbsp;ID: <c:out value="<%= member.getMemberId() %>"/></div>
										</c:if>
									</c:if>
								</td>
								<td></td>	
								<td></td>							
							</tr>
							<c:if test="<%= uninvoicedOrders != null %>">
								<% for (SalesOrder order : uninvoicedOrders) { %>
									<tr>
										<td><c:out value="<%= order.getSummary() %>"/></td>
										<td class="text-right"><c:out value="<%= order.getTotal().toString() %>"/></td>
										<td class="text-right"><sgl:pastOrFutureDate timeZoneId="<%= timeZoneId %>" date="<%= order.getDate() %>" asOfDate="<%= asOfDate %>"></sgl:pastOrFutureDate></td>
									</tr>
								<% } %>
							</c:if>
							<% } %>
						</tbody>
					</table>
