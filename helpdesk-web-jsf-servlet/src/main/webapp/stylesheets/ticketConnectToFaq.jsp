<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanConnect}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ticketActionForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['TICKET_ACTION.TITLE.CONNECT_TO_FAQ']}" >
				<f:param value="#{ticketController.ticket.id}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:backButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="backButton" action="#{ticketController.getConnectBackAction}"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>
		<e:messages />
		<t:tree2 id="tree" value="#{ticketController.faqTree}"
			var="node" varNodeToggler="t" clientSideToggle="true" 
			showRootNode="true" >
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
						<t:graphicImage value="#{departmentIconUrlProvider[node.department]}" />
						<e:text value=" #{node.department.label}" />
					</h:panelGroup>
				</h:panelGroup>
			</f:facet>
			<f:facet name="faq">
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('ticketActionForm:tree:#{node.identifier}:selectFaq');return false;">
                        <t:graphicImage value="/media/images/faq.png" rendered="#{node.leaf}" />
                        <t:graphicImage value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" rendered="#{not node.leaf}" />
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
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>

