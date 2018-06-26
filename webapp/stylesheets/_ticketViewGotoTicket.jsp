<%@include file="_include.jsp"%>
        <t:htmlTag value="div" styleClass="form-block form-goto-ticket">
            <t:htmlTag value="div" styleClass="form-item">
                <e:inputText id="ticketNumber" value="#{controlPanelController.ticketNumberString}" size="7"
                    onkeypress="if (event.keyCode == 13) { buttonClick('viewTicketForm:gotoTicketButton'); return false; }" />
             </t:htmlTag>
            <t:htmlTag value="div" styleClass="form-item">
                <e:commandButton  id="gotoTicketButton"
                            styleClass="button--secondary"
                            action="#{controlPanelController.gotoTicket}"
                            value="#{msgs['CONTROL_PANEL.BUTTON.GOTO_TICKET']}" />
             </t:htmlTag>
         </t:htmlTag>


