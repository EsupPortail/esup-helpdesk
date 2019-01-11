<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departmentSelection" locale="#{sessionController.locale}" authorized="#{departmentSelectionController.currentUserCanEditDepartmentSelection}" >
 <t:htmlTag id="departmentSelectionEditUserDefinedCondition" value="div" styleClass="page-wrapper departmentSelectionEditUserDefinedCondition">
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
                                    id="departmentSelectionEditForm" >

                                      <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['DEPARTMENT_SELECTION_EDIT.USER_DEFINED_CONDITION_TITLE']}"/>
                                                 </t:htmlTag>
                                                  <t:htmlTag value="span" styleClass="subtitle title">
                                                     <h:outputText value=" #{departmentSelectionController.nameToUpdate}" escape="false" />
                                                  </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                    <e:panelGrid columns="2" width="100%" columnClasses="colLeft,colLeftMax">
                                        <h:panelGroup>
                                            <e:outputLabel for="name"
                                                value="#{msgs['DEPARTMENT_SELECTION_EDIT.TEXT.NAME_PROMPT']} " />
                                            <e:inputText id="name" value="#{departmentSelectionController.nameToUpdate}" size="30" />
                                            <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionEditForm:updateButton');" >
                                                <e:bold value=" #{msgs['_.BUTTON.UPDATE']} " />
                                                <t:graphicImage value="/media/images/save.png"
                                                    alt="#{msgs['_.BUTTON.UPDATE']}"
                                                    title="#{msgs['_.BUTTON.UPDATE']}" />
                                            </h:panelGroup>
                                            <e:commandButton style="display:none"
                                                id="updateButton" action="#{departmentSelectionController.doEditUserDefinedCondition}"
                                                value="#{msgs['_.BUTTON.UPDATE']}" >
                                            </e:commandButton>
                                            <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentSelectionEditForm:backButton');" >
                                                <e:bold value=" #{msgs['_.BUTTON.BACK']} " />
                                                <t:graphicImage value="/media/images/back.png"
                                                    alt="#{msgs['_.BUTTON.BACK']}"
                                                    title="#{msgs['_.BUTTON.BACK']}" />
                                            </h:panelGroup>
                                            <e:commandButton style="display:none" immediate="true"
                                                id="backButton" action="back"
                                                value="#{msgs['_.BUTTON.BACK']}" >
                                            </e:commandButton>
                                        </h:panelGroup>
                                        <e:panelGrid columns="1" width="100%" columnClasses="colLeft">
                                            <e:section value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TITLE']}" />
                                        </e:panelGrid>
                                        <e:inputTextarea rows="40" cols="80" value="#{departmentSelectionController.config}"  />
                                        <h:panelGroup>
                                            <%@include file="_departmentSelectionEditHelpConditions.jsp"%>
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
