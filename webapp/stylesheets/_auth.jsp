<%@include file="_include.jsp"%>
<t:htmlTag value="div" styleClass="region-inner" rendered="#{sessionController.currentUser == null}">

	<h:panelGroup rendered="#{sessionController.state != 'createAccount' and sessionController.state != 'forgotPassword'}">
		<%@include file="_login.jsp"%>
	</h:panelGroup>
	<h:panelGroup rendered="#{sessionController.state == 'createAccount'}">
		<%@include file="_createAccount.jsp"%>
	</h:panelGroup>
	<h:panelGroup rendered="#{sessionController.state == 'forgotPassword'}">
		<%@include file="_forgotPassword.jsp"%>
	</h:panelGroup>

 </t:htmlTag>
