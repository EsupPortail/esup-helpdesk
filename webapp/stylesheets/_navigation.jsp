<%@include file="_include.jsp"%>
<e:form 
	freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
	showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
	showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
	id="navigationForm"	styleClass="dashboard-menu" >
	<e:menu>
		<%@include file="_navigationItems.jsp"%>
		<h:panelGroup rendered="#{sessionController.currentUser !=null}" >
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('navigationForm:toggleMenuButton');" >
				<t:graphicImage value="/media/images/long-menu.png" rendered="#{sessionController.showShortMenu}"/>
				<t:graphicImage value="/media/images/short-menu.png" rendered="#{not sessionController.showShortMenu}"/>
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
