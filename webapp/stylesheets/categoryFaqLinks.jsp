 <%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="categoryFaqLinksForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
			<e:section value="#{msgs['CATEGORY_FAQ_LINKS.TITLE']}">
				<f:param value="#{departmentsController.categoryToUpdate.department.label}" />
				<f:param value="#{departmentsController.categoryToUpdate.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryFaqLinksForm:cancelButton');" >
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

		<h:panelGroup rendered="#{departmentsController.categoryToUpdate.inheritFaqLinks}" >
			<e:paragraph 
				value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.INHERIT_DEPARTMENT']}" 
				rendered="#{departmentsController.categoryToUpdate.parent == null}" />
			<e:paragraph 
				value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.INHERIT_CATEGORY']}" 
				rendered="#{departmentsController.categoryToUpdate.parent != null}" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryFaqLinksForm:doNotInheritButton');" >
				<e:bold value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.DO_NOT_INHERIT']} " />
				<t:graphicImage value="/media/images/cancel.png"
					alt="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.DO_NOT_INHERIT']}" 
					title="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.DO_NOT_INHERIT']}" />
			</h:panelGroup>
			<e:commandButton id="doNotInheritButton" style="display: none"
				action="#{departmentsController.toggleCategoryInheritFaqLinks}"
				value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.DO_NOT_INHERIT']}" />
			<e:paragraph 
				value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.NO_INHERITED_FAQ_LINK']}" 
				rendered="#{empty departmentsController.inheritedFaqLinks}" />
			<e:paragraph 
				value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.INHERITED_FAQ_LINKS']}" 
				rendered="#{not empty departmentsController.inheritedFaqLinks}" />
			<t:dataList
				value="#{departmentsController.inheritedFaqLinks}"
				var="faqLink" rowIndexVar="faqLinkIndex" >
				<t:htmlTag value="br" 
					rendered="#{faqLinkIndex != 0}" />
                    <t:graphicImage value="/media/images/faq#{faqHasChildrenHelper[faqLink.faq]?'-container':''}-link.png" />
					<e:italic value=" #{faqLink.faq.label}" />
			</t:dataList>
		</h:panelGroup>
		<h:panelGroup rendered="#{not departmentsController.categoryToUpdate.inheritFaqLinks}" >
			<h:panelGroup rendered="#{departmentsController.categoryToUpdate.parent == null}" >
				<e:paragraph value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.NO_INHERIT_DEPARTMENT']}" />
				<h:panelGroup style="cursor: pointer" 
					onclick="simulateLinkClick('categoryFaqLinksForm:inheritDepartmentButton');" >
					<e:bold value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.INHERIT_DEPARTMENT']} " />
					<t:graphicImage value="/media/images/add.png" />
				</h:panelGroup>
				<e:commandButton id="inheritDepartmentButton" style="display: none"
					action="#{departmentsController.toggleCategoryInheritFaqLinks}"
					value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.INHERIT_DEPARTMENT']}" />
			</h:panelGroup>
			<h:panelGroup rendered="#{departmentsController.categoryToUpdate.parent != null}" >
				<e:paragraph value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.NO_INHERIT_CATEGORY']}" />
				<h:panelGroup style="cursor: pointer" 
					onclick="simulateLinkClick('categoryFaqLinksForm:inheritCategoryButton');" >
					<e:bold value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.INHERIT_CATEGORY']} " />
					<t:graphicImage value="/media/images/add.png" />
				</h:panelGroup>
				<e:commandButton id="inheritCategoryButton" style="display: none"
					action="#{departmentsController.toggleCategoryInheritFaqLinks}"
					value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.INHERIT_CATEGORY']}" />
			</h:panelGroup>
			<t:htmlTag value="hr" />
			<e:panelGrid columns="2" columnClasses="colLeftMaxNowrap,colLeftNowrap">
				<h:panelGroup>
					<e:subSection value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.MANAGE_FAQ_LINKS']}" />
					<e:paragraph 
						value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.NO_FAQ_LINK']}" 
						rendered="#{empty departmentsController.faqLinks}"
						/>
					<h:panelGroup rendered="#{not empty departmentsController.faqLinks}" >
						<e:paragraph value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.FAQ_LINKS']}" />
						<e:dataTable
							id="faqLinkData" rowIndexVar="variable"
							value="#{departmentsController.faqLinks}"
							var="faqLink" border="0" cellspacing="0"
							cellpadding="0">
							<f:facet name="header">
								<t:htmlTag value="hr" />
							</f:facet>
							<t:column style="white-space: nowrap" >
                                <t:graphicImage value="/media/images/faq#{faqHasChildrenHelper[subFaq]?'-container':''}.png" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/faq-scope-#{faqLink.faq.effectiveScope}.png" />
								<e:text value=" #{faqLink.faq.label}" />
							</t:column>
							<t:column>
								<h:panelGroup style="cursor: pointer" 
									onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData:#{variable}:deleteButton');" >
									<t:graphicImage value="/media/images/delete.png"
										alt="-" title="-" />
								</h:panelGroup>
								<e:commandButton id="deleteButton" value="-" style="display: none" 
									action="#{departmentsController.deleteCategoryFaqLink}" >
									<t:updateActionListener value="#{faqLink}"
										property="#{departmentsController.faqLinkToDelete}" />
								</e:commandButton>
							</t:column>
							<t:column>
								<h:panelGroup
									rendered="#{variable != departmentsController.faqLinksNumber - 1}" >
									<h:panelGroup style="cursor: pointer" 
										onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData:#{variable}:moveLastButton');" >
										<t:graphicImage value="/media/images/arrow_last.png"
											alt="vv" title="vv" />
									</h:panelGroup>
									<e:commandButton id="moveLastButton" value="vv" style="display: none" 
										action="#{departmentsController.moveCategoryFaqLinkLast}" 
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
										onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData:#{variable}:moveDownButton');" >
										<t:graphicImage value="/media/images/arrow_down.png"
											alt="v" title="v" />
									</h:panelGroup>
									<e:commandButton id="moveDownButton" value="v" style="display: none" 
										action="#{departmentsController.moveCategoryFaqLinkDown}" 
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
										onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData:#{variable}:moveUpButton');" >
										<t:graphicImage value="/media/images/arrow_up.png"
											alt="^" title="^" />
									</h:panelGroup>
									<e:commandButton id="moveUpButton" value="^" style="display: none" 
										action="#{departmentsController.moveCategoryFaqLinkUp}" 
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
										onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData:#{variable}:moveFirstButton');" >
										<t:graphicImage value="/media/images/arrow_first.png"
											alt="^^" title="^^" />
									</h:panelGroup>
									<e:commandButton id="moveFirstButton" value="^^" style="display: none" 
										action="#{departmentsController.moveCategoryFaqLinkFirst}" 
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
					<e:subSection value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.ADD_FAQ_LINKS']}" />
					<t:tree2 id="tree" value="#{departmentsController.faqTree}"
						var="node" varNodeToggler="t" clientSideToggle="true" 
						showRootNode="true" >
						<f:facet name="root">
							<h:panelGroup>
								<h:panelGroup style="white-space: nowrap" >
									<t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
									<t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
									<e:italic value=" #{msgs['CATEGORY_FAQ_LINKS.TEXT.TREE_ROOT_LABEL']}" />
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
									onclick="simulateLinkClick('categoryFaqLinksForm:tree:#{node.identifier}:selectFaq');return false;">
		                            <t:graphicImage value="/media/images/faq.png" rendered="#{node.leaf}" />
		                            <t:graphicImage value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" rendered="#{not node.leaf}" />
									<e:text value=" " />
									<t:graphicImage value="/media/images/faq-scope-#{node.faq.effectiveScope}.png" />
									<e:bold value=" #{node.faq.label}" />
								</h:panelGroup>
								<e:commandButton 
									value="->" id="selectFaq" style="display: none" 
									action="#{departmentsController.addCategoryFaqLink}" >
									<t:updateActionListener value="#{node.faq}" property="#{departmentsController.faqToLink}" />
								</e:commandButton>
							</h:panelGroup>
						</f:facet>
					</t:tree2>
				</h:panelGroup>
			</e:panelGrid>
		</h:panelGroup>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
