<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}" >

<script type="text/javascript">
	function selectManager(index) {
		simulateLinkClick('departmentViewForm:data:'+index+':selectManager');
	}
</script>

		  <t:htmlTag id="departmentView" value="div" styleClass="page-wrapper departmentView">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>
                    <t:htmlTag value="main" styleClass="content">
                        <t:htmlTag value="div" styleClass="content-inner">
                            <h:panelGroup rendered="#{not departmentsController.currentUserCanViewDepartment}" >
                                <h:panelGroup rendered="#{departmentSelectionController.currentUser == null}" >
                                    <%@include file="_auth.jsp"%>
                                </h:panelGroup>
                                <h:panelGroup rendered="#{departmentSelectionController.currentUser != null}" >
                                    <e:messages/>
                                </h:panelGroup>
                            </h:panelGroup>
                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="departmentViewForm" rendered="#{departmentsController.currentUserCanViewDepartment}" >
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['DEPARTMENT_VIEW.TITLE']}"/>
                                                 </t:htmlTag>
                                                 <t:htmlTag value="span" styleClass="subtitle title">
                                                    <h:outputText value=" #{departmentsController.department.label}" escape="false" />
                                                 </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                      </t:htmlTag>

                                      <t:htmlTag styleClass="region" value="div">
                                        <t:htmlTag styleClass="tabs" value="ul">
                                            <t:htmlTag id="departemnt-managers" styleClass="tab-link current" value="li">
                                                <h:outputText value="#{msgs['DEPARTMENT_VIEW.HEADER.MANAGERS']} " />
                                            </t:htmlTag>
                                            <t:htmlTag id="departemnt-properties" styleClass="tab-link " value="li">
                                                 <h:outputText value="#{msgs['DEPARTMENT_VIEW.HEADER.PROPERTIES']} " />
                                            </t:htmlTag>
                                             <t:htmlTag id="departemnt-invitations" styleClass="tab-link" value="li">
                                                  <h:outputText value="#{msgs['DEPARTMENT_VIEW.HEADER.INVITATIONS']} " />
                                             </t:htmlTag>
                                            <t:htmlTag id="departemnt-categories" styleClass="tab-link" value="li">
                                                 <h:outputText value="#{msgs['DEPARTMENT_VIEW.HEADER.CATEGORIES']} " />
                                            </t:htmlTag>
                                            <t:htmlTag id="departemnt-faq-links" styleClass="tab-link" value="li">
                                                 <h:outputText value="#{msgs['DEPARTMENT_VIEW.HEADER.FAQ_LINKS']} " />
                                            </t:htmlTag>
                                            <t:htmlTag id="departemnt-redirections" styleClass="tab-link" value="li">
                                                 <h:outputText value="#{msgs['DEPARTMENT_VIEW.HEADER.REDIRECTIONS']} " />
                                            </t:htmlTag>
                                            <t:htmlTag id="departemnt-delete" value="li" rendered="#{departmentsController.currentUserCanDeleteDepartment}">
                                                 <t:htmlTag value="div" styleClass="form-item">
                                                    <e:commandButton  id="deleteButton" action="#{departmentsController.deleteDepartment}"
                                                        value="#{msgs['DEPARTMENT_VIEW.BUTTON.DELETE_DEPARTMENT']}" immediate="true" />
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag id="tab-departemnt-managers" styleClass="tab-content current" value="div">
                                              <%@include file="_departmentViewManagers.jsp"%>
                                      </t:htmlTag>
                                      <t:htmlTag id="tab-departemnt-properties" styleClass="tab-content" value="div">
                                              <%@include file="_departmentViewProperties.jsp"%>
                                      </t:htmlTag>
                                      <t:htmlTag id="tab-departemnt-invitations" styleClass="tab-content" value="div">
                                              <%@include file="_departmentViewInvitations.jsp"%>
                                      </t:htmlTag>
                                      <t:htmlTag id="tab-departemnt-categories" styleClass="tab-content" value="div">
                                              <%@include file="_departmentViewCategories.jsp"%>
                                      </t:htmlTag>
                                      <t:htmlTag id="tab-departemnt-faq-links" styleClass="tab-content" value="div">
                                              <%@include file="_departmentViewFaqLinks.jsp"%>
                                      </t:htmlTag>
                                      <t:htmlTag id="tab-departemnt-redirections" styleClass="tab-content" value="div">
                                        <t:htmlTag value="div" styleClass="region">
                                            <e:bold value=" #{msgs['DEPARTMENTS.TEXT.REDIRECTION.LISTE']}">
                                                <f:param value="#{departmentsController.department.label}" />
                                            </e:bold>
                                            <t:htmlTag value="ul">
                                                <t:dataList value="#{departmentsController.virtualDepartments}" var="virtualDepartment" rowIndexVar="virtualDepartmentIndex" >
                                                    <t:htmlTag value="li">
                                                        <e:text value=" #{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.REDIRECTION_VALUE_INVERTED']}">
                                                            <f:param value="#{virtualDepartment.label}" />
                                                        </e:text>
                                                    </t:htmlTag>
                                                </t:dataList>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="region">
                                            <e:bold value=" #{msgs['CATEGORIES.TEXT.REDIRECTION.LISTE']}">
                                                    <f:param value="#{departmentsController.department.label}" />
                                            </e:bold>
                                            <t:htmlTag value="ul">
                                                <t:dataList value="#{departmentsController.virtualCategories}" var="virtualCategorie" rowIndexVar="virtualCategorieIndex">
                                                    <t:htmlTag value="li">
                                                        <e:text
                                                            value=" #{msgs['CATEGORIES.TEXT.VIRTUAL_CATEGORY']}">
                                                            <f:param value="#{virtualCategorie.department.label}" />
                                                            <f:param value="#{virtualCategorie.label}" />
                                                        </e:text>
                                                     </t:htmlTag>
                                                </t:dataList>
                                              </t:htmlTag>
                                        </t:htmlTag>
                                      </t:htmlTag>

                                        <e:italic
                                            rendered="#{departmentsController.currentUserCanManageDepartments and not departmentsController.currentUserCanDeleteDepartment}"
                                            value="#{msgs['DEPARTMENT_VIEW.TEXT.CAN_NOT_DELETE_DEPARTMENT']} " />

                            </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
           <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
           </t:htmlTag>
        </t:htmlTag>
</e:page>
