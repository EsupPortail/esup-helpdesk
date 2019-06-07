<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanMove}">
<script type="text/javascript">
function addInvitation(select) {
    if (select.value == "") {
        return;
    }
    var userId = document.getElementById("ticketActionForm:ldapUid");
    if (userId.value != "" && userId.value.charAt(userId.value.length-1) != ",") {
        userId.value += ",";
    }
    userId.value += select.options[select.selectedIndex].value;
    select.selectedIndex = 0;
}
</script>
		   <t:htmlTag id="ticketMove" value="div" styleClass="page-wrapper ticketMove">
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
                            id="ticketActionForm" enctype="multipart/form-data" >
                            <t:htmlTag value="div" styleClass="message">
                                <e:messages/>
                            </t:htmlTag>
							
							<t:htmlTag value="div" styleClass="form-block form-header">
                               <t:htmlTag value="h1">
                                   <t:htmlTag value="span" styleClass="title">
                                         <h:outputText value="#{msgs['TICKET_ACTION.TITLE.MOVE']}" escape="false" />
                                   </t:htmlTag>
                                   <t:htmlTag value="span" styleClass="subtitle">
                                       <h:outputText value=" #{ticketController.ticket.id}" escape="false" />
                                   </t:htmlTag>
                               </t:htmlTag>
                            </t:htmlTag>
					        <t:htmlTag value="div" styleClass="category-filter"  rendered="#{ticketController.moveTargetCategory == null}">
					                <t:htmlTag value="div" styleClass="block form-block">
					                    <t:htmlTag value="div" styleClass="form-item">
					                        <e:outputLabel for="filtreTree" value="#{msgs['TICKET_ACTION.SEARCH.CATEGORY']}" />
					                        <e:inputText id="filtreTree"  title="Recherche" value="#{ticketController.cateFilter}" size="15" onkeypress="if (event.keyCode == 13) { simulateLinkClick('ticketActionForm:filterTreeButton'); return false; }" />
					                    </t:htmlTag>
					                    <t:htmlTag value="div" styleClass="form-item">
					                        <e:commandButton id="filterTreeButton"
					                                    styleClass="button--secondary"
					                                    value="#{msgs['SEARCH.BUTTON.FILTER_CATEGORY']}"
					                                    action="#{ticketController.filterMoveTree}" />
					                        <e:commandButton id="cancelFilterTreeButton"
					                            style ="visibility: hidden"
					                            styleClass="button--cancel"
					                            value="#{msgs['SEARCH.BUTTON.FILTER_CATEGORY.CLEAR']}"
					                            action="#{ticketController.refreshMoveTree}" />
					                    </t:htmlTag>
								    </t:htmlTag>
					
								<h:panelGroup>
									<h:panelGroup style="display:none" onclick="simulateLinkClick('ticketActionForm:cancelButton');" >
										<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
										<t:graphicImage value="/media/images/back.png"
											alt="#{msgs['_.BUTTON.CANCEL']}" 
											title="#{msgs['_.BUTTON.CANCEL']}" />
									</h:panelGroup>
									<e:commandButton style="display: none" id="cancelButton" action="#{controlPanelController.enter}"
										value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
								</h:panelGroup>
					        </t:htmlTag>
                            <t:htmlTag value="div" styleClass="form-block form-menu" rendered="#{ticketController.moveTargetCategory == null}">
                                     <t:htmlTag value="div" styleClass="form-item current">
                                        <t:htmlTag id="showCategoriesTreeview" value="span" styleClass="show-treeview">
                                            <h:outputText value="#{msgs['TICKET_ACTION.TEXT.MOVE.TARGET_CATEGORY.TEXT']}"/>
                                        </t:htmlTag>
                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-item" rendered="#{not empty ticketController.recentMoveItems}">
                                        <t:htmlTag id="showCategoriesRecent" value="span" styleClass=" show-dropdown">
                                            <h:outputText value="#{msgs['TICKET_ACTION.TEXT.MOVE.RECENT']}"/>
                                        </t:htmlTag>
                                     </t:htmlTag>
                            </t:htmlTag>
							<t:htmlTag value="div" styleClass="category_choice" style="margin-top: inherit;" rendered="#{ticketController.moveTargetCategory == null}">
								<h:panelGroup
									rendered="#{ticketController.moveTree == null}">
									<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.ADD.NO_TARGET']}" />
								</h:panelGroup>
								<t:htmlTag value="div" rendered="#{ticketController.moveTree != null}" styleClass="treeview">
		                            <t:htmlTag value="div" styleClass="form-block" rendered="#{ticketController.moveTree == null}">
	                                  <t:htmlTag value="span">
                                        <h:outputText value="#{msgs['TICKET_ACTION.TEXT.MOVE.NO_TARGET']}" escape="false" />
	                                  </t:htmlTag>
		                            </t:htmlTag>
		                            <t:htmlTag value="div" id="recentCategoriesList" styleClass="form-block form-recent-categories hideme" rendered="#{not empty ticketController.recentMoveItems}">
	                                  <t:htmlTag value="div" styleClass="form-item form-select scrollable">
	                                      <e:selectOneMenu value="#{ticketController.moveTargetCategory}"
	                                          converter="#{categoryConverter}" >
	                                          <f:selectItems value="#{ticketController.recentMoveItems}" />
	                                      </e:selectOneMenu>
	                                  </t:htmlTag>
	                                  <t:htmlTag value="div" styleClass="form-item" >
	                                      <e:commandButton id="actionSubmit"
	                                              styleClass="button--secondary"
	                                              value="#{msgs['FORM_SUBMIT_DEFAULT_TEXT']}"
	                                              onclick="submit();" />
	                                       <e:commandButton styleClass="button--cancel" id="cancelMoveButton" action="#{ticketController.cancelAction}"
	                                          		value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
	                                  </t:htmlTag>
		                            </t:htmlTag>								
									<t:tree2  id="tree" value="#{ticketController.moveTree}"
										var="node" varNodeToggler="t" clientSideToggle="true"
										showRootNode="false">
										<f:facet name="root">
											<h:panelGroup >
												<e:italic value=" #{msgs['TICKET_ACTION.TEXT.ADD.ROOT_LABEL']}" />
											</h:panelGroup>
										</f:facet>
										<f:facet name="department">
											<h:panelGroup styleClass="department leaf" onclick="simulateLinkClick('ticketActionForm:tree:#{node.identifier}:t2');">
												<e:text value=" #{node.department.xlabel}">
												</e:text>
											</h:panelGroup>
										</f:facet>
										<f:facet name="category">
											<h:panelGroup styleClass="category leaf #{node.category.addNewTickets or node.leaf ? 'last' : 'parent'}" onclick="simulateLinkClick('ticketActionForm:tree:#{node.identifier}:#{node.category.addNewTickets or node.leaf ? 'chooseCategoryButton' : 't2'}');" >
												<e:text value=" #{msgs['TICKET_ACTION.TEXT.ADD.CATEGORY_LABEL']}" >
														<f:param value="#{node.description}" />
												</e:text>
												<e:commandButton id="chooseCategoryButton" style="display:none" value="->"
													action="#{ticketController.moveChooseCategory}"
													rendered="#{node.category.addNewTickets or node.leaf}" >
													<t:updateActionListener value="#{node.department}"
														property="#{ticketController.moveTargetDepartment}" />
													<t:updateActionListener value="#{node.category}"
														property="#{ticketController.moveTargetCategory}" />														
												</e:commandButton>
											</h:panelGroup>
										</f:facet>
									</t:tree2>
								</t:htmlTag>
							</t:htmlTag>

                                      <t:htmlTag value="div" styleClass="form-block form-category" rendered="#{ticketController.moveTargetCategory != null}">
                                                <t:htmlTag value="div" styleClass="form-item">
                                                      <t:htmlTag value="label">
                                                           <h:outputText value="#{msgs['TICKET_ACTION.TEXT.MOVE.TARGET_CATEGORY_PROMPT']} " />
                                                      </t:htmlTag>

                                                        <e:text styleClass="category-lib"  value=" #{msgs['TICKET_ACTION.TEXT.MOVE.TARGET_CATEGORY_VALUE']}" title="#{ticketController.categoryMoveMembers}"  rendered="#{departmentsController.currentUserIsManager }">
                                                            <f:param value="#{ticketController.moveTargetCategory.department.label}" />
                                                            <f:param value="#{ticketController.moveTargetCategory.label}" />
                                                        </e:text>
                                                        <e:text  styleClass="category-lib" value=" #{msgs['TICKET_ACTION.TEXT.MOVE.TARGET_CATEGORY_VALUE']}" rendered="#{!departmentsController.currentUserIsManager }">
                                                            <f:param value="#{ticketController.moveTargetCategory.department.label}" />
                                                            <f:param value="#{ticketController.moveTargetCategory.label}" />
                                                        </e:text>
                                                        <e:commandButton
                                                                id="chooseAnotherCategoryButton"
                                                                value="#{msgs['TICKET_ACTION.BUTTON.CHANGE_TARGET_CATEGORY']}"
                                                                action="#{ticketController.resetMoveTargetCategory}" >
                                                                <t:updateActionListener value="#{null}" property="#{ticketController.targetManager}" />
                                                        </e:commandButton>
                                                </t:htmlTag>

                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-block"  rendered="#{ticketController.looseTicketManagement}">
                                            <t:htmlTag value="fieldset" styleClass="active">
                                                <t:htmlTag value="legend">
                                                    <t:htmlTag value="span">
                                                        <h:outputText value="#{msgs['TICKET_ACTION.TEXT.MOVE.LOOSE_TICKET_MANAGEMENT.FIELDSET.HEADER.TEXT']}"/>
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                                <t:htmlTag value="div" styleClass="form-block">
                                                    <t:htmlTag value="div">
                                                        <e:paragraph value="#{msgs['TICKET_ACTION.TEXT.MOVE.LOOSE_TICKET_MANAGEMENT.TOP']}" >
                                                            <f:param value="#{ticketController.moveTargetCategory.department.label}" />
                                                        </e:paragraph>
                                                    </t:htmlTag>
                                                    <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                        <e:selectBooleanCheckbox value="#{ticketController.looseTicketManagementMonitor}" id="monitor"/>
                                                        <e:outputLabel for="monitor" value=" #{msgs['TICKET_ACTION.TEXT.MOVE.LOOSE_TICKET_MANAGEMENT.MONITOR']}"/>
                                                    </t:htmlTag>
                                                     <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                         <e:selectBooleanCheckbox value="#{ticketController.looseTicketManagementInvite}" id="invite"/>
                                                         <e:outputLabel for="monitor" value=" #{msgs['TICKET_ACTION.TEXT.MOVE.LOOSE_TICKET_MANAGEMENT.INVITE']}"/>
                                                     </t:htmlTag>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-block form-body" rendered="#{ticketController.moveTargetCategory != null}">
                                            <t:htmlTag value="div" styleClass="form-item">
                                                <e:outputLabel for="actionMessage" value=" #{msgs['TICKET_ACTION.TEXT.MOVE.TOP']}"/>
                                                <fck:editor
                                                    id="actionMessage"
                                                    value="#{ticketController.actionMessage}"
                                                    toolbarSet="actionMessage" />
                                            </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-block" rendered="#{ticketController.moveTargetCategory != null}">
                                            <t:htmlTag value="div" styleClass="form-item form-checkbox" rendered="#{ticketController.moveTargetCategory != null && not ticketController.looseTicketManagement && ticketController.ticket.manager == sessionController.currentUser}">
                                                <e:selectBooleanCheckbox value="#{ticketController.freeTicket}" id="freeTicket"/>
                                                <e:outputLabel for="freeTicket" value=" #{msgs['TICKET_ACTION.TEXT.MOVE.FREE_TICKET']}"/>
                                              </t:htmlTag>

                                            <t:htmlTag value="div" styleClass="form-item display-flex" >
                                                    <e:commandButton id="actionButton"
                                                            styleClass="button--primary"
                                                            value="#{msgs['TICKET_ACTION.BUTTON.MOVE']}"
                                                            action="#{ticketController.doMove}" />
                                                        <%@include file="_ticketActionCancel.jsp"%>
                                           </t:htmlTag>
                                      </t:htmlTag>

                                <t:htmlTag styleClass="region extended-properties" value="div" rendered="#{ticketController.moveTargetCategory != null}">
                                            <t:htmlTag styleClass="tabs" value="ul">
                                                <t:htmlTag id="properties" styleClass="tab-link current" value="li" rendered="#{departmentsController.currentUserCanViewDepartments}">
                                                    <h:outputText value="#{msgs['TICKET_ACTION.TAB.GIVE_INFO.PROPERTIES.TEXT']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="history" styleClass="tab-link  #{!departmentsController.currentUserCanViewDepartments ? 'current' :''}" value="li">
                                                    <h:outputText value="#{msgs['TICKET_ACTION.TAB.HISTORY.TEXT']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="files" styleClass="tab-link" value="li">
                                                    <h:outputText value="#{msgs['TICKET_ACTION.TAB.FILES.TEXT']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="responses" styleClass="tab-link " value="li" rendered="#{ticketController.userCanUseCannedResponses and not empty ticketController.responseItems}">
                                                    <h:outputText value="#{msgs['TICKET_ACTION.TAB.RESPONSES.TEXT']} " />
                                                </t:htmlTag>                                                
                                                <t:htmlTag id="faqs" styleClass="tab-link " value="li" rendered="#{not empty ticketController.moveFaqTree}">
                                                    <h:outputText value="#{msgs['FAQS.TITLE']} " />
                                                </t:htmlTag>
                                            </t:htmlTag>
                                </t:htmlTag>

                                <t:htmlTag id="tab-properties" styleClass="tab-content current" value="div" rendered="#{ticketController.moveTargetCategory != null}">
                                        <t:htmlTag value="div" styleClass="form-block">
                                            <%@include file="_ticketActionScope.jsp"%>
                                            <t:htmlTag value="div" styleClass="form-item form-checkbox" rendered="#{ticketController.userCanSetNoAlert}" >
                                                <e:selectBooleanCheckbox id="noAlert"
                                                    value="#{ticketController.noAlert}" />
                                                <e:outputLabel for="noAlert" value=" #{msgs['TICKET_ACTION.TEXT.NO_NOTIFICATION']}"/>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag id="tab-history" styleClass="tab-content view-ticket_history #{!departmentsController.currentUserCanViewDepartments ? 'current' :''}" value="div" rendered="#{ticketController.moveTargetCategory != null}">
                                        <%@include file="_ticketActionHistory.jsp"%>
                                </t:htmlTag>
                                <t:htmlTag id="tab-files" styleClass="tab-content" value="div" rendered="#{ticketController.moveTargetCategory != null}">
                                        <%@include file="_ticketActionTabUpload.jsp"%>
                                </t:htmlTag>
                                <t:htmlTag id="tab-responses" styleClass="tab-content" value="div" rendered="#{ticketController.moveTargetCategory != null}">
                                        <%@include file="_ticketActionResponses.jsp"%>
                                </t:htmlTag>                                
                                <t:htmlTag id="tab-faqs" styleClass="tab-content" value="div" rendered="#{not empty ticketController.moveFaqTree}">
                                        <%@include file="_ticketActionFaqs.jsp"%>
                                </t:htmlTag>

                            <h:panelGroup rendered="#{ticketController.moveTargetCategory != null}" >
                                <h:panelGroup rendered="#{not ticketController.looseTicketManagement}" >
                                    <e:panelGrid columns="2" width="100%" columnClasses="colLeft,colLeftMax"
                                        rendered="#{ticketController.ticket.manager == sessionController.currentUser}" >

                                    </e:panelGrid>
                                </h:panelGroup>


                            </h:panelGroup>
	                    </e:form>
	                    <%@include file="_ticketActionJavascript.jsp"%>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
    <t:htmlTag value="footer" styleClass="footer">
            <%@include file="_footer.jsp"%>
    </t:htmlTag>

</e:page>

