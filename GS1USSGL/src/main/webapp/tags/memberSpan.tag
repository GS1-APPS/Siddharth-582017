<%@tag import="org.gs1us.sgg.gbservice.api.AttributeType"%>
<%@tag import="java.util.List"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<%@ attribute name="member" required="true" type="org.gs1us.sgl.memberservice.Member" %>


<c:out value="<%= member.getCompanyName() %>" />
(GLN <%= member.getGln() %><c:if test='<%= member.getMemberId() != null %>'>, Member ID <c:out value='<%= member.getMemberId() %>'/></c:if>)