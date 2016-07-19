<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="setRealCategoryForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['CATEGORY_SET_REAL.TITLE']}">
				<f:param value="#{departmentsController.categoryToUpdate.department.label}" />
				<f:param value="#{departmentsController.categoryToUpdate.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('setRealCategoryForm:cancelButton');" >
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
			rendered="#{departmentsController.setRealCategoryTree != null}">
			<t:tree2 id="tree" value="#{departmentsController.setRealCategoryTree}"
				var="node" varNodeToggler="t" clientSideToggle="true"
				showRootNode="true">
				<f:facet name="root">
					<h:panelGroup >
						<t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
						<t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
						<e:italic value=" #{msgs['CATEGORY_SET_REAL.TEXT.ROOT_LABEL']}" />
					</h:panelGroup>
				</f:facet>
				<f:facet name="department">
					<h:panelGroup>
						<t:graphicImage value="#{departmentIconUrlProvider[node.department]}" />
						<e:text value=" #{msgs['CATEGORY_SET_REAL.TEXT.DEPARTMENT_LABEL']}">
							<f:param value="#{node.department.label}" />
						</e:text>
					</h:panelGroup>
				</f:facet>
				<f:facet name="category">
					<h:panelGroup>
						<t:graphicImage value="#{categoryIconUrlProvider[node.category]}" />
						<e:text value=" #{msgs['CATEGORY_SET_REAL.TEXT.CATEGORY_LABEL']}"
							rendered="#{departmentsController.categoryToUpdate.realCategory.id == node.category.id}">
							<f:param value="#{node.category.label}" />
						</e:text>
						<h:panelGroup
							rendered="#{departmentsController.categoryToUpdate.realCategory.id != node.category.id}" >
							<h:panelGroup
								style="cursor: pointer" 
								onclick="simulateLinkClick('setRealCategoryForm:tree:#{node.identifier}:selectButton');" >
								<e:bold value=" #{msgs['CATEGORY_SET_REAL.TEXT.CATEGORY_LABEL']}" >
									<f:param value="#{node.category.label}" />
								</e:bold>
							</h:panelGroup>
							<e:commandButton value="->" id="selectButton" style="display: none"
								action="#{departmentsController.setRealCategory}" >
								<t:updateActionListener value="#{node.category}"
									property="#{departmentsController.targetCategory}" />
							</e:commandButton>
							<h:panelGroup rendered="#{node.category.virtual}">
								<t:graphicImage value="/media/images/redirection.png" />
								<e:italic value=" #{msgs['CATEGORY_SET_REAL.TEXT.REDIRECTION']}" >
									<f:param value="#{node.category.realCategory.department.label}" />
									<f:param value="#{node.category.realCategory.label}" />
								</e:italic>
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGroup>
				</f:facet>
			</t:tree2>
		</h:panelGroup>
		<h:panelGroup 
			rendered="#{departmentsController.categoryToUpdate.virtual}" >
			<h:panelGroup
				style="cursor: pointer" 
				onclick="simulateLinkClick('setRealCategoryForm:setRealButton');" >
				<e:bold value="#{msgs['CATEGORY_SET_REAL.BUTTON.SET_REAL']} " />
				<t:graphicImage value="/media/images/redirection-none.png" />
			</h:panelGroup>
			<e:commandButton value="->" id="setRealButton" 
				action="#{departmentsController.setRealCategory}" >
				<t:updateActionListener value="#{null}"
					property="#{departmentsController.targetCategory}" />
			</e:commandButton>
		</h:panelGroup>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		hideButton("setRealCategoryForm:cancelButton");
		hideButton("setRealCategoryForm:setRealButton");
</script>
</e:page>
