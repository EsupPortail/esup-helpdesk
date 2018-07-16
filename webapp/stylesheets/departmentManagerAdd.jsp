<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentManagers}">
		   <t:htmlTag id="departmentManagerAdd" value="div" styleClass="page-wrapper departmentManagerAdd">
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
                                id="departmentManagerAddForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="dashboard-header">
                                          <t:htmlTag value="div" styleClass="controlPanel-title">
                                              <t:htmlTag value="h1">
                                                  <t:htmlTag value="span">
                                                      <h:outputText value="#{msgs['DEPARTMENT_MANAGER_ADD.TITLE']}"/>
                                                  </t:htmlTag>
                                                  <t:htmlTag value="span" styleClass="subtitle title">
                                                     <h:outputText value=" #{departmentsController.department.label}" escape="false" />
                                                  </t:htmlTag>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                       </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="form-block utils-ldapuid">
                                                         <t:htmlTag value="div" styleClass="form-item">
                                                             <e:outputLabel  for="ldapUid" value="#{domainService.useLdap ? msgs['UTILS.TEXT.USER_PROMPT'] : msgs['UTILS.TEXT.USER_PROMPT_NO_LDAP']} " />
                                                             <e:inputText id="ldapUid" value="#{departmentsController.ldapUid}"/>
                                                         </t:htmlTag>
                                                         <t:htmlTag value="div" styleClass="form-item " >
                                                             <e:commandButton rendered="#{domainService.useLdap}"
                                                                 id="ldapSearchButton" action="#{ldapSearchController.firstSearch}"
                                                                 value="#{msgs['_.BUTTON.LDAP']}" >
                                                                 <t:updateActionListener value="#{departmentsController}"
                                                                     property="#{ldapSearchController.caller}" />
                                                                 <t:updateActionListener value="userSelectedToDepartmentManagerAdd"
                                                                     property="#{ldapSearchController.successResult}" />
                                                                 <t:updateActionListener value="cancelToDepartmentManagerAdd"
                                                                     property="#{ldapSearchController.cancelResult}" />
                                                             </e:commandButton>
                                                         </t:htmlTag>
                                                          <t:htmlTag value="div" styleClass="form-item form-submit" >
                                                                <e:commandButton id="addButton" action="#{departmentsController.addDepartmentManager}"
                                                                    value="#{msgs['DEPARTMENT_MANAGER_ADD.BUTTON.ADD_DEPARTMENT_MANAGER']}" styleClass="button--primary" />
                                                                <e:commandButton id="cancelButton" action="cancel"
                                                                    value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                                          </t:htmlTag>
                                                         <t:htmlTag value="div" styleClass="form-item utils-ldapuid--info" >
                                                             <e:message for="ldapUid" />
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
	<script type="text/javascript">
		focusElement("departmentManagerAddForm:ldapUid");
	</script>
</e:page>
