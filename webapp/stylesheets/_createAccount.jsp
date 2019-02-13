<%@include file="_include.jsp"%>

<e:form id="createAccountForm" >
	<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
		<e:section value="#{msgs['SESSION.CREATE_ACCOUNT.TITLE']}" />
		<h:panelGroup>
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('createAccountForm:cancelButton');" >
				<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
				<t:graphicImage value="/media/images/back.png"
					alt="#{msgs['_.BUTTON.CANCEL']}" 
					title="#{msgs['_.BUTTON.CANCEL']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="cancelButton" action="#{sessionController.gotoLogin}"
				value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
		</h:panelGroup>
	</e:panelGrid>
	
	<e:messages />
	
	<e:paragraph value="#{msgs['SESSION.CREATE_ACCOUNT.TOP']}" />
	
	<e:panelGrid columns="2" >
		<e:outputLabel for="email"
			value="#{msgs['SESSION.CREATE_ACCOUNT.EMAIL_PROMPT']}" />
		<e:inputText id="email" value="#{sessionController.email}" size="50" />
		<e:outputLabel for="displayName"
			value="#{msgs['SESSION.CREATE_ACCOUNT.DISPLAY_NAME_PROMPT']}" />
		<e:inputText id="displayName" value="#{sessionController.displayName}" />
		<h:panelGroup/>
		<h:panelGroup>
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('createAccountForm:createAccountButton');" >
				<e:bold value="#{msgs['SESSION.CREATE_ACCOUNT.CREATE_BUTTON']} " />
				<t:graphicImage value="/media/images/add.png"
					alt="#{msgs['SESSION.CREATE_ACCOUNT.CREATE_BUTTON']}" 
					title="#{msgs['SESSION.CREATE_ACCOUNT.CREATE_BUTTON']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="createAccountButton" action="#{sessionController.createAccount}"
				value="#{msgs['SESSION.CREATE_ACCOUNT.CREATE_BUTTON']}" />
		</h:panelGroup>
	</e:panelGrid>
	<e:paragraph value="#{msgs['SESSION.CREATE_ACCOUNT.FORGOT_PASSWORD_TEXT']}"/>
	<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('createAccountForm:forgotPasswordButton');" >
		<e:bold value="#{msgs['SESSION.CREATE_ACCOUNT.FORGOT_PASSWORD_BUTTON']} " />
		<t:graphicImage value="/media/images/alert.png"
			alt="#{msgs['SESSION.CREATE_ACCOUNT.FORGOT_PASSWORD_BUTTON']}" 
			title="#{msgs['SESSION.CREATE_ACCOUNT.FORGOT_PASSWORD_BUTTON']}" />
	</h:panelGroup>
	<e:commandButton style="display: none" id="forgotPasswordButton" action="#{sessionController.gotoForgotPassword}"
		value="#{msgs['SESSION.CREATE_ACCOUNT.FORGOT_PASSWORD_BUTTON']}" />
</e:form>
