<%@include file="_include.jsp"%>

<h:panelGroup>
	<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:previewButton');" >
		<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.PREVIEW']} " />
		<t:graphicImage value="/media/images/view.png" />
	</h:panelGroup>
	<e:commandButton 
		id="previewButton" style="display: none"
		value="#{msgs['TICKET_ACTION.BUTTON.PREVIEW']} " />	
</h:panelGroup>
