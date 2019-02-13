<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartments}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="departmentAddForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['DEPARTMENT_ADD.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentAddForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton id="cancelButton" action="cancel"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:panelGrid id="departmentAddPanel" columns="2">
			<e:outputLabel for="label"
				value="#{msgs['DEPARTMENT_ADD.TEXT.LABEL']}" />
			<h:panelGroup>
				<e:inputText id="label"
					value="#{departmentsController.departmentToAdd.label}" 
					onkeypress="if (event.keyCode == 13) { focusElement('departmentAddForm:xlabel'); return false;}" />
				<e:message for="label" />
			</h:panelGroup>
			<e:outputLabel for="xlabel"
				value="#{msgs['DEPARTMENT_ADD.TEXT.XLABEL']}" />
			<h:panelGroup>
				<e:inputText id="xlabel"
					value="#{departmentsController.departmentToAdd.xlabel}" 
					onkeypress="if (event.keyCode == 13) { simulateLinkClick('departmentAddForm:addButton'); return false;}" />
				<e:message for="xlabel" />
			</h:panelGroup>
			<h:panelGroup />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentAddForm:addButton');" >
					<e:bold value=" #{msgs['DEPARTMENT_ADD.BUTTON.ADD_DEPARTMENT']} " />
					<t:graphicImage value="/media/images/add.png"
						alt="#{msgs['DEPARTMENT_ADD.BUTTON.ADD_DEPARTMENT']}" 
						title="#{msgs['DEPARTMENT_ADD.BUTTON.ADD_DEPARTMENT']}" />
				</h:panelGroup>
				<e:commandButton id="addButton" action="#{departmentsController.addDepartment}"
					value="#{msgs['DEPARTMENT_ADD.BUTTON.ADD_DEPARTMENT']}" />
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		focusElement("departmentAddForm:label");
		hideButton("departmentAddForm:cancelButton");
		hideButton("departmentAddForm:addButton");
</script>
</e:page>
