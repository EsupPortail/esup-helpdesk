<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanViewDepartment}">

		   <t:htmlTag id="departmentManagerEdit" value="div" styleClass="page-wrapper departmentManagerEdit">
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
                                id="departmentManagerEditForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['DEPARTMENT_MANAGER_EDIT.TITLE']}"/>
                                                 </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-block form-category">
                                                    <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                          <t:htmlTag value="label">
                                                               <h:outputText value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGER']}" />
                                                          </t:htmlTag>
                                                          <t:htmlTag value="span" styleClass="category-lib">
                                                            <h:outputText value="#{userFormatter[departmentsController.departmentManagerToUpdate.user]}" escape="false" />
                                                          </t:htmlTag>
                                                    </t:htmlTag>
                                                    <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                          <t:htmlTag value="label">
                                                               <h:outputText value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.DEPARTMENT']} " />
                                                          </t:htmlTag>
                                                          <t:htmlTag value="span" styleClass="category-lib">
                                                            <h:outputText value="#{departmentsController.department.label}" escape="false" />
                                                          </t:htmlTag>
                                                    </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-block">
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="manageProperties"
                                                value="#{departmentsController.departmentManagerToUpdate.manageProperties}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="manageProperties"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_PROPERTIES']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="managerManagers"
                                                value="#{departmentsController.departmentManagerToUpdate.manageManagers}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="managerManagers"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_MANAGERS']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="manageCategories"
                                                value="#{departmentsController.departmentManagerToUpdate.manageCategories}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="manageCategories"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_CATEGORIES']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="manageFaq"
                                                value="#{departmentsController.departmentManagerToUpdate.manageFaq}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="manageFaq"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_FAQ']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="refuseTicket"
                                                value="#{departmentsController.departmentManagerToUpdate.refuseTicket}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="refuseTicket"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.REFUSE_TICKET']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="takeAlreadyAssignedTicket"
                                                value="#{departmentsController.departmentManagerToUpdate.takeAlreadyAssignedTicket}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="takeAlreadyAssignedTicket"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.TAKE_ALREADY_ASSIGNED_TICKET']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="takeFreeTicket"
                                                value="#{departmentsController.departmentManagerToUpdate.takeFreeTicket}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="takeFreeTicket"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.TAKE_FREE_TICKET']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="assignTicket"
                                                value="#{departmentsController.departmentManagerToUpdate.assignTicket}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="assignTicket"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.ASSIGN_TICKET']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="modifyTicketDepartment"
                                                value="#{departmentsController.departmentManagerToUpdate.modifyTicketDepartment}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="modifyTicketDepartment"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MODIFY_TICKET_DEPARTMENT']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="reopenAllTickets"
                                                value="#{departmentsController.departmentManagerToUpdate.reopenAllTickets}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="reopenAllTickets"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.REOPEN_ALL_TICKETS']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="setOwnAvailability"
                                                value="#{departmentsController.departmentManagerToUpdate.setOwnAvailability}"
                                                disabled="#{not departmentsController.currentUserCanManageDepartmentManagers}" />
                                            <e:outputLabel for="setOwnAvailability"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.SET_OWN_AVAILABILITY']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox id="available"
                                                value="#{departmentsController.departmentManagerToUpdate.available}"
                                                disabled="#{not departmentsController.currentUserCanSetAvailability}" />
                                            <e:outputLabel for="available"
                                                value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.AVAILABLE']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:outputLabel for="rate" value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.RATE']}" />
                                            <e:inputText id="rate" value="#{departmentsController.departmentManagerToUpdate.rate}"
                                                     disabled="#{not departmentsController.currentUserCanSetAvailability}"
                                                     required="true" >
                                            </e:inputText>
                                            <e:message for="rate" />
                                            <e:italic value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.RATE_HELP']}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item display-flex" rendered="#{departmentsController.currentUserCanManageDepartmentManagers or departmentsController.currentUserCanSetAvailability}">
                                                    <e:commandButton styleClass="button--primary" id="updateManagerButton"
                                                           action="#{departmentsController.updateDepartmentManager}"
                                                           value="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.UPDATE']}" />
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
