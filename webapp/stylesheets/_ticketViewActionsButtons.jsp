<%@include file="_include.jsp"%>



		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeLabel}">
			<h:panelGroup  onclick="showHideElement('viewTicketForm:editTicketLabel');" >
				<h:outputText value="#{msgs['TICKET_VIEW.PROPERTIES.LABEL']} " />
			</h:panelGroup>
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanApproveClosure}" >
			<h:panelGroup  onclick="simulateLinkClick('viewTicketForm:approveClosureButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.APPROVE_CLOSURE']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="approveClosureButton"
				value="#{msgs['TICKET_VIEW.BUTTON.APPROVE_CLOSURE']}"
				action="#{ticketController.approveClosure}" />
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanRefuseClosure}" >
			<h:panelGroup  onclick="simulateLinkClick('viewTicketForm:refuseClosureButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.REFUSE_CLOSURE']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="refuseClosureButton"
				value="#{msgs['TICKET_VIEW.BUTTON.REFUSE_CLOSURE']}"
				action="#{ticketController.refuseClosure}" />
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanGiveInformation}" >
			<h:panelGroup  onclick="var reopen = false; if (#{ticketController.userCanReopen}) { reopen = confirm('#{msgs['TICKET_VIEW.CONFIRM.GIVE_INFORMATION_OR_REOPEN']}'); } simulateLinkClick(reopen ? 'viewTicketForm:reopenButton' : 'viewTicketForm:giveInformationButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.GIVE_INFORMATION']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="giveInformationButton"
				value="#{msgs['TICKET_VIEW.BUTTON.GIVE_INFORMATION']}"
				action="#{ticketController.giveInformation}" />
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanCancel}" >
			<h:panelGroup  
				onclick="if (#{ticketController.currentUser.showPopupOnClosure} && !confirm('#{msgs['TICKET_VIEW.CONFIRM.CANCEL']}')) { return false; } simulateLinkClick('viewTicketForm:cancelButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.CANCEL']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="cancelButton"
				value="#{msgs['TICKET_VIEW.BUTTON.CANCEL']}"
				action="#{controlPanelController.cancel}" />
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanRequestInformation}" >
			<h:panelGroup  onclick="simulateLinkClick('viewTicketForm:requestInformationButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.REQUEST_INFORMATION']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="requestInformationButton"
				value="#{msgs['TICKET_VIEW.BUTTON.REQUEST_INFORMATION']}"
				action="#{ticketController.requestInformation}" />
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanClose}" >
			<h:panelGroup 
				onclick="simulateLinkClick('viewTicketForm:closeButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.CLOSE']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="closeButton"
				value="#{msgs['TICKET_VIEW.BUTTON.CLOSE']}"
				action="#{ticketController.close}" />
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanRefuse}" >
			<h:panelGroup  
				onclick="simulateLinkClick('viewTicketForm:refuseButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.REFUSE']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="refuseButton"
				value="#{msgs['TICKET_VIEW.BUTTON.REFUSE']}"
				action="#{ticketController.refuse}" />
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanPostpone}" >
			<h:panelGroup  onclick="simulateLinkClick('viewTicketForm:postponeButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.POSTPONE']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="postponeButton"
				value="#{msgs['TICKET_VIEW.BUTTON.POSTPONE']}"
				action="#{ticketController.postpone}" />
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanCancelPostponement}" >
			<h:panelGroup  onclick="simulateLinkClick('viewTicketForm:cancelPostponementButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.CANCEL_POSTPONEMENT']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="cancelPostponementButton"
				value="#{msgs['TICKET_VIEW.BUTTON.CANCEL_POSTPONEMENT']}"
				action="#{ticketController.cancelPostponement}" />
		</t:htmlTag>

		<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanReopen}" >
			<h:panelGroup  onclick="simulateLinkClick('viewTicketForm:reopenButton');" >
				<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.REOPEN']} " />
			</h:panelGroup>
			<e:commandButton style="display: none" id="reopenButton"
				value="#{msgs['TICKET_VIEW.BUTTON.REOPEN']}"
				action="#{ticketController.reopen}" />
		</t:htmlTag>

        <t:htmlTag styleClass="action-item separator" value="div">
            <h:panelGroup styleClass="print-action"  onclick="simulateLinkClick('viewTicketForm:printButton');" >
                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.PRINT']} " />
            </h:panelGroup>
		    <e:commandButton id="printButton" style="display: none" value="#{msgs['_.BUTTON.BACK']}" action="#{ticketController.print}" />
        </t:htmlTag>

        <%@include file="_ticketViewBookmark.jsp"%>



