<%@include file="_include.jsp"%>
<t:htmlTag value="hr" rendered="#{ticketController.showConnectOnClosure}" />
<e:panelGrid columns="2" columnClasses="colLeftMax,colRight" width="100%" 
	rendered="#{ticketController.userCanConnect}" >
	<h:panelGroup >
		<h:panelGroup
			rendered="#{ticketController.showConnectOnClosure}" >
			<h:panelGroup
				rendered="#{ticketController.targetTicket == null and ticketController.targetArchivedTicket == null}">
				<e:paragraph value=" #{msgs['TICKET_ACTION.TEXT.CLOSE.NOT_CONNECTED_TO_TICKET']}" />
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:connectToTicketButton');" >
					<t:graphicImage value="/media/images/connect-to-ticket.png" />
					<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_TICKET']}" />
				</h:panelGroup>
				<e:commandButton 
					id="connectToTicketButton" style="display: none"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_TICKET']}"
					action="#{ticketController.connectToTicket}" >
					<t:updateActionListener value="#{backAction}" property="#{ticketController.connectBackAction}" />
				</e:commandButton>
			</h:panelGroup>
			<h:panelGroup
				rendered="#{ticketController.targetTicket != null or ticketController.targetArchivedTicket != null}">
				<e:paragraph value=" #{msgs['TICKET_ACTION.TEXT.CLOSE.CONNECTED_TO_TICKET']}" >
					<f:param value="#{ticketController.targetTicket != null ? ticketController.targetTicket.id : ticketController.targetArchivedTicket.ticketId}" />
					<f:param value="#{ticketController.targetTicket != null ? ticketController.targetTicket.label : ticketController.targetArchivedTicket.label}" />
				</e:paragraph>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:connectToAnotherTicketButton');" >
					<t:graphicImage value="/media/images/connect-to-ticket.png" />
					<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_ANOTHER_TICKET']} " />
				</h:panelGroup>
				<e:commandButton 
					id="connectToAnotherTicketButton" style="display: none"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_ANOTHER_TICKET']}"
					action="#{ticketController.connectToTicket}" >
					<t:updateActionListener value="#{backAction}" property="#{ticketController.connectBackAction}" />
				</e:commandButton>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:removeTicketConnectionButton');" >
					<t:graphicImage value="/media/images/connect-to-ticket-remove.png" />
					<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.CLOSE.REMOVE_TICKET_CONNECTION']}" />
				</h:panelGroup>
				<e:commandButton 
					id="removeTicketConnectionButton" style="display: none"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.REMOVE_TICKET_CONNECTION']}" 
					action="#{ticketController.resetTargetTicket}" />
			</h:panelGroup>
			<h:panelGroup
				rendered="#{ticketController.targetFaq == null}">
				<e:paragraph value=" #{msgs['TICKET_ACTION.TEXT.CLOSE.NOT_CONNECTED_TO_FAQ']}" />
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:connectToFaqButton');" >
					<t:graphicImage value="/media/images/connect-to-faq.png" />
					<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_FAQ']}" />
				</h:panelGroup>
				<e:commandButton 
					id="connectToFaqButton" style="display: none"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_FAQ']}"
					action="#{ticketController.connectToFaq}" >
					<t:updateActionListener value="#{backAction}" property="#{ticketController.connectBackAction}" />
				</e:commandButton>
			</h:panelGroup>
			<h:panelGroup
				rendered="#{ticketController.targetFaq != null}">
				<e:paragraph value=" #{msgs['TICKET_ACTION.TEXT.CLOSE.CONNECTED_TO_FAQ']}" >
					<f:param value="#{ticketController.targetFaq.label}" />
				</e:paragraph>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:connectToAnotherFaqButton');" >
					<t:graphicImage value="/media/images/connect-to-faq.png" />
					<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_ANOTHER_FAQ']} " />
				</h:panelGroup>
				<e:commandButton 
					id="connectToAnotherFaqButton" style="display: none"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.CONNECT_TO_ANOTHER_FAQ']}"
					action="#{ticketController.connectToFaq}" >
					<t:updateActionListener value="#{backAction}" property="#{ticketController.connectBackAction}" />
				</e:commandButton>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:removeFaqConnectionButton');" >
					<t:graphicImage value="/media/images/connect-to-faq-remove.png" />
					<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.CLOSE.REMOVE_FAQ_CONNECTION']}" />
				</h:panelGroup>
				<e:commandButton 
					id="removeFaqConnectionButton" style="display: none"
					value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.REMOVE_FAQ_CONNECTION']}" 
					action="#{ticketController.resetTargetFaq}" />
			</h:panelGroup>
		</h:panelGroup>
	</h:panelGroup>
	<h:panelGroup>
		<h:panelGroup
			rendered="#{ticketController.showConnectOnClosure}" 
			style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('ticketActionForm:toggleShowConnectButton');" >
			<e:bold value="#{msgs['TICKET_ACTION.TEXT.CLOSE.HIDE_CONNECT']} " />
			<t:graphicImage value="/media/images/hide.png" />
		</h:panelGroup>
		<h:panelGroup
			rendered="#{not ticketController.showConnectOnClosure}" 
			style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('ticketActionForm:toggleShowConnectButton');" >
			<e:bold value="#{msgs['TICKET_ACTION.TEXT.CLOSE.SHOW_CONNECT']} " />
			<t:graphicImage value="/media/images/show.png" />
		</h:panelGroup>
		<e:commandButton 
			id="toggleShowConnectButton" style="display: none"
			value="#{msgs['TICKET_ACTION.BUTTON.CLOSE.TOGGLE_CONNECT']}" >
			<t:updateActionListener value="#{not ticketController.showConnectOnClosure}" 
				property="#{ticketController.showConnectOnClosure}" />
		</e:commandButton>
	</h:panelGroup>
</e:panelGrid>
<t:htmlTag value="hr" rendered="#{ticketController.showConnectOnClosure}" />
