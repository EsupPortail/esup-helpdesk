<%@include file="_include.jsp"%>
<e:panelGrid columns="2" columnClasses="colRightNowrap,colLeftMaxNowrap" width="100%" 
	alternateColors="true" cellpadding="5" cellspacing="0" >
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.OWNER']} " />
	<h:panelGroup>
		<t:graphicImage value="/media/images/user.png" />
		<e:bold value=" #{userFormatter[archivedTicketController.archivedTicket.owner]} " />
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.MANAGER']} " />
	<h:panelGroup>
		<h:panelGroup rendered="#{archivedTicketController.archivedTicket.manager != null}" >
			<t:graphicImage value="/media/images/manager.png" />
			<e:bold value=" #{userFormatter[archivedTicketController.archivedTicket.manager]} " />
		</h:panelGroup>
		<e:italic 
			rendered="#{archivedTicketController.archivedTicket.manager == null}" 
			value=" #{msgs['TICKET_VIEW.PROPERTIES.NO_MANAGER']} " />
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.STATUS']}" />
	<h:panelGroup>
		<e:bold 
			value="#{msgs['DOMAIN.TICKET_STATUS.ARCHIVED']} " />
		<h:panelGroup 
			rendered="#{archivedTicketController.userCanViewConnectionTicket}" >
			<t:htmlTag value="br" />
			<h:panelGroup 
				style="cursor: pointer" onclick="simulateLinkClick('viewArchivedTicketForm:viewConnectedTicketButton');" >
				<t:graphicImage value="/media/images/ticket.png" /> 
				<e:bold value=" #{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}" >
					<f:param value="#{archivedTicketController.archivedTicket.connectionTicket.id}" />
				</e:bold>
			</h:panelGroup>
			<e:commandButton id="viewConnectedTicketButton" style="display: none"
				value="#{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}"
				action="viewTicket" >
				<f:param value="#{archivedTicketController.archivedTicket.connectionTicket.id}" />
				<t:updateActionListener 
					value="#{archivedTicketController.archivedTicket.connectionTicket}" 
					property="#{ticketController.ticket}" />
			</e:commandButton>
		</h:panelGroup>
		<h:panelGroup 
			rendered="#{archivedTicketController.userCanViewConnectionArchivedTicket}" >
			<t:htmlTag value="br" />
			<h:panelGroup 
				style="cursor: pointer" onclick="simulateLinkClick('viewArchivedTicketForm:viewConnectedArchivedTicketButton');" >
				<t:graphicImage value="/media/images/ticket.png" /> 
				<e:bold value=" #{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}" >
					<f:param value="#{archivedTicketController.archivedTicket.connectionArchivedTicket.ticketId}" />
				</e:bold>
			</h:panelGroup>
			<e:commandButton id="viewConnectedArchivedTicketButton" style="display: none"
				value="#{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}"
				action="viewArchivedTicket" >
				<f:param value="#{archivedTicketController.archivedTicket.connectionArchivedTicket.ticketId}" />
				<t:updateActionListener 
					value="#{archivedTicketController.archivedTicket.connectionArchivedTicket}" 
					property="#{archivedTicketController.archivedTicket}" />
			</e:commandButton>
		</h:panelGroup>
		<h:panelGroup 
			rendered="#{archivedTicketController.userCanViewConnectionFaq}" >
			<t:htmlTag value="br" />
			<h:panelGroup 
				style="cursor: pointer" onclick="simulateLinkClick('viewArchivedTicketForm:viewConnectedFaqButton');" >
                <t:graphicImage value="/media/images/faq#{faqHasChildrenHelper[archivedTicketController.archivedTicket.connectionFaq]?'-container':''}-link.png" />
				<e:bold value=" #{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_FAQ']}" >
					<f:param value="#{archivedTicketController.archivedTicket.connectionFaq.label}" />
				</e:bold>
			</h:panelGroup>
			<e:commandButton id="viewConnectedFaqButton" style="display: none"
				value="#{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_FAQ']}"
				action="#{faqsController.enter}" >
				<f:param value="#{archivedTicketController.archivedTicket.connectionFaq.label}" />
				<t:updateActionListener 
					value="#{archivedTicketController.archivedTicket.connectionFaq}" 
					property="#{faqsController.faq}" />
			</e:commandButton>
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<t:htmlTag value="hr" />
<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightNowrap">
	<e:subSection value="#{msgs['TICKET_VIEW.PROPERTIES.HEADER']} " />
	<h:panelGroup style="cursor: pointer" onclick="javascript:{showHideElement('viewArchivedTicketForm:properties');showHideElement('viewArchivedTicketForm:showProperties');showHideElement('viewArchivedTicketForm:hideProperties');return false;}" >
		<h:panelGroup id="showProperties" >
			<t:graphicImage value="/media/images/show.png" 
				alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
		</h:panelGroup>
		<h:panelGroup id="hideProperties" style="display: none" >
			<t:graphicImage value="/media/images/hide.png"
				alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<e:panelGrid id="properties" style="display: none" columns="2" width="100%"
	columnClasses="colRightNowrap,colLeftMaxNowrap" alternateColors="true" 
	cellspacing="0" cellpadding="5">
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CREATION_DEPARTMENT']}" 
		rendered="#{archivedTicketController.archivedTicket.department != archivedTicketController.archivedTicket.department}" />
	<e:bold rendered="#{archivedTicketController.archivedTicket.department != archivedTicketController.archivedTicket.department}"
		value="#{archivedTicketController.archivedTicket.department}" />
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY']}" />
	<e:bold value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY_VALUE']} " >
		<f:param value="#{archivedTicketController.archivedTicket.department.label}" />
		<f:param value="#{archivedTicketController.archivedTicket.categoryLabel}" />
	</e:bold>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.SCOPE']}" />
	<h:panelGroup>
		<e:bold 
			value="#{msgs[ticketScopeI18nKeyProvider[archivedTicketController.archivedTicket.effectiveScope]]} " />
		<t:graphicImage value="/media/images/public.png" rendered="#{archivedTicketController.archivedTicket.effectiveScope == 'PUBLIC'}" />
		<t:graphicImage value="/media/images/public.png" rendered="#{archivedTicketController.archivedTicket.effectiveScope == 'CAS'}" />
		<t:graphicImage value="/media/images/protected.png" rendered="#{archivedTicketController.archivedTicket.effectiveScope == 'SUBJECT_ONLY'}" />
		<t:graphicImage value="/media/images/private.png" rendered="#{archivedTicketController.archivedTicket.effectiveScope == 'PRIVATE'}" />
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.PRIORITY']}" />
	<h:outputText
		value="#{msgs[priorityI18nKeyProvider[archivedTicketController.archivedTicket.priorityLevel]]}" 
		styleClass="#{priorityStyleClassProvider[archivedTicketController.archivedTicket.priorityLevel]}" />
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.COMPUTER']}" />
	<h:panelGroup>
		<e:bold 
			rendered="#{archivedTicketController.archivedTicket.computer != null}" 
			value="#{archivedTicketController.archivedTicket.computer} " />
		<e:italic
			rendered="#{archivedTicketController.archivedTicket.computer == null}" 
			value="#{msgs['TICKET_VIEW.PROPERTIES.NO_COMPUTER']} " />
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.SPENT_TIME']}" />
	<h:panelGroup>
		<e:bold value="#{spentTimeI18nFormatter[archivedTicketController.archivedTicket.spentTime]}" 
			rendered="#{archivedTicketController.archivedTicket.spentTime != -1}"/>
			<e:italic value="#{msgs['TICKET_VIEW.PROPERTIES.NO_SPENT_TIME']}" 
			rendered="#{archivedTicketController.archivedTicket.spentTime == -1}"/>
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.ORIGIN']}" />
	<h:panelGroup>
		<e:bold 
			value="#{msgs[originI18nKeyProvider[archivedTicketController.archivedTicket.origin]]} " />
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CREATION_DATE']}" />
	<e:bold value="{0}" >
		<f:param value="#{archivedTicketController.archivedTicket.creationDate}"/>
	</e:bold>
	<e:text value="#{msgs['ARCHIVED_TICKET_VIEW.PROPERTIES.ARCHIVING_DATE']}" />
	<e:bold value="{0}" >
		<f:param value="#{archivedTicketController.archivedTicket.archivingDate}"/>
	</e:bold>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CHARGE_TIME']}" 
		rendered="#{archivedTicketController.archivedTicket.chargeTime != null}"/>
	<h:panelGroup rendered="#{archivedTicketController.archivedTicket.chargeTime != null}" >
		<e:bold value="#{elapsedTimeI18nFormatter[archivedTicketController.archivedTicket.chargeTime]}" />
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CLOSURE_TIME']}" 
		rendered="#{archivedTicketController.archivedTicket.closureTime != null}"/>
	<h:panelGroup rendered="#{archivedTicketController.archivedTicket.closureTime != null}" >
		<e:bold value="#{elapsedTimeI18nFormatter[archivedTicketController.archivedTicket.closureTime]}" />
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.PERM_LINKS']}" />
	<h:panelGroup >
		<e:text value=" #{msgs['COMMON.PERM_LINKS.APPLICATION']}" escape="false" >
			<f:param value="#{archivedTicketController.applicationPermLink}" />
		</e:text>
		<e:text value=" #{msgs['COMMON.PERM_LINKS.CAS']}" escape="false" >
			<f:param value="#{archivedTicketController.casPermLink}" />
		</e:text>
		<e:text value=" #{msgs['COMMON.PERM_LINKS.SHIBBOLETH']}" escape="false" >
			<f:param value="#{archivedTicketController.shibbolethPermLink}" />
		</e:text>
	</h:panelGroup>
</e:panelGrid>
