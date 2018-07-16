<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">
		   <t:htmlTag id="categoryAdd" value="div" styleClass="page-wrapper categoryAdd">
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
                                id="categoryAddForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="dashboard-header">
                                          <t:htmlTag value="div" styleClass="controlPanel-title">
                                              <t:htmlTag value="h1">
                                                  <t:htmlTag value="span">
                                                      <h:outputText value="#{msgs['CATEGORY_ADD.TITLE']}"/>
                                                  </t:htmlTag>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                       </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="region form-body">
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="label" value="#{msgs['CATEGORY_ADD.TEXT.LABEL']}" />
                                                    <e:inputText id="label" value="#{departmentsController.categoryToAdd.label}" size="20" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:outputLabel for="label" value="#{msgs['CATEGORY_ADD.TEXT.XLABEL']}" />
                                                    <e:inputText id="xlabel" value="#{departmentsController.categoryToAdd.xlabel}" size="50" />
                                                </t:htmlTag>
                                           </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-block form-checkbox" rendered="#{departmentsController.categoryToAdd.parent != null}">
                                                <e:selectBooleanCheckbox value="#{departmentsController.categoryToAdd.parent.addNewTickets}" />
                                                <e:italic value="#{msgs['CATEGORY_ADD.TEXT.PARENT_ADD_NEW_TICKETS_HELP']}" />
                                           </t:htmlTag>

                                           <t:htmlTag value="div" styleClass="form-item display-flex">
                                                 <e:commandButton id="addButton" action="#{departmentsController.addCategory}"
                                                        styleClass="button--primary"
                                                        value="#{msgs['CATEGORY_ADD.BUTTON.ADD_CATEGORY']}" />
                                                 <e:commandButton id="cancelButton" action="cancel" value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
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
