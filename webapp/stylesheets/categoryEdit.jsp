<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">

	<script type="text/javascript">
function selectIcon(src, value) {
	document.getElementById('categoryEditForm:selectedIconImage').src = src;
	document.getElementById('categoryEditForm:selectedIconMenu').value=value;
	hideElement('categoryEditForm:iconsPanel');
	showElement('categoryEditForm:selectedIconPanel');
}
</script>

	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="categoryEditForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['CATEGORY_EDIT.TITLE']}">
				<f:param value="#{departmentsController.categoryToUpdate.department.label}" />
				<f:param value="#{departmentsController.categoryToUpdate.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryEditForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.BACK']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.BACK']}" 
						title="#{msgs['_.BUTTON.BACK']}" />
				</h:panelGroup>
				<e:commandButton id="cancelButton" action="cancel"
					value="#{msgs['_.BUTTON.BACK']}" immediate="true" style="display: none" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:panelGrid columns="2" columnClasses="colLeftNowrap,colLeft" >
			<e:outputLabel for="id" value="#{msgs['CATEGORY_EDIT.TEXT.ID']}" />
			<e:text id="id" value="#{departmentsController.categoryToUpdate.id}" />
			<e:outputLabel for="icon" value="#{msgs['CATEGORY_EDIT.TEXT.ICON']}" />
			<h:panelGroup id="icon" >
				<h:panelGroup id="selectedIconPanel" >
					<t:graphicImage id="selectedIconImage" style="cursor: pointer" 
						value="#{categoryIconUrlProvider[departmentsController.categoryToUpdate]}" 
						onclick="showElement('categoryEditForm:iconsPanel');hideElement('categoryEditForm:selectedIconPanel');" />
					<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.ICON_HELP']}" />
					<e:selectOneMenu
						id="selectedIconMenu" style="display: none"
						value="#{departmentsController.categoryToUpdate.icon}" 
						converter="#{iconConverter}" >
						<f:selectItems value="#{departmentsController.iconItems}" />
					</e:selectOneMenu>
				</h:panelGroup>
				<h:panelGroup id="iconsPanel" style="display: none" >
					<t:htmlTag value="hr" />
					<e:text value="#{msgs['CATEGORY_EDIT.TEXT.CHOOSE_ICON']}" />
					<t:dataTable newspaperColumns="5" value="#{domainService.icons}" var="icon" 
						columnClasses="colLeftNowrap" >
						<f:facet name="header">
							<h:panelGroup>
								<t:graphicImage value="#{iconUrlProvider[domainService.defaultCategoryIcon]}" 
									onclick="selectIcon(this.src, '');" style="cursor: pointer" />
								<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_ICON']}" />
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
			<e:outputLabel for="realCategory"
				value="#{msgs['CATEGORY_EDIT.TEXT.REAL_CATEGORY']}" />
			<h:panelGroup id="realCategory" >
				<h:panelGroup rendered="#{not departmentsController.categoryToUpdateHasSubCategories}" >
					<h:panelGroup rendered="#{departmentsController.categoryToUpdate.virtual}" >
						<t:graphicImage value="/media/images/redirection.png" />
						<t:graphicImage value="#{categoryIconUrlProvider[departmentsController.categoryToUpdate.realCategory]}" />
						<e:text value=" #{msgs['CATEGORY_EDIT.TEXT.IS_VIRTUAL']}" >
							<f:param value="#{departmentsController.categoryToUpdate.realCategory.department.label}" />
							<f:param value="#{departmentsController.categoryToUpdate.realCategory.label}" />
						</e:text>
						<t:htmlTag value="br"/>
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryEditForm:setRealButton');" >
							<e:bold value="#{msgs['CATEGORY_EDIT.BUTTON.SET_REAL']} " />
							<t:graphicImage value="/media/images/redirection-none.png"
								alt="#{msgs['CATEGORY_EDIT.BUTTON.SET_REAL']}" 
								title="#{msgs['CATEGORY_EDIT.BUTTON.SET_REAL']}" />
						</h:panelGroup>
						<e:commandButton id="setRealButton" action="#{departmentsController.setRealCategory}"
							value="#{msgs['CATEGORY_EDIT.BUTTON.SET_REAL']}" immediate="true" style="display: none" >
							<t:updateActionListener value="#{null}" property="#{departmentsController.targetCategory}" />
						</e:commandButton>
						<t:htmlTag value="br"/>
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryEditForm:changeRealButton');" >
							<e:bold value=" #{msgs['CATEGORY_EDIT.BUTTON.CHANGE_REAL']} " />
							<t:graphicImage value="/media/images/redirection.png"
								alt="#{msgs['CATEGORY_EDIT.BUTTON.CHANGE_REAL']}" 
								title="#{msgs['CATEGORY_EDIT.BUTTON.CHANGE_REAL']}" />
						</h:panelGroup>
						<e:commandButton id="changeRealButton" action="#{departmentsController.gotoSetRealCategory}"
							value="#{msgs['CATEGORY_EDIT.BUTTON.CHANGE_REAL']}" immediate="true" style="display: none" />
					</h:panelGroup>
					<h:panelGroup rendered="#{not departmentsController.categoryToUpdate.virtual}" >
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryEditForm:setVirtualButton');" >
							<e:bold value=" #{msgs['CATEGORY_EDIT.BUTTON.SET_VIRTUAL']} " />
							<t:graphicImage value="/media/images/redirection.png"
								alt="#{msgs['CATEGORY_EDIT.BUTTON.SET_VIRTUAL']}" 
								title="#{msgs['CATEGORY_EDIT.BUTTON.SET_VIRTUAL']}" />
						</h:panelGroup>
						<e:commandButton id="setVirtualButton" action="#{departmentsController.gotoSetRealCategory}"
							value="#{msgs['CATEGORY_EDIT.BUTTON.SET_VIRTUAL']}" immediate="true" style="display: none" />
						<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.SET_VIRTUAL_HELP']}" />
					</h:panelGroup>
				</h:panelGroup>
				<h:panelGroup rendered="#{departmentsController.categoryToUpdateHasSubCategories}" >
					<e:italic value="#{msgs['CATEGORY_EDIT.TEXT.HAS_SUB_CATEGORIES.PREFIX']}" />
					<t:dataList id="subData" value="#{departmentsController.categoryToUpdateSubCategories}"
						var="category" rowIndexVar="variable">
						<e:italic rendered="#{variable != 0}" value="#{msgs['CATEGORY_EDIT.TEXT.HAS_SUB_CATEGORIES.SEPARATOR']} " />
						<e:italic value="#{category.label}"/>
					</t:dataList>
					<e:italic value="#{msgs['CATEGORY_EDIT.TEXT.HAS_SUB_CATEGORIES.SUFFIX']} " />
					<t:htmlTag value="br"/>
					<e:italic value="#{msgs['CATEGORY_EDIT.TEXT.NO_REDIRECTION_POSSIBLE']}" />
				</h:panelGroup>
			</h:panelGroup>
			<e:outputLabel for="label"
				value="#{msgs['CATEGORY_EDIT.TEXT.LABEL']}" />
			<h:panelGroup>
				<e:inputText id="label" size="30"
					value="#{departmentsController.categoryToUpdate.label}" required="true" />
				<e:message for="label" />
				<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.LABEL_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="xlabel"
				value="#{msgs['CATEGORY_EDIT.TEXT.XLABEL']}" />
			<h:panelGroup>
				<e:inputText id="xlabel" size="80"
					value="#{departmentsController.categoryToUpdate.xlabel}"
					required="true" />
				<e:message for="xlabel" />
				<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.XLABEL_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="autoExpire"
				value="#{msgs['CATEGORY_EDIT.TEXT.AUTO_EXPIRE']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.categoryToUpdate.virtual}" >
				<e:inputText id="autoExpire" size="4"
					value="#{departmentsController.categoryToUpdate.autoExpire}" />
				<e:message for="autoExpire" />
				<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.AUTO_EXPIRE_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="defaultTicketScope"
				value="#{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_SCOPE']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.categoryToUpdate.virtual}" >
				<e:selectOneMenu id="defaultTicketScope" 
					value="#{departmentsController.categoryToUpdate.defaultTicketScope}" >
					<f:selectItems value="#{departmentsController.scopeItems}" />
				</e:selectOneMenu>
				<e:message for="defaultTicketScope" />
				<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_SCOPE_HELP']}" >
					<f:param value="#{msgs[ticketScopeI18nKeyProvider[departmentsController.categoryToUpdate.defaultDefaultTicketScope]]}" />
				</e:italic>
			</h:panelGroup>
			<e:outputLabel for="defaultTicketPriority"
				value="#{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_PRIORITY']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.categoryToUpdate.virtual}" >
				<e:selectOneMenu id="defaultTicketPriority" 
					value="#{departmentsController.categoryToUpdate.defaultTicketPriority}" >
					<f:selectItems value="#{departmentsController.priorityItems}" />
				</e:selectOneMenu>
				<e:message for="defaultTicketPriority" />
				<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_PRIORITY_HELP']}" >
					<f:param value="#{msgs[priorityI18nKeyProvider[departmentsController.categoryToUpdate.defaultDefaultTicketPriority]]}" />
				</e:italic>
			</h:panelGroup>
			<e:outputLabel for="defaultTicketLabel"
				value="#{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_LABEL']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.categoryToUpdate.virtual}" >
				<e:inputText id="defaultTicketLabel" size="50"
					value="#{departmentsController.categoryToUpdate.defaultTicketLabel}" />
				<e:message for="defaultTicketLabel" />
				<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_LABEL_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="defaultTicketMessage"
				value="#{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_MESSAGE']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.categoryToUpdate.virtual}" >
				<fck:editor  
					id="defaultTicketMessage" 
					value="#{departmentsController.categoryToUpdate.defaultTicketMessage}" 
					toolbarSet="actionMessage" />
				<e:message for="defaultTicketMessage" />
				<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_MESSAGE_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="addNewTickets"
				value="#{msgs['CATEGORY_EDIT.TEXT.ADD_NEW_TICKETS']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual and departmentsController.categoryToUpdateHasSubCategories}" />
			<h:panelGroup
				rendered="#{not departmentsController.categoryToUpdate.virtual and departmentsController.categoryToUpdateHasSubCategories}" >
				<e:selectBooleanCheckbox id="addNewTickets"
					value="#{departmentsController.categoryToUpdate.addNewTickets}" />
				<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.ADD_NEW_TICKETS_HELP']}" />
			</h:panelGroup>
			<e:outputLabel for="hideToExternalUsers"
				value="#{msgs['CATEGORY_EDIT.TEXT.HIDDEN_TO_APPLICATION_USERS']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual and not departmentController.categoryToUpdate.department.hideToExternalUsers}" />
			<h:panelGroup>
				<h:panelGroup
					rendered="#{not departmentsController.categoryToUpdate.virtual and not departmentController.categoryToUpdate.department.hideToExternalUsers}">
					<e:selectBooleanCheckbox id="hideToExternalUsers"
						value="#{departmentsController.categoryToUpdate.hideToExternalUsers}" />
					<e:italic
						value=" #{msgs['CATEGORY_EDIT.TEXT.HIDDEN_TO_APPLICATION_USERS_HELP']}" />
				</h:panelGroup>
			</h:panelGroup>
			<e:outputLabel for="assignmentAlgorithm"
				value="#{msgs['CATEGORY_EDIT.TEXT.ASSIGNMENT_ALGORITHM']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual}" />
			<h:panelGroup
				rendered="#{not departmentsController.categoryToUpdate.virtual}" >
				<e:selectOneMenu id="assignmentAlgorithm" 
					value="#{departmentsController.categoryToUpdate.assignmentAlgorithmName}" >
					<f:selectItems value="#{departmentsController.assignmentAlgorithmItems}" />
				</e:selectOneMenu>
				<e:message for="assignmentAlgorithm" />
				<e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.ASSIGNMENT_ALGORITHM_HELP']}" >
					<f:param value="#{assignmentAlgorithmI18nDescriptionProvider[departmentsController.categoryToUpdateDefaultAssignmentAlgorithmName]}" />
				</e:italic>
			</h:panelGroup>
			<e:bold
				value="#{msgs['CATEGORY_EDIT.TEXT.EXTRA_MONITORING']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual}" />
			<h:panelGroup>
				<h:panelGroup
					rendered="#{not departmentsController.categoryToUpdate.virtual}">
					<e:panelGrid columns="2" columnClasses="colLeftNowrap,colLeft" >
						<e:outputLabel for="inheritMonitoring"
							value="#{msgs['CATEGORY_EDIT.TEXT.INHERIT_MONITORING']}" />
						<h:panelGroup>
							<e:selectBooleanCheckbox id="inheritMonitoring"
								value="#{departmentsController.categoryToUpdate.inheritMonitoring}" 
								onchange="showHideElement('categoryEditForm:monitoringEmailLabel');showHideElement('categoryEditForm:monitoringEmailData');showHideElement('categoryEditForm:monitoringEmailAuthTypeLabel');showHideElement('categoryEditForm:monitoringEmailAuthTypeData');showHideElement('categoryEditForm:monitoringLevelLabel');showHideElement('categoryEditForm:monitoringLevelData');" />
							<e:italic
								value=" #{msgs['CATEGORY_EDIT.TEXT.INHERIT_MONITORING_HELP']}" />
							<t:htmlTag value="br" />
							<e:text
								value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_NO_MONITORING']}" 
								rendered="#{departmentsController.categoryToUpdate.defaultMonitoringEmail == null or departmentsController.categoryToUpdate.defaultMonitoringLevel == 0}" />
							<e:text
								value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_MONITORING']}" 
								rendered="#{departmentsController.categoryToUpdate.defaultMonitoringEmail != null and departmentsController.categoryToUpdate.defaultMonitoringLevel != 0}" >
								<f:param value="#{msgs[categoryMonitoringI18nKeyProvider[departmentsController.categoryToUpdate.defaultMonitoringLevel]]}" />
								<f:param value="#{departmentsController.categoryToUpdate.defaultMonitoringEmail}" />
							</e:text>
						</h:panelGroup>
						<e:outputLabel id="monitoringEmailLabel" for="monitoringEmail"
							value="#{msgs['CATEGORY_EDIT.TEXT.MONITORING_EMAIL']}" 
							style="display: #{not departmentsController.categoryToUpdate.inheritMonitoring ? 'block' : 'none'}" />
						<h:panelGroup>
							<h:panelGroup id="monitoringEmailData" 
								style="display: #{not departmentsController.categoryToUpdate.inheritMonitoring ? 'block' : 'none'}" >
								<e:inputText id="monitoringEmail" size="80"
									value="#{departmentsController.categoryToUpdate.monitoringEmail}" />
								<t:htmlTag value="br" />
								<e:italic
									value=" #{msgs['CATEGORY_EDIT.TEXT.MONITORING_EMAIL_HELP']}" />
							</h:panelGroup>
						</h:panelGroup>
						<e:outputLabel id="monitoringLevelLabel" for="monitoringLevel"
							value="#{msgs['CATEGORY_EDIT.TEXT.MONITORING_LEVEL']}" 
							style="display: #{not departmentsController.categoryToUpdate.inheritMonitoring ? 'block' : 'none'}" />
						<h:panelGroup id="monitoringLevelData" 
							style="display: #{not departmentsController.categoryToUpdate.inheritMonitoring ? 'block' : 'none'}" >
							<e:selectOneMenu id="monitoringLevel" 
								value="#{departmentsController.categoryToUpdate.monitoringLevel}" >
								<f:selectItems value="#{departmentsController.monitoringLevelItems}" />
							</e:selectOneMenu>
						</h:panelGroup>
						<e:outputLabel id="monitoringEmailAuthTypeLabel" for="monitoringEmailAuthType"
							value="#{msgs['CATEGORY_EDIT.TEXT.MONITORING_EMAIL_AUTH_TYPE']}" 
							style="display: #{not departmentsController.categoryToUpdate.inheritMonitoring ? 'block' : 'none'}" />
						<h:panelGroup id="monitoringEmailAuthTypeData" 
							style="display: #{not departmentsController.categoryToUpdate.inheritMonitoring ? 'block' : 'none'}" >
							<e:selectOneMenu id="monitoringEmailAuthType" 
								value="#{departmentsController.categoryToUpdate.monitoringEmailAuthType}" >
								<f:selectItems value="#{departmentsController.monitoringEmailAuthTypeItems}" />
							</e:selectOneMenu>
							<e:italic
								value=" #{msgs['CATEGORY_EDIT.TEXT.MONITORING_EMAIL_AUTH_TYPE_HELP']}" />
						</h:panelGroup>
					</e:panelGrid>
				</h:panelGroup>
			</h:panelGroup>
			<e:outputLabel for="permLinks"
				value="#{msgs['CATEGORY_EDIT.TEXT.ADD_LINK']}" 
				rendered="#{not departmentsController.categoryToUpdate.virtual}" />
			<h:panelGroup id="permLinks" rendered="#{not departmentsController.categoryToUpdate.virtual}" >
				<e:text value=" #{msgs['COMMON.PERM_LINKS.APPLICATION']}" escape="false" >
					<f:param value="#{departmentsController.categoryToUpdateApplicationAddLink}" />
				</e:text>
				<e:text value=" #{msgs['COMMON.PERM_LINKS.CAS']}" escape="false" >
					<f:param value="#{departmentsController.categoryToUpdateCasAddLink}" />
				</e:text>
				<e:text value=" #{msgs['COMMON.PERM_LINKS.SHIBBOLETH']}" escape="false" >
					<f:param value="#{departmentsController.categoryToUpdateShibbolethAddLink}" />
				</e:text>
			</h:panelGroup>
			<h:panelGroup />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryEditForm:updateButton');" >
					<e:bold value="#{msgs['CATEGORY_EDIT.BUTTON.UPDATE']} " />
					<t:graphicImage value="/media/images/save.png"
						alt="#{msgs['CATEGORY_EDIT.BUTTON.UPDATE']}" 
						title="#{msgs['CATEGORY_EDIT.BUTTON.UPDATE']}" />
				</h:panelGroup>
				<e:commandButton id="updateButton" action="#{departmentsController.updateCategory}"
					value="#{msgs['CATEGORY_EDIT.BUTTON.UPDATE']}" style="display: none" />
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
