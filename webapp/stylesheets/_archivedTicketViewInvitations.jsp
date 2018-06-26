<%@include file="_include.jsp"%>

<h:panelGroup id="invitations" >
	<e:paragraph value="#{msgs['TICKET_VIEW.INVITATIONS.NO_INVITATION']}"
		rendered="#{empty archivedTicketController.archivedInvitations}" />
	<e:dataTable width="100%" columnClasses="colLeftNowrap" 
		id="invitationData" rowIndexVar="index" 
		value="#{archivedTicketController.archivedInvitations}"
		var="archivedInvitation" border="0" cellspacing="0" cellpadding="0"
		rendered="#{not empty archivedTicketController.archivedInvitations}" >
		<f:facet name="header">
			<t:htmlTag value="hr" />
		</f:facet>
		<f:facet name="footer">
			<t:htmlTag value="hr" />
		</f:facet>
		<t:column>
			<e:text value="#{msgs['TICKET_VIEW.INVITATIONS.INVITED_USER']}" >
				<f:param value="#{userFormatter[archivedInvitation.user]}" />
			</e:text>
		</t:column>
	</e:dataTable>
</h:panelGroup>
	
	