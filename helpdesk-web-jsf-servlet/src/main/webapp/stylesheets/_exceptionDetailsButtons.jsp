<%@include file="_commons-include.jsp"%>
<h:panelGroup id="hideExceptionDetails" 
	style="cursor: pointer" onclick="hideExceptionDetails(); return false;" >
	<e:bold value="#{msgs['EXCEPTION.TEXT.HIDE_DETAILS']} " />
	<t:graphicImage value="/media/images/hide.png"
		alt="#{msgs['EXCEPTION.TEXT.HIDE_DETAILS']}" 
		title="#{msgs['EXCEPTION.TEXT.HIDE_DETAILS']}" />
</h:panelGroup>
<h:panelGroup id="showExceptionDetails" 
	style="cursor: pointer" onclick="showExceptionDetails(); return false;" >
	<e:bold value="#{msgs['EXCEPTION.TEXT.SHOW_DETAILS']} " />
	<t:graphicImage value="/media/images/show.png"
		alt="#{msgs['EXCEPTION.TEXT.SHOW_DETAILS']}" 
		title="#{msgs['EXCEPTION.TEXT.SHOW_DETAILS']}" />
</h:panelGroup>
