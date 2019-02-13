<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="administrators"
	locale="#{sessionController.locale}" >

<script type="text/javascript">
	function editIcon(index) {
		simulateLinkClick('administratorsForm:iconData:' + index + ':editIconButton');
	}
</script>

	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{not administratorsController.pageAuthorized}" >
		<h:panelGroup rendered="#{administratorsController.currentUser == null}" >
			<%@include file="_auth.jsp"%>
		</h:panelGroup>
		<h:panelGroup rendered="#{administratorsController.currentUser != null}" >
			<e:messages/>
		</h:panelGroup>
	</h:panelGroup>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="administratorsForm" rendered="#{administratorsController.pageAuthorized}" >
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%"
			cellspacing="0" cellpadding="0">
			<e:section value="#{msgs['ADMINISTRATORS.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup rendered="#{administratorsController.currentUserCanAddAdmin}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('administratorsForm:addAdminButton');" >
						<e:bold value="#{msgs['ADMINISTRATORS.BUTTON.ADD_ADMIN']} " />
						<t:graphicImage value="/media/images/add.png"
							alt="#{msgs['ADMINISTRATORS.BUTTON.ADD_ADMIN']}" 
							title="#{msgs['ADMINISTRATORS.BUTTON.ADD_ADMIN']}" />
					</h:panelGroup>
					<e:commandButton style="display: none" id="addAdminButton" action="addAdmin"
						value="#{msgs['ADMINISTRATORS.BUTTON.ADD_ADMIN']}" />
				</h:panelGroup>
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:dataTable
			rendered="#{not empty administratorsController.paginator.visibleItems}"
			id="data" rowIndexVar="variable" width="100%"
			value="#{administratorsController.paginator.visibleItems}"
			var="admin" border="0" cellspacing="0"
			cellpadding="0">
			<f:facet name="header">
				<h:panelGroup>
					<h:panelGrid columns="3" columnClasses="colLeft,,colRight"
						width="100%">
						<h:panelGroup>
							<e:text value="#{msgs['ADMINISTRATORS.TEXT.ADMINISTRATORS']}">
								<f:param
									value="#{administratorsController.paginator.firstVisibleNumber + 1}" />
								<f:param
									value="#{administratorsController.paginator.lastVisibleNumber + 1}" />
								<f:param
									value="#{administratorsController.paginator.totalItemsCount}" />
							</e:text>
						</h:panelGroup>
						<h:panelGroup
							rendered="#{administratorsController.paginator.lastPageNumber == 0}" />
						<h:panelGroup
							rendered="#{administratorsController.paginator.lastPageNumber != 0}">
							<h:panelGroup
								rendered="#{not administratorsController.paginator.firstPage}">
								<t:graphicImage value="/media/images/page-first.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('administratorsForm:data:pageFirst');" />
								<e:commandButton id="pageFirst" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.FIRST']}"
									action="#{administratorsController.paginator.gotoFirstPage}" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/page-previous.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('administratorsForm:data:pagePrevious');" />
								<e:commandButton id="pagePrevious" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
									action="#{administratorsController.paginator.gotoPreviousPage}" />
							</h:panelGroup>
							<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
							<t:dataList
								value="#{administratorsController.paginator.nearPages}"
								var="page">
								<e:text value=" " />
								<e:italic value="#{page + 1}"
									rendered="#{page == administratorsController.paginator.currentPage}" />
								<h:commandLink value="#{page + 1}"
									rendered="#{page != administratorsController.paginator.currentPage}" >
									<t:updateActionListener value="#{page}"
										property="#{administratorsController.paginator.currentPage}" />
								</h:commandLink>
								<e:text value=" " />
							</t:dataList>
							<h:panelGroup
								rendered="#{not administratorsController.paginator.lastPage}">
								<t:graphicImage value="/media/images/page-next.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('administratorsForm:data:pageNext');" />
								<e:commandButton id="pageNext" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.NEXT']}"
									action="#{administratorsController.paginator.gotoNextPage}" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/page-last.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('administratorsForm:data:pageLast');" />
								<e:commandButton id="pageLast" style="display: none" 
									value="#{msgs['PAGINATION.BUTTON.LAST']}"
									action="#{administratorsController.paginator.gotoLastPage}" />
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup>
							<e:text
								value="#{msgs['ADMINISTRATORS.TEXT.ADMINISTRATORS_PER_PAGE']} " />
							<e:selectOneMenu onchange="javascript:{simulateLinkClick('administratorsForm:data:changeButton');}"
								value="#{administratorsController.paginator.pageSize}">
								<f:selectItems
									value="#{administratorsController.paginator.pageSizeItems}" />
							</e:selectOneMenu>
							<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}" 
								style="display: none"
								action="#{administratorsController.paginator.forceReload}" />
						</h:panelGroup>
					</h:panelGrid>
					<t:htmlTag value="hr" />
				</h:panelGroup>
			</f:facet>
			<t:column>
				<e:bold value="#{userFormatter[admin]}" />
			</t:column>
			<t:column style="text-align: right;">
				<h:panelGroup rendered="#{administratorsController.currentUser.admin and administratorsController.currentUser.id != admin.id}" >
					<t:graphicImage value="/media/images/delete.png" 
						style="cursor: pointer" 
						alt="#{msgs['ADMINISTRATORS.BUTTON.DELETE_ADMIN']}"
						title="#{msgs['ADMINISTRATORS.BUTTON.DELETE_ADMIN']}"
						onclick="simulateLinkClick('administratorsForm:data:#{variable}:deleteAdminButton');" />
					<e:commandButton id="deleteAdminButton" action="deleteAdmin" style="display: none" 
						value="#{msgs['ADMINISTRATORS.BUTTON.DELETE_ADMIN']}">
						<t:updateActionListener value="#{admin}"
							property="#{administratorsController.userToDelete}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
			<f:facet name="footer">
				<t:htmlTag value="hr" />
			</f:facet>
		</e:dataTable>

		<e:panelGrid columns="3" columnClasses="colLeft,colLeft,colLeft" width="100%">
			<h:panelGroup >
				<e:subSection value="#{msgs['ADMINISTRATORS.HEADER.INDEXING_STATISTICS']}" />
				<e:panelGrid columns="2" columnClasses="colLeft,colRight"
					alternateColors="true" cellpadding="0" cellspacing="0" >
					<f:facet name="header">
						<t:htmlTag value="hr" />
					</f:facet>
					<f:facet name="footer">
						<t:htmlTag value="hr" />
					</f:facet>
					<e:bold value="#{msgs['ADMINISTRATORS.TEXT.INDEXING_STATISTICS.TOTAL_NUMBER']} " />
					<e:bold value=" #{administratorsController.indexingDocumentsNumber}" >
						<f:convertDateTime timeZone="#{sessionController.timezone}" type="both" dateStyle="short" locale="#{sessionController.locale} "/>
					</e:bold>
					<e:text value="#{msgs['ADMINISTRATORS.TEXT.INDEXING_STATISTICS.TICKETS_NUMBER']} " />
					<e:bold value=" #{administratorsController.indexingTicketsNumber}" />
					<e:text value="#{msgs['ADMINISTRATORS.TEXT.INDEXING_STATISTICS.TICKETS_LAST_UPDATE']} " />
					<h:panelGroup>
						<e:text escape="false" value="&nbsp;&nbsp;&nbsp;" />
						<e:text value=" #{administratorsController.ticketsLastIndexTime}" >
							<f:convertDateTime timeZone="#{sessionController.timezone}" type="both" dateStyle="short" locale="#{sessionController.locale} "/>
						</e:text>
					</h:panelGroup>
					<e:bold value="#{msgs['ADMINISTRATORS.TEXT.INDEXING_STATISTICS.ARCHIVED_TICKETS_NUMBER']} " />
					<e:bold value=" #{administratorsController.indexingArchivedTicketsNumber}" />
					<e:text value="#{msgs['ADMINISTRATORS.TEXT.INDEXING_STATISTICS.ARCHIVED_TICKETS_LAST_UPDATE']} " />
					<h:panelGroup>
						<e:text escape="false" value="&nbsp;&nbsp;&nbsp;" />
						<e:text value=" #{administratorsController.archivedTicketsLastIndexTime}" >
							<f:convertDateTime timeZone="#{sessionController.timezone}" type="both" dateStyle="short" locale="#{sessionController.locale} "/>
						</e:text>
					</h:panelGroup>
					<e:bold value="#{msgs['ADMINISTRATORS.TEXT.INDEXING_STATISTICS.FAQS_NUMBER']} " />
					<e:bold value=" #{administratorsController.indexingFaqsNumber}" />
					<e:text value="#{msgs['ADMINISTRATORS.TEXT.INDEXING_STATISTICS.FAQS_LAST_UPDATE']} " />
					<h:panelGroup>
						<e:text escape="false" value="&nbsp;&nbsp;&nbsp;" />
						<e:text value=" #{administratorsController.faqsLastIndexTime}" >
							<f:convertDateTime timeZone="#{sessionController.timezone}" type="both" dateStyle="short" locale="#{sessionController.locale} "/>
						</e:text>
					</h:panelGroup>
				</e:panelGrid>
			</h:panelGroup>
			<h:panelGroup>
				<h:panelGroup rendered="#{domainService.useLdap}" >
					<e:subSection value="#{msgs['ADMINISTRATORS.HEADER.LDAP_STATISTICS']}" />
					<h:panelGroup
						rendered="#{not empty administratorsController.ldapStatistics}">
						<e:dataTable value="#{administratorsController.ldapStatistics}"
							var="string" border="0" cellspacing="0"
							cellpadding="0">
							<f:facet name="header">
								<h:panelGroup>
									<t:htmlTag value="hr" />
								</h:panelGroup>
							</f:facet>
							<t:column>
								<e:bold value="#{string}" />
							</t:column>
							<f:facet name="footer">
								<t:htmlTag value="hr" />
							</f:facet>
						</e:dataTable>
					</h:panelGroup>
					<e:subSection
						value="#{msgs['ADMINISTRATORS.TEXT.LDAP_STATISTICS.NONE']}"
						rendered="#{empty administratorsController.ldapStatistics}" />
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup>
				<e:panelGrid 
					columns="2" columnClasses="colLeft,colRight" width="100%"
					cellspacing="0" cellpadding="0">
					<e:subSection value="#{msgs['ADMINISTRATORS.HEADER.ICONS']}" />
					<h:panelGroup>
						<h:panelGroup rendered="#{administratorsController.currentUserCanManageIcons}" >
							<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('administratorsForm:addIconButton');" >
								<e:bold value="#{msgs['ADMINISTRATORS.BUTTON.ADD_ICON']} " />
								<t:graphicImage value="/media/images/add.png"
									alt="#{msgs['ADMINISTRATORS.BUTTON.ADD_ICON']}" 
									title="#{msgs['ADMINISTRATORS.BUTTON.ADD_ICON']}" />
							</h:panelGroup>
							<e:commandButton style="display: none" id="addIconButton" 
								action="#{administratorsController.addIcon}"
								value="#{msgs['ADMINISTRATORS.BUTTON.ADD_ICON']}" />
						</h:panelGroup>
					</h:panelGroup>
				</e:panelGrid>
				<e:dataTable 
					rowOnMouseOver="javascript:{if (#{administratorsController.currentUserCanManageIcons}) {previousClass = this.className; this.className = 'portlet-table-selected';}}"
					rowOnMouseOut="javascript:{if (#{administratorsController.currentUserCanManageIcons}) {this.className = previousClass;}}"
					rowIndexVar="variable"
					id="iconData" width="100%"
					value="#{domainService.icons}"
					var="icon" border="0" cellspacing="0"
					cellpadding="0" columnClasses="colLeft,colLeftMax,colRight" >
					<f:facet name="header">
						<h:panelGroup>
							<t:htmlTag value="hr" />
						</h:panelGroup>
					</f:facet>
					<t:column style="cursor: #{administratorsController.currentUserCanManageIcons? 'pointer' : 'default'}" 
						onclick="if (#{administratorsController.currentUserCanManageIcons}) editIcon(#{variable}); return false;" >
						<t:graphicImage value="#{iconUrlProvider[icon]}" />
					</t:column>
					<t:column style="cursor: #{administratorsController.currentUserCanManageIcons? 'pointer' : 'default'}" 
						onclick="if (#{administratorsController.currentUserCanManageIcons}) editIcon(#{variable}); return false;" >
						<e:text value="#{icon.name}" />
					</t:column>
					<t:column style="text-align: right;">
						<h:panelGroup rendered="#{administratorsController.currentUserCanManageIcons and domainService.defaultCategoryIcon != icon and domainService.defaultDepartmentIcon != icon}" >
							<t:graphicImage value="/media/images/delete.png" 
								style="cursor: pointer" 
								alt="#{msgs['ADMINISTRATORS.BUTTON.DELETE_ICON']}"
								title="#{msgs['ADMINISTRATORS.BUTTON.DELETE_ICON']}"
								onclick="simulateLinkClick('administratorsForm:iconData:#{variable}:deleteIconButton');" />
							<e:commandButton id="deleteIconButton" action="#{administratorsController.deleteIcon}" 
								style="display: none" value="#{msgs['ADMINISTRATORS.BUTTON.DELETE_ICON']}">
								<t:updateActionListener value="#{icon}"
									property="#{administratorsController.iconToUpdate}" />
							</e:commandButton>
						</h:panelGroup>
						<e:commandButton id="editIconButton" action="editIcon" style="display: none" 
							value="#{msgs['ADMINISTRATORS.BUTTON.EDIT_ICON']}">
							<t:updateActionListener value="#{icon}"
								property="#{administratorsController.iconToUpdate}" />
						</e:commandButton>
					</t:column>
					<f:facet name="footer">
						<t:htmlTag value="hr" />
					</f:facet>
				</e:dataTable>
				<e:subSection value="#{msgs['ADMINISTRATORS.HEADER.DEFAULT_ICONS']}" />
				<e:panelGrid 
					columns="2" columnClasses="colLeft,colRight">
					<e:outputLabel value="#{msgs['ADMINISTRATORS.TEXT.ICONS.DEPARTMENT_DEFAULT']}" 
						for="defaultDepartmentIcon" />
					<h:panelGroup >
						<t:graphicImage value="#{iconUrlProvider[domainService.defaultDepartmentIcon]}" />
						<e:text value=" " />
						<e:selectOneMenu 
							id="defaultDepartmentIcon"
							rendered="#{administratorsController.currentUserCanManageIcons}" 
							onchange="simulateLinkClick('administratorsForm:updateDefaultIconsButton');" 
							value="#{domainService.defaultDepartmentIcon}"  
							converter="#{iconConverter}" >
							<f:selectItems value="#{administratorsController.iconItems}" />
						</e:selectOneMenu>
						<e:text
							rendered="#{not administratorsController.currentUserCanManageIcons}" 
							value="#{domainService.defaultDepartmentIcon.name}" />
					</h:panelGroup>
					<e:outputLabel value="#{msgs['ADMINISTRATORS.TEXT.ICONS.CATEGORY_DEFAULT']}" 
						for="defaultCategoryIcon" />
					<h:panelGroup >
						<t:graphicImage value="#{iconUrlProvider[domainService.defaultCategoryIcon]}" />
						<e:text value=" " />
						<e:selectOneMenu
							id="defaultCategoryIcon"
							rendered="#{administratorsController.currentUserCanManageIcons}" 
							onchange="simulateLinkClick('administratorsForm:updateDefaultIconsButton');" 
							value="#{domainService.defaultCategoryIcon}" 
							converter="#{iconConverter}" >
							<f:selectItems value="#{administratorsController.iconItems}" />
						</e:selectOneMenu>
						<e:text
							rendered="#{not administratorsController.currentUserCanManageIcons}" 
							value="#{domainService.defaultCategoryIcon.name}" />
					</h:panelGroup>
				</e:panelGrid>
				<e:commandButton id="updateDefaultIconsButton" action="#{administratorsController.enter}" 
					rendered="#{administratorsController.currentUserCanManageIcons}" 
					style="display: none" 
					value="#{msgs['ADMINISTRATORS.BUTTON.UPDATE_DEFAULT_ICONS']}" />
			</h:panelGroup>
		</e:panelGrid>
		
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{administratorsController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">	
		highlightTableRows("administratorsForm:data");
	</script>
</e:page>
