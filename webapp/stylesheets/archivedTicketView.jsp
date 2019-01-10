<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" 
    menuItem="" locale="#{sessionController.locale}" 
    downloadId="#{archivedTicketController.downloadId}" >
	   <t:htmlTag id="archivedTicketView" value="div" styleClass="page-wrapper ticketView">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                    <t:htmlTag value="div" styleClass="content-inner">
                        <h:panelGroup rendered="#{not archivedTicketController.userCanViewArchivedTicket}" >
                            <h:panelGroup rendered="#{archivedTicketController.currentUser == null}" >
                                 <%@include file="_auth.jsp"%> 
                            </h:panelGroup>
                            <h:panelGroup rendered="#{archivedTicketController.currentUser != null}" >
                                <e:messages/>
                            </h:panelGroup>
                        </h:panelGroup>

                        <e:form
                            freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                            showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                            showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                            id="viewArchivedTicketForm" enctype="multipart/form-data" 
                            rendered="#{archivedTicketController.userCanViewArchivedTicket}" >

                                <t:htmlTag value="div" styleClass="message ticketView-message">
                                    <e:messages/>
                                </t:htmlTag>

                                <t:htmlTag value="div" styleClass="region view-ticket_header sticky">
                                    <t:htmlTag styleClass="region-inner" value="div">
                                        <t:htmlTag value="div">
                                            <t:htmlTag value="div">
                                                <t:htmlTag value="span" styleClass="ticket-prefix"><h:outputText value="#{msgs['TICKET_VIEW.TITLE.PREFIX']}"/> </t:htmlTag>
                                                <t:htmlTag value="span" styleClass="ticket-number"><h:outputText  value="#{archivedTicketController.archivedTicket.ticketId} "/> </t:htmlTag>
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
                                                            <%@include file="_archivedTicketViewActionsButtons.jsp"%>
                                                        </t:htmlTag>
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>

                                    </t:htmlTag>
                                </t:htmlTag>

                                <t:htmlTag value="div" styleClass="region view-ticket_title">
                                    <t:htmlTag styleClass="region-inner" value="div">
                                         <t:htmlTag value="h1"><h:outputText value="#{archivedTicketController.archivedTicket.label}"/> </t:htmlTag>
                                    </t:htmlTag>
                                </t:htmlTag>


                                 <t:htmlTag styleClass="region view-ticket_main-properties" value="div">
                                        <t:htmlTag styleClass="ticketView-properties" value="div">
                                            <%@include file="_archivedTicketViewProperties.jsp"%>
                                        </t:htmlTag>

                                        <t:htmlTag styleClass="region view-ticket_extended-properties" value="div">
                                            <t:htmlTag styleClass="tabs" value="ul">
                                                <t:htmlTag id="history" styleClass="tab-link current" value="li">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.HISTORY.HEADER']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="extended-properties" styleClass="tab-link " value="li">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.PROPERTIES.HEADER']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="archived-files" styleClass="tab-link" value="li">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.FILES.HEADER_TAB']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="archived-invitations" styleClass="tab-link " value="li">
                                                    <h:outputText value="#{msgs['TICKET_VIEW.INVITATIONS.HEADER_TAB']} " />
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>

                                        <t:htmlTag id="tab-history" styleClass="view-ticket_history tab-content current" value="div">
                                            <%@include file="_archivedTicketViewHistory.jsp"%>
                                        </t:htmlTag>

                                        <t:htmlTag id="tab-extended-properties" styleClass="view-ticket_secondary_properties tab-content" value="div">
                                            <%@include file="_archivedTicketViewExtendedProperties.jsp"%>
                                        </t:htmlTag>

                                        <t:htmlTag id="tab-archived-files"  styleClass="view-ticket_files tab-content" value="div" >
                                            <%@include file="_archivedTicketViewFiles.jsp"%>
                                        </t:htmlTag>


                                        <t:htmlTag id="tab-archived-invitations" styleClass="view-ticket_invitations tab-content" value="div">
                                            <%@include file="_archivedTicketViewInvitations.jsp"%>
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
