<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentManagers}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="departmentManagerDeleteForm" >
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['DEPARTMENT_MANAGER_DELETE.TITLE']}">
				<f:param value="#{userFormatter[departmentsController.departmentManagerToUpdate]}" />
				<f:param value="#{departmentsController.department.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentManagerDeleteForm:cancelButton');" >
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

		<e:paragraph value="#{msgs['DEPARTMENT_MANAGER_DELETE.TEXT.TOP']}">
			<f:param value="#{userFormatter[departmentsController.departmentManagerToUpdate]}" />
			<f:param value="#{departmentsController.department.label}" />
		</e:paragraph>

		<e:panelGrid columns="1">
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentManagerDeleteForm:freeButton');" >
					<e:bold value="#{msgs['DEPARTMENT_MANAGER_DELETE.TEXT.FREE']}" />
				</h:panelGroup>
				<e:italic value=" #{msgs['DEPARTMENT_MANAGER_DELETE.TEXT.FREE_HELP']}" />
				<e:commandButton id="freeButton" style="display: none" value="#{msgs['DEPARTMENT_MANAGER_DELETE.BUTTON.FREE']}"
					action="#{departmentsController.confirmDeleteDepartmentManager}" >
					<t:updateActionListener value="#{false}" property="#{departmentsController.useAssignmentAlgorithm}" />
					<t:updateActionListener value="#{null}" property="#{departmentsController.targetUser}" />
				</e:commandButton>
			</h:panelGroup>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentManagerDeleteForm:algorithmButton');" >
					<e:bold value="#{msgs['DEPARTMENT_MANAGER_DELETE.TEXT.ALGORITHM']}" />
				</h:panelGroup>
				<e:italic value=" #{msgs['DEPARTMENT_MANAGER_DELETE.TEXT.ALGORITHM_HELP']}" />
				<e:commandButton id="algorithmButton" style="display: none" value="#{msgs['DEPARTMENT_MANAGER_DELETE.BUTTON.ALGORITHM']}"
					action="#{departmentsController.confirmDeleteDepartmentManager}" >
					<t:updateActionListener value="#{true}" property="#{departmentsController.useAssignmentAlgorithm}" />
					<t:updateActionListener value="#{null}" property="#{departmentsController.targetUser}" />
				</e:commandButton>
			</h:panelGroup>
		</e:panelGrid>
		<t:dataTable
			id="data"
			rowIndexVar="index"
			value="#{departmentsController.managers}"
			var="manager">
			<t:column>
				<h:panelGroup rendered="#{manager.user.id != departmentsController.departmentManagerToUpdate.user.id}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentManagerDeleteForm:data:#{index}:assignButton');" >
						<e:bold value="#{msgs['DEPARTMENT_MANAGER_DELETE.TEXT.ASSIGN']}" >
							<f:param value="#{userFormatter[manager.user]}" />
						</e:bold>
					</h:panelGroup>
					<e:commandButton id="assignButton" style="display: none" value="#{msgs['DEPARTMENT_MANAGER_DELETE.BUTTON.ASSIGN']}"
						action="#{departmentsController.confirmDeleteDepartmentManager}" >
						<t:updateActionListener value="#{false}" property="#{departmentsController.useAssignmentAlgorithm}" />
						<t:updateActionListener value="#{manager.user}" property="#{departmentsController.targetUser}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
		</t:dataTable>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
