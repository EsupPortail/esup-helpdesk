<%@include file="_include.jsp"%>

<h:panelGroup>
	<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:cancelActionButton');" >
		<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
		<t:graphicImage value="/media/images/back.png"
			alt="#{msgs['_.BUTTON.CANCEL']}" 
			title="#{msgs['_.BUTTON.CANCEL']}" />
	</h:panelGroup>
	<e:commandButton style="display: none" id="cancelActionButton" action="#{ticketController.cancelAction}"
		value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
</h:panelGroup>
