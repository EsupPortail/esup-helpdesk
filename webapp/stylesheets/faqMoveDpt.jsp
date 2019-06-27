<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="faqs" authorized="#{faqsController.pageAuthorized}" locale="#{sessionController.locale}" >
	   <t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper">
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
								id="faqMoveForm" >
                                   <t:htmlTag value="div" styleClass="message">
                                        <e:messages/>
                                   </t:htmlTag>
                                   <t:htmlTag value="div" styleClass="dashboard-header">
                                      <t:htmlTag value="div" styleClass="controlPanel-title">
                                          <t:htmlTag value="h1">
                                              <t:htmlTag value="span">
                                                  <h:outputText value="#{msgs['FAQ_MOVE.TITLE']}"/>
                                              </t:htmlTag>
                                              <t:htmlTag value="span" styleClass="subtitle title">
                                                 <h:outputText value=" #{faqsController.faqToUpdate.label}" escape="false" />
                                              </t:htmlTag>
                                          </t:htmlTag>
                                      </t:htmlTag>
                                   </t:htmlTag>
								   <t:htmlTag value="div" styleClass="region form-body">
	                                     <t:htmlTag value="div" styleClass="form-block">
                                            <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                <e:outputLabel for="id" value="#{msgs['FAQ_MOVE.TEXT']}" />
                                            </t:htmlTag>
	                                     </t:htmlTag>
										 <t:htmlTag value="div" styleClass="form-block treeview readonly-style ">
				    						<t:htmlTag value="div" styleClass="form-item" >
												<t:tree2 id="tree" value="#{faqsController.moveTree}"
													var="node" varNodeToggler="t" clientSideToggle="true" 
													showRootNode="true" >
													<f:facet name="root">
														<h:panelGroup>
															<h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('faqMoveForm:tree:#{node.identifier}:selectRoot');">
																<t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
																<t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
																<e:text value=" " />
																<t:graphicImage value="/media/images/faq-scope-#{domainService.departmentDefaultFaqScope}.png" rendered="#{faqsController.editInterface}" />
																<e:bold value=" #{msgs['FAQS.TEXT.TREE_ROOT_LABEL']}" rendered="#{t.nodeSelected}" />
																<e:text value=" #{msgs['FAQS.TEXT.TREE_ROOT_LABEL']}" rendered="#{!t.nodeSelected}" />
															</h:panelGroup>
															<e:commandButton 
																value="->" id="selectRoot" style="display: none" 
																action="#{faqsController.doMoveFaqDpt}" >
															</e:commandButton>
														</h:panelGroup>
													</f:facet>
													<f:facet name="department">
														<h:panelGroup>
															<h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('faqMoveForm:tree:#{node.identifier}:selectDepartment');">
																<t:graphicImage value="#{departmentIconUrlProvider[node.department]}" />
																<e:text value=" " />
																<t:graphicImage value="/media/images/faq-scope-#{node.department.defaultFaqScope}.png" rendered="#{faqsController.editInterface and node.department.defaultFaqScope != 'DEFAULT'}" />
																<e:text value=" #{node.department.label}" rendered="#{!t.nodeSelected}" />
																<e:bold value=" #{node.department.label}" rendered="#{t.nodeSelected}" />
															</h:panelGroup>
															<e:commandButton 
																value="->" id="selectDepartment" style="display: none" 
																action="#{faqsController.doMoveFaqDpt}" >
																<t:updateActionListener value="#{node.department}" property="#{faqsController.targetDepartment}" />
															</e:commandButton>
														</h:panelGroup>
													</f:facet>
													<f:facet name="faq">
														<h:panelGroup>
															<h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('faqMoveForm:tree:#{node.identifier}:selectFaq');return false;">
										                        <t:graphicImage value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" />
																<e:text value=" " />
																<t:graphicImage value="/media/images/faq-scope-#{node.faq.scope}.png" rendered="#{faqsController.editInterface and node.faq.scope != 'DEFAULT'}" />
																<e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
																<e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
															</h:panelGroup>
															<e:commandButton 
																value="->" id="selectFaq" style="display: none" 
																action="#{faqsController.doMoveFaqDpt}" >
																<t:updateActionListener value="#{node.faq}" property="#{faqsController.targetFaq}" />
															</e:commandButton>
														</h:panelGroup>
													</f:facet>
												</t:tree2>
								   		</t:htmlTag>
								   	 </t:htmlTag>
							   </t:htmlTag>
						       <t:htmlTag value="div" styleClass="form-block form-header">
						          <t:htmlTag value="div" styleClass="form-item">
						             <e:commandButton id="cancelButton" action="cancel" value="#{msgs['TICKET_ACTION.BUTTON.CREATE.CANCEL']}" immediate="true" />
						          </t:htmlTag>
						       </t:htmlTag>							   
							</e:form>
	            	</t:htmlTag>
	            </t:htmlTag>
        </t:htmlTag>
        <t:htmlTag value="footer" styleClass="footer">
        	<t:aliasBean alias="#{controller}" value="#{departmentsController}" >
			    <%@include file="_footer.jsp"%>
			</t:aliasBean>
        </t:htmlTag>        
    </t:htmlTag>
</e:page>
