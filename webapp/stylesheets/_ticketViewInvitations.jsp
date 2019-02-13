<%@include file="_include.jsp"%>
<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightNowrapMid">
	<e:subSection value="#{msgs['TICKET_VIEW.INVITATIONS.HEADER']}" >
		<f:param value="#{ticketController.invitationsNumber}" />
	</e:subSection>
	<h:panelGroup style="cursor: pointer" onclick="javascript:{showHideElement('viewTicketForm:invitations');showHideElement('viewTicketForm:showInvitations');showHideElement('viewTicketForm:hideInvitations');return false;}" >
		<h:panelGroup id="showInvitations" >
			<t:graphicImage value="/media/images/show.png" 
				alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
		</h:panelGroup>
		<h:panelGroup id="hideInvitations" style="display: none" >
			<t:graphicImage value="/media/images/hide.png"
				alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<h:panelGroup id="invitations" style="display: none" >
	<h:panelGroup rendered="#{ticketController.userCanInvite}" >
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:inviteButton');" >
			<t:graphicImage value="/media/images/invite.png" />
			<e:bold value=" #{msgs['TICKET_VIEW.INVITATIONS.BUTTON.INVITE']}" />
		</h:panelGroup>
		<e:commandButton 
			id="inviteButton" style="display: none"
			value="#{msgs['TICKET_VIEW.INVITATIONS.BUTTON.INVITE']}" 
			action="#{ticketController.invite}" 
			/>
	</h:panelGroup>
	<e:paragraph value="#{msgs['TICKET_VIEW.INVITATIONS.NO_INVITATION']}"
		rendered="#{empty ticketController.invitations}" />
	<e:dataTable width="100%" columnClasses="colLeftNowrap,colRight" 
		id="invitationData" rowIndexVar="index" 
		value="#{ticketController.invitations}"
		var="invitation" border="0" cellspacing="0" cellpadding="0"
		rendered="#{not empty ticketController.invitations}" >
		<f:facet name="header">
			<t:htmlTag value="hr" />
		</f:facet>
		<f:facet name="footer">
			<t:htmlTag value="hr" />
		</f:facet>
		<t:column>
			<e:text value="#{msgs['TICKET_VIEW.INVITATIONS.INVITED_USER']}" rendered="#{!ticketController.ticket.anonymous}" >
				<f:param value="#{userFormatter[invitation.user]}" />
			</e:text>
			<e:text value="#{msgs['TICKET_VIEW.INVITATIONS.INVITED_USER']}" rendered="#{ticketController.ticket.anonymous && controlPanelController.manager}">
				<f:param value="#{userFormatter[invitation.user]}" />
			</e:text>
			<e:text value="#{msgs['TICKET_VIEW.INVITATIONS.INVITED_USER']}" rendered="#{ticketController.ticket.anonymous && !controlPanelController.manager}" >
				<f:param value="#{msgs['USER.ANONYMOUS']}" />
			</e:text>
		</t:column>
		<t:column>
			<h:panelGroup rendered="#{ticketController.userCanRemoveInvitations or ticketController.currentUser.id == invitation.user.id}" >
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:invitationData:#{index}:deleteInvitationButton');" >
					<t:graphicImage value="/media/images/delete.png" />
				</h:panelGroup>
				<e:commandButton 
					id="deleteInvitationButton" style="display: none"
					value="x" action="#{ticketController.doRemoveInvitation}" >
					<t:updateActionListener value="#{invitation}" property="#{ticketController.invitationToDelete}" />
				</e:commandButton>
			</h:panelGroup>
		</t:column>
	</e:dataTable>
</h:panelGroup>
	
	