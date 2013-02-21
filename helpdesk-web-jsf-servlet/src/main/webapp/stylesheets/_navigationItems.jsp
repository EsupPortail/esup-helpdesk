<%@include file="_include.jsp"%>
<e:menuItem id="welcome" value="#{msgs['NAVIGATION.TEXT.WELCOME']}"
	action="#{welcomeController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.WELCOME']}" />
<e:menuItem id="faqs" value="#{msgs['NAVIGATION.TEXT.FAQS']}"
	action="#{faqsController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.FAQS']}"
	rendered="#{faqsController.pageAuthorized}" />
<e:menuItem id="controlPanel" value="#{msgs['NAVIGATION.TEXT.CONTROL_PANEL']}"
	action="#{controlPanelController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.CONTROL_PANEL']}"
	rendered="#{controlPanelController.pageAuthorized}" />
<e:menuItem id="search" value="#{msgs['NAVIGATION.TEXT.SEARCH']}"
	action="#{searchController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.SEARCH']}"
	rendered="#{searchController.pageAuthorized}" />
<e:menuItem id="bookmarks"
	value="#{msgs['NAVIGATION.TEXT.BOOKMARKS']}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.BOOKMARKS']}"
	action="#{bookmarksController.enter}"
	rendered="#{bookmarksController.pageAuthorized}" />
<e:menuItem id="preferences"
	value="#{msgs['NAVIGATION.TEXT.PREFERENCES']}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.PREFERENCES']}"
	action="#{preferencesController.enter}"
	rendered="#{preferencesController.pageAuthorized and not sessionController.showShortMenu}" />
<e:menuItem id="about" value="#{msgs['NAVIGATION.TEXT.ABOUT']}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.ABOUT']}"
	action="#{aboutController.enter}" 
	rendered="#{sessionController.currentUser == null or not sessionController.showShortMenu}" />
<e:menuItem id="logout" action="#{sessionController.logout}"
	value="#{msgs['NAVIGATION.TEXT.LOGOUT']}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.LOGOUT']}"
	rendered="#{sessionController.printLogout}" />