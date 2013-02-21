<%@include file="_include.jsp"%>

<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
	<e:subSection value="#{msgs['DEPARTMENT_VIEW.HEADER.MANAGERS']}" />
	<h:panelGroup>
		<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers}" >
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:addManagerButton');" >
				<e:bold value="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']} " />
				<t:graphicImage value="/media/images/add.png"
					alt="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']}" 
					title="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']}" />
			</h:panelGroup>
			<e:commandButton style="display: none" id="addManagerButton" action="addManager"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']}" />
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<h:panelGroup
	rendered="#{not empty departmentsController.departmentManagerPaginator.visibleItems}">
	<e:dataTable id="data" rowIndexVar="variable" width="100%"
		value="#{departmentsController.departmentManagerPaginator.visibleItems}"
		var="departmentManager" border="0" cellspacing="0" cellpadding="0" 
		columnClasses="colLeftMax,colCenter,colCenter,colCenter,colCenter,colCenter,colCenter">
		<f:facet name="header">
			<h:panelGroup>
				<h:panelGrid columns="3" columnClasses="colLeftMaxNowrap,colCenterNowrap,colRightNowrap,colCenter,colCenter"
					width="100%">
					<h:panelGroup>
						<e:text
							value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.PAGINATION']}">
							<f:param
								value="#{departmentsController.departmentManagerPaginator.firstVisibleNumber + 1}" />
							<f:param
								value="#{departmentsController.departmentManagerPaginator.lastVisibleNumber + 1}" />
							<f:param
								value="#{departmentsController.departmentManagerPaginator.totalItemsCount}" />
						</e:text>
					</h:panelGroup>
					<h:panelGroup>
						<h:panelGroup
							rendered="#{departmentsController.departmentManagerPaginator.lastPageNumber != 0}">
							<h:panelGroup
								rendered="#{not departmentsController.departmentManagerPaginator.firstPage}">
								<t:graphicImage value="/media/images/page-first.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('departmentViewForm:data:pageFirst');" />
								<e:commandButton id="pageFirst" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.FIRST']}"
									action="#{departmentsController.departmentManagerPaginator.gotoFirstPage}" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/page-previous.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('departmentViewForm:data:pagePrevious');" />
								<e:commandButton id="pagePrevious" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
									action="#{departmentsController.departmentManagerPaginator.gotoPreviousPage}" />
							</h:panelGroup>
							<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
							<t:dataList
								value="#{departmentsController.departmentManagerPaginator.nearPages}"
								var="page">
								<e:text value=" " />
								<e:italic value="#{page + 1}"
									rendered="#{page == departmentsController.departmentManagerPaginator.currentPage}" />
								<h:commandLink value="#{page + 1}"
									rendered="#{page != departmentsController.departmentManagerPaginator.currentPage}" >
									<t:updateActionListener value="#{page}"
										property="#{departmentsController.departmentManagerPaginator.currentPage}" />
								</h:commandLink>
								<e:text value=" " />
							</t:dataList>
							<h:panelGroup
								rendered="#{not departmentsController.departmentManagerPaginator.lastPage}">
								<t:graphicImage value="/media/images/page-next.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('departmentViewForm:data:pageNext');" />
								<e:commandButton id="pageNext" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.NEXT']}"
									action="#{departmentsController.departmentManagerPaginator.gotoNextPage}" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/page-last.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('departmentViewForm:data:pageLast');" />
								<e:commandButton id="pageLast" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.LAST']}"
									action="#{departmentsController.departmentManagerPaginator.gotoLastPage}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGroup>
					<h:panelGroup>
						<e:text value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.PER_PAGE']} " />
						<e:selectOneMenu onchange="javascript:{simulateLinkClick('departmentViewForm:data:changeButton');}"
							value="#{departmentsController.departmentManagerPaginator.pageSize}">
							<f:selectItems
								value="#{departmentsController.departmentManagerPaginator.pageSizeItems}" />
						</e:selectOneMenu>
						<e:commandButton style="display: none" value="#{msgs['_.BUTTON.CHANGE']}"
							id="changeButton" action="#{departmentsController.departmentManagerPaginator.forceReload}" />
						<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers}" >
							<e:selectOneMenu onchange="javascript:{simulateLinkClick('departmentViewForm:data:sortManagersButton');}"
								value="#{departmentsController.managersSortOrder}">
								<f:selectItem 
									itemLabel="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.SORT.PROMPT']}"
									itemValue="" />
								<f:selectItem 
									itemLabel="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.SORT.DISPLAY_NAME']}"
									itemValue="displayName" />
								<f:selectItem 
									itemLabel="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.SORT.ID']}"
									itemValue="id" />
								<f:selectItem 
									itemLabel="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.SORT.REVERSE']}"
									itemValue="reverse" />
							</e:selectOneMenu>
							<e:commandButton value="#{msgs['DEPARTMENT_VIEW.BUTTON.SORT_MANAGERS']}" 
								id="sortManagersButton" style="display: none"
								action="#{departmentsController.reorderManagers}" />
						</h:panelGroup>
					</h:panelGroup>
				</h:panelGrid>
				<t:htmlTag value="hr" />
			</h:panelGroup>
		</f:facet>
		<t:column style="cursor: pointer" onclick="selectManager(#{variable});" >
			<e:text value="#{userFormatter[departmentManager.user]}" />
		</t:column>
		<t:column style="cursor: default" >
			<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers and variable != 0}" >
				<t:graphicImage value="/media/images/arrow_first.png" 
					alt="^^" title="^^"
					style="cursor: pointer" 
					onclick="simulateLinkClick('departmentViewForm:data:#{variable}:moveManagerFirst');" />
				<e:commandButton value="^^" id="moveManagerFirst" style="display: none"
					action="#{departmentsController.moveDepartmentManagerFirst}" 
					>
					<t:updateActionListener value="#{departmentManager}"
						property="#{departmentsController.departmentManagerToUpdate}" />
				</e:commandButton>
			</h:panelGroup>
		</t:column>
		<t:column style="cursor: default" >
			<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers and variable != 0}" >
				<t:graphicImage value="/media/images/arrow_up.png" 
					alt="^" title="^"
					style="cursor: pointer" 
					onclick="simulateLinkClick('departmentViewForm:data:#{variable}:moveManagerUp');" />
				<e:commandButton value="^^" id="moveManagerUp" style="display: none"
					action="#{departmentsController.moveDepartmentManagerUp}" 
					>
					<t:updateActionListener value="#{departmentManager}"
						property="#{departmentsController.departmentManagerToUpdate}" />
				</e:commandButton>
			</h:panelGroup>
		</t:column>
		<t:column style="cursor: default" >
			<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers and variable != departmentsController.managersNumber - 1}" >
				<t:graphicImage value="/media/images/arrow_down.png" 
					alt="v" title="v"
					style="cursor: pointer" 
					onclick="simulateLinkClick('departmentViewForm:data:#{variable}:moveManagerDown');" />
				<e:commandButton value="v" id="moveManagerDown" style="display: none"
					action="#{departmentsController.moveDepartmentManagerDown}" 
					>
					<t:updateActionListener value="#{departmentManager}"
						property="#{departmentsController.departmentManagerToUpdate}" />
				</e:commandButton>
			</h:panelGroup>
		</t:column>
		<t:column style="cursor: default" >
			<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers and variable != departmentsController.managersNumber - 1}" >
				<t:graphicImage value="/media/images/arrow_last.png" 
					alt="vv" title="vv"
					style="cursor: pointer" 
					onclick="simulateLinkClick('departmentViewForm:data:#{variable}:moveManagerLast');" />
				<e:commandButton value="vv" id="moveManagerLast" style="display: none"
					action="#{departmentsController.moveDepartmentManagerLast}" 
					>
					<t:updateActionListener value="#{departmentManager}"
						property="#{departmentsController.departmentManagerToUpdate}" />
				</e:commandButton>
			</h:panelGroup>
		</t:column>
		<t:column style="cursor: default" >
			<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentManagers}" >
				<t:graphicImage value="/media/images/delete.png" 
					alt="x" title="x"
					style="cursor: pointer" 
					onclick="simulateLinkClick('departmentViewForm:data:#{variable}:deleteManager');" />
				<e:commandButton value="x" id="deleteManager" style="display: none"
					action="#{departmentsController.deleteDepartmentManager}" >
					<t:updateActionListener value="#{departmentManager}"
						property="#{departmentsController.departmentManagerToUpdate}" />
				</e:commandButton>
			</h:panelGroup>
		</t:column>
		<t:column style="cursor: pointer" >
			<e:commandButton style="display: none" 
				value="#{msgs['_.BUTTON.VIEW_EDIT']}"
				id="selectManager" action="editDepartmentManager">
				<t:updateActionListener value="#{departmentManager}"
					property="#{departmentsController.departmentManagerToUpdate}" />
			</e:commandButton>
		</t:column>
		<f:facet name="footer">
			<t:htmlTag value="hr" />
		</f:facet>
	</e:dataTable>
	<e:paragraph value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.NOTE']}" />
</h:panelGroup>
<e:paragraph value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.NONE']}"
	rendered="#{empty departmentsController.departmentManagerPaginator.visibleItems}" />
