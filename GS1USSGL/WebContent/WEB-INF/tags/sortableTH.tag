<%@tag import="org.gs1us.sgg.gbservice.api.AttributeType"%>
<%@tag import="java.util.List"%>
<%@tag import="org.gs1us.sgl.webapp.SortPageManager"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="key" required="true" type="java.lang.String" %>
<%@ attribute name="spm" required="true" type="SortPageManager" %>
<%@ attribute name="heading" required="true" type="java.lang.String" %>


<th class='<%= spm.isSortedBy(key) ? "color-orange" : "" %>'>
	<%= heading %>
	<a href="<%= spm.getColumnLink(key) %>">
	  <c:choose>
	  	<c:when test="<%= spm.isSortedBy(key) && spm.isAscending() %>">
	  		<span class="icon-arrow_down_2"></span>
	  	</c:when>
	  	<c:when test="<%= spm.isSortedBy(key) && !spm.isAscending() %>">
	  		<span class="icon-arrow_up_2"></span>
	  	</c:when>
	  	<c:otherwise>
			<span class="icon-sort"></span>
	  	</c:otherwise>
	  </c:choose>
	</a>
</th>
