<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">


		   <t:htmlTag id="categoryEdit" value="div" styleClass="page-wrapper categoryEdit">
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
                                id="categoryEditForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="dashboard-header">
                                          <t:htmlTag value="div" styleClass="controlPanel-title">
                                              <t:htmlTag value="h1">
                                                  <t:htmlTag value="span">
                                                      <h:outputText value="#{msgs['CATEGORY_EDIT.TITLE']}"/>
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
                                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                    <e:outputLabel for="id" value="#{msgs['CATEGORY_EDIT.TEXT.ID']}" />
                                                    <e:text id="id" value="#{departmentsController.categoryToUpdate.id}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="label"  value="#{msgs['CATEGORY_EDIT.TEXT.LABEL']}" />
                                                    <e:inputText id="label" value="#{departmentsController.categoryToUpdate.label}" required="true" />
                                                    <e:message for="label" />
                                                    <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.XLABEL_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="xlabel" value="#{msgs['CATEGORY_EDIT.TEXT.XLABEL']}" />
                                                    <e:inputText id="xlabel" size="80"
                                                        value="#{departmentsController.categoryToUpdate.xlabel}"
                                                        required="true" />
                                                    <e:message for="xlabel" />
                                                    <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.XLABEL_HELP']}" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="realCategory" value="#{msgs['CATEGORY_EDIT.TEXT.REAL_CATEGORY']}" />
                                                    <t:htmlTag value="div" id="realCategory" >
                                                        <h:panelGroup rendered="#{not departmentsController.categoryToUpdateHasSubCategories}" >
                                                          <h:panelGroup rendered="#{departmentsController.categoryToUpdate.virtual}" >
                                                                 <t:htmlTag value="div">
                                                                     <h:panelGroup style="cursor: default">
                                                                        <t:htmlTag value="i" styleClass="redirect far fa-2x fa-arrow-alt-circle-right"/>
                                                                     </h:panelGroup>
                                                                     <e:text value=" #{msgs['CATEGORY_EDIT.TEXT.IS_VIRTUAL']}" >
                                                                          <f:param value="#{departmentsController.categoryToUpdate.realCategory.department.label}" />
                                                                          <f:param value="#{departmentsController.categoryToUpdate.realCategory.label}" />
                                                                     </e:text>
                                                                 </t:htmlTag>


                                                              <e:commandButton  id="setRealButton" action="#{departmentsController.setRealCategory}"
                                                                  value="#{msgs['CATEGORY_EDIT.BUTTON.SET_REAL']}" immediate="true" >
                                                                  <t:updateActionListener value="#{null}" property="#{departmentsController.targetCategory}" />
                                                              </e:commandButton>

                                                              <e:commandButton styleClass="button--secondary" id="changeRealButton" action="#{departmentsController.gotoSetRealCategory}"
                                                                  value="#{msgs['CATEGORY_EDIT.BUTTON.CHANGE_REAL']}" immediate="true"/>
                                                          </h:panelGroup>

                                                          <h:panelGroup rendered="#{not departmentsController.categoryToUpdate.virtual}" >
                                                              <e:commandButton id="setVirtualButton" action="#{departmentsController.gotoSetRealCategory}"
                                                                  value="#{msgs['CATEGORY_EDIT.BUTTON.SET_VIRTUAL']}" immediate="true" styleClass="button--secondary" />
                                                          </h:panelGroup>
                                                        </h:panelGroup>

                                                        <h:panelGroup rendered="#{departmentsController.categoryToUpdateHasSubCategories}" >
                                                          <e:italic value="#{msgs['CATEGORY_EDIT.TEXT.HAS_SUB_CATEGORIES.PREFIX']}" />
                                                          <t:dataList id="subData" value="#{departmentsController.categoryToUpdateSubCategories}"
                                                              var="category" rowIndexVar="variable">
                                                              <e:italic rendered="#{variable != 0}" value="#{msgs['CATEGORY_EDIT.TEXT.HAS_SUB_CATEGORIES.SEPARATOR']} " />
                                                              <e:italic value="#{category.label}"/>
                                                          </t:dataList>
                                                          <e:italic value="#{msgs['CATEGORY_EDIT.TEXT.HAS_SUB_CATEGORIES.SUFFIX']} " />
                                                          <t:htmlTag value="br"/>
                                                          <e:italic value="#{msgs['CATEGORY_EDIT.TEXT.NO_REDIRECTION_POSSIBLE']}" />
                                                        </h:panelGroup>
                                                    </t:htmlTag>
                                                 </t:htmlTag>
                                           </t:htmlTag>

                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                     <e:outputLabel for="autoExpire" value="#{msgs['CATEGORY_EDIT.TEXT.AUTO_EXPIRE']}"/>
                                                     <h:panelGroup>
                                                         <e:inputText id="autoExpire" size="4"
                                                             value="#{departmentsController.categoryToUpdate.autoExpire}" />
                                                         <e:message for="autoExpire" />
                                                         <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.AUTO_EXPIRE_HELP']}" />
                                                     </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                      <e:outputLabel for="defaultTicketScope" value="#{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_SCOPE']}"/>
                                                      <h:panelGroup>
                                                          <e:selectOneMenu id="defaultTicketScope"
                                                              value="#{departmentsController.categoryToUpdate.defaultTicketScope}" >
                                                              <f:selectItems value="#{departmentsController.scopeItems}" />
                                                          </e:selectOneMenu>
                                                          <e:message for="defaultTicketScope" />
                                                          <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_SCOPE_HELP']}" >
                                                              <f:param value="#{msgs[ticketScopeI18nKeyProvider[departmentsController.categoryToUpdate.defaultDefaultTicketScope]]}" />
                                                          </e:italic>
                                                      </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                     <e:outputLabel for="defaultTicketPriority" value="#{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_PRIORITY']}" />
                                                     <h:panelGroup>
                                                         <e:selectOneMenu id="defaultTicketPriority"
                                                             value="#{departmentsController.categoryToUpdate.defaultTicketPriority}" >
                                                             <f:selectItems value="#{departmentsController.priorityItems}" />
                                                         </e:selectOneMenu>
                                                         <e:message for="defaultTicketPriority" />
                                                         <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_PRIORITY_HELP']}" >
                                                             <f:param value="#{msgs[priorityI18nKeyProvider[departmentsController.categoryToUpdate.defaultDefaultTicketPriority]]}" />
                                                         </e:italic>
                                                     </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                     <e:outputLabel for="defaultTicketLabel" value="#{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_LABEL']}" />
                                                     <h:panelGroup>
                                                         <e:inputText id="defaultTicketLabel" size="50"
                                                             value="#{departmentsController.categoryToUpdate.defaultTicketLabel}" />
                                                         <e:message for="defaultTicketLabel" />
                                                         <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_LABEL_HELP']}" />
                                                     </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="defaultTicketMessage" value="#{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_MESSAGE']}"/>
                                                    <h:panelGroup>
                                                        <fck:editor
                                                            id="defaultTicketMessage"
                                                            value="#{departmentsController.categoryToUpdate.defaultTicketMessage}"
                                                            toolbarSet="actionMessage" />
                                                        <e:message for="defaultTicketMessage" />
                                                        <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_TICKET_MESSAGE_HELP']}" />
                                                    </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual and departmentsController.categoryToUpdateHasSubCategories}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="addNewTickets"  value="#{msgs['CATEGORY_EDIT.TEXT.ADD_NEW_TICKETS']}"/>
                                                    <h:panelGroup>
                                                        <e:selectBooleanCheckbox id="addNewTickets" value="#{departmentsController.categoryToUpdate.addNewTickets}" />
                                                        <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.ADD_NEW_TICKETS_HELP']}" />
                                                    </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual and not departmentController.categoryToUpdate.department.hideToExternalUsers}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="hideToExternalUsers" value="#{msgs['CATEGORY_EDIT.TEXT.HIDDEN_TO_APPLICATION_USERS']}" />
                                                        <h:panelGroup>
                                                            <e:selectBooleanCheckbox id="hideToExternalUsers"
                                                                value="#{departmentsController.categoryToUpdate.hideToExternalUsers}" />
                                                            <e:italic
                                                                value=" #{msgs['CATEGORY_EDIT.TEXT.HIDDEN_TO_APPLICATION_USERS_HELP']}" />
                                                        </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="cateInvisible" value="#{msgs['CATEGORY_EDIT.TEXT.CATEGORIE_INVISIBLE']}" />
                                                        <h:panelGroup>
                                                            <e:selectBooleanCheckbox id="cateInvisible"
                                                                value="#{departmentsController.categoryToUpdate.cateInvisible}" />
                                                            <e:italic
                                                                value=" #{msgs['CATEGORY_EDIT.TEXT.CATEGORIE_INVISIBLE_HELP']}" />
                                                        </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>                                            
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                     <e:outputLabel for="assignmentAlgorithm" value="#{msgs['CATEGORY_EDIT.TEXT.ASSIGNMENT_ALGORITHM']}"/>
                                                     <h:panelGroup>
                                                         <e:selectOneMenu id="assignmentAlgorithm"
                                                             value="#{departmentsController.categoryToUpdate.assignmentAlgorithmName}" >
                                                             <f:selectItems value="#{departmentsController.assignmentAlgorithmItems}" />
                                                         </e:selectOneMenu>
                                                         <e:message for="assignmentAlgorithm" />
                                                         <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.ASSIGNMENT_ALGORITHM_HELP']}" >
                                                             <f:param value="#{assignmentAlgorithmI18nDescriptionProvider[departmentsController.categoryToUpdateDefaultAssignmentAlgorithmName]}" />
                                                         </e:italic>
                                                     </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                   <t:htmlTag value="h3">
                                                        <h:outputText value="#{msgs['CATEGORY_EDIT.TEXT.EXTRA_MONITORING']}"/>
                                                    </t:htmlTag>
                                                     <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                        <e:outputLabel for="inheritMonitoring" value="#{msgs['CATEGORY_EDIT.TEXT.INHERIT_MONITORING']}" />
                                                        <e:selectBooleanCheckbox id="inheritMonitoring"
                                                            value="#{departmentsController.categoryToUpdate.inheritMonitoring}"
                                                            onchange="showHideElement('categoryEditForm:monitoringEmailContainer');showHideElement('categoryEditForm:monitoringLevelContainer');showHideElement('categoryEditForm:monitoringEmailAuthTypeContainer');" />
                                                        <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.INHERIT_MONITORING_HELP']}" />
                                                     </t:htmlTag>
                                                     <t:htmlTag value="div" styleClass="form-item" id="defautMonitoring">
                                                            <e:text value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_NO_MONITORING']}"
                                                                rendered="#{departmentsController.categoryToUpdate.defaultMonitoringEmail == null or departmentsController.categoryToUpdate.defaultMonitoringLevel == 0}" />
                                                            <e:text value=" #{msgs['CATEGORY_EDIT.TEXT.DEFAULT_MONITORING']}"
                                                                rendered="#{departmentsController.categoryToUpdate.defaultMonitoringEmail != null and departmentsController.categoryToUpdate.defaultMonitoringLevel != 0}" >
                                                                <f:param value="#{msgs[categoryMonitoringI18nKeyProvider[departmentsController.categoryToUpdate.defaultMonitoringLevel]]}" />
                                                                <f:param value="#{departmentsController.categoryToUpdate.defaultMonitoringEmail}" />
                                                            </e:text>
                                                     </t:htmlTag>
                                                     <t:htmlTag value="div" styleClass="region" id="extraMonitoring">

                                                         <t:htmlTag value="div" id="monitoringEmailContainer" styleClass="form-item" style="display: #{not departmentsController.categoryToUpdate.inheritMonitoring ? 'block' : 'none'}" >
                                                             <e:outputLabel id="monitoringEmailLabel" for="monitoringEmail" value="#{msgs['CATEGORY_EDIT.TEXT.MONITORING_EMAIL']}"/>
                                                             <h:panelGroup id="monitoringEmailData">
                                                                     <e:inputText id="monitoringEmail" size="80" value="#{departmentsController.categoryToUpdate.monitoringEmail}" />
                                                                     <e:italic value=" #{msgs['CATEGORY_EDIT.TEXT.MONITORING_EMAIL_HELP']}" />
                                                             </h:panelGroup>
                                                         </t:htmlTag>

                                                         <t:htmlTag value="div" id="monitoringLevelContainer" styleClass="form-item" style="display: #{not departmentsController.categoryToUpdate.inheritMonitoring ? 'block' : 'none'}" >
                                                            <e:outputLabel id="monitoringLevelLabel" for="monitoringLevel"
                                                                value="#{msgs['CATEGORY_EDIT.TEXT.MONITORING_LEVEL']}"/>
                                                            <h:panelGroup id="monitoringLevelData">
                                                                <e:selectOneMenu id="monitoringLevel" value="#{departmentsController.categoryToUpdate.monitoringLevel}" >
                                                                    <f:selectItems value="#{departmentsController.monitoringLevelItems}" />
                                                                </e:selectOneMenu>
                                                            </h:panelGroup>
                                                         </t:htmlTag>

                                                         <t:htmlTag value="div" id="monitoringEmailAuthTypeContainer" styleClass="form-item" style="display: #{not departmentsController.categoryToUpdate.inheritMonitoring ? 'block' : 'none'}">
                                                            <e:outputLabel id="monitoringEmailAuthTypeLabel" for="monitoringEmailAuthType"
                                                                value="#{msgs['CATEGORY_EDIT.TEXT.MONITORING_EMAIL_AUTH_TYPE']}" />
                                                            <h:panelGroup id="monitoringEmailAuthTypeData" >
                                                                <e:selectOneMenu id="monitoringEmailAuthType"
                                                                    value="#{departmentsController.categoryToUpdate.monitoringEmailAuthType}" >
                                                                    <f:selectItems value="#{departmentsController.monitoringEmailAuthTypeItems}" />
                                                                </e:selectOneMenu>
                                                                <e:italic
                                                                    value=" #{msgs['CATEGORY_EDIT.TEXT.MONITORING_EMAIL_AUTH_TYPE_HELP']}" />
                                                            </h:panelGroup>
                                                         </t:htmlTag>
                                                     </t:htmlTag>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.virtual}">
                                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                                <e:outputLabel for="permLinks"
                                                                    value="#{msgs['CATEGORY_EDIT.TEXT.ADD_LINK']}"
                                                                    rendered="#{not departmentsController.categoryToUpdate.virtual}" />
                                                                <h:panelGroup id="permLinks" rendered="#{not departmentsController.categoryToUpdate.virtual}" >
                                                                    <e:text value=" #{msgs['COMMON.PERM_LINKS.APPLICATION']}" escape="false" >
                                                                        <f:param value="#{departmentsController.categoryToUpdateApplicationAddLink}" />
                                                                    </e:text>
                                                                    <e:text value=" #{msgs['COMMON.PERM_LINKS.CAS']}" escape="false" >
                                                                        <f:param value="#{departmentsController.categoryToUpdateCasAddLink}" />
                                                                    </e:text>
                                                                    <e:text value=" #{msgs['COMMON.PERM_LINKS.SHIBBOLETH']}" escape="false" >
                                                                        <f:param value="#{departmentsController.categoryToUpdateShibbolethAddLink}" />
                                                                    </e:text>
                                                                </h:panelGroup>
                                                 </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-item display-flex">
                                                <e:commandButton styleClass="button--primary" id="updateButton"
                                                         action="#{departmentsController.updateCategory}"
                                                         value="#{msgs['CATEGORY_EDIT.BUTTON.UPDATE']}" />
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
