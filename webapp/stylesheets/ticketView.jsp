<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="" locale="#{sessionController.locale}" downloadId="#{ticketController.downloadId}" >
	<%@include file="_navigation.jsp"%>

	<script type="text/javascript">
	function toggleShowAlerts() {
		simulateLinkClick('viewTicketForm:toggleShowAlertsButton');
	}
</script>

	<h:panelGroup rendered="#{not ticketController.userCanViewTicket}" >
		<h:panelGroup rendered="#{ticketController.currentUser == null}" >
			<%-- <%@include file="_auth.jsp"%> --%>
		</h:panelGroup>
		<h:panelGroup rendered="#{ticketController.currentUser != null}" >
			<e:messages/>
		</h:panelGroup>
	</h:panelGroup>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="viewTicketForm" enctype="multipart/form-data" rendered="#{ticketController.userCanViewTicket}" >
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['TICKET_VIEW.TITLE']}" >
				<f:param value="#{ticketController.ticket.id}" />
				<f:param value="#{ticketController.ticket.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup 
					rendered="#{ticketController.userCanChangeLabel}" >
					<h:panelGroup 
						style="cursor: pointer" onclick="showHideElement('viewTicketForm:editTicketLabel');" >
						<e:bold value="#{msgs['TICKET_VIEW.PROPERTIES.LABEL']} " />
						<t:graphicImage value="/media/images/edit.png" alt="#{msgs['TICKET_VIEW.PROPERTIES.LABEL']}" title="#{msgs['TICKET_VIEW.PROPERTIES.LABEL']}" />
					</h:panelGroup>
					<h:outputText value="&nbsp;&nbsp;&nbsp;" escape="false" />
				</h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:printButton');" >
					<e:bold value="#{msgs['TICKET_VIEW.BUTTON.PRINT']} " />
					<t:graphicImage 
						value="/media/images/print.png" 
						alt="#{msgs['TICKET_VIEW.BUTTON.PRINT']}" 
						title="#{msgs['TICKET_VIEW.BUTTON.PRINT']}" />
				</h:panelGroup>
				<e:commandButton id="printButton" style="display: none"
					value="#{msgs['_.BUTTON.BACK']}" 
					action="#{ticketController.print}" />
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:backButton');" >
					<e:bold value=" #{msgs['_.BUTTON.BACK']} " />
					<t:graphicImage value="/media/images/back.png" alt="#{msgs['_.BUTTON.BACK']}" title="#{msgs['_.BUTTON.BACK']}" />
				</h:panelGroup>
				<e:commandButton id="backButton" style="display: none"
					value="#{msgs['_.BUTTON.BACK']}" 
					action="#{ticketController.backPage == 'search' 
						? searchController.enter 
						: (ticketController.backPage == 'journal' 
							? journalController.enter 
							: (ticketController.backPage == 'bookmarks' 
								? bookmarksController.enter 
								: (ticketController.backPage == 'statistics' 
									? statisticsController.enter 
									: controlPanelController.enter
								)
							)
						)}" />
			</h:panelGroup>
		</e:panelGrid>
		<e:messages/>
		<h:panelGroup id="editTicketLabel" 
			rendered="#{ticketController.userCanChangeLabel}" 
			style="display: none" >
			<e:panelGrid columns="2" columnClasses="colLeftMax,colLeft" width="100%" >
				<h:panelGroup/>
				<h:panelGroup>
					<e:inputText value="#{ticketController.ticketLabel}" size="80" />
					<t:htmlTag value="br"/>
					<h:panelGroup rendered="#{ticketController.userCanSetNoAlert}" >
						<e:selectBooleanCheckbox id="labelNoAlert"
							value="#{ticketController.labelNoAlert}" />
						<e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
					</h:panelGroup>
					<t:htmlTag value="br"/>
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:changeTicketLabelButton');" >
						<e:bold value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_LABEL']} " />
						<t:graphicImage value="/media/images/save.png" />
					</h:panelGroup>
					<e:commandButton 
						id="changeTicketLabelButton" style="display: none"
						value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_LABEL']}"
						action="#{ticketController.doChangeTicketLabel}" />
				</h:panelGroup>
			</e:panelGrid>
		</h:panelGroup>
		<h:panelGroup>
			<%@include file="_ticketViewButtons.jsp"%>
		</h:panelGroup> 
		<e:panelGrid columns="3" width="100%" columnClasses="colLeft,colCenter,colLeftMax">
			<f:facet name="header">
				<t:htmlTag value="hr" />
			</f:facet>
			<h:panelGroup>
				<%@include file="_ticketViewProperties.jsp"%> 
				<t:htmlTag value="hr" />
				<%@include file="_ticketViewFiles.jsp"%> 
				<t:htmlTag value="hr" />
				<%@include file="_ticketViewInvitations.jsp"%> 
				<t:htmlTag value="hr" />
				<%@include file="_ticketViewMonitoring.jsp"%> 
				<t:htmlTag value="hr" />
				<%@include file="_ticketViewBookmark.jsp"%> 
				<e:commandButton style="display: none"
					id="toggleShowAlertsButton"
					value="#{ticketController.showAlerts ? msgs['TICKET_VIEW.BUTTON.HIDE_ALERTS'] : msgs['TICKET_VIEW.BUTTON.SHOW_ALERTS']}"
					action="#{ticketController.toggleShowAlerts}" />
			</h:panelGroup>
			<h:panelGroup>
				<e:text value=" " />
			</h:panelGroup>
			<h:panelGroup>
				<%@include file="_ticketViewHistory.jsp"%> 
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{ticketController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
<script type="text/javascript"> 	 
    hideElement('viewTicketForm:hideOwnerInfo'); 	 
    hideElement('viewTicketForm:hideManagerInfo'); 	 
</script>
</e:page>
