<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="journal" locale="#{sessionController.locale}" >
<script type="text/javascript">
function selectTicket(index) {
	simulateLinkClick('journalForm:data:'+index+':viewTicketButton');
}
</script>

	   <t:htmlTag id="journal" value="div" styleClass="page-wrapper journal">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                    <t:htmlTag value="div" styleClass="content-inner">
                        <h:panelGroup rendered="#{journalController.currentUser == null}" >
                            <%@include file="_auth.jsp"%>
                        </h:panelGroup>
                        <e:form freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="journalForm" rendered="#{journalController.pageAuthorized}" >
                                <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['JOURNAL.TITLE']}"/>
                                                 </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="message">
                                      <e:messages/>
                                </t:htmlTag>

                    <t:htmlTag value="div" styleClass="dashboard-filter form-block">
                        <t:htmlTag value="div" styleClass="form-item">
                             <e:outputLabel for="journalForm:departmentFilter" value="#{msgs['JOURNAL.DEPARTMENT_FILTER.PROMPT']}" />
                             <e:selectOneMenu
                                   id="departmentFilter"
                                   value="#{journalController.currentUser.journalDepartmentFilter}"
                                   converter="#{departmentConverter}">
                                   <f:selectItems value="#{journalController.departmentItems}" />
                             </e:selectOneMenu>
                        </t:htmlTag>


                         <t:htmlTag value="div" styleClass="form-item" >
                                  <e:outputLabel for="journalForm:pageSizeFilter"
                                                                     value="#{msgs['JOURNAL.TEXT.ACTIONS_PER_PAGE']}"/>
                                   <e:selectOneMenu id="pageSizeFilter"

                                             value="#{journalController.currentUser.journalPageSize}">
                                             <f:selectItems
                                                 value="#{journalController.paginator.pageSizeItems}" />
                                   </e:selectOneMenu>
                          </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-item" >
                                  <e:commandButton  id="filterChangeButton"
                                  styleClass="button--secondary"
                                      action="#{journalController.enter}"
                                      value="#{msgs['CONTROL_PANEL.BUTTON.FILTER']}" >
                                  </e:commandButton>
                         </t:htmlTag>

                    </t:htmlTag>


                                <e:dataTable
                                    styleClass="dashboard"
                                    id="data" rowIndexVar="variable"
                                    value="#{journalController.paginator.visibleItems}" var="action"
                                    border="0"  cellspacing="0" cellpadding="0"
                                    rendered="#{not empty journalController.paginator.visibleItems}"
                                    rowClasses="oddRow,evenRow"
                                    >
                                    <f:facet name="header">
                                        <t:htmlTag styleClass="dashboard-header-wrapper header-wrapper" value="div">
                                            <t:htmlTag styleClass="items-count" value="div" rendered="#{not empty journalController.paginator.visibleItems}">
                                                    <e:text value="#{msgs['JOURNAL.TEXT.ACTIONS']}">
                                                        <f:param
                                                            value="#{journalController.paginator.firstVisibleNumber + 1}" />
                                                        <f:param
                                                            value="#{journalController.paginator.lastVisibleNumber + 1}" />
                                                        <f:param
                                                            value="#{journalController.paginator.totalItemsCount}" />
                                                    </e:text>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                    </f:facet>


                                    <t:column style="cursor: pointer" onclick="selectTicket(#{variable});" >
                                        <f:facet name="header">
                                            <t:htmlTag value="div" styleClass="column-header">
                                                <e:text value="#{msgs['JOURNAL.HEADER.TICKET']}" styleClass="column-label"/>
                                            </t:htmlTag>
                                        </f:facet>

                                        <e:text value=" #{msgs['JOURNAL.TEXT.TICKET']}" >
                                            <f:param value="#{action.ticket.id}" />
                                            <f:param value="#{action.ticket.label}" />
                                        </e:text>
                                    </t:column>

                                     <t:column style="cursor: pointer" onclick="selectTicket(#{variable});" >
                                         <f:facet name="header">
                                             <t:htmlTag value="div" styleClass="column-header">

                                             </t:htmlTag>
                                         </f:facet>

                                         <e:text value="#{msgs['JOURNAL.TEXT.CATEGORY']}" >
                                             <f:param value="#{action.ticket.department.label}" />
                                             <f:param value="#{action.ticket.category.label}" />
                                         </e:text>
                                     </t:column>

                                    <t:column style="cursor: pointer" onclick="selectTicket(#{variable});" >
                                        <f:facet name="header">
                                            <t:htmlTag value="div" styleClass="column-header">
                                                <e:text value="#{msgs['JOURNAL.HEADER.ACTION']}" styleClass="column-label"/>
                                            </t:htmlTag>
                                        </f:facet>

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
                                <h:panelGroup>
                                    <h:panelGroup style="display: none" rendered="#{journalController.paginator.lastPageNumber != 0}">
                                             <h:panelGroup
                                                 rendered="#{not journalController.paginator.firstPage}">
                                                 <e:commandButton id="pageFirst"
                                                     value="#{msgs['PAGINATION.BUTTON.FIRST']}"
                                                     action="#{journalController.paginator.gotoFirstPage}" />
                                                 <e:commandButton id="pagePrevious"
                                                     value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
                                                     action="#{journalController.paginator.gotoPreviousPage}" />
                                             </h:panelGroup>
                                             <h:panelGroup rendered="#{not journalController.paginator.lastPage}">
                                                 <e:commandButton id="pageNext"
                                                     value="#{msgs['PAGINATION.BUTTON.NEXT']}"
                                                     action="#{journalController.paginator.gotoNextPage}" />
                                                 <e:commandButton id="pageLast"
                                                     value="#{msgs['PAGINATION.BUTTON.LAST']}"
                                                     action="#{journalController.paginator.gotoLastPage}" />
                                             </h:panelGroup>
                                    </h:panelGroup>
                                    <h:panelGroup styleClass="dashboard-paginator" rendered="#{not empty journalController.paginator.visibleItems}">
                                        <%@include file="_controlJournalPanelPages.jsp"%>
                                    </h:panelGroup>
                            </h:panelGroup>
                        </f:facet>
                                </e:dataTable>
                                <e:paragraph value="#{msgs['JOURNAL.TEXT.NO_ACTION']}" rendered="#{empty journalController.paginator.visibleItems}" />
                            </e:form>

                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
                <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
                </t:htmlTag>
        </t:htmlTag>
</e:page>

