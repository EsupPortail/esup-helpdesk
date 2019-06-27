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
								id="categoryFaqLinksForm">
								
							    <t:htmlTag value="div" styleClass="message">
                                       <e:messages/>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="dashboard-header">
                                    <t:htmlTag value="div" styleClass="controlPanel-title">
                                        <t:htmlTag value="h1">
                                            <t:htmlTag value="span">
                                                <e:text value="#{msgs['CATEGORY_FAQ_LINKS.TITLE']}">
                                                	<f:param value="#{departmentsController.categoryToUpdate.department.label}" />
												<f:param value="#{departmentsController.categoryToUpdate.label}" />
											 </e:text>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                    </t:htmlTag>
                                </t:htmlTag>

                                <t:htmlTag value="div" styleClass="form-block form-header">
                                     <t:htmlTag value="div" styleClass="form-item">
                                           <e:commandButton id="cancelButton" action="back" value="#{msgs['_.BUTTON.BACK']}" immediate="true" />
                                     </t:htmlTag>
                                </t:htmlTag>
                                
								<h:panelGroup rendered="#{departmentsController.categoryToUpdate.inheritFaqLinks}" >
									<h:panelGroup rendered="#{departmentsController.categoryToUpdate.parent != null}" >
										<h:panelGroup rendered="#{not departmentsController.isFaqLinksDepartment}" >
											<e:paragraph 
												value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.INHERIT_CATEGORY']}"/>
											<h:panelGroup>
												<t:htmlTag value="div" styleClass="form-block form-submit">
												     <t:htmlTag value="div" styleClass="form-item" >
															<e:commandButton id="inheritCategoryButton1" action="#{departmentsController.toggleCategoryInheritFaqLinks}"
															    styleClass="button--secondary"
																value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.DO_NOT_INHERIT_CATEGORY']}"/>
												     </t:htmlTag>
												 </t:htmlTag>
											</h:panelGroup>	
											
											<e:paragraph value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.FAQ_LINKS']}" />
											<e:dataTable
												id="faqLinkData" rowIndexVar="variable"
												value="#{departmentsController.faqLinksParent}"
												var="faqLink" border="0" cellspacing="0"
												cellpadding="0">
												<f:facet name="header">
													<t:htmlTag value="hr" />
												</f:facet>
												<t:column style="white-space: nowrap" >
													<e:text value=" #{faqLink.faq.label}" />
												</t:column>
												<f:facet name="footer">
													<t:htmlTag value="hr" />
												</f:facet>
											</e:dataTable>
										</h:panelGroup>
										
										
										<h:panelGroup rendered="#{departmentsController.isFaqLinksDepartment}" >
											<e:paragraph 
												value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.INHERIT_CATEGORY']}"/>
											<h:panelGroup>
												<t:htmlTag value="div" styleClass="form-block form-submit">
												     <t:htmlTag value="div" styleClass="form-item" >
															<e:commandButton id="inheritCategoryButton2" action="#{departmentsController.toggleCategoryInheritFaqLinks}"
															    styleClass="button--secondary"
																value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.DO_NOT_INHERIT_CATEGORY']}"/>
												     </t:htmlTag>
												 </t:htmlTag>
											</h:panelGroup>	
											<e:paragraph 
												value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.NO_INHERITED_FAQ_LINK']}" 
												rendered="#{empty departmentsController.departmentFaqs}" />
											<e:paragraph 
												value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.INHERITED_FAQ_LINKS_CATEGORY']}" 
												rendered="#{not empty departmentsController.departmentFaqs}" />
												
												<t:htmlTag value="div" styleClass="form-block treeview readonly-style ">
												   <t:htmlTag value="div" styleClass="form-item">
												        <t:tree2 id="treefaq2" value="#{faqsController.viewTree}"
												            var="node" varNodeToggler="t" clientSideToggle="true"
												            showRootNode="false" >
												            <f:facet name="faq">
												                <h:panelGroup styleClass="faq">
												                          <h:panelGroup styleClass="leaf" style="cursor: pointer"  rendered="#{node.leaf}" onclick="simulateLinkClick('departmentViewForm:treefaq2:#{node.identifier}:selectFaq');return false;">
												                                    <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
												                                    <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
												                          </h:panelGroup>
												                          <h:panelGroup styleClass="parentLeaf" rendered="#{!node.leaf}">
												                                    <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
												                                    <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
												                          </h:panelGroup>
												
												                    <h:panelGroup style="cursor: pointer; display:none" onclick="simulateLinkClick('departmentViewForm:treeFaq2:#{node.identifier}:selectfaq');return false;">
												                        <t:graphicImage style="display:none" value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" rendered="#{not node.leaf}" />
												                        <e:text value=" " />
												                        <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
												                        <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
												                    </h:panelGroup>
												
												                    <e:commandButton value="->" id="selectFaq" style="display: none" >
												                        <t:updateActionListener value="#{node}" property="#{faqsController.node}" />
												                    </e:commandButton>
												                </h:panelGroup>
												            </f:facet>
												        </t:tree2>
												   </t:htmlTag>
												</t:htmlTag>
										</h:panelGroup>										
									</h:panelGroup>
									
									<h:panelGroup rendered="#{departmentsController.categoryToUpdate.parent == null}" >
										<e:paragraph 
											value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.INHERIT_DEPARTMENT']}"/>
										<h:panelGroup>
											<t:htmlTag value="div" styleClass="form-block form-submit">
											     <t:htmlTag value="div" styleClass="form-item" >
														<e:commandButton id="inheritDepartmentButton" action="#{departmentsController.toggleCategoryInheritFaqLinks}"
														    styleClass="button--secondary"
															value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.DO_NOT_INHERIT_DEPARTMENT']}"/>
											     </t:htmlTag>
											 </t:htmlTag>
										</h:panelGroup>
										<e:paragraph 
											value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.NO_INHERITED_FAQ_LINK']}" 
											rendered="#{empty departmentsController.departmentFaqs}" />
										<e:paragraph 
											value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.INHERITED_FAQ_LINKS_DEPARTMENT']}" 
											rendered="#{not empty departmentsController.departmentFaqs}" />
										<h:panelGroup rendered="#{not empty departmentsController.departmentFaqs}">
											<t:htmlTag value="div" styleClass="form-block treeview readonly-style ">
											   <t:htmlTag value="div" styleClass="form-item">
											        <t:tree2 id="treefaq3" value="#{faqsController.viewTree}"
											            var="node" varNodeToggler="t" clientSideToggle="true"
											            showRootNode="false" >
											
											            
											            <f:facet name="faq">
											                <h:panelGroup styleClass="faq">
											                          <h:panelGroup styleClass="leaf" style="cursor: pointer"  rendered="#{node.leaf}" onclick="simulateLinkClick('departmentViewForm:treefaq2:#{node.identifier}:selectFaq');return false;">
											                                    <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
											                                    <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
											                          </h:panelGroup>
											                          <h:panelGroup styleClass="parentLeaf" rendered="#{!node.leaf}">
											                                    <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
											                                    <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
											                          </h:panelGroup>
											
											                    <h:panelGroup style="cursor: pointer; display:none" onclick="simulateLinkClick('departmentViewForm:treeFaq3:#{node.identifier}:selectfaq');return false;">
											                        <t:graphicImage style="display:none" value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" rendered="#{not node.leaf}" />
											                        <e:text value=" " />
											                        <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
											                        <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
											                    </h:panelGroup>
											
											                    <e:commandButton value="->" id="selectFaq" style="display: none" >
											                        <t:updateActionListener value="#{node}" property="#{faqsController.node}" />
											                    </e:commandButton>
											                </h:panelGroup>
											            </f:facet>
											        </t:tree2>
											   </t:htmlTag>
											</t:htmlTag>
										</h:panelGroup>
									</h:panelGroup>
								</h:panelGroup>
								
								<h:panelGroup rendered="#{not departmentsController.categoryToUpdate.inheritFaqLinks}" >
									<h:panelGroup rendered="#{departmentsController.categoryToUpdate.parent == null}" >
										<e:paragraph value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.NO_INHERIT_DEPARTMENT']}" />
										<t:htmlTag value="div" styleClass="form-block form-submit">
										     <t:htmlTag value="div" styleClass="form-item" >
													<e:commandButton id="notInheritDepartmentButton" action="#{departmentsController.toggleCategoryInheritFaqLinks}"
													    styleClass="button--secondary"
														value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.INHERIT_DEPARTMENT']}"/>
										     </t:htmlTag>
										</t:htmlTag>
									</h:panelGroup>
									<h:panelGroup rendered="#{departmentsController.categoryToUpdate.parent != null}" >
										<e:paragraph value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.NO_INHERIT_CATEGORY']}" />
										<t:htmlTag value="div" styleClass="form-block form-submit">
										     <t:htmlTag value="div" styleClass="form-item" >
													<e:commandButton id="notInheritCategoryButton" action="#{departmentsController.toggleCategoryInheritFaqLinks}"
													    styleClass="button--secondary"
														value="#{msgs['CATEGORY_FAQ_LINKS.BUTTON.INHERIT_CATEGORY']}"/>
										     </t:htmlTag>
										 </t:htmlTag>
									</h:panelGroup>
									
								 	<e:paragraph 
										value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.NO_INHERITED_FAQ_LINK']}" 
										rendered="#{empty departmentsController.faqLinks }" />
									<h:panelGroup rendered="#{not empty departmentsController.faqLinks}" >
										<e:paragraph value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.FAQ_LINKS']}" />
										<e:dataTable
											id="faqLinkData1" rowIndexVar="variable"
											value="#{departmentsController.faqLinks}"
											var="faqLink" border="0" cellspacing="0"
											cellpadding="0">
											<f:facet name="header">
												<t:htmlTag value="hr" />
											</f:facet>
											<t:column style="white-space: nowrap" >
												<e:text value=" #{faqLink.faq.label}" />
											</t:column>
											<t:column>
												<h:panelGroup style="cursor: pointer" 
													onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData1:#{variable}:deleteButton');" >
													<t:graphicImage value="/media/images/delete.png"
														alt="-" title="-" />
												</h:panelGroup>
												<e:commandButton id="deleteButton" value="-" style="display: none" 
													action="#{departmentsController.deleteCategoryFaqLink}" >
													<t:updateActionListener value="#{faqLink}"
														property="#{departmentsController.faqLinkToDelete}" />
												</e:commandButton>
											</t:column>
											<t:column>
												<h:panelGroup
													rendered="#{variable != departmentsController.faqLinksNumber - 1}" >
													<h:panelGroup style="cursor: pointer" 
														onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData1:#{variable}:moveLastButton');" >
														<t:graphicImage value="/media/images/arrow_last.png"
															alt="vv" title="vv" />
													</h:panelGroup>
													<e:commandButton id="moveLastButton" value="vv" style="display: none" 
														action="#{departmentsController.moveCategoryFaqLinkLast}" 
														>
														<t:updateActionListener value="#{faqLink}"
															property="#{departmentsController.faqLinkToMove}" />
													</e:commandButton>
												</h:panelGroup>
											</t:column>
											<t:column>
												<h:panelGroup
													rendered="#{variable != departmentsController.faqLinksNumber - 1}" >
													<h:panelGroup style="cursor: pointer" 
														onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData1:#{variable}:moveDownButton');" >
														<t:graphicImage value="/media/images/arrow_down.png"
															alt="v" title="v" />
													</h:panelGroup>
													<e:commandButton id="moveDownButton" value="v" style="display: none" 
														action="#{departmentsController.moveCategoryFaqLinkDown}" 
														>
														<t:updateActionListener value="#{faqLink}"
															property="#{departmentsController.faqLinkToMove}" />
													</e:commandButton>
												</h:panelGroup>
											</t:column>
											<t:column>
												<h:panelGroup
													rendered="#{variable != 0}" >
													<h:panelGroup style="cursor: pointer" 
														onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData1:#{variable}:moveUpButton');" >
														<t:graphicImage value="/media/images/arrow_up.png"
															alt="^" title="^" />
													</h:panelGroup>
													<e:commandButton id="moveUpButton" value="^" style="display: none" 
														action="#{departmentsController.moveCategoryFaqLinkUp}" 
														>
														<t:updateActionListener value="#{faqLink}"
															property="#{departmentsController.faqLinkToMove}" />
													</e:commandButton>
												</h:panelGroup>
											</t:column>
											<t:column>
												<h:panelGroup
													rendered="#{variable != 0}" >
													<h:panelGroup style="cursor: pointer" 
														onclick="simulateLinkClick('categoryFaqLinksForm:faqLinkData1:#{variable}:moveFirstButton');" >
														<t:graphicImage value="/media/images/arrow_first.png"
															alt="^^" title="^^" />
													</h:panelGroup>
													<e:commandButton id="moveFirstButton" value="^^" style="display: none" 
														action="#{departmentsController.moveCategoryFaqLinkFirst}" 
														>
														<t:updateActionListener value="#{faqLink}"
															property="#{departmentsController.faqLinkToMove}" />
													</e:commandButton>
												</h:panelGroup>
											</t:column>
											<f:facet name="footer">
												<t:htmlTag value="hr" />
											</f:facet>
										</e:dataTable>
									</h:panelGroup>
									<h:panelGroup>
										<e:subSection value="#{msgs['CATEGORY_FAQ_LINKS.TEXT.ADD_FAQ_LINKS']}" />
										<%@include file="_faqsTreeDptSelect.jsp"%>
									</h:panelGroup>
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
