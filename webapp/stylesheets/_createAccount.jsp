<%@include file="_include.jsp"%>

<e:form id="createAccountForm" >

    <t:htmlTag value="div" styleClass="message">
        <e:messages/>
    </t:htmlTag>

    <t:htmlTag value="div" styleClass="block">
        <t:htmlTag value="h1">
    	     <h:outputText value="#{msgs['SESSION.CREATE_ACCOUNT.TITLE']}" escape="false" />
        </t:htmlTag>
        <t:htmlTag value="div" styleClass="form-block">
            <e:paragraph value="#{msgs['SESSION.CREATE_ACCOUNT.TOP']}" />
            <t:htmlTag value="div" styleClass="form-item">
                <e:outputLabel for="email"
                    value="#{msgs['SESSION.CREATE_ACCOUNT.EMAIL_PROMPT']}" />
                <e:inputText id="email" value="#{sessionController.email}" size="50" />
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="form-item">
                <e:outputLabel for="displayName"
                    value="#{msgs['SESSION.CREATE_ACCOUNT.DISPLAY_NAME_PROMPT']}" />
                <e:inputText id="displayName" value="#{sessionController.displayName}" />
            </t:htmlTag>
        </t:htmlTag>
        <t:htmlTag value="div" styleClass="form-block">
            <t:htmlTag value="div" styleClass="form-item form-submit" >
 			    <e:commandButton styleClass="button--primary" id="createAccountButton" action="#{sessionController.createAccount}"
 				    value="#{msgs['SESSION.CREATE_ACCOUNT.CREATE_BUTTON']}" />
		        <e:commandButton  id="cancelButton" action="#{sessionController.gotoLogin}"
				    value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                <e:commandButton id="forgotPasswordButton" action="#{sessionController.gotoForgotPassword}"
                    value="#{msgs['SESSION.CREATE_ACCOUNT.FORGOT_PASSWORD_BUTTON']}" />
            </t:htmlTag>
         </t:htmlTag>

    </t:htmlTag>

</e:form>
