<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanViewDepartment}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="departmentManagerEditForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['DEPARTMENT_MANAGER_EDIT.TITLE']}">
				<f:param value="#{userFormatter[departmentsController.departmentManagerToUpdate.user]}" />
				<f:param value="#{departmentsController.department.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentManagerEditForm:cancelButton');" >
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

		<h:panelGrid columns="2">
			<e:selectBooleanCheckbox id="manageProperties"
				value="#{departmentsController.departmentManagerToUpdate.manageProperties}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="manageProperties"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_PROPERTIES']}" />
			<e:selectBooleanCheckbox id="managerManagers"
				value="#{departmentsController.departmentManagerToUpdate.manageManagers}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="managerManagers"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_MANAGERS']}" />
			<e:selectBooleanCheckbox id="manageCategories"
				value="#{departmentsController.departmentManagerToUpdate.manageCategories}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="manageCategories"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_CATEGORIES']}" />
			<e:selectBooleanCheckbox id="manageFaq"
				value="#{departmentsController.departmentManagerToUpdate.manageFaq}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="manageFaq"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_FAQ']}" />
			<e:selectBooleanCheckbox id="refuseTicket"
				value="#{departmentsController.departmentManagerToUpdate.refuseTicket}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="refuseTicket"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.REFUSE_TICKET']}" />
			<e:selectBooleanCheckbox id="takeAlreadyAssignedTicket"
				value="#{departmentsController.departmentManagerToUpdate.takeAlreadyAssignedTicket}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="takeAlreadyAssignedTicket"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.TAKE_ALREADY_ASSIGNED_TICKET']}" />
			<e:selectBooleanCheckbox id="takeFreeTicket"
				value="#{departmentsController.departmentManagerToUpdate.takeFreeTicket}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="takeFreeTicket"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.TAKE_FREE_TICKET']}" />
			<e:selectBooleanCheckbox id="assignTicket"
				value="#{departmentsController.departmentManagerToUpdate.assignTicket}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="assignTicket"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.ASSIGN_TICKET']}" />
			<e:selectBooleanCheckbox id="modifyTicketDepartment"
				value="#{departmentsController.departmentManagerToUpdate.modifyTicketDepartment}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="modifyTicketDepartment"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MODIFY_TICKET_DEPARTMENT']}" />
			<e:selectBooleanCheckbox id="reopenAllTickets"
				value="#{departmentsController.departmentManagerToUpdate.reopenAllTickets}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="reopenAllTickets"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.REOPEN_ALL_TICKETS']}" />
			<e:selectBooleanCheckbox id="setOwnAvailability"
				value="#{departmentsController.departmentManagerToUpdate.setOwnAvailability}"
				disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
			<e:outputLabel for="setOwnAvailability"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.SET_OWN_AVAILABILITY']}" />
			<e:selectBooleanCheckbox id="available"
				value="#{departmentsController.departmentManagerToUpdate.available}"
				disabled="#{not departmentsController.currentUserCanSetAvailability}" />
			<e:outputLabel for="available"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.AVAILABLE']}" />
		</h:panelGrid>
		<h:panelGrid columns="2">
			<e:outputLabel for="rate"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.RATE']}" />
			<h:panelGroup>
				<e:inputText id="rate"
					value="#{departmentsController.departmentManagerToUpdate.rate}"
					disabled="#{not departmentsController.currentUserCanSetAvailability}" 
					required="true" >
				</e:inputText>
				<e:message for="rate" />
				<e:italic value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.RATE_HELP']}" />
			</h:panelGroup>
		</h:panelGrid>
		<h:panelGrid 
			columns="2" columnClasses="colLeft,colRight" width="100%"
			rendered="#{departmentsController.currentUserCanManageDepartmentManagers or departmentsController.currentUserCanSetAvailability}" >
			<h:panelGroup >
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentManagerEditForm:updateManagerButton');" >
					<e:bold value="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.UPDATE']} " />
					<t:graphicImage value="/media/images/save.png"
						alt="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.UPDATE']}" 
						title="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.UPDATE']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="updateManagerButton" 
					action="#{departmentsController.updateDepartmentManager}"
					value="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.UPDATE']}" />
			</h:panelGroup>
			<h:panelGroup>
				<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentManagerEditForm:deleteManagerButton');" >
						<e:bold value="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.DELETE']} " />
						<t:graphicImage value="/media/images/delete.png"
							alt="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.DELETE']}" 
							title="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.DELETE']}" />
					</h:panelGroup>
					<e:commandButton style="display: none" id="deleteManagerButton" 
						action="#{departmentsController.deleteDepartmentManager}"
						value="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.DELETE']}" immediate="true" />
				</h:panelGroup>
			</h:panelGroup>
		</h:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
