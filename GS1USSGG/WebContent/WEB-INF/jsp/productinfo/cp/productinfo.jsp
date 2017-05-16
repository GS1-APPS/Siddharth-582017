
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeSet"%>
<%@page import="org.gs1us.sgg.gbservice.api.Product"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	Map<String,String> uomTable = new HashMap<String,String>();
	uomTable.put("X_1N", "Count");
	uomTable.put("FTQ", "ft\u00b3");
	uomTable.put("MMQ", "mm\u00b3");
	uomTable.put("G21", "c");
	uomTable.put("E27", "dose");
	uomTable.put("DZN", "doz");
	uomTable.put("PTD", "pint");
	uomTable.put("EA",  "ea");
	uomTable.put("OZA", "fl\u00a0oz");
	uomTable.put("FOT", "ft");
	uomTable.put("GLL", "gal");
	uomTable.put("GRM", "g");
	uomTable.put("INH", "in");
	uomTable.put("KGM", "kg");
	uomTable.put("PTL", "pint");
	uomTable.put("QTL", "quart");
	uomTable.put("LTR", "l");
	uomTable.put("MTR", "m");
	uomTable.put("MGM", "mg");
	uomTable.put("MLT", "ml");
	uomTable.put("MMT", "mm");
	uomTable.put("NIU", "UI");
	uomTable.put("ONZ", "oz");
	uomTable.put("H87", "piece");
	uomTable.put("LBR", "lb");
	uomTable.put("QTD", "quart");
	uomTable.put("FTK", "ft\u00b2");
	uomTable.put("INK", "in\u00b2");
	uomTable.put("MTK", "m\u00b2");
	uomTable.put("MMK", "mm\u00b2");
	uomTable.put("YDK", "yard\u00b2");
	uomTable.put("E55", "use");
	uomTable.put("YRD", "yard");

	Product product = (Product)request.getAttribute("product");
	AttributeSet attributes = product.getAttributes();
	String brandOwnerName = attributes.getAttribute("brandOwnerName");
	String brandName = attributes.getAttribute("brandName");
	String subBrand = attributes.getAttribute("subBrand");
	String functionalName = attributes.getAttribute("functionalName");
	String tradeItemDescription = attributes.getAttribute("tradeItemDescription");
	String additionalTradeItemDescription = attributes.getAttribute("additionalTradeItemDescription");
	String netContent1 = attributes.getAttribute("netContent1");
	String netContent1Uom = uomTable.get(attributes.getAttribute("netContent1_uom"));
	String netContent2 = attributes.getAttribute("netContent2");
	String netContent2Uom = uomTable.get(attributes.getAttribute("netContent2_uom"));
	String netContent3 = attributes.getAttribute("netContent3");
	String netContent3Uom = uomTable.get(attributes.getAttribute("netContent3_uom"));
	String netContent4 = attributes.getAttribute("netContent4");
	String netContent4Uom = uomTable.get(attributes.getAttribute("netContent4_uom"));
	String descriptiveSize = attributes.getAttribute("descriptiveSize");
	String gpcCategoryCode = attributes.getAttribute("gpcCategoryCode");
	String informationProviderGLN = attributes.getAttribute("informationProviderGLN");
	String itemDataLanguage = attributes.getAttribute("itemDataLanguage");
	String mobileDeviceImage = attributes.getAttribute("uriProductImage");
	String trustmarkUrl = request.getContextPath() + "/images/trustmark_82x20.png";
	String imageUrl = mobileDeviceImage == null ? request.getContextPath() + "/images/placeholder_image.png" : mobileDeviceImage;
	String websiteUrl = attributes.getAttribute("uriProductWebsite");
	
%>
    
<jsp:include page="/WEB-INF/jsp/productinfo/cp/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Product" />
</jsp:include>



<section>
	<div id="product-mobileDeviceImage"><img src="<%= imageUrl %>" /></div>
    <div id="product-trustmark" class="pull-right"><img src="<%= trustmarkUrl %>" /></div>
    <h4>Brand owner</h4>
    <p><c:out value="<%= brandOwnerName %>" /></p>
    <h4>Brand name</h4>
    <p><c:out value="<%= brandName %>" /></p>
    <c:if test="<%= subBrand != null %>">
      <h4>Sub-brand</h4>
      <p><c:out value="<%= subBrand %>" /></p>
    </c:if>
    <c:if test="<%= functionalName != null %>">
      <h4>Functional product name</h4>
      <p><c:out value="<%= functionalName %>" /></p>
    </c:if>
    <h4>Product name</h4>
    <p><c:out value="<%= tradeItemDescription %>" /></p>
    <c:if test="<%= additionalTradeItemDescription != null %>">
      <h4>Description</h4>
      <p><c:out value="<%= additionalTradeItemDescription %>" /></p>
    </c:if>
    
    <c:if test="<%= netContent1 != null %>">
      <h4>Declared quantity / net content</h4>
      <p><c:out value="<%= netContent1 %>"/>&nbsp;<c:out value="<%= netContent1Uom %>"/>
      <c:if test="<%= netContent2 != null %>">
        <span class="color-light-medium-gray">|</span> <c:out value="<%= netContent2 %>"/>&nbsp;<c:out value="<%= netContent2Uom %>"/>
      </c:if>
      <c:if test="<%= netContent3 != null %>">
        <span class="color-light-medium-gray">|</span> <c:out value="<%= netContent3 %>"/>&nbsp;<c:out value="<%= netContent3Uom %>"/>
      </c:if>
      <c:if test="<%= netContent4 != null %>">
        <span class="color-light-medium-gray">|</span> <c:out value="<%= netContent4 %>"/>&nbsp;<c:out value="<%= netContent4Uom %>"/>
      </c:if></p>
    </c:if>
    
    <c:if test="<%= descriptiveSize != null %>">
      <h4>Size</h4>
      <p><c:out value="<%= descriptiveSize %>" /></p>
    </c:if>
    
    
    <h4>Product Classification</h4>
    <p><c:out value="<%= gpcCategoryCode %>" /></p>
    <h4>Information Provider GLN</h4>
    <p><c:out value="<%= informationProviderGLN %>" /></p>
    <h4>Language</h4>
    <p><c:out value="<%= itemDataLanguage %>" /></p>

    <c:if test="<%= websiteUrl != null %>">
      <h4>Web site</h4>
      <p><a href="<%= websiteUrl %>"><c:out value="<%= websiteUrl %>" /></a></p>
    </c:if>
 
</section>


<jsp:include page="/WEB-INF/jsp/productinfo/cp/footer.jsp" flush="true" />
