<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="administrators"
	locale="#{sessionController.locale}" >

		   <t:htmlTag id="administrators" value="div" styleClass="page-wrapper administrators">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>

                    <t:htmlTag value="main" styleClass="content">
                        <t:htmlTag value="div" styleClass="content-inner">
                        <e:form
                            freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                            showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                            showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                            id="administratorsForm" rendered="#{administratorsController.pageAuthorized}" >
                            <h:panelGroup rendered="#{not administratorsController.pageAuthorized}" >
                                 <h:panelGroup rendered="#{administratorsController.currentUser == null}" >
                                     <%@include file="_auth.jsp"%>
                                 </h:panelGroup>
                                 <h:panelGroup rendered="#{administratorsController.currentUser != null}" >
                                     <e:messages/>
                                 </h:panelGroup>
                            </h:panelGroup>

                            <t:htmlTag value="div" styleClass="message">
                                    <e:messages/>
                            </t:htmlTag>

                            <t:htmlTag value="div" styleClass="dashboard-header">
                                        <t:htmlTag value="div" styleClass="controlPanel-title">
                                            <t:htmlTag value="h1">
                                                <t:htmlTag value="span">
                                                    <h:outputText value="#{msgs['ADMINISTRATORS.TITLE']}"/>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>
                            </t:htmlTag>


                            <e:dataTable styleClass="admin-users" rendered="#{not empty administratorsController.paginator.visibleItems}"
                                id="data" rowIndexVar="variable" width="100%"
                                value="#{administratorsController.paginator.visibleItems}"
                                var="admin" border="0" cellspacing="0" cellpadding="0">
                                <f:facet name="header">
                                    <t:htmlTag styleClass="dashboard-header-wrapper header-wrapper" value="div">
                                        <t:htmlTag styleClass="items-count" value="div">
                                                <e:text value="#{msgs['ADMINISTRATORS.TEXT.ADMINISTRATORS']}">
                                                    <f:param value="#{administratorsController.paginator.firstVisibleNumber + 1}" />
                                                    <f:param value="#{administratorsController.paginator.lastVisibleNumber + 1}" />
                                                    <f:param value="#{administratorsController.paginator.totalItemsCount}" />
                                                </e:text>
                                        </t:htmlTag>
                                    </t:htmlTag>

                                </f:facet>

                                <t:column>
                                    <e:bold value="#{userFormatter[admin]}" />
                                </t:column>

                                <t:column>
                                        <h:panelGroup onclick="buttonClick('administratorsForm:data:#{variable}:deleteAdminButton');" rendered="#{administratorsController.currentUser.admin and administratorsController.currentUser.id != admin.id}">
                                            <t:htmlTag value="i" styleClass="fas fa-trash-alt fa-2x"/>
                                        </h:panelGroup>
                                        <e:commandButton style="display:none" id="deleteAdminButton"
                                            rendered="#{administratorsController.currentUser.admin and administratorsController.currentUser.id != admin.id}"
                                            action="deleteAdmin">
                                            <t:updateActionListener value="#{admin}" property="#{administratorsController.userToDelete}" />
                                        </e:commandButton>
                                </t:column>

                                <f:facet name="footer">
                                       <h:panelGroup>
                                         <h:panelGroup style="display: none" rendered="#{administratorsController.paginator.lastPageNumber != 0}">
                                                  <h:panelGroup
                                                      rendered="#{not administratorsController.paginator.firstPage}">
                                                      <e:commandButton id="pageFirst"
                                                          value="#{msgs['PAGINATION.BUTTON.FIRST']}"
                                                          action="#{administratorsController.paginator.gotoFirstPage}" />
                                                      <e:commandButton id="pagePrevious"
                                                          value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
                                                          action="#{administratorsController.paginator.gotoPreviousPage}" />
                                                  </h:panelGroup>
                                                  <h:panelGroup rendered="#{not administratorsController.paginator.lastPage}">
                                                      <e:commandButton id="pageNext"
                                                          value="#{msgs['PAGINATION.BUTTON.NEXT']}"
                                                          action="#{administratorsController.paginator.gotoNextPage}" />
                                                      <e:commandButton id="pageLast"
                                                          value="#{msgs['PAGINATION.BUTTON.LAST']}"
                                                          action="#{administratorsController.paginator.gotoLastPage}" />
                                                  </h:panelGroup>
                                         </h:panelGroup>
                                         <h:panelGroup styleClass="dashboard-paginator" rendered="#{not empty administratorsController.paginator.visibleItems}">
                                             <%@include file="_administratorsPages.jsp"%>
                                         </h:panelGroup>
                                       </h:panelGroup>                                   
                                </f:facet>
                            </e:dataTable>

                            <t:htmlTag value="div" styleClass="form-block" rendered="#{administratorsController.currentUserCanAddAdmin}">
                                <t:htmlTag value="div" styleClass="form-item">
                                        <e:commandButton  id="addAdminButton" action="addAdmin" styleClass="button--secondary"
                                            value="#{msgs['ADMINISTRATORS.BUTTON.ADD_ADMIN']}" />
                                </t:htmlTag>
                            </t:htmlTag>


                            <t:htmlTag value="div" styleClass="dashboard-header">
                                        <t:htmlTag value="div" styleClass="controlPanel-title">
                                            <t:htmlTag value="h1">
                                                <t:htmlTag value="span">
                                                    <h:outputText value="#{msgs['ADMINISTRATORS.HEADER.INDEXING_STATISTICS']}"/>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>
                            </t:htmlTag>
                            <t:htmlTag value="div" styleClass="form-block" rendered="#{domainService.useLdap}">
                                     <h:panelGroup rendered="#{not empty administratorsController.ldapStatistics}">
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
                            </t:htmlTag>

                            <t:htmlTag value="div" styleClass="dashboard-header" rendered="#{domainService.useLdap}">
                                        <t:htmlTag value="div" styleClass="controlPanel-title">
                                            <t:htmlTag value="h1">
                                                <t:htmlTag value="span">
                                                    <h:outputText value="#{msgs['ADMINISTRATORS.HEADER.LDAP_STATISTICS']}"/>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>
                            </t:htmlTag>
                            <t:htmlTag value="div" styleClass="form-block">
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

                            </t:htmlTag>
                        </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
           <t:htmlTag value="footer" styleClass="footer">
	           	<t:aliasBean alias="#{controller}" value="#{administratorsController}" >
				    <%@include file="_footer.jsp"%>
				</t:aliasBean>
           </t:htmlTag>
        </t:htmlTag>

</e:page>
