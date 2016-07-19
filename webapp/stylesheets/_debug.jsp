<%@include file="_commons-include.jsp"%>

<%/*

<e:panelGrid alternateColors="true" columns="2" width="100%"
	cellspacing="0"
	cellpadding="0">
	<f:facet name="header">
		<t:htmlTag value="hr" />
	</f:facet>

	<e:outputLabel for="applicationName"
		value="#{msgs['EXCEPTION.INFORMATION.APPLICATION']}" />
	<e:text id="applicationName" value="#{debugController.applicationName}" />

	<e:outputLabel for="applicationVersion"
		value="#{msgs['EXCEPTION.INFORMATION.VERSION']}" />
	<e:text id="applicationVersion"
		value="#{debugController.applicationVersion}" />

	<e:outputLabel for="server"
		value="#{msgs['EXCEPTION.INFORMATION.SERVER']}" />
	<h:panelGroup id="server">
		<e:text value="#{debugController.server}"
			rendered="#{debugController.server != null}" />
		<e:italic value="#{msgs['EXCEPTION.INFORMATION.SERVER.UNKNOWN']}"
			rendered="debugController.server = null" />
	</h:panelGroup>

	<e:outputLabel for="date"
		value="#{msgs['EXCEPTION.INFORMATION.DATE']}" />
	<e:text id="date" value="#{debugController.date}" />

	<e:outputLabel for="userId"
		value="#{msgs['EXCEPTION.INFORMATION.USER_ID']}" />
	<h:panelGroup id="userId">
		<e:text value="#{debugController.userId}"
			rendered="#{debugController.userId != null}" />
		<e:italic value="#{msgs['EXCEPTION.INFORMATION.USER_ID.UNKNOWN']}"
			rendered="debugController.userId = null" />
	</h:panelGroup>

	<e:outputLabel for="portal"
		value="#{msgs['EXCEPTION.INFORMATION.PORTAL']}" />
	<h:panelGroup id="portal">
		<h:panelGroup rendered="#{debugController.portal != null}">
			<e:text value="#{debugController.portal}" />
			<e:text value=" #{msgs['EXCEPTION.INFORMATION.PORTAL.QUICK_START']}"
				rendered="#{debugController.quickStart}" />
		</h:panelGroup>
		<e:italic value="#{msgs['EXCEPTION.INFORMATION.PORTAL.UNKNOWN']}"
			rendered="debugController.portal = null" />
	</h:panelGroup>

	<e:outputLabel for="client"
		value="#{msgs['EXCEPTION.INFORMATION.CLIENT']}" />
	<h:panelGroup id="client">
		<e:text value="#{debugController.client}"
			rendered="#{debugController.client != null}" />
		<e:italic value="#{msgs['EXCEPTION.INFORMATION.CLIENT.UNKNOWN']}"
			rendered="debugController.client = null" />
	</h:panelGroup>

	<e:outputLabel for="queryString"
		value="#{msgs['EXCEPTION.INFORMATION.QUERY_STRING']}" />
	<h:panelGroup id="queryString">
		<e:text value="#{debugController.queryString}"
			rendered="#{debugController.queryString != null}" />
		<e:italic
			value="#{msgs['EXCEPTION.INFORMATION.QUERY_STRING.UNKNOWN']}"
			rendered="debugController.queryString = null" />
	</h:panelGroup>

	<e:outputLabel for="userAgent"
		value="#{msgs['EXCEPTION.INFORMATION.USER_AGENT']}" />
	<h:panelGroup id="userAgent">
		<e:text value="#{debugController.userAgent}"
			rendered="#{debugController.userAgent != null}" />
		<e:italic value="#{msgs['EXCEPTION.INFORMATION.USER_AGENT.UNKNOWN']}"
			rendered="debugController.userAgent = null" />
	</h:panelGroup>

</e:panelGrid>

<e:subSection value="#{msgs['EXCEPTION.HEADER.REQUEST_ATTRIBUTES']}" />

<e:dataTable id="requestAttributes"
	value="#{debugController.requestAttributes}" var="string" border="0"
	style="width: 100%"
	rendered="#{not empty debugController.requestAttributes}">

	<f:facet name="header">
		<t:htmlTag value="hr" />
	</f:facet>
	<t:column>
		<e:text value="#{string}" />
	</t:column>
</e:dataTable>
<e:text value="#{msgs['EXCEPTION.REQUEST_ATTRIBUTES.NONE']}"
	rendered="#{empty debugController.requestAttributes}" />

<e:subSection value="#{msgs['EXCEPTION.HEADER.SESSION_ATTRIBUTES']}" />

<e:dataTable id="sessionAttributes"
	value="#{debugController.sessionAttributes}" var="string" border="0"
	style="width: 100%"
	rendered="#{not empty debugController.sessionAttributes}">

	<f:facet name="header">
		<t:htmlTag value="hr" />
	</f:facet>
	<t:column>
		<e:text value="#{string}" />
	</t:column>
</e:dataTable>
<e:text value="#{msgs['EXCEPTION.SESSION_ATTRIBUTES.NONE']}"
	rendered="#{empty debugController.sessionAttributes}" />

<e:subSection value="#{msgs['EXCEPTION.HEADER.GLOBAL_SESSION_ATTRIBUTES']}" />

<e:dataTable id="globalSessionAttributes"
	value="#{debugController.globalSessionAttributes}" var="string" border="0"
	style="width: 100%"
	rendered="#{not empty debugController.globalSessionAttributes}">

	<f:facet name="header">
		<t:htmlTag value="hr" />
	</f:facet>
	<t:column>
		<e:text value="#{string}" />
	</t:column>
</e:dataTable>
<e:text value="#{msgs['EXCEPTION.GLOBAL_SESSION_ATTRIBUTES.NONE']}"
	rendered="#{empty debugController.globalSessionAttributes}" />

<e:subSection value="#{msgs['EXCEPTION.HEADER.REQUEST_HEADERS']}" />

<e:dataTable id="requestHeaders"
	value="#{debugController.requestHeaders}" var="string" border="0"
	style="width: 100%"
	rendered="#{not empty debugController.requestHeaders}">

	<f:facet name="header">
		<t:htmlTag value="hr" />
	</f:facet>
	<t:column>
		<e:text value="#{string}" />
	</t:column>
</e:dataTable>
<e:text value="#{msgs['EXCEPTION.REQUEST_HEADERS.NONE']}"
	rendered="#{empty debugController.requestHeaders}" />

*/%>
	 
	 <e:subSection value="#{msgs['EXCEPTION.HEADER.REQUEST_PARAMETERS']}" />

<e:dataTable id="requestParameters"
	value="#{debugController.requestParameters}" var="string" border="0"
	style="width: 100%"
	rendered="#{not empty debugController.requestParameters}">

	<f:facet name="header">
		<t:htmlTag value="hr" />
	</f:facet>
	<t:column>
		<e:text value="#{string}" />
	</t:column>
</e:dataTable>
<e:text value="#{msgs['EXCEPTION.REQUEST_PARAMETERS.NONE']}"
	rendered="#{empty debugController.requestParameters}" />

<%/*
<e:subSection value="#{msgs['EXCEPTION.HEADER.COOKIES']}" />

<e:dataTable id="cookies" value="#{debugController.cookies}" var="string"
	border="0" style="width: 100%"
	rendered="#{not empty debugController.cookies}">

	<f:facet name="header">
		<t:htmlTag value="hr" />
	</f:facet>
	<t:column>
		<e:text value="#{string}" />
	</t:column>
</e:dataTable>
<e:text value="#{msgs['EXCEPTION.COOKIES.NONE']}"
	rendered="#{empty debugController.cookies}" />

<e:subSection value="#{msgs['EXCEPTION.HEADER.SYSTEM_PROPERTIES']}" />

<e:dataTable id="systemProperties"
	value="#{debugController.systemProperties}" var="string" border="0"
	style="width: 100%"
	rendered="#{not empty debugController.systemProperties}">

	<f:facet name="header">
		<t:htmlTag value="hr" />
	</f:facet>
	<t:column>
		<e:text value="#{string}" />
	</t:column>
</e:dataTable>
<e:text value="#{msgs['EXCEPTION.SYSTEM_PROPERTIES.NONE']}"
	rendered="#{empty debugController.systemProperties}" />
*/%>
	
	