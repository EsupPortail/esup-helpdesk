<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="categoryAddForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['CATEGORY_ADD.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryAddForm:cancelButton');" >
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

		<e:panelGrid columns="2">
			<e:outputLabel for="label"
				value="#{msgs['CATEGORY_ADD.TEXT.LABEL']}" />
			<h:panelGroup>
				<e:inputText id="label"
					value="#{departmentsController.categoryToAdd.label}" 
					onkeypress="if (event.keyCode == 13) { focusElement('categoryAddForm:xlabel'); return false;}" />
				<e:message for="label" />
			</h:panelGroup>
			<e:outputLabel for="xlabel"
				value="#{msgs['CATEGORY_ADD.TEXT.XLABEL']}" />
			<h:panelGroup>
				<e:inputText id="xlabel"
					value="#{departmentsController.categoryToAdd.xlabel}" 
					onkeypress="if (event.keyCode == 13) { simulateLinkClick('categoryAddForm:addButton'); return false;}" />
				<e:message for="xlabel" />
			</h:panelGroup>
			<h:panelGroup rendered="#{departmentsController.categoryToAdd.parent != null}" />
			<h:panelGroup rendered="#{departmentsController.categoryToAdd.parent != null}" >
				<e:selectBooleanCheckbox value="#{departmentsController.categoryToAdd.parent.addNewTickets}" />
				<e:italic 
					value="#{msgs['CATEGORY_ADD.TEXT.PARENT_ADD_NEW_TICKETS_HELP']}" />
			</h:panelGroup>
			<h:panelGroup />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryAddForm:addButton');" >
					<e:bold value=" #{msgs['CATEGORY_ADD.BUTTON.ADD_CATEGORY']} " />
					<t:graphicImage value="/media/images/add.png"
						alt="#{msgs['CATEGORY_ADD.BUTTON.ADD_CATEGORY']}" 
						title="#{msgs['CATEGORY_ADD.BUTTON.ADD_CATEGORY']}" />
				</h:panelGroup>
				<e:commandButton id="addButton" action="#{departmentsController.addCategory}"
					value="#{msgs['CATEGORY_ADD.BUTTON.ADD_CATEGORY']}" />
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		focusElement("categoryAddForm:label");
		hideButton("categoryAddForm:cancelButton");
		hideButton("categoryAddForm:addButton");
</script>
</e:page>
