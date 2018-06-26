<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="journal"
	locale="#{sessionController.locale}" >
<script type="text/javascript">	
function selectTicket(index) {
	simulateLinkClick('journalForm:data:'+index+':viewTicketButton');
}
</script>
	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{journalController.currentUser == null}" >
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="journalForm" rendered="#{journalController.pageAuthorized}" >
		<e:section value="#{msgs['JOURNAL.TITLE']}" />
		<e:messages />
		<e:outputLabel for="journalForm:departmentFilter" value="#{msgs['JOURNAL.DEPARTMENT_FILTER.PROMPT']}" />
		<e:selectOneMenu 
			id="departmentFilter"
			value="#{journalController.currentUser.journalDepartmentFilter}" 
			onchange="javascript:{simulateLinkClick('journalForm:filterChangeButton');}" 
			converter="#{departmentConverter}"
			>
			<f:selectItems value="#{journalController.departmentItems}" />
		</e:selectOneMenu>
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('journalForm:filterChangeButton');" >
			<e:bold value="#{msgs['_.BUTTON.REFRESH']} " />
			<t:graphicImage value="/media/images/refresh.png"
				alt="#{msgs['_.BUTTON.REFRESH']}" 
				title="#{msgs['_.BUTTON.REFRESH']}" />
		</h:panelGroup>
		<e:commandButton style="display: none" value="#{msgs['_.BUTTON.REFRESH']}"
			id="filterChangeButton"
			action="#{journalController.enter}" />

		<e:dataTable 
			id="data" rowIndexVar="variable"
			value="#{journalController.paginator.visibleItems}" var="action"
			border="0" style="width:100%" cellspacing="0" cellpadding="0"
			rendered="#{not empty journalController.paginator.visibleItems}" 
			columnClasses="colLeftNowrap,colLeft,colLeft"
			>
			<f:facet name="header">
				<h:panelGroup >
					<h:panelGrid columns="3" columnClasses="colLeft,,colRight"
						width="100%">
						<h:panelGroup>
							<e:text value="#{msgs['JOURNAL.TEXT.ACTIONS']}">
								<f:param
									value="#{journalController.paginator.firstVisibleNumber + 1}" />
								<f:param
									value="#{journalController.paginator.lastVisibleNumber + 1}" />
								<f:param
									value="#{journalController.paginator.totalItemsCount}" />
							</e:text>
						</h:panelGroup>
						<h:panelGroup
							rendered="#{journalController.paginator.lastPageNumber == 0}" />
						<h:panelGroup
							rendered="#{journalController.paginator.lastPageNumber != 0}">
							<h:panelGroup
								rendered="#{not journalController.paginator.firstPage}">
								<t:graphicImage value="/media/images/page-first.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('journalForm:data:pageFirst');" />
								<e:commandButton id="pageFirst" style="display: none" value="#{msgs['PAGINATION.BUTTON.FIRST']}"
									action="#{journalController.paginator.gotoFirstPage}" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/page-previous.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('journalForm:data:pagePrevious');" />
								<e:commandButton id="pagePrevious" style="display: none" value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
									action="#{journalController.paginator.gotoPreviousPage}" />
							</h:panelGroup>
							<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
							<t:dataList value="#{journalController.paginator.nearPages}"
								var="page">
								<e:text value=" " />
								<e:italic value="#{page + 1}"
									rendered="#{page == journalController.paginator.currentPage}" />
								<h:commandLink value="#{page + 1}"
									rendered="#{page != journalController.paginator.currentPage}" >
									<t:updateActionListener value="#{page}"
										property="#{journalController.paginator.currentPage}" />
								</h:commandLink>
								<e:text value=" " />
							</t:dataList>
							<h:panelGroup
								rendered="#{not journalController.paginator.lastPage}">
								<t:graphicImage value="/media/images/page-next.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('journalForm:data:pageNext');" />
								<e:commandButton id="pageNext" style="display: none" value="#{msgs['PAGINATION.BUTTON.NEXT']}"
									action="#{journalController.paginator.gotoNextPage}" />
								<e:text value=" " />
								<t:graphicImage value="/media/images/page-last.png" 
									style="cursor: pointer" 
									onclick="simulateLinkClick('journalForm:data:pageLast');" />
								<e:commandButton id="pageLast" style="display: none" value="#{msgs['PAGINATION.BUTTON.LAST']}"
									action="#{journalController.paginator.gotoLastPage}" />
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup>
							<e:text value="#{msgs['JOURNAL.TEXT.ACTIONS_PER_PAGE']} " />
							<e:selectOneMenu onchange="javascript:{simulateLinkClick('journalForm:filterChangeButton');}"
								value="#{journalController.currentUser.journalPageSize}">
								<f:selectItems
									value="#{journalController.paginator.pageSizeItems}" />
							</e:selectOneMenu>
						</h:panelGroup>
					</h:panelGrid>
					<t:htmlTag value="hr" />
				</h:panelGroup>
			</f:facet>
			<t:column style="cursor: pointer" onclick="selectTicket(#{variable});" >
				<f:facet name="header">
					<h:panelGroup>
						<e:bold value="#{msgs['JOURNAL.HEADER.TICKET']}" />
					</h:panelGroup>
				</f:facet>
				<t:graphicImage value="/media/images/public.png" rendered="#{action.ticket.effectiveScope == 'PUBLIC'}" />
				<t:graphicImage value="/media/images/public.png" rendered="#{action.ticket.effectiveScope == 'CAS'}" />
				<t:graphicImage value="/media/images/protected.png" rendered="#{action.ticket.effectiveScope == 'SUBJECT_ONLY'}" />
				<t:graphicImage value="/media/images/private.png" rendered="#{action.ticket.effectiveScope == 'PRIVATE'}" />
				<e:text value=" #{msgs['JOURNAL.TEXT.TICKET']}" >
					<f:param value="#{action.ticket.id}" />
					<f:param value="#{action.ticket.label}" />
				</e:text>
				<t:htmlTag value="br" />
				<e:text value="#{msgs['JOURNAL.TEXT.CATEGORY']}" >
					<f:param value="#{action.ticket.department.label}" />
					<f:param value="#{action.ticket.category.label}" />
				</e:text>
			</t:column>
			<t:column style="cursor: pointer" onclick="selectTicket(#{variable});" >
				<f:facet name="header">
					<h:panelGroup>
						<e:bold value="#{msgs['JOURNAL.HEADER.ACTION']}" />
					</h:panelGroup>
				</f:facet>
				<t:graphicImage value="/media/images/public.png" rendered="#{action.scope == 'DEFAULT'}" />
				<t:graphicImage value="/media/images/invited.png" rendered="#{action.scope == 'INVITED'}" />
				<t:graphicImage value="/media/images/invited.png" rendered="#{action.scope == 'INVITED_MANAGER'}" />
				<t:graphicImage value="/media/images/protected.png" rendered="#{action.scope == 'OWNER'}" />
				<t:graphicImage value="/media/images/private.png" rendered="#{action.scope == 'MANAGER'}" />
				<e:bold value=" #{actionI18nTitleProvider[action]}" />
				<h:panelGroup rendered="#{action.message != null}" >
					<t:htmlTag value="br" />
					<e:text value="#{action.message}" escape="false" />
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: pointer" >
				<e:commandButton 
					value="#{msgs['_.BUTTON.VIEW_EDIT']}" 
					id="viewTicketButton"
					style="display: none"
					action="#{journalController.viewTicket}"
					immediate="true" >
					<t:updateActionListener value="#{action.ticket}"
						property="#{journalController.ticketToView}" />
					<t:updateActionListener value="journal"
						property="#{ticketController.backPage}" />
				</e:commandButton>
			</t:column>
			<f:facet name="footer">
				<t:htmlTag value="hr" />
			</f:facet>
		</e:dataTable>
		<e:paragraph value="#{msgs['JOURNAL.TEXT.NO_ACTION']}" rendered="#{empty journalController.paginator.visibleItems}" />
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{journalController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">	
		highlightTableRows("journalForm:data");
</script>
	
</e:page>
