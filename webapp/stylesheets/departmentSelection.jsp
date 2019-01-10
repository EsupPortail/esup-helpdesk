<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departmentSelection" locale="#{sessionController.locale}" downloadId="#{departmentSelectionController.downloadId}" >
 <t:htmlTag id="departmentSelection" value="div" styleClass="page-wrapper departmentSelection">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
               </t:htmlTag>
               <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>
                    <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                        <t:htmlTag value="div" styleClass="content-inner">
                            <h:panelGroup rendered="#{not departmentSelectionController.pageAuthorized}" >
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
                                id="departmentSelectionForm" enctype="multipart/form-data"
                                rendered="#{departmentSelectionController.pageAuthorized}" >

                                      <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['DEPARTMENT_SELECTION.TITLE']}"/>
                                                 </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>

                                <e:panelGrid columns="2" columnClasses="colLeftMax,colRightNowrap" width="100%" >
                                    <h:panelGroup >
                                        <h:panelGroup rendered="#{departmentSelectionController.currentUserCanEditDepartmentSelection}">
                                            <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionForm:toggleAdvancedButton');" >
                                                <e:bold value="#{msgs[departmentSelectionController.advanced ? 'DEPARTMENT_SELECTION.BUTTON.HIDE_ADVANCED' : 'DEPARTMENT_SELECTION.BUTTON.SHOW_ADVANCED']} " />
                                                <t:graphicImage value="/media/images/#{departmentSelectionController.advanced ? 'hide' : 'show'}.png" />
                                            </h:panelGroup>
                                            <e:commandButton style="display:none"
                                                id="toggleAdvancedButton" action="#{departmentSelectionController.toggleAdvanced}"
                                                value="#{msgs[departmentSelectionController.advanced ? 'DEPARTMENT_SELECTION.BUTTON.HIDE_ADVANCED' : 'DEPARTMENT_SELECTION.BUTTON.SHOW_ADVANCED']}" >
                                            </e:commandButton>
                                        </h:panelGroup>
                                    </h:panelGroup>
                                </e:panelGrid>

                                <e:panelGrid columns="2" columnClasses="colLeftMax,colLeft" width="100%"
                                    cellspacing="0" cellpadding="0">
                                    <h:panelGroup >
                                        <h:panelGroup rendered="#{departmentSelectionController.userDefinedConditionsTree != null}">
                                            <e:subSection value="#{msgs['DEPARTMENT_SELECTION.HEADER.USER_DEFINED_CONDITIONS']}" />
                                            <t:tree2
                                                id="userDefinedConditionsTree" value="#{departmentSelectionController.userDefinedConditionsTree}"
                                                var="node" varNodeToggler="t" clientSideToggle="true"
                                                showRootNode="true" >
                                                <f:facet name="userDefinedConditions">
                                                    <h:panelGroup >
                                                        <t:graphicImage value="/media/images/user-defined-conditions-opened.png" rendered="#{t.nodeExpanded}" />
                                                        <t:graphicImage value="/media/images/user-defined-conditions-closed.png" rendered="#{!t.nodeExpanded}" />
                                                        <t:graphicImage value="/media/images/condition-result.png" />
                                                        <h:panelGroup rendered="#{departmentSelectionController.advanced}">
                                                            <e:text value=" " />
                                                            <t:graphicImage
                                                                style="cursor: pointer"
                                                                value="/media/images/add.png"
                                                                onclick="simulateLinkClick('departmentSelectionForm:userDefinedConditionsTree:#{node.identifier}:addButton');" />
                                                            <e:commandButton style="display:none" id="addButton"
                                                                action="#{departmentSelectionController.addUserDefinedCondition}" />
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="userDefinedCondition">
                                                    <h:panelGroup>
                                                        <h:panelGroup rendered="#{not departmentSelectionController.advanced}">
                                                            <%@include file="_conditionResult.jsp"%>
                                                        </h:panelGroup>
                                                        <h:panelGroup rendered="#{departmentSelectionController.advanced}">
                                                            <t:graphicImage value="/media/images/condition-result.png" />
                                                        </h:panelGroup>
                                                        <h:panelGroup rendered="#{not node.userDefinedCondition.used}" >
                                                            <e:italic value=" #{msgs['DEPARTMENT_SELECTION.USER_DEFINED_CONDITION.UNUSED']} " >
                                                                <f:param value="#{node.userDefinedCondition.name}" />
                                                            </e:italic>
                                                            <t:graphicImage value="/media/images/unused-user-defined-condition.png" />
                                                        </h:panelGroup>
                                                        <e:bold value=" #{msgs['DEPARTMENT_SELECTION.USER_DEFINED_CONDITION.USED']}" rendered="#{node.userDefinedCondition.used}" >
                                                            <f:param value="#{node.userDefinedCondition.name}" />
                                                        </e:bold>
                                                        <h:panelGroup rendered="#{departmentSelectionController.advanced}">
                                                            <h:panelGroup rendered="#{not node.last}" >
                                                                <e:text value=" " />
                                                                <t:graphicImage
                                                                    style="cursor: pointer"
                                                                    value="/media/images/arrow_last.png"
                                                                    onclick="simulateLinkClick('departmentSelectionForm:userDefinedConditionsTree:#{node.identifier}:moveLastButton');" />
                                                                <e:commandButton style="display:none" id="moveLastButton"
                                                                    action="#{departmentSelectionController.moveUserDefinedCondition}" >
                                                                    <t:updateActionListener value="#{node.index}"
                                                                        property="#{departmentSelectionController.indexToUpdate}" />
                                                                    <t:updateActionListener value="LAST"
                                                                        property="#{departmentSelectionController.direction}" />
                                                                </e:commandButton>
                                                                <e:text value=" " />
                                                                <t:graphicImage
                                                                    style="cursor: pointer"
                                                                    value="/media/images/arrow_down.png"
                                                                    onclick="simulateLinkClick('departmentSelectionForm:userDefinedConditionsTree:#{node.identifier}:moveDownButton');" />
                                                                <e:commandButton style="display:none" id="moveDownButton"
                                                                    action="#{departmentSelectionController.moveUserDefinedCondition}" >
                                                                    <t:updateActionListener value="#{node.index}"
                                                                        property="#{departmentSelectionController.indexToUpdate}" />
                                                                    <t:updateActionListener value="DOWN"
                                                                        property="#{departmentSelectionController.direction}" />
                                                                </e:commandButton>
                                                            </h:panelGroup>
                                                            <h:panelGroup rendered="#{not node.first}" >
                                                                <e:text value=" " />
                                                                <t:graphicImage
                                                                    style="cursor: pointer"
                                                                    value="/media/images/arrow_up.png"
                                                                    onclick="simulateLinkClick('departmentSelectionForm:userDefinedConditionsTree:#{node.identifier}:moveUpButton');" />
                                                                <e:commandButton style="display:none" id="moveUpButton"
                                                                    action="#{departmentSelectionController.moveUserDefinedCondition}" >
                                                                    <t:updateActionListener value="#{node.index}"
                                                                        property="#{departmentSelectionController.indexToUpdate}" />
                                                                    <t:updateActionListener value="UP"
                                                                        property="#{departmentSelectionController.direction}" />
                                                                </e:commandButton>
                                                                <e:text value=" " />
                                                                <t:graphicImage
                                                                    style="cursor: pointer"
                                                                    value="/media/images/arrow_first.png"
                                                                    onclick="simulateLinkClick('departmentSelectionForm:userDefinedConditionsTree:#{node.identifier}:moveFirstButton');" />
                                                                <e:commandButton style="display:none" id="moveFirstButton"
                                                                    action="#{departmentSelectionController.moveUserDefinedCondition}" >
                                                                    <t:updateActionListener value="#{node.index}"
                                                                        property="#{departmentSelectionController.indexToUpdate}" />
                                                                    <t:updateActionListener value="FIRST"
                                                                        property="#{departmentSelectionController.direction}" />
                                                                </e:commandButton>
                                                            </h:panelGroup>
                                                            <h:panelGroup rendered="#{not node.userDefinedCondition.used}" >
                                                                <e:text value=" " />
                                                                <t:graphicImage
                                                                    style="cursor: pointer"
                                                                    value="/media/images/delete.png"
                                                                    onclick="simulateLinkClick('departmentSelectionForm:userDefinedConditionsTree:#{node.identifier}:deleteButton');" />
                                                                <e:commandButton style="display:none" id="deleteButton"
                                                                    action="#{departmentSelectionController.moveUserDefinedCondition}" >
                                                                    <t:updateActionListener value="#{node.index}"
                                                                        property="#{departmentSelectionController.indexToUpdate}" />
                                                                    <t:updateActionListener value="DELETE"
                                                                        property="#{departmentSelectionController.direction}" />
                                                                </e:commandButton>
                                                            </h:panelGroup>
                                                            <e:text value=" " />
                                                            <t:graphicImage
                                                                style="cursor: pointer"
                                                                value="/media/images/edit.png"
                                                                onclick="simulateLinkClick('departmentSelectionForm:userDefinedConditionsTree:#{node.identifier}:editButton');" />
                                                            <e:commandButton style="display:none" id="editButton"
                                                                action="#{departmentSelectionController.editUserDefinedCondition}" >
                                                                <t:updateActionListener value="#{node.index}"
                                                                    property="#{departmentSelectionController.indexToUpdate}" />
                                                                <t:updateActionListener value="#{node.userDefinedCondition.name}"
                                                                    property="#{departmentSelectionController.nameToUpdate}" />
                                                                <t:updateActionListener value="#{node.userDefinedCondition.name}"
                                                                    property="#{departmentSelectionController.previousName}" />
                                                            </e:commandButton>
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <%@include file="_conditionFacet.jsp"%>
                                            </t:tree2>
                                        </h:panelGroup>
                                        <h:panelGroup rendered="#{departmentSelectionController.rulesTree != null}">
                                            <e:subSection value="#{msgs['DEPARTMENT_SELECTION.HEADER.RULES']}" />
                                            <t:tree2
                                                id="rulesTree" value="#{departmentSelectionController.rulesTree}"
                                                var="node" varNodeToggler="t" clientSideToggle="true"
                                                showRootNode="true" >
                                                <f:facet name="rules">
                                                    <h:panelGroup >
                                                        <t:graphicImage value="/media/images/rules-opened.png" rendered="#{t.nodeExpanded}" />
                                                        <t:graphicImage value="/media/images/rules-closed.png" rendered="#{!t.nodeExpanded}" />
                                                        <h:panelGroup rendered="#{departmentSelectionController.advanced}">
                                                            <t:graphicImage value="/media/images/rule.png" />
                                                            <e:text value=" " />
                                                            <t:graphicImage
                                                                style="cursor: pointer"
                                                                value="/media/images/add.png"
                                                                onclick="simulateLinkClick('departmentSelectionForm:rulesTree:#{node.identifier}:addButton');" />
                                                            <e:commandButton style="display:none" id="addButton"
                                                                action="#{departmentSelectionController.addRule}" />
                                                        </h:panelGroup>
                                                        <h:panelGroup rendered="#{not departmentSelectionController.advanced}">
                                                            <t:graphicImage value="/media/images/rule#{node.evalResult != null ? '-evaluated' : ''}.png" />
                                                            <t:dataList value="#{node.evalResult}" var="department" rowIndexVar="index"
                                                                rendered="#{not empty node.evalResult}" >
                                                                <e:bold value=" " rendered="#{index == 0}" />
                                                                <e:bold value="#{msgs['DEPARTMENT_SELECTION.ACTION.DEPARTMENTS_SEPARATOR']}" rendered="#{index != 0}" />
                                                                <e:bold value=" #{department.label}" />
                                                            </t:dataList>
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="rule">
                                                    <h:panelGroup>
                                                        <h:panelGroup rendered="#{departmentSelectionController.advanced}">
                                                            <t:graphicImage value="/media/images/rule.png" />
                                                        </h:panelGroup>
                                                        <h:panelGroup rendered="#{not departmentSelectionController.advanced}">
                                                            <t:graphicImage value="/media/images/rule#{node.evalResult != null ? '-evaluated' : ''}.png" />
                                                        </h:panelGroup>
                                                        <e:text value=" #{msgs['DEPARTMENT_SELECTION.RULE.UNNAMED']} " rendered="#{node.rule.name == null}" />
                                                        <e:bold value=" #{node.rule.name} " rendered="#{node.rule.name != null}" />
                                                        <h:panelGroup rendered="#{not departmentSelectionController.advanced}">
                                                            <h:panelGroup rendered="#{node.evalResult != null}" >
                                                                <t:graphicImage
                                                                    value="/media/images/action-stopped.png"
                                                                    rendered="#{node.evalResult.skipNextRule}" />
                                                                <t:graphicImage
                                                                    value="/media/images/action-added.png"
                                                                    rendered="#{not node.evalResult.skipNextRule}" />
                                                                <t:dataList value="#{node.evalResult.departments}" var="department" rowIndexVar="index"
                                                                    rendered="#{not empty node.evalResult.departments}" >
                                                                    <e:bold value=" " rendered="#{index == 0}" />
                                                                    <e:bold value="#{msgs['DEPARTMENT_SELECTION.ACTION.DEPARTMENTS_SEPARATOR']}" rendered="#{index != 0}" />
                                                                    <e:bold value=" #{department.label}" />
                                                                </t:dataList>
                                                            </h:panelGroup>
                                                        </h:panelGroup>
                                                        <h:panelGroup rendered="#{departmentSelectionController.advanced}">
                                                            <h:panelGroup rendered="#{not node.last}" >
                                                                <e:text value=" " />
                                                                <t:graphicImage
                                                                    style="cursor: pointer"
                                                                    value="/media/images/arrow_last.png"
                                                                    onclick="simulateLinkClick('departmentSelectionForm:rulesTree:#{node.identifier}:moveLastButton');" />
                                                                <e:commandButton style="display:none" id="moveLastButton"
                                                                    action="#{departmentSelectionController.moveRule}" >
                                                                    <t:updateActionListener value="#{node.index}"
                                                                        property="#{departmentSelectionController.indexToUpdate}" />
                                                                    <t:updateActionListener value="LAST"
                                                                        property="#{departmentSelectionController.direction}" />
                                                                </e:commandButton>
                                                                <e:text value=" " />
                                                                <t:graphicImage
                                                                    style="cursor: pointer"
                                                                    value="/media/images/arrow_down.png"
                                                                    onclick="simulateLinkClick('departmentSelectionForm:rulesTree:#{node.identifier}:moveDownButton');" />
                                                                <e:commandButton style="display:none" id="moveDownButton"
                                                                    action="#{departmentSelectionController.moveRule}" >
                                                                    <t:updateActionListener value="#{node.index}"
                                                                        property="#{departmentSelectionController.indexToUpdate}" />
                                                                    <t:updateActionListener value="DOWN"
                                                                        property="#{departmentSelectionController.direction}" />
                                                                </e:commandButton>
                                                            </h:panelGroup>
                                                            <h:panelGroup rendered="#{not node.first}" >
                                                                <e:text value=" " />
                                                                <t:graphicImage
                                                                    style="cursor: pointer"
                                                                    value="/media/images/arrow_up.png"
                                                                    onclick="simulateLinkClick('departmentSelectionForm:rulesTree:#{node.identifier}:moveUpButton');" />
                                                                <e:commandButton style="display:none" id="moveUpButton"
                                                                    action="#{departmentSelectionController.moveRule}" >
                                                                    <t:updateActionListener value="#{node.index}"
                                                                        property="#{departmentSelectionController.indexToUpdate}" />
                                                                    <t:updateActionListener value="UP"
                                                                        property="#{departmentSelectionController.direction}" />
                                                                </e:commandButton>
                                                                <e:text value=" " />
                                                                <t:graphicImage
                                                                    style="cursor: pointer"
                                                                    value="/media/images/arrow_first.png"
                                                                    onclick="simulateLinkClick('departmentSelectionForm:rulesTree:#{node.identifier}:moveFirstButton');" />
                                                                <e:commandButton style="display:none" id="moveFirstButton"
                                                                    action="#{departmentSelectionController.moveRule}" >
                                                                    <t:updateActionListener value="#{node.index}"
                                                                        property="#{departmentSelectionController.indexToUpdate}" />
                                                                    <t:updateActionListener value="FIRST"
                                                                        property="#{departmentSelectionController.direction}" />
                                                                </e:commandButton>
                                                            </h:panelGroup>
                                                            <e:text value=" " />
                                                            <t:graphicImage
                                                                style="cursor: pointer"
                                                                value="/media/images/delete.png"
                                                                onclick="simulateLinkClick('departmentSelectionForm:rulesTree:#{node.identifier}:deleteButton');" />
                                                            <e:commandButton style="display:none" id="deleteButton"
                                                                action="#{departmentSelectionController.moveRule}" >
                                                                <t:updateActionListener value="#{node.index}"
                                                                    property="#{departmentSelectionController.indexToUpdate}" />
                                                                <t:updateActionListener value="DELETE"
                                                                    property="#{departmentSelectionController.direction}" />
                                                            </e:commandButton>
                                                            <e:text value=" " />
                                                            <t:graphicImage
                                                                style="cursor: pointer"
                                                                value="/media/images/edit.png"
                                                                onclick="simulateLinkClick('departmentSelectionForm:rulesTree:#{node.identifier}:editButton');" />
                                                            <e:commandButton style="display:none" id="editButton"
                                                                action="#{departmentSelectionController.editRule}" >
                                                                <t:updateActionListener value="#{node.index}"
                                                                    property="#{departmentSelectionController.indexToUpdate}" />
                                                                <t:updateActionListener value="#{node.rule.name}"
                                                                    property="#{departmentSelectionController.nameToUpdate}" />
                                                            </e:commandButton>
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <%@include file="_conditionFacet.jsp"%>
                                                <%@include file="_actionFacet.jsp"%>
                                            </t:tree2>
                                        </h:panelGroup>
                                        <h:panelGroup rendered="#{departmentSelectionController.whenEmptyActionsTree != null}">
                                            <e:subSection value="#{msgs['DEPARTMENT_SELECTION.HEADER.WHEN_EMPTY_ACTIONS']}" />
                                            <t:tree2
                                                id="whenEmptyActionsTree" value="#{departmentSelectionController.whenEmptyActionsTree}"
                                                var="node" varNodeToggler="t" clientSideToggle="true"
                                                showRootNode="true" >
                                                <f:facet name="actions">
                                                    <h:panelGroup >
                                                        <t:graphicImage value="/media/images/actions-opened.png" rendered="#{t.nodeExpanded}" />
                                                        <t:graphicImage value="/media/images/actions-closed.png" rendered="#{!t.nodeExpanded}" />
                                                        <h:panelGroup rendered="#{node.evalResult != null and not departmentSelectionController.advanced}" >
                                                            <t:graphicImage value="/media/images/action-added.png" />
                                                            <t:dataList value="#{node.evalResult.departments}"
                                                                var="department" rowIndexVar="index" >
                                                                <e:bold value=" " rendered="#{index == 0}" />
                                                                <e:bold value="#{msgs['DEPARTMENT_SELECTION.ACTION.DEPARTMENTS_SEPARATOR']}" rendered="#{index != 0}" />
                                                                <e:bold value=" #{department.label}" />
                                                            </t:dataList>
                                                        </h:panelGroup>
                                                        <h:panelGroup rendered="#{departmentSelectionController.advanced}" >
                                                            <e:text value=" " />
                                                            <t:graphicImage
                                                                style="cursor: pointer"
                                                                value="/media/images/edit.png"
                                                                onclick="simulateLinkClick('departmentSelectionForm:whenEmptyActionsTree:#{node.identifier}:editButton');" />
                                                            <e:commandButton style="display:none" id="editButton"
                                                                action="#{departmentSelectionController.editActions}" >
                                                            </e:commandButton>
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <%@include file="_actionFacet.jsp"%>
                                            </t:tree2>
                                        </h:panelGroup>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:panelGrid columns="1" columnClasses="colLeftNowrap" >
                                            <e:subSection value="#{msgs['DEPARTMENT_SELECTION.HEADER.OPERATIONS']}" />
                                            <h:panelGroup rendered="#{departmentSelectionController.currentUserCanEditDepartmentSelection}" >
                                                <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionForm:downloadConfigButton');" >
                                                    <t:graphicImage value="/media/images/department-selection-download-config.png"
                                                        alt="#{msgs['DEPARTMENT_SELECTION.BUTTON.DOWNLOAD_CONFIG']}"
                                                        title="#{msgs['DEPARTMENT_SELECTION.BUTTON.DOWNLOAD_CONFIG']}" />
                                                    <e:bold value=" #{msgs['DEPARTMENT_SELECTION.BUTTON.DOWNLOAD_CONFIG']} " />
                                                </h:panelGroup>
                                                <e:commandButton style="display:none"
                                                    id="downloadConfigButton" action="#{departmentSelectionController.downloadConfig}"
                                                    value="#{msgs['DEPARTMENT_SELECTION.BUTTON.DOWNLOAD_CONFIG']}" >
                                                </e:commandButton>
                                            </h:panelGroup>
                                            <h:panelGroup>
                                                <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionForm:editConfigButton');" >
                                                    <t:graphicImage value="/media/images/department-selection-edit-config.png"
                                                        alt="#{msgs['DEPARTMENT_SELECTION.BUTTON.EDIT_CONFIG']}"
                                                        title="#{msgs['DEPARTMENT_SELECTION.BUTTON.EDIT_CONFIG']}" />
                                                    <e:bold value=" #{msgs['DEPARTMENT_SELECTION.BUTTON.EDIT_CONFIG']} " />
                                                </h:panelGroup>
                                                <e:commandButton style="display:none"
                                                    id="editConfigButton" action="#{departmentSelectionController.editConfig}"
                                                    value="#{msgs['DEPARTMENT_SELECTION.BUTTON.EDIT_CONFIG']}" >
                                                </e:commandButton>
                                            </h:panelGroup>
                                            <h:panelGroup rendered="#{departmentSelectionController.currentUserCanEditDepartmentSelection}" >
                                                <h:panelGroup style="cursor: pointer" onclick="showElement('departmentSelectionForm:uploadPanel');" >
                                                    <t:graphicImage value="/media/images/department-selection-upload-config.png"
                                                        alt="#{msgs['DEPARTMENT_SELECTION.BUTTON.UPLOAD_CONFIG']}"
                                                        title="#{msgs['DEPARTMENT_SELECTION.BUTTON.UPLOAD_CONFIG']}" />
                                                    <e:bold value=" #{msgs['DEPARTMENT_SELECTION.BUTTON.UPLOAD_CONFIG']} " />
                                                </h:panelGroup>
                                                <h:panelGroup id="uploadPanel" style="display: none" >
                                                    <e:inputFileUpload id="uploadedFile" value="#{departmentSelectionController.uploadedFile}" storage="memory" />
                                                    <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionForm:uploadConfigButton');" >
                                                        <t:graphicImage value="/media/images/next.png"
                                                            alt="#{msgs['DEPARTMENT_SELECTION.BUTTON.UPLOAD_CONFIG']}"
                                                            title="#{msgs['DEPARTMENT_SELECTION.BUTTON.UPLOAD_CONFIG']}" />
                                                    </h:panelGroup>
                                                </h:panelGroup>
                                                <e:commandButton style="display:none"
                                                    id="uploadConfigButton" action="#{departmentSelectionController.uploadConfig}"
                                                    value="#{msgs['DEPARTMENT_SELECTION.BUTTON.UPLOAD_CONFIG']}" >
                                                </e:commandButton>
                                            </h:panelGroup>
                                            <h:panelGroup rendered="#{departmentSelectionController.currentUserCanEditDepartmentSelection}" >
                                                <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionForm:loadConfigButton');" >
                                                    <t:graphicImage value="/media/images/department-selection-load-config.png"
                                                        alt="#{msgs['DEPARTMENT_SELECTION.BUTTON.LOAD_CONFIG']}"
                                                        title="#{msgs['DEPARTMENT_SELECTION.BUTTON.LOAD_CONFIG']}" />
                                                    <e:bold value=" #{msgs['DEPARTMENT_SELECTION.BUTTON.LOAD_CONFIG']} " />
                                                </h:panelGroup>
                                                <e:commandButton style="display:none"
                                                    id="loadConfigButton" action="#{departmentSelectionController.loadConfig}"
                                                    value="#{msgs['DEPARTMENT_SELECTION.BUTTON.LOAD_CONFIG']}" >
                                                </e:commandButton>
                                            </h:panelGroup>
                                            <h:panelGroup rendered="#{departmentSelectionController.currentUserCanEditDepartmentSelection}" >
                                                <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionForm:saveConfigButton');" >
                                                    <t:graphicImage value="/media/images/department-selection-save-config.png"
                                                        alt="#{msgs['DEPARTMENT_SELECTION.BUTTON.SAVE_CONFIG']}"
                                                        title="#{msgs['DEPARTMENT_SELECTION.BUTTON.SAVE_CONFIG']}" />
                                                    <e:bold value=" #{msgs['DEPARTMENT_SELECTION.BUTTON.SAVE_CONFIG']} " />
                                                </h:panelGroup>
                                                <e:commandButton style="display:none"
                                                    id="saveConfigButton" action="#{departmentSelectionController.saveConfig}"
                                                    value="#{msgs['DEPARTMENT_SELECTION.BUTTON.SAVE_CONFIG']}" >
                                                </e:commandButton>
                                            </h:panelGroup>
                                            <h:panelGroup rendered="#{not departmentSelectionController.advanced}" >
                                                <t:htmlTag value="hr" />
                                                <e:subSection value="#{msgs['DEPARTMENT_SELECTION.HEADER.TEST']}" />
                                                <e:outputLabel for="type"
                                                    value="#{msgs['DEPARTMENT_SELECTION.TEXT.TYPE_PROMPT']} " />
                                                <t:htmlTag value="br" />
                                                <h:panelGroup>
                                                    <e:selectOneMenu id="type" value="#{departmentSelectionController.type}" >
                                                        <f:selectItems value="#{departmentSelectionController.typeItems}" />
                                                    </e:selectOneMenu>
                                                    <e:commandButton style="display: none"
                                                        value="#{msgs['_.BUTTON.UPDATE']}"
                                                        id="changeTypeButton"
                                                        action="#{departmentSelectionController.enter}" />
                                                </h:panelGroup>
                                                <t:htmlTag value="br" />
                                                <e:outputLabel for="ldapUid"
                                                    value="#{domainService.useLdap ? msgs['DEPARTMENT_SELECTION.TEXT.USER_PROMPT'] : msgs['DEPARTMENT_SELECTION.TEXT.USER_PROMPT_NO_LDAP']} " />
                                                <t:htmlTag value="br" />
                                                <h:panelGroup >
                                                    <e:inputText id="ldapUid" value="#{departmentSelectionController.ldapUid}"
                                                            onkeypress="if (event.keyCode == 13) { simulateLinkClick('departmentSelectionForm:testButton'); return false; }" />
                                                    <h:panelGroup rendered="#{domainService.useLdap}" >
                                                        <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionForm:ldapSearchButton');" >
                                                            <e:bold value=" #{msgs['_.BUTTON.LDAP']} " />
                                                            <t:graphicImage value="/media/images/search.png"
                                                                alt="#{msgs['_.BUTTON.LDAP']}"
                                                                title="#{msgs['_.BUTTON.LDAP']}" />
                                                        </h:panelGroup>
                                                        <e:commandButton style="display:none"
                                                            id="ldapSearchButton" action="#{ldapSearchController.firstSearch}"
                                                            value="#{msgs['_.BUTTON.LDAP']}" >
                                                            <t:updateActionListener value="#{departmentSelectionController}"
                                                                property="#{ldapSearchController.caller}" />
                                                            <t:updateActionListener value="userSelectedToDepartmentSelection"
                                                                property="#{ldapSearchController.successResult}" />
                                                            <t:updateActionListener value="cancelToDepartmentSelection"
                                                                property="#{ldapSearchController.cancelResult}" />
                                                        </e:commandButton>
                                                    </h:panelGroup>
                                                </h:panelGroup>
                                                <t:htmlTag value="br" />
                                                <e:outputLabel for="computer"
                                                    value="#{msgs['DEPARTMENT_SELECTION.TEXT.COMPUTER_PROMPT']} " />
                                                <t:htmlTag value="br" />
                                                <e:inputText id="computer" value="#{departmentSelectionController.computer}" size="50"
                                                            onkeypress="if (event.keyCode == 13) { simulateLinkClick('departmentSelectionForm:testButton'); return false;}" />
                                                <t:htmlTag value="br" />
                                                <h:panelGroup >
                                                    <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionForm:testButton');" >
                                                        <e:bold value=" #{msgs['DEPARTMENT_SELECTION.BUTTON.TEST']} " />
                                                        <t:graphicImage value="/media/images/next.png" />
                                                    </h:panelGroup>
                                                    <e:commandButton style="display:none"
                                                        id="testButton" action="#{departmentSelectionController.test}"
                                                        value="#{msgs['DEPARTMENT_SELECTION.BUTTON.TEST']}" >
                                                    </e:commandButton>
                                                    <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionForm:clearButton');" >
                                                        <e:bold value=" #{msgs['DEPARTMENT_SELECTION.BUTTON.CLEAR']} " />
                                                        <t:graphicImage value="/media/images/next.png" />
                                                    </h:panelGroup>
                                                    <e:commandButton style="display:none"
                                                        id="clearButton" action="#{departmentSelectionController.clear}"
                                                        value="#{msgs['DEPARTMENT_SELECTION.BUTTON.CLEAR']}" >
                                                    </e:commandButton>
                                                </h:panelGroup>
                                            </h:panelGroup>
                                            <t:htmlTag value="hr" />
                                            <e:subSection value="#{msgs['DEPARTMENT_SELECTION.HEADER.LEGEND']}" />
                                            <e:bold value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.CONDITIONS_SUBHEADER']}" />
                                            <e:panelGrid columns="2" columnClasses="colLeft,colLeft" >
                                                <t:graphicImage value="/media/images/condition-result.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.CONDITION_RESULT']}" />
                                                <t:graphicImage value="/media/images/condition-result-true.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.CONDITION_RESULT_TRUE']}" />
                                                <t:graphicImage value="/media/images/condition-result-false.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.CONDITION_RESULT_FALSE']}" />
                                            </e:panelGrid>
                                            <e:bold value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.RULES_SUBHEADER']}" />
                                            <e:panelGrid columns="2" columnClasses="colLeft,colLeft" >
                                                <t:graphicImage value="/media/images/rule.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.RULE']}" />
                                                <t:graphicImage value="/media/images/rule-evaluated.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.RULE_EVALUATED']}" />
                                            </e:panelGrid>
                                            <e:bold value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.ACTIONS_SUBHEADER']}" />
                                            <e:panelGrid columns="2" columnClasses="colLeft,colLeft" >
                                                <t:graphicImage value="/media/images/action-add.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.ACTION_ADD']}" />
                                                <t:graphicImage value="/media/images/action-added.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.ACTION_ADDED']}" />
                                                <t:graphicImage value="/media/images/action-stop.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.ACTION_STOP']}" />
                                                <t:graphicImage value="/media/images/action-stopped.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.ACTION_STOPPED']}" />
                                                <t:graphicImage value="/media/images/action-for-ticket-creation.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.TICKET_CREATION']}" />
                                                <t:graphicImage value="/media/images/action-for-ticket-view.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.TICKET_VIEW']}" />
                                                <t:graphicImage value="/media/images/action-for-faq-view.png" />
                                                <e:text value="#{msgs['DEPARTMENT_SELECTION.TEXT.LEGEND.FAQ_VIEW']}" />
                                            </e:panelGrid>
                                        </e:panelGrid>
                                    </h:panelGroup>
                                </e:panelGrid>
                            </e:form>
                        </t:htmlTag>
                    </t:htmlTag>
               </t:htmlTag>
               <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
               </t:htmlTag>
 </t:htmlTag>
</e:page>
