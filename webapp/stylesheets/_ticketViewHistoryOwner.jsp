<%@include file="_include.jsp"%>
<script type="text/javascript">

	function selectHistoryTicket(index) {
		buttonClick('viewTicketForm:historyOwnerData:'+index+':viewTicketButton');
	}
</script>

           
     <e:dataTable
         id="historyOwnerData" rowIndexVar="variable"
         value="#{ticketController.ticketsByOwner}" var="cipe"
         border="0" style="width:100%" cellspacing="0" cellpadding="0"
         rendered="#{not empty ticketController.ticketsByOwner}"
         columnClasses="colLeftNowrap,colLeft,colLeft,colLeft">

         <t:column style="cursor: #{cipe.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
             <f:facet name="header">
                 <e:text value="#{msgs['TICKET.HISTORY.OWNER.ENTRY.NUMBER.LIB']}" />
             </f:facet>
             <h:panelGroup>
                     <h:outputText value="#{cipe.ticket.id} " />
             </h:panelGroup>
         </t:column>
         <t:column style="cursor: #{cipe.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
             <f:facet name="header">
                 <e:text value="#{msgs['TICKET.HISTORY.OWNER.ENTRY.TICKET.LIB']}" />
             </f:facet>
              <h:panelGroup>
                      <h:outputText value="#{cipe.ticket.label}" />
              </h:panelGroup>
         </t:column>
         <t:column style="cursor: #{cipe.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
             <f:facet name="header">
                 <e:bold value="#{msgs['TICKET.HISTORY.OWNER.HEADER.DEPARTMENT']}" />
             </f:facet>
             <e:text value="#{cipe.ticket.department.label}" />
         </t:column>
         <t:column style="cursor: #{cipe.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
             <f:facet name="header">
                 <e:bold value="#{msgs['TICKET.HISTORY.OWNER.HEADER.MANAGER']}" />
             </f:facet>
             <h:panelGroup>
                 <e:text value="#{userFormatter[cipe.ticket.manager]}" />
             </h:panelGroup>
         </t:column>
         <t:column style="cursor: #{cipe.canRead ? 'pointer' : 'default'}" >
             <h:panelGroup style="display: none" rendered="#{cipe.canRead}" >
                 <e:commandButton id="viewTicketButton"
                     action="viewTicket" immediate="true">
                     <t:updateActionListener value="#{cipe.ticket}"
                         property="#{ticketController.updatedTicket}" />
                     <t:updateActionListener value="historyOwnerData"
                         property="#{ticketController.backPage}" />
                 </e:commandButton>
             </h:panelGroup>
         </t:column>         
         <f:facet name="footer">
             <t:htmlTag value="hr" />
         </f:facet>
     </e:dataTable>
     <e:paragraph value="#{msgs['TICKET.HISTORY.OWNER.TEXT.NO_HISTORY']}" rendered="#{empty ticketController.ticketsByOwner}" />
	
							 