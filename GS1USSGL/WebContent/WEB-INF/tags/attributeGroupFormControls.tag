<%@tag import="java.util.Map"%>
<%@tag import="org.springframework.security.core.Authentication"%>
<%@tag import="org.gs1us.sgl.memberservice.User"%>
<%@tag import="org.gs1us.sgg.util.UserInputUtil"%>
<%@tag import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeEnumValue"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeType"%>
<%@tag import="java.util.List"%>
<%@tag import="org.gs1us.sgg.gbservice.api.AttributeDesc"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="moduleDesc" required="true" type="org.gs1us.sgg.gbservice.api.ModuleDesc" %>
<%@ attribute name="parameters" required="true" type="java.util.Map" %>
<%@ attribute name="validationErrors" required="true" type="java.util.Map" %>
<%@ attribute name="appAction" required="true" type="org.gs1us.sgg.gbservice.api.Action" %>


  	<%
  		if (moduleDesc != null && moduleDesc.getUserAttributeDescs() != null) {

  		    String previousGroupHeading = null;
 	  	    for (AttributeDesc attrDesc : moduleDesc.getUserAttributeDescs()) { 
 	  	 	  	 	  	  	    String[] valueHolder = parameters == null ? null : ((Map<String,String[]>)parameters).get(attrDesc.getName());
 	  	 	  	 	  	  	    String value = (valueHolder == null ? null : valueHolder[0]);
 	  	 	  	 	  	  	    List<String> errorMessages = (List<String>)validationErrors.get(attrDesc.getName());
 	  	 	  	 	  	  	    boolean hasErrors = errorMessages != null && errorMessages.size() > 0;
 	  	 	  	 	  	  	    String formControlClass = hasErrors ? "form-control alert-danger" : "form-control";
 	  	 	  	 	  	  	    String thisGroupHeading = attrDesc.getGroupHeading();
 	  	 	  	 	  	  	    String errorCssClass = "form-control-feedback";
 	  	 	  	 	  	  	    if (appAction.matches(attrDesc.getActions())) {
 	  	%>
 	  	<c:if test="<%= thisGroupHeading != null && !thisGroupHeading.equals(previousGroupHeading) %>">
 	  		<h3 class="color-orange"><c:out value="<%= thisGroupHeading %>" /></h3>
 	  	</c:if>
	  	<div class='<%=errorMessages != null && errorMessages.size() > 0 ? "form-group has-error" : "form-group"%>'>
	  		<c:if test="<%=attrDesc.getType() != AttributeType.AFFIRMATION && attrDesc.getType() != AttributeType.BOOLEAN %>">
	    	  <label><c:out value="<%=attrDesc.getTitle()%>"/><c:if test="<%=attrDesc.isRequired()%>"> (required)</c:if></label>
	    	</c:if>
	    	<c:set var="value" value='<%=value%>'/>
	    	<c:choose>
	    		<c:when test="<%=attrDesc.getType() == AttributeType.DATE%>">
	    			<div class="form-date-picker input-group date" placeholder="mm/dd/yyyy">
      					<input type="text" class="<%= formControlClass %>" name="<%= attrDesc.getName() %>" value='${fn:escapeXml(value)}' />
      					<span class="input-group-addon glyphicon glyphicon-calendar"></span>
    				</div>
	    		</c:when>
	    		<c:when test="<%=attrDesc.getType() == AttributeType.AFFIRMATION || attrDesc.getType() == AttributeType.BOOLEAN%>">
	    			<% errorCssClass += " alert-danger"; %>
	    			<div>
	    				<c:choose>
	    					<c:when test="<%= attrDesc.getType() == AttributeType.BOOLEAN && value != null && value.length() > 0 %>">
				 				<input type="checkbox" checked="checked" name="<%=attrDesc.getName()%>"  />
			 				</c:when>
			 				<c:otherwise>
				 				<input type="checkbox" name="<%=attrDesc.getName()%>"  />
			 				</c:otherwise>
		 				</c:choose>
				 		<%= WebappUtil.markdown(attrDesc.getTitle())%>
			 		</div>
	    		</c:when>
	    		<c:when test="<%=attrDesc.getType() == AttributeType.MEASUREMENT%>">
	    			<% { String[] uomValueHolder = parameters == null ? null : ((Map<String,String[]>)parameters).get(attrDesc.getName() + "_uom"); 
	    			     String uomValue = uomValueHolder == null ? null : uomValueHolder[0]; %>
	    			<div>
	    			<input type="text" class="<%= formControlClass + " sg-measurement-value"%>" name="<%= attrDesc.getName() %>" value='${fn:escapeXml(value)}' />
	    			
	    			<select class="<%= formControlClass + " sg-measurement-unit"%>" name='<%= attrDesc.getName() + "_uom" %>'>
	    				<option Value="">Choose unit...</option>
						<% for (AttributeEnumValue uom : attrDesc.getEnumValues()) { %>
							<c:choose>
								<c:when test="<%= uom.getCode().equals(uomValue) %>">
									<option selected="selected" value="<%= uom.getCode() %>"><c:out value="<%= uom.getDisplayName() %>" /></option>
								</c:when>
								<c:otherwise>
									<option value="<%= uom.getCode() %>"><c:out value="<%= uom.getDisplayName() %>" /></option>
								</c:otherwise>
							</c:choose>
						<% } %>
					</select>
					</div>
					<% } %>
	    		</c:when>
	    		<c:when test="<%=attrDesc.getType() == AttributeType.ENUM%>">
	    			<div>
	    			
	    			<select class="<%= formControlClass %>" name='<%= attrDesc.getName() %>'>
	    				<option Value="">Choose...</option>
						<% for (AttributeEnumValue enumValue : attrDesc.getEnumValues()) { %>
							<c:choose>
								<c:when test="<%= enumValue.getCode().equals(value) %>">
									<option selected="selected" value="<%= enumValue.getCode() %>"><c:out value="<%= enumValue.getDisplayName() %>" /></option>
								</c:when>
								<c:otherwise>
									<option value="<%= enumValue.getCode() %>"><c:out value="<%= enumValue.getDisplayName() %>" /></option>
								</c:otherwise>
							</c:choose>
						<% } %>
					</select>
					</div>
	    		</c:when>
	    		<c:otherwise>
	    			<input type="text" class="<%= formControlClass %>" name="<%= attrDesc.getName() %>" value='${fn:escapeXml(value)}' />
	    		</c:otherwise>
	    	</c:choose>
	    	<c:if test="<%= errorMessages != null %>">
	    	<% for (String em : errorMessages) { %>
	    	<div class="<%= errorCssClass %>"><span class="icon-warning_sign"></span>&nbsp;<c:out value="<%= em %>"/></div>
	    	<% } %>
	    	</c:if>
	    	<div class="sgl-product-attribute-instr"><c:out value="<%= attrDesc.getEntryInstructions() %>"/></div>
	  	</div>
    <% previousGroupHeading = thisGroupHeading;}}} %>
