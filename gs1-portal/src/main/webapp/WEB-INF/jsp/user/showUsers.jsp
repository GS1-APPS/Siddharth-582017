<%@page import="org.gs1us.sgl.webapp.SortPageManager"%>
<%@page import="org.gs1us.sgl.webapp.WebappUtil"%>
<%@page import="org.gs1us.sgl.memberservice.standalone.StandaloneMember"%>
<%@page import="org.gs1us.sgg.util.UserInputUtil"%>
<%@page import="org.gs1us.sgl.webapp.standalone.UserController"%>
<%@page import="org.gs1us.sgl.memberservice.standalone.StandaloneUser"%>

<%@page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder"%>

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sgl" tagdir="/WEB-INF/tags" %>

<%
	List<StandaloneUser> users = (List<StandaloneUser>)request.getAttribute("users");
	SortPageManager spm = (SortPageManager)request.getAttribute("spm");
	
	String timezone = (String)request.getAttribute("timezone");

	String membersUrl = MvcUriComponentsBuilder.fromMethodName(UserController.class, "showMembers", null, null).toUriString();
%>
    
<jsp:include page="/WEB-INF/jsp/includes/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Users" />
  <jsp:param name="selectedItem" value="users" />
  <jsp:param name="fluid" value="false" />
</jsp:include>

<c:choose>
	<c:when test='<%= users.size() == 0 %>'>
		<div class="page-header"></div>
	
		<div class="row">
			<div class="col-md-12">
				<p>There are no users registered. Click <a href="<%= membersUrl %>">here</a> to register one.</p>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-md-12">
				<h1>All Users</h1>
				<p>To register a new user, find the user's company on the <a href="<%= membersUrl %>">Members</a> page and add a user to that member.
				Or, register a new member company and its initial user from that same page.</p>
				<table class="table table-striped">
					<thead>
						<tr>
							<sgl:sortableTH key="username" spm="<%= spm %>" heading="Email"/>
							<sgl:sortableTH key="firstName" spm="<%= spm %>" heading="First Name"/>
							<sgl:sortableTH key="lastName" spm="<%= spm %>" heading="Last Name"/>
							<sgl:sortableTH key="companyName" spm="<%= spm %>" heading="Company"/>
							<sgl:sortableTH key="created" spm="<%= spm %>" heading="Created"/>
							<sgl:sortableTH key="lastLogin" spm="<%= spm %>" heading="Last Login"/>
							<sgl:sortableTH key="loginCount" spm="<%= spm %>" heading="Logins"/>
							<th>Status</th>
							<th>Role</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<% for (StandaloneUser user : users) { 
						    StandaloneMember member = (StandaloneMember)user.getMember();
						 	 String editLink = MvcUriComponentsBuilder.fromMethodName(UserController.class, "editUserGet", null, user.getId()).toUriString();
						 	 String deleteLink = MvcUriComponentsBuilder.fromMethodName(UserController.class, "deleteUserGet", null, user.getId()).toUriString();
							 UserController.UserType userType = UserController.UserType.getByRole(user.getRoles());
							 String userTypeDisplay = userType == null ? "" : userType.getDisplayName();
						%>
							<tr>
								<td><c:out value='<%= user.getUsername() %>' /></td>
								<td><c:out value='<%= user.getFirstName() %>' /></td>
								<td><c:out value='<%= user.getLastName() %>' /></td>
								<td>
									<c:if test="<%= member != null %>">
										<a href='<%= MvcUriComponentsBuilder.fromMethodName(UserController.class, "editMemberGet", null, null, member.getId()).toUriString() %>'><c:out value="<%= member.getCompanyName() %>" /></a>
									</c:if>
								</td>
								<td><%= UserInputUtil.dateToString(user.getCreated(), timezone) %></td>
								<td><%= user.getLastLogin() == null ? "Never"  : UserInputUtil.dateToString(user.getLastLogin(), timezone) %></td>
								<td class="text-right"><%= user.getLoginCount() %></td>
								<td><c:out value='<%= user.getState() %>' /></td>
								<td><c:out value='<%= userTypeDisplay %>' /></td>
								<td>
								  <a href="<%= editLink %>"><span class="icon-pencil"></span></a>
								  <a href="<%= deleteLink %>"><span class="icon-trashcan"></span></a>
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
