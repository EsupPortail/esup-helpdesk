<%@include file="_include.jsp"%>
<t:htmlTag styleClass="action-item separator" value="div" rendered="#{ticketController.bookmark != null}"></t:htmlTag>
<t:htmlTag styleClass="action-item" value="div"  rendered="#{ticketController.bookmark != null}">
		<h:panelGroup onclick="simulateLinkClick('viewTicketForm:deleteBookmarkButton');" >
			<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.DELETE_BOOKMARK']} " />
		</h:panelGroup>
		<e:commandButton style="display: none" id="deleteBookmarkButton"
			value="#{msgs['TICKET_VIEW.BUTTON.DELETE_BOOKMARK']}" action="#{ticketController.deleteBookmark}" />
</t:htmlTag>

<t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.bookmark == null}">
		<h:panelGroup onclick="simulateLinkClick('viewTicketForm:addBookmarkButton');" >
			<h:outputText value="#{msgs['TICKET_VIEW.BUTTON.ADD_BOOKMARK']} " />
		</h:panelGroup>
		<e:commandButton style="display: none" id="addBookmarkButton"
			value="#{msgs['TICKET_VIEW.BUTTON.ADD_BOOKMARK']}" action="#{ticketController.addBookmark}" />
</t:htmlTag>
	
	