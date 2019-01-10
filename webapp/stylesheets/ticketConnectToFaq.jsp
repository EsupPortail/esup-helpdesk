<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanConnect}">
		   <t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper ticketConnectToFaq">
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
                                            <e:text value="#{msgs['TICKET_ACTION.TITLE.CONNECT_TO_FAQ']}" >
                                                <f:param value="#{ticketController.ticket.id}" />
                                            </e:text>
                                         </t:htmlTag>
                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-block form-body">
                                         <t:htmlTag value="div" styleClass="form-item">
                                            <t:tree2 id="tree" value="#{ticketController.faqTree}"
                                                    var="node" varNodeToggler="t" clientSideToggle="true"
                                                    showRootNode="false" >
                                                <f:facet name="root">
                                                    <h:panelGroup>
                                                        <h:panelGroup style="white-space: nowrap" >
                                                            <t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
                                                            <t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
                                                            <e:text value="#{msgs['TICKET_ACTION.TEXT.CONNECT_TO_FAQ.CHOOSE_TARGET_FAQ']}" />
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="department">
                                                    <h:panelGroup>
                                                        <h:panelGroup style="white-space: nowrap" >
                                                            <t:graphicImage style="display:none" value="#{departmentIconUrlProvider[node.department]}" />
                                                            <e:text value=" #{node.department.label}" />
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="faq">
                                                    <h:panelGroup>
                                                        <h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('ticketActionForm:tree:#{node.identifier}:selectFaq');return false;">
                                                            <t:graphicImage style="display:none" value="/media/images/faq.png" rendered="#{node.leaf}" />
                                                            <t:graphicImage style="display:none" value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" rendered="#{not node.leaf}" />
                                                            <e:bold value=" #{node.faq.label}" />
                                                        </h:panelGroup>
                                                        <e:commandButton
                                                            value="->" id="selectFaq" style="display: none"
                                                            action="#{ticketController.getConnectBackAction}" >
                                                            <t:updateActionListener value="#{node.faq}" property="#{ticketController.targetFaq}" />
                                                        </e:commandButton>
                                                    </h:panelGroup>
                                                </f:facet>
                                        </t:tree2>
                                         </t:htmlTag>
                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-block">
                                         <t:htmlTag value="div" styleClass="form-item" >
                                                 <e:commandButton  id="backButton" action="#{ticketController.getConnectBackAction}" value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                        </t:htmlTag>
                                     </t:htmlTag>
                                 </t:htmlTag>

                        </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
    <t:htmlTag value="footer" styleClass="footer">
            <%@include file="_footer.jsp"%>
    </t:htmlTag>
</e:page>

