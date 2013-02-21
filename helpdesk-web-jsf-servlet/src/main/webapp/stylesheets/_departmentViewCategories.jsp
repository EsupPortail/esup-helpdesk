<%@include file="_include.jsp"%>

<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
	<e:subSection value="#{msgs['DEPARTMENT_VIEW.HEADER.CATEGORIES']}" />
	<h:panelGroup>
		<h:panelGroup rendered="#{departmentsController.currentUserCanViewCategories}" >
			<e:commandButton id="editCategoriesButton" action="editCategories"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_CATEGORIES']}" style="display: none" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:editCategoriesButton');" >
				<e:bold value="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_CATEGORIES']} " />
				<t:graphicImage value="/media/images/edit.png"
					alt="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_CATEGORIES']}" 
					title="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_CATEGORIES']}" />
			</h:panelGroup>
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<t:tree2 id="tree" value="#{departmentsController.categoryTree}"
	var="node" varNodeToggler="t" clientSideToggle="true"
	showRootNode="false" >
	<f:facet name="department">
		<h:panelGroup/>
	</f:facet>
	<f:facet name="category">
		<h:panelGroup>
			<t:graphicImage value="#{categoryIconUrlProvider[node.category]}" />
			<e:text value=" #{msgs['DEPARTMENT_VIEW.CATEGORIES.CATEGORY_LABEL']}" >
				<f:param value="#{node.category.label}" />
			</e:text>
			<h:panelGroup rendered="#{node.category.virtual}" >
				<e:italic value=" " />
				<t:graphicImage value="/media/images/redirection.png" />
				<e:italic value=" #{msgs['DEPARTMENT_VIEW.CATEGORIES.REDIRECTION']}" >
					<f:param value="#{node.category.realCategory.department.label}" />
					<f:param value="#{node.category.realCategory.label}" />
				</e:italic>
			</h:panelGroup>
			<h:panelGroup rendered="#{not empty node.virtualCategories}" >
				<e:italic value=" " />
				<t:graphicImage value="/media/images/redirection-inverted.png" />
				<e:italic value=" #{msgs['DEPARTMENT_VIEW.CATEGORIES.VIRTUAL_CATEGORIES_NUMBER']}" >
					<f:param value="#{node.virtualCategoriesNumber}" />
				</e:italic>
			</h:panelGroup>
		</h:panelGroup>
	</f:facet>
</t:tree2>
<e:paragraph 
	rendered="#{departmentsController.department.virtual}" 
	value="#{msgs['DEPARTMENT_VIEW.TEXT.VIRTUAL_HAS_NO_CATEGORY']}" />
