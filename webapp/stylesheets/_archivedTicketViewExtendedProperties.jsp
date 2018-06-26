<%@include file="_include.jsp"%>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.SCOPE']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
            <e:text value="#{msgs[ticketScopeI18nKeyProvider[archivedTicketController.archivedTicket.effectiveScope]]} " />
        </t:htmlTag>
</t:htmlTag>


<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.PRIORITY']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
	        <e:text value="#{msgs[priorityI18nKeyProvider[archivedTicketController.archivedTicket.priorityLevel]]}" />
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.COMPUTER']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
	        	<e:text
            			rendered="#{archivedTicketController.archivedTicket.computer != null}"
            			value="#{archivedTicketController.archivedTicket.computer} " />
                <e:italic
            			rendered="#{archivedTicketController.archivedTicket.computer == null}"
            			value="#{msgs['TICKET_VIEW.PROPERTIES.NO_COMPUTER']} " />
        </t:htmlTag>
</t:htmlTag>


<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.SPENT_TIME']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
		    <e:text value="#{spentTimeI18nFormatter[archivedTicketController.archivedTicket.spentTime]}"
			rendered="#{archivedTicketController.archivedTicket.spentTime != -1}"/>
			<e:italic value="#{msgs['TICKET_VIEW.PROPERTIES.NO_SPENT_TIME']}"
			rendered="#{archivedTicketController.archivedTicket.spentTime == -1}"/>
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.ORIGIN']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
	       <e:text value="#{msgs[originI18nKeyProvider[archivedTicketController.archivedTicket.origin]]} " />
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.CREATION_DATE']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
	        <e:text value="{0}" >
		        <f:param value="#{archivedTicketController.archivedTicket.creationDate}"/>
	        </e:text>
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['ARCHIVED_TICKET_VIEW.PROPERTIES.ARCHIVING_DATE']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
	        <e:text value="{0}" >
		        <f:param value="#{archivedTicketController.archivedTicket.archivingDate}"/>
	        </e:text>
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block" rendered="#{archivedTicketController.archivedTicket.chargeTime != null}">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.CHARGE_TIME']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
	       <e:text value="#{elapsedTimeI18nFormatter[archivedTicketController.archivedTicket.chargeTime]}" />
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block" rendered="#{archivedTicketController.archivedTicket.closureTime != null}">
        <t:htmlTag value="div">
            <t:outputText value=" #{msgs['TICKET_VIEW.PROPERTIES.CLOSURE_TIME']}" />
        </t:htmlTag>
        <t:htmlTag value="div">
	       <e:text value="#{elapsedTimeI18nFormatter[archivedTicketController.archivedTicket.closureTime]}" />
        </t:htmlTag>
</t:htmlTag>

