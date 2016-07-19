<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" >

	<e:emptyMenu/>

	<e:section value="#{msgs['INFO.TITLE']}" />

	<e:subSection value="#{msgs['INFO.APPLICATION.TITLE']}" />

	<e:panelGrid columns="2" columnClasses="colLeft,colLeft" >
		<e:text value="#{msgs['INFO.APPLICATION.NAME']}" />
		<e:text value="#{infoController.applicationName}" />
		<e:text value="#{msgs['INFO.APPLICATION.VERSION']}" />
		<e:text value="#{infoController.applicationVersion}" />
		<e:text value="#{msgs['INFO.APPLICATION.DEPLOY_TYPE']}" />
		<h:panelGroup>
			<e:text value="#{infoController.applicationDeployType}" 
				rendered="#{not infoController.applicationQuickStart}"/>
			<e:text value="#{msgs['INFO.APPLICATION.DEPLOY_TYPE_QUICK_START']}" 
				rendered="#{infoController.applicationQuickStart}"/>
		</h:panelGroup>
		<e:text value="#{msgs['INFO.APPLICATION.PORTAL_INFO']}" 
			rendered="#{infoController.applicationPortalInfo != null}"/>
		<e:text value="#{infoController.applicationPortalInfo}" 
			rendered="#{infoController.applicationPortalInfo != null}"/>
		<e:text value="#{msgs['INFO.APPLICATION.SERVER_INFO']}" />
		<e:text value="#{infoController.applicationServerInfo}" />
	</e:panelGrid>

	<e:subSection value="#{msgs['INFO.DATABASE.TITLE']}" />

	<e:panelGrid columns="2" columnClasses="colLeft,colLeft">
		<e:text value="#{msgs['INFO.DATABASE.DRIVER']}" />
		<e:text value="#{infoController.databaseDriver}" />
		<e:text value="#{msgs['INFO.DATABASE.DIALECT']}" />
		<e:text value="#{infoController.databaseDialect}" />
		<e:text value="#{msgs['INFO.DATABASE.USE_JNDI']}" />
		<e:text value="#{infoController.databaseUseJndi}" />
	</e:panelGrid>

	<e:subSection value="#{msgs['INFO.DATA.TITLE']}" />

	<e:panelGrid columns="2" columnClasses="colLeft,colLeft">
		<e:text value="#{msgs['INFO.DATA.DEPARTMENTS']}" />
		<e:text value="#{msgs['INFO.DATA.DEPARTMENTS_VALUE']}" >
			<f:param value="#{infoController.realDepartmentsNumber}" />
			<f:param value="#{infoController.virtualDepartmentsNumber}" />
			<f:param value="#{infoController.totalDepartmentsNumber}" />
		</e:text>
		<e:text value="#{msgs['INFO.DATA.CATEGORIES']}" />
		<e:text value="#{msgs['INFO.DATA.CATEGORIES_VALUE']}" >
			<f:param value="#{infoController.realCategoriesNumber}" />
			<f:param value="#{infoController.virtualCategoriesNumber}" />
			<f:param value="#{infoController.totalCategoriesNumber}" />
		</e:text>
		<e:text value="#{msgs['INFO.DATA.TICKETS']}" />
		<e:text value="#{msgs['INFO.DATA.TICKETS_VALUE']}" >
			<f:param value="#{infoController.activeTicketsNumber}" />
			<f:param value="#{infoController.archivedTicketsNumber}" />
			<f:param value="#{infoController.totalTicketsNumber}" />
		</e:text>
		<e:text value="#{msgs['INFO.DATA.USERS']}" />
		<e:text value="#{msgs['INFO.DATA.USERS_VALUE']}" >
			<f:param value="#{infoController.applicationUsersNumber}" />
			<f:param value="#{infoController.casUsersNumber}" />
			<f:param value="#{infoController.shibbolethUsersNumber}" />
			<f:param value="#{infoController.totalUsersNumber}" />
		</e:text>
		<e:text value="#{msgs['INFO.DATA.MANAGERS']}" />
		<e:text value="#{msgs['INFO.DATA.MANAGERS_VALUE']}" >
			<f:param value="#{infoController.managerUsersNumber}" />
		</e:text>
	</e:panelGrid>

</e:page>
