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
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ticketActionForm" enctype="multipart/form-data" >
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['TICKET_ACTION.TITLE.MOVE']}" >
				<f:param value="#{ticketController.ticket.id}" />
				<f:param value="#{ticketController.ticket.label}" />
			</e:section>
			<%@include file="_ticketActionCancel.jsp"%>
		</e:panelGrid>
		<e:messages />
		<h:panelGroup
			rendered="#{ticketController.moveTargetCategory == null}">
			<h:panelGroup
				rendered="#{ticketController.moveTree == null}">
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.MOVE.NO_TARGET']}" />
			</h:panelGroup>
			<h:panelGroup
				rendered="#{ticketController.moveTree != null}">
				<h:panelGroup rendered="#{not empty ticketController.recentMoveItems}" >
					<e:bold value=" #{msgs['TICKET_ACTION.TEXT.MOVE.RECENT']} " />
					<e:selectOneMenu value="#{ticketController.moveTargetCategory}" onchange="submit();" 
						converter="#{categoryConverter}" >
						<f:selectItems value="#{ticketController.recentMoveItems}" />
					</e:selectOneMenu>
					<t:htmlTag value="br" />
				</h:panelGroup>
				<t:tree2 id="tree" value="#{ticketController.moveTree}"
					var="node" varNodeToggler="t" clientSideToggle="true"
					showRootNode="true" >
					<f:facet name="root">
						<h:panelGroup >
							<t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
							<t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
							<e:italic value=" #{msgs['TICKET_ACTION.TEXT.MOVE.ROOT_LABEL']}" />
						</h:panelGroup>
					</f:facet>
					<f:facet name="department">
						<h:panelGroup>
							<t:graphicImage value="#{departmentIconUrlProvider[node.department]}" />
							<e:text value=" #{msgs['TICKET_ACTION.TEXT.MOVE.DEPARTMENT_LABEL']}">
								<f:param value="#{node.department.xlabel}" />
							</e:text>
						</h:panelGroup>
					</f:facet>
					<f:facet name="category">
						<h:panelGroup>
							<t:graphicImage value="#{categoryIconUrlProvider[node.category]}" />
							<h:panelGroup rendered="#{node.category.addNewTickets or node.leaf}" >
								<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:tree:#{node.identifier}:#{node.category.addNewTickets or node.leaf ? 'chooseCategoryButton' : 't2'}');" >
									<e:bold value=" #{msgs['TICKET_ACTION.TEXT.MOVE.CATEGORY_LABEL']}" >
										<f:param value="#{node.category.xlabel}" />
									</e:bold>
								</h:panelGroup>
								<e:commandButton 
									id="chooseCategoryButton" style="display: none"
									value="->" action="continue">
									<t:updateActionListener value="#{node.category}"
										property="#{ticketController.moveTargetCategory}" />
									<t:updateActionListener value="#{node.department}"
										property="#{ticketController.moveTargetDepartment}" />										
								</e:commandButton>
							</h:panelGroup>
							<e:italic value=" #{msgs['TICKET_ACTION.TEXT.MOVE.CATEGORY_LABEL']}" 
								rendered="#{!node.category.addNewTickets or !node.leaf}" >
								<f:param value="#{node.category.xlabel}" />
							</e:italic>
						</h:panelGroup>
					</f:facet>
				</t:tree2>
			</h:panelGroup>
		</h:panelGroup>
		<h:panelGroup rendered="#{ticketController.moveTargetCategory != null}" >
			<e:panelGrid columns="2" columnClasses="colLeft,colLeft" >
				<e:text value="#{msgs['TICKET_ACTION.TEXT.MOVE.TARGET_CATEGORY_PROMPT']}" />
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:chooseAnotherCategoryButton');">
						<t:graphicImage value="#{categoryIconUrlProvider[ticketController.moveTargetCategory]}" />
						<e:bold value=" #{msgs['TICKET_ACTION.TEXT.MOVE.TARGET_CATEGORY_VALUE']}" title="#{ticketController.categoryMoveMembers}"  rendered="#{departmentsController.currentUserIsManager }">
							<f:param value="#{ticketController.moveTargetCategory.department.label}" />
							<f:param value="#{ticketController.moveTargetCategory.label}" />
						</e:bold>
						<e:bold value=" #{msgs['TICKET_ACTION.TEXT.MOVE.TARGET_CATEGORY_VALUE']}" rendered="#{!departmentsController.currentUserIsManager }">
							<f:param value="#{ticketController.moveTargetCategory.department.label}" />
							<f:param value="#{ticketController.moveTargetCategory.label}" />
						</e:bold>
						<e:italic value=" #{msgs['TICKET_ACTION.TEXT.MOVE.TARGET_CATEGORY_HELP']}" />
					</h:panelGroup>
					<e:commandButton 
						id="chooseAnotherCategoryButton" style="display: none"
						value="#{msgs['TICKET_ACTION.BUTTON.CHANGE_TARGET_CATEGORY']}"
						action="#{ticketController.resetMoveTargetCategory}" >
						<t:updateActionListener value="#{null}" property="#{ticketController.targetManager}" />
					</e:commandButton>
				</h:panelGroup>
			</e:panelGrid>
			<h:panelGroup
				rendered="#{not ticketController.looseTicketManagement}" >
				<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colLeftMax" 
					rendered="#{ticketController.ticket.manager == sessionController.currentUser}" >
					<e:selectBooleanCheckbox 
						value="#{ticketController.freeTicket}" />
					<e:text value=" #{msgs['TICKET_ACTION.TEXT.MOVE.FREE_TICKET']} " />
				</e:panelGrid>
			</h:panelGroup>
			<h:panelGroup
				rendered="#{ticketController.looseTicketManagement}" >
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.MOVE.LOOSE_TICKET_MANAGEMENT.TOP']}" >
					<f:param value="#{ticketController.moveTargetCategory.department.label}" />
				</e:paragraph>
				<e:panelGrid columns="2" columnClasses="colLeft,colLeft" >
					<e:selectBooleanCheckbox 
						value="#{ticketController.looseTicketManagementMonitor}" />
					<e:text value=" #{msgs['TICKET_ACTION.TEXT.MOVE.LOOSE_TICKET_MANAGEMENT.MONITOR']} " />
					<e:selectBooleanCheckbox 
						value="#{ticketController.looseTicketManagementInvite}" />
					<e:text value=" #{msgs['TICKET_ACTION.TEXT.MOVE.LOOSE_TICKET_MANAGEMENT.INVITE']} " />
				</e:panelGrid>
			</h:panelGroup>
			<e:panelGrid columns="3" width="100%" columnClasses="colLeftMax,colRightNowrap,colRightNowrap">
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.MOVE.TOP']}" />
				<%@include file="_ticketActionResponses.jsp"%>
				<%@include file="_ticketActionUpload.jsp"%>
			</e:panelGrid>
			<fck:editor  
				id="actionMessage" 
				value="#{ticketController.actionMessage}" 
				toolbarSet="actionMessage" />
			<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightMaxNowrap" width="100%" >
				<h:panelGroup id="mainButtonGroup" style="position: absolute; white-space: nowrap;" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:actionButton');" >
						<e:bold value="#{msgs['TICKET_ACTION.BUTTON.MOVE']} " >
							<f:param value="#{ticketController.moveTargetCategory.department.label}" />
							<f:param value="#{ticketController.moveTargetCategory.label}" />
						</e:bold>
						<t:graphicImage value="/media/images/move.png" />
					</h:panelGroup>
					<e:commandButton 
						id="actionButton" style="display: none"
						value="#{msgs['TICKET_ACTION.BUTTON.MOVE']}"
						action="#{ticketController.doMove}" >
						<f:param value="#{ticketController.moveTargetCategory.department.label}" />
						<f:param value="#{ticketController.moveTargetCategory.label}" />
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
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<%@include file="_ticketActionJavascript.jsp"%>
</e:page>

