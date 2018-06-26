<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanConnect}">
		   <t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper ticketConnectToTicket">
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
                                                      <h:outputText value="#{msgs['TICKET_ACTION.TITLE.CONNECT_TO_TICKET']}" escape="false" />
                                                </t:htmlTag>
                                            </t:htmlTag>
                                    </t:htmlTag>
                                    <t:htmlTag value="div" styleClass="form-block form-body">
                                        <t:htmlTag value="div" styleClass="form-item source-ticket-id">
                                            <e:outputLabel  value=" #{msgs['TICKET_ACTION.TEXT.CONNECT_TO_TICKET.SOURCE']}"/>
                                            <t:htmlTag value="span">
                                                 <h:outputText value="#{ticketController.ticket.id}" escape="false" />
                                            </t:htmlTag>
                                        </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="form-item target-ticket-id">
                                            <e:outputLabel for="targetTicketId" value=" #{msgs['TICKET_ACTION.TEXT.CONNECT_TO_TICKET.CHOOSE_TARGET_TICKET']}"/>
                                            <e:inputText value="#{ticketController.targetTicketId}" id="targetTicketId" size="5" />
                                        </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="form-item display-flex">
                                            <e:commandButton styleClass="button--primary" id="nextButton" action="#{ticketController.setTargetTicketFromId}"
                                                value="#{msgs['_.BUTTON_SAVE']}" />
                                            <e:commandButton styleClass="cancel" id="backButton" action="#{ticketController.getConnectBackAction}"
                                                value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                        </t:htmlTag>
                                    </t:htmlTag>
                                 </t:htmlTag>

                        </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
    <t:htmlTag value="footer" styleClass="footer">
            <%@include file="_footer.jsp"%>
    </t:htmlTag>
</e:page>

