<%@page import="java.util.Map"%>
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
	String upcaUrl  = request.getContextPath() + "/images/barcode-upca.png";
	String ean13Url = request.getContextPath() + "/images/barcode-ean13.png";
	String ean8Url  = request.getContextPath() + "/images/barcode-ean8.png";
	String upceUrl  = request.getContextPath() + "/images/barcode-upce.png";
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="About" />
  <jsp:param name="selectedItem" value="home" />
</jsp:include>


<h1>What is a UPC-E barcode?</h1>
<div class="row">
<div class="col-md-12">

<p>Most packages for sale in the US are marked with a UPC-A barcode symbol. 
For certain products, typically smaller sized packages, a UPC-E barcode symbol may be used instead.
</p>
</div>
</div>
<div class="row">
<div class="col-md-3">
<div class="text-center"><strong>UPC-A</strong></div>
<div class="text-center"><img src="<%= upcaUrl %>" /></div>
</div>
<div class="col-md-3">
<div class="text-center"><strong>UPC-E</strong></div>
<div class="text-center"><img src="<%= upceUrl %>" /></div>
</div>
</div>

<div class="row">
<div class="col-md-12">

<p>If your product is marked with a UPC-E, you must check the UPC-E box when creating a DWCode. 
This allows the DWCode to behave correctly when scanned at point-of-sale. 
</p>
<p>If your product is marked with a UPC-A, do not check the UPC-E box. 
If you find you have made an incorrect selection, you can change it at any time.
</p>
<p>
To determine whether your product is marked with a UPC-E, look at the barcode on your product's package and compare it to the samples above.
</p>

<h2>How to populate the GTIN field in the DWCode portal</h2>

</div>
</div>
<div class="row">
<div class="col-md-5">
<div class="text-center"><strong>UPC-A</strong></div>
<div class="text-center"><img src="<%= upcaUrl %>" /></div>
<ul>
<li><strong>Do not</strong> check the UPC-E box.</li>
<li>Enter all 12 digits into the GTIN box.</li>
</ul>
</div>
<div class="col-md-7">
<div class="text-center"><strong>UPC-E</strong></div>
<div class="text-center"><img src="<%= upceUrl %>" /></div>
<ul>
<li>Note that the first digit of a UPC-E is always a zero.</li>
<li><strong>Do</strong> check the UPC-E box.</li>
<li>Enter the full 12 digit GTIN (not just the eight digits printed beneath the barcode)</li>
</ul>
</div>
</div>

<div class="row">
<div class="col-md-12">
<h2>If your product is imported from outside the US</h2>
<p>Finally, if your product is imported from outside the US, it may carry either an EAN-8 or EAN-13 barcode.<p>
</div>
</div>

<div class="row">
<div class="col-md-5">
<div class="text-center"><strong>EAN-13</strong></div>
<div class="text-center"><img src="<%= ean13Url %>" /></div>
<ul>
<li><strong>Do not</strong> check the UPC-E box.</li>
<li>Enter all 13 digits into the GTIN box.</li>
</ul>
</div>
<div class="col-md-7">
<div class="text-center"><strong>EAN-8</strong></div>
<div class="text-center"><img src="<%= ean8Url %>" /></div>
<ul>
<li>Note that the first digit of an EAN-8 is never a zero.</li>
<li><strong>Do not</strong> check the UPC-E box.</li>
<li>Enter all 8 digits into the GTIN box.</li>
</ul>
</div>
</div>





<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />