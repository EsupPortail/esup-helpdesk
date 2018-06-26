<%@include file="_include.jsp"%>

<e:panelGrid columns="2" width="100%" columnClasses="colLeftNowrap,colRight">

	<h:panelGroup>
		<h:panelGroup rendered="#{ticketController.previousUnreadTicket != null}" >
			<h:panelGroup style="cursor: pointer"
				onclick="simulateLinkClick('viewTicketForm:gotoPreviousUnreadTicketButton');">
				<t:graphicImage 
					value="/media/images/previous-unread-ticket.png" 
					title="#{ticketController.previousUnreadTicketTitle}" />
				<e:text value=" " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="gotoPreviousUnreadTicketButton"
				action="viewTicket"
				value="#{ticketController.previousUnreadTicketTitle}" >
				<t:updateActionListener 
					value="#{ticketController.previousUnreadTicket}" 
					property="#{ticketController.ticket}" />
			</e:commandButton>
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.previousVisibleTicket != null}" >
			<h:panelGroup style="cursor: pointer"
				onclick="simulateLinkClick('viewTicketForm:gotoPreviousVisibleTicketButton');">
				<t:graphicImage 
					value="/media/images/previous-visible-ticket.png" 
					title="#{ticketController.previousVisibleTicketTitle}" />
				<e:text value=" " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="gotoPreviousVisibleTicketButton"
				action="viewTicket"
				value="#{ticketController.previousVisibleTicketTitle}" >
				<t:updateActionListener 
					value="#{ticketController.previousVisibleTicket}" 
					property="#{ticketController.ticket}" />
			</e:commandButton>
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.nextVisibleTicket != null}" >
			<h:panelGroup style="cursor: pointer"
				onclick="simulateLinkClick('viewTicketForm:gotoNextVisibleTicketButton');">
				<t:graphicImage 
					value="/media/images/next-visible-ticket.png" 
					title="#{ticketController.nextVisibleTicketTitle}" />
				<e:text value=" " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="gotoNextVisibleTicketButton"
				action="viewTicket"
				value="#{ticketController.nextVisibleTicketTitle}" >
				<t:updateActionListener 
					value="#{ticketController.nextVisibleTicket}" 
					property="#{ticketController.ticket}" />
			</e:commandButton>
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.nextUnreadTicket != null}" >
			<h:panelGroup style="cursor: pointer"
				onclick="simulateLinkClick('viewTicketForm:gotoNextUnreadTicketButton');">
				<t:graphicImage 
					value="/media/images/next-unread-ticket.png" 
					title="#{ticketController.nextUnreadTicketTitle}" />
				<e:text value=" " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="gotoNextUnreadTicketButton"
				action="viewTicket"
				value="#{ticketController.nextUnreadTicketTitle}" >
				<t:updateActionListener 
					value="#{ticketController.nextUnreadTicket}" 
					property="#{ticketController.ticket}" />
			</e:commandButton>
		</h:panelGroup>

		<h:panelGroup style="cursor: pointer"
			onclick="simulateLinkClick('viewTicketForm:gotoTicketButton');">
			<e:bold value=" #{msgs['CONTROL_PANEL.BUTTON.GOTO_TICKET']} " />
		</h:panelGroup>
		<e:commandButton style="display: none" id="gotoTicketButton"
			action="#{controlPanelController.gotoTicket}"
			value="#{msgs['CONTROL_PANEL.BUTTON.GOTO_TICKET']}" />
		<e:inputText id="ticketNumber" value="#{controlPanelController.ticketNumberString}" size="5" 
			onkeypress="if (event.keyCode == 13) { simulateLinkClick('viewTicketForm:gotoTicketButton'); return false; }" />
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:addTicketButton');" >
			<e:bold value=" #{msgs['CONTROL_PANEL.BUTTON.ADD_TICKET']} " />
			<t:graphicImage value="/media/images/ticket.png" />
			<t:graphicImage value="/media/images/add.png" />
		</h:panelGroup>
		<e:commandButton id="addTicketButton" action="#{ticketController.add}" style="display: none"
			value="#{msgs['CONTROL_PANEL.BUTTON.ADD_TICKET']}" immediate="true" />

	</h:panelGroup>

	<%-- --%>

	<h:panelGroup>
		<h:panelGroup rendered="#{ticketController.userCanApproveClosure}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:approveClosureButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.APPROVE_CLOSURE']} " />
				<t:graphicImage value="/media/images/approve-closure.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="approveClosureButton"
				value="#{msgs['TICKET_VIEW.BUTTON.APPROVE_CLOSURE']}"
				action="#{ticketController.approveClosure}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.userCanRefuseClosure}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:refuseClosureButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.REFUSE_CLOSURE']} " />
				<t:graphicImage value="/media/images/refuse-closure.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="refuseClosureButton"
				value="#{msgs['TICKET_VIEW.BUTTON.REFUSE_CLOSURE']}"
				action="#{ticketController.refuseClosure}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.userCanGiveInformation}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" onclick="var reopen = false; if (#{ticketController.userCanReopen}) { reopen = confirm('#{msgs['TICKET_VIEW.CONFIRM.GIVE_INFORMATION_OR_REOPEN']}'); } simulateLinkClick(reopen ? 'viewTicketForm:reopenButton' : 'viewTicketForm:giveInformationButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.GIVE_INFORMATION']} " />
				<t:graphicImage value="/media/images/add.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="giveInformationButton"
				value="#{msgs['TICKET_VIEW.BUTTON.GIVE_INFORMATION']}"
				action="#{ticketController.giveInformation}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.userCanCancel}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" 
				onclick="if (#{ticketController.currentUser.showPopupOnClosure} && !confirm('#{msgs['TICKET_VIEW.CONFIRM.CANCEL']}')) { return false; } simulateLinkClick('viewTicketForm:cancelButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.CANCEL']} " />
				<t:graphicImage value="/media/images/cancel.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="cancelButton"
				value="#{msgs['TICKET_VIEW.BUTTON.CANCEL']}"
				action="#{ticketController.cancel}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.userCanRequestInformation}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:requestInformationButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.REQUEST_INFORMATION']} " />
				<t:graphicImage value="/media/images/request-information.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="requestInformationButton"
				value="#{msgs['TICKET_VIEW.BUTTON.REQUEST_INFORMATION']}"
				action="#{ticketController.requestInformation}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.userCanClose}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" 
				onclick="simulateLinkClick('viewTicketForm:closeButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.CLOSE']} " />
				<t:graphicImage value="/media/images/close.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="closeButton"
				value="#{msgs['TICKET_VIEW.BUTTON.CLOSE']}"
				action="#{ticketController.close}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.userCanRefuse}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" 
				onclick="if (#{ticketController.currentUser.showPopupOnClosure} && !confirm('#{msgs['TICKET_VIEW.CONFIRM.REFUSE']}')) { return false; } simulateLinkClick('viewTicketForm:refuseButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.REFUSE']} " />
				<t:graphicImage value="/media/images/refuse.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="refuseButton"
				value="#{msgs['TICKET_VIEW.BUTTON.REFUSE']}"
				action="#{ticketController.refuse}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.userCanPostpone}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:postponeButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.POSTPONE']} " />
				<t:graphicImage value="/media/images/postpone.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="postponeButton"
				value="#{msgs['TICKET_VIEW.BUTTON.POSTPONE']}"
				action="#{ticketController.postpone}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.userCanCancelPostponement}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:cancelPostponementButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.CANCEL_POSTPONEMENT']} " />
				<t:graphicImage value="/media/images/cancel-postponement.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="cancelPostponementButton"
				value="#{msgs['TICKET_VIEW.BUTTON.CANCEL_POSTPONEMENT']}"
				action="#{ticketController.cancelPostponement}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{ticketController.userCanReopen}" >
			<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:reopenButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.REOPEN']} " />
				<t:graphicImage value="/media/images/reopen.png" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="reopenButton"
				value="#{msgs['TICKET_VIEW.BUTTON.REOPEN']}"
				action="#{ticketController.reopen}" />
		</h:panelGroup>

	</h:panelGroup>
</e:panelGrid>
