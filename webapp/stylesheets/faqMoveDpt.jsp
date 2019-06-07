<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="faqs" authorized="#{faqsController.pageAuthorized}" locale="#{sessionController.locale}" >
	   <t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

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
                <e:commandButton id="changeCategoryButton"
                         styleClass="button--cancel"
                         value="#{msgs['TICKET_ACTION.BUTTON.CREATE.CANCEL']}"
                         action="cancel" />                            
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
						action="#{faqsController.doMoveFaqDpt}" >
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
						action="#{faqsController.doMoveFaqDpt}" >
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
						action="#{faqsController.doMoveFaqDpt}" >
						<t:updateActionListener value="#{node.faq}" property="#{faqsController.targetFaq}" />
					</e:commandButton>
				</h:panelGroup>
			</f:facet>
		</t:tree2>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	            </t:htmlTag>
                <t:htmlTag value="footer" styleClass="footer">
		           	<t:aliasBean alias="#{controller}" value="#{faqsController}" >
					    <%@include file="_footer.jsp"%>
					</t:aliasBean>
                </t:htmlTag>
        </t:htmlTag>
</e:page>
