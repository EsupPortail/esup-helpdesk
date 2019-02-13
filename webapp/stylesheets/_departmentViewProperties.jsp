<%@include file="_include.jsp"%>
<e:panelGrid columns="2" width="100%"  columnClasses="colLeft,colRight" >
	<e:subSection value="#{msgs['DEPARTMENT_VIEW.HEADER.PROPERTIES']}" />
	<h:panelGroup>
		<h:panelGroup rendered="#{departmentsController.currentUserCanEditDepartmentProperties}" >
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:editPropertiesButton');" >
				<e:bold value="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_PROPERTIES']} " />
				<t:graphicImage value="/media/images/edit.png"
					alt="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_PROPERTIES']}" 
					title="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_PROPERTIES']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="editPropertiesButton" action="editProperties"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_PROPERTIES']}" >
				<t:updateActionListener value="#{departmentsController.department}"
					property="#{departmentsController.departmentToUpdate}" />
			</e:commandButton>
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<e:panelGrid columns="2">
	<e:outputLabel for="id"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.ID']}" />
	<e:text value=" #{departmentsController.department.id}" id="id" />
	<e:outputLabel for="icon"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.ICON']}" />
	<h:panelGroup id="icon" >
		<t:graphicImage value="#{departmentIconUrlProvider[departmentsController.department]}" />
		<e:italic value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.DEFAULT_ICON']}" 
			rendered="#{departmentsController.department.icon == null}" />
		<e:text value=" #{departmentsController.department.icon.name}" 
			rendered="#{departmentsController.department.icon != null}" />
	</h:panelGroup>
	<e:outputLabel for="enabled"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.ENABLED']}" />
	<h:panelGroup id="enabled" >
		<e:text
			value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.ENABLED_TRUE']}"
			rendered="#{departmentsController.department.enabled}"
			style="color: #007f00" />
		<e:text
			value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.ENABLED_FALSE']}"
			rendered="#{not departmentsController.department.enabled}"
			style="color: #ff0000" />
	</h:panelGroup>
	<e:outputLabel for="label"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.LABEL']}" />
	<e:text value=" #{departmentsController.department.label}" id="label" />
	<e:outputLabel for="xlabel"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.XLABEL']}" />
	<e:text value=" #{departmentsController.department.xlabel}" id="xlabel" />
	<e:outputLabel for="autoExpire"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.AUTO_EXPIRE']}" />
	<e:text
		value=" #{departmentsController.department.autoExpire == null ? msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.AUTO_EXPIRE_DEFAULT'] : msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.AUTO_EXPIRE_VALUE']}"
		id="autoExpire">
		<f:param value="#{departmentsController.department.autoExpire}" />
	</e:text>
	<e:outputLabel for="filter"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.FILTER']}" />
	<e:text value=" #{departmentsController.department.filter}" id="filter" />
	<e:outputLabel for="spentTimeNeeded"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.SPENT_TIME_NEEDED']}" />
	<e:text
		value=" #{msgs[departmentsController.department.spentTimeNeeded ? 'DEPARTMENT_VIEW.TEXT.PROPERTIES.SPENT_TIME_NEEDED_TRUE' : 'DEPARTMENT_VIEW.TEXT.PROPERTIES.SPENT_TIME_NEEDED_FALSE']}"
		id="spentTimeNeeded" />
	<e:outputLabel for="redirection"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.REDIRECTION']}" />
	<h:panelGroup>
		<t:graphicImage value="/media/images/redirection.png" rendered="#{departmentsController.department.virtual}"/>
		<e:text
			value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.REDIRECTION_VALUE']}"
			rendered="#{departmentsController.department.virtual}">
			<f:param
				value="#{departmentsController.department.realDepartment.label}" />
		</e:text>
		<h:panelGroup
			rendered="#{not empty departmentsController.virtualDepartments}">
			<t:dataList value="#{departmentsController.virtualDepartments}"
				var="virtualDepartment" rowIndexVar="virtualDepartmentIndex">
				<t:htmlTag value="br" />
				<t:graphicImage value="/media/images/redirection-inverted.png" />
				<e:text
					value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.REDIRECTION_VALUE_INVERTED']}">
					<f:param value="#{virtualDepartment.label}" />
				</e:text>
			</t:dataList>
		</h:panelGroup>
		<e:italic
			value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.REDIRECTION_NONE']}"
			rendered="#{not departmentsController.department.virtual and empty departmentsController.virtualDepartments}" />
	</h:panelGroup>
	<e:outputLabel for="defaultTicketScope"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.DEFAULT_TICKET_SCOPE']}" 
		rendered="#{not departmentsController.department.virtual}" />
	<h:panelGroup id="defaultTicketScope" 
		rendered="#{not departmentsController.department.virtual}" >
		<e:text
			value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.DEFAULT_TICKET_SCOPE_DEFAULT']}"
			rendered="#{departmentsController.department.defaultTicketScope == 'DEFAULT'}" >
			<f:param
				value="#{msgs[ticketScopeI18nKeyProvider[domainService.departmentDefaultTicketScope]]}" />
		</e:text>
		<e:text
			value=" #{msgs[ticketScopeI18nKeyProvider[departmentsController.department.defaultTicketScope]]}"
			rendered="#{departmentsController.department.defaultTicketScope != 'DEFAULT'}" />
	</h:panelGroup>
	<e:outputLabel for="visibilityInterSrv"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.VISIBILITE_INTER_SRV']}" />
	<e:text value=" #{departmentsController.department.visibilityInterSrv}" id="visibilityInterSrv"/>	
	<e:outputLabel for="srvAnonymous"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.SERVICE_ANONYMOUS']}" />
	<e:text value=" #{departmentsController.department.srvAnonymous ? msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.SERVICE_ANONYMOUS_TRUE'] : msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.SERVICE_ANONYMOUS_FALSE']}" id="srvAnonymous"/>	
	<e:outputLabel for="defaultTicketLabel"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.DEFAULT_TICKET_LABEL']}" 
		rendered="#{not departmentsController.department.virtual}" />
	<e:text
		value=" #{departmentsController.department.defaultTicketLabel == null ? msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.DEFAULT_TICKET_LABEL_DEFAULT'] : departmentsController.department.defaultTicketLabel}"
		id="defaultTicketLabel"
		rendered="#{not departmentsController.department.virtual}" />
	<e:outputLabel for="defaultTicketMessage"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.DEFAULT_TICKET_MESSAGE']}"
		rendered="#{not departmentsController.department.virtual}" />
	<e:text
		value=" #{departmentsController.department.defaultTicketMessage == null ? msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.DEFAULT_TICKET_MESSAGE_DEFAULT'] : departmentsController.department.defaultTicketMessage}"
		id="defaultTicketMessage" 
		escape="false"
		rendered="#{not departmentsController.department.virtual}" />
	<e:outputLabel for="defaultTicketPriority"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.DEFAULT_TICKET_PRIORITY']}"
		rendered="#{not departmentsController.department.virtual}" />
	<e:text
		value=" #{msgs[priorityI18nKeyProvider[departmentsController.department.defaultTicketPriority]]}"
		id="defaultTicketPriority"
		rendered="#{not departmentsController.department.virtual}" />
	<e:outputLabel for="defaultAssignmentAlgorithm"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.ASSIGNMENT_ALGORITHM']}" 
		rendered="#{not departmentsController.department.virtual}" />
	<h:panelGroup id="defaultAssignmentAlgorithm" 
		rendered="#{not departmentsController.department.virtual}" >
		<e:text
			value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.ASSIGNMENT_ALGORITHM_DEFAULT']}"
			rendered="#{departmentsController.department.assignmentAlgorithmName == null}" >
			<f:param
				value="#{assignmentAlgorithmI18nDescriptionProvider[domainService.defaultAssignmentAlgorithmName]}" />
		</e:text>
		<e:text
			value=" #{assignmentAlgorithmI18nDescriptionProvider[departmentsController.department.assignmentAlgorithmName]}"
			rendered="#{departmentsController.department.assignmentAlgorithmName != null}" />
	</h:panelGroup>
	<e:outputLabel for="defaultComputerUrlBuilder"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.COMPUTER_URL_BUILDER']}" 
		rendered="#{not departmentsController.department.virtual}" />
	<h:panelGroup id="defaultComputerUrlBuilder" 
		rendered="#{not departmentsController.department.virtual}" >
		<e:text
			value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.COMPUTER_URL_BUILDER_DEFAULT']}"
			rendered="#{departmentsController.department.computerUrlBuilderName == null}" >
			<f:param
				value="#{computerUrlBuilderI18nDescriptionProvider[domainService.defaultComputerUrlBuilderName]}" />
		</e:text>
		<e:text
			value=" #{computerUrlBuilderI18nDescriptionProvider[departmentsController.department.computerUrlBuilderName]}"
			rendered="#{departmentsController.department.computerUrlBuilderName != null}" />
	</h:panelGroup>
	<e:outputLabel for="hideToExternalUsers"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.HIDDEN_TO_APPLICATION_USERS']}"  
		rendered="#{not departmentsController.department.virtual}" />
	<e:text
		value=" #{msgs[departmentsController.department.hideToExternalUsers ? 'DEPARTMENT_VIEW.TEXT.PROPERTIES.HIDDEN_TO_APPLICATION_USERS_TRUE' : 'DEPARTMENT_VIEW.TEXT.PROPERTIES.HIDDEN_TO_APPLICATION_USERS_FALSE']}"
		id="hideToExternalUsers" 
		rendered="#{not departmentsController.department.virtual}" />
	<e:bold 
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.EXTRA_MONITORING']}" 
		rendered="#{not departmentsController.department.virtual}" />
	<h:panelGroup
		rendered="#{not departmentsController.department.virtual}" >
		<e:text
			rendered="#{departmentsController.department.monitoringEmail != null and departmentsController.department.monitoringLevel != 0}" 
			value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.EXTRA_MONITORING_VALUE']}" >
			<f:param value="#{departmentsController.department.monitoringEmail}" />
			<f:param value="#{msgs[categoryMonitoringI18nKeyProvider[departmentsController.department.monitoringLevel]]}" />
		</e:text>
		<e:italic
			rendered="#{departmentsController.department.monitoringEmail == null or departmentsController.department.monitoringLevel == 0}" 
			value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.EXTRA_MONITORING_NONE']}" />
	</h:panelGroup>
	<e:outputLabel for="ticketsNumber"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.TICKETS_NUMBER']}" />
	<e:text
		value=" #{departmentsController.ticketsNumber}"
		id="ticketsNumber" />
	<e:outputLabel for="archivedTicketsNumber"
		value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.ARCHIVED_TICKETS_NUMBER']}" />
	<e:text
		value=" #{departmentsController.archivedTicketsNumber}"
		id="archivedTicketsNumber" />
</e:panelGrid>
