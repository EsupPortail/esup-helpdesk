<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}" 
	downloadId="#{archivedTicketController.downloadId}" >
	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{not archivedTicketController.userCanViewArchivedTicket}" >
		<h:panelGroup rendered="#{archivedTicketController.currentUser == null}" >
			<%@include file="_auth.jsp"%>
		</h:panelGroup>
		<h:panelGroup rendered="#{archivedTicketController.currentUser != null}" >
			<e:messages/>
		</h:panelGroup>
	</h:panelGroup>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="viewArchivedTicketForm" enctype="multipart/form-data" 
		rendered="#{archivedTicketController.userCanViewArchivedTicket}">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['ARCHIVED_TICKET_VIEW.TITLE']}" >
				<f:param value="#{archivedTicketController.archivedTicket.ticketId}" />
				<f:param value="#{archivedTicketController.archivedTicket.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewArchivedTicketForm:backButton');" >
					<e:bold value="#{msgs['_.BUTTON.BACK']} " />
					<t:graphicImage value="/media/images/back.png" alt="#{msgs['_.BUTTON.BACK']}" title="#{msgs['_.BUTTON.BACK']}" />
				</h:panelGroup>
				<e:commandButton id="backButton"
					value="#{msgs['_.BUTTON.BACK']}" 
					action="#{archivedTicketController.backPage == 'controlPanel' 
						? controlPanelController.enter 
						: (archivedTicketController.backPage == 'journal' 
							? journalController.enter 
							: (archivedTicketController.backPage == 'bookmarks' 
								? bookmarksController.enter 
								: (archivedTicketController.backPage == 'statistics' 
									? statisticsController.enter 
									: searchController.enter)))}" />
			</h:panelGroup>
		</e:panelGrid>
		<e:messages/>
		<h:panelGroup>
			<h:panelGroup style="cursor: pointer"
				onclick="simulateLinkClick('viewArchivedTicketForm:gotoTicketButton');">
				<t:graphicImage value="/media/images/search.png" />
				<e:bold value=" #{msgs['CONTROL_PANEL.BUTTON.GOTO_TICKET']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="gotoTicketButton"
				action="#{controlPanelController.gotoTicket}"
				value="#{msgs['CONTROL_PANEL.BUTTON.GOTO_TICKET']}" />
			<e:inputText id="ticketNumber" value="#{controlPanelController.ticketNumberString}" size="5" 
				onkeypress="if (event.keyCode == 13) { simulateLinkClick('viewArchivedTicketForm:gotoTicketButton'); return false; }" />
		</h:panelGroup>
		<e:panelGrid columns="3" width="100%" columnClasses="colLeft,colCenter,colLeft">
			<f:facet name="header">
				<t:htmlTag value="hr" />
			</f:facet>
			<h:panelGroup>
				<%@include file="_archivedTicketViewProperties.jsp"%> 
				<t:htmlTag value="hr" />
				<%@include file="_archivedTicketViewFiles.jsp"%> 
				<t:htmlTag value="hr" />
				<%@include file="_archivedTicketViewInvitations.jsp"%>  
				<t:htmlTag value="hr" />
				<%@include file="_archivedTicketViewBookmark.jsp"%>  
			</h:panelGroup>
			<h:panelGroup>
				<e:text value=" " />
			</h:panelGroup>
			<h:panelGroup>
				<%@include file="_archivedTicketViewHistory.jsp"%> 
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{archivedTicketController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		hideTableButtons("viewArchivedTicketForm:fileData","downloadButton");
		hideButton("viewArchivedTicketForm:backButton");
	</script>
</e:page>
