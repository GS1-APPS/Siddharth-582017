
<%@page import="org.gs1us.sgl.webapp.BillingReportController"%>
<%@page import="org.gs1us.sgl.webapp.SignupController"%>
<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.gs1us.sgg.clockservice.ClockService"%>
<%@page import="org.gs1us.sgl.webapp.TestController"%>
<%@page import="org.gs1us.sgl.webapp.BillingController"%>
<%@page import="org.gs1us.sgl.webapp.AccountController"%>
<%@page import="org.gs1us.sgl.webapp.AppController"%>
<%@page import="org.gs1us.sgl.webapp.ProductController"%>
<%@page import="org.gs1us.sgl.webapp.standalone.UserController"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page import="org.gs1us.sgl.memberservice.standalone.StandaloneUser"%>
<%@page import="org.gs1us.sgl.webapp.LogoutController"%>
<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.gs1us.sgl.webapp.HomeController"%>
<%@page import="org.gs1us.sgl.webapp.SearchController"%>
<%@page import="org.gs1us.sgl.webapp.ReportController"%>

<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<% String selectedItem = request.getParameter("selectedItem"); %>
<% Boolean fluidContainer = Boolean.parseBoolean(request.getParameter("fluid")); %>
<% String contextPath = request.getContextPath(); %>
<%
 	ClockService clockService = (ClockService)request.getAttribute("clockService");
    String searchUri;
	String homeUri = MvcUriComponentsBuilder.fromMethodName(HomeController.class, "home", (Object)null, (Object)null).toUriString();		
	String productSearchUri = MvcUriComponentsBuilder.fromMethodName(SearchController.class, "search", (Object)null).toUriString();	
	String reportsUri = MvcUriComponentsBuilder.fromMethodName(ReportController.class, "report", (Object)null).toUriString();	
	String loginUri = MvcUriComponentsBuilder.fromMethodName(LoginController.class, "login", (Object)null).toUriString();
	String logoutUri = MvcUriComponentsBuilder.fromMethodName(LogoutController.class, "logout", (Object)null).toUriString();
	String productsUri = MvcUriComponentsBuilder.fromMethodName(ProductController.class, "showProducts", (Object)null, (Object)null).toUriString();
	String agreementsUri = MvcUriComponentsBuilder.fromMethodName(SignupController.class, "agreementsGet", (Object)null, (Object)null).toUriString();
	String appsUri = MvcUriComponentsBuilder.fromMethodName(AppController.class, "showApps", (Object)null, (Object)null).toUriString();
	
	String userAccountUri = MvcUriComponentsBuilder.fromMethodName(UserController.class, "userAccount", (Object)null, (Object)null).toUriString();
	
	String accountUri = MvcUriComponentsBuilder.fromMethodName(AccountController.class, "showAccount", (Object)null, (Object)null).toUriString();
	String transactionsUri = MvcUriComponentsBuilder.fromMethodName(AccountController.class, "showTransactions", (Object)null, (Object)null).toUriString();
	String ordersUri = MvcUriComponentsBuilder.fromMethodName(AccountController.class, "showOrders", (Object)null, (Object)null).toUriString();
	String membersUri = MvcUriComponentsBuilder.fromMethodName(UserController.class, "showMembers", null, null).toUriString();
	String usersUri = MvcUriComponentsBuilder.fromMethodName(UserController.class, "showUsers", null, null).toUriString();
	String billingSummaryUri = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingSummary", (Object)null, (Object)null).toUriString();
    String billingOrdersUri = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "invoiceAndBillGet", null, null).toUriString();
    String billingInvoicedUri = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingInvoiced", null, null).toUriString();
    String billingBilledUri = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingBilled", null, null).toUriString();
    String billingUnpaidUri = MvcUriComponentsBuilder.fromMethodName(BillingController.class, "billingUnpaid", null, null).toUriString();
    String billingReportUri = MvcUriComponentsBuilder.fromMethodName(BillingReportController.class, "billingReportGet", null, null, null, null).toUriString();

	//String feedbackUri = MvcUriComponentsBuilder.fromMethodName(FeedbackController.class, "feedbackGet", (Object)null).toUriString();
	String tickUri = MvcUriComponentsBuilder.fromMethodName(TestController.class, "tick", (Object)null, (Object)null, null).toUriString();
	String tomorrowUri = MvcUriComponentsBuilder.fromMethodName(TestController.class, "tomorrow", (Object)null, (Object)null, null).toUriString();
	String setTimeUri = MvcUriComponentsBuilder.fromMethodName(TestController.class, "setTimeGet", (Object)null).toUriString();
	String dmverifyUri = MvcUriComponentsBuilder.fromMethodName(TestController.class, "testGet", (Object)null, (Object)null, "dmVerify", null).toUriString();
	
%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The meta tag below can not be altered or removed -->
        <meta name="gs1-template" content="gs1-v1.0"/>
        <meta name="format-detection" content="telephone=no">
        <title><%= WebappUtil.longProductName() %> - <%= request.getParameter("pageTitle") %></title>
        
        <link rel='SHORTCUT ICON' href='<%= contextPath + "/images/favicon.ico" %>' type='image/x-icon' />

        <link rel="stylesheet" type="text/css" href="//cloud.typography.com/6247692/752844/css/fonts.css" />
        <link href='//fonts.googleapis.com/css?family=Source+Code+Pro:500' rel='stylesheet' type='text/css' />

        <link href="<%= contextPath + "/css/bootstrap.min.css" %>" rel="stylesheet" />
        <link href="<%= contextPath + "/css/bootstrap-theme.min.css" %>" rel="stylesheet" />
        <link href="<%= contextPath + "/css/datepicker3.css" %>" rel="stylesheet" />
        <link href="<%= contextPath + "/css/bootstrap-accessibility.min.css" %>" rel="stylesheet" />

        <link href="<%= contextPath + "/css/ae.css" %>" rel="stylesheet" />
                <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
                <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<%= request.getContextPath() + "/js/jquery.min.js" %>"></script>
        
    </head>

    <body>
    <!-- Google Tag Manager -->
<noscript><iframe src="//www.googletagmanager.com/ns.html?id=GTM-P2JJVS"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0], j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-P2JJVS');</script>
<!-- End Google Tag Manager -->
    
        <div class="toolkit">
		<header>
			<div class="header-top">
				<div class="container">
					<div class="logo">
						<div class="logo1">
							<a href="<%= homeUri %>"><img
								src="<%= contextPath + "/images/" + WebappUtil.productLogoFilename() %>"
								alt="<%= WebappUtil.productLogoAlt() %>" /></a>
							<p class="large hidden-xs hidden-sm">
							    <!-- 
								<img id="ae-banner-image" src="<%= contextPath + "/images/banner-textual.png" %>" alt="<%= WebappUtil.longProductName() %>" />
								 -->
								 <!-- 
								 <span class="ae-banner-text"><span class="ae-banner-company"><%= WebappUtil.productOperator() %></span> <span class="ae-banner-product"><%= WebappUtil.shortProductName() %></span></span>
							     -->
							     <strong><%= WebappUtil.productBrand() %></strong><br/>
                                    <span class="color-orange"><%= WebappUtil.shortProductHtml() %></span>
							</p>
						</div>
					</div>
					<div class="page-flag-small color-white"><a class="color-white" href="<%= WebappUtil.homeLinkUrl() %>"><%= WebappUtil.homeLinkText() %></a></div>
					
					<div class="secondary-nav">
						<ul class="list-inline">
								<c:if test='<%= request.isUserInRole("ROLE_TESTER") && clockService != null %>'>
									<li class="color-orange"><c:out value="<%= clockService.now() %>" /></li>
								</c:if>
							<!--
							<% if (request.getUserPrincipal() != null) { %>
								<li><a href="<%= logoutUri %>">Logout</a></li>

 							<% } %>
							-->
 							<!-- 
							<li><a href="/ui/resources">Resources</a></li>
							<% if (request.getUserPrincipal() != null) { %>
								<li><a href="/ui/feedback">Help</a></li>
							<% } %>
							 -->
						</ul>

						<form action="<%= "TODO" %>" method="get" class="form-group form-search-box">
							<div class="input-wrapper glyphicon-search">
							    <% String searchBoxValue = (String)request.getAttribute("searchBoxValue"); 
							       if (searchBoxValue != null) { %>
								     <input type="text" class="form-control" placeholder="Search" name="q" value="<%= searchBoxValue %>" />
								   <% } else { %>
								     <input type="text" class="form-control" placeholder="Search" name="q"  />
								<% } %>
							</div>
							<div>
								<button type="submit" class="btn-search">
									<span class="icon-arrow_right"></span>
								</button>
							</div>
						</form>

							<% if (request.getUserPrincipal() != null) { 
							    StandaloneUser user = (StandaloneUser)((Authentication)request.getUserPrincipal()).getPrincipal();
							    String greeting = user.getSalutation();
							%>
														
                                <div class="btn-group">
                                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                        Hello, <%= greeting %>
                                        <!-- <span class="badge">4</span> -->
                                        <span class="icon-arrow_down"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu">
				        <!--
                                        <li><h4>My Account</h4></li>
                                        <li><a href="0">You have <span class="color-orange">4</span> product notifications</a></li>
                                        <li class="divider"></li>
                                        <li><h4>Account Actions</h4></li>
					-->
                                        <li><a href="<%= logoutUri %>">Log Out</a></li>
                                    </ul>
                                </div>
<!--
								<span>&nbsp;&nbsp;&nbsp;Welcome, <%= greeting %></span>
-->
							<% }  else { %>
						<a class="btn-primary login-button" role="button" href="<%= loginUri %>"><span
							class="icon-user"></span> Login</a>
							<% } %>
					</div>
				</div>
			</div>
			<div class="navigation">
				<div class="container">
					<nav class="navbar yamm navbar-default" role="navigation">
						<div class="navbar-header">
							<div class="logo1 visible-xs">
								<a href="<%= homeUri %>"><img src="<%= contextPath + "/images/" + WebappUtil.productLogoFilename() %>"
									width="72" height="59" border="0" alt="<%= WebappUtil.productLogoAlt() %>" /></a>
							</div>
							<button type="button" data-toggle="collapse"
								data-target="#navbar-collapse-example"
								class="navbar-toggle collapsed">
								<span class="icon-bar"></span><span class="icon-bar"></span><span
									class="icon-bar"></span>
							</button>
							<div class="secondary-nav">
								<a class="btn-primary login-button" role="button" href="<%= loginUri %>"><span
									class="icon-user"></span> Login</a> <a class="search" href="#0"><span
									class="icon-search"></span></a>
							</div>
						</div>
						<div id="navbar-collapse-example" class="navbar-collapse collapse">
							<ul class="nav navbar-nav">
								<li class='<%= selectedItem.equals("home") ? "active" : "" %>'><a href="<%= homeUri %>">Home</a></li>
								
								<li class='<%= selectedItem.equals("productSearch") ? "active" : "" %>'><a href="<%= productSearchUri %>">Search</a></li>
								
								<c:if test='<%= request.isUserInRole("ROLE_USER") %>'>
									<li class='<%= selectedItem.equals("products") ? "active" : "" %>'><a href="<%= productsUri %>">Products</a></li>
								</c:if>

								<c:if test='<%= request.isUserInRole("ROLE_USER") %>'>
									<li class='<%= selectedItem.equals("userAccount") ? "active" : "" %>'><a href="<%= userAccountUri %>">Account</a></li>
								</c:if>
								
								<!-- 
								<c:if test='<%= request.isUserInRole("ROLE_USER") %>'>
									<li class='<%= selectedItem.equals("apps") ? "active" : "" %>'><a href="<%= appsUri %>">Apps</a></li>
								</c:if>
								
								<c:if test='<%= request.isUserInRole("ROLE_USER") %>'>
									<li class='<%= selectedItem.equals("account") ? "active" : "" %>'><a href="<%= accountUri %>">Purchases</a></li>
								</c:if>
								<c:if test='<%= request.isUserInRole("ROLE_USER") %>'>
									<li class='<%= selectedItem.equals("agreements") ? "active" : "" %>'><a href="<%= agreementsUri %>">Agreements</a></li>
								</c:if>
								-->
																
								<!-- 
								<c:if test='<%= request.isUserInRole("ROLE_USER") %>'>
									<li class='<%= (selectedItem.equals("account") ? "active" : "") + " dropdown " %>'><a data-toggle="dropdown" class="dropdown-toggle" href="#">Account</a>
										<ul class="dropdown-menu"><li>
										  <div class="col-sm-3 column">
											<h4><a href="<%= ordersUri %>">Orders</a></h4>
											<h4><a href="<%= transactionsUri %>">Transactions</a></h4>
										  </div>
										</li></ul>
									</li>
								</c:if>
								 -->
								
								<c:if test='<%= request.isUserInRole("ROLE_ADMIN") %>'>
									<li class='<%= selectedItem.equals("members") ? "active" : "" %>'><a href="<%= membersUri %>">Members</a></li>
								</c:if>
								<c:if test='<%= request.isUserInRole("ROLE_ADMIN") %>'>
									<li class='<%= selectedItem.equals("users") ? "active" : "" %>'><a href="<%= usersUri %>">Users</a></li>
								</c:if>
								<c:if test='<%= request.isUserInRole("ROLE_ADMIN") %>'>
									<li class='<%= selectedItem.equals("reports") ? "active" : "" %>'><a href="<%= reportsUri %>">Admin Reports</a></li>
								</c:if>
																
								<!-- 
								<c:if test='<%= request.isUserInRole("ROLE_ADMIN") %>'>
									<li class='<%= selectedItem.equals("billing") ? "active" : "" %>'><a href="#">Billing</a></li>
								</c:if>
								 -->
								<!--  
								<c:if test='<%= request.isUserInRole("ROLE_ADMIN") %>'>
									<li class='<%= (selectedItem.equals("billing") ? "active" : "") + " dropdown " %>'><a data-toggle="dropdown" class="dropdown-toggle" href="#">Billing</a>
										<ul class="dropdown-menu"><li>
										  <div class="col-sm-3 column">
											<h4><a href="<%= billingSummaryUri %>">Action center</a></h4>
											<h4><a href="<%= billingOrdersUri %>">Invoice new orders</a></h4>
											<h4><a href="<%= billingInvoicedUri %>">Bill out invoices</a></h4>
											<h4><a href="<%= billingBilledUri %>">Record invoice payments</a></h4>
											<h4><a href="<%= billingUnpaidUri %>">Record payments received</a></h4>
											<h4><a href="<%= billingReportUri %>">Reports</a></h4>
										  </div>
										</li></ul>
									</li>
								</c:if>
								 -->
								 
								<c:if test='<%= request.isUserInRole("ROLE_TESTER") %>'>
									<li class='<%= (selectedItem.equals("test") ? "active" : "") + " dropdown " %>'><a data-toggle="dropdown" class="dropdown-toggle" href="#">Test</a>
										<ul class="dropdown-menu"><li>
										  <div class="col-sm-3 column">
											<h4><a href="<%= tickUri %>">Tick</a></h4>
											<h4><a href="<%= tomorrowUri %>">Tomorrow</a></h4>
											<h4><a href="<%= setTimeUri %>">Set Time</a></h4>
											<h4><a href="#">Reset DB</a></h4>
											<h4><a href="<%= dmverifyUri %>">Digimarc Verify</a></h4>
										  </div>
										</li></ul>
									</li>
								</c:if>
							</ul>
						</div>
					</nav>
				</div>
			</div>
		</header>

		<% if (fluidContainer) { %>
			<div class="container-fluid site-content">
		<% } else { %>
			<div class="container site-content">
		<% } %>
