<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="responses" locale="#{sessionController.locale}" >

<script type="text/javascript">
	function toggleContent(data, index) {
		showHideElement('responsesForm:'+data+':'+index+':content'); 
		showHideElement('responsesForm:'+data+':'+index+':showContent'); 
		showHideElement('responsesForm:'+data+':'+index+':hideContent'); 
	}
	function toggleGlobalContent(index) {
		toggleContent('globalData', index);  
	}
	function toggleUserContent(index) {
		toggleContent('userData', index);  
	}
	function toggleDepartmentContent(departmentIndex,index) {
		toggleContent('departmentData:'+departmentIndex+':data', index);  
	}
</script>

    <t:htmlTag id="responses" value="div" styleClass="page-wrapper responses">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>

                    <t:htmlTag value="main" styleClass="content">
                        <t:htmlTag value="div" styleClass="content-inner">
                        <h:panelGroup rendered="#{responsesController.currentUser == null}" >
                            <%@include file="_auth.jsp"%>
                        </h:panelGroup>
                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="responsesForm" rendered="#{responsesController.pageAuthorized}" >
                                    <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                    </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="dashboard-header">
                                        <t:htmlTag value="div" styleClass="controlPanel-title">
                                            <t:htmlTag value="h1">
                                                <t:htmlTag value="span">
                                                    <h:outputText value="#{msgs['RESPONSES.TITLE']}"/>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                     </t:htmlTag>

                                     <t:htmlTag value="fieldset" styleClass="collapsible collapsed">
                                         <t:htmlTag value="legend">
                                                  <t:htmlTag value="span" >
                                                            <h:outputText value="#{msgs['RESPONSES.HEADER.GLOBAL_RESPONSES']}"/>
                                                            <h:outputText value=" (#{responsesController.globalResponsesNumber})"/>
                                                  </t:htmlTag>
                                         </t:htmlTag>
                                         <t:htmlTag value="div" styleClass="response-list">
                                            <t:dataList value="#{responsesController.globalResponses}" var="response" rowIndexVar="index">
                                                <t:htmlTag value="div" styleClass="response-item">
                                                    <t:htmlTag value="div" styleClass="response-label">
                                                        <h:outputText value="#{response.label}" styleClass="link"/>

                                                        <t:htmlTag value="div"  styleClass="response-content hideme">
                                                            <h:outputText  value="#{response.message}" escape="false" />
                                                            <t:htmlTag value="div" styleClass="form-item display-flex" rendered="#{responsesController.userCanManageGlobalResponses}">
                                                                <e:commandButton
                                                                    id="editResponseButton" styleClass="button--secondary"
                                                                    value="#{msgs['RESPONSES.MESSAGE.EDIT']}"
                                                                    action="#{responsesController.editResponse}" >
                                                                    <t:updateActionListener value="#{response}" property="#{responsesController.responseToUpdate}" />
                                                                </e:commandButton>
                                                                <e:commandButton
                                                                    id="deleteResponseButton"
                                                                    value="#{msgs['_.BUTTON.DELETE']}"
                                                                    action="#{responsesController.doDeleteResponse}" >
                                                                    <t:updateActionListener value="#{response}" property="#{responsesController.responseToDelete}" />
                                                                </e:commandButton>
                                                            </t:htmlTag>
                                                        </t:htmlTag>
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                            </t:dataList>
                                            <t:htmlTag value="div" styleClass="form-item" rendered="#{responsesController.userCanManageGlobalResponses}">
                                                 <e:commandButton id="addResponseButton" styleClass="button--secondary"
                                                    value="#{msgs['RESPONSES.BUTTON.ADD_GLOBAL_RESPONSE']}"
                                                    action="#{responsesController.addGlobalResponse}" />
                                            </t:htmlTag>
                                        </t:htmlTag>
                                     </t:htmlTag>

                                     <t:htmlTag value="fieldset" styleClass="collapsible collapsed">
                                         <t:htmlTag value="legend">
                                                  <t:htmlTag value="span" >
                                                            <h:outputText value="#{msgs['RESPONSES.HEADER.USER_RESPONSES']}"/>
                                                            <h:outputText value=" (#{responsesController.userResponsesNumber})"/>
                                                  </t:htmlTag>
                                         </t:htmlTag>
                                         <t:htmlTag value="div" styleClass="response-list">
                                            <t:dataList id="userData" value="#{responsesController.userResponses}" var="response" rowIndexVar="index">
                                                <t:htmlTag value="div" styleClass="response-item">
                                                    <t:htmlTag value="div" styleClass="response-label">
                                                        <h:outputText value="#{userFormatter[responsesController.currentUser]}" styleClass="link"/>
                                                        <h:outputText value="#{response.label}" styleClass="link"/>

                                                        <t:htmlTag value="div"  styleClass="response-content hideme">
                                                            <h:outputText  value="#{response.message}" escape="false" />
                                                            <t:htmlTag value="div" styleClass="form-item display-flex" rendered="#{responsesController.userCanManageGlobalResponses}">
                                                                <e:commandButton
                                                                    id="editResponseButton" styleClass="button--secondary"
                                                                    value="#{msgs['RESPONSES.MESSAGE.EDIT']}"
                                                                    action="#{responsesController.editResponse}" >
                                                                    <t:updateActionListener value="#{response}" property="#{responsesController.responseToUpdate}" />
                                                                </e:commandButton>
                                                                <e:commandButton
                                                                    id="deleteResponseButton"
                                                                    value="#{msgs['_.BUTTON.DELETE']}"
                                                                    action="#{responsesController.doDeleteResponse}" >
                                                                    <t:updateActionListener value="#{response}" property="#{responsesController.responseToDelete}" />
                                                                </e:commandButton>
                                                            </t:htmlTag>
                                                        </t:htmlTag>
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                            </t:dataList>
                                            <t:htmlTag value="div" styleClass="form-item" rendered="#{responsesController.userCanManageGlobalResponses}">
                                                 <e:commandButton id="addResponseButton" styleClass="button--secondary"
                                                    value="#{msgs['RESPONSES.BUTTON.ADD_USER_RESPONSE']}"
                                                    action="#{responsesController.addUserResponse}" />
                                            </t:htmlTag>
                                        </t:htmlTag>
                                     </t:htmlTag>


                                        <t:dataList id="departmentData"
                                                    value="#{responsesController.departments}" var="department"
                                                    rowIndexVar="departmentIndex" >
                                            <t:htmlTag value="fieldset" styleClass="collapsible collapsed">
                                                 <t:htmlTag value="legend">
                                                          <t:htmlTag value="span" >
                                                                    <h:outputText value="#{department.label}"/>
                                                                    <h:outputText value=" (#{responsesController.departmentResponsesNumber[department]})"/>
                                                          </t:htmlTag>
                                                 </t:htmlTag>
                                                 <t:htmlTag value="div" styleClass="response-list">
                                                    <t:dataList value="#{responsesController.departmentResponses[department]}" var="response" rowIndexVar="index">
                                                        <t:htmlTag value="div" styleClass="response-item">
                                                            <t:htmlTag value="div" styleClass="response-label">
                                                                <h:outputText value="#{response.label}" styleClass="link"/>

                                                                <t:htmlTag value="div"  styleClass="response-content hideme">
                                                                    <h:outputText  value="#{response.message}" escape="false" />
                                                                    <t:htmlTag value="div" styleClass="form-item display-flex" rendered="#{responsesController.userCanManageDepartmentResponses[department]}">
                                                                        <e:commandButton
                                                                            id="editResponseButton" styleClass="button--secondary"
                                                                            value="#{msgs['RESPONSES.MESSAGE.EDIT']}"
                                                                            action="#{responsesController.editResponse}" >
                                                                            <t:updateActionListener value="#{response}" property="#{responsesController.responseToUpdate}" />
                                                                        </e:commandButton>

                                                                        <e:commandButton
                                                                            id="deleteResponseButton"
                                                                            value="#{msgs['_.BUTTON.DELETE']}"
                                                                            action="#{responsesController.doDeleteResponse}" >
                                                                            <t:updateActionListener value="#{response}" property="#{responsesController.responseToDelete}" />
                                                                        </e:commandButton>
                                                                    </t:htmlTag>
                                                                </t:htmlTag>
                                                            </t:htmlTag>
                                                        </t:htmlTag>
                                                    </t:dataList>
                                                    <t:htmlTag value="div" styleClass="form-item" rendered="#{responsesController.userCanManageDepartmentResponses[department]}">
                                                           <e:commandButton
                                                                 id="addResponseButton" styleClass="buton-secondary"
                                                                        value="#{msgs['RESPONSES.BUTTON.ADD_DEPARTMENT_RESPONSE']}"
                                                                        action="#{responsesController.addDepartmentResponse}" >
                                                                        <t:updateActionListener value="#{department}" property="#{responsesController.targetDepartment}" />
                                                           </e:commandButton>
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:dataList>
                            </e:form>
                    </t:htmlTag>
                    </t:htmlTag>
                </t:htmlTag>
             <t:htmlTag value="footer" styleClass="footer">
                     <%@include file="_footer.jsp"%>
             </t:htmlTag>
        </t:htmlTag>
</e:page>
