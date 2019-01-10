<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanChangeOwner}">
		   <t:htmlTag id="ticketChangeOwner" value="div" styleClass="page-wrapper ticketChangeOwner">
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
                                id="ticketActionForm">
                                <t:htmlTag value="div" styleClass="message">
                                     <e:messages/>
                                 </t:htmlTag>
                                 <t:htmlTag value="div" styleClass="ticket-form">
                                     <t:htmlTag value="div" styleClass="form-block form-header">
                                         <t:htmlTag value="h1">
                                             <t:htmlTag value="span" styleClass="title">
                                                   <h:outputText value="#{msgs['TICKET_ACTION.TITLE.CHANGE_OWNER']}" escape="false" />
                                             </t:htmlTag>
                                             <t:htmlTag value="span" styleClass="subtitle">
                                                 <h:outputText value=" #{ticketController.ticket.id}" escape="false" />
                                             </t:htmlTag>
                                         </t:htmlTag>
                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-block utils-ldapuid">
                                         <t:htmlTag value="div" styleClass="form-item">
                                            <e:outputLabel  for="ldapUid" value="#{domainService.useLdap ? msgs['UTILS.TEXT.USER_PROMPT'] : msgs['UTILS.TEXT.USER_PROMPT_NO_LDAP']} " />
                                            <e:inputText id="ldapUid" value="#{ticketController.ldapUid}"/>
                                         </t:htmlTag>
                                         <t:htmlTag value="div" styleClass="form-item " >
                                            <e:commandButton rendered="#{domainService.useLdap}" id="ldapSearchButton"
                                                value="#{msgs['_.BUTTON.LDAP']}" action="#{ldapSearchController.firstSearch}" >
                                                <t:updateActionListener value="#{ticketController}"
                                                                        property="#{ldapSearchController.caller}" />
                                                <t:updateActionListener value="userSelectedToTicketChangeOwner"
                                                                        property="#{ldapSearchController.successResult}" />
                                                <t:updateActionListener value="cancelToTicketChangeOwner"
                                                                        property="#{ldapSearchController.cancelResult}" />
                                            </e:commandButton>
                                          </t:htmlTag>

                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-block form-body">
                                         <t:htmlTag value="div" styleClass="form-item">
                                                 <fck:editor
                                                 id="actionMessage"
                                                 value="#{ticketController.actionMessage}"
                                                 toolbarSet="actionMessage" />
                                         </t:htmlTag>
                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-block">
                                         <t:htmlTag value="div" styleClass="form-item display-flex" >
                                             <e:commandButton id="actionButton"
                                                     styleClass="button--primary"
                                                     value="#{msgs['TICKET_ACTION.BUTTON.CHANGE_OWNER']}"
                                                     action="#{ticketController.doChangeOwner}" />
                                             <%@include file="_ticketActionCancel.jsp"%>
                                        </t:htmlTag>
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
	<%@include file="_ticketActionJavascript.jsp"%>
</e:page>

