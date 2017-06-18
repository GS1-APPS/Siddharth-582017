<%@tag import="org.gs1us.sgg.gbservice.api.AttributeType"%>
<%@tag import="java.util.List"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="member" required="true" type="org.gs1us.sgl.memberservice.Member" %>


<td>
	<div>
		<c:out value="<%= member.getCompanyName() %>"/>
	</div>
	<div class="small color-light-medium-gray">
		GLN: <c:out value="<%= member.getGln() %>"/>
	</div>
	<c:if test="<%= member.getMemberId() != null %>">
		<div class="small color-light-medium-gray">Member ID: <c:out value="<%= member.getMemberId() %>"/></div>
	</c:if>
</td>

