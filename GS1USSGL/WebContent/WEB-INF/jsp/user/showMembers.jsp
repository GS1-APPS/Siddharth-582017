<%@page import="org.gs1us.sgl.webapp.SortPageManager"%>
<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>
<%@page import="org.gs1us.sgl.webapp.standalone.UserController"%>
<%@page import="org.gs1us.sgl.memberservice.standalone.StandaloneMember"%>

<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%
	List<StandaloneMember> members = (List<StandaloneMember>)request.getAttribute("members");
	SortPageManager spm = (SortPageManager)request.getAttribute("spm");
 	String newMemberLink = MvcUriComponentsBuilder.fromMethodName(UserController.class, "newMemberGet", (Object)null).toUriString();

%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Members" />
  <jsp:param name="selectedItem" value="members" />
</jsp:include>

<c:choose>
	<c:when test='<%= members.size() == 0 %>'>
		<div class="page-header"></div>
	
		<div class="row">
			<div class="col-md-12">
				<p>There are no members registered. Click <a href="<%= newMemberLink %>">here</a> to register one.</p>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-md-12">
				<h1>Members</h1>
				<p><a href="<%= newMemberLink %>"><span class="icon-plus"></span> Register a new member and its first user</a></p>
				<table class="table table-striped">
					<thead>
						<tr>
							<sgl:sortableTH key="companyName" spm="<%= spm %>" heading="Company Name"/>
							<sgl:sortableTH key="gln" spm="<%= spm %>" heading="GLN"/>
							<sgl:sortableTH key="memberId" spm="<%= spm %>" heading="Member ID"/>
							<th>GCPs</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<% for (StandaloneMember member : members) { 
						 	 String editLink = MvcUriComponentsBuilder.fromMethodName(UserController.class, "editMemberGet", null, null, member.getId()).toUriString();
						 	 String deleteLink = MvcUriComponentsBuilder.fromMethodName(UserController.class, "deleteMemberGet", null, member.getId()).toUriString();
						 	 String memberNewUserLink = MvcUriComponentsBuilder.fromMethodName(UserController.class, "memberNewUserGet", null, member.getId()).toUriString();
							 String accountLink = MvcUriComponentsBuilder.fromMethodName(UserController.class, "showAccount", null, null, member.getId()).toUriString();
							 String productsLink = MvcUriComponentsBuilder.fromMethodName(UserController.class, "showProducts", null, null, member.getId()).toUriString();
					    %>
							<tr>
								<td><c:out value='<%= member.getCompanyName() %>' /></td>
								<td><c:out value='<%= member.getGln() %>' /></td>
								<td><c:out value='<%= member.getMemberId() %>' /></td>
								<td><sgl:ellipsisList list='<%= member.getGcps() %>' threshold="6" limit="2" popupid='<%= "gcps-" + member.getGln() %>' popuptitle='<%= "GCPs for " + member.getCompanyName() %>'/></td>
								<td>
								  <a href="<%= editLink %>" title="Edit this member's information"><span class="icon-pencil"></span></a>
								  <a href="<%= deleteLink %>" title="Remove this member"><span class="icon-trashcan"></span></a>
								  <a href="<%= memberNewUserLink %>" title="Add a new user for this member">+<span class="icon-user"></span></a>
								  <a href="<%= accountLink %>" title="Show this member's purchase history"><span class="icon-shopping_cart"></span></a>
								  <a href="<%= productsLink %>" title="Show this member's registered products"><span class="icon-menu"></span></a>
								</td>
							</tr>
						<% } %>
					</tbody>
				</table>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" flush="true" />
