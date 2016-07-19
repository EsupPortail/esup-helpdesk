<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="administrators" locale="#{sessionController.locale}"
	authorized="#{administratorsController.currentUserCanAddAdmin}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="administratorAddForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['ADMINISTRATOR_ADD.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('administratorAddForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton id="cancelButton" action="cancel"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:panelGrid columns="2" >
			<e:outputLabel for="ldapUid"
				value="#{domainService.useLdap ? msgs['ADMINISTRATOR_ADD.TEXT.PROMPT_LDAP'] : msgs['ADMINISTRATOR_ADD.TEXT.PROMPT_NO_LDAP']}" />
			<h:panelGroup>
				<e:inputText id="ldapUid" value="#{administratorsController.ldapUid}" 
					onkeypress="if (event.keyCode == 13) { simulateLinkClick('administratorAddForm:addButton'); return false;}" />
				<e:message for="ldapUid" />
				<h:panelGroup rendered="#{domainService.useLdap}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('administratorAddForm:ldapSearchButton');" >
						<e:bold value=" #{msgs['_.BUTTON.LDAP']} " />
						<t:graphicImage value="/media/images/search.png"
							alt="#{msgs['_.BUTTON.LDAP']}" 
							title="#{msgs['_.BUTTON.LDAP']}" />
					</h:panelGroup>
					<e:commandButton style="display:none"
						id="ldapSearchButton" action="#{ldapSearchController.firstSearch}"
						value="#{msgs['_.BUTTON.LDAP']}" >
						<t:updateActionListener value="#{administratorsController}"
							property="#{ldapSearchController.caller}" />
						<t:updateActionListener value="userSelectedToAdministratorAdd"
							property="#{ldapSearchController.successResult}" />
						<t:updateActionListener value="cancelToAdministratorAdd"
							property="#{ldapSearchController.cancelResult}" />
					</e:commandButton>
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('administratorAddForm:addButton');" >
					<e:bold value=" #{msgs['ADMINISTRATOR_ADD.BUTTON.ADD_ADMIN']} " />
					<t:graphicImage value="/media/images/add.png"
						alt="#{msgs['ADMINISTRATOR_ADD.BUTTON.ADD_ADMIN']}" 
						title="#{msgs['ADMINISTRATOR_ADD.BUTTON.ADD_ADMIN']}" />
				</h:panelGroup>
				<e:commandButton id="addButton" action="#{administratorsController.addAdmin}"
					value="#{msgs['ADMINISTRATOR_ADD.BUTTON.ADD_ADMIN']}" />
			</h:panelGroup>		
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		focusElement("administratorAddForm:ldapUid");
		hideButton("administratorAddForm:cancelButton");
		hideButton("administratorAddForm:addButton");
	</script>
</e:page>
