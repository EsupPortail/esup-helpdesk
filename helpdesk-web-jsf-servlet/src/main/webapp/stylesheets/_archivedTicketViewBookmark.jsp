<%@include file="_include.jsp"%>
<h:panelGroup id="bookmark" >
	<h:panelGroup rendered="#{archivedTicketController.bookmark != null}" >
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewArchivedTicketForm:deleteBookmarkButton');" >
			<e:bold value="#{msgs['TICKET_VIEW.BUTTON.DELETE_BOOKMARK']} " />
			<t:graphicImage value="/media/images/delete-bookmark.png" />
			<e:commandButton style="display: none" id="deleteBookmarkButton"
				value="#{msgs['TICKET_VIEW.BUTTON.DELETE_BOOKMARK']}" action="#{archivedTicketController.deleteBookmark}" />
		</h:panelGroup>
	</h:panelGroup>
	<h:panelGroup rendered="#{archivedTicketController.bookmark == null}" >
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewArchivedTicketForm:addBookmarkButton');" >
			<e:bold value="#{msgs['TICKET_VIEW.BUTTON.ADD_BOOKMARK']} " />
			<t:graphicImage value="/media/images/add-bookmark.png" />
			<e:commandButton style="display: none" id="addBookmarkButton"
				value="#{msgs['TICKET_VIEW.BUTTON.ADD_BOOKMARK']}" action="#{archivedTicketController.addBookmark}" />
		</h:panelGroup>
	</h:panelGroup>
</h:panelGroup>
	
	