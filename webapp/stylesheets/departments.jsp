<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}" >
	<%@include file="_navigation.jsp"%>

<script type="text/javascript">
	function selectDepartment(index) {
		simulateLinkClick('departmentsForm:data:' + index + ':selectDepartmentButton');
	}
</script>

	<h:panelGroup rendered="#{not departmentsController.currentUserCanViewDepartments}" >
		<h:panelGroup rendered="#{departmentsController.currentUser == null}" >
			<%@include file="_auth.jsp"%>
		</h:panelGroup>
		<h:panelGroup rendered="#{departmentsController.currentUser != null}" >
			<e:messages/>
		</h:panelGroup>
	</h:panelGroup>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="departmentsForm" rendered="#{departmentsController.currentUserCanViewDepartments}" >
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%"
			cellspacing="0" cellpadding="0">
			<e:section value="#{msgs['DEPARTMENTS.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartments}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentsForm:addDepartmentButton');" >
						<e:bold value="#{msgs['DEPARTMENTS.BUTTON.ADD_DEPARTMENT']} " />
						<t:graphicImage value="/media/images/add.png"
							alt="#{msgs['DEPARTMENTS.BUTTON.ADD_DEPARTMENT']}" 
							title="#{msgs['DEPARTMENTS.BUTTON.ADD_DEPARTMENT']}" />
					</h:panelGroup>
					<e:commandButton style="display: none" id="addDepartmentButton" action="addDepartment"
						value="#{msgs['DEPARTMENTS.BUTTON.ADD_DEPARTMENT']}" />
				</h:panelGroup>
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:dataTable id="data" value="#{departmentsController.departmentPaginator.visibleItems}"
			var="department" rowIndexVar="variable" border="0"
			style="width: 100%" cellspacing="0" cellpadding="0"
			rowStyle="cursor: pointer"  
			rowId="#{department.id}"
			rendered="#{not empty departmentsController.departmentPaginator.visibleItems}">
			<f:facet name="header">
				<h:panelGroup>
					<h:panelGrid columns="3" columnClasses="colLeft,colCenter,colRight" width="100%" >
						<h:panelGroup>
							<e:text value="#{msgs['DEPARTMENTS.TEXT.DEPARTMENTS']}">
								<f:param
									value="#{departmentsController.departmentPaginator.firstVisibleNumber + 1}" />
								<f:param
									value="#{departmentsController.departmentPaginator.lastVisibleNumber + 1}" />
								<f:param
									value="#{departmentsController.departmentPaginator.totalItemsCount}" />
							</e:text>
						</h:panelGroup>
						<h:panelGroup>
							<h:panelGroup rendered="#{departmentsController.departmentPaginator.lastPageNumber != 0}" >
								<h:panelGroup rendered="#{not departmentsController.departmentPaginator.firstPage}" >
									<t:graphicImage value="/media/images/page-first.png" 
										style="cursor: pointer" 
										onclick="simulateLinkClick('departmentsForm:data:pageFirst');" />
									<e:commandButton id="pageFirst" style="display: none" 
										value="#{msgs['PAGINATION.BUTTON.FIRST']}"
										action="#{departmentsController.departmentPaginator.gotoFirstPage}" />
									<e:text value=" " />
									<t:graphicImage value="/media/images/page-previous.png" 
										style="cursor: pointer" 
										onclick="simulateLinkClick('departmentsForm:data:pagePrevious');" />
									<e:commandButton id="pagePrevious" style="display: none" 
										value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
										action="#{departmentsController.departmentPaginator.gotoPreviousPage}" />
								</h:panelGroup>
								<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
								<t:dataList value="#{departmentsController.departmentPaginator.nearPages}" var="page">
									<e:text value=" " />
									<e:italic value="#{page + 1}" rendered="#{page == departmentsController.departmentPaginator.currentPage}" />
									<h:commandLink value="#{page + 1}" 
										rendered="#{page != departmentsController.departmentPaginator.currentPage}" >
										<t:updateActionListener
											value="#{page}"
											property="#{departmentsController.departmentPaginator.currentPage}" />
									</h:commandLink>
									<e:text value=" " />
								</t:dataList>
								<h:panelGroup rendered="#{not departmentsController.departmentPaginator.lastPage}" >
									<t:graphicImage value="/media/images/page-next.png" 
										style="cursor: pointer" 
										onclick="simulateLinkClick('departmentsForm:data:pageNext');" />
									<e:commandButton id="pageNext" style="display: none" 
										value="#{msgs['PAGINATION.BUTTON.NEXT']}" 
										action="#{departmentsController.departmentPaginator.gotoNextPage}" />
									<e:text value=" " />
									<t:graphicImage value="/media/images/page-last.png" 
										style="cursor: pointer" 
										onclick="simulateLinkClick('departmentsForm:data:pageLast');" />
									<e:commandButton id="pageLast" style="display: none" 
										value="#{msgs['PAGINATION.BUTTON.LAST']}"
										action="#{departmentsController.departmentPaginator.gotoLastPage}" />
								</h:panelGroup>
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup>
							<e:text value="#{msgs['DEPARTMENTS.TEXT.DEPARTMENTS_PER_PAGE']} " />
							<e:selectOneMenu onchange="javascript:{simulateLinkClick('departmentsForm:data:changeButton');}"
								value="#{departmentsController.departmentPaginator.pageSize}" >
								<f:selectItems value="#{departmentsController.departmentPaginator.pageSizeItems}" />
							</e:selectOneMenu>
							<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}" id="changeButton" 
								action="#{departmentsController.departmentPaginator.forceReload}" />
							<h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartments}" >
								<e:selectOneMenu onchange="javascript:{simulateLinkClick('departmentsForm:data:sortDepartmentsButton');}"
									value="#{departmentsController.departmentsSortOrder}">
									<f:selectItem 
										itemLabel="#{msgs['DEPARTMENTS.TEXT.SORT.PROMPT']}"
										itemValue="" />
									<f:selectItem 
										itemLabel="#{msgs['DEPARTMENTS.TEXT.SORT.LABEL']}"
										itemValue="label" />
									<f:selectItem 
										itemLabel="#{msgs['DEPARTMENTS.TEXT.SORT.XLABEL']}"
										itemValue="xlabel" />
									<f:selectItem 
										itemLabel="#{msgs['DEPARTMENTS.TEXT.SORT.REVERSE']}"
										itemValue="reverse" />
								</e:selectOneMenu>
								<e:commandButton value="#{msgs['DEPARTMENTS.BUTTON.SORT_DEPARTMENTS']}" 
									id="sortDepartmentsButton" style="display: none"
									action="#{departmentsController.reorderDepartments}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGrid>
					<t:htmlTag value="hr" />
				</h:panelGroup>
			</f:facet>
			<t:column style="cursor: pointer" onclick="selectDepartment(#{variable});" >
				<t:graphicImage value="#{departmentIconUrlProvider[department]}" />
			</t:column>
			<t:column style="cursor: pointer" onclick="selectDepartment(#{variable});" >
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENTS.TEXT.HEADER.LABEL']}" />
				</f:facet>
				<e:text value="#{department.label}" />
			</t:column>
			<t:column style="cursor: pointer" onclick="selectDepartment(#{variable});" >
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENTS.TEXT.HEADER.XLABEL']}" />
				</f:facet>
				<e:text value="#{department.xlabel}" />
			</t:column>
			<t:column style="cursor: pointer" onclick="selectDepartment(#{variable});" >
				<e:text 
					value="#{msgs[department.enabled ? 'DEPARTMENTS.TEXT.ENABLED.TRUE' : 'DEPARTMENTS.TEXT.ENABLED.FALSE']}"
					style="color: #{department.enabled ? '#007f00' : '#ff0000'}" />
			</t:column>
			<t:column style="cursor: pointer" onclick="selectDepartment(#{variable});" >
				<e:text 
					value="#{msgs['DEPARTMENTS.TEXT.REDIRECTION']}"
					rendered="#{department.virtual}" >
					<f:param
						value="#{department.realDepartment.label}" />
				</e:text>
			</t:column>
			<t:column style="cursor: default" rendered="#{departmentsController.currentUserCanManageDepartments}" >
				<h:panelGroup rendered="#{not departmentsController.departmentPaginator.firstPage or variable != 0}">
					<t:graphicImage value="/media/images/arrow_first.png" 
						style="cursor: pointer" 
						alt="^^" title="^^"
						onclick="simulateLinkClick('departmentsForm:data:#{variable}:moveDepartmentFirst');" />
					<e:commandButton value="^^" id="moveDepartmentFirst" style="display: none"
						action="#{departmentsController.moveDepartmentFirst}" >
						<t:updateActionListener value="#{department}"
							property="#{departmentsController.departmentToUpdate}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: default" rendered="#{departmentsController.currentUserCanManageDepartments}" >
				<h:panelGroup rendered="#{not departmentsController.departmentPaginator.firstPage or variable != 0}">
					<t:graphicImage value="/media/images/arrow_up.png" 
						style="cursor: pointer" 
						alt="^" title="^"
						onclick="simulateLinkClick('departmentsForm:data:#{variable}:moveDepartmentUp');" />
					<e:commandButton value="^" id="moveDepartmentUp" style="display: none"
						action="#{departmentsController.moveDepartmentUp}" >
						<t:updateActionListener value="#{department}"
							property="#{departmentsController.departmentToUpdate}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: default" rendered="#{departmentsController.currentUserCanManageDepartments}" >
				<h:panelGroup rendered="#{not departmentsController.departmentPaginator.lastPage or variable != departmentsController.departmentPaginator.visibleItemsCount - 1}">
					<t:graphicImage value="/media/images/arrow_down.png" 
						style="cursor: pointer" 
						alt="v" title="v"
						onclick="simulateLinkClick('departmentsForm:data:#{variable}:moveDepartmentDown');" />
					<e:commandButton value="v" id="moveDepartmentDown" style="display: none"
						action="#{departmentsController.moveDepartmentDown}" >
						<t:updateActionListener value="#{department}"
							property="#{departmentsController.departmentToUpdate}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: default" rendered="#{departmentsController.currentUserCanManageDepartments}" >
				<h:panelGroup rendered="#{not departmentsController.departmentPaginator.lastPage or variable != departmentsController.departmentPaginator.visibleItemsCount - 1}">
					<t:graphicImage value="/media/images/arrow_last.png" 
						alt="vv" title="vv"
						style="cursor: pointer" 
						onclick="simulateLinkClick('departmentsForm:data:#{variable}:moveDepartmentLast');" />
					<e:commandButton value="vv" id="moveDepartmentLast" style="display: none"
						action="#{departmentsController.moveDepartmentLast}" >
						<t:updateActionListener value="#{department}"
							property="#{departmentsController.departmentToUpdate}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
			<t:column >
				<e:commandButton style="display: none"
					value="#{msgs['_.BUTTON.VIEW_EDIT']}" id="selectDepartmentButton"
					action="#{departmentsController.viewDepartment}" immediate="true">
					<t:updateActionListener value="#{department}"
						property="#{departmentsController.department}" />
				</e:commandButton>
			</t:column>
			<f:facet name="footer">
				<t:htmlTag value="hr" />
			</f:facet>
		</e:dataTable>
		<e:text value="#{msgs['DEPARTMENTS.TEXT.NO_DEPARTMENT']}"
			rendered="#{empty departmentsController.departmentPaginator.visibleItems}" />
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{departmentsController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
highlightTableRows("departmentsForm:data");
hideButton("departmentsForm:data:changeButton");
</script>
</e:page>
