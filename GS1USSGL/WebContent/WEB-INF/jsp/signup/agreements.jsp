<%@page import="org.gs1us.sgg.util.UserInputUtil"%>
<%@page import="java.util.Date"%>
<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.gs1us.sgl.serviceterms.TermsOfService"%>
<%@page import="org.gs1us.sgl.webapp.LoginController"%>
<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>
<%@page import="org.springframework.web.util.UriComponentsBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
	String errorMessage = (String)request.getAttribute("errorMessage");

	Date brandOwnerLicenseAgreementDate = (Date)request.getAttribute("brandOwnerLicenseAgreementDate");
	Date digimarcAgreementDate = (Date)request.getAttribute("digimarcAgreementDate");
	String brandOwnerAgreementActionUrl = (String)request.getAttribute("brandOwnerAgreementActionUrl");
	String digimarcActionUrl = (String)request.getAttribute("digimarcActionUrl");
	String timeZoneId = (String)request.getAttribute("timeZoneId");
	
	String bolaUrl = request.getContextPath() + "/files/BOLA-20160428.pdf";
	String dmaUrl  = request.getContextPath() + "/files/DMA-20160430.pdf";
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Agreements" />
  <jsp:param name="selectedItem" value="agreements" />
</jsp:include>

<c:if test="<%= errorMessage != null %>">
  <h3></h3>
  <div class="alert alert-danger">
     <%= errorMessage %>
  </div>
</c:if>

<section>
    <h1>Licenses and Agreements</h1>
    <p>Welcome! To use the <%= WebappUtil.longProductHtml() %> you must review and accept the following agreements.</p>
    
    <hr>

    <div class="row">
        <div class="col-sm-10">
            <h3 class="color-orange"><%= WebappUtil.productOperator() %> Brand Owner License Agreement</h3>
            <c:choose>
	            <c:when test="<%= brandOwnerLicenseAgreementDate == null %>">
		            <form id="brandOwner" method="post" action="<%= brandOwnerAgreementActionUrl %>">
		                <input type="checkbox" name="agreed">&nbsp;&nbsp;&nbsp;<label for="">By checking this box, you are agreeing to the <a target="_blank" href="<%= bolaUrl %>"><%= WebappUtil.productOperator() %> Brand Owner License Agreement</a></label><br><br>
		
		                <button class="btn-large btn-primary btn-margin" disabled="disabled">Submit</button>
		                  <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
		            </form>
	            </c:when>
	            <c:otherwise>
		                <div><input type="checkbox" checked="checked" disabled="disabled">&nbsp;&nbsp;&nbsp;<label for="">By checking this box, you are agreeing to the <a target="_blank" href="<%= bolaUrl %>"><%= WebappUtil.productOperator() %> Brand Owner License Agreement</a></label><br><br>
						</div>
		                <div>You agreed to the <%= WebappUtil.productOperator() %> Brand Owner License Agreement on <%= UserInputUtil.dateOnlyToString(brandOwnerLicenseAgreementDate, timeZoneId) %>.</div>
	            </c:otherwise>
            </c:choose>
        </div>
        <div class="col-sm-2">
            <img class="img-responsive pull-right" src="<%= request.getContextPath() + "/images/logo-us-96.png" %>" alt="GS1" >
        </div>
    </div>

    <hr>

    <div class="row">
        <div class="col-sm-10">
            <h3 class="color-orange"><strong>DWC</strong>ode Terms of Service</h3>
            <c:choose>
	            <c:when test="<%= brandOwnerLicenseAgreementDate == null %>">

		            <div>
		                <input type="checkbox" disabled="disabled">&nbsp;&nbsp;&nbsp;<label for="">By checking this box, you are agreeing to the <a target="_blank" href="<%= dmaUrl %>">DWCode Terms of Service</a></label><br><br>
					</div>
					<div>
		                You must first agree to the <%= WebappUtil.productOperator() %> Brand Owner License Agreeement, above, before you can agree to the DWCode Terms of Service.
		            </div>

	            </c:when>
	            <c:when test="<%= digimarcAgreementDate == null %>">
		            <form id="digimarc" method="post" action="<%= digimarcActionUrl %>">
		                <input type="checkbox" name="agreed">&nbsp;&nbsp;&nbsp;<label for="">By checking this box, you are agreeing to the <a target="_blank" href="<%= dmaUrl %>">DWCode Terms of Service</a></label><br><br>
		                <button class="btn-large btn-primary btn-margin">Submit</button>
		                  <input type="hidden"
	name="${_csrf.parameterName}"
	value="${_csrf.token}"/>
		            </form>
	            </c:when>
	            <c:otherwise>
		                <div><input type="checkbox" checked="checked" disabled="disabled">&nbsp;&nbsp;&nbsp;<label for="">By checking this box, you are agreeing to the <a target="_blank" href="<%= dmaUrl %>">DWCode Terms of Service</a></label><br><br>
						</div>
		                <div>You agreed to the DWCode Terms of Service on <%= UserInputUtil.dateOnlyToString(digimarcAgreementDate, timeZoneId) %>.</div>
	            </c:otherwise>
            </c:choose>
        </div>
        <div class="col-sm-2">
            <img class="img-responsive pull-right" src="<%= request.getContextPath() + "/images/digimarc-96.png" %>" alt="Digimarc">
        </div>
<!-- 
        <div class="col-md-12 btn-margin">To learn more about the pricing and process for applying DWCodes to your products, click <a target="_blank" href="https://www.digimarc.com/gs1-us-dwcode">here</a>.</div>
 -->
    </div>
</section>

<script>
$("#brandOwner > input").on("click", function(){
	var checked = this.checked;
	$("#brandOwner > button").prop("disabled", !checked);
});
$("#digimarc > input").on("click", function(){
	var checked = this.checked;
	$("#digimarc > button").prop("disabled", !checked);
});
$("#digimarc").submit(function(event){
	$("#digimarc > button").prop("disabled", true).addClass("bg-orange-disabled");
});
</script>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
