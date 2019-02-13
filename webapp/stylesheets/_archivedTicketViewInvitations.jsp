<%@include file="_include.jsp"%>
<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightNowrap">
	<e:subSection value="#{msgs['TICKET_VIEW.INVITATIONS.HEADER']}" >
		<f:param value="#{archivedTicketController.archivedInvitationsNumber}" />
	</e:subSection>
	<h:panelGroup style="cursor: pointer" onclick="javascript:{showHideElement('viewArchivedTicketForm:invitations');showHideElement('viewArchivedTicketForm:showInvitations');showHideElement('viewArchivedTicketForm:hideInvitations');return false;}" >
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
	
	