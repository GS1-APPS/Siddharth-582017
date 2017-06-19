<%@page import="java.util.HashMap"%>
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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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
    Member forMember = (Member) request.getAttribute("forMember");
    
    //Collection<Product> products = (Collection<Product>) request.getAttribute("products");
    //String productLine1AttrName = (String) request.getAttribute("productLine1AttrName");
    //String productLine2AttrName = (String) request.getAttribute("productLine2AttrName");    
    //AttributeDesc productLine1AttrDesc = (AttributeDesc)request.getAttribute("productLine1AttrDesc");
    //AttributeDesc productLine2AttrDesc = (AttributeDesc)request.getAttribute("productLine2AttrDesc");

    Integer productCount = (Integer) request.getAttribute("productCount");
    Integer noOfPages = (Integer) request.getAttribute("noOfPages");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
        
    String newProductLink = MvcUriComponentsBuilder
            .fromMethodName(ProductController.class, "newProductGet",
                            (Object) null, (Object) null).toUriString();
    String importLink = MvcUriComponentsBuilder
            .fromMethodName(ImportController.class, "importShowAllGet",
                            (Object) null, (Object) null).toUriString();
    User user = (User) ((Authentication) request.getUserPrincipal()).getPrincipal();
    String timeZoneId = user.getTimezone();
    
	HashMap<String, String>  countryCodes = new HashMap<String, String>();
	countryCodes.put("1","AF");
	countryCodes.put("2","AL");
	countryCodes.put("3","DZ");
	countryCodes.put("4","AS");
	countryCodes.put("5","AD");
	countryCodes.put("6","AO");
	countryCodes.put("7","AI");
	countryCodes.put("8","AQ");
	countryCodes.put("9","AG");
	countryCodes.put("10","AR");
	countryCodes.put("11","AM");
	countryCodes.put("12","AW");
	countryCodes.put("13","AU");
	countryCodes.put("14","AT");
	countryCodes.put("15","AZ");
	countryCodes.put("16","BS");
	countryCodes.put("17","BH");
	countryCodes.put("18","BD");
	countryCodes.put("19","BB");
	countryCodes.put("20","BY");
	countryCodes.put("21","BE");
	countryCodes.put("22","BZ");
	countryCodes.put("23","BJ");
	countryCodes.put("24","BM");
	countryCodes.put("25","BT");
	countryCodes.put("26","BO");
	countryCodes.put("27","BA");
	countryCodes.put("28","BW");
	countryCodes.put("29","BV");
	countryCodes.put("30","BR");
	countryCodes.put("31","IO");
	countryCodes.put("32","BN");
	countryCodes.put("33","BG");
	countryCodes.put("34","BF");
	countryCodes.put("35","BI");
	countryCodes.put("36","KH");
	countryCodes.put("37","CM");
	countryCodes.put("38","CA");
	countryCodes.put("39","CV");
	countryCodes.put("40","KY");
	countryCodes.put("41","CF");
	countryCodes.put("42","TD");
	countryCodes.put("43","CL");
	countryCodes.put("44","CN");
	countryCodes.put("45","CX");
	countryCodes.put("46","CC");
	countryCodes.put("47","CO");
	countryCodes.put("48","KM");
	countryCodes.put("49","CG");
	countryCodes.put("50","CK");
	countryCodes.put("51","CR");
	countryCodes.put("52","CI");
	countryCodes.put("53","HR");
	countryCodes.put("54","CU");
	countryCodes.put("55","CY");
	countryCodes.put("56","CZ");
	countryCodes.put("57","DK");
	countryCodes.put("58","DJ");
	countryCodes.put("59","DM");
	countryCodes.put("60","DO");
	countryCodes.put("61","TP");
	countryCodes.put("62","EC");
	countryCodes.put("63","EG");
	countryCodes.put("64","SV");
	countryCodes.put("65","GQ");
	countryCodes.put("66","ER");
	countryCodes.put("67","EE");
	countryCodes.put("68","ET");
	countryCodes.put("69","FK");
	countryCodes.put("70","FO");
	countryCodes.put("71","FJ");
	countryCodes.put("72","FI");
	countryCodes.put("73","FR");
	countryCodes.put("74","FX");
	countryCodes.put("75","GF");
	countryCodes.put("76","PF");
	countryCodes.put("77","TF");
	countryCodes.put("78","GA");
	countryCodes.put("79","GM");
	countryCodes.put("80","GE");
	countryCodes.put("81","DE");
	countryCodes.put("82","GH");
	countryCodes.put("83","GI");
	countryCodes.put("84","GR");
	countryCodes.put("85","GL");
	countryCodes.put("86","GD");
	countryCodes.put("87","GP");
	countryCodes.put("88","GU");
	countryCodes.put("89","GT");
	countryCodes.put("90","GN");
	countryCodes.put("91","GW");
	countryCodes.put("92","GY");
	countryCodes.put("93","HT");
	countryCodes.put("94","HM");
	countryCodes.put("95","HN");
	countryCodes.put("96","HK");
	countryCodes.put("97","HU");
	countryCodes.put("98","IS");
	countryCodes.put("99","IN");
	countryCodes.put("100","ID");
	countryCodes.put("101","IR");
	countryCodes.put("102","IQ");
	countryCodes.put("103","IE");
	countryCodes.put("104","IL");
	countryCodes.put("105","IT");
	countryCodes.put("106","JM");
	countryCodes.put("107","JP");
	countryCodes.put("108","JO");
	countryCodes.put("109","KZ");
	countryCodes.put("110","KE");
	countryCodes.put("111","KI");
	countryCodes.put("112","KP");
	countryCodes.put("113","KR");
	countryCodes.put("114","KW");
	countryCodes.put("115","KG");
	countryCodes.put("116","LA");
	countryCodes.put("117","LV");
	countryCodes.put("118","LB");
	countryCodes.put("119","LS");
	countryCodes.put("120","LR");
	countryCodes.put("121","LY");
	countryCodes.put("122","LI");
	countryCodes.put("123","LT");
	countryCodes.put("124","LU");
	countryCodes.put("125","MO");
	countryCodes.put("126","MK");
	countryCodes.put("127","MG");
	countryCodes.put("128","MW");
	countryCodes.put("129","MY");
	countryCodes.put("130","MV");
	countryCodes.put("131","ML");
	countryCodes.put("132","MT");
	countryCodes.put("133","MH");
	countryCodes.put("134","MQ");
	countryCodes.put("135","MR");
	countryCodes.put("136","MU");
	countryCodes.put("137","YT");
	countryCodes.put("138","MX");
	countryCodes.put("139","FM");
	countryCodes.put("140","MD");
	countryCodes.put("141","MC");
	countryCodes.put("142","MN");
	countryCodes.put("143","MS");
	countryCodes.put("144","MA");
	countryCodes.put("145","MZ");
	countryCodes.put("146","MM");
	countryCodes.put("147","NA");
	countryCodes.put("148","NR");
	countryCodes.put("149","NP");
	countryCodes.put("150","NL");
	countryCodes.put("151","AN");
	countryCodes.put("152","NC");
	countryCodes.put("153","NZ");
	countryCodes.put("154","NI");
	countryCodes.put("155","NE");
	countryCodes.put("156","NG");
	countryCodes.put("157","NU");
	countryCodes.put("158","NF");
	countryCodes.put("159","MP");
	countryCodes.put("160","NO");
	countryCodes.put("161","OM");
	countryCodes.put("162","PK");
	countryCodes.put("163","PW");
	countryCodes.put("164","PA");
	countryCodes.put("165","PG");
	countryCodes.put("166","PY");
	countryCodes.put("167","PE");
	countryCodes.put("168","PH");
	countryCodes.put("169","PN");
	countryCodes.put("170","PL");
	countryCodes.put("171","PT");
	countryCodes.put("172","PR");
	countryCodes.put("173","QA");
	countryCodes.put("174","RE");
	countryCodes.put("175","RO");
	countryCodes.put("176","RU");
	countryCodes.put("177","RW");
	countryCodes.put("178","KN");
	countryCodes.put("179","LC");
	countryCodes.put("180","VC");
	countryCodes.put("181","WS");
	countryCodes.put("182","SM");
	countryCodes.put("183","ST");
	countryCodes.put("184","SA");
	countryCodes.put("185","SN");
	countryCodes.put("186","SC");
	countryCodes.put("187","SL");
	countryCodes.put("188","SG");
	countryCodes.put("189","SK");
	countryCodes.put("190","SI");
	countryCodes.put("191","SB");
	countryCodes.put("192","SO");
	countryCodes.put("193","ZA");
	countryCodes.put("194","GS");
	countryCodes.put("195","ES");
	countryCodes.put("196","LK");
	countryCodes.put("197","SH");
	countryCodes.put("198","PM");
	countryCodes.put("199","SD");
	countryCodes.put("200","SR");
	countryCodes.put("201","SJ");
	countryCodes.put("202","SZ");
	countryCodes.put("203","SE");
	countryCodes.put("204","CH");
	countryCodes.put("205","SY");
	countryCodes.put("206","TW");
	countryCodes.put("207","TJ");
	countryCodes.put("208","TZ");
	countryCodes.put("209","TH");
	countryCodes.put("210","TG");
	countryCodes.put("211","TK");
	countryCodes.put("212","TO");
	countryCodes.put("213","TT");
	countryCodes.put("214","TN");
	countryCodes.put("215","TR");
	countryCodes.put("216","TM");
	countryCodes.put("217","TC");
	countryCodes.put("218","TV");
	countryCodes.put("219","UG");
	countryCodes.put("220","UA");
	countryCodes.put("221","AE");
	countryCodes.put("222","GB");
	countryCodes.put("223","US");
	countryCodes.put("224","UM");
	countryCodes.put("225","UY");
	countryCodes.put("226","UZ");
	countryCodes.put("227","VU");
	countryCodes.put("228","VA");
	countryCodes.put("229","VE");
	countryCodes.put("230","VN");
	countryCodes.put("231","VG");
	countryCodes.put("232","VI");
	countryCodes.put("233","WF");
	countryCodes.put("234","EH");
	countryCodes.put("235","YE");
	countryCodes.put("236","YU");
	countryCodes.put("237","ZR");
	countryCodes.put("238","ZM");
	countryCodes.put("239","ZW");
%>

<jsp:include page="/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Products" />
  <jsp:param name="selectedItem" value="products" />
</jsp:include>

<spring:url value="/ui/product" var="pageurl" />

<div class="row">
	<div class="col-md-12">
	<h1>Products<span style="padding-left:600px;">Total: <%= productCount %></span></h1>	
				
		<c:choose>
			<c:when test='<%= productCount == 0 %>'>
				<c:choose>
					<c:when test="<%= forMember == null %>">
						<p>You have not yet registered any products.  To register a new product, click the button below.</p>
						<p>
							<a class="btn-large btn-primary btn-margin" href="<%=newProductLink%>" role="button"><span class="icon-plus"></span> Register a new product</a>
							<c:if test="<%= WebappUtil.showExperimentalFeatures() %>">
		                    	<a class="btn-large btn-primary btn-margin" href="<%= importLink %>" role="button"><span class="icon-upload"></span> Upload Products</a>
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
				<!--<p>To register a new product, click the button below. To edit an existing product, select &#x201c;Edit&#x201d; under the product's &#x201c;Actions&#x201d; section.</p>-->
				<p>
                    <a class="btn-large btn-primary btn-margin" href="<%= newProductLink %>" role="button"><span class="icon-plus"></span> Register a new product</a>
					<c:if test="<%= WebappUtil.showExperimentalFeatures() %>">
                    	<a class="btn-large btn-primary btn-margin" href="<%= importLink %>" role="button"><span class="icon-upload"></span> Upload Products</a>
					</c:if>
				</p>
				</c:if>
				
				<div>
					<c:set var="productListHolder" value="${productPagedList}" scope="request" />
					<table class="table table-striped">
						<thead>
							<tr>
								<th>GTIN</th>
								<th>TM</th>
								<th>Brand</th>
								<th>Label Description</th>
								<th>Last Modified</th>
								<th>Status</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="ph" items="${productListHolder.pageList}">
					            <% 
					            	Product pp = (Product) (pageContext.getAttribute("ph")); 
								 	String editLink = MvcUriComponentsBuilder.fromMethodName(ProductController.class, "editProductGet", null, null, pp.getGtin()).toUriString();
								 	String deleteLink = MvcUriComponentsBuilder.fromMethodName(ProductController.class, "deleteProductGet", null, null, pp.getGtin()).toUriString();
								 	String moreDetailsLink = MvcUriComponentsBuilder.fromMethodName(ProductController.class, "detailProductGet", null, null, pp.getGtin()).toUriString();	            
					            %>
				            	<tr>
				            		<td style="width:190px;"><a href="<%= moreDetailsLink %>" target="_new">${ph.gtin}</a></td>
				            		<td><c:out value='<%= countryCodes.get(pp.getAttributes().getAttribute("targetMarket")) %>' /></td>	            		
				            		<td>${ph.attributes.getAttribute("brandName")}</td>
				            		<td style="width:500px;">${ph.attributes.getAttribute("additionalTradeItemDescription")}</td>
				            		<td style="width:250px;"><c:out value='<%= UserInputUtil.dateToString(pp.getModifiedDate(), timeZoneId) %>' /></td>
				            		
									<% if (pp.getAttributes().getAttribute("targetMarket") != null && pp.getAttributes().getAttribute("brandName") != null
									  		&& pp.getAttributes().getAttribute("itemDataLanguage") != null && pp.getAttributes().getAttribute("additionalTradeItemDescription") != null
									  		&& pp.getAttributes().getAttribute("gpcCategoryCode") != null && pp.getAttributes().getAttribute("companyName") != null 
									  		&& pp.getAttributes().getAttribute("informationProviderGLN") != null && pp.getAttributes().getAttribute("uriProductImage") != null ) { %>
										<td><span class="icon-check color-orange" data-toggle="tooltip" data-placement="top" title="Complete product record"></span></td>
									<% } else { %>								
										<td><span class="icon-exclamation_sign color-orange" data-toggle="tooltip" data-placement="top" title="Partial product record"></span></td>
									<% } %>								
				            		
				            		<td>
										<c:if test="<%= forMember == null %>">
										  <a href="<%= editLink %>" title="Edit this product's data"><span class="icon-pencil"></span></a>
										  <a href="<%= deleteLink %>" title="Delete this product's data" onclick="return confirm('Are you sure you want to delete this product?');"><span class="icon-trashcan"></span></a>								
										</c:if>	            			            		
				            		</td>	   
				            	</tr>							
							</c:forEach>												
						</tbody>
					</table>				
				</div>
				
				<div style="width: 1150px; white-space:wrap; text-align:center">
					<%--For displaying Previous link except for the 1st page --%>
				    <c:if test="${currentPage != 1}">
				        <a href="${pageurl}/${currentPage - 1}">Previous</a>
				    </c:if>
				    
				    <% if (currentPage == 1) { %>
				    <c:forEach begin="${currentPage}" end="${currentPage + 19}" varStatus="i">
						<c:choose>
				           <c:when test="${currentPage eq i.index}">
				               ${i.index}
				           </c:when>
				           <c:otherwise>
				               <a href="${pageurl}/${i.index}">${i.index}</a>
				           </c:otherwise>
				       	</c:choose>
				       	&nbsp;
				    </c:forEach>				    
				    <% } else if ( (currentPage + 20) < noOfPages ) { %>
				    <c:forEach begin="${currentPage}" end="${currentPage + 20}" varStatus="i">
						<c:choose>
				           <c:when test="${currentPage eq i.index}">
				               ${i.index}
				           </c:when>
				           <c:otherwise>
				               <a href="${pageurl}/${i.index}">${i.index}</a>
				           </c:otherwise>
				       	</c:choose>
				       	&nbsp;
				    </c:forEach>
				    <% } else { %>
				    <% int handle = (noOfPages - currentPage); int handle1 = (20 - handle); int handle2 = (currentPage - handle1); if (handle2 == 0) { handle2 ++ ;}  %>
				    <c:forEach begin="<%=handle2 %>" end="${noOfPages}" varStatus="i">
						<c:choose>
				           <c:when test="${currentPage eq i.index}">
				               ${i.index}
				           </c:when>
				           <c:otherwise>
				               <a href="${pageurl}/${i.index}">${i.index}</a>
				           </c:otherwise>
				       	</c:choose>
				       	&nbsp;
				    </c:forEach>				   				    
				    <% } %>
				    
					<%--For displaying Next link --%>
				    <c:if test="${currentPage lt noOfPages}">
				        <a href="${pageurl}/${currentPage + 1}">Next</a>
				    </c:if>
				</div>								
			</c:otherwise>
		</c:choose>
	</div>
</div>

<jsp:include page="/jsp/includes/footer.jsp" flush="true" />