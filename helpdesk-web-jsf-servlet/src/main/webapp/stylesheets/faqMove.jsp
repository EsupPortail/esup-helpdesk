<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="faqs"
	authorized="#{faqsController.pageAuthorized}"
	locale="#{sessionController.locale}" >
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="faqMoveForm" >
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%"
			cellspacing="0" cellpadding="0" >
			<e:section value="#{msgs['FAQ_MOVE.TITLE']}" >
				<f:param value="#{faqsController.faqToUpdate.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('faqMoveForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton id="cancelButton" action="cancel" style="display: none"
                    value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />
		
		<e:paragraph value="#{msgs['FAQ_MOVE.TEXT']}" />

		<t:tree2 id="tree" value="#{faqsController.moveTree}"
			var="node" varNodeToggler="t" clientSideToggle="true" 
			showRootNode="true" >
			<f:facet name="root">
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('faqMoveForm:tree:#{node.identifier}:selectRoot');">
						<t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
						<t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
						<e:text value=" " />
						<t:graphicImage value="/media/images/faq-scope-#{domainService.departmentDefaultFaqScope}.png" rendered="#{faqsController.editInterface}" />
						<e:bold value=" #{msgs['FAQS.TEXT.TREE_ROOT_LABEL']}" rendered="#{t.nodeSelected}" />
						<e:text value=" #{msgs['FAQS.TEXT.TREE_ROOT_LABEL']}" rendered="#{!t.nodeSelected}" />
					</h:panelGroup>
					<e:commandButton 
						value="->" id="selectRoot" style="display: none" 
						action="#{faqsController.doMoveFaq}" >
					</e:commandButton>
				</h:panelGroup>
			</f:facet>
			<f:facet name="department">
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('faqMoveForm:tree:#{node.identifier}:selectDepartment');">
						<t:graphicImage value="#{departmentIconUrlProvider[node.department]}" />
						<e:text value=" " />
						<t:graphicImage value="/media/images/faq-scope-#{node.department.defaultFaqScope}.png" rendered="#{faqsController.editInterface and node.department.defaultFaqScope != 'DEFAULT'}" />
						<e:text value=" #{node.department.label}" rendered="#{!t.nodeSelected}" />
						<e:bold value=" #{node.department.label}" rendered="#{t.nodeSelected}" />
					</h:panelGroup>
					<e:commandButton 
						value="->" id="selectDepartment" style="display: none" 
						action="#{faqsController.doMoveFaq}" >
						<t:updateActionListener value="#{node.department}" property="#{faqsController.targetDepartment}" />
					</e:commandButton>
				</h:panelGroup>
			</f:facet>
			<f:facet name="faq">
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('faqMoveForm:tree:#{node.identifier}:selectFaq');return false;">
                        <t:graphicImage value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" />
						<e:text value=" " />
						<t:graphicImage value="/media/images/faq-scope-#{node.faq.scope}.png" rendered="#{faqsController.editInterface and node.faq.scope != 'DEFAULT'}" />
						<e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
						<e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
					</h:panelGroup>
					<e:commandButton 
						value="->" id="selectFaq" style="display: none" 
						action="#{faqsController.doMoveFaq}" >
						<t:updateActionListener value="#{node.faq}" property="#{faqsController.targetFaq}" />
					</e:commandButton>
				</h:panelGroup>
			</f:facet>
		</t:tree2>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
