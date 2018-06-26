<%@include file="_include.jsp"%>

<t:htmlTag styleClass="action-item" value="div"  rendered="#{archivedTicketController.bookmark != null}">
		<h:panelGroup onclick="simulateLinkClick('viewArchivedTicketForm:deleteBookmarkButton');" >
			<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.DELETE_BOOKMARK']} " />
		</h:panelGroup>
		<e:commandButton style="display: none" id="deleteBookmarkButton"
			value="#{msgs['TICKET_VIEW.BUTTON.DELETE_BOOKMARK']}" action="#{archivedTicketController.deleteBookmark}" />
</t:htmlTag>

<t:htmlTag styleClass="action-item" value="div" rendered="#{archivedTicketController.bookmark == null}">
		<h:panelGroup onclick="simulateLinkClick('viewArchivedTicketForm:addBookmarkButton');" >
			<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.ADD_BOOKMARK']} " />
		</h:panelGroup>
		<e:commandButton style="display: none" id="addBookmarkButton"
			value="#{msgs['TICKET_VIEW.BUTTON.ADD_BOOKMARK']}" action="#{archivedTicketController.addBookmark}" />
</t:htmlTag>
	
	