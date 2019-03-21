<%@include file="_include.jsp"%>

    <t:htmlTag value="div" styleClass="message">
        <e:messages/>
    </t:htmlTag>

<t:htmlTag value="div" styleClass="login-form">
	<t:htmlTag value="div" styleClass="login-form--cas cursor--pointer block block--card" rendered="#{sessionController.printCasLoginForm}">
		<e:form id="casLoginForm">
			<h:panelGroup  onclick="simulateLinkClick('casLoginForm:loginButton');" >
			    <t:htmlTag value="h1">
					<e:text value="#{msgs['SESSION.LOGIN.CAS.TEXT.3']}" >
                    	<f:param value="#{sessionController.institutionName}" />
                    </e:text>
			    </t:htmlTag>
			    <t:htmlTag value="i" styleClass="fas fa-sign-out-alt"/>
                <e:text value="#{msgs['SESSION.LOGIN.CAS.TEXT.4']}" >
                	<f:param value="#{sessionController.institutionName}" />
            	</e:text>
            	</h:panelGroup>
			<e:commandButton id="loginButton" style="display: none"
				action="#{sessionController.casLogin}" />
			<e:text value="#{msgs['SESSION.LOGIN.CAS.TEXT.1']}" />
			<e:text value="#{msgs['SESSION.LOGIN.CAS.TEXT.2']}" />
		</e:form>
	</t:htmlTag>


	<t:htmlTag value="div" styleClass="login-form--shibboleth cursor--pointer block block--card" rendered="#{sessionController.printShibbolethLoginForm}">
    		<e:form id="shibbolethLoginForm">
    			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('shibbolethLoginForm:loginButton');" >
    				<e:subSection value="#{msgs['SESSION.LOGIN.SHIBBOLETH.LOGIN_BUTTON']} " />
    				<t:graphicImage value="/media/images/shibboleth-login.png"
    					alt="#{msgs['SESSION.LOGIN.SHIBBOLETH.LOGIN_BUTTON']}"
    					title="#{msgs['SESSION.LOGIN.SHIBBOLETH.LOGIN_BUTTON']}" />
    			</h:panelGroup>
    			<e:commandButton id="loginButton" style="display: none"
    				value="#{msgs['SESSION.LOGIN.SHIBBOLETH.LOGIN_BUTTON']}"
    				action="#{sessionController.shibbolethLogin}" />
    			<e:paragraph value=" " />
    			<e:text value="#{msgs['SESSION.LOGIN.SHIBBOLETH.TEXT.1']}" />
    			<e:text value="#{msgs['SESSION.LOGIN.SHIBBOLETH.TEXT.2']}" />
    		</e:form>
    </t:htmlTag>

    <t:htmlTag value="div" styleClass="login-form--application block block--card" rendered="#{sessionController.printApplicationLoginForm}">
    		<e:form id="applicationLoginForm">
    			<t:htmlTag value="div" styleClass="block cursor--pointer" rendered="#{not sessionController.showApplicationLoginForm}">
    				<h:panelGroup  onclick="simulateLinkClick('applicationLoginForm:showApplicationLoginFormButton');" >
                        <t:htmlTag value="h1">
			                <h:outputText value="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_FORM.TITLE']}" escape="false" />
			            </t:htmlTag>
			            <t:htmlTag value="i" styleClass="fas fa-envelope"/>
                        <e:text value="#{msgs['SESSION.LOGIN.APPLICATION.TEXT.1']}"/>
    				</h:panelGroup>
    				<e:commandButton id="showApplicationLoginFormButton" style="display: none"
    					value="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']}" >
    					<t:updateActionListener value="true" property="#{sessionController.showApplicationLoginForm}" />
    				</e:commandButton>
    			</t:htmlTag>

                <t:htmlTag value="div" styleClass="block" rendered="#{sessionController.showApplicationLoginForm}">
    				    <t:htmlTag value="h1">
                    	    <h:outputText value="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_FORM.TITLE']}" escape="false" />
                    	</t:htmlTag>
                    	<t:htmlTag value="div" styleClass="block form-block">
                                <t:htmlTag value="div" styleClass="form-item">
                                    <e:outputLabel for="email" value="#{msgs['SESSION.LOGIN.APPLICATION.EMAIL_PROMPT']}" />
                                    <e:inputText id="email" value="#{sessionController.email}" />
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item">
                                    <e:outputLabel for="applicationPassword" value="#{msgs['SESSION.LOGIN.APPLICATION.PASSWORD_PROMPT']}" />
                                    <e:inputSecret id="applicationPassword" value="#{sessionController.password}" />
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item form-submit">
                                       <e:commandButton id="loginButton"
                                       styleClass="button--primary"
                                       value="#{msgs['SESSION.LOGIN.APPLICATION.LOGIN_BUTTON']}" action="#{sessionController.applicationLoginFromForm}" />
                                </t:htmlTag>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="block form-block">
                            		<h:panelGroup styleClass="block-inline link" onclick="simulateLinkClick('applicationLoginForm:forgotPasswordButton');" >
                                					<h:outputText value="#{msgs['SESSION.LOGIN.APPLICATION.FORGOT_PASSWORD_BUTTON']} " />
                                	</h:panelGroup>

                                	<h:panelGroup styleClass="block-inline link" onclick="simulateLinkClick('applicationLoginForm:createAccountButton');" >
                                					<h:outputText value="#{msgs['SESSION.LOGIN.APPLICATION.CREATE_ACCOUNT_BUTTON']} " />
                                	</h:panelGroup>

                                	<e:commandButton id="forgotPasswordButton" style="display: none"
                                            value="#{msgs['SESSION.LOGIN.APPLICATION.FORGOT_PASSWORD_BUTTON']}"
                                            action="#{sessionController.gotoForgotPassword}" />
                                	<e:commandButton id="createAccountButton" style="display: none"
                                			value="#{msgs['SESSION.LOGIN.APPLICATION.CREATE_ACCOUNT_BUTTON']}"
                                			action="#{sessionController.gotoCreateAccount}" />
                         </t:htmlTag>
                </t:htmlTag>
    		</e:form>
    	</t:htmlTag>

        <t:htmlTag value="div" styleClass="login-form--specific cursor--pointer block block--card" rendered="#{sessionController.printSpecificLoginForm}">
        		<e:form id="specificLoginForm">
        			<h:panelGroup rendered="#{not sessionController.showSpecificLoginForm}">
        				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('specificLoginForm:showSpecificLoginFormButton');" >
        					<e:subSection value="#{msgs['SESSION.LOGIN.SPECIFIC.LOGIN_BUTTON']} " />
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
        			<e:text value="#{msgs['SESSION.LOGIN.SPECIFIC.TEXT.2']}" />
        		</e:form>
        	</t:htmlTag>

</t:htmlTag>



