<%@include file="_include.jsp"%>

<h:panelGroup >
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'ID'}"
		value="#{cpe.ticket.id} " />
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'DEPARTMENT'}" 
		value="#{cpe.ticket.department.label} " />
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DEPARTMENT'}"
		value="#{cpe.ticket.creationDepartment.label} " />
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CATEGORY'}"
		value="#{cpe.ticket.category.label} " />
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'LABEL'}"
		value="#{controlPanelSubjectTruncator[cpe.ticket.label]} " />
	<h:outputText 
		styleClass="ticket-status #{! cpe.ticket.opened ? 'ticket-closed' : 'ticket-open'} #{cpe.ticket.postponed ? 'ticket-postponed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'STATUS'}"
		value="#{msgs[ticketStatusI18nKeyProvider[cpe.ticket.status]]} " />
	<h:outputText 
		styleClass="ticket-priority #{! cpe.ticket.opened ? 'ticket-closed' : 'ticket-open'} #{priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel]}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; cursor:default;"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'PRIORITY'}"
		value="#{msgs[priorityI18nKeyProvider[cpe.ticket.effectivePriorityLevel]]} " />
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE_TIME'}"
		value="#{cpe.ticket.creationDate}" >
		<f:convertDateTime timeZone="#{sessionController.timezone}" type="both" dateStyle="short" locale="#{sessionController.locale}" />
	</h:outputText>
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE'}"
		value="#{cpe.ticket.creationDate}" >
		<f:convertDateTime timeZone="#{sessionController.timezone}" type="date" dateStyle="short" locale="#{sessionController.locale}"/>
	</h:outputText>
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE_TIME'}"
		value="#{cpe.ticket.lastActionDate}" >
		<f:convertDateTime timeZone="#{sessionController.timezone}" type="both" dateStyle="short" locale="#{sessionController.locale}"/>
	</h:outputText>
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; "
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE'}"
		value="#{cpe.ticket.lastActionDate}" >
		<f:convertDateTime timeZone="#{sessionController.timezone}" type="date" dateStyle="short" locale="#{sessionController.locale}"/>
	</h:outputText>
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; "
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'OWNER'}"
		value="#{userFormatter[cpe.ticket.owner]} " />
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'MANAGER' && !cpe.ticket.anonymous}"
		value="#{userFormatter[cpe.ticket.manager]} " />
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'MANAGER' && cpe.ticket.anonymous && controlPanelController.manager}"
		value="#{userFormatter[cpe.ticket.manager]} " />
	<h:outputText 
		styleClass="#{! cpe.ticket.opened ? 'ticket-closed' : ''} #{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'};"
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'MANAGER' && cpe.ticket.anonymous && !controlPanelController.manager}"
		value="#{msgs['USER.ANONYMOUS']}" />
</h:panelGroup>
