<%@tag import="org.gs1us.sgl.memberservice.Member"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%@ attribute name="member" required="true" type="Member" %>

<div><c:out value="<%= member.getCompanyName() %>"/></div>
<c:if test="<%= member.getAddress1() != null %>" >
	<div class="small color-dark-medium-gray"><c:out value="<%= member.getAddress1() %>"/></div>
</c:if>
<c:if test="<%= member.getAddress2() != null %>" >
	<div class="small color-dark-medium-gray"><c:out value="<%= member.getAddress2() %>"/></div>
</c:if>
<c:if test="<%= member.getCity() != null || member.getState() != null || member.getPostalCode() != null %>" >
<div class="small color-dark-medium-gray">
	<c:if test="<%= member.getCity() != null %>">
		<c:out value="<%= member.getCity() %>"/>
	</c:if>
	<c:if test="<%= member.getState() != null %>">
		<c:out value="<%= member.getState() %>"/>
	</c:if>
	<c:if test="<%= member.getPostalCode() != null %>">
		<c:out value="<%= member.getPostalCode() %>"/>
	</c:if>
</div>
</c:if>
<div class="small color-dark-medium-gray">GLN:&nbsp;<%= member.getGln() %></div>
<c:if test="<%= member.getMemberId() != null %>">
	<div class="small color-dark-medium-gray">Member&nbsp;ID: <c:out value="<%= member.getMemberId() %>"/></div>
</c:if>
