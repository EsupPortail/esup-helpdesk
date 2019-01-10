<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="administrators" locale="#{sessionController.locale}"
	authorized="#{administratorsController.currentUserCanAddAdmin}">
		   <t:htmlTag id="administratorAdd" value="div" styleClass="page-wrapper administratorAdd">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>

                    <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                        <t:htmlTag value="div" styleClass="content-inner">

                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="administratorAddForm">
                                 <t:htmlTag value="div" styleClass="message">
                                         <e:messages/>
                                 </t:htmlTag>
                                <t:htmlTag value="div" styleClass="dashboard-header">
                                            <t:htmlTag value="div" styleClass="controlPanel-title">
                                                <t:htmlTag value="h1">
                                                    <t:htmlTag value="span">
                                                        <h:outputText value="#{msgs['ADMINISTRATOR_ADD.TITLE']}"/>
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-block form-body">
                                            <t:htmlTag value="div" styleClass="form-item">
                                                <e:outputLabel  for="ldapUid" value="#{domainService.useLdap ? msgs['ADMINISTRATOR_ADD.TEXT.PROMPT_LDAP'] : msgs['ADMINISTRATOR_ADD.TEXT.PROMPT_NO_LDAP']} " />
                                                <e:inputText id="ldapUid" value="#{administratorsController.ldapUid}"/>
                                                <e:message for="ldapUid" />
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-item " >
                                                <e:commandButton rendered="#{domainService.useLdap}" id="ldapSearchButton" action="#{ldapSearchController.firstSearch}" value="#{msgs['_.BUTTON.LDAP']}" >
                                                    <t:updateActionListener value="#{administratorsController}"
                                                        property="#{ldapSearchController.caller}" />
                                                    <t:updateActionListener value="userSelectedToAdministratorAdd"
                                                        property="#{ldapSearchController.successResult}" />
                                                    <t:updateActionListener value="cancelToAdministratorAdd"
                                                        property="#{ldapSearchController.cancelResult}" />
                                                                            </e:commandButton>
                                            </t:htmlTag>
                                             <t:htmlTag value="div" styleClass="form-item display-flex" >
                                                    <e:commandButton id="addButton" action="#{administratorsController.addAdmin}" value="#{msgs['ADMINISTRATOR_ADD.BUTTON.ADD_ADMIN']}" styleClass="button--primary"/>
                                                    <e:commandButton id="cancelButton" action="cancel" value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                             </t:htmlTag>
                                </t:htmlTag>

                            </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
           <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
           </t:htmlTag>
        </t:htmlTag>
	<script type="text/javascript">

	</script>
</e:page>
