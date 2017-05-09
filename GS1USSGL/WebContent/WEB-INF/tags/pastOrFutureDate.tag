<%@tag import="org.gs1us.sgg.util.UserInputUtil"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeType"%>
<%@tag import="java.util.List"%>
<%@tag import="org.gs1us.sgl.webapp.SortPageManager"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="date" required="true" type="java.util.Date" %>
<%@ attribute name="asOfDate" required="true" type="java.util.Date" %>
<%@ attribute name="timeZoneId" required="true" type="java.lang.String" %>


<c:if test="<%= date != null %>">
<c:choose>
	<c:when test="<%= date.before(asOfDate) %>">
		<%= UserInputUtil.dateOnlyToString(date, timeZoneId) %>
	</c:when>
	<c:otherwise>
		<span class="color-dark-medium-gray">[<%= UserInputUtil.dateOnlyToString(date, timeZoneId) %>]</span>
	</c:otherwise>
</c:choose>
</c:if>