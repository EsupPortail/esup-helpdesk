<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanViewCategories}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="categoriesForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['CATEGORIES.TITLE']}" >
				<f:param value="#{departmentsController.department.label}"/>
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoriesForm:cancelButton');" >
					<e:bold value="#{msgs['CATEGORIES.BUTTON.BACK']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['CATEGORIES.BUTTON.BACK']}" 
						title="#{msgs['CATEGORIES.BUTTON.BACK']}" />
				</h:panelGroup>
				<e:commandButton id="cancelButton" action="back"
					value="#{msgs['CATEGORIES.BUTTON.BACK']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>
		<e:messages />
		<e:panelGrid columns="2" columnClasses="colLeftMax,colRightNowrap" width="100%" >
			<h:panelGroup >
				<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories}" >
					<e:text value="#{msgs['CATEGORIES.TEXT.LEGEND.TITLE']} " 
						rendered="#{departmentsController.categoriesAction != null}" />
					<h:panelGroup rendered="#{departmentsController.categoriesAction == 'ADD_DELETE'}" >
						<t:graphicImage value="/media/images/add.png" />
						<e:text value="#{msgs['CATEGORIES.TEXT.LEGEND.ADD']} " />
						<t:graphicImage value="/media/images/delete.png" />
						<e:text value=" #{msgs['CATEGORIES.TEXT.LEGEND.DELETE']} " />
					</h:panelGroup>
					<h:panelGroup rendered="#{departmentsController.categoriesAction == 'MOVE'}" >
						<t:graphicImage value="/media/images/move.png" />
						<e:text value=" #{msgs['CATEGORIES.TEXT.LEGEND.MOVE']} " />
						<t:graphicImage value="/media/images/arrow_last.png" />
						<t:graphicImage value="/media/images/arrow_down.png" />
						<t:graphicImage value="/media/images/arrow_up.png" />
						<t:graphicImage value="/media/images/arrow_first.png" />
						<e:text value=" #{msgs['CATEGORIES.TEXT.LEGEND.CHANGE_ORDER']} " />
					</h:panelGroup>
					<h:panelGroup rendered="#{departmentsController.categoriesAction == 'PROPERTIES'}" >
						<t:graphicImage value="/media/images/edit.png" />
						<e:text value=" #{msgs['CATEGORIES.TEXT.LEGEND.EDIT']} " />
					</h:panelGroup>
					<h:panelGroup rendered="#{departmentsController.categoriesAction == 'MEMBERS'}" >
						<t:graphicImage value="/media/images/members.png" />
						<e:text value=" #{msgs['CATEGORIES.TEXT.LEGEND.MEMBERS']}" />
					</h:panelGroup>
					<h:panelGroup rendered="#{departmentsController.categoriesAction == 'FAQ_LINKS'}" >
						<t:graphicImage value="/media/images/faq-links.png" />
						<e:text value=" #{msgs['CATEGORIES.TEXT.LEGEND.FAQ_LINKS']}" />
					</h:panelGroup>
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup >
				<e:outputLabel for="categoriesAction" value="#{msgs['CATEGORIES.TEXT.ACTION.PROMPT']} " /> 
				<e:selectOneMenu id="categoriesAction" 
					onchange="javascript:{simulateLinkClick('categoriesForm:actionsButton');}"
					value="#{departmentsController.categoriesAction}">
					<f:selectItems 
						value="#{departmentsController.categoriesActionItems}" />
				</e:selectOneMenu>
				<e:commandButton value="#{msgs['_.BUTTON.REFRESH']}" 
					id="actionsButton" style="display: none" />
				<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories}" >
					<e:selectOneMenu onchange="javascript:{simulateLinkClick('categoriesForm:sortCategoriesButton');}"
						value="#{departmentsController.categoriesSortOrder}">
						<f:selectItem 
							itemLabel="#{msgs['CATEGORIES.TEXT.SORT.PROMPT']}"
							itemValue="" />
						<f:selectItem 
							itemLabel="#{msgs['CATEGORIES.TEXT.SORT.LABEL']}"
							itemValue="label" />
						<f:selectItem 
							itemLabel="#{msgs['CATEGORIES.TEXT.SORT.XLABEL']}"
							itemValue="xlabel" />
						<f:selectItem 
							itemLabel="#{msgs['CATEGORIES.TEXT.SORT.REVERSE']}"
							itemValue="reverse" />
					</e:selectOneMenu>
					<e:commandButton value="#{msgs['CATEGORIES.BUTTON.SORT_CATEGORIES']}" 
						id="sortCategoriesButton" style="display: none"
						action="#{departmentsController.reorderCategories}" />
				</h:panelGroup>
			</h:panelGroup>
		</e:panelGrid>
		<t:htmlTag value="hr" />
		<h:panelGroup> 
			<t:tree2 rendered="#{departmentsController.categoryTree != null}" 
				id="tree" value="#{departmentsController.categoryTree}"
				var="node" varNodeToggler="t" clientSideToggle="true"
				showRootNode="true" >
				<f:facet name="department">
					<h:panelGroup>
						<t:graphicImage value="#{departmentIconUrlProvider[departmentsController.department]}" />
						<e:text value=" #{msgs['CATEGORIES.TEXT.DEPARTMENT_LABEL']}" >
							<f:param value="#{departmentsController.department.label}" />
						</e:text>
						<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories and departmentsController.categoriesAction == 'ADD_DELETE'}" >
							<e:bold value=" " />
							<t:graphicImage value="/media/images/add.png"
								alt="#{msgs['CATEGORIES.ALT.ADD']}" 
								title="#{msgs['CATEGORIES.ALT.ADD']}" 
								style="cursor: pointer" 
								onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:addButton');" />
							<e:commandButton value="+" id="addButton" style="display: none"
								action="addCategory" >
								<t:updateActionListener value="#{null}"
									property="#{departmentsController.categoryToAdd.parent}" />
								<t:updateActionListener value="#{departmentsController.department}"
									property="#{departmentsController.categoryToAdd.department}" />
							</e:commandButton>
						</h:panelGroup>
						<h:panelGroup rendered="#{departmentsController.categoriesAction == 'PROPERTIES'}" >
							<e:bold value=" #{msgs['CATEGORIES.TEXT.AUTO_EXPIRE']}"
								rendered="#{node.department.autoExpire != null}" >
								<f:param value="#{node.department.autoExpire}" />
							</e:bold>
							<e:bold value=" #{msgs['CATEGORIES.TEXT.DEFAULT_LABEL']}"
								rendered="#{node.department.defaultTicketLabel != null}" />
							<e:bold value=" #{msgs['CATEGORIES.TEXT.DEFAULT_MESSAGE']}"
								rendered="#{node.department.defaultTicketMessage != null}" />
							<e:bold value=" #{msgs['CATEGORIES.TEXT.PRIORITY']}"
								rendered="#{node.department.defaultTicketPriority != 0}" >
								<f:param value="#{msgs[priorityI18nKeyProvider[node.department.defaultTicketPriority]]}" />
							</e:bold>
							<e:bold value=" #{msgs['CATEGORIES.TEXT.SCOPE']}"
								rendered="#{node.department.defaultTicketScope != 'DEFAULT'}" >
								<f:param value="#{msgs[ticketScopeI18nKeyProvider[node.department.defaultTicketScope]]}" />
							</e:bold>
							<e:bold value=" #{msgs['CATEGORIES.TEXT.ALGORITHM']}"
								rendered="#{node.department.assignmentAlgorithmName != null}" >
								<f:param value="#{assignmentAlgorithmI18nDescriptionProvider[node.department.assignmentAlgorithmName]}" />
							</e:bold>
							<e:bold value=" #{msgs['CATEGORIES.TEXT.HIDDEN_TO_APPLICATION_USERS']}"
								rendered="#{node.department.hideToExternalUsers}" />
							<e:bold value=" #{msgs['CATEGORIES.TEXT.MONITORING_VALUE']}"
								rendered="#{node.department.monitoringEmail != null and node.department.monitoringLevel != 0}" >
								<f:param value="#{node.department.monitoringEmail}" />
							</e:bold>
							<h:panelGroup rendered="#{departmentsController.currentUserCanEditDepartmentProperties}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/edit.png"
									alt="#{msgs['CATEGORIES.ALT.EDIT_PROPERTIES']}" 
									title="#{msgs['CATEGORIES.ALT.EDIT_PROPERTIES']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:editPropertiesButton');" />
								<e:commandButton value="#{msgs['CATEGORIES.BUTTON.EDIT_PROPERTIES']}" 
									id="editPropertiesButton"  style="display: none"
									action="editDepartment" >
									<t:updateActionListener value="#{node.department}"
										property="#{departmentsController.departmentToUpdate}" />
								</e:commandButton>
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGroup>
				</f:facet>
				<f:facet name="category">
					<h:panelGroup>
						<t:graphicImage value="#{categoryIconUrlProvider[node.category]}" />
						<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories and departmentsController.categoriesAction == 'MOVE'}" >
							<h:panelGroup rendered="#{not node.first}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/arrow_first.png"
									alt="#{msgs['CATEGORIES.ALT.MOVE_FIRST']}" 
									title="#{msgs['CATEGORIES.ALT.MOVE_FIRST']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:moveFirstButton');" />
								<e:commandButton value="^^" id="moveFirstButton" style="display: none"
									action="#{departmentsController.moveCategoryFirst}" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToUpdate}" />
								</e:commandButton>
								<e:bold value=" " />
								<t:graphicImage value="/media/images/arrow_up.png"
									alt="#{msgs['CATEGORIES.ALT.MOVE_UP']}" 
									title="#{msgs['CATEGORIES.ALT.MOVE_UP']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:moveUpButton');" />
								<e:commandButton value="^" id="moveUpButton" style="display: none"
									action="#{departmentsController.moveCategoryUp}" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToUpdate}" />
								</e:commandButton>
							</h:panelGroup>
							<h:panelGroup rendered="#{node.first and not node.last}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/arrow_trans.png" />
								<e:bold value=" " />
								<t:graphicImage value="/media/images/arrow_trans.png" />
							</h:panelGroup>
							<h:panelGroup rendered="#{not node.last}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/arrow_down.png"
									alt="#{msgs['CATEGORIES.ALT.MOVE_DOWN']}" 
									title="#{msgs['CATEGORIES.ALT.MOVE_DOWN']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:moveDownButton');" />
								<e:commandButton value="v" id="moveDownButton" style="display: none"
									action="#{departmentsController.moveCategoryDown}" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToUpdate}" />
								</e:commandButton>
								<e:bold value=" " />
								<t:graphicImage value="/media/images/arrow_last.png"
									alt="#{msgs['CATEGORIES.ALT.MOVE_LAST']}" 
									title="#{msgs['CATEGORIES.ALT.MOVE_LAST']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:moveLastButton');" />
								<e:commandButton value="vv" id="moveLastButton" style="display: none"
									action="#{departmentsController.moveCategoryLast}" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToUpdate}" />
								</e:commandButton>
							</h:panelGroup>
							<h:panelGroup rendered="#{node.last and not node.first}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/arrow_trans.png" />
								<e:bold value=" " />
								<t:graphicImage value="/media/images/arrow_trans.png" />
							</h:panelGroup>
							<h:panelGroup >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/move.png"
									alt="#{msgs['CATEGORIES.ALT.MOVE']}" 
									title="#{msgs['CATEGORIES.ALT.MOVE']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:moveButton');" />
								<e:commandButton value="->" id="moveButton" style="display: none"
									action="#{departmentsController.gotoMoveCategory}" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToUpdate}" />
								</e:commandButton>
							</h:panelGroup>
						</h:panelGroup>
						<e:text value=" #{msgs['CATEGORIES.TEXT.CATEGORY_LABEL']} " >
							<f:param value="#{node.category.order}" />
							<f:param value="#{node.category.label}" />
							<f:param value="#{node.category.xlabel}" />
						</e:text>
						<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories and departmentsController.categoriesAction == 'ADD_DELETE'}" >
							<h:panelGroup rendered="#{not node.category.virtual}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/add.png"
									alt="#{msgs['CATEGORIES.ALT.ADD']}" 
									title="#{msgs['CATEGORIES.ALT.ADD']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:addButton');" />
								<e:commandButton value="+" id="addButton" style="display: none"
									action="addCategory" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToAdd.parent}" />
									<t:updateActionListener value="#{departmentsController.department}"
										property="#{departmentsController.categoryToAdd.department}" />
								</e:commandButton>
							</h:panelGroup>
							<h:panelGroup rendered="#{node.childCount == 0 and empty node.virtualCategories}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/delete.png"
									alt="#{msgs['CATEGORIES.ALT.DELETE']}" 
									title="#{msgs['CATEGORIES.ALT.DELETE']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:deleteButton');" />
								<e:commandButton value="x" id="deleteButton" style="display: none"
									action="#{departmentsController.deleteCategory}" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToUpdate}" />
								</e:commandButton>
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup rendered="#{departmentsController.categoriesAction == 'PROPERTIES'}" >
							<e:bold value=" #{msgs['CATEGORIES.TEXT.AUTO_EXPIRE']}"
								rendered="#{not node.category.virtual and node.category.autoExpire != null}" >
								<f:param value="#{node.category.autoExpire}" />
							</e:bold>
							<e:bold value=" #{msgs['CATEGORIES.TEXT.DEFAULT_LABEL']}"
								rendered="#{not node.category.virtual and node.category.defaultTicketLabel != null}" />
							<e:bold value=" #{msgs['CATEGORIES.TEXT.DEFAULT_MESSAGE']}"
								rendered="#{not node.category.virtual and node.category.defaultTicketMessage != null}" />
							<e:bold value=" #{msgs['CATEGORIES.TEXT.PRIORITY']}"
								rendered="#{not node.category.virtual and node.category.defaultTicketPriority != 0}" >
								<f:param value="#{msgs[priorityI18nKeyProvider[node.category.defaultTicketPriority]]}" />
							</e:bold>
							<e:bold value=" #{msgs['CATEGORIES.TEXT.SCOPE']}"
								rendered="#{not node.category.virtual and node.category.defaultTicketScope != 'DEFAULT'}" >
								<f:param value="#{msgs[ticketScopeI18nKeyProvider[node.category.defaultTicketScope]]}" />
							</e:bold>
							<e:bold value=" #{msgs['CATEGORIES.TEXT.ALGORITHM']}"
								rendered="#{not node.category.virtual and node.category.assignmentAlgorithmName != null}" >
								<f:param value="#{assignmentAlgorithmI18nDescriptionProvider[node.category.assignmentAlgorithmName]}" />
							</e:bold>
							<e:bold value=" #{msgs['CATEGORIES.TEXT.HIDE_TO_EXTERNAL_USERS']}"
								rendered="#{not node.category.virtual and node.category.hideToExternalUsers}" />
							<h:panelGroup
								rendered="#{not node.category.virtual and not node.category.inheritMonitoring}" >
								<e:bold value=" #{msgs['CATEGORIES.TEXT.MONITORING_NONE']}"
									rendered="#{node.category.monitoringEmail == null or node.category.monitoringLevel == 0}" />
								<e:bold value=" #{msgs['CATEGORIES.TEXT.MONITORING_VALUE']}"
									rendered="#{node.category.monitoringEmail != null and node.category.monitoringLevel != 0}" >
									<f:param value="#{node.category.monitoringEmail}" />
								</e:bold>
							</h:panelGroup>
							<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/edit.png"
									alt="#{msgs['CATEGORIES.ALT.EDIT_PROPERTIES']}" 
									title="#{msgs['CATEGORIES.ALT.EDIT_PROPERTIES']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:editPropertiesButton');" />
								<e:commandButton value="#{msgs['CATEGORIES.BUTTON.EDIT_PROPERTIES']}" 
									id="editPropertiesButton"  style="display: none"
									action="editCategory" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToUpdate}" />
								</e:commandButton>
							</h:panelGroup>
							<h:panelGroup rendered="#{node.category.virtual}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/redirection.png" />
								<e:bold value=" #{msgs['CATEGORIES.TEXT.REDIRECTION']}" >
									<f:param value="#{node.category.realCategory.department.label}" />
									<f:param value="#{node.category.realCategory.label}" />
								</e:bold>
							</h:panelGroup>
							<h:panelGroup rendered="#{not empty node.virtualCategories}" >
								<h:panelGroup style="cursor: pointer" 
									onclick="showHideElement('categoriesForm:tree:#{node.identifier}:virtualCategories');" >
									<e:bold value=" " />
									<t:graphicImage value="/media/images/redirection-inverted.png" />
									<e:bold value=" #{msgs['CATEGORIES.TEXT.VIRTUAL_CATEGORIES_NUMBER']}" >
										<f:param value="#{node.virtualCategoriesNumber}" />
									</e:bold>
								</h:panelGroup>
								<h:panelGroup id="virtualCategories" 
									rendered="#{not empty node.virtualCategories}" style="display: none" >
									<t:dataList value="#{node.virtualCategories}" var="virtualCategory" 
										rowIndexVar="virtualCategoryIndex">
										<t:htmlTag value="br" rendered="#{virtualCategoryIndex != 0}" />
										<t:graphicImage value="/media/images/category-trans.png" />
										<t:graphicImage value="/media/images/redirection-inverted.png" />
										<e:italic value=" #{msgs['CATEGORIES.TEXT.VIRTUAL_CATEGORY']}" >
											<f:param value="#{virtualCategory.department.label}" />
											<f:param value="#{virtualCategory.label}" />
										</e:italic>
									</t:dataList>
								</h:panelGroup>
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup rendered="#{departmentsController.categoriesAction == 'MEMBERS'}" >
							<h:panelGroup rendered="#{not node.category.inheritMembers}">
								<h:panelGroup 
									style="cursor: pointer"
									onclick="showHideElement('categoriesForm:tree:#{node.identifier}:members');"
									rendered="#{node.membersNumber != 0}" >
									<e:bold value=" #{msgs['CATEGORIES.TEXT.MEMBERS_NUMBER']}">
										<f:param value="#{node.membersNumber}" />
									</e:bold>
									<t:graphicImage value="/media/images/show.png" />
								</h:panelGroup>
								<e:text rendered="#{node.membersNumber == 0}" 
									value=" #{msgs['CATEGORIES.TEXT.NO_MEMBER']}" />
							</h:panelGroup>
							<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/members.png"
									alt="#{msgs['CATEGORIES.ALT.EDIT_MEMBERS']}" 
									title="#{msgs['CATEGORIES.ALT.EDIT_MEMBERS']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:editMembersButton');" />
								<e:commandButton value="#{msgs['CATEGORIES.BUTTON.EDIT_MEMBERS']}" 
									id="editMembersButton" style="display: none"
									action="#{departmentsController.editCategoryMembers}" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToUpdate}" />
								</e:commandButton>
							</h:panelGroup>
							<h:panelGroup id="members" 
								rendered="#{not node.category.inheritMembers and node.membersNumber != 0}" 
								style="display: none" >
								<t:dataList value="#{node.members}" var="member" 
									rowIndexVar="memberIndex">
									<e:italic value="#{msgs['CATEGORIES.TEXT.MEMBER_SEPARATOR']}" 
										rendered="#{memberIndex != 0}" />
									<e:italic value="#{msgs['CATEGORIES.TEXT.MEMBER']}" >
										<f:param value="#{userFormatter[member.user]}" />
									</e:italic>
								</t:dataList>
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup rendered="#{departmentsController.categoriesAction == 'FAQ_LINKS'}" >
							<h:panelGroup rendered="#{not node.category.inheritFaqLinks}">
								<h:panelGroup 
									style="cursor: pointer"
									onclick="showHideElement('categoriesForm:tree:#{node.identifier}:faqLinks');"
									rendered="#{node.faqLinksNumber != 0}" >
									<e:bold value=" #{msgs['CATEGORIES.TEXT.FAQ_LINKS_NUMBER']}">
										<f:param value="#{node.faqLinksNumber}" />
									</e:bold>
									<t:graphicImage value="/media/images/show.png" />
								</h:panelGroup>
								<e:bold rendered="#{node.faqLinksNumber == 0}" 
									value=" #{msgs['CATEGORIES.TEXT.NO_FAQ_LINK']}" />
							</h:panelGroup>
							<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories}" >
								<e:bold value=" " />
								<t:graphicImage value="/media/images/faq-links.png"
									alt="#{msgs['CATEGORIES.ALT.EDIT_FAQ_LINKS']}" 
									title="#{msgs['CATEGORIES.ALT.EDIT_FAQ_LINKS']}" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:editFaqLinksButton');" />
								<e:commandButton value="#{msgs['CATEGORIES.BUTTON.EDIT_FAQ_LINKS']}" 
									id="editFaqLinksButton" style="display: none"
									action="#{departmentsController.editCategoryFaqLinks}" >
									<t:updateActionListener value="#{node.category}"
										property="#{departmentsController.categoryToUpdate}" />
								</e:commandButton>
							</h:panelGroup>
							<h:panelGroup id="faqLinks" 
								rendered="#{not node.category.inheritFaqLinks and node.faqLinksNumber != 0}" 
								style="display: none" >
								<t:dataList value="#{node.faqLinks}" var="faqLink" 
									rowIndexVar="faqLinkIndex">
									<t:htmlTag value="br" 
										rendered="#{faqLinkIndex != 0}" />
									<t:graphicImage value="/media/images/faq-link.png" />
									<e:italic value=" #{faqLink.faq.label}" />
								</t:dataList>
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGroup>
				</f:facet>
			</t:tree2>
		</h:panelGroup>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		hideButton("categoriesForm:cancelButton");
</script>
</e:page>
