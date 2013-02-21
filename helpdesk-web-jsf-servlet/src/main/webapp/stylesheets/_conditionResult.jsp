<%@include file="_include.jsp"%>
<t:graphicImage value="/media/images/condition-result.png" 
	rendered="#{node.evalResult == null or departmentSelectionController.advanced}" />
<h:panelGroup rendered="#{node.evalResult != null and not departmentSelectionController.advanced}" >
	<t:graphicImage value="/media/images/condition-result-true.png" rendered="#{node.evalResult}" />
	<t:graphicImage value="/media/images/condition-result-false.png" rendered="#{not node.evalResult}" />
</h:panelGroup>
