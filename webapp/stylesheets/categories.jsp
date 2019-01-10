<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanViewCategories}">
 <t:htmlTag id="categories" value="div" styleClass="page-wrapper categories">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>
                    <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                        <t:htmlTag value="div" styleClass="content-inner">

                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="categoriesForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['CATEGORIES.TITLE']}"/>
                                                 </t:htmlTag>
                                                 <t:htmlTag value="span" styleClass="subtitle title">
                                                    <h:outputText value=" #{departmentsController.department.label}" escape="false" />
                                                 </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-block form-header">
                                            <t:htmlTag value="div" styleClass="form-item">
                                            <h:panelGroup id="actions">
                                                <e:selectOneMenu id="categoriesAction"
                                                    value="#{departmentsController.categoriesAction}">
                                                    <f:selectItems
                                                        value="#{departmentsController.categoriesActionItems}" />
                                                </e:selectOneMenu>
                                                <e:commandButton value="#{msgs['CATEGORIES.TEXT.ACTION.PROMPT']}"
                                                    id="actionsButton" styleClass="button--secondary"/>
                                                <h:panelGroup id="sortActions" styleClass="hideme" rendered="#{departmentsController.currentUserCanManageDepartmentCategories}" >
                                                    <e:selectOneMenu onchange="javascript:{simulateLinkClick('categoriesForm:sortCategoriesButton');}"
                                                        value="#{departmentsController.categoriesSortOrder}">
                                                        <f:selectItem
                                                            itemLabel="#{msgs['CATEGORIES.TEXT.SORT.PROMPT']}"
                                                            itemValue="" />
                                                        <f:selectItem
                                                            itemLabel="#{msgs['CATEGORIES.TEXT.SORT.LABEL']}"
                                                            itemValue="label" />
                                                        <f:selectItem
                                                            itemLabel="#{msgs['CATEGORIES.TEXT.SORT.XLABEL']}"
                                                            itemValue="xlabel" />
                                                        <f:selectItem
                                                            itemLabel="#{msgs['CATEGORIES.TEXT.SORT.REVERSE']}"
                                                            itemValue="reverse" />
                                                    </e:selectOneMenu>
                                                    <e:commandButton value="#{msgs['CATEGORIES.BUTTON.SORT_CATEGORIES']}"
                                                        id="sortCategoriesButton" style="display: none"
                                                        action="#{departmentsController.reorderCategories}" />
                                                </h:panelGroup>
                                            </h:panelGroup>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-item">
                                                <e:commandButton id="cancelButton" action="back" value="#{msgs['CATEGORIES.BUTTON.BACK']}" immediate="true" />
                                            </t:htmlTag>
                                      </t:htmlTag>


                                <t:htmlTag value="div" styleClass="form-block treeview readonly-style ">
                                    <t:htmlTag value="div" styleClass="form-item" >
                                    <t:tree2 rendered="#{departmentsController.categoryTree != null}"
                                        id="tree" value="#{departmentsController.categoryTree}"
                                        var="node" varNodeToggler="t" clientSideToggle="true"
                                        showRootNode="true" >
                                        <f:facet name="department">
                                            <t:htmlTag value="div" styleClass="department">

                                                <e:text styleClass="label" value=" #{msgs['CATEGORIES.TEXT.DEPARTMENT_LABEL']}" >
                                                    <f:param value="#{departmentsController.department.label}" />
                                                </e:text>
                                                <h:panelGroup styleClass="action" rendered="#{departmentsController.currentUserCanManageDepartmentCategories and departmentsController.categoriesAction == 'ADD_DELETE'}" >
                                                    <t:htmlTag value="span" styleClass="form-item form-submit">
                                                        <e:commandButton value="#{msgs['CATEGORIES.ALT.ADD']}" id="addButton"
                                                            action="addCategory" >
                                                            <t:updateActionListener value="#{null}"
                                                                property="#{departmentsController.categoryToAdd.parent}" />
                                                            <t:updateActionListener value="#{departmentsController.department}"
                                                                property="#{departmentsController.categoryToAdd.department}" />
                                                        </e:commandButton>
                                                    </t:htmlTag>
                                                </h:panelGroup>

                                                <h:panelGroup styleClass="properties" rendered="#{departmentsController.categoriesAction == 'PROPERTIES'}" >
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.AUTO_EXPIRE']}"
                                                        rendered="#{node.department.autoExpire != null}" >
                                                        <f:param value="#{node.department.autoExpire}" />
                                                    </e:text>
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.DEFAULT_LABEL']}"
                                                        rendered="#{node.department.defaultTicketLabel != null}" />
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.DEFAULT_MESSAGE']}"
                                                        rendered="#{node.department.defaultTicketMessage != null}" />
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.PRIORITY']}"
                                                        rendered="#{node.department.defaultTicketPriority != 0}" >
                                                        <f:param value="#{msgs[priorityI18nKeyProvider[node.department.defaultTicketPriority]]}" />
                                                    </e:text>
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.SCOPE']}"
                                                        rendered="#{node.department.defaultTicketScope != 'DEFAULT'}" >
                                                        <f:param value="#{msgs[ticketScopeI18nKeyProvider[node.department.defaultTicketScope]]}" />
                                                    </e:text>
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.ALGORITHM']}"
                                                        rendered="#{node.department.assignmentAlgorithmName != null}" >
                                                        <f:param value="#{assignmentAlgorithmI18nDescriptionProvider[node.department.assignmentAlgorithmName]}" />
                                                    </e:text>
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.HIDDEN_TO_APPLICATION_USERS']}"
                                                        rendered="#{node.department.hideToExternalUsers}" />
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.MONITORING_VALUE']}"
                                                        rendered="#{node.department.monitoringEmail != null and node.department.monitoringLevel != 0}" >
                                                        <f:param value="#{node.department.monitoringEmail}" />
                                                    </e:text>

                                                    <h:panelGroup rendered="#{departmentsController.currentUserCanEditDepartmentProperties}" >
                                                        <t:htmlTag value="span" styleClass="form-item form-submit">
                                                            <e:commandButton value="#{msgs['CATEGORIES.BUTTON.EDIT_PROPERTIES']}"
                                                                id="editPropertiesButton" action="editDepartment" >
                                                                <t:updateActionListener value="#{node.department}"
                                                                    property="#{departmentsController.departmentToUpdate}" />
                                                            </e:commandButton>
                                                        </t:htmlTag>
                                                    </h:panelGroup>
                                                </h:panelGroup>
                                            </t:htmlTag>
                                        </f:facet>

                                        <f:facet name="category">
                                            <t:htmlTag value="div" styleClass="category">
                                                <e:text value=" #{msgs['CATEGORIES.TEXT.CATEGORY_LABEL']} " >
                                                    <f:param value="#{node.category.order}" />
                                                    <f:param value="#{node.category.label}" />
                                                    <f:param value="#{node.category.xlabel}" />
                                                </e:text>
                                                <h:panelGroup  rendered="#{departmentsController.currentUserCanManageDepartmentCategories and departmentsController.categoriesAction == 'MOVE'}" >
                                                    <h:panelGroup rendered="#{not node.first}" >
                                                        <h:panelGroup styleClass="order" style="cursor: pointer" onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:moveFirstButton');">
                                                            <t:htmlTag value="i" styleClass="fas fa-angle-double-up"/>
                                                        </h:panelGroup>
                                                        <e:commandButton value="^^" id="moveFirstButton" style="display: none"
                                                            action="#{departmentsController.moveCategoryFirst}" >
                                                            <t:updateActionListener value="#{node.category}"
                                                                property="#{departmentsController.categoryToUpdate}" />
                                                        </e:commandButton>
                                                        <h:panelGroup styleClass="order" style="cursor: pointer" onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:moveUpButton');">
                                                            <t:htmlTag value="i" styleClass="fas fa-angle-up"/>
                                                        </h:panelGroup>
                                                        <e:commandButton value="^" id="moveUpButton" style="display: none"
                                                            action="#{departmentsController.moveCategoryUp}" >
                                                            <t:updateActionListener value="#{node.category}"
                                                                property="#{departmentsController.categoryToUpdate}" />
                                                        </e:commandButton>
                                                    </h:panelGroup>
                                                    <h:panelGroup rendered="#{not node.last}" >
                                                        <h:panelGroup styleClass="order" style="cursor: pointer" onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:moveDownButton');">
                                                            <t:htmlTag value="i" styleClass="fas fa-angle-down"/>
                                                        </h:panelGroup>
                                                        <e:commandButton value="v" id="moveDownButton" style="display: none"
                                                            action="#{departmentsController.moveCategoryDown}" >
                                                            <t:updateActionListener value="#{node.category}"
                                                                property="#{departmentsController.categoryToUpdate}" />
                                                        </e:commandButton>
                                                        <h:panelGroup styleClass="order" style="cursor: pointer" onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:moveLastButton');">
                                                            <t:htmlTag value="i" styleClass="fas fa-angle-double-down"/>
                                                        </h:panelGroup>
                                                        <e:commandButton value="vv" id="moveLastButton" style="display: none"
                                                            action="#{departmentsController.moveCategoryLast}" >
                                                            <t:updateActionListener value="#{node.category}"
                                                                property="#{departmentsController.categoryToUpdate}" />
                                                        </e:commandButton>
                                                    </h:panelGroup>

                                                    <h:panelGroup styleClass="action">
                                                        <t:htmlTag value="div" styleClass="form-item form-submit">
                                                            <e:commandButton value="#{msgs['CATEGORIES.ALT.MOVE']}"  id="moveButton"
                                                                action="#{departmentsController.gotoMoveCategory}" >
                                                                <t:updateActionListener value="#{node.category}"
                                                                    property="#{departmentsController.categoryToUpdate}" />
                                                            </e:commandButton>
                                                        </t:htmlTag>
                                                    </h:panelGroup>
                                                </h:panelGroup>

                                                <h:panelGroup styleClass="action" rendered="#{departmentsController.currentUserCanManageDepartmentCategories and departmentsController.categoriesAction == 'ADD_DELETE'}" >
                                                    <h:panelGroup rendered="#{not node.category.virtual}" >
                                                        <t:htmlTag value="div" styleClass="form-item form-submit">
                                                            <e:commandButton value="#{msgs['CATEGORIES.ALT.ADD']}" id="addButton"
                                                                action="addCategory" >
                                                                <t:updateActionListener value="#{node.category}"
                                                                    property="#{departmentsController.categoryToAdd.parent}" />
                                                                <t:updateActionListener value="#{departmentsController.department}"
                                                                    property="#{departmentsController.categoryToAdd.department}" />
                                                            </e:commandButton>
                                                         </t:htmlTag>
                                                    </h:panelGroup>

                                                    <h:panelGroup rendered="#{node.childCount == 0 and empty node.virtualCategories}" >
                                                        <t:htmlTag value="div" styleClass="form-item form-submit">
                                                            <e:commandButton value="#{msgs['CATEGORIES.ALT.DELETE']}" id="deleteButton"
                                                                action="#{departmentsController.deleteCategory}" >
                                                                <t:updateActionListener value="#{node.category}"
                                                                    property="#{departmentsController.categoryToUpdate}" />
                                                            </e:commandButton>
                                                        </t:htmlTag>
                                                    </h:panelGroup>
                                                </h:panelGroup>

                                                <h:panelGroup styleClass="properties" rendered="#{departmentsController.categoriesAction == 'PROPERTIES'}" >
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.AUTO_EXPIRE']}"
                                                        rendered="#{not node.category.virtual and node.category.autoExpire != null}" >
                                                        <f:param value="#{node.category.autoExpire}" />
                                                    </e:text>
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.DEFAULT_LABEL']}"
                                                        rendered="#{not node.category.virtual and node.category.defaultTicketLabel != null}" />
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.DEFAULT_MESSAGE']}"
                                                        rendered="#{not node.category.virtual and node.category.defaultTicketMessage != null}" />
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.PRIORITY']}"
                                                        rendered="#{not node.category.virtual and node.category.defaultTicketPriority != 0}" >
                                                        <f:param value="#{msgs[priorityI18nKeyProvider[node.category.defaultTicketPriority]]}" />
                                                    </e:text>
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.SCOPE']}"
                                                        rendered="#{not node.category.virtual and node.category.defaultTicketScope != 'DEFAULT'}" >
                                                        <f:param value="#{msgs[ticketScopeI18nKeyProvider[node.category.defaultTicketScope]]}" />
                                                    </e:text>
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.ALGORITHM']}"
                                                        rendered="#{not node.category.virtual and node.category.assignmentAlgorithmName != null}" >
                                                        <f:param value="#{assignmentAlgorithmI18nDescriptionProvider[node.category.assignmentAlgorithmName]}" />
                                                    </e:text>
                                                    <e:text value=" #{msgs['CATEGORIES.TEXT.HIDE_TO_EXTERNAL_USERS']}"
                                                        rendered="#{not node.category.virtual and node.category.hideToExternalUsers}" />
                                                    <h:panelGroup
                                                        rendered="#{not node.category.virtual and not node.category.inheritMonitoring}" >
                                                        <e:text value=" #{msgs['CATEGORIES.TEXT.MONITORING_NONE']}"
                                                            rendered="#{node.category.monitoringEmail == null or node.category.monitoringLevel == 0}" />
                                                        <e:text value=" #{msgs['CATEGORIES.TEXT.MONITORING_VALUE']}"
                                                            rendered="#{node.category.monitoringEmail != null and node.category.monitoringLevel != 0}" >
                                                            <f:param value="#{node.category.monitoringEmail}" />
                                                        </e:text>
                                                    </h:panelGroup>
                                                    <h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories}" >
                                                        <t:htmlTag value="span" styleClass="form-item form-submit">
                                                            <e:commandButton value="#{msgs['CATEGORIES.BUTTON.EDIT_PROPERTIES']}"
                                                                id="editPropertiesButton"
                                                                action="editCategory" >
                                                                <t:updateActionListener value="#{node.category}"
                                                                    property="#{departmentsController.categoryToUpdate}" />
                                                            </e:commandButton>
                                                        </t:htmlTag>
                                                    </h:panelGroup>
                                                    <h:panelGroup rendered="#{node.category.virtual}" >

                                                        <h:panelGroup style="cursor: default" >
                                                             <t:htmlTag value="i" styleClass="redirect far  fa-arrow-alt-circle-right"/>
                                                        </h:panelGroup>
                                                        <e:text value=" #{msgs['CATEGORIES.TEXT.REDIRECTION']}" >
                                                            <f:param value="#{node.category.realCategory.department.label}" />
                                                            <f:param value="#{node.category.realCategory.label}" />
                                                        </e:text>
                                                    </h:panelGroup>

                                                    <h:panelGroup styleClass="virtualCategories" rendered="#{not empty node.virtualCategories}" >
                                                        <h:panelGroup style="cursor: pointer" onclick="showHideElement('categoriesForm:tree:#{node.identifier}:virtualCategories');" >
                                                            <h:panelGroup style="cursor: default" >
                                                                 <t:htmlTag value="i" styleClass="redirect far  fa-arrow-alt-circle-left"/>
                                                            </h:panelGroup>
                                                            <e:text value=" #{msgs['CATEGORIES.TEXT.VIRTUAL_CATEGORIES_NUMBER']}" >
                                                                <f:param value="#{node.virtualCategoriesNumber}" />
                                                            </e:text>
                                                        </h:panelGroup>
                                                        <h:panelGroup id="virtualCategories" styleClass="virtualCategories-list"
                                                            rendered="#{not empty node.virtualCategories}" style="display: none" >
                                                            <t:htmlTag value="ul">
                                                                <t:dataList value="#{node.virtualCategories}" var="virtualCategory"
                                                                    rowIndexVar="virtualCategoryIndex">
                                                                    <t:htmlTag value="li">
                                                                        <e:italic value=" #{msgs['CATEGORIES.TEXT.VIRTUAL_CATEGORY']}" >
                                                                            <f:param value="#{virtualCategory.department.label}" />
                                                                            <f:param value="#{virtualCategory.label}" />
                                                                        </e:italic>
                                                                     </t:htmlTag>
                                                                </t:dataList>
                                                            </t:htmlTag>
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </h:panelGroup>
                                                <h:panelGroup rendered="#{departmentsController.categoriesAction == 'MEMBERS'}" >
                                                    <h:panelGroup styleClass="members" rendered="#{not node.category.inheritMembers}">
                                                        <h:panelGroup style="cursor: pointer"
                                                            onclick="showHideElement('categoriesForm:tree:#{node.identifier}:members');"
                                                            rendered="#{node.membersNumber != 0}" >
                                                            <e:text styleClass="link" value=" #{msgs['CATEGORIES.TEXT.MEMBERS_NUMBER']}">
                                                                <f:param value="#{node.membersNumber}" />
                                                            </e:text>
                                                        </h:panelGroup>
                                                        <e:text styleClass="no-members" rendered="#{node.membersNumber == 0}" value=" #{msgs['CATEGORIES.TEXT.NO_MEMBER']}" />
                                                            <h:panelGroup id="members" rendered="#{not node.category.inheritMembers and node.membersNumber != 0}"
                                                                style="display: none" styleClass="members-list">
                                                                 <t:htmlTag value="ul">
                                                                     <t:dataList value="#{node.members}" var="member" rowIndexVar="memberIndex">
                                                                          <t:htmlTag value="li">
                                                                            <e:text value="#{msgs['CATEGORIES.TEXT.MEMBER']}" >
                                                                                <f:param value="#{userFormatter[member.user]}" />
                                                                            </e:text>
                                                                          </t:htmlTag>
                                                                    </t:dataList>
                                                                </t:htmlTag>
                                                            </h:panelGroup>
                                                    </h:panelGroup>

                                                    <h:panelGroup styleClass="action"  rendered="#{departmentsController.currentUserCanManageDepartmentCategories}" >
                                                        <t:htmlTag value="span" styleClass="form-item form-submit">
                                                        <e:commandButton value="#{msgs['CATEGORIES.ALT.EDIT_MEMBERS']}"
                                                            id="editMembersButton"
                                                            action="#{departmentsController.editCategoryMembers}" >
                                                            <t:updateActionListener value="#{node.category}"
                                                                property="#{departmentsController.categoryToUpdate}" />
                                                        </e:commandButton>
                                                        </t:htmlTag>
                                                    </h:panelGroup>

                                                </h:panelGroup>
                                                <h:panelGroup rendered="#{departmentsController.categoriesAction == 'FAQ_LINKS'}" >
                                                    <h:panelGroup rendered="#{not node.category.inheritFaqLinks}">
                                                        <h:panelGroup
                                                            style="cursor: pointer"
                                                            onclick="showHideElement('categoriesForm:tree:#{node.identifier}:faqLinks');"
                                                            rendered="#{node.faqLinksNumber != 0}" >
                                                            <e:bold value=" #{msgs['CATEGORIES.TEXT.FAQ_LINKS_NUMBER']}">
                                                                <f:param value="#{node.faqLinksNumber}" />
                                                            </e:bold>
                                                            <t:graphicImage value="/media/images/show.png" />
                                                        </h:panelGroup>
                                                        <e:bold rendered="#{node.faqLinksNumber == 0}"
                                                            value=" #{msgs['CATEGORIES.TEXT.NO_FAQ_LINK']}" />
                                                    </h:panelGroup>
                                                    <h:panelGroup rendered="#{departmentsController.currentUserCanManageDepartmentCategories}" >
                                                        <e:bold value=" " />
                                                        <t:graphicImage value="/media/images/faq-links.png"
                                                            alt="#{msgs['CATEGORIES.ALT.EDIT_FAQ_LINKS']}"
                                                            title="#{msgs['CATEGORIES.ALT.EDIT_FAQ_LINKS']}"
                                                            style="cursor: pointer"
                                                            onclick="simulateLinkClick('categoriesForm:tree:#{node.identifier}:editFaqLinksButton');" />
                                                        <e:commandButton value="#{msgs['CATEGORIES.BUTTON.EDIT_FAQ_LINKS']}"
                                                            id="editFaqLinksButton" style="display: none"
                                                            action="#{departmentsController.editCategoryFaqLinks}" >
                                                            <t:updateActionListener value="#{node.category}"
                                                                property="#{departmentsController.categoryToUpdate}" />
                                                        </e:commandButton>
                                                    </h:panelGroup>
                                                    <h:panelGroup id="faqLinks"
                                                        rendered="#{not node.category.inheritFaqLinks and node.faqLinksNumber != 0}"
                                                        style="display: none" >
                                                        <t:dataList value="#{node.faqLinks}" var="faqLink"
                                                            rowIndexVar="faqLinkIndex">
                                                            <t:htmlTag value="br"
                                                                rendered="#{faqLinkIndex != 0}" />
                                                            <t:graphicImage value="/media/images/faq-link.png" />
                                                            <e:italic value=" #{faqLink.faq.label}" />
                                                        </t:dataList>
                                                    </h:panelGroup>
                                                </h:panelGroup>
                                            </t:htmlTag>
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
