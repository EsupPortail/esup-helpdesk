<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}" authorized="#{departmentsController.currentUserCanDeleteDepartment}" >
 <t:htmlTag id="departmentDelete" value="div" styleClass="page-wrapper departmentDelete">
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
                                id="departmentDeleteForm" >
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="dashboard-header">
                                          <t:htmlTag value="div" styleClass="controlPanel-title">
                                              <t:htmlTag value="h1">
                                                  <t:htmlTag value="span">
                                                      <h:outputText value="#{msgs['DEPARTMENT_DELETE.TITLE']}"/>
                                                  </t:htmlTag>
                                                  <t:htmlTag value="span" styleClass="subtitle title">
                                                     <h:outputText value=" #{departmentsController.department.label}" escape="false" />
                                                  </t:htmlTag>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-item display-flex">
                                        <e:commandButton styleClass="button--primary" id="confirmButton" action="#{departmentsController.doDeleteDepartment}"
                                            value="#{msgs['_.BUTTON.CONFIRM']}" />
                                            <e:commandButton id="cancelButton" action="cancel" value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                      </t:htmlTag>


                                <h:panelGroup rendered="#{not empty departmentsController.deleteTargetDepartments}">
                                    <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentDeleteForm:deleteArchivedTicketsButton');" >
                                        <t:graphicImage value="/media/images/delete.png" />
                                        <e:bold value=" #{msgs['DEPARTMENT_DELETE.BUTTON.DELETE_ARCHIVED_TICKETS']}" />
                                    </h:panelGroup>
                                    <e:commandButton style="display: none" id="deleteArchivedTicketsButton"
                                        action="#{departmentsController.doDeleteDepartment}"
                                        value="#{msgs['DEPARTMENT_DELETE.BUTTON.DELETE_ARCHIVED_TICKETS']}" >
                                            <t:updateActionListener value="#{null}" property="#{departmentsController.targetDepartment}" />
                                    </e:commandButton>
                                    <t:dataList id="data" value="#{departmentsController.deleteTargetDepartments}" var="department" rowIndexVar="index" >
                                        <t:htmlTag value="br" />
                                        <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentDeleteForm:data:#{index}:moveArchivedTicketsButton');" >
                                            <t:graphicImage value="/media/images/move.png" />
                                            <t:graphicImage value="/media/images/delete.png" />
                                            <e:bold value=" #{msgs['DEPARTMENT_DELETE.BUTTON.MOVE_ARCHIVED_TICKETS']}" >
                                                <f:param value="#{department.label}" />
                                            </e:bold>
                                        </h:panelGroup>
                                        <e:commandButton style="display: none" id="moveArchivedTicketsButton"
                                            action="#{departmentsController.doDeleteDepartment}"
                                            value="#{msgs['DEPARTMENT_DELETE.BUTTON.MOVE_ARCHIVED_TICKETS']}" >
                                            <t:updateActionListener value="#{department}" property="#{departmentsController.targetDepartment}" />
                                        </e:commandButton>
                                    </t:dataList>
                                </h:panelGroup>

                            </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
           <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
           </t:htmlTag>
        </t:htmlTag>
</e:page>
