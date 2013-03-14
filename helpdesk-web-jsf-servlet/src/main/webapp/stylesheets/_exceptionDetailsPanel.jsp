<%@include file="_commons-include.jsp"%>
<h:panelGroup id="exceptionDetails" rendered="#{printDetails}" >
	<t:htmlTag value="br" />		
	<%@include file="_exceptionDetails.jsp"%>
</h:panelGroup>

<script>
function hideExceptionDetails() {
	hideElement('hideExceptionDetails');
	showElement('showExceptionDetails');
	hideElement('exceptionDetails');
}
function showExceptionDetails() {
	showElement('hideExceptionDetails');
	hideElement('showExceptionDetails');
	showElement('exceptionDetails');
}
hideExceptionDetails();
</script>
