<%@tag import="org.gs1us.sgg.gbservice.api.AttributeType"%>
<%@tag import="java.util.List"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="validationErrors" required="true" type="java.util.Map" %>
<%@ attribute name="name" required="true" type="java.lang.String" %>
<%@ attribute name="value" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>

	<%
	    { List<String> errorMessages = (List<String>)validationErrors.get(name);
	    String formGroupClass = errorMessages != null && errorMessages.size() > 0 ? "form-group has-error" : "form-group";
	    String formControlClass = errorMessages != null && errorMessages.size() > 0 ? "form-control alert-danger" : "form-control";
	%>
		<c:set var="value" value='<%=value%>'/>
	
	  	<div class='<%= formGroupClass %>'>
	    	<label><c:out value="<%= label %>"/></label>
	    	<input type="text" class="<%= formControlClass %>" name="<%= name %>" value='${fn:escapeXml(value)}'/>
	    	<c:if test='<%=errorMessages != null%>'>
	    		<%
	    		    for (String em : errorMessages) {
	    		%>
	    		<div class="form-control-feedback"><c:out value="<%=em%>"/></div>
	    		<%
	    		    }
	    		%>
	    	</c:if>
	  	</div>
	<%
	    }
	%>

