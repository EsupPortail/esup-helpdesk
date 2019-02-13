<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="search"
	locale="#{sessionController.locale}" >

<script type="text/javascript">
	function selectEntry(index) {
		simulateLinkClick('searchForm:data:'+index+':viewTicketButton');
		simulateLinkClick('searchForm:data:'+index+':viewArchivedTicketButton');
		simulateLinkClick('searchForm:data:'+index+':viewFaqButton');
	}
</script>

	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{not searchController.pageAuthorized}" >
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="searchForm" rendered="#{searchController.pageAuthorized}" >
		<e:panelGrid columns="2" width="100%" columnClasses="colLeftMax,ColRight">
			<e:section value="#{msgs['SEARCH.TITLE']}" />
			<h:panelGroup>
				<e:selectOneMenu 
					id="advancedSearch"
					value="#{searchController.currentUser.advancedSearch}" 
					onchange="javascript:{simulateLinkClick('searchForm:advancedSearchButton');simulateLinkClick('searchForm:simpleSearchButton');}" 
					>
					<f:selectItems value="#{searchController.advancedItems}" />
				</e:selectOneMenu>
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />
		
		<h:panelGroup rendered="#{not searchController.currentUser.advancedSearch}" >
			<e:outputLabel for="searchForm:simpleTokens" value="#{msgs['SEARCH.TOKENS.SIMPLE']} " />
			<e:inputText id="simpleTokens" value="#{searchController.tokens}" size="50" 
			onkeypress="if (event.keyCode == 13) { simulateLinkClick('searchForm:simpleSearchButton'); return false; }" />

			<e:outputLabel for="searchForm:simpleDepartmentFilter" value=" #{msgs['SEARCH.DEPARTMENT_FILTER.PROMPT']} " />
			<e:selectOneMenu 
				id="simpleDepartmentFilter"
				value="#{searchController.currentUser.searchDepartmentFilter}" 
				onchange="javascript:{simulateLinkClick('searchForm:simpleSearchButton');}" 
				converter="#{departmentConverter}"
				>
				<f:selectItems value="#{searchController.departmentItems}" />
			</e:selectOneMenu>

			<e:outputLabel for="searchForm:simpleSearchTypeFilter" value=" #{msgs['SEARCH.SEARCH_TYPE_FILTER.PROMPT']} " />
			<e:selectOneMenu 
				id="simpleSearchTypeFilter"
				value="#{searchController.currentUser.searchTypeFilter}" 
				onchange="javascript:{simulateLinkClick('searchForm:simpleSearchButton');}" 
				>
				<f:selectItems value="#{searchController.searchTypeItems}" />
			</e:selectOneMenu>

			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('searchForm:simpleSearchButton');" >
				<e:bold value=" #{msgs['SEARCH.BUTTON.SEARCH']} " />
				<t:graphicImage value="/media/images/search.png"
					alt="#{msgs['SEARCH.BUTTON.SEARCH']}" 
					title="#{msgs['SEARCH.BUTTON.SEARCH']}" />
			</h:panelGroup>
			<e:commandButton id="simpleSearchButton" style="display: none"
				value="#{msgs['SEARCH.BUTTON.SEARCH']}"
				action="#{searchController.search}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{searchController.currentUser.advancedSearch}" >
			<e:panelGrid columns="3" columnClasses="colLeft,colLeft,colLeft">
				<e:panelGrid columns="2" columnClasses="colRightNowrap,colLeft">
					<e:outputLabel for="searchForm:allTokens" value="#{msgs['SEARCH.TOKENS.ALL']} " />
					<e:inputText id="allTokens" value="#{searchController.tokens}" size="50" 
						onkeypress="if (event.keyCode == 13) { simulateLinkClick('searchForm:advancedSearchButton'); return false; }" />
					<e:outputLabel for="searchForm:exprTokens" value="#{msgs['SEARCH.TOKENS.EXPR']} " />
					<e:inputText id="exprTokens" value="#{searchController.exprTokens}" size="50" 
						onkeypress="if (event.keyCode == 13) { simulateLinkClick('searchForm:advancedSearchButton'); return false; }" />
					<e:outputLabel for="searchForm:orTokens" value="#{msgs['SEARCH.TOKENS.OR']} " />
					<e:inputText id="orTokens" value="#{searchController.orTokens}" size="50" 
						onkeypress="if (event.keyCode == 13) { simulateLinkClick('searchForm:advancedSearchButton'); return false; }" />
					<e:outputLabel for="searchForm:notTokens" value="#{msgs['SEARCH.TOKENS.NOT']} " />
					<e:inputText id="notTokens" value="#{searchController.notTokens}" size="50" 
						onkeypress="if (event.keyCode == 13) { simulateLinkClick('searchForm:advancedSearchButton'); return false; }" />
				</e:panelGrid>
				<e:panelGrid columns="1" columnClasses="colCenter">
					<e:panelGrid columns="2" columnClasses="colRightNowrap,colLeft">
						<e:outputLabel for="searchForm:managerId" value="#{msgs['SEARCH.MANAGER.PROMPT']} " />
						<e:inputText id="managerId" value="#{searchController.managerId}" size="20" 
							onkeypress="if (event.keyCode == 13) { simulateLinkClick('searchForm:advancedSearchButton'); return false; }" />
						<e:outputLabel for="searchForm:ownerId" value="#{msgs['SEARCH.OWNER.PROMPT']} " />
						<e:inputText id="ownerId" value="#{searchController.ownerId}" size="20" 
							onkeypress="if (event.keyCode == 13) { simulateLinkClick('searchForm:advancedSearchButton'); return false; }" />
						<e:outputLabel for="searchForm:userId" value="#{msgs['SEARCH.USER.PROMPT']} " />
						<e:inputText id="userId" value="#{searchController.userId}" size="20" 
							onkeypress="if (event.keyCode == 13) { simulateLinkClick('searchForm:advancedSearchButton'); return false; }" />
					</e:panelGrid>
					<e:text value="#{msgs['SEARCH.TICKETS_ONLY']}" />
				</e:panelGrid>
				<e:panelGrid columns="2" columnClasses="colRightNowrap,colLeft">
					<e:bold value="#{msgs['SEARCH.DATE_FILTER.PROMPT']} " />
					<h:panelGroup>
						<e:selectOneMenu 
							value="#{searchController.currentUser.searchDate1}" 
							onchange="javascript:{simulateLinkClick('searchForm:advancedSearchButton');}" 
							converter="#{timestampConverter}"
							>
							<f:selectItems value="#{searchController.date1Items}" />
						</e:selectOneMenu>
						<e:bold value=" #{msgs['SEARCH.DATE_FILTER.SEPARATOR']} " />
						<e:selectOneMenu 
							value="#{searchController.currentUser.searchDate2}" 
							onchange="javascript:{simulateLinkClick('searchForm:advancedSearchButton');}" 
							converter="#{timestampConverter}"
							>
							<f:selectItems value="#{searchController.date2Items}" />
						</e:selectOneMenu>
					</h:panelGroup>
					<e:outputLabel for="searchForm:advancedSearchTypeFilter" value="#{msgs['SEARCH.SEARCH_TYPE_FILTER.PROMPT']} " />
					<e:selectOneMenu 
						id="advancedSearchTypeFilter"
						value="#{searchController.currentUser.searchTypeFilter}" 
						onchange="javascript:{simulateLinkClick('searchForm:advancedSearchButton');}" 
						>
						<f:selectItems value="#{searchController.searchTypeItems}" />
					</e:selectOneMenu>
					<e:outputLabel for="searchForm:advancedDepartmentFilter" value="#{msgs['SEARCH.DEPARTMENT_FILTER.PROMPT']} " />
					<e:selectOneMenu 
						id="advancedDepartmentFilter"
						value="#{searchController.currentUser.searchDepartmentFilter}" 
						onchange="javascript:{simulateLinkClick('searchForm:advancedSearchButton');}" 
						converter="#{departmentConverter}"
						>
						<f:selectItems value="#{searchController.departmentItems}" />
					</e:selectOneMenu>
					<e:outputLabel for="searchForm:advancedSearchSort" value="#{msgs['SEARCH.SORT.PROMPT']} " />
					<h:panelGroup>
						<e:selectOneMenu 
							id="advancedSearchSort"
							value="#{searchController.currentUser.searchSortByDate}" 
							onchange="javascript:{simulateLinkClick('searchForm:advancedSearchButton');}" 
							>
							<f:selectItems value="#{searchController.searchSortItems}" />
						</e:selectOneMenu>
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('searchForm:advancedSearchButton');" >
							<e:bold value=" #{msgs['SEARCH.BUTTON.SEARCH']} " />
							<t:graphicImage value="/media/images/search.png"
								alt="#{msgs['SEARCH.BUTTON.SEARCH']}" 
								title="#{msgs['SEARCH.BUTTON.SEARCH']}" />
						</h:panelGroup>
						<e:commandButton id="advancedSearchButton" style="display: none"
							value="#{msgs['SEARCH.BUTTON.SEARCH']}"
							action="#{searchController.search}" />
					</h:panelGroup>
				</e:panelGrid>
			</e:panelGrid>
		</h:panelGroup>

		<e:dataTable 
			id="data" rowIndexVar="variable"
			value="#{searchController.searchResults.results}" var="sr"
			border="0" style="width:100%" cellspacing="0" cellpadding="0"
			rendered="#{not empty searchController.searchResults.results}" 
			columnClasses="colRightNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap"
			>
			<f:facet name="header">
				<t:htmlTag value="hr" />
			</f:facet>
			<t:column style="cursor: pointer" onclick="selectEntry(#{variable});">
				<e:text
					value="#{sr.score}% " />
			</t:column>
			<t:column style="cursor: pointer" onclick="selectEntry(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['SEARCH.HEADER.SCORE']}" />
				</f:facet>
				<t:graphicImage value="/media/images/searchScore.png" width="#{sr.score}" height="13" />
			</t:column>
			<t:column style="cursor: pointer" onclick="selectEntry(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['SEARCH.HEADER.DATE']}" />
				</f:facet>
				<e:text rendered="#{sr.ticketSearchResult}"
					value="#{sr.ticket.lastActionDate}" />
				<e:text rendered="#{sr.archivedTicketSearchResult}"
					value="#{sr.archivedTicket.archivingDate}" />
				<e:text rendered="#{sr.faqSearchResult}"
					value="#{sr.faq.lastUpdate}" />
			</t:column>
			<t:column style="cursor: pointer" onclick="selectEntry(#{variable});">
				<f:facet name="header">
					<h:panelGroup>
						<h:panelGroup 
							rendered="#{searchController.searchResults.totalResultsNumber != searchController.searchResults.resultsNumber}" >
							<e:bold value="#{msgs['SEARCH.HEADER.RESULTS.TRUNCATED_ESTIMATED']}"
								rendered="#{searchController.searchResults.totalResultsNumberEstimated}" >
								<f:param value="#{searchController.searchResults.resultsNumber}" />
								<f:param value="#{searchController.searchResults.totalResultsNumber}" />
							</e:bold>
							<e:bold value="#{msgs['SEARCH.HEADER.RESULTS.TRUNCATED_EXACT']}"
								rendered="#{not searchController.searchResults.totalResultsNumberEstimated}" >
								<f:param value="#{searchController.searchResults.resultsNumber}" />
								<f:param value="#{searchController.searchResults.totalResultsNumber}" />
							</e:bold>
						</h:panelGroup>
						<e:bold value="#{msgs['SEARCH.HEADER.RESULTS.COMPLETE']}"
							rendered="#{searchController.searchResults.totalResultsNumber == searchController.searchResults.resultsNumber}" >
							<f:param value="#{searchController.searchResults.resultsNumber}" />
						</e:bold>
					</h:panelGroup>
				</f:facet>
				<h:panelGroup rendered="#{sr.ticketSearchResult}" >
					<e:text value="#{msgs['SEARCH.RESULTS.TICKET_RESULT']}" >
						<f:param value="#{sr.ticket.id}" />
						<f:param value="#{sr.ticket.label}" />
					</e:text>
				</h:panelGroup>
				<h:panelGroup rendered="#{sr.archivedTicketSearchResult}" >
					<e:text value="#{msgs['SEARCH.RESULTS.ARCHIVED_TICKET_RESULT']}" >
						<f:param value="#{sr.archivedTicket.ticketId}" />
						<f:param value="#{sr.archivedTicket.label}" />
					</e:text>
				</h:panelGroup>
				<h:panelGroup rendered="#{sr.faqSearchResult}" >
					<e:text value="#{msgs['SEARCH.RESULTS.FAQ_RESULT']}" >
						<f:param value="#{sr.faq.label}" />
					</e:text>
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: pointer" onclick="selectEntry(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['SEARCH.HEADER.DEPARTMENT']}" />
				</f:facet>
				<e:text rendered="#{sr.ticketSearchResult}"
					value="#{sr.ticket.department.label}" />
				<e:text rendered="#{sr.archivedTicketSearchResult}"
					value="#{sr.archivedTicket.department.label}" />
				<e:text rendered="#{sr.faqSearchResult}"
					value="#{sr.faq.department.label}" />
			</t:column>
			<t:column style="cursor: pointer" onclick="selectEntry(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['SEARCH.HEADER.OWNER']}" />
				</f:facet>
				<h:panelGroup>
					<e:text rendered="#{sr.ticketSearchResult}"
						value="#{userFormatter[sr.ticket.owner]}" />
					<e:text rendered="#{sr.archivedTicketSearchResult}"
						value="#{userFormatter[sr.archivedTicket.owner]}" />
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: pointer" onclick="selectEntry(#{variable});">
				<f:facet name="header">
					<e:bold value="#{msgs['SEARCH.HEADER.MANAGER']}" />
				</f:facet>
				<h:panelGroup>
					<e:text rendered="#{sr.ticketSearchResult and sr.ticket.manager != null}"
						value="#{userFormatter[sr.ticket.manager]}" />
					<e:text rendered="#{sr.archivedTicketSearchResult and sr.archivedTicket.manager != null}"
						value="#{userFormatter[sr.archivedTicket.manager]}" />
				</h:panelGroup>
			</t:column>
			<t:column style="cursor: pointer" >
				<h:panelGroup rendered="#{sr.ticketSearchResult}" >
					<e:commandButton id="viewTicketButton" style="display: none"
						action="viewTicket" immediate="true">
						<t:updateActionListener value="#{sr.ticket}"
							property="#{ticketController.updatedTicket}" />
						<t:updateActionListener value="search"
							property="#{ticketController.backPage}" />
					</e:commandButton>
				</h:panelGroup>
				<h:panelGroup rendered="#{sr.archivedTicketSearchResult}" >
					<e:commandButton id="viewArchivedTicketButton" style="display: none"
						action="viewArchivedTicket" immediate="true">
						<t:updateActionListener value="#{sr.archivedTicket}"
							property="#{archivedTicketController.archivedTicket}" />
						<t:updateActionListener value="search"
							property="#{archivedTicketController.backPage}" />
					</e:commandButton>
				</h:panelGroup>
				<h:panelGroup rendered="#{sr.faqSearchResult}" >
					<e:commandButton id="viewFaqButton" style="display: none"
						action="viewFaq" immediate="true">
						<t:updateActionListener value="#{sr.faq}"
							property="#{faqsController.faq}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
			<f:facet name="footer">
				<t:htmlTag value="hr" />
			</f:facet>
		</e:dataTable>
		<e:paragraph 
			value="#{msgs['SEARCH.TEXT.NO_RESULT']}" 
			rendered="#{
				(
					not searchController.currentUser.advancedSearch 
					and searchController.searchResults.resultsNumber == 0 
					and searchController.tokens != null
				) or (
					searchController.currentUser.advancedSearch 
					and searchController.searchResults.resultsNumber == 0 
					and (
						searchController.tokens != null 
						or searchController.exprTokens != null 
						or searchController.orTokens != null
						or searchController.notTokens != null 
						or searchController.managerId != null 
						or searchController.ownerId != null 
						or searchController.userId != null
					)
				)}" />
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{searchController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		highlightTableRows("searchForm:data");
</script>
	
</e:page>
