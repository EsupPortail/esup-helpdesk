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
                                id="departmentViewForm">
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
                                                <e:commandButton id="cancelButton" action="#{faqsController.back}" value="#{msgs['CATEGORIES.BUTTON.BACK']}" immediate="true" />
                                            </t:htmlTag>
                                      </t:htmlTag>

                                      <t:htmlTag value="div" styleClass="form-block" rendered="#{empty faqsController.viewTree}">
                                            <t:htmlTag value="div" styleClass="form-item" >
                                                       <e:paragraph value="#{msgs['DEPARTMENT_FAQ_LINKS.TEXT.NO_FAQ_LINK']}" />
                                           </t:htmlTag>
                                      </t:htmlTag>

									  <%@include file="_departmentViewFaqLinks.jsp"%>
		   <h:panelGroup rendered="#{faqsController.userCanEdit and faqsController.editInterfaceDpt}" > 
				<h:panelGroup style="cursor: pointer" 
					onclick="simulateLinkClick('departmentViewForm:addFaqButton');" >
					<e:bold value="#{msgs['FAQS.BUTTON.ADD_FAQ']} " /> 
					<t:graphicImage value="/media/images/add.png" 
						alt="#{msgs['FAQS.BUTTON.ADD_FAQ']}" 
						title="#{msgs['FAQS.BUTTON.ADD_FAQ']}" 
						/>
				</h:panelGroup>
	               <e:commandButton 
	                   id="addFaqButton" style="display: none"
	                   value="#{msgs['FAQS.BUTTON.ADD_FAQ']}" 
	                   action="#{faqsController.addFaq}" />
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
