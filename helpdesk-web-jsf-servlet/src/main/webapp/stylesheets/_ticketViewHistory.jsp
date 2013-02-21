<%@include file="_include.jsp"%>

<e:subSection value="#{msgs['TICKET_VIEW.HISTORY.HEADER']}" />
<e:dataTable columnClasses="colCenter,colLeftMax" id="actionData" width="100%" 
	value="#{ticketController.historyEntries}" rowIndexVar="variable" 
	var="he" border="0" cellspacing="0" cellpadding="0">
	<t:column >
		<t:graphicImage value="/media/images/public.png" style="#{he.canChangeScope ? 'cursor: pointer' : ''}" rendered="#{he.action.scope == 'DEFAULT'}" onclick="javascript:{showHideElement('viewTicketForm:actionData:'+#{variable}+':editScope');}" />
		<t:graphicImage value="/media/images/invited.png" style="#{he.canChangeScope ? 'cursor: pointer' : ''}" rendered="#{he.action.scope == 'INVITED'}" onclick="javascript:{showHideElement('viewTicketForm:actionData:'+#{variable}+':editScope');}" />
		<t:graphicImage value="/media/images/protected.png" style="#{he.canChangeScope ? 'cursor: pointer' : ''}" rendered="#{he.action.scope == 'OWNER'}" onclick="javascript:{showHideElement('viewTicketForm:actionData:'+#{variable}+':editScope');}" />
		<t:graphicImage value="/media/images/private.png" style="#{he.canChangeScope ? 'cursor: pointer' : ''}" rendered="#{he.action.scope == 'MANAGER'}" onclick="javascript:{showHideElement('viewTicketForm:actionData:'+#{variable}+':editScope');}" />
	</t:column>
	<t:column >
		<h:panelGroup id="editScope" rendered="#{he.canChangeScope}" style="display: none" >
			<e:selectOneMenu id="actionScope"
				value="#{he.newScope}" 
				onchange="javascript:{simulateLinkClick('viewTicketForm:actionData:'+#{variable}+':changeActionScopeButton');return false;}"
				>
				<f:selectItems value="#{ticketController.actionScopeItems}" />
			</e:selectOneMenu>
			<e:commandButton 
				id="changeActionScopeButton"
				value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_SCOPE']}"
				action="#{ticketController.doUpdateActionScope}" 
				style="display: none" >
				<t:updateActionListener value="#{he.action}" property="#{ticketController.actionToUpdate}" />
				<t:updateActionListener value="#{he.newScope}" property="#{ticketController.actionScopeToSet}" />
			</e:commandButton>
			<t:htmlTag value="br" />
		</h:panelGroup>
		<e:bold value="#{actionI18nTitleProvider[he.action]}" />
		<h:panelGroup rendered="#{he.action.user == null and he.canView and he.action.message != null}" >
			<e:bold value=" " />
			<t:graphicImage value="/media/images/view.png" style="cursor: pointer" 
				onclick="javascript:{showHideElement('viewTicketForm:actionData:'+#{variable}+':message');}" />
		</h:panelGroup>
		<h:panelGroup rendered="#{not empty he.alerts}" >
			<e:bold value=" " />
			<t:graphicImage value="/media/images/alert.png" 
				style="cursor: pointer" 
				rendered="#{not ticketController.showAlerts and not empty he.alerts}" 
				onclick="javascript:{toggleShowAlerts();}" />
		</h:panelGroup>
		<h:panelGroup rendered="#{ticketController.showAlerts and not empty he.alerts}" >
			<t:htmlTag value="br" />
			<t:graphicImage value="/media/images/alert.png" style="cursor: pointer" 
				onclick="javascript:{toggleShowAlerts();}" />
			<e:italic value=" #{msgs['TICKET_VIEW.HISTORY.ALERTS_PREFIX']}" />
			<t:dataList value="#{he.alerts}" var="alert" rowIndexVar="index">
				<e:italic value=" " rendered="#{index == 0}" />
				<e:italic value="#{msgs['TICKET_VIEW.HISTORY.ALERTS_SEPARATOR']}" rendered="#{index != 0}" />
				<e:italic rendered="#{alert.user != null}" value=" #{userFormatter[alert.user]}" />
				<e:italic rendered="#{alert.user == null}" value=" #{alert.email}" />
			</t:dataList>
			<e:italic value="#{msgs['TICKET_VIEW.HISTORY.ALERTS_SUFFIX']}" />
		</h:panelGroup>
		<h:panelGroup 
			id="message" rendered="#{he.action.message != null}" 
			style="display: #{he.action.user == null and he.canView ? 'none' : 'block'}" >
			<t:div
				styleClass="#{he.styleClass}" >
				<e:text 
					escape="false"
					rendered="#{he.canView}"
					value="#{he.action.message}" 
					styleClass="" />
			</t:div>
		</h:panelGroup>
	</t:column>
</e:dataTable>				
