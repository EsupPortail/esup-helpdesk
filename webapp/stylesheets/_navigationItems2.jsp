<%@include file="_include.jsp"%>
<e:menuItem id="journal" value="#{msgs['NAVIGATION.TEXT.JOURNAL']}"
	action="#{journalController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.JOURNAL']}"
	rendered="#{journalController.pageAuthorized}" />
<e:menuItem id="responses" value="#{msgs['NAVIGATION.TEXT.RESPONSES']}"
	action="#{responsesController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.RESPONSES']}"
	rendered="#{responsesController.pageAuthorized}" />
<e:menuItem id="utils" value="#{msgs['NAVIGATION.TEXT.UTILS']}"
	action="#{utilsController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.UTILS']}"
	rendered="#{utilsController.pageAuthorized}" />
<e:menuItem id="administrators"
	value="#{msgs['NAVIGATION.TEXT.ADMINISTRATION']}"
	action="#{administratorsController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.ADMINISTRATION']}"
	rendered="#{administratorsController.pageAuthorized}" />
<e:menuItem id="departments"
	value="#{msgs['NAVIGATION.TEXT.DEPARTMENTS']}"
	action="#{departmentsController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.DEPARTMENTS']}"
	rendered="#{departmentsController.pageAuthorized}" />
<e:menuItem id="departmentSelection"
	value="#{msgs['NAVIGATION.TEXT.DEPARTMENT_SELECTION']}"
	action="#{departmentSelectionController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.DEPARTMENT_SELECTION']}"
	rendered="#{departmentSelectionController.pageAuthorized}" />
<e:menuItem id="statistics"
	value="#{msgs['NAVIGATION.TEXT.STATISTICS']}"
	action="#{statisticsController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.STATISTICS']}"
	rendered="#{statisticsController.pageAuthorized}" />
	