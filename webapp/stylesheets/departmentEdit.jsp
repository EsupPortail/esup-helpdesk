<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanEditDepartmentProperties}">

	<script type="text/javascript">
function selectIcon(src, value) {
	document.getElementById('departmentEditForm:selectedIconImage').src = src;
	document.getElementById('departmentEditForm:selectedIconMenu').value=value;
	hideElement('departmentEditForm:iconsPanel');
	showElement('departmentEditForm:selectedIconPanel');
}
</script>

	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="departmentEditForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['DEPARTMENT_EDIT.TITLE']}">
				<f:param value="#{departmentsController.department.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentEditForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="cancelButton" action="cancel"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:panelGrid columns="2" columnClasses="colLeftNowrap,colLeft" >
			<e:outputLabel for="id" value="#{msgs['DEPARTMENT_EDIT.TEXT.ID']}" />
			<e:text id="id" value="#{departmentsController.departmentToUpdate.id}" />
			<e:outputLabel for="icon" value="#{msgs['DEPARTMENT_EDIT.TEXT.ICON']}" />
			<h:panelGroup id="icon" >
				<h:panelGroup id="selectedIconPanel" >
					<t:graphicImage id="selectedIconImage" style="cursor: pointer" 
						value="#{departmentIconUrlProvider[departmentsController.departmentToUpdate]}" 
						onclick="showElement('departmentEditForm:iconsPanel');hideElement('departmentEditForm:selectedIconPanel');" />
					<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.ICON_HELP']}" />
					<e:selectOneMenu
						id="selectedIconMenu" style="display: none"
						value="#{departmentsController.departmentToUpdate.icon}" 
						converter="#{iconConverter}" >
						<f:selectItems value="#{departmentsController.iconItems}" />
					</e:selectOneMenu>
				</h:panelGroup>
				<h:panelGroup id="iconsPanel" style="display: none" >
					<t:htmlTag value="hr" />
					<e:text value="#{msgs['DEPARTMENT_EDIT.TEXT.CHOOSE_ICON']}" />
					<t:dataTable newspaperColumns="5" value="#{domainService.icons}" var="icon" 
						columnClasses="colLeftNowrap" >
						<f:facet name="header">
							<h:panelGroup>
								<t:graphicImage value="#{iconUrlProvider[domainService.defaultDepartmentIcon]}" 
									onclick="selectIcon(this.src, '');" style="cursor: pointer" />
								<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_ICON']}" />
							</h:panelGroup>
						</f:facet>
						<h:column>
							<t:graphicImage value="#{iconUrlProvider[icon]}" 
								onclick="selectIcon(this.src, '#{icon.id}');" style="cursor: pointer" />
							<e:text value=" #{icon.name}" />
						</h:column>
					</t:dataTable>
					<t:htmlTag value="hr" />
				</h:panelGroup>
			</h:panelGroup>
			<e:outputLabel for="enabled"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.ENABLED']}" />
			<h:panelGroup>
				<e:selectBooleanCheckbox id="enabled"
					value="#{departmentsController.departmentToUpdate.enabled}" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.ENABLED_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="label"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.LABEL']}" />
			<h:panelGroup>
				<e:inputText id="label" 
					value="#{departmentsController.departmentToUpdate.label}" required="true" />
				<e:message for="label" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.LABEL_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="xlabel"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.XLABEL']}" />
			<h:panelGroup>
				<e:inputText id="xlabel" size="80"
					value="#{departmentsController.departmentToUpdate.xlabel}"
					required="true" />
				<e:message for="xlabel" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.XLABEL_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="realDepartment"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.REAL_DEPARTMENT']}" />
			<h:panelGroup id="realDepartment" >
				<h:panelGroup rendered="#{not departmentsController.departmentToUpdateHasVirtualDepartments and not departmentsController.departmentToUpdateHasCategories}" >
					<h:panelGroup rendered="#{departmentsController.departmentToUpdate.virtual}" >
						<t:graphicImage value="/media/images/redirection.png" />
						<t:graphicImage value="#{departmentIconUrlProvider[departmentsController.departmentToUpdate.realDepartment]}" />
						<e:text value=" #{msgs['DEPARTMENT_EDIT.TEXT.IS_VIRTUAL']}" >
							<f:param value="#{departmentsController.departmentToUpdate.realDepartment.label}" />
						</e:text>
						<t:htmlTag value="br"/>
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentEditForm:setRealButton');" >
							<e:bold value="#{msgs['DEPARTMENT_EDIT.BUTTON.SET_REAL']} " />
							<t:graphicImage value="/media/images/redirection-none.png"
								alt="#{msgs['DEPARTMENT_EDIT.BUTTON.SET_REAL']}" 
								title="#{msgs['DEPARTMENT_EDIT.BUTTON.SET_REAL']}" />
						</h:panelGroup>
						<e:commandButton id="setRealButton" action="#{departmentsController.setRealDepartment}"
							value="#{msgs['DEPARTMENT_EDIT.BUTTON.SET_REAL']}" immediate="true" >
							<t:updateActionListener value="#{null}" property="#{departmentsController.targetDepartment}" />
						</e:commandButton>
						<t:htmlTag value="br"/>
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentEditForm:changeRealButton');" >
							<e:bold value=" #{msgs['DEPARTMENT_EDIT.BUTTON.CHANGE_REAL']} " />
							<t:graphicImage value="/media/images/redirection.png"
								alt="#{msgs['DEPARTMENT_EDIT.BUTTON.CHANGE_REAL']}" 
								title="#{msgs['DEPARTMENT_EDIT.BUTTON.CHANGE_REAL']}" />
						</h:panelGroup>
						<e:commandButton id="changeRealButton" action="#{departmentsController.gotoSetRealDepartment}"
							value="#{msgs['DEPARTMENT_EDIT.BUTTON.CHANGE_REAL']}" immediate="true" style="display: none" />
					</h:panelGroup>
					<h:panelGroup rendered="#{not departmentsController.departmentToUpdate.virtual}" >
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentEditForm:setVirtualButton');" >
							<e:bold value=" #{msgs['DEPARTMENT_EDIT.BUTTON.SET_VIRTUAL']} " />
							<t:graphicImage value="/media/images/redirection.png"
								alt="#{msgs['DEPARTMENT_EDIT.BUTTON.SET_VIRTUAL']}" 
								title="#{msgs['DEPARTMENT_EDIT.BUTTON.SET_VIRTUAL']}" />
						</h:panelGroup>
						<e:commandButton id="setVirtualButton" action="#{departmentsController.gotoSetRealDepartment}"
							value="#{msgs['DEPARTMENT_EDIT.BUTTON.SET_VIRTUAL']}" immediate="true" style="display: none" />
						<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.SET_VIRTUAL_HELP']}" />
					</h:panelGroup>
				</h:panelGroup>
				<h:panelGroup rendered="#{departmentsController.departmentToUpdateHasVirtualDepartments}" >
					<e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_VIRTUAL_DEPARTMENTS.PREFIX']} " />
					<t:dataList id="virtualData" value="#{departmentsController.departmentToUpdateVirtualDepartments}"
						var="department" rowIndexVar="variable" >
						<e:italic rendered="#{variable != 0}" value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_VIRTUAL_DEPARTMENTS.SEPARATOR']} " />
						<e:italic value="#{department.label}" />
					</t:dataList>
					<e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_VIRTUAL_DEPARTMENTS.SUFFIX']} " />
					<t:htmlTag value="br"/>
				</h:panelGroup>
				<h:panelGroup rendered="#{departmentsController.departmentToUpdateHasCategories}" >
					<e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_CATEGORIES.PREFIX']} " />
					<t:dataList id="subData" value="#{departmentsController.departmentToUpdateCategories}"
						var="category" rowIndexVar="variable" >
						<e:italic rendered="#{variable != 0}" value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_CATEGORIES.SEPARATOR']} " />
						<e:italic value="#{category.label}" />
					</t:dataList>
					<e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_CATEGORIES.SUFFIX']} " />
					<t:htmlTag value="br"/>
				</h:panelGroup>
				<e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.NO_REDIRECTION_POSSIBLE']}" 
					rendered="#{departmentsController.departmentToUpdateHasVirtualDepartments or departmentsController.departmentToUpdateHasCategories}" />
			</h:panelGroup>
			<e:outputLabel for="autoExpire"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.AUTO_EXPIRE']}" />
			<h:panelGroup>
				<e:inputText id="autoExpire"
					value="#{departmentsController.departmentToUpdate.autoExpire}" 
					size="3"/>
				<e:message for="autoExpire" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.AUTO_EXPIRE_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="spentTimeNeeded"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.SPENT_TIME_NEEDED']}" />
			<h:panelGroup>
				<e:selectBooleanCheckbox id="spentTimeNeeded"
					value="#{departmentsController.departmentToUpdate.spentTimeNeeded}" />
				<e:message for="spentTimeNeeded" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.SPENT_TIME_NEEDED_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="filter"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.FILTER']}" />
			<h:panelGroup>
				<e:inputText id="filter"
					value="#{departmentsController.departmentToUpdate.filter}" />
				<e:message for="filter" />
			</h:panelGroup>
			<e:outputLabel for="defaultTicketScope"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_SCOPE']}" 
				rendered="#{not departmentsController.departmentToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.departmentToUpdate.virtual}" >
				<e:selectOneMenu id="defaultTicketScope" 
					value="#{departmentsController.departmentToUpdate.defaultTicketScope}" >
					<f:selectItems value="#{departmentsController.scopeItems}" />
				</e:selectOneMenu>
				<e:message for="defaultTicketScope" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_SCOPE_HELP']}" >
					<f:param value="#{msgs[ticketScopeI18nKeyProvider[domainService.departmentDefaultTicketScope]]}" />
				</e:italic>
			</h:panelGroup>
			<e:outputLabel for="visibilityInterSrv"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.VISIBILITE_INTER_SRV']}" />
			<h:panelGroup>
				<e:inputText id="visibilityInterSrv"
					value="#{departmentsController.departmentToUpdate.visibilityInterSrv}" />
				<e:message for="visibilityInterSrv" />
			</h:panelGroup>
			<e:outputLabel for="srvAnonymous"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.SERVICE_ANONYMOUS']}" />
			<h:panelGroup>
				<e:selectBooleanCheckbox id="srvAnonymous"
					value="#{departmentsController.departmentToUpdate.srvAnonymous}" />
				<e:message for="srvAnonymous" />
			</h:panelGroup>			
			<e:outputLabel for="defaultTicketPriority"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_PRIORITY']}" 
				rendered="#{not departmentsController.departmentToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.departmentToUpdate.virtual}" >
				<e:selectOneMenu id="defaultTicketPriority" 
					value="#{departmentsController.departmentToUpdate.defaultTicketPriority}" >
					<f:selectItems value="#{departmentsController.priorityItems}" />
				</e:selectOneMenu>
				<e:message for="defaultTicketPriority" />
			</h:panelGroup>
			<e:outputLabel for="defaultTicketLabel"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_LABEL']}"
				rendered="#{not departmentsController.departmentToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.departmentToUpdate.virtual}" >
				<e:inputText id="defaultTicketLabel"
					value="#{departmentsController.departmentToUpdate.defaultTicketLabel}" />
				<e:message for="defaultTicketLabel" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_LABEL_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="defaultTicketMessage"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_MESSAGE']}"
				rendered="#{not departmentsController.departmentToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.departmentToUpdate.virtual}" >
				<fck:editor  
					id="defaultTicketMessage" 
					value="#{departmentsController.departmentToUpdate.defaultTicketMessage}" 
					toolbarSet="actionMessage" />
				<e:message for="defaultTicketMessage" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_MESSAGE_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="assignmentAlgorithm"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.ASSIGNMENT_ALGORITHM']}" 
				rendered="#{not departmentsController.departmentToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.departmentToUpdate.virtual}" >
				<e:selectOneMenu id="assignmentAlgorithm" 
					value="#{departmentsController.departmentToUpdate.assignmentAlgorithmName}" >
					<f:selectItems value="#{departmentsController.assignmentAlgorithmItems}" />
				</e:selectOneMenu>
				<e:message for="assignmentAlgorithm" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.ASSIGNMENT_ALGORITHM_HELP']}" >
					<f:param value="#{assignmentAlgorithmI18nDescriptionProvider[domainService.defaultAssignmentAlgorithmName]}" />
				</e:italic>
			</h:panelGroup>
			<e:outputLabel for="computerUrlBuilder"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.COMPUTER_URL_BUILDER']}" 
				rendered="#{not departmentsController.departmentToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.departmentToUpdate.virtual}" >
				<e:selectOneMenu id="computerUrlBuilder" 
					value="#{departmentsController.departmentToUpdate.computerUrlBuilderName}" >
					<f:selectItems value="#{departmentsController.computerUrlBuilderItems}" />
				</e:selectOneMenu>
				<e:message for="computerUrlBuilder" />
				<e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.COMPUTER_URL_BUILDER_HELP']}" >
					<f:param value="#{computerUrlBuilderI18nDescriptionProvider[domainService.defaultComputerUrlBuilderName]}" />
				</e:italic>
			</h:panelGroup>
			<e:outputLabel for="hideToExternalUsers"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.HIDDEN_TO_APPLICATION_USERS']}" 
				rendered="#{not departmentsController.departmentToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.departmentToUpdate.virtual}">
				<e:selectBooleanCheckbox id="hideToExternalUsers"
					value="#{departmentsController.departmentToUpdate.hideToExternalUsers}" />
				<e:message for="hideToExternalUsers" />
				<e:italic
					value=" #{msgs['DEPARTMENT_EDIT.TEXT.HIDDEN_TO_APPLICATION_USERS_HELP']}" />
			</h:panelGroup>
			<e:bold 
				value="#{msgs['DEPARTMENT_EDIT.TEXT.EXTRA_MONITORING']}" 
				rendered="#{not departmentsController.departmentToUpdate.virtual}" />
			<e:panelGrid columns="2" columnClasses="colLeftNowrap,colLeft"
				rendered="#{not departmentsController.departmentToUpdate.virtual}" >
				<e:outputLabel for="monitoringEmail"
					value="#{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_EMAIL']}" />
				<h:panelGroup >
					<e:inputText id="monitoringEmail" size="80"
						value="#{departmentsController.departmentToUpdate.monitoringEmail}" />
					<t:htmlTag value="br" />
					<e:italic
						value=" #{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_EMAIL_HELP']}" />
				</h:panelGroup>
				<e:outputLabel id="monitoringEmailAuthTypeLabel" for="monitoringEmailAuthType"
					value="#{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_EMAIL_AUTH_TYPE']}" />
				<h:panelGroup id="monitoringEmailAuthTypeData" >
					<e:selectOneMenu id="monitoringEmailAuthType" 
						value="#{departmentsController.departmentToUpdate.monitoringEmailAuthType}" >
						<f:selectItems value="#{departmentsController.monitoringEmailAuthTypeItems}" />
					</e:selectOneMenu>
					<e:italic
						value=" #{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_EMAIL_AUTH_TYPE_HELP']}" />
				</h:panelGroup>
				<e:outputLabel id="monitoringLevelLabel" for="monitoringLevel"
					value="#{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_LEVEL']}" />
				<h:panelGroup id="monitoringLevelData" >
					<e:selectOneMenu id="monitoringLevel" 
						value="#{departmentsController.departmentToUpdate.monitoringLevel}" >
						<f:selectItems value="#{departmentsController.monitoringLevelItems}" />
					</e:selectOneMenu>
				</h:panelGroup>
			</e:panelGrid>
			<h:panelGroup />
			<h:panelGroup >
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentEditForm:updateButton');" >
					<e:bold value="#{msgs['DEPARTMENT_EDIT.BUTTON.UPDATE']} " />
					<t:graphicImage value="/media/images/save.png"
						alt="#{msgs['DEPARTMENT_EDIT.BUTTON.UPDATE']}" 
						title="#{msgs['DEPARTMENT_EDIT.BUTTON.UPDATE']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="updateButton" 
					action="#{departmentsController.updateDepartment}"
					value="#{msgs['DEPARTMENT_EDIT.BUTTON.UPDATE']}" />
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		focusElement("departmentEditForm:label");
	</script>
</e:page>
