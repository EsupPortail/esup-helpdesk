<%@include file="_include.jsp"%>
<h:panelGroup id="bookmark" >
	<h:panelGroup rendered="#{ticketController.bookmark != null}" >
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:deleteBookmarkButton');" >
			<e:bold value="#{msgs['TICKET_VIEW.BUTTON.DELETE_BOOKMARK']} " />
			<t:graphicImage value="/media/images/delete-bookmark.png" />
		</h:panelGroup>
		<e:commandButton style="display: none" id="deleteBookmarkButton"
			value="#{msgs['TICKET_VIEW.BUTTON.DELETE_BOOKMARK']}" action="#{ticketController.deleteBookmark}" />
	</h:panelGroup>
	<h:panelGroup rendered="#{ticketController.bookmark == null}" >
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:addBookmarkButton');" >
			<e:bold value="#{msgs['TICKET_VIEW.BUTTON.ADD_BOOKMARK']} " />
			<t:graphicImage value="/media/images/add-bookmark.png" />
		</h:panelGroup>
		<e:commandButton style="display: none" id="addBookmarkButton"
			value="#{msgs['TICKET_VIEW.BUTTON.ADD_BOOKMARK']}" action="#{ticketController.addBookmark}" />
	</h:panelGroup>
</h:panelGroup>
	
	