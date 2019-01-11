<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="preferences" locale="#{sessionController.locale}" >
<t:htmlTag id="preferences" value="div" styleClass="page-wrapper preferences">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content">
                    <t:htmlTag value="div" styleClass="content-inner">

                    <h:panelGroup rendered="#{not preferencesController.pageAuthorized}" >
                        <%@include file="_auth.jsp"%>
                    </h:panelGroup>
                    <e:form
                        freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                        showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                        showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                        id="preferencesForm" rendered="#{preferencesController.pageAuthorized}" >
                             <t:htmlTag value="div" styleClass="message">
                                     <e:messages/>
                             </t:htmlTag>
                             <t:htmlTag value="div" styleClass="dashboard-header">
                                <t:htmlTag value="div" styleClass="controlPanel-title">
                                    <t:htmlTag value="h1">
                                        <t:htmlTag value="span">
                                            <h:outputText value="#{msgs['PREFERENCES.TITLE']}"/>
                                        </t:htmlTag>
                                        <h:panelGroup rendered="#{preferencesController.userManagerOrAdmin}">
                                             <t:htmlTag value="span">
                                                <h:outputText value="#{msgs['CONTROL_PANEL.TITLE.USER']}"/>
                                             </t:htmlTag>
                                        </h:panelGroup>
                                    </t:htmlTag>

                                    <t:htmlTag styleClass="dashboard-toggle" value="div">
                                        <h:panelGroup
                                            rendered="#{preferencesController.userManagerOrAdmin}">
                                            <h:panelGroup style="cursor: pointer"
                                                onclick="simulateLinkClick('preferencesForm:managerPreferencesButton');">
                                                <t:htmlTag value="i"
                                                styleClass="fas fa-toggle-off"/>
                                            </h:panelGroup>
                                            <e:commandButton style="display: none" id="managerPreferencesButton" action="managerPreferences"
                                                value="#{msgs['PREFERENCES.BUTTON.MANAGER']}" immediate="true" />
                                        </h:panelGroup>
                                    </t:htmlTag>
                                </t:htmlTag>
                             </t:htmlTag>

                             <t:htmlTag value="fieldset">
                                <t:htmlTag value="legend">
                                    <t:htmlTag value="span" >
                                        <h:outputText value="#{msgs['PREFERENCES.HEADER.LANGUAGE']}"/>
                                    </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item">
                                     <e:selectOneMenu id="locale"
                                         value="#{preferencesController.locale}" converter="#{localeConverter}" >
                                         <f:selectItems value="#{preferencesController.localeItems}" />
                                     </e:selectOneMenu>
                                </t:htmlTag>
                              </t:htmlTag>

                             <t:htmlTag value="fieldset" rendered="#{sessionController.applicationUser}">
                                <t:htmlTag value="legend">
                                    <t:htmlTag value="span" >
                                        <h:outputText value="#{msgs['PREFERENCES.CHANGE_PASSWORD.TITLE']}"/>
                                    </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item">
                                     <e:commandButton
                                         id="changePasswordButton"
                                         action="#{preferencesController.gotoChangePassword}"
                                         value="#{msgs['PREFERENCES.BUTTON.CHANGE_PASSWORD']}" immediate="true" />
                                </t:htmlTag>
                             </t:htmlTag>

                             <t:htmlTag value="fieldset">
                                <t:htmlTag value="legend">
                                    <t:htmlTag value="span" >
                                        <h:outputText value="#{msgs['PREFERENCES.HEADER.APPLICATION_BEHAVIOUR']}"/>
                                    </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                         <e:outputLabel for="startPage" value="#{msgs['PREFERENCES.TEXT.START_PAGE']} " />
                                         <e:selectOneMenu id="startPage" value="#{preferencesController.currentUser.startPage}" >
                                             <f:selectItems value="#{preferencesController.startPageItems}" />
                                         </e:selectOneMenu>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                        <e:outputLabel  for="pageTransition" value="#{msgs['PREFERENCES.TEXT.PAGE_TRANSITION']} " />
                                        <e:selectOneMenu id="pageTransition" value="#{preferencesController.currentUser.pageTransition}" >
                                            <f:selectItems value="#{preferencesController.pageTransitionItems}" />
                                        </e:selectOneMenu>
                                </t:htmlTag>
                               <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                        <e:selectBooleanCheckbox
                                            id="showAddTicketHelp"
                                            value="#{preferencesController.currentUser.showAddTicketHelp}" />
                                        <e:outputLabel for="showAddTicketHelp" value=" #{msgs['PREFERENCES.TEXT.SHOW_ADD_TICKET_HELP']}" />
                                </t:htmlTag>

                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                        <e:selectBooleanCheckbox
                                            id="showPopupOnClosure"
                                            value="#{preferencesController.currentUser.showPopupOnClosure}" />
                                        <e:outputLabel for="showPopupOnClosure" value=" #{msgs['PREFERENCES.TEXT.SHOW_POPUP_ON_CLOSURE']}" />
                                </t:htmlTag>
                             </t:htmlTag>

                             <t:htmlTag value="fieldset">
                                <t:htmlTag value="legend">
                                    <t:htmlTag value="span" >
                                        <h:outputText value="#{msgs['PREFERENCES.HEADER.MONITORING']}"/>
                                    </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                          <e:selectBooleanCheckbox id="ownerMonitoring"
                                              value="#{preferencesController.currentUser.ownerMonitoring}" />
                                          <e:outputLabel for="ownerMonitoring" value=" #{msgs['PREFERENCES.TEXT.MONITORING.OWNER']}" />
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                       <e:selectBooleanCheckbox id="invitedMonitoring"
                                             value="#{preferencesController.currentUser.invitedMonitoring}" />
                                         <e:outputLabel for="invitedMonitoring" value=" #{msgs['PREFERENCES.TEXT.MONITORING.INVITED']}" />
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                         <e:selectBooleanCheckbox id="bookmarkMonitoring"
                                             value="#{preferencesController.currentUser.bookmarkMonitoring}" />
                                         <e:outputLabel for="bookmarkMonitoring" value=" #{msgs['PREFERENCES.TEXT.MONITORING.BOOKMARK']}" />
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                          <e:selectBooleanCheckbox id="expirationMonitoring"
                                              value="#{preferencesController.currentUser.expirationMonitoring}" />
                                          <e:outputLabel for="expirationMonitoring" value=" #{msgs['PREFERENCES.TEXT.MONITORING.EXPIRATION']}" />
                                </t:htmlTag>
                             </t:htmlTag>

                             <t:htmlTag value="div" styleClass="form-block">
                                        <t:htmlTag value="div" styleClass="form-item form-submit" >
                                            <e:commandButton styleClass="button--primary" value="#{msgs['_.BUTTON.UPDATE']}"
                                                    id="updateUserButton" action="#{preferencesController.updateUser}" />
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
