<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanEditDepartmentProperties}">

	<script type="text/javascript">
function selectIcon(src, value) {
	document.getElementById('departmentEditForm:selectedIconImage').src = src;
	document.getElementById('departmentEditForm:selectedIconMenu').value=value;
	hideElement('departmentEditForm:iconsPanel');
	showElement('departmentEditForm:selectedIconPanel');
}
</script>
		   <t:htmlTag id="departmenEdit" value="div" styleClass="page-wrapper departmenEdit">
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
                                id="departmentEditForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="dashboard-header">
                                          <t:htmlTag value="div" styleClass="controlPanel-title">
                                              <t:htmlTag value="h1">
                                                  <t:htmlTag value="span">
                                                      <h:outputText value="#{msgs['DEPARTMENT_EDIT.TITLE']}"/>
                                                  </t:htmlTag>
                                                  <t:htmlTag value="span" styleClass="subtitle title">
                                                     <h:outputText value=" #{departmentsController.department.label}" escape="false" />
                                                  </t:htmlTag>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                       </t:htmlTag>

                                       <t:htmlTag value="div" styleClass="region form-body">
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                    <e:outputLabel for="id" value="#{msgs['DEPARTMENT_EDIT.TEXT.ID']}" />
                                                    <e:text id="id" value="#{departmentsController.departmentToUpdate.id}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="enabled"  value="#{msgs['DEPARTMENT_EDIT.TEXT.ENABLED']}" />
                                                    <e:selectBooleanCheckbox id="enabled" value="#{departmentsController.departmentToUpdate.enabled}" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.ENABLED_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="label"  value="#{msgs['DEPARTMENT_EDIT.TEXT.LABEL']}" />
                                                    <e:inputText id="label" value="#{departmentsController.departmentToUpdate.label}" required="true" />
                                                    <e:message for="label" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.LABEL_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="xlabel" value="#{msgs['DEPARTMENT_EDIT.TEXT.XLABEL']}" />
                                                    <e:inputText id="xlabel" size="80"
                                                        value="#{departmentsController.departmentToUpdate.xlabel}"
                                                        required="true" />
                                                    <e:message for="xlabel" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.XLABEL_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="realDepartment" value="#{msgs['DEPARTMENT_EDIT.TEXT.REAL_DEPARTMENT']}" />
                                                    <t:htmlTag value="div" id="realDepartment" >
                                                        <h:panelGroup rendered="#{not departmentsController.departmentToUpdateHasVirtualDepartments and not departmentsController.departmentToUpdateHasCategories}" >
                                                            <h:panelGroup rendered="#{departmentsController.departmentToUpdate.virtual}" >
                                                                <t:htmlTag value="div">
                                                                    <h:panelGroup style="cursor: default">
                                                                       <t:htmlTag value="i" styleClass="redirect far fa-2x fa-arrow-alt-circle-right"/>
                                                                    </h:panelGroup>
                                                                    <e:text value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.REDIRECTION_VALUE']}">
                                                                        <f:param
                                                                            value="#{departmentsController.department.realDepartment.label}" />
                                                                    </e:text>
                                                                </t:htmlTag>
                                                                <e:commandButton id="setRealButton" action="#{departmentsController.setRealDepartment}"
                                                                    value="#{msgs['DEPARTMENT_EDIT.BUTTON.SET_REAL']}" immediate="true" >
                                                                    <t:updateActionListener value="#{null}" property="#{departmentsController.targetDepartment}" />
                                                                </e:commandButton>


                                                                <e:commandButton id="changeRealButton" action="#{departmentsController.gotoSetRealDepartment}"
                                                                    value="#{msgs['DEPARTMENT_EDIT.BUTTON.CHANGE_REAL']}" styleClass="button--secondary" immediate="true"/>
                                                            </h:panelGroup>
                                                            <h:panelGroup rendered="#{not departmentsController.departmentToUpdate.virtual}" >
                                                                <e:commandButton id="setVirtualButton" action="#{departmentsController.gotoSetRealDepartment}"
                                                                    value="#{msgs['DEPARTMENT_EDIT.BUTTON.SET_VIRTUAL']}" styleClass="button--secondary" immediate="true"  />
                                                            </h:panelGroup>
                                                        </h:panelGroup>
                                                        <h:panelGroup rendered="#{departmentsController.departmentToUpdateHasVirtualDepartments}" >
                                                            <e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_VIRTUAL_DEPARTMENTS.PREFIX']} " />
                                                            <t:dataList id="virtualData" value="#{departmentsController.departmentToUpdateVirtualDepartments}"
                                                                var="department" rowIndexVar="variable" >
                                                                <e:italic rendered="#{variable != 0}" value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_VIRTUAL_DEPARTMENTS.SEPARATOR']} " />
                                                                <e:italic value="#{department.label}" />
                                                            </t:dataList>
                                                            <e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_VIRTUAL_DEPARTMENTS.SUFFIX']} " />
                                                            <t:htmlTag value="br"/>
                                                        </h:panelGroup>
                                                        <h:panelGroup rendered="#{departmentsController.departmentToUpdateHasCategories}" >
                                                            <e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_CATEGORIES.PREFIX']} " />
                                                            <t:dataList id="subData" value="#{departmentsController.departmentToUpdateCategories}"
                                                                var="category" rowIndexVar="variable" >
                                                                <e:italic rendered="#{variable != 0}" value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_CATEGORIES.SEPARATOR']} " />
                                                                <e:italic value="#{category.label}" />
                                                            </t:dataList>
                                                            <e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.HAS_CATEGORIES.SUFFIX']} " />
                                                            <t:htmlTag value="br"/>
                                                        </h:panelGroup>
                                                        <e:italic value="#{msgs['DEPARTMENT_EDIT.TEXT.NO_REDIRECTION_POSSIBLE']}"
                                                            rendered="#{departmentsController.departmentToUpdateHasVirtualDepartments or departmentsController.departmentToUpdateHasCategories}" />
                                                    </t:htmlTag>
                                                 </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                      <e:outputLabel for="autoExpire" value="#{msgs['DEPARTMENT_EDIT.TEXT.AUTO_EXPIRE']}" />
                                                      <e:inputText id="autoExpire" value="#{departmentsController.departmentToUpdate.autoExpire}" size="3"/>
                                                      <e:message for="autoExpire" />
                                                      <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.AUTO_EXPIRE_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="spentTimeNeeded" value="#{msgs['DEPARTMENT_EDIT.TEXT.SPENT_TIME_NEEDED']}" />
                                                    <e:selectBooleanCheckbox id="spentTimeNeeded" value="#{departmentsController.departmentToUpdate.spentTimeNeeded}" />
                                                    <e:message for="spentTimeNeeded" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.SPENT_TIME_NEEDED_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="filter" value="#{msgs['DEPARTMENT_EDIT.TEXT.FILTER']}" />
                                                    <e:inputText id="filter"  value="#{departmentsController.departmentToUpdate.filter}" />
                                                    <e:message for="filter" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.departmentToUpdate.virtual}">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="defaultTicketScope" value="#{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_SCOPE']}"/>
                                                    <e:selectOneMenu id="defaultTicketScope"
                                                        value="#{departmentsController.departmentToUpdate.defaultTicketScope}" >
                                                        <f:selectItems value="#{departmentsController.scopeItems}" />
                                                    </e:selectOneMenu>
                                                    <e:message for="defaultTicketScope" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_SCOPE_HELP']}" >
                                                        <f:param value="#{msgs[ticketScopeI18nKeyProvider[domainService.departmentDefaultTicketScope]]}" />
                                                    </e:italic>
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="visibilityInterSrv" value="#{msgs['DEPARTMENT_EDIT.TEXT.VISIBILITE_INTER_SRV']}" />
                                                    <e:inputText id="visibilityInterSrv" value="#{departmentsController.departmentToUpdate.visibilityInterSrv}" />
                                                    <e:message for="visibilityInterSrv" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="srvAnonymous" value="#{msgs['DEPARTMENT_EDIT.TEXT.SERVICE_ANONYMOUS']}" />
                                                    <e:selectBooleanCheckbox id="srvAnonymous" value="#{departmentsController.departmentToUpdate.srvAnonymous}" />
                                                    <e:message for="srvAnonymous" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.departmentToUpdate.virtual}">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="defaultTicketPriority" value="#{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_PRIORITY']}"/>
                                                    <e:selectOneMenu id="defaultTicketPriority" value="#{departmentsController.departmentToUpdate.defaultTicketPriority}" >
                                                        <f:selectItems value="#{departmentsController.priorityItems}" />
                                                    </e:selectOneMenu>
                                                    <e:message for="defaultTicketPriority" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.departmentToUpdate.virtual}">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="defaultTicketLabel" value="#{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_LABEL']}"/>
                                                    <e:inputText id="defaultTicketLabel" value="#{departmentsController.departmentToUpdate.defaultTicketLabel}" />
                                                    <e:message for="defaultTicketLabel" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_LABEL_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.departmentToUpdate.virtual}">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="defaultTicketMessage" value="#{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_MESSAGE']}"/>
                                                    <fck:editor
                                                        id="defaultTicketMessage"
                                                        value="#{departmentsController.departmentToUpdate.defaultTicketMessage}"
                                                        toolbarSet="actionMessage" />
                                                    <e:message for="defaultTicketMessage" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.DEFAULT_TICKET_MESSAGE_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.departmentToUpdate.virtual}">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="assignmentAlgorithm" value="#{msgs['DEPARTMENT_EDIT.TEXT.ASSIGNMENT_ALGORITHM']}"/>
                                                    <e:selectOneMenu id="assignmentAlgorithm" value="#{departmentsController.departmentToUpdate.assignmentAlgorithmName}" >
                                                        <f:selectItems value="#{departmentsController.assignmentAlgorithmItems}" />
                                                    </e:selectOneMenu>
                                                    <e:message for="assignmentAlgorithm" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.ASSIGNMENT_ALGORITHM_HELP']}" >
                                                        <f:param value="#{assignmentAlgorithmI18nDescriptionProvider[domainService.defaultAssignmentAlgorithmName]}" />
                                                    </e:italic>
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.departmentToUpdate.virtual}" >
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="computerUrlBuilder" value="#{msgs['DEPARTMENT_EDIT.TEXT.COMPUTER_URL_BUILDER']}"/>
                                                    <e:selectOneMenu id="computerUrlBuilder" value="#{departmentsController.departmentToUpdate.computerUrlBuilderName}" >
                                                        <f:selectItems value="#{departmentsController.computerUrlBuilderItems}" />
                                                    </e:selectOneMenu>
                                                    <e:message for="computerUrlBuilder" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.COMPUTER_URL_BUILDER_HELP']}" >
                                                        <f:param value="#{computerUrlBuilderI18nDescriptionProvider[domainService.defaultComputerUrlBuilderName]}" />
                                                    </e:italic>
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.departmentToUpdate.virtual}">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="hideToExternalUsers" value="#{msgs['DEPARTMENT_EDIT.TEXT.HIDDEN_TO_APPLICATION_USERS']}"/>
                                                    <e:selectBooleanCheckbox id="hideToExternalUsers" value="#{departmentsController.departmentToUpdate.hideToExternalUsers}" />
                                                    <e:message for="hideToExternalUsers" />
                                                    <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.HIDDEN_TO_APPLICATION_USERS_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.departmentToUpdate.virtual}" >
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <t:htmlTag value="h3">
                                                        <h:outputText value="#{msgs['DEPARTMENT_EDIT.TEXT.EXTRA_MONITORING']}"/>
                                                    </t:htmlTag>
                                                    <t:htmlTag value="div" styleClass="form-item">
                                                         <e:outputLabel for="monitoringEmail" value="#{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_EMAIL']}" />
                                                         <e:inputText id="monitoringEmail" size="80" value="#{departmentsController.departmentToUpdate.monitoringEmail}" />
                                                         <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_EMAIL_HELP']}" />
                                                    </t:htmlTag>
                                                   <t:htmlTag value="div" id="monitoringEmailAuthTypeData" styleClass="form-item">
                                                        <e:outputLabel id="monitoringEmailAuthTypeLabel" for="monitoringEmailAuthType" value="#{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_EMAIL_AUTH_TYPE']}" />
                                                        <e:selectOneMenu id="monitoringEmailAuthType" value="#{departmentsController.departmentToUpdate.monitoringEmailAuthType}" >
                                                            <f:selectItems value="#{departmentsController.monitoringEmailAuthTypeItems}" />
                                                        </e:selectOneMenu>
                                                        <e:italic value=" #{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_EMAIL_AUTH_TYPE_HELP']}" />
                                                   </t:htmlTag>
                                                   <t:htmlTag value="div" id="monitoringLevelLabel" styleClass="form-item">
                                                        <e:outputLabel  for="monitoringLevel" value="#{msgs['DEPARTMENT_EDIT.TEXT.MONITORING_LEVEL']}" />
                                                        <e:selectOneMenu id="monitoringLevel" value="#{departmentsController.departmentToUpdate.monitoringLevel}" >
                                                            <f:selectItems value="#{departmentsController.monitoringLevelItems}" />
                                                        </e:selectOneMenu>
                                                   </t:htmlTag>
                                                </t:htmlTag>
                                           </t:htmlTag>

                                                <t:htmlTag value="div" styleClass="form-item display-flex">
                                                    <e:commandButton styleClass="button--primary" id="updateButton"
                                                        action="#{departmentsController.updateDepartment}"
                                                        value="#{msgs['DEPARTMENT_EDIT.BUTTON.UPDATE']}" />
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
