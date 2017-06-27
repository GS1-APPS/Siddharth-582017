<%@tag import="org.gs1us.sgg.util.UserInputUtil"%>
<%@ tag language="java"     pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="list" required="true" type="java.lang.String[]" %>
<%@ attribute name="popupid" required="true" type="java.lang.String" %>
<%@ attribute name="popuptitle" required="true" type="java.lang.String" %>
<%@ attribute name="threshold" required="true" type="java.lang.Integer" %>
<%@ attribute name="limit" required="true" type="java.lang.Integer" %>


<c:choose>
	<c:when test="<%= list.length <= threshold %>">    
    	<c:out value='<%= UserInputUtil.printList(list, ", ") %>' />
   	</c:when>
   	<c:otherwise>
   		<%
   			String[] truncated = new String[limit];
   			System.arraycopy(list, 0, truncated, 0, limit);
   		%>
   		<c:out value='<%= UserInputUtil.printList(truncated, ", ") %>' />, ...<small>(<a href="#" data-toggle="modal" data-target='<%= "#" + popupid %>'>show all <%= list.length %></a>)</small>
 <div class="modal fade" id="<%= popupid %>" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h1 class="modal-title" id="myModalLabel"><c:out value="<%= popuptitle %>"/></h1>
      </div>
      <div class="modal-body">
      	<c:out value='<%= UserInputUtil.printList(list, ", ") %>' />
      </div>

    </div>
  </div>
</div>
   		
   	</c:otherwise>
</c:choose>
   	