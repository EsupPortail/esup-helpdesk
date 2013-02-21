<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanChangeOwner}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ticketActionForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['TICKET_ACTION.TITLE.CHANGE_OWNER']}" >
				<f:param value="#{ticketController.ticket.id}" />
			</e:section>
			<%@include file="_ticketActionCancel.jsp"%>
		</e:panelGrid>
		<e:messages />
		<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.CHANGE_OWNER.1_LDAP']}" rendered="#{domainService.useLdap}" />
		<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.CHANGE_OWNER.1_NO_LDAP']}" rendered="#{not domainService.useLdap}" />
		<e:inputText id="ldapUid" value="#{ticketController.ldapUid}" size="50"
			onkeypress="if (event.keyCode == 13) { simulateLinkClick('ticketActionForm:actionButton'); return false;}" />
		<h:panelGroup rendered="#{domainService.useLdap}" >
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:ldapSearchButton');" >
				<e:bold value=" #{msgs['_.BUTTON.LDAP']} " />
				<t:graphicImage value="/media/images/search.png"
					alt="#{msgs['_.BUTTON.LDAP']}" 
					title="#{msgs['_.BUTTON.LDAP']}" />
			</h:panelGroup>
			<e:commandButton 
				id="ldapSearchButton" style="display: none"
				value="#{msgs['_.BUTTON.LDAP']}" action="#{ldapSearchController.firstSearch}" >
				<t:updateActionListener value="#{ticketController}"
					property="#{ldapSearchController.caller}" />
				<t:updateActionListener value="userSelectedToTicketChangeOwner"
					property="#{ldapSearchController.successResult}" />
				<t:updateActionListener value="cancelToTicketChangeOwner"
					property="#{ldapSearchController.cancelResult}" />
			</e:commandButton>
		</h:panelGroup>
		<e:panelGrid columns="2" width="100%" columnClasses="colLeftMax,colRightNowrap">
			<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.CHANGE_OWNER.2']}" />
			<%@include file="_ticketActionResponses.jsp"%>
		</e:panelGrid>
		<fck:editor  
			id="actionMessage" 
			value="#{ticketController.actionMessage}" 
			toolbarSet="actionMessage" />
		<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightMaxNowrap" width="100%" >
			<h:panelGroup id="mainButtonGroup" style="position: absolute; white-space: nowrap;" >
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:actionButton');" >
					<e:bold value="#{msgs['TICKET_ACTION.BUTTON.CHANGE_OWNER']} " />
					<t:graphicImage value="/media/images/user.png" />
				</h:panelGroup>
				<e:commandButton 
					id="actionButton" style="display: none"
					value="#{msgs['TICKET_ACTION.BUTTON.CHANGE_OWNER']}"
					action="#{ticketController.doChangeOwner}" />
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

