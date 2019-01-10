<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">
		   <t:htmlTag id="categoryDelete" value="div" styleClass="page-wrapper categoryDelete">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>
                    <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                        <t:htmlTag value="div" styleClass="content-inner">
                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="categoryDeleteForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="dashboard-header">
                                          <t:htmlTag value="div" styleClass="controlPanel-title">
                                              <t:htmlTag value="h1">
                                                  <t:htmlTag value="span">
                                                      <h:outputText value="#{msgs['CATEGORY_DELETE.TITLE']}"/>
                                                  </t:htmlTag>
                                                  <t:htmlTag value="span" styleClass="subtitle title">
                                                     <h:outputText value=" #{departmentsController.categoryToUpdate.department.label}" escape="false" />
                                                     <h:outputText value=" - " escape="false" />
                                                     <h:outputText value=" #{departmentsController.categoryToUpdate.label}" escape="false" />
                                                  </t:htmlTag>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                       </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="region form-body">
                                            <t:htmlTag value="div" styleClass="form-block">
                                                <e:text value="#{msgs['CATEGORY_DELETE.TEXT.TOP']}" />
                                            </t:htmlTag>

                                            <t:htmlTag value="div" styleClass="form-item display-flex">
                                                <e:commandButton id="deleteButton"
                                                    styleClass="button--primary"
                                                    value="#{msgs['CATEGORY_DELETE.BUTTON.ALSO_DELETE_TICKETS']}"
                                                    action="#{departmentsController.moveTicketsAndDeleteCategory}">
                                                    <t:updateActionListener value="#{null}"
                                                        property="#{departmentsController.targetCategory}" />
                                                </e:commandButton>
                                                <h:panelGroup  id="moveCategoryTickets"
                                                    rendered="#{departmentsController.moveCategoryTree != null}"
                                                    onclick="showHideElement('categoryDeleteForm:moveCategoryTree');"
                                                    styleClass="button--secondary">
                                                    <h:outputText value="#{msgs['CATEGORY_DELETE.MOVE_TICKETS']}"/>
                                                  </h:panelGroup>
                                                 <e:commandButton id="cancelButton" action="cancel" value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                            </t:htmlTag>
                                            <t:htmlTag value="div"  rendered="#{departmentsController.moveCategoryTree != null}" id="moveCategoryTree" styleClass="form-block" style="display :none">
                                                <t:tree2 id="tree" value="#{departmentsController.moveCategoryTree}"
                                                    var="node" varNodeToggler="t" clientSideToggle="true"
                                                    showRootNode="true" >
                                                    <f:facet name="root">
                                                        <h:panelGroup >
                                                            <e:italic value=" #{msgs['CATEGORY_DELETE.TEXT.ROOT_LABEL']}" />
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <f:facet name="department">
                                                        <h:panelGroup>
                                                            <e:text value=" #{msgs['CATEGORY_DELETE.TEXT.DEPARTMENT_LABEL']}">
                                                                <f:param value="#{node.department.label}" />
                                                            </e:text>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <f:facet name="category">
                                                        <h:panelGroup>
                                                            <h:panelGroup
                                                                style="cursor: pointer"
                                                                onclick="simulateLinkClick('categoryDeleteForm:tree:#{node.identifier}:selectButton');" >
                                                                <e:bold
                                                                    value=" #{msgs['CATEGORY_DELETE.TEXT.CATEGORY_LABEL']}" >
                                                                    <f:param value="#{node.category.label}" />
                                                                </e:bold>
                                                            </h:panelGroup>
                                                            <e:commandButton value="->" id="selectButton" style="display: none"
                                                                action="#{departmentsController.moveTicketsAndDeleteCategory}">
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
                            </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
           <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
           </t:htmlTag>
        </t:htmlTag>
</e:page>
