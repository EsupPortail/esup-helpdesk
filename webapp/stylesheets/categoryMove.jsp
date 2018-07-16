<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">
		   <t:htmlTag id="categoryMove" value="div" styleClass="page-wrapper categoryMove">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>
                    <t:htmlTag value="main" styleClass="content">
                        <t:htmlTag value="div" styleClass="content-inner">
                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="categoryMoveForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="dashboard-header">
                                          <t:htmlTag value="div" styleClass="controlPanel-title">
                                              <t:htmlTag value="h1">
                                                  <t:htmlTag value="span">
                                                      <h:outputText value="#{msgs['CATEGORY_SET_REAL.TITLE']}"/>
                                                  </t:htmlTag>
                                                  <t:htmlTag value="span" styleClass="subtitle title">
                                                     <h:outputText value="#{departmentsController.categoryToUpdate.department.label}" escape="false" />
                                                     <h:outputText value=" - " escape="false" />
                                                     <h:outputText value="#{departmentsController.categoryToUpdate.label}" escape="false" />
                                                  </t:htmlTag>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                       </t:htmlTag>

                                       <t:htmlTag value="div" styleClass="region form-body">
                                           <t:htmlTag value="div" styleClass="form-block treeview" rendered="#{departmentsController.moveCategoryTree != null}">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <t:tree2 id="tree" value="#{departmentsController.moveCategoryTree}"
                                                        var="node" varNodeToggler="t" clientSideToggle="true"
                                                        showRootNode="true">
                                                        <f:facet name="root">
                                                            <h:panelGroup >
                                                                <e:italic value=" #{msgs['CATEGORY_MOVE.TEXT.ROOT_LABEL']}" />
                                                            </h:panelGroup>
                                                        </f:facet>
                                                        <f:facet name="department">
                                                            <h:panelGroup>
                                                                <h:panelGroup
                                                                    style="cursor: pointer" styleClass="link"
                                                                    onclick="simulateLinkClick('categoryMoveForm:tree:#{node.identifier}:moveButton');" >
                                                                    <e:text
                                                                        value=" #{msgs['CATEGORY_MOVE.TEXT.DEPARTMENT_LABEL']}" >
                                                                        <f:param value="#{node.department.label}" />
                                                                    </e:text>
                                                                </h:panelGroup>
                                                                <e:commandButton value="->" id="moveButton" style="display: none"
                                                                    action="#{departmentsController.moveCategory}">
                                                                    <t:updateActionListener value="#{node.department}"
                                                                        property="#{departmentsController.targetDepartment}" />
                                                                    <t:updateActionListener value="#{null}"
                                                                        property="#{departmentsController.targetCategory}" />
                                                                </e:commandButton>
                                                            </h:panelGroup>
                                                        </f:facet>
                                                        <f:facet name="category">
                                                            <h:panelGroup>
                                                                <h:panelGroup
                                                                    style="cursor: pointer"  styleClass="link"
                                                                    onclick="simulateLinkClick('categoryMoveForm:tree:#{node.identifier}:moveButton');" >
                                                                    <e:text
                                                                        value=" #{msgs['CATEGORY_MOVE.TEXT.CATEGORY_LABEL']}" >
                                                                        <f:param value="#{node.category.label}" />
                                                                    </e:text>
                                                                </h:panelGroup>
                                                                <e:commandButton value="->" id="moveButton" style="display: none"
                                                                    action="#{departmentsController.moveCategory}">
                                                                    <t:updateActionListener value="#{node.department}"
                                                                        property="#{departmentsController.targetDepartment}" />
                                                                    <t:updateActionListener value="#{node.category}"
                                                                        property="#{departmentsController.targetCategory}" />
                                                                </e:commandButton>
                                                            </h:panelGroup>
                                                        </f:facet>
                                                    </t:tree2>
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-item">
                                                 <e:commandButton id="cancelButton" action="cancel" value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                           </t:htmlTag>
                                       </t:htmlTag>

                            </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
           <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
           </t:htmlTag>
        </t:htmlTag>
</e:page>
