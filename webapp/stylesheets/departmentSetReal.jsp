<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanEditDepartmentProperties}">
		   <t:htmlTag id="departmentSetReal" value="div" styleClass="page-wrapper departmentSetReal">
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
                                id="setRealDepartmentForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="dashboard-header">
                                          <t:htmlTag value="div" styleClass="controlPanel-title">
                                              <t:htmlTag value="h1">
                                                  <t:htmlTag value="span">
                                                      <h:outputText value="#{msgs['DEPARTMENT_SET_REAL.TITLE']}"/>
                                                  </t:htmlTag>
                                                  <t:htmlTag value="span" styleClass="subtitle title">
                                                     <h:outputText value="#{departmentsController.departmentToUpdate.label}" escape="false" />
                                                  </t:htmlTag>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                       </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="region form-body">
                                           <t:htmlTag value="div" styleClass="form-block treeview" rendered="#{departmentsController.setRealDepartmentTree != null}">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                <t:tree2 id="tree" value="#{departmentsController.setRealDepartmentTree}"
                                                    var="node" varNodeToggler="t" clientSideToggle="true" showRootNode="true">
                                                    <f:facet name="root">
                                                        <h:panelGroup >
                                                            <e:italic value=" #{msgs['DEPARTMENT_SET_REAL.TEXT.ROOT_LABEL']}" />
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <f:facet name="department">
                                                        <h:panelGroup>
                                                            <e:text value=" #{msgs['DEPARTMENT_SET_REAL.TEXT.DEPARTMENT_LABEL']}"
                                                                rendered="#{departmentsController.departmentToUpdate.realDepartment.id == node.department.id}" >
                                                                <f:param value="#{node.department.label}" />
                                                            </e:text>
                                                            <h:panelGroup
                                                                rendered="#{departmentsController.departmentToUpdate.realDepartment.id != node.department.id}" >
                                                                <h:panelGroup
                                                                    style="cursor: pointer" styleClass="link"
                                                                    onclick="simulateLinkClick('setRealDepartmentForm:tree:#{node.identifier}:selectButton');" >
                                                                    <e:text value=" #{msgs['DEPARTMENT_SET_REAL.TEXT.DEPARTMENT_LABEL']}" >
                                                                        <f:param value="#{node.department.label}" />
                                                                    </e:text>
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
                                                 </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-item diplay-flex">
                                                <e:commandButton styleClass="button--primary hideme" value="#{msgs['DEPARTMENT_SET_REAL.BUTTON.SET_REAL']}" id="setRealButton"
                                                    action="#{departmentsController.setRealDepartment}"  rendered="#{departmentsController.departmentToUpdate.virtual}" >
                                                    <t:updateActionListener value="#{null}"
                                                        property="#{departmentsController.targetDepartment}" />
                                                </e:commandButton>
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
