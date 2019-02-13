<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}" authorized="#{departmentsController.currentUserCanDeleteDepartment}" >
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="departmentDeleteForm" >
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['DEPARTMENT_DELETE.TITLE']}">
				<f:param value="#{departmentsController.department.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentDeleteForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="cancelButton" action="cancel"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>

		<h:panelGroup rendered="#{empty departmentsController.deleteTargetDepartments}">
			<e:paragraph value="#{msgs['DEPARTMENT_DELETE.TEXT.TOP']}" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentDeleteForm:confirmButton');" >
				<e:bold value="#{msgs['_.BUTTON.CONFIRM']} " />
				<t:graphicImage value="/media/images/delete.png"
					alt="#{msgs['_.BUTTON.CONFIRM']}" 
					title="#{msgs['_.BUTTON.CONFIRM']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="confirmButton" action="#{departmentsController.doDeleteDepartment}"
				value="#{msgs['_.BUTTON.CONFIRM']}" />
		</h:panelGroup>
		<h:panelGroup rendered="#{not empty departmentsController.deleteTargetDepartments}">
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentDeleteForm:deleteArchivedTicketsButton');" >
				<t:graphicImage value="/media/images/delete.png" />
				<e:bold value=" #{msgs['DEPARTMENT_DELETE.BUTTON.DELETE_ARCHIVED_TICKETS']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="deleteArchivedTicketsButton" 
				action="#{departmentsController.doDeleteDepartment}"
				value="#{msgs['DEPARTMENT_DELETE.BUTTON.DELETE_ARCHIVED_TICKETS']}" >
					<t:updateActionListener value="#{null}" property="#{departmentsController.targetDepartment}" />
			</e:commandButton>
			<t:dataList id="data" value="#{departmentsController.deleteTargetDepartments}" var="department" rowIndexVar="index" >
				<t:htmlTag value="br" />
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentDeleteForm:data:#{index}:moveArchivedTicketsButton');" >
					<t:graphicImage value="/media/images/move.png" />
					<t:graphicImage value="/media/images/delete.png" />
					<e:bold value=" #{msgs['DEPARTMENT_DELETE.BUTTON.MOVE_ARCHIVED_TICKETS']}" >
						<f:param value="#{department.label}" />
					</e:bold>
				</h:panelGroup>
				<e:commandButton style="display: none" id="moveArchivedTicketsButton" 
					action="#{departmentsController.doDeleteDepartment}"
					value="#{msgs['DEPARTMENT_DELETE.BUTTON.MOVE_ARCHIVED_TICKETS']}" >
					<t:updateActionListener value="#{department}" property="#{departmentsController.targetDepartment}" />
				</e:commandButton>
			</t:dataList>
		</h:panelGroup>

	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
