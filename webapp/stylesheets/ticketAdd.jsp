<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}" >
	   <t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper ticket-add">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content">
                    <t:htmlTag value="div" styleClass="content-inner">
	<h:panelGroup rendered="#{not ticketController.userCanAdd}" >
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ticketActionForm" rendered="#{ticketController.userCanAdd}" enctype="multipart/form-data" styleClass="ticketActionForm">

        <t:htmlTag value="div" styleClass="message ticketView-message">
           <e:messages/>
        </t:htmlTag>

        <t:htmlTag value="div" styleClass="category-filter"  rendered="#{not ticketController.showAddHelp and ticketController.addTargetCategory == null}">
                <t:htmlTag value="h1">
                    <t:htmlTag value="span" styleClass="title">
                          <h:outputText value="#{msgs['TICKET_ACTION.TITLE.ADD']}" escape="false" />
                    </t:htmlTag>
                    <t:htmlTag value="span" styleClass="subtitle">
                        <h:outputText value=" : #{msgs['TICKET_ACTION.TITLE.ETAPE_1.ADD']}" escape="false" />
                    </t:htmlTag>
                </t:htmlTag>

                <t:htmlTag value="div" styleClass="block form-block" rendered="#{ticketController.addTargetDepartment == null}">
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:outputLabel for="filtreTree" value="#{msgs['TICKET_ACTION.SEARCH.CATEGORY']}" />
                        <e:inputText id="filtreTree"  title="Recherche" value="#{ticketController.cateFilter}" size="15" onkeypress="if (event.keyCode == 13) { simulateLinkClick('ticketActionForm:filterTreeButton'); return false; }" />
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="form-item">
                        <e:commandButton id="filterTreeButton"
                                    styleClass="button--secondary"
                                    value="#{msgs['SEARCH.BUTTON.FILTER_CATEGORY']}"
                                    action="#{ticketController.filterAddTree}" />
                        <e:commandButton id="cancelFilterTreeButton"
                            style ="visibility: hidden"
                            styleClass="button--cancel"
                            value="#{msgs['SEARCH.BUTTON.FILTER_CATEGORY.CLEAR']}"
                            action="#{ticketController.refreshAddTree}" />
                    </t:htmlTag>
			    </t:htmlTag>
        </t:htmlTag>

		<h:panelGroup rendered="#{ticketController.showAddHelp}">
			<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.ADD.HELP.TOP.1']}" rendered="#{msgs['TICKET_ACTION.TEXT.ADD.HELP.TOP.1'] != ''}" />
			<e:li value="#{msgs['TICKET_ACTION.TEXT.ADD.HELP.STEP.1']}" />
			<e:li value="#{msgs['TICKET_ACTION.TEXT.ADD.HELP.STEP.2']}" />
			<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.ADD.HELP.BOTTOM.1']}" rendered="#{msgs['TICKET_ACTION.TEXT.ADD.HELP.BOTTOM.1'] != ''}" />
			<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.ADD.HELP.BOTTOM.2']}" rendered="#{msgs['TICKET_ACTION.TEXT.ADD.HELP.BOTTOM.2'] != ''}" />
			<e:selectBooleanCheckbox 
				value="#{ticketController.skipAddHelp}" 
				onchange="javascript:{simulateLinkClick('ticketActionForm:gotoChooseCategoryButton');}" />
			<e:text value="#{msgs['TICKET_ACTION.TEXT.ADD.HELP.SKIP']}" />
			<t:htmlTag value="br" />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:gotoChooseCategoryButton');" >
					<e:bold value="#{msgs['_.BUTTON.NEXT']} " />
					<t:graphicImage value="/media/images/next.png"
						alt="#{msgs['_.BUTTON.NEXT']}" 
						title="#{msgs['_.BUTTON.NEXT']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="gotoChooseCategoryButton" 
					value="#{msgs['_.BUTTON.NEXT']}" 
					action="continue" >
					<t:updateActionListener value="false" property="#{ticketController.showAddHelp}" />
				</e:commandButton>
			</h:panelGroup>
		</h:panelGroup>

	
		<t:htmlTag value="div" styleClass="category_choice" rendered="#{not ticketController.showAddHelp and ticketController.addTargetCategory == null and ticketController.addTargetDepartment == null}">
			<h:panelGroup
				rendered="#{ticketController.addTree == null}">
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.ADD.NO_TARGET']}" />
			</h:panelGroup>
			<t:htmlTag value="div" rendered="#{ticketController.addTree != null}" styleClass="treeview">
				<t:tree2  id="tree" value="#{ticketController.addTree}"
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
								action="#{ticketController.addChooseCategory}"
								rendered="#{node.category.addNewTickets or node.leaf}" >
								<t:updateActionListener value="#{node.department}"
									property="#{ticketController.addTargetDepartment}" />
								<t:updateActionListener value="#{node.category}"
									property="#{ticketController.addTargetCategory}" />
							</e:commandButton>
						</h:panelGroup>
					</f:facet>
				</t:tree2>
			</t:htmlTag>
		</t:htmlTag>

		<t:htmlTag value="div" styleClass="category_choice" rendered="#{not ticketController.showAddHelp and ticketController.addTargetCategory == null and ticketController.addTargetDepartment != null}">
			<h:panelGroup
				rendered="#{ticketController.filteredTree == null}">
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.ADD.NO_TARGET']}" />
			</h:panelGroup>
			<t:htmlTag value="div" rendered="#{ticketController.filteredTree != null}" styleClass="treeview">
				<t:tree2  id="tree" value="#{ticketController.filteredTree}"
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
								action="#{ticketController.addChooseCategory}"
								rendered="#{node.category.addNewTickets or node.leaf}" >
								<t:updateActionListener value="#{node.department}"
									property="#{ticketController.addTargetDepartment}" />
								<t:updateActionListener value="#{node.category}"
									property="#{ticketController.addTargetCategory}" />
							</e:commandButton>
						</h:panelGroup>
					</f:facet>
				</t:tree2>
			</t:htmlTag>
		</t:htmlTag>
						
		<t:htmlTag value="div" styleClass="ticket-form" rendered="#{not ticketController.showAddHelp and ticketController.addTargetCategory != null}">

			<t:htmlTag value="div" styleClass="form-block form-header">
                    <t:htmlTag value="h1">
                        <t:htmlTag value="span" styleClass="title">
                              <h:outputText value="#{msgs['TICKET_ACTION.TITLE.ADD']}" escape="false" />
                        </t:htmlTag>
                        <t:htmlTag value="span" styleClass="subtitle">
                            <h:outputText value=" : #{msgs['TICKET_ACTION.TITLE.ETAPE_2.ADD']}" escape="false" />
                        </t:htmlTag>
                    </t:htmlTag>
			 </t:htmlTag>

			 <t:htmlTag value="div" styleClass="form-block form-category">
			    <t:htmlTag value="div" styleClass="form-item">
                   <t:htmlTag value="label">
                        <h:outputText value="#{msgs['TICKET_ACTION.TEXT.ADD.TARGET_CATEGORY']} " />
                   </t:htmlTag>
                   <t:htmlTag value="span" styleClass="category-lib">
                        <h:outputText value="#{ticketController.addTargetDepartment.label} - #{ticketController.addTargetCategory.xlabel}" />
                   </t:htmlTag>
                </t:htmlTag>
			 </t:htmlTag>

            <t:htmlTag value="div" styleClass="form-block form-subject">
                <t:htmlTag value="div" styleClass="form-item">
                    	<e:outputLabel for="label" value="#{msgs['TICKET_ACTION.TEXT.ADD.LABEL_PROMPT']} " />
                    	<e:inputText id="label" value="#{ticketController.ticketLabel}" size="50"
                    							onkeypress="if (event.keyCode == 13) { focusFckEditor('ticketActionForm:actionMessage'); return false;}" />
                </t:htmlTag>
            </t:htmlTag>

            <t:htmlTag value="div" styleClass="form-block form-body">
                <t:htmlTag value="div" styleClass="form-item">
                     <t:htmlTag value="div" styleClass="block">
                         <h:outputText value="#{msgs['TICKET_ACTION.TEXT.ADD.TOP']}" rendered="#{ticketController.addFaqTree == null}"/>
                         <h:outputText value="#{msgs['TICKET_ACTION.TEXT.ADD.TOP_FAQ_LINKS']}" rendered="#{ticketController.addFaqTree != null}"/>
                     </t:htmlTag>
                      <fck:editor  id="actionMessage"
                                        styleClass="fck-container"
                                        value="#{ticketController.actionMessage}"
                                        toolbarSet="actionMessage" />
                </t:htmlTag>
           </t:htmlTag>

            <t:htmlTag  value="div" styleClass="form-block form-files">
                <%@include file="_ticketActionUpload.jsp"%>
            </t:htmlTag>

			<t:htmlTag  value="div" styleClass="form-block form-properties">
                <t:htmlTag id="ticketProperties" value="div" styleClass=" block accordion accordion-plus">
                    <t:htmlTag value="h2">
                          <t:htmlTag value="span"><h:outputText value="#{msgs['TICKET_ACTION.TEXT.ADD.SHOW_ADVANCED']}" escape="false" /></t:htmlTag>
                          <t:htmlTag value="i" styleClass="fas fa-chevron-down"/>
                    </t:htmlTag>
		    <t:htmlTag value="hr">
                         <h:outputText value="#{msgs['TICKET_ACTION.MESSAGE.ADD.TICKET.PUBLIC']}" rendered="#{ticketController.ticketScope == 'PUBLIC'"/>
	            </t:htmlTag>
                    <t:htmlTag value="div" styleClass="content">
                        <t:htmlTag value="div" styleClass="form-block">
                            <t:htmlTag value="div" styleClass="form-item">
                                    <e:outputLabel for="scope" value="#{msgs['TICKET_ACTION.TEXT.ADD.SCOPE_PROMPT']} " />
                                    <h:panelGroup>
                                        <e:selectOneMenu id="scope"
                                            value="#{ticketController.ticketScope}" >
                                            <f:selectItems value="#{ticketController.ticketScopeItems}" />
                                        </e:selectOneMenu>
                                        <e:italic value=" #{msgs['TICKET_ACTION.TEXT.ADD.SCOPE_HELP']}" >
                                            <f:param value="#{msgs[ticketScopeI18nKeyProvider[ticketController.addTargetCategory.effectiveDefaultTicketScope]]}" />
                                        </e:italic>
                                    </h:panelGroup>
                             </t:htmlTag>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-block">
                            <t:htmlTag value="div" styleClass="form-item">
                                     <e:outputLabel
                                         for="priority"
                                         value="#{msgs['TICKET_ACTION.TEXT.ADD.PRIORITY_PROMPT']} " />
                                     <h:panelGroup>
                                         <e:selectOneMenu id="priority"
                                             value="#{ticketController.ticketPriority}" >
                                             <f:selectItems value="#{ticketController.ticketPriorityItems}" />
                                         </e:selectOneMenu>
                                         <e:italic value=" #{msgs['TICKET_ACTION.TEXT.ADD.PRIORITY_HELP']}" >
                                             <f:param value="#{msgs[priorityI18nKeyProvider[ticketController.addTargetCategory.effectiveDefaultTicketPriority]]}" />
                                         </e:italic>
                                     </h:panelGroup>
                             </t:htmlTag>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-block">
                            <t:htmlTag value="div" styleClass="form-item">
                                    <e:outputLabel
                                        for="origin"
                                        value="#{msgs['TICKET_ACTION.TEXT.ADD.ORIGIN_PROMPT']} " />
                                    <h:panelGroup id="origin">
                                        <e:selectOneMenu
                                            value="#{ticketController.ticketOrigin}"
                                            rendered="#{ticketController.userCanSetOrigin}" >
                                            <f:selectItems value="#{ticketController.originItems}" />
                                        </e:selectOneMenu>
                                        <e:text value="#{msgs[originI18nKeyProvider[ticketController.ticketOrigin]]}"
                                            rendered="#{not ticketController.userCanSetOrigin}" />
                                    </h:panelGroup>
                              </t:htmlTag>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-block">
                             <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                    <e:outputLabel for="owner" value="#{msgs['TICKET_ACTION.TEXT.ADD.OWNER_PROMPT']} "
                                        rendered="#{ticketController.userCanSetOwner}" />
                                        <h:panelGroup rendered="#{ticketController.userCanSetOwner}" >
                                            <e:inputText id="owner" value="#{ticketController.ldapUid}" size="50"
                                                onkeypress="if (event.keyCode == 13) { return false;}" />
                                            <h:panelGroup rendered="#{domainService.useLdap}" >

                                                <e:commandButton
                                                    id="ldapSearchButton" action="#{ldapSearchController.firstSearch}"
                                                    value="#{msgs['_.BUTTON.LDAP']}" >
                                                    <t:updateActionListener value="#{ticketController}"
                                                        property="#{ldapSearchController.caller}" />
                                                    <t:updateActionListener value="userSelectedToTicketAdd"
                                                        property="#{ldapSearchController.successResult}" />
                                                    <t:updateActionListener value="cancelToTicketAdd"
                                                        property="#{ldapSearchController.cancelResult}" />
                                                </e:commandButton>
                                            </h:panelGroup>
                                            <t:htmlTag value="br" />
                                            <e:italic value=" #{domainService.useLdap ? msgs['TICKET_ACTION.TEXT.ADD.OWNER_HELP_LDAP'] : msgs['TICKET_ACTION.TEXT.ADD.OWNER_HELP_NO_LDAP']}" />
                                        </h:panelGroup>
                             </t:htmlTag>
                        </t:htmlTag>
                    </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>

            <h:panelGroup rendered="#{ticketController.addFaqTree != null}" >
            <t:htmlTag value="div" styleClass="form-block form-faqlinks">
                <t:htmlTag id="ticketFaqs" value="div" styleClass=" block accordion accordion-plus">
                    <t:htmlTag value="h2">
                          <t:htmlTag value="span"><h:outputText value="#{msgs['TICKET_ACTION.TEXT.ADD.FAQ_LINKS']}" escape="false" /></t:htmlTag>
                          <t:htmlTag value="i" styleClass="fas fa-chevron-down"/>
                    </t:htmlTag>

                    <t:htmlTag value="div" styleClass="content">
                        <t:tree2 id="faqTree" value="#{ticketController.addFaqTree}"
							var="node" varNodeToggler="t" clientSideToggle="true"
							showRootNode="false" >
							<f:facet name="root">
								<h:panelGroup>
									<h:panelGroup style="white-space: nowrap" >
										<e:italic value=" #{msgs['TICKET_ACTION.TEXT.ADD.FAQ_LINKS_HELP']}" />
									</h:panelGroup>
								</h:panelGroup>
							</f:facet>
							<f:facet name="faq">
								<h:panelGroup>
									<h:panelGroup style="cursor: pointer; white-space: nowrap"
										onclick="showHideElement('ticketActionForm:faqTree:#{node.identifier}:faqContent');return false;">
										<e:bold value=" #{node.faq.label} " />
									</h:panelGroup>
									<t:htmlTag value="div" id="faqContent"  styleClass="faq-content" style="display: none">
									<e:text  escape="false" value="#{node.faq.content}" />
									</t:htmlTag>
								</h:panelGroup>
							</f:facet>
						</t:tree2>
                    </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
            </h:panelGroup>

            <t:htmlTag value="div" styleClass="form-block">
                <t:htmlTag value="div" styleClass="form-item display-flex" >
                    <e:commandButton id="addButton"
                            styleClass="button--primary"
                            value="#{msgs['TICKET_ACTION.BUTTON.ADD']}"
                            action="#{ticketController.doAdd}" />

                    <e:commandButton id="changeCategoryButton"
                             styleClass="button--cancel"
                             value="#{msgs['TICKET_ACTION.BUTTON.CREATE.CANCEL']}"
                             action="#{ticketController.add}" />
                   </t:htmlTag>
            </t:htmlTag>


		</t:htmlTag>

	</e:form>

	<%@include file="_ticketActionJavascript.jsp" %>

	            </t:htmlTag>

            </t:htmlTag>

	</t:htmlTag>
	<t:htmlTag value="footer" styleClass="footer">
		<t:aliasBean alias="#{controller}" value="#{ticketController}" >
		    <%@include file="_footer.jsp"%>
		</t:aliasBean>
	</t:htmlTag>
	</t:htmlTag>
</e:page>

