<%@include file="_include.jsp"%>
<e:panelGrid columns="2" columnClasses="colRightNowrap,colLeftMaxNowrap" 
	alternateColors="true" cellpadding="5" cellspacing="0" >
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY']} " />
	<e:bold value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY_VALUE']} " title="#{ticketController.labelCategories}">
		<f:param value="#{ticketController.ticket.department.label}" />
		<f:param value="#{ticketController.ticket.category.label}" />
	</e:bold>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.OWNER']} " />
	<h:panelGroup>
		<e:panelGrid columns="2" columnClasses="colLeftNowrap,colLeftMaxNowrap" >
			<h:panelGroup>
				<h:panelGroup rendered="#{ticketController.ownerInfo != null}" >
					<h:panelGroup style="cursor: pointer"  
						onclick="javascript:{showHideElement('viewTicketForm:ownerInfo');showHideElement('viewTicketForm:showOwnerInfo');showHideElement('viewTicketForm:hideOwnerInfo');}" >
						<t:graphicImage value="/media/images/show.png" id="showOwnerInfo" />
						<t:graphicImage value="/media/images/hide.png" id="hideOwnerInfo" />
					</h:panelGroup>
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup > 
				<h:panelGroup
					style="#{ticketController.userCanChangeOwner ? 'cursor: pointer' : ''}" 
					onclick="simulateLinkClick('viewTicketForm:changeOwnerButton');"  >
					<t:graphicImage value="/media/images/user.png"
						alt="#{ticketController.userCanChangeOwner ? msgs['TICKET_VIEW.BUTTON.CHANGE_OWNER'] : ''}" 
						title="#{ticketController.userCanChangeOwner ? msgs['TICKET_VIEW.BUTTON.CHANGE_OWNER'] : ''}" />
					<e:bold value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} " >
						<f:param value="#{userFormatter[ticketController.ticket.owner]}" />
					</e:bold>
					<t:graphicImage value="/media/images/edit.png" rendered="#{ticketController.userCanChangeOwner}" />
				</h:panelGroup>
				<e:commandButton id="changeOwnerButton" style="display: none"
					rendered="#{ticketController.userCanChangeOwner}" 
					value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_OWNER']}"
					action="#{ticketController.changeOwner}" />
			</h:panelGroup>
		</e:panelGrid>
		<h:panelGroup id="ownerInfo" 
			rendered="#{ticketController.ownerInfo != null}" 
			style="display: none" styleClass="userInfo" >
			<e:text 
				escape="false"
				value="#{ticketController.ownerInfo}" />
		</h:panelGroup>
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.MANAGER']} " />
	<h:panelGroup>
		<e:panelGrid columns="2" columnClasses="colLeftNowrap,colLeftMaxNowrap" >
			<h:panelGroup>
				<h:panelGroup rendered="#{ticketController.managerInfo != null}" >
					<h:panelGroup style="cursor: pointer"  
						onclick="javascript:{showHideElement('viewTicketForm:managerInfo');showHideElement('viewTicketForm:showManagerInfo');showHideElement('viewTicketForm:hideManagerInfo');}" >
						<t:graphicImage value="/media/images/show.png" id="showManagerInfo" />
						<t:graphicImage value="/media/images/hide.png" id="hideManagerInfo" />
					</h:panelGroup>
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup 
				style="#{ticketController.userCanChangeManager ? 'cursor: pointer' : ''}" 
				onclick="javascript:{if (#{ticketController.userCanChangeManager}) {showHideElement('viewTicketForm:editTicketManager');}}"  >
				<h:panelGroup rendered="#{ticketController.ticket.manager != null}" >
					<t:graphicImage value="/media/images/manager.png" />
					<e:bold value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} " rendered="#{!ticketController.ticket.anonymous}">
						<f:param value="#{userFormatter[ticketController.ticket.manager]}" />
					</e:bold>											
					<e:bold value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} " rendered="#{ticketController.ticket.anonymous && controlPanelController.manager}">
						<f:param value="#{userFormatter[ticketController.ticket.manager]}" />
					</e:bold>											
					<e:bold value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} " rendered="#{ticketController.ticket.anonymous && !controlPanelController.manager}">
						<f:param value="#{msgs['USER.ANONYMOUS']}" />
					</e:bold>											
				</h:panelGroup>
				<e:italic 
					rendered="#{ticketController.ticket.manager == null}" 
					value=" #{msgs['TICKET_VIEW.PROPERTIES.NO_MANAGER']} " />
				<t:graphicImage value="/media/images/edit.png" 
					rendered="#{ticketController.userCanChangeManager}" />
			</h:panelGroup>
		</e:panelGrid>
		<h:panelGroup id="managerInfo" 
			rendered="#{ticketController.managerInfo != null}" 
			style="display: none" styleClass="userInfo" >
			<e:text 
				escape="false"
				value="#{ticketController.managerInfo}" />
		</h:panelGroup>
		<e:panelGrid id="editTicketManager" 
			columns="1" cellpadding="0" cellspacing="0" columnClasses="colLeft"
			rendered="#{ticketController.userCanChangeManager}" 
			style="display: block" >
			<h:panelGroup
				rendered="#{ticketController.userCanTake}" >
				<h:panelGroup 
					style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:takeButton');" >
					<e:bold value="#{msgs['TICKET_VIEW.BUTTON.TAKE']} " />
					<t:graphicImage value="/media/images/take.png" 
						alt="#{msgs['TICKET_VIEW.BUTTON.TAKE']}" 
						title="#{msgs['TICKET_VIEW.BUTTON.TAKE']}" />
				</h:panelGroup>
				<e:commandButton id="takeButton" style="display: none"
					value="#{msgs['TICKET_VIEW.BUTTON.TAKE']}"
					action="#{ticketController.take}" 
				/>
			</h:panelGroup>
			<h:panelGroup 
				rendered="#{ticketController.userCanTakeAndClose}" >
				<h:panelGroup 
					style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:takeAndCloseButton');" >
					<e:bold value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_CLOSE']} " />
					<t:graphicImage value="/media/images/take-and-close.png" 
						alt="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_CLOSE']}" 
						title="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_CLOSE']}" />
				</h:panelGroup>
				<e:commandButton id="takeAndCloseButton" style="display: none"
					value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_CLOSE']}"
					action="#{ticketController.takeAndClose}" 
				/>
			</h:panelGroup>
			<h:panelGroup 
				rendered="#{ticketController.userCanTakeAndRequestInformation}" >
				<h:panelGroup 
					style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:takeAndRequestInformationButton');" >
					<e:bold value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_REQUEST_INFORMATION']} " />
					<t:graphicImage value="/media/images/take-and-request-information.png" 
						alt="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_REQUEST_INFORMATION']}" 
						title="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_REQUEST_INFORMATION']}" />
				</h:panelGroup>
				<e:commandButton id="takeAndRequestInformationButton" style="display: none"
					value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_REQUEST_INFORMATION']}"
					action="#{ticketController.takeAndRequestInformation}" 
				/>
			</h:panelGroup>
			<h:panelGroup 
				rendered="#{ticketController.userCanFree}" >
				<h:panelGroup 
					style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:freeButton');" >
					<e:bold value="#{msgs['TICKET_VIEW.BUTTON.FREE']} " />
					<t:graphicImage value="/media/images/free.png" 
						alt="#{msgs['TICKET_VIEW.BUTTON.FREE']}" 
						title="#{msgs['TICKET_VIEW.BUTTON.FREE']}" />
				</h:panelGroup>
				<e:commandButton id="freeButton" style="display: none"
					value="#{msgs['TICKET_VIEW.BUTTON.FREE']}"
					action="#{ticketController.free}" 
				/>
			</h:panelGroup>
			<h:panelGroup 
				rendered="#{ticketController.userCanAssign}" >
				<h:panelGroup 
					style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:assignButton');" >
					<e:bold value="#{msgs['TICKET_VIEW.BUTTON.ASSIGN']} " />
					<t:graphicImage value="/media/images/assign.png" 
						alt="#{msgs['TICKET_VIEW.BUTTON.ASSIGN']}" 
						title="#{msgs['TICKET_VIEW.BUTTON.ASSIGN']}" />
				</h:panelGroup>
				<e:commandButton id="assignButton" style="display: none"
					value="#{msgs['TICKET_VIEW.BUTTON.ASSIGN']}"
					action="#{ticketController.assign}" 
				/>
			</h:panelGroup>
		</e:panelGrid>
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.STATUS']}" />
	<h:panelGroup>
		<e:bold 
			value="#{msgs[ticketStatusI18nKeyProvider[ticketController.ticket.status]]} " />
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.RECALL_DATE']}" 
		rendered="#{ticketController.ticket.status == 'POSTPONED' and ticketController.ticket.recallDate != null}" />
	<h:panelGroup
		rendered="#{ticketController.ticket.status == 'POSTPONED' and ticketController.ticket.recallDate != null}" >
		<e:bold value="{0}" >
			<f:param value="#{ticketController.ticket.recallDate}" />
		</e:bold>
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.SEE_ALSO']}" 
		rendered="#{ticketController.userCanViewConnectionTicket or ticketController.userCanViewConnectionArchivedTicket or ticketController.userCanViewConnectionFaq}" />
	<e:panelGrid
		columns="1"
		columnClasses="colLeftNowrap" 
		rendered="#{ticketController.userCanViewConnectionTicket or ticketController.userCanViewConnectionArchivedTicket or ticketController.userCanViewConnectionFaq}" >
		<h:panelGroup 
			rendered="#{ticketController.userCanViewConnectionTicket}" >
			<h:panelGroup 
				style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:viewConnectedTicketButton');" >
				<t:graphicImage value="/media/images/ticket.png" /> 
				<e:bold value=" #{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}" >
					<f:param value="#{ticketController.ticket.connectionTicket.id}" />
				</e:bold>
			</h:panelGroup>
			<e:commandButton id="viewConnectedTicketButton" style="display: none"
				value="#{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}"
				action="viewTicket" >
				<f:param value="#{ticketController.ticket.connectionTicket.id}" />
				<t:updateActionListener 
					value="#{ticketController.ticket.connectionTicket}" 
					property="#{ticketController.ticket}" />
			</e:commandButton>
		</h:panelGroup>
		<h:panelGroup 
			rendered="#{ticketController.userCanViewConnectionArchivedTicket}" >
			<h:panelGroup 
				style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:viewConnectedArchivedTicketButton');" >
				<t:graphicImage value="/media/images/ticket.png" /> 
				<e:bold value=" #{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}" >
					<f:param value="#{ticketController.ticket.connectionArchivedTicket.ticketId}" />
				</e:bold>
			</h:panelGroup>
			<e:commandButton id="viewConnectedArchivedTicketButton" style="display: none"
				value="#{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}"
				action="viewArchivedTicket" >
				<f:param value="#{ticketController.ticket.connectionArchivedTicket.ticketId}" />
				<t:updateActionListener 
					value="#{ticketController.ticket.connectionArchivedTicket}" 
					property="#{archivedTicketController.archivedTicket}" />
			</e:commandButton>
		</h:panelGroup>
		<h:panelGroup 
			rendered="#{ticketController.userCanViewConnectionFaq}" >
			<h:panelGroup 
				style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:viewConnectedFaqButton');" >
                <t:graphicImage value="/media/images/faq#{faqHasChildrenHelper[ticketController.ticket.connectionFaq]?'-container':''}-link.png" />
				<e:bold value=" #{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_FAQ']}" >
					<f:param value="#{ticketController.ticket.connectionFaq.label}" />
				</e:bold>
			</h:panelGroup>
			<e:commandButton id="viewConnectedFaqButton" style="display: none"
				value="#{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_FAQ']}"
				action="#{faqsController.enter}" >
				<f:param value="#{ticketController.ticket.connectionFaq.label}" />
				<t:updateActionListener 
					value="#{ticketController.ticket.connectionFaq}" 
					property="#{faqsController.faq}" />
			</e:commandButton>
		</h:panelGroup>
	</e:panelGrid>
</e:panelGrid>
<t:htmlTag value="hr" />
<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightNowrap">
	<e:subSection value="#{msgs['TICKET_VIEW.PROPERTIES.HEADER']} " />
	<h:panelGroup style="cursor: pointer" onclick="javascript:{showHideElement('viewTicketForm:properties');showHideElement('viewTicketForm:showProperties');showHideElement('viewTicketForm:hideProperties');return false;}" >
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
<e:panelGrid id="properties" style="display: none" columns="3" 
	columnClasses="colRightNowrap,colLeftMaxNowrap,colRightNowrap" alternateColors="true" 
	cellspacing="0" cellpadding="5">
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CREATION_DEPARTMENT']}" 
		rendered="#{ticketController.ticket.creationDepartment != ticketController.ticket.department}" />
	<e:bold rendered="#{ticketController.ticket.creationDepartment != ticketController.ticket.department}"
		value="#{ticketController.ticket.creationDepartment.label}" />
	<h:panelGroup rendered="#{ticketController.ticket.creationDepartment != ticketController.ticket.department}" />
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY']}" />
	<h:panelGroup
		style="#{ticketController.userCanMove ? 'cursor: pointer' : ''}" 
		onclick="simulateLinkClick('viewTicketForm:moveButton');" >
		<e:bold value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY_VALUE']}" title="#{ticketController.labelCategories}" >
			<f:param value="#{ticketController.ticket.department.label}" />
			<f:param value="#{ticketController.ticket.category.label}" />
		</e:bold>
	</h:panelGroup>
	<h:panelGroup>
		<h:panelGroup rendered="#{ticketController.userCanMove}" 
			style="cursor: pointer" 
			onclick="simulateLinkClick('viewTicketForm:moveButton');" >
			<t:graphicImage value="/media/images/move.png" 
				alt="#{msgs['TICKET_VIEW.BUTTON.MOVE']}"
				title="#{msgs['TICKET_VIEW.BUTTON.MOVE']}" />
		</h:panelGroup>
		<e:commandButton id="moveButton" style="display: none"
			value="#{msgs['TICKET_VIEW.BUTTON.MOVE']}"
			action="#{ticketController.move}" 
			rendered="#{ticketController.userCanMove}" />
		<h:panelGroup rendered="#{ticketController.userCanMoveBack}" 
			style="cursor: pointer" 
			onclick="simulateLinkClick('viewTicketForm:giveInformationMoveBackButton');" >
			<t:graphicImage value="/media/images/move-back.png" 
				alt="#{msgs['TICKET_VIEW.BUTTON.MOVE.BACK']}"
				title="#{msgs['TICKET_VIEW.BUTTON.MOVE.BACK']}" />
	</h:panelGroup>
		<e:commandButton id="giveInformationMoveBackButton" style="display: none"
			value="#{msgs['TICKET_VIEW.BUTTON.MOVE']}"
			action="#{ticketController.giveInformationMoveBack}" 
			rendered="#{ticketController.userCanMoveBack}" />
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.SCOPE']}" />
	<h:panelGroup>
		<h:panelGroup
			style="#{ticketController.userCanChangeScope ? 'cursor: pointer' : ''}" 
			onclick="showHideElement('viewTicketForm:editTicketScope');" >
			<e:bold rendered="#{ticketController.ticket.scope == 'DEFAULT'}" 
				value=" #{msgs['TICKET_VIEW.PROPERTIES.SCOPE_VALUE_DEFAULT']} " >
				<f:param value="#{msgs[ticketScopeI18nKeyProvider['DEFAULT']]}" />
				<f:param value="#{msgs[ticketScopeI18nKeyProvider[ticketController.ticket.effectiveScope]]}" />
			</e:bold>
			<e:bold 
				rendered="#{ticketController.ticket.scope != 'DEFAULT'}" 
				value="#{msgs[ticketScopeI18nKeyProvider[ticketController.ticket.scope]]} " />
			<t:graphicImage value="/media/images/public.png" rendered="#{ticketController.ticket.effectiveScope == 'PUBLIC'}" />
			<t:graphicImage value="/media/images/public.png" rendered="#{ticketController.ticket.effectiveScope == 'CAS'}" />
			<t:graphicImage value="/media/images/protected.png" rendered="#{ticketController.ticket.effectiveScope == 'SUBJECT_ONLY'}" />
			<t:graphicImage value="/media/images/private.png" rendered="#{ticketController.ticket.effectiveScope == 'PRIVATE'}" />
		</h:panelGroup>
		<h:panelGroup id="editTicketScope" style="display: none" 
			rendered="#{ticketController.userCanChangeScope}" >
			<e:selectOneMenu id="ticketScope"
				value="#{ticketController.ticketScope}" 
				onchange="javascript:{simulateLinkClick('viewTicketForm:changeTicketScopeButton');}"
				>
				<f:selectItems value="#{ticketController.ticketScopeItems}" />
			</e:selectOneMenu>
			<h:panelGroup rendered="#{ticketController.userCanSetNoAlert}" >
				<e:text value=" " />
				<e:selectBooleanCheckbox 
					value="#{ticketController.scopeNoAlert}" />
				<e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
			</h:panelGroup>
			<e:commandButton style="display: none"
				id="changeTicketScopeButton" 
				value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_SCOPE']}"
				action="#{ticketController.doChangeTicketScope}" 
			/>
		</h:panelGroup >
	</h:panelGroup>
	<h:panelGroup>
		<h:panelGroup
			style="cursor: pointer" 
			onclick="showHideElement('viewTicketForm:editTicketScope');" 
			rendered="#{ticketController.userCanChangeScope}" > 
			<e:bold value=" " /> 
			<t:graphicImage value="/media/images/edit.png" />
		</h:panelGroup>
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.PRIORITY']}" />
	<h:panelGroup>
		<h:panelGroup
			style="#{ticketController.userCanChangePriority ? 'cursor: pointer' : ''}" 
			onclick="showHideElement('viewTicketForm:editTicketPriority');" >
			<h:outputText
				rendered="#{ticketController.ticket.priorityLevel == 0}" 
				value="#{msgs[priorityI18nKeyProvider[0]]} (#{msgs[priorityI18nKeyProvider[ticketController.ticket.effectivePriorityLevel]]}) " 
				styleClass="#{priorityStyleClassProvider[ticketController.ticket.effectivePriorityLevel]}" />
			<h:outputText
				rendered="#{ticketController.ticket.priorityLevel != 0}" 
				value="#{msgs[priorityI18nKeyProvider[ticketController.ticket.priorityLevel]]} " 
				styleClass="#{priorityStyleClassProvider[ticketController.ticket.priorityLevel]}" />
		</h:panelGroup>
		<h:panelGroup id="editTicketPriority" style="display: none" 
			rendered="#{ticketController.userCanChangePriority}" >
			<e:selectOneMenu id="ticketPriority"
				value="#{ticketController.ticketPriority}" 
				onchange="javascript:{simulateLinkClick('viewTicketForm:changeTicketPriorityButton'); return false;}"
				>
				<f:selectItems value="#{ticketController.ticketPriorityItems}" />
			</e:selectOneMenu>
			<h:panelGroup rendered="#{ticketController.userCanSetNoAlert}" >
				<e:text value=" " />
				<e:selectBooleanCheckbox 
					value="#{ticketController.priorityNoAlert}" />
				<e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
			</h:panelGroup>
			<e:commandButton style="display: none"
				id="changeTicketPriorityButton" 
				value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_PRIORITY']}"
				action="#{ticketController.doChangeTicketPriority}" 
			/>
		</h:panelGroup >
	</h:panelGroup>
	<h:panelGroup>
		<h:panelGroup
			style="cursor: pointer" 
			onclick="showHideElement('viewTicketForm:editTicketPriority');" 
			rendered="#{ticketController.userCanChangePriority}" > 
				<t:graphicImage value="/media/images/edit.png" />
		</h:panelGroup>
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.COMPUTER']}" />
	<h:panelGroup>
		<h:panelGroup
			style="#{ticketController.userCanChangeComputer ? 'cursor: pointer' : ''}" 
			onclick="showHideElement('viewTicketForm:editTicketComputer');" >
			<e:italic
				rendered="#{ticketController.ticket.computer == null}" 
				value="#{msgs['TICKET_VIEW.PROPERTIES.NO_COMPUTER']} " />
		</h:panelGroup>
		<h:panelGroup rendered="#{ticketController.ticket.computer != null}" > 
			<e:bold value="#{ticketController.ticket.computer}" />
			<h:panelGroup rendered="#{computerUrlProvider[ticketController.ticket] != null}" > 
				<h:outputFormat value=" <a href=&quot;{0}&quot; target=&quot;_blank&quot; >" escape="false" >
					<f:param value="#{computerUrlProvider[ticketController.ticket]}" />
				</h:outputFormat>
				<t:graphicImage value="/media/images/computer-url.png" border="0" />
				<h:outputFormat value="</a>" escape="false" />
			</h:panelGroup>
		</h:panelGroup>
		<h:panelGroup id="editTicketComputer" style="display: none" 
			rendered="#{ticketController.userCanChangeComputer}" >
			<e:inputText id="ticketComputer" size="30"
				value="#{ticketController.ticketComputer}" />
			<h:panelGroup rendered="#{ticketController.userCanSetNoAlert}" >
				<e:text value=" " />
				<e:selectBooleanCheckbox 
					value="#{ticketController.computerNoAlert}" />
				<e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
			</h:panelGroup>
			<t:htmlTag value="br" />
			<h:panelGroup style="cursor: pointer" 
				onclick="simulateLinkClick('viewTicketForm:changeTicketComputerButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_COMPUTER']} " />
				<t:graphicImage value="/media/images/save.png" />
			</h:panelGroup>
			<e:commandButton style="display: none"
				id="changeTicketComputerButton"
				value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_COMPUTER']}"
				action="#{ticketController.doChangeTicketComputer}" 
			/>
		</h:panelGroup >
	</h:panelGroup>
	<h:panelGroup>
		<h:panelGroup
			style="cursor: pointer" 
			onclick="showHideElement('viewTicketForm:editTicketComputer');" 
			rendered="#{ticketController.userCanChangeComputer}" > 
				<t:graphicImage value="/media/images/edit.png" />
		</h:panelGroup>
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.SPENT_TIME']}" />
	<h:panelGroup>
		<h:panelGroup
			style="#{ticketController.userCanChangeSpentTime ? 'cursor: pointer' : ''}" 
			onclick="showHideElement('viewTicketForm:editTicketSpentTime');" >
			<e:bold value="#{spentTimeI18nFormatter[ticketController.ticket.spentTime]}" 
				rendered="#{ticketController.ticket.spentTime != -1}"/>
			<e:italic value="#{msgs['TICKET_VIEW.PROPERTIES.NO_SPENT_TIME']}" 
				rendered="#{ticketController.ticket.spentTime == -1}"/>
		</h:panelGroup>
		<h:panelGroup id="editTicketSpentTime" style="display: none" 
			rendered="#{ticketController.userCanChangeSpentTime}" >
			<%@include file="_ticketEditSpentTime.jsp"%>
			<t:htmlTag value="br" />
			<h:panelGroup style="cursor: pointer" 
				onclick="simulateLinkClick('viewTicketForm:changeTicketSpentTimeButton');" >
				<e:bold value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_SPENT_TIME']} " />
				<t:graphicImage value="/media/images/save.png" />
			</h:panelGroup>
			<e:commandButton style="display: none"
				id="changeTicketSpentTimeButton" 
				value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_SPENT_TIME']}"
				action="#{ticketController.doChangeTicketSpentTime}" 
			/>
			<h:panelGroup rendered="#{ticketController.userCanSetNoAlert}" >
				<e:text value=" " />
				<e:selectBooleanCheckbox 
					value="#{ticketController.spentTimeNoAlert}" />
				<e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
			</h:panelGroup>
		</h:panelGroup >
	</h:panelGroup> 
	<h:panelGroup>
		<h:panelGroup
			style="#{ticketController.userCanChangeSpentTime ? 'cursor: pointer' : ''}" 
			onclick="showHideElement('viewTicketForm:editTicketSpentTime');" 
			rendered="#{ticketController.userCanChangeSpentTime}" > 
			<t:graphicImage value="/media/images/edit.png" />
		</h:panelGroup>
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.ORIGIN']}" />
	<h:panelGroup>
		<h:panelGroup
			style="#{ticketController.userCanChangeOrigin ? 'cursor: pointer' : ''}" 
			onclick="showHideElement('viewTicketForm:editTicketOrigin');" >
			<e:bold 
				value="#{msgs[originI18nKeyProvider[ticketController.ticket.origin]]} " />
		</h:panelGroup>
		<h:panelGroup id="editTicketOrigin" style="display: none" 
			rendered="#{ticketController.userCanChangeOrigin}" >
			<e:selectOneMenu id="ticketOrigin"
				value="#{ticketController.ticketOrigin}" 
				onchange="javascript:{simulateLinkClick('viewTicketForm:changeTicketOriginButton'); return false;}"
				>
				<f:selectItems value="#{ticketController.originItems}" />
			</e:selectOneMenu>
			<h:panelGroup rendered="#{ticketController.userCanSetNoAlert}" >
				<e:text value=" " />
				<e:selectBooleanCheckbox 
					value="#{ticketController.originNoAlert}" />
				<e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
			</h:panelGroup>
			<e:commandButton style="display: none"
				id="changeTicketOriginButton" 
				value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_ORIGIN']}"
				action="#{ticketController.doChangeTicketOrigin}" 
			/>
		</h:panelGroup >
	</h:panelGroup>
	<h:panelGroup>
		<h:panelGroup
			style="cursor: pointer" 
			onclick="showHideElement('viewTicketForm:editTicketOrigin');" 
			rendered="#{ticketController.userCanChangeOrigin}" > 
			<t:graphicImage value="/media/images/edit.png" />
		</h:panelGroup>
	</h:panelGroup>
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CREATION_DATE']}" />
	<e:bold value="{0}" >
		<f:param value="#{ticketController.ticket.creationDate}"/>
	</e:bold>
	<h:panelGroup />
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.LAST_ACTION_DATE']}" />
	<e:bold value="{0}" >
		<f:param value="#{ticketController.ticket.lastActionDate}"/>
	</e:bold>
	<h:panelGroup />
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CHARGE_TIME']}" 
		rendered="#{ticketController.ticket.chargeTime != null}"/>
	<h:panelGroup rendered="#{ticketController.ticket.chargeTime != null}" >
		<e:bold value="#{elapsedTimeI18nFormatter[ticketController.ticket.chargeTime]}" />
	</h:panelGroup>
	<h:panelGroup rendered="#{ticketController.ticket.chargeTime != null}" />
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CLOSURE_TIME']}" 
		rendered="#{ticketController.ticket.closureTime != null}"/>
	<h:panelGroup rendered="#{ticketController.ticket.closureTime != null}" >
		<e:bold value="#{elapsedTimeI18nFormatter[ticketController.ticket.closureTime]}" />
	</h:panelGroup>
	<h:panelGroup rendered="#{ticketController.ticket.closureTime != null}" />
	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.PERM_LINKS']}" />
	<h:panelGroup >
		<e:text value=" #{msgs['COMMON.PERM_LINKS.APPLICATION']}" escape="false" >
			<f:param value="#{ticketController.applicationPermLink}" />
		</e:text>
		<e:text value=" #{msgs['COMMON.PERM_LINKS.CAS']}" escape="false" >
			<f:param value="#{ticketController.casPermLink}" />
		</e:text>
		<e:text value=" #{msgs['COMMON.PERM_LINKS.SHIBBOLETH']}" escape="false" >
			<f:param value="#{ticketController.shibbolethPermLink}" />
		</e:text>
	</h:panelGroup>
	<h:panelGroup />
</e:panelGrid>

