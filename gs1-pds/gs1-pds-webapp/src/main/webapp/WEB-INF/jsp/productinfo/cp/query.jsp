
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeSet"%>
<%@page import="org.gs1us.sgg.gbservice.api.Product"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
	String gtin = (String)request.getAttribute("gtin");
    if (gtin == null)
        gtin = "";
	String actionUrl = request.getContextPath() + "/product/query";
	
%>
    
<jsp:include page="/src/main/webapp/WEB-INF/jsp/productinfo/cp/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Product" />
</jsp:include>



<section>
<form method="get" action="<%= actionUrl %>">
  <div class="form-group">
    <label>GTIN</label>
    <input type="text" class="form-control" value="<%= gtin %>" name="gtin"/>
  </div>
</form>
 
</section>


<jsp:include page="/src/main/webapp/WEB-INF/jsp/productinfo/cp/footer.jsp" flush="true" />
