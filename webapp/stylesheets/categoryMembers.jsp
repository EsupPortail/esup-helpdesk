 <%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="categoryMembersForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
			<e:section value="#{msgs['CATEGORY_MEMBERS.TITLE']}">
				<f:param value="#{departmentsController.categoryToUpdate.department.label}" />
				<f:param value="#{departmentsController.categoryToUpdate.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryMembersForm:cancelButton');" >
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

		<h:panelGroup rendered="#{departmentsController.categoryToUpdate.inheritMembers}" >
			<e:paragraph 
				value="#{msgs['CATEGORY_MEMBERS.TEXT.INHERIT_DEPARTMENT']}" 
				rendered="#{departmentsController.categoryToUpdate.parent == null}" />
			<e:paragraph 
				value="#{msgs['CATEGORY_MEMBERS.TEXT.INHERIT_CATEGORY']}" 
				rendered="#{departmentsController.categoryToUpdate.parent != null}" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryMembersForm:doNotInheritButton');" >
				<e:bold value="#{msgs['CATEGORY_MEMBERS.BUTTON.DO_NOT_INHERIT']} " />
				<t:graphicImage value="/media/images/cancel.png"
					alt="#{msgs['CATEGORY_MEMBERS.BUTTON.DO_NOT_INHERIT']}" 
					title="#{msgs['CATEGORY_MEMBERS.BUTTON.DO_NOT_INHERIT']}" />
			</h:panelGroup>
			<e:commandButton id="doNotInheritButton" style="display: none"
				action="#{departmentsController.toggleInheritMembers}"
				value="#{msgs['CATEGORY_MEMBERS.BUTTON.DO_NOT_INHERIT']}" />
			<e:paragraph 
				value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_INHERITED_MEMBER']}" 
				rendered="#{empty departmentsController.inheritedMembers}" />
			<e:paragraph 
				value="#{msgs['CATEGORY_MEMBERS.TEXT.INHERITED_MEMBERS']}" 
				rendered="#{not empty departmentsController.inheritedMembers}" />
			<t:dataList
				value="#{departmentsController.inheritedMembers}"
				var="departmentManager" >
				<e:li value="#{userFormatter[departmentManager.user]}" />
			</t:dataList>
		</h:panelGroup>
		<h:panelGroup rendered="#{not departmentsController.categoryToUpdate.inheritMembers}" >
			<h:panelGroup rendered="#{departmentsController.categoryToUpdate.parent == null}" >
				<e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_INHERIT_DEPARTMENT']}" />
				<h:panelGroup style="cursor: pointer" 
					onclick="simulateLinkClick('categoryMembersForm:inheritDepartmentButton');" >
					<e:bold value="#{msgs['CATEGORY_MEMBERS.BUTTON.INHERIT_DEPARTMENT']} " />
					<t:graphicImage value="/media/images/add.png" />
				</h:panelGroup>
				<e:commandButton id="inheritDepartmentButton" style="display: none"
					action="#{departmentsController.toggleInheritMembers}"
					value="#{msgs['CATEGORY_MEMBERS.BUTTON.INHERIT_DEPARTMENT']}" />
			</h:panelGroup>
			<h:panelGroup rendered="#{departmentsController.categoryToUpdate.parent != null}" >
				<e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_INHERIT_CATEGORY']}" />
				<h:panelGroup style="cursor: pointer" 
					onclick="simulateLinkClick('categoryMembersForm:inheritCategoryButton');" >
					<e:bold value="#{msgs['CATEGORY_MEMBERS.BUTTON.INHERIT_CATEGORY']} " />
					<t:graphicImage value="/media/images/add.png" />
				</h:panelGroup>
				<e:commandButton id="inheritCategoryButton" style="display: none"
					action="#{departmentsController.toggleInheritMembers}"
					value="#{msgs['CATEGORY_MEMBERS.BUTTON.INHERIT_CATEGORY']}" />
			</h:panelGroup>
			<e:paragraph 
				value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_MEMBER']}" 
				rendered="#{empty departmentsController.members}"
				/>
			<h:panelGroup rendered="#{not empty departmentsController.members}" >
				<e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.MEMBERS']}" />
				<e:dataTable
					id="memberData" rowIndexVar="variable"
					value="#{departmentsController.members}"
					var="member" border="0" cellspacing="0"
					cellpadding="0">
					<f:facet name="header">
						<h:panelGroup>
							<e:panelGrid rendered="#{departmentsController.membersNumber gt 1}" columns="2">
								<e:selectOneMenu onchange="javascript:{simulateLinkClick('categoryMembersForm:memberData:sortMembersButton');}"
									value="#{departmentsController.membersSortOrder}">
									<f:selectItem 
										itemLabel="#{msgs['CATEGORY_MEMBERS.MEMBERS.SORT.PROMPT']}"
										itemValue="" />
									<f:selectItem 
										itemLabel="#{msgs['CATEGORY_MEMBERS.MEMBERS.SORT.DISPLAY_NAME']}"
										itemValue="displayName" />
									<f:selectItem 
										itemLabel="#{msgs['CATEGORY_MEMBERS.MEMBERS.SORT.ID']}"
										itemValue="id" />
									<f:selectItem 
										itemLabel="#{msgs['CATEGORY_MEMBERS.MEMBERS.SORT.REVERSE']}"
										itemValue="reverse" />
								</e:selectOneMenu>
								<e:commandButton value="#{msgs['CATEGORY_MEMBERS.BUTTON.SORT_MEMBERS']}" 
									id="sortMembersButton" style="display: none"
									action="#{departmentsController.reorderMembers}" />
							</e:panelGrid>
							<t:htmlTag value="hr" />
						</h:panelGroup>
					</f:facet>
					<t:column>
						<e:bold value="#{userFormatter[member.user]}" />
					</t:column>
					<t:column>
						<h:panelGroup style="cursor: pointer" 
							onclick="simulateLinkClick('categoryMembersForm:memberData:#{variable}:deleteButton');" >
							<t:graphicImage value="/media/images/delete.png"
								alt="-" title="-" />
						</h:panelGroup>
						<e:commandButton id="deleteButton" value="-" style="display: none" 
							action="#{departmentsController.deleteCategoryMember}" >
							<t:updateActionListener value="#{member}"
								property="#{departmentsController.memberToDelete}" />
						</e:commandButton>
					</t:column>
					<t:column>
						<h:panelGroup
							rendered="#{variable != departmentsController.membersNumber - 1}" >
							<h:panelGroup style="cursor: pointer" 
								onclick="simulateLinkClick('categoryMembersForm:memberData:#{variable}:moveLastButton');" >
								<t:graphicImage value="/media/images/arrow_last.png"
									alt="vv" title="vv" />
							</h:panelGroup>
							<e:commandButton id="moveLastButton" value="vv" style="display: none" 
								action="#{departmentsController.moveCategoryMemberLast}" 
								>
								<t:updateActionListener value="#{member}"
									property="#{departmentsController.memberToMove}" />
							</e:commandButton>
						</h:panelGroup>
					</t:column>
					<t:column>
						<h:panelGroup
							rendered="#{variable != departmentsController.membersNumber - 1}" >
							<h:panelGroup style="cursor: pointer" 
								onclick="simulateLinkClick('categoryMembersForm:memberData:#{variable}:moveDownButton');" >
								<t:graphicImage value="/media/images/arrow_down.png"
									alt="v" title="v" />
							</h:panelGroup>
							<e:commandButton id="moveDownButton" value="v" style="display: none" 
								action="#{departmentsController.moveCategoryMemberDown}" 
								>
								<t:updateActionListener value="#{member}"
									property="#{departmentsController.memberToMove}" />
							</e:commandButton>
						</h:panelGroup>
					</t:column>
					<t:column>
						<h:panelGroup
							rendered="#{variable != 0}" >
							<h:panelGroup style="cursor: pointer" 
								onclick="simulateLinkClick('categoryMembersForm:memberData:#{variable}:moveUpButton');" >
								<t:graphicImage value="/media/images/arrow_up.png"
									alt="^" title="^" />
							</h:panelGroup>
							<e:commandButton id="moveUpButton" value="^" style="display: none" 
								action="#{departmentsController.moveCategoryMemberUp}" 
								>
								<t:updateActionListener value="#{member}"
									property="#{departmentsController.memberToMove}" />
							</e:commandButton>
						</h:panelGroup>
					</t:column>
					<t:column>
						<h:panelGroup
							rendered="#{variable != 0}" >
							<h:panelGroup style="cursor: pointer" 
								onclick="simulateLinkClick('categoryMembersForm:memberData:#{variable}:moveFirstButton');" >
								<t:graphicImage value="/media/images/arrow_first.png"
									alt="^^" title="^^" />
							</h:panelGroup>
							<e:commandButton id="moveFirstButton" value="^^" style="display: none" 
								action="#{departmentsController.moveCategoryMemberFirst}" 
								>
								<t:updateActionListener value="#{member}"
									property="#{departmentsController.memberToMove}" />
							</e:commandButton>
						</h:panelGroup>
					</t:column>
					<f:facet name="footer">
						<t:htmlTag value="hr" />
					</f:facet>
				</e:dataTable>
			</h:panelGroup>
			<e:paragraph 
				value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_NOT_MEMBER']}" 
				rendered="#{empty departmentsController.notMembers}"
				/>
			<h:panelGroup rendered="#{not empty departmentsController.notMembers}" >
				<e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.NOT_MEMBERS']}" />
				<e:dataTable
					id="notMemberData" rowIndexVar="variable"
					value="#{departmentsController.notMembers}"
					var="departmentManager" border="0" cellspacing="0"
					cellpadding="0">
					<f:facet name="header">
						<h:panelGroup>
							<e:panelGrid rendered="#{departmentsController.notMembersNumber gt 1}" >
								<e:selectOneMenu onchange="javascript:{simulateLinkClick('categoryMembersForm:notMemberData:sortNotMembersButton');}"
									value="#{departmentsController.notMembersPresentOrder}">
									<f:selectItem 
										itemLabel="#{msgs['CATEGORY_MEMBERS.NOT_MEMBERS.SORT.ORDER']}"
										itemValue="order" />
									<f:selectItem 
										itemLabel="#{msgs['CATEGORY_MEMBERS.NOT_MEMBERS.SORT.DISPLAY_NAME']}"
										itemValue="displayName" />
									<f:selectItem 
										itemLabel="#{msgs['CATEGORY_MEMBERS.NOT_MEMBERS.SORT.ID']}"
										itemValue="id" />
								</e:selectOneMenu>
								<e:commandButton value="#{msgs['CATEGORY_MEMBERS.BUTTON.SORT_NOT_MEMBERS']}" 
									id="sortNotMembersButton" style="display: none"
									action="#{departmentsController.resetMembers}" />
							</e:panelGrid>
							<t:htmlTag value="hr" />
						</h:panelGroup>
					</f:facet>
					<t:column>
						<e:bold value="#{userFormatter[departmentManager.user]}" />
					</t:column>
					<t:column>
						<h:panelGroup style="cursor: pointer" 
							onclick="simulateLinkClick('categoryMembersForm:notMemberData:#{variable}:addButton');" >
							<t:graphicImage value="/media/images/add.png"
								alt="+" title="+" />
						</h:panelGroup>
						<e:commandButton id="addButton" value="+" style="display: none" 
							action="#{departmentsController.addCategoryMember}" >
							<t:updateActionListener value="#{departmentManager.user}"
								property="#{departmentsController.memberToAdd}" />
						</e:commandButton>
					</t:column>
					<f:facet name="footer">
						<t:htmlTag value="hr" />
					</f:facet>
				</e:dataTable>
				<h:panelGroup style="cursor: pointer" 
					onclick="simulateLinkClick('categoryMembersForm:addAllButton');" >
					<e:bold value="#{msgs['CATEGORY_MEMBERS.BUTTON.ADD_ALL']} " />
					<t:graphicImage value="/media/images/add.png"
						alt="+*" title="+*" />
				</h:panelGroup>
				<e:commandButton id="addAllButton" value="+*" style="display: none" 
					action="#{departmentsController.addAllCategoryMembers}" />
			</h:panelGroup>
		</h:panelGroup>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
