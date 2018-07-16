<%@include file="_include.jsp"%>


<t:htmlTag value="div" id="inviteComment" styleClass="form-block">
	<e:paragraph value="#{msgs['TICKET_VIEW.INVITATIONS.NO_INVITATION']}"
		rendered="#{empty ticketController.invitations}" />
	<e:dataTable width="100%" columnClasses="colLeftNowrap,colRight" 
		id="invitationData" rowIndexVar="index"
		styleClass="invite_array"
		value="#{ticketController.invitations}"
		var="invitation" border="0" cellspacing="0" cellpadding="0"
		rendered="#{not empty ticketController.invitations}" >

		<t:column styleClass="column">
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
		<t:column styleClass="column">
	        <t:htmlTag value="div" styleClass="form-item" rendered="#{ticketController.userCanRemoveInvitations or ticketController.currentUser.id == invitation.user.id}">
				<e:commandButton 
					id="deleteInvitationButton"
					value="#{msgs['_.BUTTON.DELETE']}" action="#{ticketController.doRemoveInvitation}" >
					<t:updateActionListener value="#{invitation}" property="#{ticketController.invitationToDelete}" />
				</e:commandButton>
			</t:htmlTag>
		</t:column>
	</e:dataTable>
	</t:htmlTag>

    <t:htmlTag value="div" styleClass="form-block " rendered="#{ticketController.userCanInvite}" >
        <t:htmlTag value="div" styleClass="form-item">
		<e:commandButton
			id="inviteButton"
			onclick="storeInSession('invite-user-select',null)"
			value="#{msgs['TICKET_VIEW.INVITATIONS.BUTTON.INVITE']}"
			styleClass="button--secondary"
			action="#{ticketController.invite}" />
        </t:htmlTag>
    </t:htmlTag>


	
	