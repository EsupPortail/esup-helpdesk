<%@include file="_include.jsp"%>

<h:panelGroup rendered="#{not empty departmentsController.departmentInvitationPaginator.visibleItems}">
	<e:dataTable id="invitationData" rowIndexVar="variable" style="width: 100%"
		value="#{departmentsController.departmentInvitationPaginator.visibleItems}"
		var="departmentInvitation" border="0" cellspacing="0" cellpadding="0"
		columnClasses="colLeft,colCenter,colCenter,colCenter,colCenter">

		<t:column>
			<e:text value="#{userFormatter[departmentInvitation.user]}" />
		</t:column>

		<t:column style="cursor: default">
             <h:panelGroup onclick="simulateLinkClick('departmentViewForm:invitationData:#{variable}:deleteInvitation');" rendered="#{departmentsController.currentUserCanManageDepartmentManagers}">
                  <t:htmlTag value="i" styleClass="fas fa-trash-alt fa-2x"/>
             </h:panelGroup>
				<e:commandButton value="x" id="deleteInvitation" style="display: none"
					action="#{departmentsController.deleteDepartmentInvitation}" >
					<t:updateActionListener value="#{departmentInvitation}"
						property="#{departmentsController.departmentInvitationToDelete}" />
				</e:commandButton>
		</t:column>

	</e:dataTable>
</h:panelGroup>

 <t:htmlTag value="div" styleClass="form-block form-submit" rendered="#{departmentsController.currentUserCanManageDepartmentManagers}">
     <t:htmlTag value="div" styleClass="form-item" >
   		<e:commandButton  id="addInvitationButton"
   				action="addInvitation" styleClass="button--secondary"
   				value="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_INVITATION']}" />
     </t:htmlTag>
 </t:htmlTag>

<t:htmlTag styleClass="region" value="div">
<e:paragraph value="#{msgs['DEPARTMENT_VIEW.TEXT.INVITATIONS.NONE']}" rendered="#{empty departmentsController.departmentInvitationPaginator.visibleItems}" />
</t:htmlTag>
