<%@include file="_include.jsp"%>
<e:form 
	freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
	showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
	showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
	id="navigationForm"	styleClass="dashboard-menu">
	<e:menu>
		<%@include file="_navigationItems.jsp"%>
		<%@include file="_navigationItems2.jsp"%>
	</e:menu>
		<h:panelGroup rendered="#{sessionController.currentUser !=null}" >
			<h:panelGroup style="cursor: pointer" styleClass="#{(controlPanelController.currentUserDepartmentManager) ? 'manager' : 'user'}" >

			    <t:htmlTag value="div" styleClass="moreItems" rendered="#{sessionController.showShortMenu}">
			    <t:htmlTag value="i" styleClass="fas fa-chevron-right fa-2x"/>
			     </t:htmlTag>

			    <t:htmlTag value="div" styleClass="moreItems" rendered="#{not sessionController.showShortMenu}">
			    <t:htmlTag value="i" styleClass="fas fa-chevron-left fa-2x"/>
			     </t:htmlTag>

			</h:panelGroup>
			<e:commandButton 
				id="toggleMenuButton" style="display: none"
				value="#{msgs[sessionController.showShortMenu ? 'NAVIGATION.BUTTON.SHOW_LONG_MENU' : 'NAVIGATION.BUTTON.SHOW_SHORT_MENU']}"
				action="#{sessionController.toggleShowShortMenu}" />
		</h:panelGroup>
</e:form>
