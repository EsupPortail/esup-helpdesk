<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanInvite}">

	   <t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper ticketInvite">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content">
                    <t:htmlTag value="div" styleClass="content-inner">

<script type="text/javascript">
function addInvitation(select) {
    if (select.value == "") {
        return;
    }
    var userId = document.getElementById("ticketActionForm:ldapUid");
    if (userId.value != "" && userId.value.charAt(userId.value.length-1) != ",") {
        userId.value += ",";
    }
    userId.value += select.options[select.selectedIndex].value;
    select.selectedIndex = 0;
}
</script>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ticketActionForm">
        <t:htmlTag value="div" styleClass="message">
                <e:messages/>
        </t:htmlTag>
        <t:htmlTag value="div" styleClass="invite-form">
            <t:htmlTag value="div" styleClass="form-block form-header">
                <t:htmlTag value="h1">
                    <t:htmlTag value="span" styleClass="title">
                        <h:outputText value="#{msgs['TICKET_ACTION.TITLE.INVITE']}" escape="false" />
                    </t:htmlTag>
                    <t:htmlTag value="span" styleClass="subtitle">
                        <h:outputText value=" #{ticketController.ticket.id}" escape="false" />
                    </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="form-block form-main">
                 <t:htmlTag value="div" styleClass="form-block form-menu">
                          <t:htmlTag value="div" styleClass="form-item current" rendered="#{domainService.useLdap}">
                             <t:htmlTag id="showLdapForm" value="span" styleClass="show-ldap-input ">
                                 <h:outputText value="#{msgs['TICKET_ACTION.TEXT.INVITE.SEARCH_LDAP.TEXT']}"/>
                             </t:htmlTag>
                          </t:htmlTag>
                          <t:htmlTag value="div" styleClass="form-item">
                             <t:htmlTag id="showUIDs" value="span" styleClass=" show-form-input">
                                 <h:outputText value="#{msgs['TICKET_ACTION.TEXT.INVITE.SHOW_UIDS-INPUT']}"/>
                             </t:htmlTag>
                          </t:htmlTag>
                         <t:htmlTag value="div" styleClass="form-item" rendered="#{ticketController.userCanInviteGroup}">
                            <h:panelGroup styleClass="" id="showInviteTree">
                                <h:outputText value="#{msgs['TICKET_ACTION.BUTTON.SEARCH_GROUP']}"/>
                            </h:panelGroup>
                         </t:htmlTag>
                         <t:htmlTag value="div" styleClass="form-item" rendered="#{not empty ticketController.recentInvitationItems}">
                            <h:panelGroup styleClass="" id="showRecentInvitations">
                                <h:outputText value="#{msgs['TICKET_ACTION.TEXT.INVITE.RECENT']}"/>
                            </h:panelGroup>
                         </t:htmlTag>
                 </t:htmlTag>

                 <t:htmlTag value="div" styleClass="form-block form-content">
                      <t:htmlTag value="div" id="recentInvitationList" styleClass="form-block form-recent-invitations hideme" rendered="#{not empty ticketController.recentInvitationItems}" >
                           <t:htmlTag value="div" styleClass="form-item">
                                     <e:selectOneMenu id="addInvitation"  onchange="addInvitation(this);" >
                                         <f:selectItems value="#{ticketController.recentInvitationItems}" />
                                     </e:selectOneMenu>
                                     <e:commandButton id="cancelActionButton02" action="#{ticketController.cancelAction}"
                                                                         		value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                           </t:htmlTag>
                      </t:htmlTag>
                      <t:htmlTag value="div" id="uids" styleClass="form-block form-uids ">
                           <t:htmlTag value="div" styleClass="form-item">
                                <e:outputLabel id="ldapUid-label" for="ldapUid" value=" #{msgs['TICKET_ACTION.TEXT.INVITE.3']}" styleClass="hideme"/>
                                <t:htmlTag value="div" styleClass="ldap-search-form">
                                    <e:inputText  id="ldapUid" value="#{ticketController.ldapUid}" size="50"
                                            onkeypress="if (event.keyCode == 13) { focusFckEditor('ticketActionForm:actionMessage'); return false;}" />
                                    <h:panelGroup id="ldap-search-button" onclick="buttonClick('ticketActionForm:ldapSearchButton');" styleClass="cursor--pointer button--secondary">
                                        <h:outputText value="#{msgs['SEARCH.BUTTON.SEARCH']}"/>
                                    </h:panelGroup>
                                    	<e:commandButton id="cancelActionButton00" action="#{ticketController.cancelAction}"
                                    		value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                    <e:commandButton style="display:none"
                                        id="ldapSearchButton" action="#{ldapSearchController.firstSearch}"
                                        value="#{msgs['_.BUTTON.LDAP']}" >
                                        <t:updateActionListener value="#{ticketController}"
                                            property="#{ldapSearchController.caller}" />
                                        <t:updateActionListener value="userSelectedToTicketInvite"
                                            property="#{ldapSearchController.successResult}" />
                                        <t:updateActionListener value="cancelToTicketInvite"
                                            property="#{ldapSearchController.cancelResult}" />
                                    </e:commandButton>
                                </t:htmlTag>
                                <t:htmlTag value="div" id="about-uids" styleClass="hideme">
                                    <h:outputText  value="#{msgs['TICKET_ACTION.TEXT.INVITE.1_LDAP']}" rendered="#{domainService.useLdap}" />
				                    <h:outputText  value="#{msgs['TICKET_ACTION.TEXT.INVITE.1_NO_LDAP']}" rendered="#{not domainService.useLdap}" />
                                </t:htmlTag>
                           </t:htmlTag>
                      </t:htmlTag>
                      <t:htmlTag value="div" id="ldapSearch" styleClass="form-block form-ldap-search ">
                           <t:htmlTag value="div" styleClass="form-item">

                           </t:htmlTag>
                      </t:htmlTag>
                      <t:htmlTag value="div" id="inviteTree" styleClass="form-block form-invite-tree hideme treeview basic-style" rendered="#{ticketController.userCanInviteGroup}" >
                           <t:htmlTag value="div" styleClass="form-item">
                                 <t:tree2 id="tree" value="#{ticketController.inviteTree}"
                                                var="node" varNodeToggler="t" clientSideToggle="true"
                                                showRootNode="false" >
                                                <f:facet name="root">
                                                    <h:panelGroup>
                                                        <t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
                                                        <t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="department">
                                                    <h:panelGroup>
                                                        <h:panelGroup
                                                            style="cursor: pointer"
                                                            styleClass="leaf leaf-active"
                                                            onclick="document.getElementById('ticketActionForm:ldapUid').value='#{node.managersString}';" >
                                                            <e:text value=" #{msgs['TICKET_ACTION.TEXT.INVITE.DEPARTMENT_NODE']}" >
                                                                <f:param value="#{node.department.xlabel}" />
                                                                <f:param value="#{node.managersNumber}" />
                                                            </e:text>
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="category">
                                                    <h:panelGroup>
                                                        <h:panelGroup rendered="#{node.managersNumber gt 0}" >
                                                            <h:panelGroup
                                                                style="cursor: pointer"
                                                                styleClass="leaf leaf-active"
                                                                onclick="document.getElementById('ticketActionForm:ldapUid').value='#{node.managersString}';" >
                                                                <e:text value=" #{msgs['TICKET_ACTION.TEXT.INVITE.CATEGORY_NODE_WITH_MEMBERS']}" >
                                                                    <f:param value="#{node.category.xlabel}" />
                                                                    <f:param value="#{node.managersNumber}" />
                                                                </e:text>
                                                            </h:panelGroup>
                                                        </h:panelGroup>
                                                        <e:italic value=" #{msgs['TICKET_ACTION.TEXT.INVITE.CATEGORY_NODE']}"
                                                            rendered="#{node.managersNumber == 0}" >
                                                            <f:param value="#{node.category.xlabel}" />
                                                        </e:italic>
                                                    </h:panelGroup>
                                                </f:facet>
                                  </t:tree2>
                                  <e:commandButton id="cancelActionButton01" action="#{ticketController.cancelAction}"
                                                                      		value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                           </t:htmlTag>
                      </t:htmlTag>

                      <t:htmlTag value="div" id="inviteComment" styleClass="form-block hideme form-body">
                            <t:htmlTag value="div" styleClass="form-item">
                                <e:outputLabel for="actionMessage" value="#{msgs['TICKET_ACTION.TEXT.INVITE.2']}" />
                                <fck:editor id="actionMessage"  toolbarSet="actionMessage" />
                            </t:htmlTag>
                            <t:htmlTag value="div" styleClass="form-item display-flex">
                                <e:commandButton id="actionButton" value="#{msgs['TICKET_ACTION.BUTTON.INVITE']}"
                                        action="#{ticketController.doInvite}"
                                        styleClass="button--primary"/>
                                <%@include file="_ticketActionCancel.jsp"%>
                            </t:htmlTag>

                      </t:htmlTag>

                 </t:htmlTag>
            </t:htmlTag>

		</t:htmlTag>



	                    </e:form>

	                    <%@include file="_ticketActionJavascript.jsp"%>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>

                <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
                </t:htmlTag>

        </t:htmlTag>


</e:page>

