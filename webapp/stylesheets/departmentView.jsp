<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}" >

<script type="text/javascript">
	function selectManager(index) {
		simulateLinkClick('departmentViewForm:data:'+index+':selectManager');
	}
</script>

	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{not departmentsController.currentUserCanViewDepartment}" >
		<h:panelGroup rendered="#{departmentSelectionController.currentUser == null}" >
			<%@include file="_auth.jsp"%>
		</h:panelGroup>
		<h:panelGroup rendered="#{departmentSelectionController.currentUser != null}" >
			<e:messages/>
		</h:panelGroup>
	</h:panelGroup>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="departmentViewForm" rendered="#{departmentsController.currentUserCanViewDepartment}" >
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%"
			cellspacing="0" cellpadding="0" id="topPanelGrid">
			<e:section value="#{msgs['DEPARTMENT_VIEW.TITLE']}">
				<f:param value="#{departmentsController.department.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:backButton');" >
					<e:bold value="#{msgs['DEPARTMENT_VIEW.BUTTON.BACK']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['DEPARTMENT_VIEW.BUTTON.BACK']}" 
						title="#{msgs['DEPARTMENT_VIEW.BUTTON.BACK']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="backButton" action="back"
					value="#{msgs['DEPARTMENT_VIEW.BUTTON.BACK']}" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:panelGrid columns="2" columnClasses="colLeftNowrap,colLeftNowrap" width="100%" >
			<h:panelGroup>
				<%@include file="_departmentViewManagers.jsp"%>
				<t:htmlTag value="hr" />
				<%@include file="_departmentViewProperties.jsp"%>
			</h:panelGroup>
			<h:panelGroup>
				<%@include file="_departmentViewInvitations.jsp"%>
				<t:htmlTag value="hr" />
				<%@include file="_departmentViewCategories.jsp"%>
				<t:htmlTag value="hr" />
				<%@include file="_departmentViewFaqLinks.jsp"%>
			</h:panelGroup>
			<h:panelGroup>
				<h:panelGroup
					rendered="#{departmentsController.currentUserCanDeleteDepartment}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:deleteButton');" >
						<e:bold value="#{msgs['DEPARTMENT_VIEW.BUTTON.DELETE_DEPARTMENT']} " />
						<t:graphicImage value="/media/images/delete.png"
							alt="#{msgs['DEPARTMENT_VIEW.BUTTON.DELETE_DEPARTMENT']}" 
							title="#{msgs['DEPARTMENT_VIEW.BUTTON.DELETE_DEPARTMENT']}" />
					</h:panelGroup>
					<e:commandButton style="display: none" id="deleteButton" action="#{departmentsController.deleteDepartment}"
						value="#{msgs['DEPARTMENT_VIEW.BUTTON.DELETE_DEPARTMENT']}" immediate="true" />
				</h:panelGroup>
				<e:italic
					rendered="#{departmentsController.currentUserCanManageDepartments and not departmentsController.currentUserCanDeleteDepartment}" 
					value="#{msgs['DEPARTMENT_VIEW.TEXT.CAN_NOT_DELETE_DEPARTMENT']} " />

				<t:dataList value="#{departmentsController.virtualDepartments}"
					var="virtualDepartment" rowIndexVar="virtualDepartmentIndex" >
					<t:htmlTag value="br" />
					<t:htmlTag value="br" rendered="#{virtualDepartmentIndex == 0}" />
					<e:text value=" #{msgs['DEPARTMENTS.TEXT.REDIRECTION.LISTE']}" rendered="#{virtualDepartmentIndex == 0}">
						<f:param value="#{departmentsController.department.label}" />
					</e:text>
					<t:htmlTag value="br" rendered="#{virtualDepartmentIndex == 0}" />
					<t:graphicImage value="/media/images/redirection-inverted.png" />
					<e:text
						value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.REDIRECTION_VALUE_INVERTED']}">
						<f:param value="#{virtualDepartment.label}" />
					</e:text>
				</t:dataList>
				<t:dataList value="#{departmentsController.virtualCategories}"
					var="virtualCategorie" rowIndexVar="virtualCategorieIndex">
					<t:htmlTag value="br" />
					<t:htmlTag value="br" rendered="#{virtualCategorieIndex == 0}" />
					<e:text value=" #{msgs['CATEGORIES.TEXT.REDIRECTION.LISTE']}" rendered="#{virtualCategorieIndex == 0}">
						<f:param value="#{departmentsController.department.label}" />
					</e:text>
					<t:htmlTag value="br" rendered="#{virtualCategorieIndex == 0}" />					
 					<t:graphicImage value="/media/images/redirection-inverted.png" />					
					<e:text
						value=" #{msgs['CATEGORIES.TEXT.VIRTUAL_CATEGORY']}">
						<f:param value="#{virtualCategorie.department.label}" />
						<f:param value="#{virtualCategorie.label}" />
					</e:text>
				</t:dataList>


			</h:panelGroup>
			<h:panelGroup/>
		</e:panelGrid>

	</e:form>
	<t:aliasBean alias="#{controller}" value="#{departmentsController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		highlightTableRows("departmentViewForm:data");
		highlightTableRows("departmentViewForm:invitationData");
</script>
</e:page>
