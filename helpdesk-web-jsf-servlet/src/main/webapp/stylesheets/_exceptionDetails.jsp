<%@include file="_include.jsp"%>
<e:panelGrid columnClasses="colLeft,colLeft" alternateColors="true" columns="2" width="100%"
	cellspacing="0" cellpadding="0">
	<f:facet name="header">
		<t:htmlTag value="hr" />
	</f:facet>

	<e:outputLabel for="applicationName"
		value="#{msgs['EXCEPTION.INFORMATION.APPLICATION']}" />
	<e:text id="applicationName"
		value="#{exceptionController.applicationName}" />

	<e:outputLabel for="applicationVersion"
		value="#{msgs['EXCEPTION.INFORMATION.VERSION']}" />
	<e:text id="applicationVersion"
		value="#{exceptionController.applicationVersion}" />

	<e:outputLabel for="server"
		value="#{msgs['EXCEPTION.INFORMATION.SERVER']}" />
	<h:panelGroup id="server">
		<e:text value="#{exceptionController.server}"
			rendered="#{exceptionController.server != null}" />
		<e:italic value="#{msgs['EXCEPTION.INFORMATION.SERVER.UNKNOWN']}"
			rendered="#{exceptionController.server == null}" />
	</h:panelGroup>

	<e:outputLabel for="date" value="#{msgs['EXCEPTION.INFORMATION.DATE']}" />
	<e:text id="date" value="#{exceptionController.date}" />

	<e:outputLabel for="client"
		value="#{msgs['EXCEPTION.INFORMATION.CLIENT']}" />
	<h:panelGroup id="client">
		<e:text value="#{exceptionController.client}"
			rendered="#{exceptionController.client != null}" />
		<e:italic value="#{msgs['EXCEPTION.INFORMATION.CLIENT.UNKNOWN']}"
			rendered="#{exceptionController.client == null}" />
	</h:panelGroup>

	<e:outputLabel for="exceptionName"
		value="#{msgs['EXCEPTION.EXCEPTION.NAME']}" />
	<e:text id="exceptionName" value="#{exceptionController.exceptionName}" />

	<e:outputLabel for="exceptionMessage"
		value="#{msgs['EXCEPTION.EXCEPTION.MESSAGE']}" />
	<e:text id="exceptionMessage"
		value="#{exceptionController.exceptionMessage}" />

	<e:outputLabel for="exceptionShortStackTrace"
		value="#{msgs['EXCEPTION.EXCEPTION.SHORT_STACK_TRACE']}" />
	<e:dataTable id="exceptionShortStackTrace"
		value="#{exceptionController.exceptionShortStackTrace}" var="string"
		border="0" style="width: 100%">
		<t:column>
			<e:text value="#{string}" />
		</t:column>
	</e:dataTable>

	<f:facet name="footer">
		<t:htmlTag value="hr" />
	</f:facet>
</e:panelGrid>

<e:paragraph rendered="#{exceptionController.recipientEmail != null}"
	value="#{msgs['EXCEPTION.TEXT.BOTTOM']}">
	<f:param value="#{exceptionController.recipientEmail}" />
</e:paragraph>
