<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanConnect}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ticketActionForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['TICKET_ACTION.TITLE.CONNECT_TO_TICKET']}" >
				<f:param value="#{ticketController.ticket.id}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:backButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="backButton" action="#{ticketController.getConnectBackAction}"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>
		<e:messages />
		<h:panelGroup>
			<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.CONNECT_TO_TICKET.CHOOSE_TARGET_TICKET']}" />
			<e:inputText value="#{ticketController.targetTicketId}" size="5" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:nextButton');" >
				<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.SET_TARGET_TICKET']} " />
				<t:graphicImage value="/media/images/next.png"
					alt="#{msgs['TICKET_ACTION.BUTTON.SET_TARGET_TICKET']}" 
					title="#{msgs['TICKET_ACTION.BUTTON.SET_TARGET_TICKET']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="nextButton" action="#{ticketController.setTargetTicketFromId}"
				value="#{msgs['TICKET_ACTION.BUTTON.SET_TARGET_TICKET']}" />
		</h:panelGroup>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>

