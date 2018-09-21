<%@include file="_include.jsp"%>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.SCOPE']}" />
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{!ticketController.userCanChangeScope}">
            <e:text rendered="#{ticketController.ticket.scope == 'DEFAULT'}"
				value=" #{msgs['TICKET_VIEW.PROPERTIES.SCOPE_VALUE_DEFAULT']} " >
				<f:param value="#{msgs[ticketScopeI18nKeyProvider['DEFAULT']]}" />
				<f:param value="#{msgs[ticketScopeI18nKeyProvider[ticketController.ticket.effectiveScope]]}" />
			</e:text>
			<e:text rendered="#{ticketController.ticket.scope != 'DEFAULT'}"
				value="#{msgs[ticketScopeI18nKeyProvider[ticketController.ticket.scope]]} " />
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{ticketController.userCanChangeScope}">
            <h:panelGroup onclick="showHideElement('viewTicketForm:editTicketScope'); showHideElement(this.id);" id="showScopeForm">
                <e:text  styleClass="link" rendered="#{ticketController.ticket.scope == 'DEFAULT'}"
                    value=" #{msgs['TICKET_VIEW.PROPERTIES.SCOPE_VALUE_DEFAULT']} " >
                    <f:param value="#{msgs[ticketScopeI18nKeyProvider['DEFAULT']]}" />
                    <f:param value="#{msgs[ticketScopeI18nKeyProvider[ticketController.ticket.effectiveScope]]}" />
                </e:text>
			    <e:text styleClass="link" rendered="#{ticketController.ticket.scope != 'DEFAULT'}" value="#{msgs[ticketScopeI18nKeyProvider[ticketController.ticket.scope]]} " />
			</h:panelGroup>
        </t:htmlTag>
        <t:htmlTag value="div" id="editTicketScope" styleClass="ticket-scope_edit" style="display: none" rendered="#{ticketController.userCanChangeScope}" >
                <t:htmlTag value="div" styleClass="ticket-property_edit-inner">
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:selectOneMenu id="ticketScope"
                            value="#{ticketController.ticketScope}">
                            <f:selectItems value="#{ticketController.ticketScopeItems}" />
                        </e:selectOneMenu>
                        <t:htmlTag value="div" rendered="#{ticketController.userCanSetNoAlert}" >
                            <e:selectBooleanCheckbox
                                value="#{ticketController.scopeNoAlert}" />
                            <e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
                        </t:htmlTag>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:commandButton styleClass="button--primary"
                         id="changeTicketScopeButton"
                         value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_SCOPE']}"
                         action="#{ticketController.doChangeTicketScope}"/>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <h:panelGroup styleClass="cancel" onclick="showHideElement('viewTicketForm:editTicketScope');showHideElement('viewTicketForm:showScopeForm')">
                            <h:outputText value=" #{msgs['_.BUTTON.CANCEL']} "/>
                         </h:panelGroup>
                    </t:htmlTag>

                </t:htmlTag>
        </t:htmlTag>
</t:htmlTag>


<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.PRIORITY']}" />
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{!ticketController.userCanChangePriority}">
			<e:text  rendered="#{ticketController.ticket.priorityLevel == 0}"
				value="#{msgs[priorityI18nKeyProvider[0]]} (#{msgs[priorityI18nKeyProvider[ticketController.ticket.effectivePriorityLevel]]}) " />
			<e:text rendered="#{ticketController.ticket.priorityLevel != 0}"
				value="#{msgs[priorityI18nKeyProvider[ticketController.ticket.priorityLevel]]} "/>
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{ticketController.userCanChangePriority}">
            <h:panelGroup onclick="showHideElement('viewTicketForm:editTicketPriority'); showHideElement(this.id);" id="showPriorityForm">
                <e:text  styleClass="link"  rendered="#{ticketController.ticket.priorityLevel == 0}"
                    value="#{msgs[priorityI18nKeyProvider[0]]} (#{msgs[priorityI18nKeyProvider[ticketController.ticket.effectivePriorityLevel]]}) " />
                <e:text styleClass="link"  rendered="#{ticketController.ticket.priorityLevel != 0}"
                    value="#{msgs[priorityI18nKeyProvider[ticketController.ticket.priorityLevel]]} "/>
			</h:panelGroup>
        </t:htmlTag>
        <t:htmlTag value="div" id="editTicketPriority" styleClass="ticket-priority_edit" style="display: none" rendered="#{ticketController.userCanChangePriority}" >
                <t:htmlTag value="div" styleClass="ticket-property_edit-inner">
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:selectOneMenu id="ticketPriority"
                            value="#{ticketController.ticketPriority}">
                            <f:selectItems value="#{ticketController.ticketPriorityItems}" />
                        </e:selectOneMenu>
                        <t:htmlTag value="div" rendered="#{ticketController.userCanSetNoAlert}" >
                            <e:selectBooleanCheckbox
                                value="#{ticketController.priorityNoAlert}" />
				            <e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
                        </t:htmlTag>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:commandButton styleClass="button--primary"
                         id="changeTicketPriorityButton"
                         value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_PRIORITY']}"
                         action="#{ticketController.doChangeTicketPriority}"/>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <h:panelGroup styleClass="cancel" onclick="showHideElement('viewTicketForm:editTicketPriority');showHideElement('viewTicketForm:showPriorityForm')">
                            <h:outputText value=" #{msgs['_.BUTTON.CANCEL']} "/>
                         </h:panelGroup>
                    </t:htmlTag>

                </t:htmlTag>
        </t:htmlTag>
</t:htmlTag>


<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.COMPUTER']}" />
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{!ticketController.userCanChangeComputer}">
            <e:text rendered="#{ticketController.ticket.computer == null}" value="#{msgs['TICKET_VIEW.PROPERTIES.NO_COMPUTER']} " />
            <h:panelGroup rendered="#{ticketController.ticket.computer != null}" >
                <e:text value="#{ticketController.ticket.computer}" />
                <h:panelGroup rendered="#{computerUrlProvider[ticketController.ticket] != null}" >
                    <h:outputFormat value=" <a href=&quot;{0}&quot; target=&quot;_blank&quot; >" escape="false" >
                        <f:param value="#{computerUrlProvider[ticketController.ticket]}" />
                    </h:outputFormat>
                        <t:htmlTag value="i" styleClass="fas fa-link"/>
                    <h:outputFormat value="</a>" escape="false" />
                </h:panelGroup>
            </h:panelGroup>
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{ticketController.userCanChangeComputer}">
            <h:panelGroup onclick="showHideElement('viewTicketForm:editTicketComputer'); showHideElement(this.id);" id="showComputerForm">
            <e:text styleClass="link" rendered="#{ticketController.ticket.computer == null}" value="#{msgs['TICKET_VIEW.PROPERTIES.NO_COMPUTER']} " />
            <h:panelGroup rendered="#{ticketController.ticket.computer != null}" >
                <e:text styleClass="link"  value="#{ticketController.ticket.computer}" />
                <h:panelGroup rendered="#{computerUrlProvider[ticketController.ticket] != null}" >
                    <h:outputFormat value=" <a href=&quot;{0}&quot; target=&quot;_blank&quot; >" escape="false" >
                        <f:param value="#{computerUrlProvider[ticketController.ticket]}" />
                    </h:outputFormat>
                        <t:htmlTag value="i" styleClass="fas fa-link"/>
                    <h:outputFormat value="</a>" escape="false" />
                </h:panelGroup>
            </h:panelGroup>

			</h:panelGroup>
        </t:htmlTag>

        <t:htmlTag value="div" id="editTicketComputer" styleClass="ticket-computer_edit" style="display: none" rendered="#{ticketController.userCanChangeComputer}" >
                <t:htmlTag value="div" styleClass="ticket-property_edit-inner">
                    <t:htmlTag value="div" styleClass="form-item">
			            <e:inputText id="ticketComputer" size="30" value="#{ticketController.ticketComputer}" />
                        <t:htmlTag value="div" rendered="#{ticketController.userCanSetNoAlert}" >
                            <e:selectBooleanCheckbox
                                value="#{ticketController.computerNoAlert}" />
				            <e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
                        </t:htmlTag>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:commandButton styleClass="button--primary"
                         id="changeTicketComputerButton"
                         value="#{msgs['_.BUTTON.CHANGE']}"
                         action="#{ticketController.doChangeTicketComputer}"/>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <h:panelGroup styleClass="cancel" onclick="showHideElement('viewTicketForm:editTicketComputer');showHideElement('viewTicketForm:showComputerForm')">
                            <h:outputText value=" #{msgs['_.BUTTON.CANCEL']} "/>
                         </h:panelGroup>
                    </t:htmlTag>
                </t:htmlTag>
        </t:htmlTag>
</t:htmlTag>


<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.SPENT_TIME']}" />
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{!ticketController.userCanChangeSpentTime}">
		    <e:text value="#{spentTimeI18nFormatter[ticketController.ticket.spentTime]}" rendered="#{ticketController.ticket.spentTime != -1}"/>
			<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.NO_SPENT_TIME']}" rendered="#{ticketController.ticket.spentTime == -1}"/>
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{ticketController.userCanChangeSpentTime}">
            <h:panelGroup onclick="showHideElement('viewTicketForm:editTicketSpentTime'); showHideElement(this.id);" id="showSpentTimeForm">
		        <e:text styleClass="link" value="#{spentTimeI18nFormatter[ticketController.ticket.spentTime]}" rendered="#{ticketController.ticket.spentTime != -1}"/>
			    <e:text styleClass="link" value="#{msgs['TICKET_VIEW.PROPERTIES.NO_SPENT_TIME']}" rendered="#{ticketController.ticket.spentTime == -1}"/>
			</h:panelGroup>
        </t:htmlTag>

        <t:htmlTag value="div" id="editTicketSpentTime" styleClass="ticket-spent-time_edit" style="display: none" rendered="#{ticketController.userCanChangeComputer}" >
                <t:htmlTag value="div" styleClass="ticket-property_edit-inner">
                    <t:htmlTag value="div" styleClass="form-item">
			            <%@include file="_ticketEditSpentTime.jsp"%>
                        <t:htmlTag value="div" rendered="#{ticketController.userCanSetNoAlert}" >
                            <e:selectBooleanCheckbox
                                value="#{ticketController.spentTimeNoAlert}" />
				            <e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
                        </t:htmlTag>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:commandButton styleClass="button--primary"
                         id="changeTicketSpentTimeButton"
                         value="#{msgs['_.BUTTON.CHANGE']}"
                         action="#{ticketController.doChangeTicketSpentTime}"/>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <h:panelGroup styleClass="cancel" onclick="showHideElement('viewTicketForm:editTicketSpentTime');showHideElement('viewTicketForm:showSpentTimeForm')">
                            <h:outputText value=" #{msgs['_.BUTTON.CANCEL']} "/>
                         </h:panelGroup>
                    </t:htmlTag>
                </t:htmlTag>
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.ORIGIN']}" />
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{!ticketController.userCanChangeOrigin}">
			<e:text value="#{msgs[originI18nKeyProvider[ticketController.ticket.origin]]} " />
        </t:htmlTag>
        <t:htmlTag value="div" rendered="#{ticketController.userCanChangeOrigin}">
            <h:panelGroup onclick="showHideElement('viewTicketForm:editTicketOrigin'); showHideElement(this.id);" id="showOriginForm">
                <e:text  styleClass="link" value="#{msgs[originI18nKeyProvider[ticketController.ticket.origin]]} " />
			</h:panelGroup>
        </t:htmlTag>
        <t:htmlTag value="div" id="editTicketOrigin" styleClass="ticket-origin_edit" style="display: none" rendered="#{ticketController.userCanChangeOrigin}" >
                <t:htmlTag value="div" styleClass="ticket-property_edit-inner">
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:selectOneMenu id="ticketOrigin"
                            value="#{ticketController.ticketOrigin}">
                            <f:selectItems value="#{ticketController.originItems}" />
                        </e:selectOneMenu>
                        <t:htmlTag value="div" rendered="#{ticketController.userCanSetNoAlert}" >
                            <e:selectBooleanCheckbox
                                value="#{ticketController.originNoAlert}" />
				            <e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
                        </t:htmlTag>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:commandButton styleClass="button--primary"
                         id="changeTicketOriginButton"
                         value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_ORIGIN']}"
                         action="#{ticketController.doChangeTicketOrigin}"/>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <h:panelGroup styleClass="cancel" onclick="showHideElement('viewTicketForm:editTicketOrigin');showHideElement('viewTicketForm:showOriginForm')">
                            <h:outputText value=" #{msgs['_.BUTTON.CANCEL']} "/>
                         </h:panelGroup>
                    </t:htmlTag>

                </t:htmlTag>
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.CREATION_DATE']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
			<e:text value="{0}" >
            		<f:param value="#{ticketController.ticket.creationDate}"/>
            </e:text>
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.LAST_ACTION_DATE']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
			<e:text value="{0}" >
            		<f:param value="#{ticketController.ticket.lastActionDate}"/>
            </e:text>
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block" rendered="#{ticketController.ticket.chargeTime != null}">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.CHARGE_TIME']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
			<e:text value="#{elapsedTimeI18nFormatter[ticketController.ticket.chargeTime]}" />
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block" rendered="#{ticketController.ticket.closureTime != null}">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.CLOSURE_TIME']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
			<e:text value="#{elapsedTimeI18nFormatter[ticketController.ticket.closureTime]}" />
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['COMMON.PERM_LINKS.PROMPT']}" />
        </t:htmlTag>
        <t:htmlTag value="div" styleClass="form-item">
            <h:panelGroup styleClass="button--default copy no-margin">
                    <h:outputText value=" #{msgs['COMMON.PERM_LINKS.COPY_LINK']}"/>
            </h:panelGroup>
        <e:text  id="ticketLink" style="display:none" escape="false" value="#{ticketController.permLink}" />
        </t:htmlTag>
</t:htmlTag>


