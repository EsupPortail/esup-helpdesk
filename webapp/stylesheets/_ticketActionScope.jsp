<%@include file="_include.jsp"%>

<h:panelGroup>
	<h:panelGroup rendered="#{ticketController.actionScopeItems != null}" >
		<e:text value="#{msgs['TICKET_ACTION.TEXT.SCOPE_PROMPT']} " />
		<e:selectOneMenu id="scope" 
			value="#{ticketController.actionScope}" >
			<f:selectItems value="#{ticketController.actionScopeItems}" />
		</e:selectOneMenu>
	</h:panelGroup>
</h:panelGroup>
