<%@include file="_include.jsp"%>

<t:htmlTag styleClass="ticket-property ticket-status" value="div">
    <t:htmlTag styleClass="ticket-property-inner" value="div">
        <t:htmlTag styleClass="ticket-property_label" value="div">
              <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.STATUS']} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_lib" value="div">
            	<e:text  value="#{msgs[ticketStatusI18nKeyProvider[archivedTicketController.archivedTicket.status]]} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_actions" value="div">

        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_info" value="div">
           
         </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>

<t:htmlTag styleClass="ticket-property ticket-category" value="div">
    <t:htmlTag styleClass="ticket-property-inner" value="div">
        <t:htmlTag styleClass="ticket-property_label" value="div">
              <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY']} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_lib" value="div">
            <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY_VALUE']}" >
                <f:param value="#{archivedTicketController.archivedTicket.department.label}" />
               <f:param value="#{archivedTicketController.archivedTicket.categoryLabel}" />
            </e:text>
        </t:htmlTag>
        <t:htmlTag id="ticket-category_actions" styleClass="ticket-property_actions form-item form-select actions" value="div">
     

        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_info" value="div">
           
         </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>

<t:htmlTag styleClass="ticket-property ticket-owner" value="div">
    <t:htmlTag styleClass="ticket-property-inner" value="div">
        <t:htmlTag styleClass="ticket-property_label" value="div">
              	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.OWNER']} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_lib" value="div">
            <e:text value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} " >
                <f:param value="#{userFormatter[archivedTicketController.archivedTicket.owner]}" />
            </e:text>
        </t:htmlTag>
        <t:htmlTag id="ticket-owner_actions" styleClass="ticket-property_actions form-item form-select actions" value="div">


        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_info" value="div">

        </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>



<t:htmlTag styleClass="ticket-property ticket-manager" value="div">
  <t:htmlTag styleClass="ticket-property-inner" value="div">
    <t:htmlTag styleClass="ticket-property_label" value="div">
      <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.MANAGER']} " />
    </t:htmlTag>
    <t:htmlTag styleClass="ticket-property_lib" value="div">
      <h:panelGroup rendered="#{archivedTicketController.archivedTicket.manager != null}" >
					<e:text value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} ">
						<f:param value="#{userFormatter[archivedTicketController.archivedTicket.manager]}" />
					</e:text>
      </h:panelGroup>
      <e:italic
        rendered="#{archivedTicketController.archivedTicket.manager == null}"
        value=" #{msgs['TICKET_VIEW.PROPERTIES.NO_MANAGER']} " />
    </t:htmlTag>

    <t:htmlTag id="ticket-manager_actions" styleClass="ticket-property_actions form-item form-select actions" value="div">

    </t:htmlTag>

    <t:htmlTag styleClass="ticket-property_info" value="div">

    </t:htmlTag>
  </t:htmlTag>
</t:htmlTag>