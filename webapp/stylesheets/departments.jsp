<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}" >
<script type="text/javascript">
	function selectDepartment(index) {
		simulateLinkClick('departmentsForm:data:' + index + ':selectDepartmentButton');
	}
</script>
		   <t:htmlTag id="departments" value="div" styleClass="page-wrapper departments">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>
                    <t:htmlTag value="main" styleClass="content">
                        <t:htmlTag value="div" styleClass="content-inner">
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
                                  <t:htmlTag value="div" styleClass="message">
                                      <e:messages/>
                                  </t:htmlTag>
                                  <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['DEPARTMENTS.TITLE']}"/>
                                                 </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                   </t:htmlTag>


                            <e:dataTable id="data" value="#{departmentsController.departmentPaginator.visibleItems}"
                                var="department" rowIndexVar="variable" border="0"
                                style="width: 100%" cellspacing="0" cellpadding="0"
                                rowClasses="oddRow,evenRow" rowId="#{department.id}"
                                styleClass="dashboard">
                                <f:facet name="header">
                                    <t:htmlTag styleClass="dashboard-header-wrapper header-wrapper" value="div">
                                        <t:htmlTag styleClass="items-count" value="div" rendered="#{not empty departmentsController.departmentPaginator.visibleItems}">
                                                    <e:text value="#{msgs['DEPARTMENTS.TEXT.DEPARTMENTS']}">
                                                        <f:param value="#{departmentsController.departmentPaginator.firstVisibleNumber + 1}" />
                                                        <f:param value="#{departmentsController.departmentPaginator.lastVisibleNumber + 1}" />
                                                        <f:param value="#{departmentsController.departmentPaginator.totalItemsCount}" />
                                                    </e:text>
                                        </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="department-filter">
	                                        <t:htmlTag value="div" styleClass="block form-block">
							                    <t:htmlTag value="div" styleClass="form-item">
							                        <e:outputLabel for="filterDepartment" value="#{msgs['TICKET_ACTION.SEARCH.DEPARTMENT']}" />
							                        <e:inputText id="filterDepartment"  title="Recherche" value="#{sessionController.departmentFilter}" size="15" onkeypress="if (event.keyCode == 13) { simulateLinkClick('ticketActionForm:filterDepartmentButton'); return false; }" />
							                    </t:htmlTag>
							                    <t:htmlTag value="div" styleClass="form-item">
							                        <e:commandButton id="filterDepartmentButton"
							                                    styleClass="button--secondary"
							                                    value="#{msgs['SEARCH.BUTTON.FILTER_CATEGORY']}"
							                                    action="#{departmentsController.enter}" />
							                        <e:commandButton id="cancelFilterDepartmentButton"
							                            style ="visibility: hidden"
							                            styleClass="button--cancel"
							                            value="#{msgs['SEARCH.BUTTON.FILTER_CATEGORY.CLEAR']}"
							                            action="#{departmentsController.resetEnter}" />
							                    </t:htmlTag>
								       		</t:htmlTag>
								        </t:htmlTag>
                                        <t:htmlTag styleClass="form-block items-sort" value="div">
                                                    <t:htmlTag styleClass="form-item" value="div">
                                                        <e:selectOneMenu value="#{departmentsController.departmentPaginator.pageSize}" onchange="javascript:{simulateLinkClick('departmentsForm:data:changeButton');}">
                                                            <f:selectItems value="#{departmentsController.departmentPaginator.pageSizeItems}" />
                                                        </e:selectOneMenu>
                                                        <e:commandButton value="sss" id="changeButton"
                                                            action="#{departmentsController.departmentPaginator.forceReload}" />
                                                     </t:htmlTag>
                                                    <t:htmlTag styleClass="form-item" value="div" rendered="#{departmentsController.currentUserCanManageDepartments}">
                                                        <e:selectOneMenu value="#{departmentsController.departmentsSortOrder}">
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
                                                            id="sortDepartmentsButton" styleClass="button--secondary"
                                                            action="#{departmentsController.reorderDepartments}" />
                                                    </t:htmlTag>
                                        </t:htmlTag>
                                    </t:htmlTag>
                                </f:facet>


                                <t:column style="cursor: pointer" onclick="selectDepartment(#{variable});">
                                    <f:facet name="header">
                                         <t:htmlTag value="div" styleClass="column-header">
                                            <e:text value="#{msgs['DEPARTMENTS.TEXT.HEADER.LABEL']}"/>
                                        </t:htmlTag>
                                    </f:facet>
                                     <t:htmlTag value="span">
                                        <h:outputText value="#{department.label}"/>
                                    </t:htmlTag>
                                </t:column>

                                <t:column style="cursor: pointer" onclick="selectDepartment(#{variable});" >
                                    <f:facet name="header">
                                         <t:htmlTag value="div" styleClass="column-header">
                                            <e:text value="#{msgs['DEPARTMENTS.TEXT.HEADER.XLABEL']}"/>
                                        </t:htmlTag>
                                    </f:facet>
                                     <t:htmlTag value="span">
                                        <h:outputText value="#{department.xlabel}"/>
                                    </t:htmlTag>
                                </t:column>

                                <t:column style="cursor: pointer" onclick="selectDepartment(#{variable});" >

                                    <h:panelGroup styleClass="#{department.enabled ? 'department-active' : 'department-inactive'}">
                                     <h:outputText value="#{msgs[department.enabled ? 'DEPARTMENTS.TEXT.ENABLED.TRUE' : 'DEPARTMENTS.TEXT.ENABLED.FALSE']}" escape="false" />
                                    </h:panelGroup>

                                </t:column>

                                <t:column style="cursor: pointer" onclick="selectDepartment(#{variable});" >

                                    <h:panelGroup rendered="#{department.virtual}" style="cursor: default">
                                       <t:htmlTag value="i" styleClass="redirect far fa-2x fa-arrow-alt-circle-right"/>
                                    </h:panelGroup>
                                    <e:text
                                        value="#{msgs['DEPARTMENTS.TEXT.REDIRECTION']}"
                                        rendered="#{department.virtual}" >
                                        <f:param
                                            value="#{department.realDepartment.label}" />
                                    </e:text>
                                </t:column>

                                <t:column styleClass="move" rendered="#{departmentsController.currentUserCanManageDepartments}" >
                                    <h:panelGroup rendered="#{not departmentsController.departmentPaginator.firstPage or variable != 0}">
                                        <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentsForm:data:#{variable}:moveDepartmentFirst');">
                                            <t:htmlTag value="i" styleClass="fas fa-angle-double-up"/>
                                        </h:panelGroup>
                                        <e:commandButton value="^^" id="moveDepartmentFirst" style="display: none"
                                            action="#{departmentsController.moveDepartmentFirst}" >
                                            <t:updateActionListener value="#{department}"
                                                property="#{departmentsController.departmentToUpdate}" />
                                        </e:commandButton>
                                    </h:panelGroup>
                                </t:column>

                                <t:column styleClass="move" rendered="#{departmentsController.currentUserCanManageDepartments}" >
                                    <h:panelGroup rendered="#{not departmentsController.departmentPaginator.firstPage or variable != 0}">
                                        <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentsForm:data:#{variable}:moveDepartmentUp');">
                                                   <t:htmlTag value="i" styleClass="fas fa-angle-up"/>
                                               </h:panelGroup>
                                        <e:commandButton value="^" id="moveDepartmentUp" style="display: none"
                                            action="#{departmentsController.moveDepartmentUp}" >
                                            <t:updateActionListener value="#{department}"
                                                property="#{departmentsController.departmentToUpdate}" />
                                        </e:commandButton>
                                    </h:panelGroup>
                                </t:column>

                                <t:column styleClass="move" rendered="#{departmentsController.currentUserCanManageDepartments}" >
                                    <h:panelGroup rendered="#{not departmentsController.departmentPaginator.lastPage or variable != departmentsController.departmentPaginator.visibleItemsCount - 1}">
                                        <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentsForm:data:#{variable}:moveDepartmentDown');">
                                            <t:htmlTag value="i" styleClass="fas fa-angle-down"/>
                                        </h:panelGroup>
                                        <e:commandButton value="v" id="moveDepartmentDown" style="display: none"
                                            action="#{departmentsController.moveDepartmentDown}" >
                                            <t:updateActionListener value="#{department}"
                                                property="#{departmentsController.departmentToUpdate}" />
                                        </e:commandButton>
                                    </h:panelGroup>
                                </t:column>

                                <t:column styleClass="move" rendered="#{departmentsController.currentUserCanManageDepartments}" >
                                    <h:panelGroup rendered="#{not departmentsController.departmentPaginator.lastPage or variable != departmentsController.departmentPaginator.visibleItemsCount - 1}">
                                        <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentsForm:data:#{variable}:moveDepartmentLast');">
                                            <t:htmlTag value="i" styleClass="fas fa-angle-double-down"/>
                                        </h:panelGroup>
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
                                    <h:panelGroup>
                                        <h:panelGroup style="display: none" rendered="#{departmentsController.departmentPaginator.lastPageNumber != 0}">
                                                 <h:panelGroup
                                                     rendered="#{not departmentsController.departmentPaginator.firstPage}">
                                                     <e:commandButton id="pageFirst"
                                                         value="#{msgs['PAGINATION.BUTTON.FIRST']}"
                                                         action="#{departmentsController.departmentPaginator.gotoFirstPage}" />
                                                     <e:commandButton id="pagePrevious"
                                                         value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
                                                         action="#{departmentsController.departmentPaginator.gotoPreviousPage}" />
                                                 </h:panelGroup>
                                                 <h:panelGroup rendered="#{not departmentsController.departmentPaginator.lastPage}">
                                                     <e:commandButton id="pageNext"
                                                         value="#{msgs['PAGINATION.BUTTON.NEXT']}"
                                                         action="#{departmentsController.departmentPaginator.gotoNextPage}" />
                                                     <e:commandButton id="pageLast"
                                                         value="#{msgs['PAGINATION.BUTTON.LAST']}"
                                                         action="#{departmentsController.departmentPaginator.gotoLastPage}" />
                                                 </h:panelGroup>
                                        </h:panelGroup>
                                        <h:panelGroup styleClass="dashboard-paginator" rendered="#{not empty departmentsController.departmentPaginator.visibleItems}">
                                            <%@include file="_departmentsPages.jsp"%>
                                        </h:panelGroup>
                                    </h:panelGroup>
                                </f:facet>
                            </e:dataTable>

                            <t:htmlTag value="div" styleClass="form-block"  rendered="#{departmentsController.currentUserCanManageDepartments}">
                                <t:htmlTag value="div" styleClass="form-item">
                                        <e:commandButton styleClass="button--secondary" id="addDepartmentButton" action="addDepartment"
                                            value="#{msgs['DEPARTMENTS.BUTTON.ADD_DEPARTMENT']}" />
                                </t:htmlTag>
                            </t:htmlTag>

                            <t:htmlTag value="div" styleClass="form-block"  rendered="#{empty departmentsController.departmentPaginator.visibleItems}">
                                 <t:htmlTag value="div" styleClass="form-item">
                                                  <e:text value="#{msgs['DEPARTMENTS.TEXT.NO_DEPARTMENT']}" />
                                 </t:htmlTag>
                             </t:htmlTag>

                        </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
           <t:htmlTag value="footer" styleClass="footer">
				<t:aliasBean alias="#{controller}" value="#{departmentsController}" >
				    <%@include file="_footer.jsp"%>
				</t:aliasBean>
           </t:htmlTag>
        </t:htmlTag>

	<script type="text/javascript">
        hideButton("departmentsForm:data:changeButton");
    </script>
</e:page>
