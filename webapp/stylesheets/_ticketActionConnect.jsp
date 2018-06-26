<%@include file="_include.jsp"%>

<e:panelGrid columns="2" columnClasses="colLeftMax,colRight" width="100%" rendered="#{ticketController.userCanConnect}" >
	<h:panelGroup >
		<h:panelGroup  >


            <t:htmlTag value="div" styleClass="form-item" rendered="#{ticketController.targetTicket == null and ticketController.targetArchivedTicket == null}">
				<e:commandButton 
					id="connectToTicketButton" styleClass="button--secondary"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_TICKET']}"
					action="#{ticketController.connectToTicket}" >
					<t:updateActionListener value="#{backAction}" property="#{ticketController.connectBackAction}" />
				</e:commandButton>
            </t:htmlTag>

            <t:htmlTag value="div" styleClass="form-item" rendered="#{ticketController.targetTicket != null or ticketController.targetArchivedTicket != null}">

				<e:paragraph value=" #{msgs['TICKET_ACTION.TEXT.CLOSE.CONNECTED_TO_TICKET']}" >
					<f:param value="#{ticketController.targetTicket != null ? ticketController.targetTicket.id : ticketController.targetArchivedTicket.ticketId}" />
					<f:param value="#{ticketController.targetTicket != null ? ticketController.targetTicket.label : ticketController.targetArchivedTicket.label}" />
				</e:paragraph>

				<e:commandButton 
					id="connectToAnotherTicketButton" styleClass="button--tertiary"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_ANOTHER_TICKET']}"
					action="#{ticketController.connectToTicket}" >
					<t:updateActionListener value="#{backAction}" property="#{ticketController.connectBackAction}" />
				</e:commandButton>

				<e:commandButton styleClass="cancel"
					id="removeTicketConnectionButton"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.REMOVE_TICKET_CONNECTION']}" 
					action="#{ticketController.resetTargetTicket}" />

			</t:htmlTag>

             <t:htmlTag value="div" styleClass="form-item" rendered="#{ticketController.targetFaq == null}">
				<e:commandButton 
					id="connectToFaqButton" styleClass="button--secondary"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_FAQ']}"
					action="#{ticketController.connectToFaq}" >
					<t:updateActionListener value="#{backAction}" property="#{ticketController.connectBackAction}" />
				</e:commandButton>
             </t:htmlTag>

            <t:htmlTag value="div" styleClass="form-item" rendered="#{ticketController.targetFaq != null}">
				<e:paragraph value=" #{msgs['TICKET_ACTION.TEXT.CLOSE.CONNECTED_TO_FAQ']}" >
					<f:param value="#{ticketController.targetFaq.label}" />
				</e:paragraph>

				<e:commandButton styleClass="button--tertiary"
					id="connectToAnotherFaqButton"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_ANOTHER_FAQ']}"
					action="#{ticketController.connectToFaq}" >
					<t:updateActionListener value="#{backAction}" property="#{ticketController.connectBackAction}" />
				</e:commandButton>

				<e:commandButton 
					id="removeFaqConnectionButton"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.REMOVE_FAQ_CONNECTION']}" 
					action="#{ticketController.resetTargetFaq}" />
			</t:htmlTag>
		</h:panelGroup>
	</h:panelGroup>

</e:panelGrid>

