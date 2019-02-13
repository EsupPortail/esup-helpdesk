<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanAssign}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ticketActionForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['TICKET_ACTION.TITLE.ASSIGN']}" >
				<f:param value="#{ticketController.ticket.id}" />
			</e:section>
			<%@include file="_ticketActionCancel.jsp"%>
		</e:panelGrid>
		<e:messages />
		<e:paragraph
			value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.NO_AVAILABLE_MANAGER']}" 
			rendered="#{empty ticketController.targetCategoryMembers and empty ticketController.targetNonCategoryMembers}" />
		<h:panelGroup rendered="#{not empty ticketController.targetCategoryMembers or not empty ticketController.targetNonCategoryMembers}" >
			<h:panelGroup rendered="#{ticketController.targetManager == null}" >
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.1']}" />
				<e:dataTable
					id="targetCategoryMembersData"
					value="#{ticketController.targetCategoryMembers}" var="departmentManager"
					rendered="#{not empty ticketController.targetCategoryMembers}" 
					border="0" cellspacing="0" cellpadding="0" rowIndexVar="index" >
					<t:column>
						<f:facet name="header">
							<h:panelGroup>
								<e:panelGrid rendered="#{ticketController.targetCategoryMembersNumber gt 1}" >
									<e:selectOneMenu onchange="javascript:{simulateLinkClick('ticketActionForm:targetCategoryMembersData:sortTargetCategoryMembersButton');}"
										value="#{ticketController.targetCategoryMembersPresentOrder}">
										<f:selectItem 
											itemLabel="#{msgs['TICKET_ACTION.TEXT.ASSIGN.SORT_TARGET_CATEGORY_MEMBERS.ORDER']}"
											itemValue="order" />
										<f:selectItem 
											itemLabel="#{msgs['TICKET_ACTION.TEXT.ASSIGN.SORT_TARGET_CATEGORY_MEMBERS.DISPLAY_NAME']}"
											itemValue="displayName" />
										<f:selectItem 
											itemLabel="#{msgs['TICKET_ACTION.TEXT.ASSIGN.SORT_TARGET_CATEGORY_MEMBERS.ID']}"
											itemValue="id" />
									</e:selectOneMenu>
									<e:commandButton value="#{msgs['TICKET_ACTION.BUTTON.SORT_TARGET_CATEGORY_MEMBERS']}" 
										id="sortTargetCategoryMembersButton" style="display: none"
										action="#{ticketController.refreshTicket}" />
								</e:panelGrid>
								<t:htmlTag value="hr" />
								<e:bold value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.CATEGORY_MEMBERS']}" />
							</h:panelGroup>
						</f:facet>
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:targetCategoryMembersData:#{index}:chooseManagerButton');" >
							<t:graphicImage value="/media/images/assign.png" />
							<e:bold value=" #{msgs['TICKET_ACTION.TEXT.ASSIGN.USER']}" >
								<f:param value="#{userFormatter[departmentManager.user]}" />
							</e:bold>
						</h:panelGroup>
					</t:column>
					<t:column>
						<e:commandButton style="display: none" id="chooseManagerButton" 
							value="->" immediate="true" >
							<t:updateActionListener value="#{departmentManager.user}" property="#{ticketController.targetManager}" />
						</e:commandButton>
					</t:column>
				</e:dataTable>
				<e:dataTable
					id="targetNonCategoryMembersData"
					value="#{ticketController.targetNonCategoryMembers}" var="departmentManager"
					rendered="#{not empty ticketController.targetNonCategoryMembers}" 
					border="0" cellspacing="0" cellpadding="0" rowIndexVar="index" >
					<t:column>
						<f:facet name="header">
							<h:panelGroup>
								<e:panelGrid rendered="#{ticketController.targetNonCategoryMembersNumber gt 1}" >
									<e:selectOneMenu onchange="javascript:{simulateLinkClick('ticketActionForm:targetNonCategoryMembersData:sortTargetNonCategoryMembersButton');}"
										value="#{ticketController.targetNonCategoryMembersPresentOrder}">
										<f:selectItem 
											itemLabel="#{msgs['TICKET_ACTION.TEXT.ASSIGN.SORT_TARGET_NON_CATEGORY_MEMBERS.ORDER']}"
											itemValue="order" />
										<f:selectItem 
											itemLabel="#{msgs['TICKET_ACTION.TEXT.ASSIGN.SORT_TARGET_NON_CATEGORY_MEMBERS.DISPLAY_NAME']}"
											itemValue="displayName" />
										<f:selectItem 
											itemLabel="#{msgs['TICKET_ACTION.TEXT.ASSIGN.SORT_TARGET_NON_CATEGORY_MEMBERS.ID']}"
											itemValue="id" />
									</e:selectOneMenu>
									<e:commandButton value="#{msgs['TICKET_ACTION.BUTTON.SORT_TARGET_NON_CATEGORY_MEMBERS']}" 
										id="sortTargetNonCategoryMembersButton" style="display: none"
										action="#{ticketController.refreshTicket}" />
								</e:panelGrid>
								<t:htmlTag value="hr" />
								<e:bold value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.NON_CATEGORY_MEMBERS']}" />
							</h:panelGroup>
						</f:facet>
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:targetNonCategoryMembersData:#{index}:chooseManagerButton');" >
							<t:graphicImage value="/media/images/assign.png" />
							<e:bold value=" #{msgs['TICKET_ACTION.TEXT.ASSIGN.USER']}" >
								<f:param value="#{userFormatter[departmentManager.user]}" />
							</e:bold>
						</h:panelGroup>
					</t:column>
					<t:column>
						<e:commandButton style="display: none" id="chooseManagerButton" 
							value="->" immediate="true" >
							<t:updateActionListener value="#{departmentManager.user}" property="#{ticketController.targetManager}" />
						</e:commandButton>
					</t:column>
				</e:dataTable>
			</h:panelGroup>
			<h:panelGroup rendered="#{ticketController.targetManager != null}" >
				<e:text value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.MANAGER_PROMPT']} " />
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:chooseAnotherManagerButton');" >
					<t:graphicImage value="/media/images/manager.png" />
					<e:bold value=" #{msgs['TICKET_ACTION.TEXT.ASSIGN.MANAGER_VALUE']}" >
						<f:param value="#{userFormatter[ticketController.targetManager]}" />
					</e:bold>
				</h:panelGroup>
				<e:italic value=" #{msgs['TICKET_ACTION.TEXT.ASSIGN.MANAGER_HELP']}" />
				<e:commandButton 
					id="chooseAnotherManagerButton" style="display: none"
					value="#{msgs['TICKET_ACTION.BUTTON.ASSIGN_TO_ANOTHER']}">
					<t:updateActionListener value="#{null}" property="#{ticketController.targetManager}" />
				</e:commandButton>
				<e:panelGrid columns="2" width="100%" columnClasses="colLeftMax,colRightNowrap">
					<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.ASSIGN.2']}" />
					<%@include file="_ticketActionResponses.jsp"%>
				</e:panelGrid>
				<fck:editor  
					id="actionMessage" 
					value="#{ticketController.actionMessage}" 
					toolbarSet="actionMessage" />
				<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightMaxNowrap" width="100%" >
					<h:panelGroup id="mainButtonGroup" style="position: absolute; white-space: nowrap;" >
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:actionButton');" >
							<e:bold value="#{msgs['TICKET_ACTION.BUTTON.ASSIGN']} " >
								<f:param value="#{userFormatter[ticketController.targetManager]}" />
							</e:bold>
							<t:graphicImage value="/media/images/assign.png" />
						</h:panelGroup>
						<e:commandButton 
							id="actionButton" style="display: none"
							value="#{msgs['TICKET_ACTION.BUTTON.ASSIGN']}"
							action="#{ticketController.doAssign}" >
							<f:param value="#{userFormatter[ticketController.targetManager]}" />
						</e:commandButton>
					</h:panelGroup>
					<h:panelGroup>
						<%@include file="_ticketActionScope.jsp"%>
                		                <h:panelGroup rendered="#{ticketController.userCanSetNoAlert}" >
		                                        <e:selectBooleanCheckbox id="noAlert"
                		                                value="#{ticketController.noAlert}" />
                                		        <e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
		                                </h:panelGroup>
						<%@include file="_ticketActionPreviewButton.jsp"%>
					</h:panelGroup>
				</e:panelGrid>
				<%@include file="_ticketActionPreview.jsp"%>
				<%@include file="_ticketActionHistory.jsp"%>
			</h:panelGroup>
		</h:panelGroup>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<%@include file="_ticketActionJavascript.jsp"%>
</e:page>

