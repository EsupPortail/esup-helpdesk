<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanEditDepartmentProperties}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="setRealDepartmentForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['DEPARTMENT_SET_REAL.TITLE']}">
				<f:param value="#{departmentsController.departmentToUpdate.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('setRealDepartmentForm:cancelButton');" >
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
		<h:panelGroup
			rendered="#{departmentsController.setRealDepartmentTree != null}">
			<t:tree2 id="tree" value="#{departmentsController.setRealDepartmentTree}"
				var="node" varNodeToggler="t" clientSideToggle="true"
				showRootNode="true">
				<f:facet name="root">
					<h:panelGroup >
						<t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
						<t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
						<e:italic value=" #{msgs['DEPARTMENT_SET_REAL.TEXT.ROOT_LABEL']}" />
					</h:panelGroup>
				</f:facet>
				<f:facet name="department">
					<h:panelGroup>
						<t:graphicImage value="#{departmentIconUrlProvider[node.department]}" />
						<e:text value=" #{msgs['DEPARTMENT_SET_REAL.TEXT.DEPARTMENT_LABEL']}"
							rendered="#{departmentsController.departmentToUpdate.realDepartment.id == node.department.id}" >
							<f:param value="#{node.department.label}" />
						</e:text>
						<h:panelGroup
							rendered="#{departmentsController.departmentToUpdate.realDepartment.id != node.department.id}" >
							<h:panelGroup
								style="cursor: pointer" 
								onclick="simulateLinkClick('setRealDepartmentForm:tree:#{node.identifier}:selectButton');" >
								<e:bold value=" #{msgs['DEPARTMENT_SET_REAL.TEXT.DEPARTMENT_LABEL']}" >
									<f:param value="#{node.department.label}" />
								</e:bold>
							</h:panelGroup>
							<e:commandButton value="->" id="selectButton" style="display: none"
								action="#{departmentsController.setRealDepartment}" >
								<t:updateActionListener value="#{node.department}"
									property="#{departmentsController.targetDepartment}" />
							</e:commandButton>
						</h:panelGroup>
					</h:panelGroup>
				</f:facet>
			</t:tree2>
		</h:panelGroup>
		<h:panelGroup 
			rendered="#{departmentsController.departmentToUpdate.virtual}" >
			<h:panelGroup
				style="cursor: pointer" 
				onclick="simulateLinkClick('setRealDepartmentForm:setRealButton');" >
				<e:bold value="#{msgs['DEPARTMENT_SET_REAL.BUTTON.SET_REAL']} " />
				<t:graphicImage value="/media/images/redirection-none.png" />
			</h:panelGroup>
			<e:commandButton value="->" id="setRealButton" 
				action="#{departmentsController.setRealDepartment}" >
				<t:updateActionListener value="#{null}"
					property="#{departmentsController.targetDepartment}" />
			</e:commandButton>
		</h:panelGroup>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		hideButton("setRealDepartmentForm:cancelButton");
		hideButton("setRealDepartmentForm:setRealButton");
</script>
</e:page>
