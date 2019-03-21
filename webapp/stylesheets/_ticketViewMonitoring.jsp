<%@include file="_include.jsp"%>

<t:htmlTag value="div" styleClass="block">

        <e:paragraph value="#{msgs['TICKET_VIEW.MONITORING.NO_USER']}"
            rendered="#{empty ticketController.monitoringUsers}" />
        <e:text value="#{msgs['TICKET_VIEW.MONITORING.PEOPLE']}" />
        <t:htmlTag value="ul" >
        <t:dataList
            id="monitoringData"
            value="#{ticketController.monitoringUsers}"
            var="user"
            rendered="#{not empty ticketController.monitoringUsers}" >
                <t:htmlTag value="li" >
                <e:text value="#{msgs['TICKET_VIEW.MONITORING.USER']}" rendered="#{!ticketController.ticket.anonymous}" >
                    <f:param value="#{userFormatter[user]}" />
                </e:text>
                <e:text value="#{msgs['TICKET_VIEW.MONITORING.USER']}" rendered="#{ticketController.ticket.anonymous && controlPanelController.manager}" >
                    <f:param value="#{userFormatter[user]}" />
                </e:text>
                <e:text value="#{msgs['TICKET_VIEW.MONITORING.USER']}" rendered="#{ticketController.ticket.anonymous && !controlPanelController.manager}" >
                    <f:param value="#{msgs['USER.ANONYMOUS']}" />
                </e:text>
            </t:htmlTag>
        </t:dataList>
        </t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="form-block" rendered="#{!ticketController.userInMonitoringUsersMandatory}">
    <t:htmlTag value="div" styleClass="form-item">
        	<e:selectBooleanCheckbox value="#{ticketController.userMonitorsTicket}"/>

        	<e:text value="#{msgs['TICKET_VIEW.MONITORING.MONITOR']}"/>
        	<e:commandButton styleClass="button--primary"
        		value="#{msgs['_.BUTTON.UPDATE']}"
        		id="updateButton"
        		action="#{ticketController.refreshTicket}"
        		/>
    </t:htmlTag>
</t:htmlTag>


	
	