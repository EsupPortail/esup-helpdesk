<%@include file="_include.jsp"%>

<e:section value="#{msgs['SESSION.LOGIN.TITLE']}" />

<e:messages />

<e:panelGrid columns="4" columnClasses="colCenter,colCenter,colCenter,colCenter" width="100%" >
	<h:panelGroup>
		<e:form id="casLoginForm" 
			rendered="#{sessionController.printCasLoginForm}">
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('casLoginForm:loginButton');" >
				<e:subSection value="#{msgs['SESSION.LOGIN.CAS.LOGIN_BUTTON']} " />
				<t:htmlTag value="br"/>
				<t:graphicImage value="/media/images/cas-login.png"
					alt="#{msgs['SESSION.LOGIN.CAS.LOGIN_BUTTON']}" 
					title="#{msgs['SESSION.LOGIN.CAS.LOGIN_BUTTON']}" />
			</h:panelGroup>
			<e:commandButton id="loginButton" style="display: none"
				value="#{msgs['SESSION.LOGIN.CAS.LOGIN_BUTTON']}"
				action="#{sessionController.casLogin}" />
			<e:paragraph value=" " />
			<e:text value="#{msgs['SESSION.LOGIN.CAS.TEXT.1']}" />
			<t:htmlTag value="br"/>
			<e:text value="#{msgs['SESSION.LOGIN.CAS.TEXT.2']}" />
		</e:form>
	</h:panelGroup>
	<h:panelGroup>
		<e:form id="shibbolethLoginForm" 
			rendered="#{sessionController.printShibbolethLoginForm}">
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('shibbolethLoginForm:loginButton');" >
				<e:subSection value="#{msgs['SESSION.LOGIN.SHIBBOLETH.LOGIN_BUTTON']} " />
				<t:htmlTag value="br"/>
				<t:graphicImage value="/media/images/shibboleth-login.png"
					alt="#{msgs['SESSION.LOGIN.SHIBBOLETH.LOGIN_BUTTON']}" 
					title="#{msgs['SESSION.LOGIN.SHIBBOLETH.LOGIN_BUTTON']}" />
			</h:panelGroup>
			<e:commandButton id="loginButton" style="display: none"
				value="#{msgs['SESSION.LOGIN.SHIBBOLETH.LOGIN_BUTTON']}"
				action="#{sessionController.shibbolethLogin}" />
			<e:paragraph value=" " />
			<e:text value="#{msgs['SESSION.LOGIN.SHIBBOLETH.TEXT.1']}" />
			<t:htmlTag value="br"/>
			<e:text value="#{msgs['SESSION.LOGIN.SHIBBOLETH.TEXT.2']}" />
		</e:form>
	</h:panelGroup>
	<h:panelGroup>
		<e:form id="applicationLoginForm" 
			rendered="#{sessionController.printApplicationLoginForm}">
			<h:panelGroup rendered="#{not sessionController.showApplicationLoginForm}">
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('applicationLoginForm:showApplicationLoginFormButton');" >
					<e:subSection value="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']} " />
					<t:htmlTag value="br"/>
					<t:graphicImage value="/media/images/application-login.png"
						alt="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']}" 
						title="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']}" />
				</h:panelGroup>
				<e:commandButton id="showApplicationLoginFormButton" style="display: none"
					value="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']}" >
					<t:updateActionListener value="true" property="#{sessionController.showApplicationLoginForm}" />
				</e:commandButton>
			</h:panelGroup>
			<h:panelGroup rendered="#{sessionController.showApplicationLoginForm}">
				<e:subSection value="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']} " />
				<e:outputLabel for="email"
					value="#{msgs['SESSION.LOGIN.APPLICATION.EMAIL_PROMPT']}" />
				<t:htmlTag value="br"/>
				<e:inputText id="email" value="#{sessionController.email}" size="50" />
				<t:htmlTag value="br"/>
				<e:outputLabel for="applicationPassword"
					value="#{msgs['SESSION.LOGIN.APPLICATION.PASSWORD_PROMPT']}" />
				<t:htmlTag value="br"/>
				<e:inputSecret id="applicationPassword" value="#{sessionController.password}" />
				<t:htmlTag value="br"/>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('applicationLoginForm:loginButton');" >
					<t:htmlTag value="br"/>
					<t:graphicImage value="/media/images/application-login.png"
						alt="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']}" 
						title="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']}" />
				</h:panelGroup>
				<e:commandButton id="loginButton" style="display: none"
					value="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']}" 
					action="#{sessionController.applicationLoginFromForm}" />
				<e:paragraph value=" " />
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('applicationLoginForm:forgotPasswordButton');" >
					<e:bold value="#{msgs['SESSION.LOGIN.APPLICATION.FORGOT_PASSWORD_BUTTON']} " />
				</h:panelGroup>
				<e:commandButton id="forgotPasswordButton" style="display: none"
					value="#{msgs['SESSION.LOGIN.APPLICATION.FORGOT_PASSWORD_BUTTON']}" 
					action="#{sessionController.gotoForgotPassword}" />
				<e:paragraph value=" " />
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('applicationLoginForm:createAccountButton');" >
					<e:bold value="#{msgs['SESSION.LOGIN.APPLICATION.CREATE_ACCOUNT_BUTTON']} " />
				</h:panelGroup>
				<e:commandButton id="createAccountButton" style="display: none"
					value="#{msgs['SESSION.LOGIN.APPLICATION.CREATE_ACCOUNT_BUTTON']}" 
					action="#{sessionController.gotoCreateAccount}" />
			</h:panelGroup>
			<e:paragraph value=" " />
			<e:text value="#{msgs['SESSION.LOGIN.APPLICATION.TEXT.1']}" />
			<t:htmlTag value="br"/>
			<e:text value="#{msgs['SESSION.LOGIN.APPLICATION.TEXT.2']}" />
		</e:form>
	</h:panelGroup>
	<h:panelGroup>
		<e:form id="specificLoginForm" 
			rendered="#{sessionController.printSpecificLoginForm}">
			<h:panelGroup rendered="#{not sessionController.showSpecificLoginForm}">
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('specificLoginForm:showSpecificLoginFormButton');" >
					<e:subSection value="#{msgs['SESSION.LOGIN.SPECIFIC.LOGIN_BUTTON']} " />
					<t:htmlTag value="br"/>
					<t:graphicImage value="/media/images/specific-login.png"
						alt="#{msgs['SESSION.LOGIN.SPECIFIC.LOGIN_BUTTON']}" 
						title="#{msgs['SESSION.LOGIN.SPECIFIC.LOGIN_BUTTON']}" />
				</h:panelGroup>
				<e:commandButton id="showSpecificLoginFormButton" style="display: none"
					value="#{msgs['SESSION.LOGIN.SPECIFIC.LOGIN_BUTTON']}" >
					<t:updateActionListener value="true" property="#{sessionController.showSpecificLoginForm}" />
				</e:commandButton>
			</h:panelGroup>
			<h:panelGroup rendered="#{sessionController.showSpecificLoginForm}">
				<e:subSection value="#{msgs['SESSION.LOGIN.SPECIFIC.LOGIN_BUTTON']} " />
				<e:outputLabel for="id"
					value="#{msgs['SESSION.LOGIN.SPECIFIC.ID_PROMPT']}" />
				<t:htmlTag value="br"/>
				<e:inputText id="id" value="#{sessionController.id}" size="50" />
				<t:htmlTag value="br"/>
				<e:outputLabel for="specificPassword"
					value="#{msgs['SESSION.LOGIN.SPECIFIC.PASSWORD_PROMPT']}" />
				<t:htmlTag value="br"/>
				<e:inputSecret id="specificPassword" value="#{sessionController.password}" />
				<t:htmlTag value="br"/>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('specificLoginForm:loginButton');" >
					<t:htmlTag value="br"/>
					<t:graphicImage value="/media/images/specific-login.png"
						alt="#{msgs['SESSION.LOGIN.SPECIFIC.LOGIN_BUTTON']}" 
						title="#{msgs['SESSION.LOGIN.SPECIFIC.LOGIN_BUTTON']}" />
				</h:panelGroup>
				<e:commandButton id="loginButton" style="display: none"
					value="#{msgs['SESSION.LOGIN.SPECIFIC.LOGIN_BUTTON']}" 
					action="#{sessionController.specificLogin}" />
			</h:panelGroup>
			<e:paragraph value=" " />
			<e:text value="#{msgs['SESSION.LOGIN.SPECIFIC.TEXT.1']}" />
			<t:htmlTag value="br"/>
			<e:text value="#{msgs['SESSION.LOGIN.SPECIFIC.TEXT.2']}" />
		</e:form>
	</h:panelGroup>
</e:panelGrid>

