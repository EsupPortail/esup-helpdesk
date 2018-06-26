<%@include file="_include.jsp"%>
<t:htmlTag value="div" styleClass="tickets-navigation">
        <t:htmlTag value="div" styleClass="nav-item">
            <t:htmlTag value="div" rendered="#{ticketController.previousUnreadTicket != null}" >
                <h:panelGroup 
                    onclick="buttonClick('viewTicketForm:gotoPreviousUnreadTicketButton');">
                    <t:htmlTag value="i" styleClass="far fa-caret-square-left tooltip active">
                    <h:panelGroup styleClass="tooltiptext">
                        <h:outputText value="#{ticketController.previousUnreadTicketTitle}"/>
                    </h:panelGroup>
                    </t:htmlTag>
                </h:panelGroup>
                <e:commandButton style="display: none" id="gotoPreviousUnreadTicketButton"
                    action="viewTicket"
                    value="#{ticketController.previousUnreadTicketTitle}" >
                    <t:updateActionListener
                        value="#{ticketController.previousUnreadTicket}"
                        property="#{ticketController.ticket}" />
                </e:commandButton>
            </t:htmlTag>

            <t:htmlTag value="div" rendered="#{ticketController.previousUnreadTicket == null}" >
                <t:htmlTag value="i" styleClass="far fa-caret-square-left inactive"/>
            </t:htmlTag>
        </t:htmlTag>

        <t:htmlTag value="div" styleClass="nav-item">
            <t:htmlTag value="div" rendered="#{ticketController.previousVisibleTicket != null}" >
                <h:panelGroup 
                    onclick="buttonClick('viewTicketForm:gotoPreviousVisibleTicketButton');">
                    <t:htmlTag value="i" styleClass="fas fa-angle-left tooltip active">
                        <h:panelGroup styleClass="tooltiptext">
                             <h:outputText value="#{ticketController.previousVisibleTicketTitle}"/>
                        </h:panelGroup>
                    </t:htmlTag>
                </h:panelGroup>
                <e:commandButton style="display: none" id="gotoPreviousVisibleTicketButton"
                    action="viewTicket"
                    value="#{ticketController.previousVisibleTicketTitle}" >
                    <t:updateActionListener
                        value="#{ticketController.previousVisibleTicket}"
                        property="#{ticketController.ticket}" />
                </e:commandButton>
            </t:htmlTag>
            <t:htmlTag value="div" rendered="#{ticketController.previousVisibleTicket == null}" >
                <t:htmlTag value="i" styleClass="fas fa-angle-left inactive"/>
            </t:htmlTag>
        </t:htmlTag>

        <t:htmlTag value="div" styleClass="nav-item">
            <t:htmlTag value="div" rendered="#{ticketController.nextVisibleTicket != null}" >
                <h:panelGroup 
                    onclick="buttonClick('viewTicketForm:gotoNextVisibleTicketButton');">
                    <t:htmlTag value="i" styleClass="fas fa-angle-right tooltip active">
                        <h:panelGroup styleClass="tooltiptext">
                             <h:outputText value="#{ticketController.nextVisibleTicketTitle}"/>
                        </h:panelGroup>
                    </t:htmlTag>
                </h:panelGroup>
                <e:commandButton style="display: none" id="gotoNextVisibleTicketButton"
                    action="viewTicket"
                    value="#{ticketController.nextVisibleTicketTitle}" >
                    <t:updateActionListener
                        value="#{ticketController.nextVisibleTicket}"
                        property="#{ticketController.ticket}" />
                </e:commandButton>
            </t:htmlTag>
            <t:htmlTag value="div" rendered="#{ticketController.nextVisibleTicket == null}" >
                <t:htmlTag value="i" styleClass="fas fa-angle-right inactive"/>
            </t:htmlTag>
        </t:htmlTag>

        <t:htmlTag value="div" styleClass="nav-item">
            <t:htmlTag value="div" rendered="#{ticketController.nextUnreadTicket != null}" >
                <h:panelGroup 
                    onclick="buttonClick('viewTicketForm:gotoNextUnreadTicketButton');">
                    <t:htmlTag value="i" styleClass="far fa-caret-square-right tooltip active">
                    <h:panelGroup styleClass="tooltiptext">
                        <h:outputText value="#{ticketController.nextUnreadTicketTitle}"/>
                    </h:panelGroup>
                    </t:htmlTag>
                </h:panelGroup>
                <e:commandButton style="display: none" id="gotoNextUnreadTicketButton"
                    action="viewTicket"
                    value="#{ticketController.nextUnreadTicketTitle}" >
                    <t:updateActionListener
                        value="#{ticketController.nextUnreadTicket}"
                        property="#{ticketController.ticket}" />
                </e:commandButton>
            </t:htmlTag>
             <t:htmlTag value="div" rendered="#{ticketController.nextUnreadTicketTitle == null}" >
                 <t:htmlTag value="i" styleClass="far fa-caret-square-right inactive"/>
             </t:htmlTag>
        </t:htmlTag>
        <%@include file="_ticketViewGotoTicket.jsp"%>

 </t:htmlTag>

