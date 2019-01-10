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

<t:htmlTag id="search" value="div" styleClass="page-wrapper search">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                    <t:htmlTag value="div" styleClass="content-inner">
                    <h:panelGroup rendered="#{not searchController.pageAuthorized}" >
                        <%@include file="_auth.jsp"%>
                    </h:panelGroup>
                    <e:form
                        freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                        showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                        showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                        id="searchForm" rendered="#{searchController.pageAuthorized}" >
                             <t:htmlTag value="div" styleClass="message">
                                     <e:messages/>
                             </t:htmlTag>
                             <t:htmlTag value="div" styleClass="dashboard-header">
                                <t:htmlTag value="div" styleClass="controlPanel-title">
                                    <t:htmlTag value="h1">
                                        <t:htmlTag value="span" rendered="#{not searchController.currentUser.advancedSearch}">
                                            <h:outputText value="#{msgs['SEARCH.ADVANCED.FALSE']}"/>
                                        </t:htmlTag>
                                        <h:panelGroup rendered="#{searchController.currentUser.advancedSearch}">
                                             <t:htmlTag value="span">
                                                <h:outputText value="#{msgs['SEARCH.ADVANCED.TRUE']}"/>
                                             </t:htmlTag>
                                        </h:panelGroup>
                                    </t:htmlTag>

                                    <t:htmlTag styleClass="dashboard-toggle" value="div">
                                        <h:panelGroup rendered="#{not searchController.currentUser.advancedSearch}">
                                            <h:panelGroup style="cursor: pointer"
                                                onclick="selectChange('searchForm:advancedSearch','true')">
                                                <t:htmlTag value="i"
                                                styleClass="fas fa-toggle-on"/>
                                            </h:panelGroup>
                                        </h:panelGroup>
                                        <h:panelGroup rendered="#{searchController.currentUser.advancedSearch}">
                                            <h:panelGroup style="cursor: pointer"
                                                onclick="selectChange('searchForm:advancedSearch','false')">
                                                <t:htmlTag value="i"
                                                styleClass="fas fa-toggle-off"/>
                                            </h:panelGroup>
                                        </h:panelGroup>
                                         <e:selectOneMenu styleClass="hideme" id="advancedSearch" value="#{searchController.currentUser.advancedSearch}"
                                                 onchange="javascript:{simulateLinkClick('searchForm:simpleSearchButton');simulateLinkClick('searchForm:advancedSearchButton');}">
                                            <f:selectItems value="#{searchController.advancedItems}" />
                                        </e:selectOneMenu>
                                    </t:htmlTag>
                                </t:htmlTag>
                             </t:htmlTag>

                             <t:htmlTag value="div" styleClass="search-filter form-block" rendered="#{not searchController.currentUser.advancedSearch}">
                                <t:htmlTag value="div" styleClass="form-item">
                                    <e:outputLabel for="searchForm:simpleTokens" value="#{msgs['SEARCH.TOKENS.SIMPLE']}"/>
                                    <e:inputText id="simpleTokens" value="#{searchController.tokens}" size="50" />
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item">
                                    <e:outputLabel for="searchForm:simpleDepartmentFilter" value=" #{msgs['SEARCH.DEPARTMENT_FILTER.PROMPT']} " />
                                    <e:selectOneMenu
                                        id="simpleDepartmentFilter"
                                        value="#{searchController.currentUser.searchDepartmentFilter}"
                                        converter="#{departmentConverter}">
                                        <f:selectItems value="#{searchController.departmentItems}" />
                                    </e:selectOneMenu>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item">
                                     <e:outputLabel for="searchForm:simpleSearchTypeFilter" value=" #{msgs['SEARCH.SEARCH_TYPE_FILTER.PROMPT']} " />
                                     <e:selectOneMenu
                                         id="simpleSearchTypeFilter"
                                         value="#{searchController.currentUser.searchTypeFilter}">
                                         <f:selectItems value="#{searchController.searchTypeItems}" />
                                     </e:selectOneMenu>
                                </t:htmlTag>
                                 <t:htmlTag value="div" styleClass="form-item" >
                                           <e:commandButton  id="simpleSearchButton" styleClass="button--secondary"
                                               action="#{searchController.search}"
                                               value="#{msgs['SEARCH.BUTTON.SEARCH']}" >
                                           </e:commandButton>
                                  </t:htmlTag>
                             </t:htmlTag>

                             <t:htmlTag value="div" styleClass="advancedSearch-filter form-block" rendered="#{searchController.currentUser.advancedSearch}">
                                <t:htmlTag value="div" styleClass="form-block">
                                     <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:allTokens" value="#{msgs['SEARCH.TOKENS.ALL']}"/>
                                        <e:inputText id="allTokens" value="#{searchController.tokens}" size="50" />
                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:exprTokens" value="#{msgs['SEARCH.TOKENS.EXPR']} " />
                                        <e:inputText id="exprTokens" value="#{searchController.exprTokens}" size="50"/>
                                     </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:orTokens" value="#{msgs['SEARCH.TOKENS.OR']} " />
                                        <e:inputText id="orTokens" value="#{searchController.orTokens}" size="50"/>
                                      </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:notTokens" value="#{msgs['SEARCH.TOKENS.NOT']} " />
                                    <e:inputText id="notTokens" value="#{searchController.notTokens}" size="50"/>
                                     </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-block">
                                      <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:managerId" value="#{msgs['SEARCH.MANAGER.PROMPT']} " />
                                        <e:inputText id="managerId" value="#{searchController.managerId}" size="20"/>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:ownerId" value="#{msgs['SEARCH.OWNER.PROMPT']} " />
                                        <e:inputText id="ownerId" value="#{searchController.ownerId}" size="20"/>
                                      </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:userId" value="#{msgs['SEARCH.USER.PROMPT']} " />
                                        <e:inputText id="userId" value="#{searchController.userId}" size="20"/>
                                       </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-block">
                                     <t:htmlTag value="div" styleClass="form-item">
                                        <t:htmlTag value="div">
                                            <h:outputLabel value=" #{msgs['SEARCH.DATE_FILTER.PROMPT']}" />
                                        </t:htmlTag>
                                         <t:htmlTag value="div">
                                              <e:selectOneMenu
                                                  value="#{searchController.currentUser.searchDate1}"
                                                  converter="#{timestampConverter}">
                                                  <f:selectItems value="#{searchController.date1Items}" />
                                              </e:selectOneMenu>
                                              <e:text value=" #{msgs['SEARCH.DATE_FILTER.SEPARATOR']} " />
                                              <e:selectOneMenu
                                                  value="#{searchController.currentUser.searchDate2}"
                                                  converter="#{timestampConverter}">
                                                  <f:selectItems value="#{searchController.date2Items}" />
                                              </e:selectOneMenu>
                                        </t:htmlTag>
                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:advancedSearchTypeFilter" value="#{msgs['SEARCH.SEARCH_TYPE_FILTER.PROMPT']} " />
                                        <e:selectOneMenu
                                             id="advancedSearchTypeFilter"
                                             value="#{searchController.currentUser.searchTypeFilter}">
                                             <f:selectItems value="#{searchController.searchTypeItems}" />
                                        </e:selectOneMenu>
                                     </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:advancedDepartmentFilter" value="#{msgs['SEARCH.DEPARTMENT_FILTER.PROMPT']} " />
                                        <e:selectOneMenu
                                            id="advancedDepartmentFilter"
                                            value="#{searchController.currentUser.searchDepartmentFilter}"
                                            converter="#{departmentConverter}">
                                            <f:selectItems value="#{searchController.departmentItems}" />
                                        </e:selectOneMenu>
                                      </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-item">
                                        <e:outputLabel for="searchForm:advancedSearchSort" value="#{msgs['SEARCH.SORT.PROMPT']} " />
                                        <e:selectOneMenu
                                                 id="advancedSearchSort"
                                                 value="#{searchController.currentUser.searchSortByDate}">
                                                 <f:selectItems value="#{searchController.searchSortItems}" />
                                         </e:selectOneMenu>
                                     </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-block">
                                    <t:htmlTag value="div" styleClass="form-item">
                                         <e:commandButton id="advancedSearchButton" styleClass="button--secondary"
                                             value="#{msgs['SEARCH.BUTTON.SEARCH']}"
                                             action="#{searchController.search}" />
                                      </t:htmlTag>
                                </t:htmlTag>
                              </t:htmlTag>



                        <e:dataTable
                            id="data" rowIndexVar="variable"
                            rowClasses="oddRow,evenRow"
                            styleClass="dashboard"
                            value="#{searchController.searchResults.results}" var="sr"
                            border="0"  cellspacing="0" cellpadding="0"
                            rendered="#{not empty searchController.searchResults.results}">
                            <f:facet name="header">
                                <t:htmlTag styleClass="dashboard-header-wrapper header-wrapper" value="div">
                                        <t:htmlTag  value="div" styleClass="items-count" rendered="#{searchController.searchResults.totalResultsNumber != searchController.searchResults.resultsNumber}">
                                             <e:text value="#{msgs['SEARCH.HEADER.RESULTS.TRUNCATED_ESTIMATED']}"
                                                 rendered="#{searchController.searchResults.totalResultsNumberEstimated}" >
                                                 <f:param value="#{searchController.searchResults.resultsNumber}" />
                                                 <f:param value="#{searchController.searchResults.totalResultsNumber}" />
                                             </e:text>
                                             <e:text value="#{msgs['SEARCH.HEADER.RESULTS.TRUNCATED_EXACT']}"
                                                 rendered="#{not searchController.searchResults.totalResultsNumberEstimated}" >
                                                 <f:param value="#{searchController.searchResults.resultsNumber}" />
                                                 <f:param value="#{searchController.searchResults.totalResultsNumber}" />
                                             </e:text>
                                        </t:htmlTag>
                                         <t:htmlTag styleClass="items-count" value="div" rendered="#{searchController.searchResults.totalResultsNumber == searchController.searchResults.resultsNumber}">
                                                      <e:text value="#{msgs['SEARCH.HEADER.RESULTS.COMPLETE']}">
                                                          <f:param value="#{searchController.searchResults.resultsNumber}" />
                                                      </e:text>
                                         </t:htmlTag>
                                 </t:htmlTag>
                            </f:facet>

                            <t:column style="cursor: pointer" onclick="selectEntry(#{variable});"/>

                            <t:column style="cursor: pointer" onclick="selectEntry(#{variable});">
                                <f:facet name="header">
                                    <t:htmlTag value="div" styleClass="column-header">
                                        <e:text value="#{msgs['SEARCH.HEADER.SCORE']}" />
                                    </t:htmlTag>
                                </f:facet>
                                <e:text styleClass="search-result-score" value="#{sr.score}% " />

                            </t:column>

                            <t:column style="cursor: pointer" onclick="selectEntry(#{variable});">
                                <f:facet name="header">
                                     <t:htmlTag value="div" styleClass="column-header">
                                        <e:text value="#{msgs['SEARCH.HEADER.DATE']}" />
                                    </t:htmlTag>
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
                                    <t:htmlTag value="div" styleClass="column-header">
                                        <e:text value="#{msgs['SEARCH.HEADER.DEPARTMENT']}" />
                                    </t:htmlTag>
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
                                    <t:htmlTag value="div" styleClass="column-header">
                                        <e:text value="#{msgs['SEARCH.HEADER.OWNER']}" />
                                    </t:htmlTag>
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
                                    <t:htmlTag value="div" styleClass="column-header">
                                        <e:text value="#{msgs['SEARCH.HEADER.MANAGER']}" />
                                    </t:htmlTag>
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
                        <e:paragraph value="#{msgs['SEARCH.TEXT.NO_RESULT']}"
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

                  </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
                <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
                </t:htmlTag>
        </t:htmlTag>

	
</e:page>
