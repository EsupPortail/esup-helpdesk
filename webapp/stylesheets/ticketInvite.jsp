<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanInvite}">
<script type="text/javascript">
function addInvitation(select) {
    if (select.value == "") {
        return;
    }
    var userId = document.getElementById("ticketActionForm:ldapUid");
    if (userId.value != "" && userId.value.charAt(userId.value.length-1) != ",") {
        userId.value += ",";
    }
    userId.value += select.options[select.selectedIndex].value;
    select.selectedIndex = 0;
}
</script>
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ticketActionForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['TICKET_ACTION.TITLE.INVITE']}" >
				<f:param value="#{ticketController.ticket.id}" />
			</e:section>
			<%@include file="_ticketActionCancel.jsp"%>
		</e:panelGrid>
		<e:messages />
		<t:htmlTag value="hr" />
		<e:panelGrid columns="2" columnClasses="colLeftMaxNowrap,colLeftNowrap">
			<h:panelGroup>
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.INVITE.1_LDAP']}" rendered="#{domainService.useLdap}" />
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.INVITE.1_NO_LDAP']}" rendered="#{not domainService.useLdap}" />
				<e:inputText id="ldapUid" value="#{ticketController.ldapUid}" size="50"
								onkeypress="if (event.keyCode == 13) { focusFckEditor('ticketActionForm:actionMessage'); return false;}" />
				<h:panelGroup rendered="#{domainService.useLdap}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:ldapSearchButton');" >
						<e:bold value=" #{msgs['_.BUTTON.LDAP']} " />
						<t:graphicImage value="/media/images/search.png"
							alt="#{msgs['_.BUTTON.LDAP']}" 
							title="#{msgs['_.BUTTON.LDAP']}" />
					</h:panelGroup>
					<e:commandButton style="display:none"
						id="ldapSearchButton" action="#{ldapSearchController.firstSearch}"
						value="#{msgs['_.BUTTON.LDAP']}" >
						<t:updateActionListener value="#{ticketController}"
							property="#{ldapSearchController.caller}" />
						<t:updateActionListener value="userSelectedToTicketInvite"
							property="#{ldapSearchController.successResult}" />
						<t:updateActionListener value="cancelToTicketInvite"
							property="#{ldapSearchController.cancelResult}" />
					</e:commandButton>
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup>
				<h:panelGroup rendered="#{ticketController.userCanInviteGroup}" >
					<h:panelGroup style="cursor: pointer" onclick="showHideElement('ticketActionForm:inviteTree');" >
						<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.SEARCH_GROUP']} " />
						<t:graphicImage value="/media/images/members.png"
							alt="#{msgs['TICKET_ACTION.BUTTON.SEARCH_GROUP']}" 
							title="#{msgs['TICKET_ACTION.BUTTON.SEARCH_GROUP']}" />
					</h:panelGroup>
				</h:panelGroup>
				<t:htmlTag value="br" rendered="#{ticketController.userCanInviteGroup and not empty ticketController.recentInvitationItems}" />
				<h:panelGroup rendered="#{not empty ticketController.recentInvitationItems}" >
					<e:bold value=" #{msgs['TICKET_ACTION.TEXT.INVITE.RECENT']} " />
					<e:selectOneMenu onchange="addInvitation(this);" >
						<f:selectItems value="#{ticketController.recentInvitationItems}" />
					</e:selectOneMenu>
				</h:panelGroup>
			</h:panelGroup>
		</e:panelGrid>
		<h:panelGroup id="inviteTree" style="display: none" 
			rendered="#{ticketController.userCanInviteGroup}" >
			<t:tree2 id="tree" value="#{ticketController.inviteTree}"
				var="node" varNodeToggler="t" clientSideToggle="true"
				showRootNode="true" >
				<f:facet name="root">
					<h:panelGroup >
						<t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
						<t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
						<e:italic value=" #{msgs['TICKET_ACTION.TEXT.INVITE.ROOT_LABEL']}" />
					</h:panelGroup>
				</f:facet>
				<f:facet name="department">
					<h:panelGroup>
						<t:graphicImage value="#{departmentIconUrlProvider[node.department]}" />
						<h:panelGroup 
							style="cursor: pointer" 
							onclick="document.getElementById('ticketActionForm:ldapUid').value='#{node.managersString}';hideElement('ticketActionForm:inviteTree');" >
							<e:bold value=" #{msgs['TICKET_ACTION.TEXT.INVITE.DEPARTMENT_NODE']}" >
								<f:param value="#{node.department.xlabel}" />
								<f:param value="#{node.managersNumber}" />
							</e:bold>
						</h:panelGroup>
					</h:panelGroup>
				</f:facet>
				<f:facet name="category">
					<h:panelGroup>
						<t:graphicImage value="#{categoryIconUrlProvider[node.category]}" />
						<h:panelGroup rendered="#{node.managersNumber gt 0}" >
							<h:panelGroup 
								style="cursor: pointer" 
								onclick="document.getElementById('ticketActionForm:ldapUid').value='#{node.managersString}';hideElement('ticketActionForm:inviteTree');" >
								<e:bold value=" #{msgs['TICKET_ACTION.TEXT.INVITE.CATEGORY_NODE_WITH_MEMBERS']}" >
									<f:param value="#{node.category.xlabel}" />
									<f:param value="#{node.managersNumber}" />
								</e:bold>
							</h:panelGroup>
						</h:panelGroup>
						<e:italic value=" #{msgs['TICKET_ACTION.TEXT.INVITE.CATEGORY_NODE']}" 
							rendered="#{node.managersNumber == 0}" >
							<f:param value="#{node.category.xlabel}" />
						</e:italic>
					</h:panelGroup>
				</f:facet>
			</t:tree2>
		</h:panelGroup>
		<t:htmlTag value="hr" />
		<e:panelGrid columns="2" width="100%" columnClasses="colLeftMax,colRightNowrap">
			<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.INVITE.2']}" />
			<%@include file="_ticketActionResponses.jsp"%>
		</e:panelGrid>
		<fck:editor  
			id="actionMessage" 
			value="#{ticketController.actionMessage}" 
			toolbarSet="actionMessage" />
		<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightMaxNowrap" width="100%" >
			<h:panelGroup id="mainButtonGroup" style="position: absolute; white-space: nowrap;" >
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:actionButton');" >
					<e:bold value="#{msgs['TICKET_ACTION.BUTTON.INVITE']} " />
					<t:graphicImage value="/media/images/invite.png" />
				</h:panelGroup>
				<e:commandButton 
					id="actionButton" style="display: none"
					value="#{msgs['TICKET_ACTION.BUTTON.INVITE']}"
					action="#{ticketController.doInvite}" />
			</h:panelGroup>
			<h:panelGroup>
				<%@include file="_ticketActionScope.jsp"%>
				<%@include file="_ticketActionPreviewButton.jsp"%>
			</h:panelGroup>
		</e:panelGrid>
		<%@include file="_ticketActionPreview.jsp"%>
		<%@include file="_ticketActionHistory.jsp"%>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<%@include file="_ticketActionJavascript.jsp"%>
</e:page>

