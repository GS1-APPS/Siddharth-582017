<%@page import="org.gs1us.sgl.webapp.AccountController"%>
<%@page import="org.gs1us.sgl.webapp.standalone.UserController"%>
<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="java.util.Collection"%>
<%@page import="org.gs1us.sgl.webapp.ProductController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.gs1us.sgl.webapp.SignupController"%>
<%@page import="org.gs1us.sgg.gbservice.api.Product"%>
<%@page import="java.util.List"%>
<%@page import="org.gs1us.sgg.gbservice.api.GBAccount"%>
<%@page import="java.security.Principal"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    

<%
	Principal user = request.getUserPrincipal();
	String gbAccountGln = (String)request.getAttribute("gbAccountGln");
	Collection<Product> products = (Collection<Product>)request.getAttribute("products");
	int productCount = products == null ? 0 : products.size();
	String signupUrl = MvcUriComponentsBuilder.fromMethodName(SignupController.class, "agreementsGet", (Object)null, (Object)null).toUriString();
	
	String showProductsUrl = MvcUriComponentsBuilder.fromMethodName(ProductController.class, "showProducts", (Object)null, (Object)null).toUriString();
	String newProductUrl = MvcUriComponentsBuilder.fromMethodName(ProductController.class, "newProductGet", (Object)null, (Object)null).toUriString();
	
	String accountUrl = MvcUriComponentsBuilder.fromMethodName(AccountController.class, "showAccount", (Object)null, (Object)null).toUriString();
		
	String loginUrl = MvcUriComponentsBuilder.fromMethodName(LoginController.class, "login", (Object)null).toUriString();
	String forgotPasswordUrl = MvcUriComponentsBuilder.fromMethodName(UserController.class, "forgotPasswordGet", (Object)null).toUriString();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Home" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>

<c:choose>
<c:when test="<%= user == null %>">
  <section>
      <h1>Welcome to the <%= WebappUtil.longProductHtml() %></h1>
      <p>Use this portal to register your products.</p>
      
  </section>
  <section>
      <div class="row">
          <div class="col-sm-5">
              <div class="content-module-wrapper top-border-orange">
                  <div class="content-module content-module-padding">
                      <h4>Existing User</h4>
                      <p>Log in to register products.</p>
                      <a href="<%= loginUrl %>" class="btn-lg btn-margin btn-primary" role="button">Log in to <%= WebappUtil.shortProductHtml() %></a><br><br>
                      <a href="<%= forgotPasswordUrl %>">Forgot your password?</a>
                  </div>
              </div>
          </div>
      </div>
  </section>

</c:when>
<c:when test="<%= gbAccountGln == null %>">

      <h1>Welcome to the <%= WebappUtil.longProductHtml() %></h1>
      <p>Use this portal to register your products.</p>

<p><span class="large color-orange icon-warning_sign"></span> You're almost ready to go! But first, click the link below.</p>
<p>&gt;&nbsp;&nbsp;<a href="<%= signupUrl %>">Review and accept agreements on behalf your company to use the <%= WebappUtil.shortProductHtml() %>.</a></p>

</c:when>
<c:when test="true">
</div>
                <div class="bg-orange">
                    <div class="container">
                        <div class="row">
                            <div class="col-sm-12">
                                <h1 class="color-white">Welcome to the <%= WebappUtil.longProductHtml() %></h1>
                                <p class="color-blue">This portal is designed to help you register products, plus add or manage product attributes around an existing GTIN.</p>
                                <p><a href="<%= newProductUrl %>" role="button" class="btn-large btn-primary btn-margin bg-blue">Register a new product</a></p>
                            </div> 
                            <!-- 
                            <div class="col-sm-6"><img class="img-responsive pull-right" src="<%= request.getContextPath() + "/images/page-7-featured.jpg" %>" alt="<strong>DW</strong>Code Portal"></div>
 -->
                         </div>
                    </div>
                </div>
                 
                <div class="bg-light-gray">
                    <div class="container">
                        <section >
                            <div class="row">
                                <div class="col-sm-7">
                                    <div class="content-module-wrapper top-border-orange">
                                        <div class="content-module content-module-padding">
                                            <h4 class="color-blue">Products</h4>
                                            <p>You have <%= productCount %> <%= productCount == 1 ? "product" : "products" %> registered with the <%= WebappUtil.shortProductHtml() %>:</p>
                                            <!-- 
                                            <ul class="standard">
                                                <li>You have <span class="color-orange">2</span> products with upcoming registration renewal dates.</li>
                                                <li>You have <span class="color-orange">1</span> product with a pending transaction.</li>
                                                <li>You have <span class="color-orange">1</span> product with an unpaid payment.</li>
                                            </ul>
											-->
                                            <p>
                                                <a href="<%= showProductsUrl %>">Show all registered products &gt;</a><br>
                                                <a href="<%= newProductUrl %>">Register a new product &gt;</a>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <!-- 
                                <div class="col-sm-4">
                                    <div class="content-module-wrapper top-border-blue">
                                        <div class="content-module content-module-padding">
                                            <h4 class="color-blue">Purchases</h4>
                                            <p>Review your transaction history.</p>
                                            <a href="<%= accountUrl %>">View all purchases and balances &gt;</a>
                                        </div>
                                    </div>
                                </div>
                                 -->
                            </div>
                        </section>
                    </div>
                </div>
<div>
</c:when>
<c:when test="<%= productCount == 0 %>">
<h3>Welcome to the <%= WebappUtil.longProductHtml() %>!</h3>

<p>
The <%= WebappUtil.longProductHtml() %> lets you register your products and data attributes.
</p>

<p>You do not yet have any products registered with the <%= WebappUtil.shortProductHtml() %>.</p>
<p>&gt;&nbsp;&nbsp;<a href="<%= newProductUrl %>">Register your first product</a></p>
</c:when>
<c:otherwise>
<h3>Welcome to the <%= WebappUtil.longProductHtml() %>!</h3>

<p>
The <%= WebappUtil.longProductHtml() %> lets you register your products and data attributes.
</p>

<p>You have <%= productCount %> <%= productCount == 1 ? "product" : "products" %> registered with the <%= WebappUtil.shortProductHtml() %>.</p>
<p>&gt;&nbsp;&nbsp;<a href="<%= showProductsUrl %>">Show all registered products</a></p>
</c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />