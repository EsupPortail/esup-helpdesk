<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanEditDepartmentProperties}">
 <t:htmlTag id="departmentFaqLinks" value="div" styleClass="page-wrapper departmentFaqLinks">
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
                                id="departmentFaqLinksForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['DEPARTMENT_FAQ_LINKS.TITLE']}"/>
                                                 </t:htmlTag>
                                                 <t:htmlTag value="span" styleClass="subtitle title">
                                                    <h:outputText value=" #{departmentsController.department.label}" escape="false" />
                                                 </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                      </t:htmlTag>

                                      <t:htmlTag value="div" styleClass="form-block form-header">
                                            <t:htmlTag value="div" styleClass="form-item">
                                                <e:commandButton id="cancelButton" action="back" value="#{msgs['CATEGORIES.BUTTON.BACK']}" immediate="true" />
                                            </t:htmlTag>
                                      </t:htmlTag>

                                      <t:htmlTag value="div" styleClass="form-block" rendered="#{empty departmentsController.faqLinks}">
                                            <t:htmlTag value="div" styleClass="form-item" >
                                                       <e:paragraph value="#{msgs['DEPARTMENT_FAQ_LINKS.TEXT.NO_FAQ_LINK']}" />
                                           </t:htmlTag>
                                      </t:htmlTag>

                                      <t:htmlTag value="div" styleClass="form-block current-faq-links-list" rendered="#{not empty departmentsController.faqLinks}" >
                                            <t:htmlTag value="div" styleClass="form-item" >
                                                <e:dataTable
                                                    id="faqLinkData" rowIndexVar="variable"
                                                    value="#{departmentsController.faqLinks}"
                                                    var="faqLink" border="0" cellspacing="0"
                                                    cellpadding="0">
                                                    <t:column>
                                                         <h:panelGroup>
                                                             <e:text value="- #{faqLink.faq.label}" />
                                                         </h:panelGroup>
                                                    </t:column>
                                                    <t:column style="cursor: pointer">
                                                       <h:panelGroup onclick="buttonClick('departmentFaqLinksForm:faqLinkData:#{variable}:deleteButton');" >
                                                            <t:htmlTag value="i" styleClass="fas fa-trash-alt fa-2x"/>
                                                        </h:panelGroup>
                                                        <e:commandButton id="deleteButton" value="-" style="display: none"
                                                            action="#{departmentsController.deleteDepartmentFaqLink}" >
                                                            <t:updateActionListener value="#{faqLink}"
                                                                property="#{departmentsController.faqLinkToDelete}" />
                                                        </e:commandButton>
                                                    </t:column>
                                                </e:dataTable>
                                           </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-block treeview readonly-style">
                                                <t:htmlTag value="div" styleClass="form-item" >
                                                    <t:tree2 id="tree" value="#{departmentsController.faqTree}"
                                                        var="node" varNodeToggler="t" clientSideToggle="true"
                                                        showRootNode="true" >
                                                        <f:facet name="root">
                                                            <h:panelGroup>
                                                                    <e:text value=" #{msgs['DEPARTMENT_FAQ_LINKS.TEXT.TREE_ROOT_LABEL']}" />
                                                            </h:panelGroup>
                                                        </f:facet>
                                                        <f:facet name="department">
                                                            <h:panelGroup>
                                                                    <e:text value=" #{node.department.label}" />
                                                            </h:panelGroup>
                                                        </f:facet>
                                                        <f:facet name="faq">
                                                            <h:panelGroup>
                                                                <h:panelGroup style="cursor: pointer; white-space: nowrap"
                                                                    onclick="simulateLinkClick('departmentFaqLinksForm:tree:#{node.identifier}:selectFaq');return false;">
                                                                    <e:text value=" #{node.faq.label}" styleClass="link"/>
                                                                </h:panelGroup>
                                                                <e:commandButton
                                                                    value="->" id="selectFaq" style="display: none"
                                                                    action="#{departmentsController.addDepartmentFaqLink}" >
                                                                    <t:updateActionListener value="#{node.faq}" property="#{departmentsController.faqToLink}" />
                                                                </e:commandButton>
                                                            </h:panelGroup>
                                                        </f:facet>
                                                    </t:tree2>
                                               </t:htmlTag>
                                      </t:htmlTag>
                            </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
           <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
           </t:htmlTag>
        </t:htmlTag>
</e:page>
