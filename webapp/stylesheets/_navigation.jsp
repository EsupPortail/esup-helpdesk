<%@include file="_include.jsp"%>
<e:form 
	freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
	showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
	showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
	id="navigationForm"	styleClass="dashboard-menu">
	<e:menu>
		<%@include file="_navigationItems.jsp"%>
		<h:panelGroup rendered="#{sessionController.currentUser !=null}" >
			<h:panelGroup style="cursor: pointer" onclick="buttonClick('navigationForm:toggleMenuButton');" >

			    <t:htmlTag value="div" styleClass="moreItems" rendered="#{sessionController.showShortMenu}">
			    <t:htmlTag value="i" styleClass="fas fa-chevron-down"/>
			     </t:htmlTag>

			    <t:htmlTag value="div" styleClass="moreItems" rendered="#{not sessionController.showShortMenu}">
			    <t:htmlTag value="i" styleClass="fas fa-chevron-up"/>
			     </t:htmlTag>

			</h:panelGroup>
			<e:commandButton 
				id="toggleMenuButton" style="display: none"
				value="#{msgs[sessionController.showShortMenu ? 'NAVIGATION.BUTTON.SHOW_LONG_MENU' : 'NAVIGATION.BUTTON.SHOW_SHORT_MENU']}"
				action="#{sessionController.toggleShowShortMenu}" />
		</h:panelGroup>
	</e:menu>
	<e:menu rendered="#{not sessionController.showShortMenu}" >
		<%@include file="_navigationItems2.jsp"%>
	</e:menu>
</e:form>
