<%@page import="org.gs1us.sgl.webapp.ImportController"%>
<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.gs1us.sgg.gbservice.api.AppSubscription"%>
<%@page import="org.gs1us.sgl.memberservice.Member"%>
<%@page import="org.gs1us.sgg.gbservice.api.ProductApp"%>
<%@page import="org.gs1us.sgg.gbservice.api.AppDesc"%>
<%@page import="java.util.Date"%>
<%@page import="org.gs1us.sgg.clockservice.ClockService"%>
<%@page import="java.util.Collection"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.gs1us.sgl.memberservice.User"%>
<%@page import="org.gs1us.sgl.webapp.ProductController"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>
<%@page import="org.gs1us.sgl.webapp.standalone.UserController"%>
<%@page import="org.gs1us.sgg.gbservice.api.Product" %>

<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%
    Collection<? extends AppSubscription> subs = (Collection<? extends AppSubscription>)request.getAttribute("subs");
    AppSubscription dwcodeSub = null;
    for (AppSubscription sub : subs)
    {
        if (sub.getAppName().equals("dwcode"))
        {
            dwcodeSub = sub;
            break;
        }
    }
    ClockService clockService = (ClockService) request.getAttribute("clockService");
    Date now = clockService == null ? null : clockService.now();
    Collection<Product> products = (Collection<Product>) request.getAttribute("products");
    String productLine1AttrName = (String) request.getAttribute("productLine1AttrName");
    String productLine2AttrName = (String) request.getAttribute("productLine2AttrName");
    Member forMember = (Member) request.getAttribute("forMember");
    //AttributeDesc productLine1AttrDesc = (AttributeDesc)request.getAttribute("productLine1AttrDesc");
    //AttributeDesc productLine2AttrDesc = (AttributeDesc)request.getAttribute("productLine2AttrDesc");

    String newProductLink = MvcUriComponentsBuilder
            .fromMethodName(ProductController.class, "newProductGet",
                            (Object) null, (Object) null).toUriString();
    String importLink = MvcUriComponentsBuilder
            .fromMethodName(ImportController.class, "importUploadGet",
                            (Object) null, (Object) null).toUriString();
    User user = (User) ((Authentication) request.getUserPrincipal()).getPrincipal();
    String timeZoneId = user.getTimezone();
%>

   
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Products" />
  <jsp:param name="selectedItem" value="products" />
</jsp:include>

<div class="row">
	<div class="col-md-12">
		<h1>Products</h1>
		<c:choose>
			<c:when test='<%= products.size() == 0 %>'>
				<c:choose>
					<c:when test="<%= forMember == null %>">
						<p>You have not yet registered any products.  To register a new product, click the button below.</p>
						<p>
							<a class="btn-large btn-primary btn-margin" href="<%=newProductLink%>" role="button"><span class="icon-plus"></span> Register a new product</a>
							<c:if test="<%= WebappUtil.showExperimentalFeatures() %>">
		                    	<a class="btn-large btn-primary btn-margin" href="<%= importLink %>" role="button"><span class="icon-upload"></span> Upload a file of product data</a>
							</c:if>
						</p>
					</c:when>
					<c:otherwise>
						<c:out value="<%= forMember.getCompanyName() %>"/> has not yet registered any products.
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:if test="<%= forMember != null %>">
					<div>Showing products for:</div>
					<sgl:memberNameAndAddressBlock member="<%= forMember %>"/>
				</c:if>
			
				<c:if test="<%= forMember == null %>">
				<p>To register a new product, click the button below. To edit an existing product, select &#x201c;Edit&#x201d; under the product's &#x201c;Actions&#x201d; section.</p>
				<p>
                    <a class="btn-large btn-primary btn-margin" href="<%= newProductLink %>" role="button"><span class="icon-plus"></span> Register a new product</a>
					<c:if test="<%= WebappUtil.showExperimentalFeatures() %>">
                    	<a class="btn-large btn-primary btn-margin" href="<%= importLink %>" role="button"><span class="icon-upload"></span> Upload a file of product data</a>
					</c:if>
				</p>
				</c:if>
				
				<table class="table table-striped">
					<thead>
						<tr>
							<th>GTIN</th>
							<th>Product</th>
							<th>Last Modified</th>
							<%--<th>
							 <th>Renewal Date</th>
							<c:if test="<%= dwcodeSub != null %>">
								<th>DWCode</th>
							</c:if>
							<th>Status</th> --%>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<% for (Product product : products) { 
						 	 String editLink = MvcUriComponentsBuilder.fromMethodName(ProductController.class, "editProductGet", null, null, product.getGtin()).toUriString();
						 	 String renewLink = MvcUriComponentsBuilder.fromMethodName(ProductController.class, "renewProductGet", null, null, product.getGtin()).toUriString();
						 	 String deleteLink = MvcUriComponentsBuilder.fromMethodName(ProductController.class, "deleteProductGet", null, null, product.getGtin()).toUriString();
							String productLine1 = product.getAttributes().getAttribute(productLine1AttrName);
							String productLine2 = product.getAttributes().getAttribute(productLine2AttrName);
						%>
							<tr>
								<td><c:out value='<%= product.getGtin() %>' /></td>
								<td>
									<div class="sgl-show-product-brandName"><c:out value='<%= productLine1 %>' /></div>
									<div class="sgl-show-product-name"><c:out value='<%= productLine2 %>' /></div>
								</td>
								<td><c:out value='<%= UserInputUtil.dateToString(product.getModifiedDate(), timeZoneId) %>' /></td>
							<%-- 	<td>
									<c:choose>
										<c:when test='<%= product.getNextActionDate() != null %>'>
											<div><c:out value='<%= UserInputUtil.dateOnlyToString(product.getNextActionDate(), timeZoneId) %>' /></div>
											<c:if test='<%= product.getPendingNextActionDate() != null %>'>
												<div class="small">(Pending: <c:out value='<%= UserInputUtil.dateOnlyToString(product.getPendingNextActionDate(), timeZoneId) %>' />)</div>
											</c:if>
										</c:when>
										<c:otherwise>
											<c:if test='<%= product.getPendingNextActionDate() != null %>'>
												<div class="small">(Pending payment)</div>
											</c:if>
										</c:otherwise>
									</c:choose>
								</td> 
								<c:if test="<%= dwcodeSub != null %>">
								<td class="color-forest">
									<% ProductApp productApp = new ProductApp(dwcodeSub.getAppDesc(), product);
									     if (productApp.isPaid(now))
									     {
									%>
									    <span class="icon-check"></span>
									<% }%>
							    </td>
							    </c:if>
							    <td >
								  <c:choose>
								  <c:when test="<%= product.getPendingOrderIds() != null &&  product.getPendingOrderIds().length > 0 %>">
								    <a href="#" class="color-teal" data-toggle="tooltip" data-placement="top" title="A billing transaction is being processed."><span class="icon-shopping_cart"></span></a>
								  </c:when>
								  <c:when test="<%= now != null && product.getNextActionDate() != null && now.after(new Date(product.getNextActionDate().getTime() - (86400000L * 30))) %>">
									<c:choose>
										<c:when test="<%= forMember == null %>">
								    		<a href="<%= renewLink %>" class="color-orange" data-toggle="tooltip" data-placement="top" title="Your renewal date is approaching. Click here to renew."><span class="icon-calendar"></span></a>
										</c:when>
										<c:otherwise>
								    		<a href="#" class="color-orange" data-toggle="tooltip" data-placement="top" title="The renewal date is approaching."><span class="icon-calendar"></span></a>
										</c:otherwise>
									</c:choose>
								  </c:when>
								  </c:choose>
								</td>--%>
								<td>
								<c:if test="<%= forMember == null %>">
								  <a href="<%= editLink %>" title="Edit this product's data"><small>Edit</small></a>
								   <a href="<%= deleteLink%>" title="Delete this product's data" onclick="return confirm('Are you sure you want to delete this product?');"><small>Delete</small></a>
								</c:if>
								</td>
							</tr>
						<% } %>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>


    
</div>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />