<%@include file="_commons-include.jsp"%>
<e:form id="exceptionForm" >
	<h:panelGroup>
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('exceptionForm:restartButton');" >
			<e:bold value="#{msgs['EXCEPTION.BUTTON.RESTART']} " />
			<t:graphicImage value="/media/images/restart.png"
				alt="#{msgs['EXCEPTION.BUTTON.RESTART']}" 
				title="#{msgs['EXCEPTION.BUTTON.RESTART']}" />
		</h:panelGroup>
		<e:commandButton style="display: none" id="restartButton" 
			action="#{exceptionController.restart}"
			value="#{msgs['EXCEPTION.BUTTON.RESTART']}" />
	</h:panelGroup>
</e:form>
