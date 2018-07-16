<%@include file="_include.jsp"%>
<t:htmlTag value="div" styleClass="form-block treeview readonly-style ">
    <t:htmlTag value="div" styleClass="form-item" >
        <t:tree2 id="tree" value="#{departmentsController.categoryTree}"
            var="node" varNodeToggler="t" clientSideToggle="true"
            showRootNode="false" >

            <f:facet name="category">
                <h:panelGroup styleClass="leaf">
                    <e:text value=" #{msgs['DEPARTMENT_VIEW.CATEGORIES.CATEGORY_LABEL']}" >
                        <f:param value="#{node.category.label}" />
                    </e:text>
                    <h:panelGroup rendered="#{node.category.virtual}" >
                        <h:panelGroup style="cursor: default" >
                             <t:htmlTag value="i" styleClass="redirect far  fa-arrow-alt-circle-right"/>
                        </h:panelGroup>

                        <e:italic value=" #{msgs['DEPARTMENT_VIEW.CATEGORIES.REDIRECTION']}" >
                            <f:param value="#{node.category.realCategory.department.label}" />
                            <f:param value="#{node.category.realCategory.label}" />
                        </e:italic>
                    </h:panelGroup>

                    <h:panelGroup styleClass="redirections leaf" rendered="#{not empty node.virtualCategories}" >
                        <t:dataList value="#{node.virtualCategories}" var="virtualCategory"
                            rowIndexVar="virtualCategoryIndex">
                            <h:panelGroup style="cursor: default" >
                                 <t:htmlTag value="i" styleClass="redirect far  fa-arrow-alt-circle-left"/>
                            </h:panelGroup>
                            <e:italic value=" #{msgs['CATEGORIES.TEXT.VIRTUAL_CATEGORY']}" >
                                <f:param value="#{virtualCategory.department.label}" />
                                <f:param value="#{virtualCategory.label}" />
                            </e:italic>
                        </t:dataList>
                    </h:panelGroup>
                </h:panelGroup>
            </f:facet>
        </t:tree2>
     </t:htmlTag>
  </t:htmlTag>
<e:paragraph 
	rendered="#{departmentsController.department.virtual}" 
	value="#{msgs['DEPARTMENT_VIEW.TEXT.VIRTUAL_HAS_NO_CATEGORY']}" />

 <t:htmlTag value="div" styleClass="form-block form-submit" rendered="#{departmentsController.currentUserCanViewCategories}">
     <t:htmlTag value="div" styleClass="form-item" >
			<e:commandButton id="editCategoriesButton" action="editCategories"
			    styleClass="button--secondary"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_CATEGORIES']}"/>
     </t:htmlTag>
 </t:htmlTag>
