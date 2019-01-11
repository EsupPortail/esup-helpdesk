<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanAssign}">
<t:htmlTag id="ticketAssign" value="div" styleClass="page-wrapper ticketAssign">
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
                        id="ticketActionForm">
                                <t:htmlTag value="div" styleClass="message">
                                    <e:messages/>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="ticket-form">
                                    <t:htmlTag value="div" styleClass="form-block form-header">
                                        <t:htmlTag value="h1">
                                            <t:htmlTag value="span" styleClass="title">
                                                  <h:outputText value="#{msgs['TICKET_ACTION.TITLE.ASSIGN']}" escape="false" />
                                            </t:htmlTag>
                                            <t:htmlTag value="span" styleClass="subtitle">
                                                <h:outputText value=" #{ticketController.ticket.id}" escape="false" />
                                            </t:htmlTag>
                                        </t:htmlTag>
                                    </t:htmlTag>
                                </t:htmlTag>

                                <t:htmlTag styleClass="region" value="div" rendered="#{ticketController.targetManager == null}">
                                    <t:htmlTag styleClass="tabs without-state" value="ul">
                                        <t:htmlTag id="categoryMembers" styleClass="#{not empty ticketController.targetCategoryMembers?'tab-link without-state current':'tab-link without-state'} ;" value="li" rendered="#{not empty ticketController.targetCategoryMembers}">
                                            <h:outputText value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.CATEGORY_MEMBERS']} " />
                                        </t:htmlTag>
                                        <t:htmlTag id="nonCategoryMembers" styleClass="#{empty ticketController.targetCategoryMembers?'tab-link without-state current':'tab-link without-state'} ;" value="li" rendered="#{not empty ticketController.targetNonCategoryMembers}">
                                            <t:htmlTag value="span"><h:outputText value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.NON_CATEGORY_MEMBERS']}" /></t:htmlTag>
                                        </t:htmlTag>
                                    </t:htmlTag>
                                </t:htmlTag>

                                <t:htmlTag styleClass="region" value="div" rendered="#{ticketController.targetManager == null}">
                                    <t:htmlTag id="tab-categoryMembers" styleClass="#{not empty ticketController.targetCategoryMembers?'tab-content  without-state current':'tab-content without-state'} ;"  value="div" rendered="#{not empty ticketController.targetCategoryMembers}">
                                        <t:htmlTag value="div"  rendered="#{empty ticketController.targetCategoryMembers">
                                            <e:text value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.NO_AVAILABLE_MANAGER']}"/>
                                        </t:htmlTag>
                                        <t:htmlTag value="div" rendered="#{not empty ticketController.targetCategoryMembers}">
                                                <e:dataTable
                                                    id="targetCategoryMembersData"
                                                    styleClass="members-list"
                                                    value="#{ticketController.targetCategoryMembers}" var="departmentManager"
                                                    border="0" cellspacing="0" cellpadding="0" rowIndexVar="index">
                                                    <t:column>
                                                         <t:htmlTag value="div" styleClass="formated-user-data">
                                                            <t:htmlTag value="div" styleClass="user--display-name">
                                                                                <e:text value=" #{msgs['TICKET_ACTION.TEXT.ASSIGN.USER']}" >
                                                                                    <f:param value="#{userFormatter[departmentManager.user]}" />
                                                                                </e:text>
                                                            </t:htmlTag>
                                                            <t:htmlTag value="div" styleClass="form-item">
                                                                                  <e:commandButton  id="chooseManagerButton"
                                                                                      value="#{msgs['LDAP_SEARCH.BUTTON.USER.SELECT']}" immediate="true" styleClass="user-select">
                                                                                      <t:updateActionListener value="#{departmentManager.user}" property="#{ticketController.targetManager}" />
                                                                                  </e:commandButton>
                                                             </t:htmlTag>
                                                         </t:htmlTag>
                                                    </t:column>
                                                <f:facet name="footer">
                                                     <t:htmlTag value="div" styleClass="form-block">
                                                         <t:htmlTag value="div" styleClass="form-item form-submit">
                                                          <%@include file="_ticketActionCancel.jsp"%>
                                                         </t:htmlTag>
                                                     </t:htmlTag>
                                                </f:facet>
                                        </e:dataTable>
                                        </t:htmlTag>
                                    </t:htmlTag>

                                    <t:htmlTag id="tab-nonCategoryMembers" styleClass="#{empty ticketController.targetCategoryMembers?'tab-content  without-state current':'tab-content without-state'} ;"  value="div">
                                        <t:htmlTag value="div"  rendered="#{empty ticketController.targetNonCategoryMembers}">
                                            <e:text  value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.NO_AVAILABLE_MANAGER']}"/>
                                        </t:htmlTag>
                                        <t:htmlTag value="div" rendered="#{not empty ticketController.targetNonCategoryMembers}">
                                            <e:dataTable
                                                 id="targetNonCategoryMembersData"
                                                 styleClass="members-list"
                                                 value="#{ticketController.targetNonCategoryMembers}" var="departmentManager"
                                                 rendered="#{not empty ticketController.targetNonCategoryMembers}"
                                                 border="0" cellspacing="0" cellpadding="0" rowIndexVar="index">
                                                 <t:column>
                                                         <t:htmlTag value="div" styleClass="formated-user-data">
                                                            <t:htmlTag value="div" styleClass="user--display-name">
                                                                                <e:text value=" #{msgs['TICKET_ACTION.TEXT.ASSIGN.USER']}" >
                                                                                    <f:param value="#{userFormatter[departmentManager.user]}" />
                                                                                </e:text>
                                                            </t:htmlTag>
                                                            <t:htmlTag value="div" styleClass="form-item">
                                                                                  <e:commandButton  id="chooseManagerButton"
                                                                                      value="#{msgs['LDAP_SEARCH.BUTTON.USER.SELECT']}" immediate="true" styleClass="user-select">
                                                                                      <t:updateActionListener value="#{departmentManager.user}" property="#{ticketController.targetManager}" />
                                                                                  </e:commandButton>
                                                             </t:htmlTag>
                                                         </t:htmlTag>
                                                 </t:column>
                                                <f:facet name="footer">
                                                     <t:htmlTag value="div" styleClass="form-block">
                                                         <t:htmlTag value="div" styleClass="form-item form-submit">
                                                          <%@include file="_ticketActionCancel.jsp"%>
                                                         </t:htmlTag>
                                                     </t:htmlTag>
                                                </f:facet>
                                            </e:dataTable>
                                        </t:htmlTag>
                                    </t:htmlTag>
                                </t:htmlTag>

                                <t:htmlTag styleClass="region" value="div" rendered="#{ticketController.targetManager != null}">
                                    <t:htmlTag value="div" styleClass="ticket-form">
                                        <t:htmlTag value="div" styleClass="form-block form-body">
                                            <t:htmlTag value="div" styleClass="form-item">
                                               <t:htmlTag value="label">
                                                    <h:outputText value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.MANAGER_PROMPT']} " />
                                               </t:htmlTag>
                                               <t:htmlTag value="span" styleClass="selected-manager">
                                                    <h:outputText value="#{userFormatter[ticketController.targetManager]}" />
                                               </t:htmlTag>
                                            </t:htmlTag>

                                            <t:htmlTag value="div" styleClass="form-item">
                                                <e:outputLabel for="actionMessage" value=" #{msgs['TICKET_ACTION.TEXT.ASSIGN.2']}"/>
                                                <fck:editor
                                                    id="actionMessage"
                                                    value="#{ticketController.actionMessage}"
                                                    toolbarSet="actionMessage" />
                                            </t:htmlTag>
                                        </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="form-block">
                                            <t:htmlTag value="div" styleClass="form-item display-flex" >
                                                <e:commandButton id="actionButton"
                                                        styleClass="button--primary"
                                                        value="#{msgs['TICKET_ACTION.BUTTON.ASSIGN']}"
                                                        action="#{ticketController.doAssign}" />
                                                <%@include file="_ticketActionCancel.jsp"%>
                                           </t:htmlTag>
                                        </t:htmlTag>
                                    </t:htmlTag>
                                    <t:htmlTag styleClass="region extended-properties" value="div">
                                                <t:htmlTag styleClass="tabs" value="ul">
                                                    <t:htmlTag id="properties" styleClass="tab-link current" value="li">
                                                        <h:outputText value="#{msgs['TICKET_ACTION.TAB.GIVE_INFO.PROPERTIES.TEXT']} " />
                                                    </t:htmlTag>
                                                    <t:htmlTag id="history" styleClass="tab-link " value="li">
                                                        <h:outputText value="#{msgs['TICKET_ACTION.TAB.HISTORY.TEXT']} " />
                                                    </t:htmlTag>
                                                    <t:htmlTag id="files" styleClass="tab-link" value="li">
                                                        <h:outputText value="#{msgs['TICKET_ACTION.TAB.FILES.TEXT']} " />
                                                    </t:htmlTag>
                                                    <t:htmlTag id="cannedResponses" styleClass="tab-link " value="li" rendered="#{ticketController.userCanUseCannedResponses and not empty ticketController.responseItems}">
                                                        <h:outputText value="#{msgs['TICKET_ACTION.TAB.RESPONSES.TEXT']} " />
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                    </t:htmlTag>
                                    <t:htmlTag id="tab-properties" styleClass="tab-content current" value="div">
                                            <t:htmlTag value="div" styleClass="form-block">
                                                <%@include file="_ticketActionScope.jsp"%>
                                                <t:htmlTag value="div" styleClass="form-item form-checkbox" rendered="#{ticketController.userCanSetNoAlert}" >
                                                    <e:selectBooleanCheckbox id="noAlert"
                                                        value="#{ticketController.noAlert}" />
                                                    <e:outputLabel for="noAlert" value=" #{msgs['TICKET_ACTION.TEXT.NO_NOTIFICATION']}"/>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                    </t:htmlTag>
                                    <t:htmlTag id="tab-history" styleClass="tab-content view-ticket_history" value="div">
                                            <%@include file="_ticketActionHistory.jsp"%>
                                    </t:htmlTag>
                                    <t:htmlTag id="tab-files" styleClass="tab-content" value="div">
                                            <%@include file="_ticketActionTabUpload.jsp"%>
                                    </t:htmlTag>
                                    <t:htmlTag id="tab-cannedResponses" styleClass="tab-content" value="div">
                                            <%@include file="_ticketActionResponses.jsp"%>
                                    </t:htmlTag>
                               </t:htmlTag>
                    </e:form>

	                <%@include file="_ticketActionJavascript.jsp"%>
                    </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
                <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
                </t:htmlTag>
        </t:htmlTag>

</e:page>

