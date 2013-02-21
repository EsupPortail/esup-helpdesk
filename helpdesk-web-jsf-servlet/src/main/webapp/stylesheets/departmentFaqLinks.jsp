 <%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanEditDepartmentProperties}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="departmentFaqLinksForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
			<e:section value="#{msgs['DEPARTMENT_FAQ_LINKS.TITLE']}">
				<f:param value="#{departmentsController.department.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentFaqLinksForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.BACK']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.BACK']}" 
						title="#{msgs['_.BUTTON.BACK']}" />
				</h:panelGroup>
				<e:commandButton id="cancelButton" action="back" style="display: none"
					value="#{msgs['_.BUTTON.BACK']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:panelGrid columns="2" columnClasses="colLeftMaxNowrap,colLeftNowrap">
			<h:panelGroup>
				<e:subSection value="#{msgs['DEPARTMENT_FAQ_LINKS.TEXT.MANAGE_FAQ_LINKS']}" />
				<e:paragraph 
					value="#{msgs['DEPARTMENT_FAQ_LINKS.TEXT.NO_FAQ_LINK']}" 
					rendered="#{empty departmentsController.faqLinks}"
					/>
				<h:panelGroup rendered="#{not empty departmentsController.faqLinks}" >
					<e:paragraph value="#{msgs['DEPARTMENT_FAQ_LINKS.TEXT.FAQ_LINKS']}" />
					<e:dataTable
						id="faqLinkData" rowIndexVar="variable"
						value="#{departmentsController.faqLinks}"
						var="faqLink" border="0" cellspacing="0"
						cellpadding="0">
						<f:facet name="header">
							<t:htmlTag value="hr" />
						</f:facet>
						<t:column style="white-space: nowrap" >
                            <t:graphicImage value="/media/images/faq#{faqHasChildrenHelper[faqLink.faq]?'-container':''}.png" />
							<e:text value=" " />
							<t:graphicImage value="/media/images/faq-scope-#{faqLink.faq.effectiveScope}.png" />
							<e:text value=" #{faqLink.faq.label}" />
						</t:column>
						<t:column>
							<h:panelGroup style="cursor: pointer" 
								onclick="simulateLinkClick('departmentFaqLinksForm:faqLinkData:#{variable}:deleteButton');" >
								<t:graphicImage value="/media/images/delete.png"
									alt="-" title="-" />
							</h:panelGroup>
							<e:commandButton id="deleteButton" value="-" style="display: none" 
								action="#{departmentsController.deleteDepartmentFaqLink}" >
								<t:updateActionListener value="#{faqLink}"
									property="#{departmentsController.faqLinkToDelete}" />
							</e:commandButton>
						</t:column>
						<t:column>
							<h:panelGroup
								rendered="#{variable != departmentsController.faqLinksNumber - 1}" >
								<h:panelGroup style="cursor: pointer" 
									onclick="simulateLinkClick('departmentFaqLinksForm:faqLinkData:#{variable}:moveLastButton');" >
									<t:graphicImage value="/media/images/arrow_last.png"
										alt="vv" title="vv" />
								</h:panelGroup>
								<e:commandButton id="moveLastButton" value="vv" style="display: none" 
									action="#{departmentsController.moveDepartmentFaqLinkLast}" 
									>
									<t:updateActionListener value="#{faqLink}"
										property="#{departmentsController.faqLinkToMove}" />
								</e:commandButton>
							</h:panelGroup>
						</t:column>
						<t:column>
							<h:panelGroup
								rendered="#{variable != departmentsController.faqLinksNumber - 1}" >
								<h:panelGroup style="cursor: pointer" 
									onclick="simulateLinkClick('departmentFaqLinksForm:faqLinkData:#{variable}:moveDownButton');" >
									<t:graphicImage value="/media/images/arrow_down.png"
										alt="v" title="v" />
								</h:panelGroup>
								<e:commandButton id="moveDownButton" value="v" style="display: none" 
									action="#{departmentsController.moveDepartmentFaqLinkDown}" 
									>
									<t:updateActionListener value="#{faqLink}"
										property="#{departmentsController.faqLinkToMove}" />
								</e:commandButton>
							</h:panelGroup>
						</t:column>
						<t:column>
							<h:panelGroup
								rendered="#{variable != 0}" >
								<h:panelGroup style="cursor: pointer" 
									onclick="simulateLinkClick('departmentFaqLinksForm:faqLinkData:#{variable}:moveUpButton');" >
									<t:graphicImage value="/media/images/arrow_up.png"
										alt="^" title="^" />
								</h:panelGroup>
								<e:commandButton id="moveUpButton" value="^" style="display: none" 
									action="#{departmentsController.moveDepartmentFaqLinkUp}" 
									>
									<t:updateActionListener value="#{faqLink}"
										property="#{departmentsController.faqLinkToMove}" />
								</e:commandButton>
							</h:panelGroup>
						</t:column>
						<t:column>
							<h:panelGroup
								rendered="#{variable != 0}" >
								<h:panelGroup style="cursor: pointer" 
									onclick="simulateLinkClick('departmentFaqLinksForm:faqLinkData:#{variable}:moveFirstButton');" >
									<t:graphicImage value="/media/images/arrow_first.png"
										alt="^^" title="^^" />
								</h:panelGroup>
								<e:commandButton id="moveFirstButton" value="^^" style="display: none" 
									action="#{departmentsController.moveDepartmentFaqLinkFirst}" 
									>
									<t:updateActionListener value="#{faqLink}"
										property="#{departmentsController.faqLinkToMove}" />
								</e:commandButton>
							</h:panelGroup>
						</t:column>
						<f:facet name="footer">
							<t:htmlTag value="hr" />
						</f:facet>
					</e:dataTable>
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup>
				<e:subSection value="#{msgs['DEPARTMENT_FAQ_LINKS.TEXT.ADD_FAQ_LINKS']}" />
				<t:tree2 id="tree" value="#{departmentsController.faqTree}"
					var="node" varNodeToggler="t" clientSideToggle="true" 
					showRootNode="true" >
					<f:facet name="root">
						<h:panelGroup>
							<h:panelGroup style="white-space: nowrap" >
								<t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
								<t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
								<e:italic value=" #{msgs['DEPARTMENT_FAQ_LINKS.TEXT.TREE_ROOT_LABEL']}" />
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
							<h:panelGroup style="cursor: pointer; white-space: nowrap" 
								onclick="simulateLinkClick('departmentFaqLinksForm:tree:#{node.identifier}:selectFaq');return false;">
	                            <t:graphicImage value="/media/images/faq.png" rendered="#{node.leaf}" />
	                            <t:graphicImage value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" rendered="#{not node.leaf}" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/faq-scope-#{node.faq.effectiveScope}.png" />
								<e:bold value=" #{node.faq.label}" />
							</h:panelGroup>
							<e:commandButton 
								value="->" id="selectFaq" style="display: none" 
								action="#{departmentsController.addDepartmentFaqLink}" >
								<t:updateActionListener value="#{node.faq}" property="#{departmentsController.faqToLink}" />
							</e:commandButton>
						</h:panelGroup>
					</f:facet>
				</t:tree2>
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
