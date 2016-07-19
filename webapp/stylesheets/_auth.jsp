<%@include file="_include.jsp"%>

<h:panelGroup rendered="#{sessionController.currentUser == null}">
	<h:panelGroup rendered="#{sessionController.state != 'createAccount' and sessionController.state != 'forgotPassword'}">
		<%@include file="_login.jsp"%>
	</h:panelGroup>
	<h:panelGroup rendered="#{sessionController.state == 'createAccount'}">
		<%@include file="_createAccount.jsp"%>
	</h:panelGroup>
	<h:panelGroup rendered="#{sessionController.state == 'forgotPassword'}">
		<%@include file="_forgotPassword.jsp"%>
	</h:panelGroup>
	<t:htmlTag value="br" />
	<%@include file="_lang.jsp"%>
</h:panelGroup>
