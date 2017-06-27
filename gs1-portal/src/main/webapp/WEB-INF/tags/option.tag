<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="test" required="true" type="java.lang.Boolean" %>
<%@ attribute name="value" required="true" type="java.lang.String" %>
<%@ attribute name="display" required="true" type="java.lang.String" %>

<c:choose>
<c:when test="<%= test %>">
<option selected="selected" value="<%= value %>"><c:out value="<%= display %>"/></option> 
</c:when>
<c:otherwise>
<option value="<%= value %>"><c:out value="<%= display %>"/></option> 
</c:otherwise>
</c:choose>