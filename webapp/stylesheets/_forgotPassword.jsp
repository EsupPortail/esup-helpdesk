<%@include file="_include.jsp"%>


<e:form id="forgotPasswordForm" >

    <t:htmlTag value="div" styleClass="message">
        <e:messages/>
    </t:htmlTag>

    <t:htmlTag value="div" styleClass="block">
        <t:htmlTag value="h1">
    	     <h:outputText value="#{msgs['SESSION.FORGOT_PASSWORD.TITLE']}" escape="false" />
        </t:htmlTag>
        <t:htmlTag value="div" styleClass="form-block">
            <e:paragraph value="#{msgs['SESSION.FORGOT_PASSWORD.TOP']}" />
            <t:htmlTag value="div" styleClass="form-item">
                <e:outputLabel for="email"
                    value="#{msgs['SESSION.FORGOT_PASSWORD.EMAIL_PROMPT']}" />
                <e:inputText id="email" value="#{sessionController.email}" size="50" />
            </t:htmlTag>

        </t:htmlTag>
        <t:htmlTag value="div" styleClass="form-block">
            <t:htmlTag value="div" styleClass="form-item form-submit" >
                <e:commandButton styleClass="button--primary" id="receiveButton" action="#{sessionController.forgotPassword}"
                    value="#{msgs['SESSION.FORGOT_PASSWORD.RECEIVE_BUTTON']}" />
		        <e:commandButton  id="cancelButton" action="#{sessionController.gotoLogin}"
				    value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                <e:commandButton id="forgotPasswordButton" action="#{sessionController.gotoCreateAccount}"
                    value="#{msgs['SESSION.FORGOT_PASSWORD.CREATE_ACCOUNT_BUTTON']}" />
            </t:htmlTag>
         </t:htmlTag>

    </t:htmlTag>

</e:form>

