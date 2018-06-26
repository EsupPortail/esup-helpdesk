<%@include file="_include.jsp"%>

<e:panelGrid id="viewManagers" columns="2"
	columnClasses="colLeft,colRight" width="100%">
	<e:subSection value="#{msgs['DEPARTMENT_VIEW.HEADER.MANAGERS']}" />
	<h:panelGroup>
		<h:panelGroup
			rendered="#{departmentsController.currentUserCanManageDepartmentManagers}">
			<h:panelGroup style="cursor: pointer"
				onclick="simulateLinkClick('departmentViewForm:addManagerButton');">
				<e:bold value="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']} " />
				<t:graphicImage value="/media/images/add.png"
					alt="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']}"
					title="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="addManagerButton"
				action="addManager"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']}" />
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<h:panelGroup
	rendered="#{not empty departmentsController.departmentManagers}">
	<e:dataTable id="data" rowIndexVar="variable" style="width: 100%"
		value="#{departmentsController.departmentManagers}"
		var="departmentManager" border="0" cellspacing="0" cellpadding="0"
		columnClasses="colLeft,colCenter,colCenter,colCenter,colCenter">
		<f:facet name="header">
			<h:panelGroup>
				<h:panelGrid width="100%" columns="1" columnClasses="colRightMax"> 
					<h:panelGroup style="cursor: pointer"
						onclick="javascript:{showHideElementDisplay('departmentViewForm:data:tbody_element', 'table-row-group');showHideElement('departmentViewForm:data:showFiles');showHideElement('departmentViewForm:data:hideFiles');return false;}">
						<h:panelGroup id="showFiles">
							<t:graphicImage value="/media/images/short-menu.png"
								alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
						</h:panelGroup>
						<h:panelGroup id="hideFiles" style="display: none">
							<t:graphicImage value="/media/images/long-menu.png"
								alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
						</h:panelGroup>
					</h:panelGroup>
					</h:panelGrid>
				<t:htmlTag value="hr" />
			</h:panelGroup>
		</f:facet>
		<t:column style="cursor: pointer;"
			onclick="selectManager(#{variable});">
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS']}" />
				</f:facet>
			<e:text value="#{userFormatter[departmentManager.user]}" />
		</t:column>
		<t:column style="cursor: pointer"
			onclick="selectManager(#{variable});">
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGER.MANAGERS']} " />
				</f:facet>	
				<h:panelGroup rendered="#{departmentManager.manageManagers}">
					<t:graphicImage value="/media/images/condition-result-true.png" style="cursor: pointer" />
				</h:panelGroup>						
		</t:column>
		<t:column style="cursor: pointer"
			onclick="selectManager(#{variable});">
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGER.SERVICE']}" />
				</f:facet>
				<h:panelGroup rendered="#{departmentManager.manageProperties}">
					<t:graphicImage value="/media/images/condition-result-true.png" style="cursor: pointer" />
				</h:panelGroup>
		</t:column>
		<t:column style="cursor: pointer"
			onclick="selectManager(#{variable});">
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGER.CATEGORIES']}" />
				</f:facet>			
				<h:panelGroup rendered="#{departmentManager.manageCategories}">
					<t:graphicImage value="/media/images/condition-result-true.png" style="cursor: pointer" />
				</h:panelGroup>
		</t:column>
		<t:column style="cursor: default">
			<h:panelGroup
				rendered="#{departmentsController.currentUserCanManageDepartmentManagers}">
				<t:graphicImage value="/media/images/delete.png" alt="x" title="x"
					style="cursor: pointer"
					onclick="simulateLinkClick('departmentViewForm:data:#{variable}:deleteManager');" />
				<e:commandButton value="x" id="deleteManager" style="display: none"
					action="#{departmentsController.deleteDepartmentManager}">
					<t:updateActionListener value="#{departmentManager}"
						property="#{departmentsController.departmentManagerToUpdate}" />
				</e:commandButton>
			</h:panelGroup>
		</t:column>
		<t:column style="cursor: pointer">
			<e:commandButton style="display: none"
				value="#{msgs['_.BUTTON.VIEW_EDIT']}" id="selectManager"
				action="editDepartmentManager">
				<t:updateActionListener value="#{departmentManager}"
					property="#{departmentsController.departmentManagerToUpdate}" />
			</e:commandButton>
		</t:column>
		<f:facet name="footer">
			<t:htmlTag value="hr" />
		</f:facet>
	</e:dataTable>
	<e:paragraph value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.NOTE']}" />
</h:panelGroup>
<e:paragraph value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.NONE']}"
	rendered="#{empty departmentsController.departmentManagers}" />
