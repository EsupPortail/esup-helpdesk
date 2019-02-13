<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="welcome"
	locale="#{sessionController.locale}">
	<%@include file="_navigation.jsp"%>
	
	<h:panelGroup rendered="#{welcomeController.currentUser != null}" >
	 	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="welcomeForm" >
			<e:section value="#{msgs['WELCOME.TITLE']}" />
			<e:messages />
			<e:paragraph value="#{msgs['WELCOME.TEXT.WELCOME']}" />
			<e:paragraph value="#{msgs['WELCOME.TEXT.SHORTCUTS']}" />
	 		<e:panelGrid columns="2" cellpadding="5" cellspacing="0" columnClasses="colRight,colLeft" >
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('welcomeForm:addTicketButton');" >
					<t:graphicImage value="/media/images/ticket.png" />
					<t:graphicImage value="/media/images/add.png" />
				</h:panelGroup>
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('welcomeForm:addTicketButton');" >
						<e:bold value=" #{msgs['WELCOME.BUTTON.ADD_TICKET']}" />
					</h:panelGroup>
					<e:commandButton id="addTicketButton" action="#{ticketController.add}" style="display: none"
						value="#{msgs['WELCOME.BUTTON.ADD_TICKET']}" immediate="true" />
				</h:panelGroup>
				<h:panelGroup rendered="#{welcomeController.showFaqLink}" 
					style="cursor: pointer" onclick="simulateLinkClick('welcomeForm:faqsButton');" >
					<t:graphicImage value="/media/images/faq-container.png" />
				</h:panelGroup>
				<h:panelGroup rendered="#{welcomeController.showFaqLink}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('welcomeForm:faqsButton');" >
						<e:bold value=" #{msgs['WELCOME.BUTTON.FAQS']}" />
					</h:panelGroup>
					<e:commandButton id="faqsButton" action="#{faqsController.enter}" style="display: none"
						value="#{msgs['WELCOME.BUTTON.FAQS']}" immediate="true" />
				</h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('welcomeForm:controlPanelButton');" >
					<t:graphicImage value="/media/images/control-panel.png" />
				</h:panelGroup>
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('welcomeForm:controlPanelButton');" >
						<e:bold value=" #{msgs['WELCOME.BUTTON.CONTROL_PANEL']}" />
					</h:panelGroup>
					<e:commandButton id="controlPanelButton" action="#{controlPanelController.enter}" style="display: none"
						value="#{msgs['WELCOME.BUTTON.CONTROL_PANEL']}" immediate="true" />
				</h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('welcomeForm:searchButton');" >
					<t:graphicImage value="/media/images/search.png" />
				</h:panelGroup>
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('welcomeForm:searchButton');" >
						<e:bold value=" #{msgs['WELCOME.BUTTON.SEARCH']}" />
					</h:panelGroup>
					<e:commandButton id="searchButton" action="#{searchController.enter}" style="display: none"
						value="#{msgs['WELCOME.BUTTON.SEARCH']}" immediate="true" />
				</h:panelGroup>
	 		</e:panelGrid>
			<e:paragraph value="#{msgs['WELCOME.TEXT.BOOKMARKS']}" />
		</e:form>
	</h:panelGroup>

	<h:panelGroup rendered="#{welcomeController.currentUser == null}">
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
	
	<t:aliasBean alias="#{controller}" value="#{welcomeController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>

</e:page>
