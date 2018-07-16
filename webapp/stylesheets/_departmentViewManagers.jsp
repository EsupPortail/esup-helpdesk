<%@include file="_include.jsp"%>

<h:panelGroup
	rendered="#{not empty departmentsController.departmentManagers}">
	<e:dataTable id="data" rowIndexVar="variable" style="width: 100%"
		value="#{departmentsController.departmentManagers}"
		var="departmentManager" border="0" cellspacing="0" cellpadding="0"
		columnClasses="colLeft,colCenter,colCenter,colCenter,colCenter">
		<f:facet name="header">

		</f:facet>
		<t:column style="cursor: pointer;" onclick="selectManager(#{variable});">
				<f:facet name="header"/>
			<e:text value="#{userFormatter[departmentManager.user]}" />
		</t:column>
		<t:column style="cursor: pointer"
			onclick="selectManager(#{variable});">
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGER.MANAGERS']} " />
				</f:facet>	
				<h:panelGroup rendered="#{departmentManager.manageManagers}">
					<t:htmlTag value="i" styleClass="fas fa-check-square fa-2x"/>
				</h:panelGroup>						
		</t:column>
		<t:column style="cursor: pointer"
			onclick="selectManager(#{variable});">
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGER.SERVICE']}" />
				</f:facet>
				<h:panelGroup rendered="#{departmentManager.manageProperties}">
					<t:htmlTag value="i" styleClass="fas fa-check-square fa-2x"/>
				</h:panelGroup>
		</t:column>
		<t:column style="cursor: pointer"
			onclick="selectManager(#{variable});">
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGER.CATEGORIES']}" />
				</f:facet>			
				<h:panelGroup rendered="#{departmentManager.manageCategories}">
					 <t:htmlTag value="i" styleClass="fas fa-check-square fa-2x"/>
				</h:panelGroup>
		</t:column>
		<t:column style="cursor: default">
             <h:panelGroup onclick="buttonClick('departmentViewForm:data:#{variable}:deleteManager');" rendered="#{departmentsController.currentUserCanManageDepartmentManagers}">
                  <t:htmlTag value="i" styleClass="fas fa-trash-alt fa-2x"/>
             </h:panelGroup>
		     <e:commandButton value="x" id="deleteManager" style="display: none"
		            rendered="#{departmentsController.currentUserCanManageDepartmentManagers}"
					action="#{departmentsController.deleteDepartmentManager}">
					<t:updateActionListener value="#{departmentManager}"
						property="#{departmentsController.departmentManagerToUpdate}" />
				</e:commandButton>
		</t:column>
		<t:column style="cursor: pointer">
			<e:commandButton style="display: none"
				value="#{msgs['_.BUTTON.VIEW_EDIT']}" id="selectManager"
				action="editDepartmentManager">
				<t:updateActionListener value="#{departmentManager}"
					property="#{departmentsController.departmentManagerToUpdate}" />
			</e:commandButton>
		</t:column>
		<f:facet name="footer"/>
	</e:dataTable>
</h:panelGroup>

 <t:htmlTag value="div" styleClass="form-block form-submit" rendered="#{departmentsController.currentUserCanManageDepartmentManagers}">
     <t:htmlTag value="div" styleClass="form-item" >
   		<e:commandButton  id="addManagerButton"
   				action="addManager" styleClass="button--secondary"
   				value="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']}" />
     </t:htmlTag>
 </t:htmlTag>

<t:htmlTag styleClass="region" value="div">
    <e:paragraph value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.NONE']}" rendered="#{empty departmentsController.departmentManagers}" />
</t:htmlTag>