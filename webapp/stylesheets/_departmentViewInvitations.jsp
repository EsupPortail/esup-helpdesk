<%@include file="_include.jsp"%>

<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
	<e:subSection value="#{msgs['DEPARTMENT_VIEW.HEADER.INVITATIONS']}" />
	<h:panelGroup>
		<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers}" >
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:addInvitationButton');" >
				<e:bold value="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_INVITATION']} " />
				<t:graphicImage value="/media/images/add.png"
					alt="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_INVITATION']}" 
					title="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_INVITATION']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="addInvitationButton" action="addInvitation"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_INVITATION']}" />
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<h:panelGroup
	rendered="#{not empty departmentsController.departmentInvitationPaginator.visibleItems}">
	<e:dataTable id="invitationData" rowIndexVar="variable" width="100%"
		value="#{departmentsController.departmentInvitationPaginator.visibleItems}"
		var="departmentInvitation" border="0" cellspacing="0" cellpadding="0" columnClasses="colLeftMax,colCenter">
		<f:facet name="header">
			<h:panelGroup>
				<h:panelGrid columns="3" columnClasses="colLeftMaxNowrap,colCenterNowrap,colRightNowrap"
					width="100%">
					<h:panelGroup>
						<e:text
							value="#{msgs['DEPARTMENT_VIEW.TEXT.INVITATIONS.PAGINATION']}">
							<f:param
								value="#{departmentsController.departmentInvitationPaginator.firstVisibleNumber + 1}" />
							<f:param
								value="#{departmentsController.departmentInvitationPaginator.lastVisibleNumber + 1}" />
							<f:param
								value="#{departmentsController.departmentInvitationPaginator.totalItemsCount}" />
						</e:text>
					</h:panelGroup>
					<h:panelGroup>
						<h:panelGroup
							rendered="#{departmentsController.departmentInvitationPaginator.lastPageNumber != 0}">
							<h:panelGroup
								rendered="#{not departmentsController.departmentInvitationPaginator.firstPage}">
								<t:graphicImage value="/media/images/page-first.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('departmentViewForm:invitationData:pageFirst');" />
								<e:commandButton id="pageFirst" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.FIRST']}"
									action="#{departmentsController.departmentInvitationPaginator.gotoFirstPage}" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/page-previous.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('departmentViewForm:invitationData:pagePrevious');" />
								<e:commandButton id="pagePrevious" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
									action="#{departmentsController.departmentInvitationPaginator.gotoPreviousPage}" />
							</h:panelGroup>
							<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
							<t:dataList
								value="#{departmentsController.departmentInvitationPaginator.nearPages}"
								var="page">
								<e:text value=" " />
								<e:italic value="#{page + 1}"
									rendered="#{page == departmentsController.departmentInvitationPaginator.currentPage}" />
								<h:commandLink value="#{page + 1}"
									rendered="#{page != departmentsController.departmentInvitationPaginator.currentPage}" >
									<t:updateActionListener value="#{page}"
										property="#{departmentsController.departmentInvitationPaginator.currentPage}" />
								</h:commandLink>
								<e:text value=" " />
							</t:dataList>
							<h:panelGroup
								rendered="#{not departmentsController.departmentInvitationPaginator.lastPage}">
								<t:graphicImage value="/media/images/page-next.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('departmentViewForm:invitationData:pageNext');" />
								<e:commandButton id="pageNext" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.NEXT']}"
									action="#{departmentsController.departmentInvitationPaginator.gotoNextPage}" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/page-last.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('departmentViewForm:invitationData:pageLast');" />
								<e:commandButton id="pageLast" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.LAST']}"
									action="#{departmentsController.departmentInvitationPaginator.gotoLastPage}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGroup>
					<h:panelGroup>
						<e:text value="#{msgs['DEPARTMENT_VIEW.TEXT.INVITATIONS.PER_PAGE']} " />
						<e:selectOneMenu onchange="javascript:{simulateLinkClick('departmentViewForm:invitationData:changeButton');}"
							value="#{departmentsController.departmentInvitationPaginator.pageSize}">
							<f:selectItems
								value="#{departmentsController.departmentInvitationPaginator.pageSizeItems}" />
						</e:selectOneMenu>
						<e:commandButton style="display: none" value="#{msgs['_.BUTTON.CHANGE']}"
							id="changeButton" action="#{departmentsController.departmentInvitationPaginator.forceReload}" />
					</h:panelGroup>
				</h:panelGrid>
				<t:htmlTag value="hr" />
			</h:panelGroup>
		</f:facet>
		<t:column >
			<e:text value="#{userFormatter[departmentInvitation.user]}" />
		</t:column>
		<t:column >
			<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers}" >
				<t:graphicImage value="/media/images/delete.png" 
					alt="x" title="x"
					style="cursor: pointer" 
					onclick="simulateLinkClick('departmentViewForm:invitationData:#{variable}:deleteInvitation');" />
				<e:commandButton value="x" id="deleteInvitation" style="display: none"
					action="#{departmentsController.deleteDepartmentInvitation}" >
					<t:updateActionListener value="#{departmentInvitation}"
						property="#{departmentsController.departmentInvitationToDelete}" />
				</e:commandButton>
			</h:panelGroup>
		</t:column>
		<f:facet name="footer">
			<t:htmlTag value="hr" />
		</f:facet>
	</e:dataTable>
</h:panelGroup>
<e:paragraph value="#{msgs['DEPARTMENT_VIEW.TEXT.INVITATIONS.NONE']}"
	rendered="#{empty departmentsController.departmentInvitationPaginator.visibleItems}" />
