<%@include file="_include.jsp"%>

<h:panelGroup>
	<h:panelGroup rendered="#{ticketController.actionMessage != null}" >
		<t:htmlTag value="hr" />
		<e:subSection value="#{msgs['TICKET_ACTION.TEXT.PREVIEW']}"/>
		<e:paragraph escape="false" value="#{ticketController.actionMessage}"/>
	</h:panelGroup>
</h:panelGroup>