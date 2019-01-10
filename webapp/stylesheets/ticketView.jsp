<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="" locale="#{sessionController.locale}" downloadId="#{ticketController.downloadId}" >
	   <t:htmlTag id="ticketView" value="div" styleClass="page-wrapper ticketView">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                    <t:htmlTag value="div" styleClass="content-inner">
                        <h:panelGroup rendered="#{not ticketController.userCanViewTicket}" >
                            <h:panelGroup rendered="#{ticketController.currentUser == null}" >
                                <%-- <%@include file="_auth.jsp"%> --%>
                            </h:panelGroup>
                            <h:panelGroup rendered="#{ticketController.currentUser != null}" >
                                <e:messages/>
                            </h:panelGroup>
                        </h:panelGroup>

                        <e:form
                            freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                            showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                            showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                            id="viewTicketForm" enctype="multipart/form-data" rendered="#{ticketController.userCanViewTicket}" >

                                <t:htmlTag value="div" styleClass="message ticketView-message">
                                    <e:messages/>
                                </t:htmlTag>

                                <t:htmlTag value="div" styleClass="region view-ticket_header sticky">
                                    <t:htmlTag styleClass="region-inner" value="div">
                                        <t:htmlTag value="div">
                                            <t:htmlTag value="div">
                                                <t:htmlTag value="span" styleClass="ticket-prefix"><h:outputText value="#{msgs['TICKET_VIEW.TITLE.PREFIX']}"/> </t:htmlTag>
                                                <t:htmlTag value="span" styleClass="ticket-number"><h:outputText  value="#{ticketController.ticket.id} "/> </t:htmlTag>
                                            </t:htmlTag>
                                            <t:htmlTag value="div">
                                                <t:htmlTag id="ticket-actions" styleClass="form-item form-select actions" value="div">
                                                    <t:htmlTag styleClass="actions-header" value="div">
                                                        <t:htmlTag styleClass="actions-header-inner" value="div">
                                                            <t:htmlTag value="span"><h:outputText value="#{msgs['TICKET_VIEW.ACTIONS.HEADER']}"/></t:htmlTag>
                                                            <t:htmlTag value="i" styleClass="fas fa-chevron-down"/>
                                                        </t:htmlTag>
                                                     </t:htmlTag>

                                                    <t:htmlTag styleClass="actions-list hideme" value="div">
                                                        <t:htmlTag styleClass="actions-list-inner" value="div">
                                                            <%@include file="_ticketViewActionsButtons.jsp"%>
                                                        </t:htmlTag>
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                            </t:htmlTag>
											<h:panelGroup>
												<t:htmlTag value="div" styleClass="form-item returnPanel">
									                <e:commandButton  id="returnPanel"
									                            styleClass="button--secondary"
																action="back"											                            
																value="#{msgs['CONTROL_PANEL.BUTTON.RETURN.PANEL']}" />
									            </t:htmlTag>
											</h:panelGroup>                                            
                                        </t:htmlTag>
                                            <%@include file="_ticketViewNavigationButtons.jsp"%>
                                    </t:htmlTag>
                                </t:htmlTag>

                                <t:htmlTag value="div" styleClass="region view-ticket_title">
                                    <t:htmlTag styleClass="region-inner" value="div">
                                         <t:htmlTag value="h1"><h:outputText value="#{ticketController.ticket.label}"/> </t:htmlTag>
                                    </t:htmlTag>
                                </t:htmlTag>

                                <h:panelGroup styleClass="region ticketView-form-label" id="editTicketLabel" style="display: none" rendered="#{ticketController.userCanChangeLabel}" >
                                    <t:htmlTag value="fieldset" styleClass="active">
                                        <t:htmlTag value="legend">
                                            <t:htmlTag value="span">
                                                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_LABEL']}"/>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="form-block">
                                            <t:htmlTag value="div" styleClass="form-item">
                                                <e:inputText value="#{ticketController.ticketLabel}" size="80" />
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-item form-checkbox" rendered="#{ticketController.userCanSetNoAlert}">
                                                    <e:selectBooleanCheckbox id="labelNoAlert"
                                                        value="#{ticketController.labelNoAlert}" />
                                                     <e:outputLabel for="labelNoAlert" value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
                                            </t:htmlTag>

                                            <t:htmlTag value="div" styleClass="form-item display-flex">
                                                <e:commandButton
                                                    id="changeTicketLabelButton"
                                                    styleClass="button--primary"
                                                    value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_LABEL.BUTTON_TEXT']}"
                                                    action="#{ticketController.doChangeTicketLabel}">
                                                </e:commandButton>
                                                <h:panelGroup styleClass="cancel"  onclick="showHideElement('viewTicketForm:editTicketLabel');" >
                                                                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_LABEL.CANCEL_TEXT']} " />
                                                </h:panelGroup>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                    </t:htmlTag>
                                </h:panelGroup>

                                 <t:htmlTag styleClass="region view-ticket_main-properties" value="div">
                                        <t:htmlTag styleClass="ticketView-properties" value="div">
                                            <%@include file="_ticketViewProperties.jsp"%>
                                        </t:htmlTag>

                                        <t:htmlTag styleClass="region view-ticket_extended-properties" value="div">
                                            <t:htmlTag styleClass="tabs" value="ul">
                                                <t:htmlTag id="history" styleClass="tab-link current" value="li">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.HISTORY.HEADER']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="extended-properties" styleClass="tab-link " value="li">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.PROPERTIES.HEADER']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="files" styleClass="tab-link" value="li">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.FILES.HEADER_TAB']} " />
                                                    <h:outputText styleClass="entriesNumber" value="#{ticketController.fileInfoEntriesNumber}" />
                                                </t:htmlTag>
                                                <t:htmlTag id="invitations" styleClass="tab-link " value="li">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.INVITATIONS.HEADER_TAB']} " />
                                                     <h:outputText styleClass="entriesNumber" value="#{ticketController.invitationsNumber}" />
                                                </t:htmlTag>
                                                <t:htmlTag id="monitoring" styleClass="tab-link" value="li">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.MONITORING.HEADER_TAB']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="history_owner" styleClass="tab-link" value="li" rendered="#{departmentsController.currentUserCanViewDepartments}">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.HISTORY_OWNER.HEADER_TAB']} " />
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>

                                        <t:htmlTag id="tab-history" styleClass="view-ticket_history tab-content current" value="div">
                                            <%@include file="_ticketViewHistory.jsp"%>
                                        </t:htmlTag>

                                        <t:htmlTag id="tab-extended-properties" styleClass="view-ticket_secondary_properties tab-content" value="div">
                                            <%@include file="_ticketViewExtendedProperties.jsp"%>
                                        </t:htmlTag>

                                        <t:htmlTag id="tab-files"  styleClass="view-ticket_files tab-content" value="div" >
                                            <%@include file="_ticketViewFiles.jsp"%>
                                        </t:htmlTag>

                                         <t:htmlTag id="tab-invitations" styleClass="view-ticket_invitations tab-content" value="div" rendered="#{ticketController.userCanInvite}">
                                             <%@include file="_ticketViewInvitations.jsp"%>
                                         </t:htmlTag>

                                         <t:htmlTag id="tab-monitoring" styleClass="view-ticket_monitoring tab-content" value="div">
                                             <%@include file="_ticketViewMonitoring.jsp"%>
                                         </t:htmlTag>

                                         <t:htmlTag id="tab-history_owner" styleClass="view-ticket_history_owner tab-content" value="div">
                                             <%@include file="_ticketViewHistoryOwner.jsp"%>
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
