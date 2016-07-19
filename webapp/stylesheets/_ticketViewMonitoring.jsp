<%@include file="_include.jsp"%>
<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightNowrap">
	<e:subSection value="#{msgs['TICKET_VIEW.MONITORING.HEADER']}" >
		<f:param value="#{ticketController.monitoringUsersNumber}" />
	</e:subSection>
	<h:panelGroup style="cursor: pointer" onclick="javascript:{showHideElement('viewTicketForm:monitoringUsers');showHideElement('viewTicketForm:showMonitoringUsers');showHideElement('viewTicketForm:hideMonitoringUsers');return false;}" >
		<h:panelGroup id="showMonitoringUsers" >
			<t:graphicImage value="/media/images/show.png" 
				alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
		</h:panelGroup>
		<h:panelGroup id="hideMonitoringUsers" style="display: none" >
			<t:graphicImage value="/media/images/hide.png"
				alt="#{msgs['TICKET_VIEW.TEXT.HIDE']} " />
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<h:panelGroup id="monitoringUsers" style="display: none" >
	<e:selectBooleanCheckbox 
		value="#{ticketController.userMonitorsTicket}" 
		onchange="javascript:{simulateLinkClick('viewTicketForm:updateButton');}" >
	</e:selectBooleanCheckbox>
	<e:text value="#{msgs['TICKET_VIEW.MONITORING.MONITOR']}" />
	<e:commandButton style="display: none"
		value="#{msgs['_.BUTTON.UPDATE']}" 
		id="updateButton"
		action="#{ticketController.refreshTicket}"
		/>
	<e:paragraph value="#{msgs['TICKET_VIEW.MONITORING.NO_USER']}"
		rendered="#{empty ticketController.monitoringUsers}" />
	<e:dataTable width="100%" columnClasses="colLeftNowrap" 
		id="monitoringData" rowIndexVar="variable" 
		value="#{ticketController.monitoringUsers}"
		var="user" 
		rendered="#{not empty ticketController.monitoringUsers}" >
		<f:facet name="header">
			<t:htmlTag value="hr" />
		</f:facet>
		<f:facet name="footer">
			<t:htmlTag value="hr" />
		</f:facet>
		<t:column>
			<e:text value="#{msgs['TICKET_VIEW.MONITORING.USER']}" >
				<f:param value="#{userFormatter[user]}" />
			</e:text>
		</t:column>
	</e:dataTable>
</h:panelGroup>
	
	