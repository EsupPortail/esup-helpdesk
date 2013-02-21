<%@include file="_include.jsp"%>

<e:form id="forgotPasswordForm" >

	<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
		<e:section value="#{msgs['SESSION.FORGOT_PASSWORD.TITLE']}" />
		<h:panelGroup>
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('forgotPasswordForm:cancelButton');" >
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
	
	<e:paragraph value="#{msgs['SESSION.FORGOT_PASSWORD.TOP']}" />
	
	<e:outputLabel for="email"
		value="#{msgs['SESSION.FORGOT_PASSWORD.EMAIL_PROMPT']}" />
	<e:inputText id="email" value="#{sessionController.email}" size="50" />
	<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('forgotPasswordForm:receiveButton');" >
		<e:bold value=" #{msgs['SESSION.FORGOT_PASSWORD.RECEIVE_BUTTON']} " />
		<t:graphicImage value="/media/images/alert.png"
			alt="#{msgs['SESSION.FORGOT_PASSWORD.RECEIVE_BUTTON']}" 
			title="#{msgs['SESSION.FORGOT_PASSWORD.RECEIVE_BUTTON']}" />
	</h:panelGroup>
	<e:commandButton style="display: none" id="receiveButton" action="#{sessionController.forgotPassword}"
		value="#{msgs['SESSION.FORGOT_PASSWORD.RECEIVE_BUTTON']}" />
	<e:paragraph value="#{msgs['SESSION.FORGOT_PASSWORD.NO_ACCOUNT_TEXT']}"/>
	<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('forgotPasswordForm:createAccountButton');" >
		<e:bold value="#{msgs['SESSION.FORGOT_PASSWORD.CREATE_ACCOUNT_BUTTON']} " />
		<t:graphicImage value="/media/images/add.png"
			alt="#{msgs['SESSION.FORGOT_PASSWORD.CREATE_ACCOUNT_BUTTON']}" 
			title="#{msgs['SESSION.FORGOT_PASSWORD.CREATE_ACCOUNT_BUTTON']}" />
	</h:panelGroup>
	<e:commandButton style="display: none" id="createAccountButton" action="#{sessionController.gotoCreateAccount}"
		value="#{msgs['SESSION.FORGOT_PASSWORD.CREATE_ACCOUNT_BUTTON']}" />
</e:form>
