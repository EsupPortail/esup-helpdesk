<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="bookmarks"
	locale="#{sessionController.locale}">

<script type="text/javascript">
	function selectBookmarkTicket(index) {
		simulateLinkClick('bookmarksForm:bookmarkData:'+index+':viewTicketButton');
		simulateLinkClick('bookmarksForm:bookmarkData:'+index+':viewArchivedTicketButton');
	}
	function selectHistoryTicket(index) {
		simulateLinkClick('bookmarksForm:historyData:'+index+':viewTicketButton');
		simulateLinkClick('bookmarksForm:historyData:'+index+':viewArchivedTicketButton');
	}
</script>

	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{not bookmarksController.pageAuthorized}">
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="bookmarksForm" 
		rendered="#{bookmarksController.pageAuthorized}">

		<e:messages />

		<e:section value="#{msgs['BOOKMARKS.BOOKMARKS.TITLE']}" />
		<e:dataTable 
			rowOnMouseOver="javascript:{if (#{be.canRead}) {previousClass = this.className; this.className = 'portlet-table-selected';}}"
			rowOnMouseOut="javascript:{if (#{be.canRead}) {this.className = previousClass;}}"
			id="bookmarkData" rowIndexVar="variable"
			value="#{bookmarksController.bookmarkEntries}" var="be"
			border="0" style="width:100%" cellspacing="0" cellpadding="0"
			rendered="#{not empty bookmarksController.bookmarkEntries}" 
			columnClasses="colLeftNowrap,colLeft,colLeft,colLeft,colRight"
			>
			<f:facet name="header">
				<t:htmlTag value="hr" />
			</f:facet>
			<t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" onclick="selectBookmarkTicket(#{variable});">
				<f:facet name="header">
					<h:panelGroup>
						<e:bold value="#{msgs['BOOKMARKS.BOOKMARKS.HEADER.NUMBER']}" >
							<f:param value="#{bookmarksController.bookmarkEntriesNumber}" />
						</e:bold>
					</h:panelGroup>
				</f:facet>
				<h:panelGroup rendered="#{be.bookmark.ticketBookmark}" >
					<e:text value="#{msgs['BOOKMARKS.BOOKMARKS.ENTRY.TICKET']}" >
						<f:param value="#{be.bookmark.ticket.id}" />
						<f:param value="#{be.bookmark.ticket.label}" />
					</e:text>
				</h:panelGroup>
				<h:panelGroup rendered="#{be.bookmark.archivedTicketBookmark}" >
					<e:text value="#{msgs['BOOKMARKS.BOOKMARKS.ENTRY.ARCHIVED_TICKET']}" >
						<f:param value="#{be.bookmark.archivedTicket.ticketId}" />
						<f:param value="#{be.bookmark.archivedTicket.label}" />
					</e:text>
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" onclick="selectBookmarkTicket(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['BOOKMARKS.BOOKMARKS.HEADER.DEPARTMENT']}" />
				</f:facet>
				<e:text rendered="#{be.bookmark.ticketBookmark}"
					value="#{be.bookmark.ticket.department.label}" />
				<e:text rendered="#{be.bookmark.archivedTicketBookmark}"
					value="#{be.bookmark.archivedTicket.department.label}" />
			</t:column>
			<t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" onclick="selectBookmarkTicket(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['BOOKMARKS.BOOKMARKS.HEADER.OWNER']}" />
				</f:facet>
				<h:panelGroup>
					<e:text rendered="#{be.bookmark.ticketBookmark}"
						value="#{userFormatter[be.bookmark.ticket.owner]}" />
				</h:panelGroup>
				<h:panelGroup>
					<e:text rendered="#{be.bookmark.archivedTicketBookmark}"
						value="#{userFormatter[be.bookmark.archivedTicket.owner]}" />
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" onclick="selectBookmarkTicket(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['BOOKMARKS.BOOKMARKS.HEADER.MANAGER']}" />
				</f:facet>
				<h:panelGroup>
					<e:text rendered="#{be.bookmark.ticketBookmark and be.bookmark.ticket.manager != null}"
						value="#{userFormatter[be.bookmark.ticket.manager]}" />
				</h:panelGroup>
				<h:panelGroup>
					<e:text rendered="#{be.bookmark.archivedTicketBookmark and be.bookmark.archivedTicket.manager != null}"
						value="#{userFormatter[be.bookmark.archivedTicket.manager]}" />
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: pointer" >
				<h:panelGroup onclick="simulateLinkClick('bookmarksForm:bookmarkData:#{variable}:deleteBookmarkButton');" >
					<t:graphicImage value="/media/images/delete-bookmark.png" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="deleteBookmarkButton"
					action="#{bookmarksController.deleteBookmark}" >
					<t:updateActionListener value="#{be.bookmark}" property="#{bookmarksController.bookmarkToDelete}" />
				</e:commandButton>
			</t:column>
			<t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" >
				<h:panelGroup style="display: none" rendered="#{be.canRead}" >
					<e:commandButton id="viewTicketButton"
						rendered="#{be.bookmark.ticketBookmark}" 
						action="viewTicket" immediate="true">
						<t:updateActionListener value="#{be.bookmark.ticket}"
							property="#{ticketController.updatedTicket}" />
						<t:updateActionListener value="bookmarks"
							property="#{ticketController.backPage}" />
					</e:commandButton>
					<e:commandButton id="viewArchivedTicketButton" 
						rendered="#{be.bookmark.archivedTicketBookmark}"
						action="viewArchivedTicket" immediate="true">
						<t:updateActionListener value="#{be.bookmark.archivedTicket}"
							property="#{archivedTicketController.archivedTicket}" />
						<t:updateActionListener value="bookmarks"
							property="#{archivedTicketController.backPage}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
			<f:facet name="footer">
				<t:htmlTag value="hr" />
			</f:facet>
		</e:dataTable>
		<e:paragraph 
			value="#{msgs['BOOKMARKS.BOOKMARKS.TEXT.NO_BOOKMARK']}" 
			rendered="#{empty bookmarksController.bookmarkEntries}" />

		<e:section value="#{msgs['BOOKMARKS.HISTORY.TITLE']}" />
		<e:dataTable 
			rowOnMouseOver="javascript:{if (#{hie.canRead}) {previousClass = this.className; this.className = 'portlet-table-selected';}}"
			rowOnMouseOut="javascript:{if (#{hie.canRead}) {this.className = previousClass;}}"
			id="historyData" rowIndexVar="variable"
			value="#{bookmarksController.historyItemEntries}" var="hie"
			border="0" style="width:100%" cellspacing="0" cellpadding="0"
			rendered="#{not empty bookmarksController.historyItemEntries}" 
			columnClasses="colLeftNowrap,colLeft,colLeft,colLeft"
			>
			<f:facet name="header">
				<t:htmlTag value="hr" />
			</f:facet>
			<t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
				<f:facet name="header" >
					<h:panelGroup rendered="#{not empty bookmarksController.historyItemEntries}" >
						<h:panelGroup onclick="simulateLinkClick('bookmarksForm:historyData:clearButton');" 
							style="cursor: pointer" >
							<t:graphicImage value="/media/images/delete.png" />
							<e:bold value=" #{msgs['BOOKMARKS.HISTORY.BUTTON.CLEAR']}" />
						</h:panelGroup>
						<e:commandButton style="display: none" id="clearButton"
							action="#{bookmarksController.clearHistory}" >
						</e:commandButton>
					</h:panelGroup>
				</f:facet>
				<h:panelGroup rendered="#{hie.historyItem.ticketHistoryItem}" >
					<e:text value="#{msgs['BOOKMARKS.HISTORY.ENTRY.TICKET']}" >
						<f:param value="#{hie.historyItem.ticket.id}" />
						<f:param value="#{hie.historyItem.ticket.label}" />
					</e:text>
				</h:panelGroup>
				<h:panelGroup rendered="#{hie.historyItem.archivedTicketHistoryItem}" >
					<e:text value="#{msgs['BOOKMARKS.HISTORY.ENTRY.ARCHIVED_TICKET']}" >
						<f:param value="#{hie.historyItem.archivedTicket.ticketId}" />
						<f:param value="#{hie.historyItem.archivedTicket.label}" />
					</e:text>
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['BOOKMARKS.HISTORY.HEADER.DEPARTMENT']}" />
				</f:facet>
				<e:text rendered="#{hie.historyItem.ticketHistoryItem}"
					value="#{hie.historyItem.ticket.department.label}" />
				<e:text rendered="#{hie.historyItem.archivedTicketHistoryItem}"
					value="#{hie.historyItem.archivedTicket.department.label}" />
			</t:column>
			<t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['BOOKMARKS.HISTORY.HEADER.OWNER']}" />
				</f:facet>
				<h:panelGroup>
					<e:text rendered="#{hie.historyItem.ticketHistoryItem}"
						value="#{userFormatter[hie.historyItem.ticket.owner]}" />
				</h:panelGroup>
				<h:panelGroup>
					<e:text rendered="#{hie.historyItem.archivedTicketHistoryItem}"
						value="#{userFormatter[hie.historyItem.archivedTicket.owner]}" />
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['BOOKMARKS.HISTORY.HEADER.MANAGER']}" />
				</f:facet>
				<h:panelGroup>
					<e:text rendered="#{hie.historyItem.ticketHistoryItem and hie.historyItem.ticket.manager != null}"
						value="#{userFormatter[hie.historyItem.ticket.manager]}" />
				</h:panelGroup>
				<h:panelGroup>
					<e:text rendered="#{hie.historyItem.archivedTicketHistoryItem and hie.historyItem.archivedTicket.manager != null}"
						value="#{userFormatter[hie.historyItem.archivedTicket.manager]}" />
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" >
				<h:panelGroup style="display: none" rendered="#{hie.canRead}" >
					<e:commandButton id="viewTicketButton"
						rendered="#{hie.historyItem.ticketHistoryItem}" 
						action="viewTicket" immediate="true">
						<t:updateActionListener value="#{hie.historyItem.ticket}"
							property="#{ticketController.updatedTicket}" />
						<t:updateActionListener value="bookmarks"
							property="#{ticketController.backPage}" />
					</e:commandButton>
					<e:commandButton id="viewArchivedTicketButton" 
						rendered="#{hie.historyItem.archivedTicketHistoryItem}"
						action="viewArchivedTicket" immediate="true">
						<t:updateActionListener value="#{hie.historyItem.archivedTicket}"
							property="#{archivedTicketController.archivedTicket}" />
						<t:updateActionListener value="bookmarks"
							property="#{archivedTicketController.backPage}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
			<f:facet name="footer">
				<t:htmlTag value="hr" />
			</f:facet>
		</e:dataTable>
		<e:paragraph 
			value="#{msgs['BOOKMARKS.HISTORY.TEXT.NO_HISTORY']}" 
			rendered="#{empty bookmarksController.historyItemEntries}" />
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{bookmarksController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
	
