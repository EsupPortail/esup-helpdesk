<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="controlPanel"
	locale="#{sessionController.locale}">
	<%@include file="_navigation.jsp"%>

<script type="text/javascript">
function formatMessage() { 
  var num = arguments.length; 
  var oStr = arguments[0];   
  for (var i = 1; i < num; i++) { 
    var pattern = "\\{" + (i-1) + "\\}"; 
    var re = new RegExp(pattern, "g"); 
    oStr = oStr.replace(re, arguments[i]); 
  } 
  return oStr; 
} 
</script>

<h:outputText 
	value="<script type=&quot;text/javascript&quot;>function getRefreshMessage(delay) { if (delay <= 0) return '#{msgs['CONTROL_PANEL.TEXT.REFRESHING']}'; if (delay <= 60000) return formatMessage('#{msgs['CONTROL_PANEL.TEXT.REFRESH_IN_SECONDS']}',delay/1000); return formatMessage('#{msgs['CONTROL_PANEL.TEXT.REFRESH_IN_MINUTES']}',Math.round((delay+1)/60000));}</script>" 
	escape="false" />

<script type="text/javascript">
	function selectTicket(index) {
		simulateLinkClick('controlPanelForm:data:' + index + ':selectTicketButton');
	}
	function refreshWindow(delay) {
		var marker = document.getElementById('controlPanelForm:data:refreshDelay');
		if (!marker) {
			marker = document.getElementById('controlPanelForm:refreshDelay');
		}
		marker.innerHTML = getRefreshMessage(delay);
		if (delay <= 0) {
			simulateLinkClick('controlPanelForm:filterChangeButton');
		} else {
			setTimeout("refreshWindow("+(delay - 10000)+")", 10000);
		}
	}
</script>

	<h:panelGroup rendered="#{not controlPanelController.pageAuthorized}">
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="controlPanelForm"
		rendered="#{controlPanelController.pageAuthorized}">
		<e:panelGrid columns="3"
			columnClasses="colLeftNowrap,colLeftNowrap,colRightMax" width="100%"
			cellspacing="0" cellpadding="0">
			<h:panelGroup>
				<e:section value="#{msgs['CONTROL_PANEL.TITLE.SIMPLE']} "
					rendered="#{not controlPanelController.currentUserDepartmentManager}" />
				<h:panelGroup
					rendered="#{controlPanelController.currentUserDepartmentManager}">
					<e:section value="#{msgs['CONTROL_PANEL.TITLE.USER']} "
						rendered="#{controlPanelController.currentUser.controlPanelUserInterface}" />
					<e:section value="#{msgs['CONTROL_PANEL.TITLE.MANAGER']} "
						rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}" />
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup>
				<h:panelGroup
					rendered="#{controlPanelController.currentUserDepartmentManager}">
					<e:text value="&nbsp;" escape="false" />
					<h:panelGroup style="cursor: pointer"
						onclick="simulateLinkClick('controlPanelForm:toggleInterfaceButton');"
						rendered="#{controlPanelController.currentUserDepartmentManager}">
						<t:graphicImage value="/media/images/user.png" title="#{msgs['CONTROL_PANEL.TITLE.HELP']}" />
						<e:bold value="/" />
						<t:graphicImage value="/media/images/manager.png" title="#{msgs['CONTROL_PANEL.TITLE.HELP']}" />
					</h:panelGroup>
					<e:commandButton style="display: none"
						value="#{controlPanelController.currentUser.controlPanelUserInterface ? msgs['CONTROL_PANEL.BUTTON.SET_MANAGER_INTERFACE'] : msgs['CONTROL_PANEL.BUTTON.SET_USER_INTERFACE'] }"
						id="toggleInterfaceButton"
						action="#{controlPanelController.enter}">
						<t:updateActionListener
							value="#{not controlPanelController.currentUser.controlPanelUserInterface}"
							property="#{controlPanelController.currentUser.controlPanelUserInterface}" />
					</e:commandButton>
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer"
					onclick="simulateLinkClick('controlPanelForm:addTicketButton');">
					<e:bold value=" #{msgs['CONTROL_PANEL.BUTTON.ADD_TICKET']} " />
					<t:graphicImage value="/media/images/ticket.png" />
					<t:graphicImage value="/media/images/add.png" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="addTicketButton"
					action="#{ticketController.add}"
					value="#{msgs['CONTROL_PANEL.BUTTON.ADD_TICKET']}" />
				<h:panelGroup rendered="#{not empty controlPanelController.paginator.visibleItems}" >
					<h:panelGroup style="cursor: pointer"
						onclick="simulateLinkClick('controlPanelForm:markAllReadButton');">
						<e:bold value=" #{msgs['CONTROL_PANEL.BUTTON.MARK_ALL_READ']} " />
						<t:graphicImage value="/media/images/readTicket.png" />
					</h:panelGroup>
					<e:commandButton style="display: none"
						value="#{msgs['CONTROL_PANEL.BUTTON.MARK_ALL_READ']}" id="markAllReadButton"
						action="#{controlPanelController.markAllRead}" />
				</h:panelGroup>
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer"
						onclick="simulateLinkClick('controlPanelForm:filterChangeButton');">
						<e:bold value=" #{msgs['_.BUTTON.REFRESH']} " />
						<t:graphicImage value="/media/images/refresh.png"
							alt="#{msgs['_.BUTTON.REFRESH']}"
							title="#{msgs['_.BUTTON.REFRESH']}" />
					</h:panelGroup>
					<e:commandButton style="display: none"
						value="#{msgs['_.BUTTON.REFRESH']}" id="filterChangeButton"
						action="#{controlPanelController.enter}" />
				</h:panelGroup>
			</h:panelGroup>
		</e:panelGrid>
		<e:messages />
		<e:panelGrid columns="2" width="100%" columnClasses="colLeftMax,colRight">
			<e:panelGrid columns="4" columnClasses="colLeft,colLeftNowrap,colLeft,colLeft">
                <e:outputLabel for="controlPanelForm:userStatusFilter"
                    value="#{msgs['CONTROL_PANEL.STATUS_FILTER.PROMPT']}" 
                    rendered="#{controlPanelController.currentUser.controlPanelUserInterface}" />
                <e:outputLabel for="controlPanelForm:managerStatusFilter"
                    value="#{msgs['CONTROL_PANEL.STATUS_FILTER.PROMPT']}" 
                    rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}" />
				<e:outputLabel for="controlPanelForm:userDepartmentFilter"
					value="#{msgs['CONTROL_PANEL.DEPARTMENT_FILTER.PROMPT']}"
					rendered="#{controlPanelController.currentUser.controlPanelUserInterface}" />
				<e:outputLabel for="controlPanelForm:managerDepartmentFilter"
					value="#{msgs['CONTROL_PANEL.DEPARTMENT_FILTER.PROMPT']}"
					rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}" />
                <e:outputLabel for="controlPanelForm:userInvolvementFilter"
                    value="#{msgs['CONTROL_PANEL.INVOLVEMENT_FILTER.PROMPT']}" 
                    rendered="#{controlPanelController.currentUser.controlPanelUserInterface}" />
                <e:outputLabel for="controlPanelForm:managerInvolvementFilter"
                    value="#{msgs['CONTROL_PANEL.INVOLVEMENT_FILTER.PROMPT']}" 
                    rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}" />
				<h:panelGroup>
					<e:outputLabel for="controlPanelForm:managerManagerFilter"
						value="#{msgs['CONTROL_PANEL.MANAGER_FILTER.PROMPT']}" 
						rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}" />
				</h:panelGroup>
				<e:selectOneMenu id="userStatusFilter"
					value="#{controlPanelController.currentUser.controlPanelUserStatusFilter}"
					onchange="javascript:{simulateLinkClick('controlPanelForm:filterChangeButton');}"
					rendered="#{controlPanelController.currentUser.controlPanelUserInterface}">
					<f:selectItems value="#{controlPanelController.statusItems}" />
				</e:selectOneMenu>
				<e:selectOneMenu id="managerStatusFilter"
					value="#{controlPanelController.currentUser.controlPanelManagerStatusFilter}"
					onchange="javascript:{simulateLinkClick('controlPanelForm:filterChangeButton');}"
					rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}">
					<f:selectItems value="#{controlPanelController.statusItems}" />
				</e:selectOneMenu>
				<e:selectOneMenu id="userDepartmentFilter"
					value="#{controlPanelController.currentUser.controlPanelUserDepartmentFilter}"
					onchange="javascript:{simulateLinkClick('controlPanelForm:filterChangeButton');}"
					converter="#{departmentConverter}"
					rendered="#{controlPanelController.currentUser.controlPanelUserInterface}">
					<f:selectItems
						value="#{controlPanelController.userDepartmentItems}" />
				</e:selectOneMenu>
				<h:panelGroup rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}" >
					<e:selectOneMenu id="managerDepartmentFilter"
						value="#{controlPanelController.currentUser.controlPanelManagerDepartmentFilter}"
						onchange="javascript:{simulateLinkClick('controlPanelForm:filterChangeButton');}"
						converter="#{departmentConverter}" >
						<f:selectItems
							value="#{controlPanelController.managerDepartmentItems}" />
					</e:selectOneMenu>
					<e:selectOneMenu id="managerCategoryFilterSpec"
						value="#{controlPanelController.managerCategoryFilterSpec}"
						onchange="javascript:{simulateLinkClick('controlPanelForm:filterChangeButton');}" 
						rendered="#{not empty controlPanelController.managerCategorySpecItems}" >
						<f:selectItems
							value="#{controlPanelController.managerCategorySpecItems}" />
					</e:selectOneMenu>
				</h:panelGroup>
				<e:selectOneMenu id="userInvolvementFilter"
					value="#{controlPanelController.currentUser.controlPanelUserInvolvementFilter}"
					onchange="javascript:{simulateLinkClick('controlPanelForm:filterChangeButton');}"
					rendered="#{controlPanelController.currentUser.controlPanelUserInterface}">
					<f:selectItems
						value="#{controlPanelController.userInvolvementItems}" />
				</e:selectOneMenu>
                <e:selectOneMenu id="managerInvolvementFilter"
                    value="#{controlPanelController.currentUser.controlPanelManagerInvolvementFilter}"
                    onchange="javascript:{simulateLinkClick('controlPanelForm:filterChangeButton');}"
                    rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}">
					<f:selectItems
						value="#{controlPanelController.managerInvolvementItems}" />
				</e:selectOneMenu>
				<h:panelGroup>
					<e:selectOneMenu id="managerManagerFilter"
						value="#{controlPanelController.paginator.selectedManager}"
						onchange="javascript:{simulateLinkClick('controlPanelForm:filterChangeButton');}"
						converter="#{userConverter}" 
						rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}" >
						<f:selectItems value="#{controlPanelController.managerManagerItems}" />
					</e:selectOneMenu>
				</h:panelGroup>
			</e:panelGrid>
			<e:panelGrid columns="1" columnClasses="colLeftNowrap">
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer"
						onclick="simulateLinkClick('controlPanelForm:gotoTicketButton');">
						<t:graphicImage value="/media/images/search.png" />
						<e:bold value=" #{msgs['CONTROL_PANEL.BUTTON.GOTO_TICKET']}" />
					</h:panelGroup>
					<e:commandButton style="display: none" id="gotoTicketButton"
						action="#{controlPanelController.gotoTicket}"
						value="#{msgs['CONTROL_PANEL.BUTTON.GOTO_TICKET']}" >
						<t:updateActionListener value="controlPanel"
							property="#{ticketController.backPage}" />
					</e:commandButton>
				</h:panelGroup>
				<e:inputText id="ticketNumber" value="#{controlPanelController.ticketNumberString}" size="5" 
					onkeypress="if (event.keyCode == 13) { simulateLinkClick('controlPanelForm:gotoTicketButton'); return false; }" />
			</e:panelGrid>
		</e:panelGrid>

		<e:dataTable
			rowOnMouseOver="javascript:{if (#{cpe.canRead}) {previousClass = this.className; this.className = 'portlet-table-selected';}}"
			rowOnMouseOut="javascript:{if (#{cpe.canRead}) {this.className = previousClass;}}"
			id="data" rowIndexVar="variable"
			value="#{controlPanelController.paginator.visibleItems}" var="cpe"
			border="0" style="width:100%" cellspacing="0" cellpadding="0"
			rendered="#{not empty controlPanelController.paginator.visibleItems}"
			columnClasses="colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap"
			styleClass="dashboard-header">
			<f:facet name="header" >
				<h:panelGroup 
					rendered="#{not empty controlPanelController.paginator.visibleItems}">
					<h:panelGroup style="display: none"
						rendered="#{controlPanelController.paginator.lastPageNumber != 0}">
						<h:panelGroup
							rendered="#{not controlPanelController.paginator.firstPage}">
							<e:commandButton id="pageFirst"
								value="#{msgs['PAGINATION.BUTTON.FIRST']}"
								action="#{controlPanelController.paginator.gotoFirstPage}" />
							<e:commandButton id="pagePrevious" 
								value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
								action="#{controlPanelController.paginator.gotoPreviousPage}" />
						</h:panelGroup>
						<h:panelGroup
							rendered="#{not controlPanelController.paginator.lastPage}">
							<e:commandButton id="pageNext" 
								value="#{msgs['PAGINATION.BUTTON.NEXT']}"
								action="#{controlPanelController.paginator.gotoNextPage}" />
							<e:commandButton id="pageLast"
								value="#{msgs['PAGINATION.BUTTON.LAST']}"
								action="#{controlPanelController.paginator.gotoLastPage}" />
						</h:panelGroup>
					</h:panelGroup>
					<h:panelGrid columns="3" columnClasses="colLeft,,colRight"
						width="100%">
						<h:panelGroup>
							<e:text value="#{msgs['CONTROL_PANEL.TEXT.TICKETS']}">
								<f:param value="#{controlPanelController.paginator.firstVisibleNumber + 1}" />
								<f:param value="#{controlPanelController.paginator.lastVisibleNumber + 1}" />
								<f:param value="#{controlPanelController.paginator.totalItemsCount}" />
							</e:text>
						</h:panelGroup>
						<h:panelGroup>
							<%@include file="_controlPanelPages.jsp"%>
						</h:panelGroup>
						<h:panelGroup>
							<e:text value="#{msgs['CONTROL_PANEL.TEXT.TICKETS_PER_PAGE']} " />
							<e:selectOneMenu
								onchange="javascript:{simulateLinkClick('controlPanelForm:filterChangeButton');}"
								value="#{controlPanelController.currentUser.controlPanelPageSize}">
								<f:selectItems
									value="#{controlPanelController.paginator.pageSizeItems}" />
							</e:selectOneMenu>
						</h:panelGroup>
					</h:panelGrid>
					<t:htmlTag value="hr" />
				</h:panelGroup>
			</f:facet>
			<t:column 
                style="cursor: pointer; padding-left: 0em; padding-right: 0em"
                headerstyle="padding-left: 0em; padding-right: 0em" >
				<h:panelGroup rendered="#{not cpe.bookmarked}" >
					<t:graphicImage value="/media/images/bookmark-ticket.png" 
						onclick="javascript:{simulateLinkClick('controlPanelForm:data:#{variable}:bookmarkTicketButton');}" />
					<e:commandButton id="bookmarkTicketButton" style="display: none"
						value="#{msgs['CONTROL_PANEL.BUTTON.BOOKMARK']}"
						action="#{controlPanelController.bookmarkTicket}" immediate="true" >
						<t:updateActionListener value="#{cpe.ticket}"
							property="#{controlPanelController.ticketToBookmark}" />
					</e:commandButton>
				</h:panelGroup>
				<h:panelGroup rendered="#{cpe.bookmarked}" >
					<t:graphicImage value="/media/images/unbookmark-ticket.png" 
						onclick="javascript:{simulateLinkClick('controlPanelForm:data:#{variable}:unbookmarkTicketButton');}" />
					<e:commandButton id="unbookmarkTicketButton" style="display: none"
						value="#{msgs['CONTROL_PANEL.BUTTON.UNBOOKMARK']}"
						action="#{controlPanelController.unbookmarkTicket}" immediate="true" >
						<t:updateActionListener value="#{cpe.ticket}"
							property="#{controlPanelController.ticketToBookmark}" />
					</e:commandButton>
				</h:panelGroup>
				<h:panelGroup rendered="#{cpe.viewed}" >
					<t:graphicImage value="/media/images/readTicket.png" 
						onclick="javascript:{simulateLinkClick('controlPanelForm:data:#{variable}:markTicketUnreadButton');}" />
					<e:commandButton id="markTicketUnreadButton" style="display: none"
						value="#{msgs['CONTROL_PANEL.BUTTON.MARK_UNREAD']}"
						action="#{controlPanelController.markTicketUnread}" immediate="true" >
						<t:updateActionListener value="#{cpe.ticket}"
							property="#{controlPanelController.ticketToMarkReadUnread}" />
					</e:commandButton>
				</h:panelGroup>
				<h:panelGroup rendered="#{not cpe.viewed}" >
					<t:graphicImage value="/media/images/unreadTicket.png" 
						onclick="javascript:{simulateLinkClick('controlPanelForm:data:#{variable}:markTicketReadButton');}" />
					<e:commandButton id="markTicketReadButton" style="display: none"
						value="#{msgs['CONTROL_PANEL.BUTTON.MARK_READ']}"
						action="#{controlPanelController.markTicketRead}" immediate="true" >
						<t:updateActionListener value="#{cpe.ticket}"
							property="#{controlPanelController.ticketToMarkReadUnread}" />
					</e:commandButton>
				</h:panelGroup>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[0] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[0] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{0}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{0}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[1] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[1] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{1}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{1}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[2] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[2] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{2}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{2}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[3] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[3] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{3}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{3}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[4] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[4] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{4}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{4}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[5] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[5] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{5}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{5}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[6] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[6] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{6}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{6}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[7] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[7] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{7}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{7}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[8] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[8] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{8}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{8}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[9] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[9] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{9}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{9}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[10] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[10] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header" >
					<t:aliasBean alias="#{columnIndex}" value="#{10}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{10}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[11] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[11] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{11}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{11}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
            <t:column 
                style="cursor: pointer; #{controlPanelController.columnsOrderer[12] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
                headerstyle="#{controlPanelController.columnsOrderer[12] == null ? 'padding-left: 0em; padding-right: 0em' : ''}"
				onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
				<f:facet name="header">
					<t:aliasBean alias="#{columnIndex}" value="#{12}">
						<%@include file="_controlPanelColumnFacet.jsp"%>
					</t:aliasBean>
				</f:facet>
				<t:aliasBean alias="#{columnIndex}" value="#{12}">
					<%@include file="_controlPanelColumnContent.jsp"%>
				</t:aliasBean>
			</t:column>
			<t:column 
                style="cursor: pointer; padding-left: 0em; padding-right: 0em"
                headerstyle="padding-left: 0em; padding-right: 0em" >
				<e:commandButton value="#{msgs['_.BUTTON.VIEW_EDIT']}"
					id="selectTicketButton" style="display: none"
					action="#{controlPanelController.viewTicket}" immediate="true"
					rendered="#{cpe.canRead}">
					<t:updateActionListener value="#{cpe.ticket}"
						property="#{controlPanelController.ticketToView}" />
				</e:commandButton>
			</t:column>
			<f:facet name="footer">
				<h:panelGroup>
					<t:htmlTag value="hr" />
					<e:panelGrid columns="3" columnClasses="colLeft,,colRight" cellpadding="0"
						cellspacing="0" width="100%" rendered="#{not controlPanelController.editColumns}" >
						<h:panelGroup>
							<h:panelGroup style="cursor: pointer"
								onclick="simulateLinkClick('controlPanelForm:data:editColumnsButton');" >
								<t:graphicImage value="/media/images/edit-columns.png" />
								<e:bold value=" #{msgs['CONTROL_PANEL.BUTTON.EDIT_COLUMNS']}" />
							</h:panelGroup>
							<e:commandButton id="editColumnsButton" style="display: none"
								value="#{msgs['CONTROL_PANEL.BUTTON.EDIT_COLUMNS']}"
								action="#{controlPanelController.toggleEditColumns}" />
							<%@include file="_controlPanelSortColumnsButtons.jsp"%>
						</h:panelGroup>
						<h:panelGroup>
							<%@include file="_controlPanelPages.jsp"%>
						</h:panelGroup>
						<h:panelGroup >
							<e:italic id="refreshDelay" />
						</h:panelGroup>
					</e:panelGrid>
					<e:panelGrid columns="3" columnClasses="colLeft,,colRight" cellpadding="0"
						cellspacing="0" width="100%" rendered="#{controlPanelController.editColumns}" >
						<h:panelGroup>
							<h:panelGroup style="cursor: pointer"
								onclick="simulateLinkClick('controlPanelForm:data:editColumnsQuitButton');" >
								<t:graphicImage value="/media/images/edit-columns-quit.png" />
								<e:bold value=" #{msgs['CONTROL_PANEL.BUTTON.EDIT_COLUMNS_QUIT']}" />
							</h:panelGroup>
							<%@include file="_controlPanelEditColumnsButtons.jsp"%>
						</h:panelGroup>
						<h:panelGroup>
							<h:panelGroup 
								rendered="#{controlPanelController.editColumns}" >
								<h:panelGroup style="cursor: pointer"
									onclick="simulateLinkClick('controlPanelForm:data:resetColumnsButton');">
									<t:graphicImage value="/media/images/reset-columns.png"
										alt="#{msgs['CONTROL_PANEL.BUTTON.RESET_COLUMNS']}"
										title="#{msgs['CONTROL_PANEL.BUTTON.RESET_COLUMNS']}" />
									<e:bold value=" #{msgs['CONTROL_PANEL.BUTTON.RESET_COLUMNS']}" />
								</h:panelGroup>
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup>
							<h:panelGroup id="addColumnPanel" 
								rendered="#{not empty controlPanelController.addColumnItems}">
								<t:graphicImage value="/media/images/add-column.png" />
								<e:text value=" " />
								<e:selectOneMenu value="#{controlPanelController.columnToAdd}"
									onchange="simulateLinkClick('controlPanelForm:data:addColumnButton');">
									<f:selectItems value="#{controlPanelController.addColumnItems}" />
								</e:selectOneMenu>
							</h:panelGroup>
						</h:panelGroup>
					</e:panelGrid>
				</h:panelGroup>
			</f:facet>
		</e:dataTable>
		<e:panelGrid rendered="#{empty controlPanelController.paginator.visibleItems}" 
			columnClasses="colLeft,colRight" columns="2" width="100%" >
			<e:text value="#{msgs['CONTROL_PANEL.TEXT.NO_TICKET']}" />
			<e:italic id="refreshDelay" />
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{controlPanelController}">
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
	    var previousClass = null;
</script>

<h:outputText 
	rendered="#{not controlPanelController.currentUser.controlPanelUserInterface and not controlPanelController.editColumns and controlPanelController.currentUser.controlPanelRefreshDelay ge 1}" 
	value="<script type=&quot;text/javascript&quot;>refreshWindow(#{controlPanelController.currentUser.controlPanelRefreshDelay}*60000);</script>" 
	escape="false" />

</e:page>
