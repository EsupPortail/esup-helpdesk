<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="preferences" locale="#{sessionController.locale}" >
	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{not preferencesController.pageAuthorized}" >
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
 	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="preferencesForm" rendered="#{preferencesController.pageAuthorized}" >
		<e:section value="#{msgs['PREFERENCES.TITLE']}" />
		<e:messages />
		<e:panelGrid columnClasses="colLeft,colLeft" columns="2" width="100%" >
			<h:panelGroup>
				<e:subSection value="#{msgs['PREFERENCES.HEADER.LANGUAGE']}"/>
				<h:panelGroup>
					<e:selectOneMenu id="locale" onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
						value="#{preferencesController.locale}" converter="#{localeConverter}" >
						<f:selectItems value="#{preferencesController.localeItems}" />
					</e:selectOneMenu>
				</h:panelGroup>
				<e:subSection value="#{msgs['PREFERENCES.HEADER.IDENTITY']}"/>
				<e:panelGrid columns="2" columnClasses="colLeft,colLeft" >
                    <e:outputLabel for="id" value="#{msgs['PREFERENCES.TEXT.ID']}" />
                    <e:text id="id" value="#{preferencesController.currentUser.realId}" />
                    <e:outputLabel for="auth" value="#{msgs['PREFERENCES.TEXT.AUTH']}" />
					<e:text id="auth" value="#{msgs[authTypeI18nKeyProvider[preferencesController.currentUser.authType]]}" />
					<e:outputLabel for="displayName"
                        value="#{msgs['PREFERENCES.TEXT.DISPLAY_NAME']}" />
					<h:panelGroup>
						<e:text 
							rendered="#{not sessionController.applicationUser}" 
							value="#{preferencesController.currentUser.displayName}" />
						<h:panelGroup 
							rendered="#{sessionController.applicationUser}" >
							<e:inputText id="displayName" value="#{preferencesController.currentUser.displayName}" />
							<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('preferencesForm:updateUserButton');" >
								<e:bold value=" #{msgs['_.BUTTON.UPDATE']} " />
								<t:graphicImage value="/media/images/save.png"
									alt="#{msgs['_.BUTTON.UPDATE']}" 
									title="#{msgs['_.BUTTON.UPDATE']}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGroup>
				</e:panelGrid>
				<h:panelGroup rendered="#{sessionController.applicationUser}">
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('preferencesForm:changePasswordButton');" >
						<e:bold value="#{msgs['PREFERENCES.BUTTON.CHANGE_PASSWORD']} " />
						<t:graphicImage value="/media/images/password.png"
							alt="#{msgs['PREFERENCES.BUTTON.CHANGE_PASSWORD']}" 
							title="#{msgs['PREFERENCES.BUTTON.CHANGE_PASSWORD']}" />
					</h:panelGroup>
					<e:commandButton 
						id="changePasswordButton" style="display: none" 
						action="#{preferencesController.gotoChangePassword}"
						value="#{msgs['PREFERENCES.BUTTON.CHANGE_PASSWORD']}" immediate="true" />
				</h:panelGroup>
				<e:subSection value="#{msgs['PREFERENCES.HEADER.BROWSER']}"/>
				<e:paragraph value="#{preferencesController.userAgent}"/>
			</h:panelGroup>
			<h:panelGroup>
				<e:subSection value="#{msgs['PREFERENCES.HEADER.APPLICATION_BEHAVIOUR']}" />
				<e:panelGrid columns="1">
					<h:panelGroup>
						<e:outputLabel for="startPage" value="#{msgs['PREFERENCES.TEXT.START_PAGE']} " />
						<e:selectOneMenu id="startPage" onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
							value="#{preferencesController.currentUser.startPage}" >
							<f:selectItems value="#{preferencesController.startPageItems}" />
						</e:selectOneMenu>
					</h:panelGroup>
					<h:panelGroup>
						<e:outputLabel for="pageTransition" value="#{msgs['PREFERENCES.TEXT.PAGE_TRANSITION']} " />
						<e:selectOneMenu id="pageTransition" onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
							value="#{preferencesController.currentUser.pageTransition}" >
							<f:selectItems value="#{preferencesController.pageTransitionItems}" />
						</e:selectOneMenu>
					</h:panelGroup>
					<h:panelGroup>
						<e:selectBooleanCheckbox 
							id="showAddTicketHelp" 
							onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
							value="#{preferencesController.currentUser.showAddTicketHelp}" />
						<e:outputLabel for="showAddTicketHelp" value=" #{msgs['PREFERENCES.TEXT.SHOW_ADD_TICKET_HELP']}" />
					</h:panelGroup>
					<h:panelGroup>
						<e:selectBooleanCheckbox 
							id="showPopupOnClosure" 
							onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
							value="#{preferencesController.currentUser.showPopupOnClosure}" />
						<e:outputLabel for="showPopupOnClosure" value=" #{msgs['PREFERENCES.TEXT.SHOW_POPUP_ON_CLOSURE']}" />
					</h:panelGroup>
				</e:panelGrid>
				<e:commandButton style="display: none" value="#{msgs['_.BUTTON.CHANGE']}" 
					id="updateUserButton" action="#{preferencesController.updateUser}" />
				<e:subSection value="#{msgs['PREFERENCES.HEADER.MONITORING']}" />
				<e:panelGrid columns="1">
					<h:panelGroup>
						<e:selectBooleanCheckbox id="ownerMonitoring" onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
							value="#{preferencesController.currentUser.ownerMonitoring}" />
						<e:outputLabel for="ownerMonitoring" value=" #{msgs['PREFERENCES.TEXT.MONITORING.OWNER']}" />
					</h:panelGroup>
					<h:panelGroup>
						<e:selectBooleanCheckbox id="invitedMonitoring" onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
							value="#{preferencesController.currentUser.invitedMonitoring}" />
						<e:outputLabel for="invitedMonitoring" value=" #{msgs['PREFERENCES.TEXT.MONITORING.INVITED']}" />
					</h:panelGroup>
					<h:panelGroup>
						<e:selectBooleanCheckbox id="bookmarkMonitoring" onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
							value="#{preferencesController.currentUser.bookmarkMonitoring}" />
						<e:outputLabel for="bookmarkMonitoring" value=" #{msgs['PREFERENCES.TEXT.MONITORING.BOOKMARK']}" />
					</h:panelGroup>
					<h:panelGroup>
						<e:selectBooleanCheckbox id="expirationMonitoring" onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
							value="#{preferencesController.currentUser.expirationMonitoring}" />
						<e:outputLabel for="expirationMonitoring" value=" #{msgs['PREFERENCES.TEXT.MONITORING.EXPIRATION']}" />
					</h:panelGroup>
				</e:panelGrid>
				<h:panelGroup
					rendered="#{preferencesController.userManagerOrAdmin}" > 
					<e:subSection value="#{msgs['PREFERENCES.HEADER.MANAGER']}" />
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('preferencesForm:managerPreferencesButton');" >
						<e:bold value="#{msgs['PREFERENCES.BUTTON.MANAGER']} " />
						<t:graphicImage value="/media/images/manager.png"
							alt="#{msgs['PREFERENCES.BUTTON.MANAGER']}" 
							title="#{msgs['PREFERENCES.BUTTON.MANAGER']}" />
					</h:panelGroup>
					<e:commandButton style="display: none" id="managerPreferencesButton" action="managerPreferences"
						value="#{msgs['PREFERENCES.BUTTON.MANAGER']}" immediate="true" />
				</h:panelGroup>
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{preferencesController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
