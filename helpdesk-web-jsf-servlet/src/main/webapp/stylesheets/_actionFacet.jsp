<%@include file="_include.jsp"%>
<f:facet name="addAll">
	<h:panelGroup>
		<h:panelGroup rendered="#{node.evalResult == null or departmentSelectionController.advanced}">
			<t:graphicImage value="/media/images/action-add.png" />
			<%@include file="_actionFor.jsp"%>
			<e:italic value=" #{msgs['DEPARTMENT_SELECTION.ACTION.ADD_ALL']}" />
		</h:panelGroup>
		<h:panelGroup rendered="#{node.evalResult != null and not departmentSelectionController.advanced}" >
			<t:graphicImage value="/media/images/action-added.png" />
			<%@include file="_actionFor.jsp"%>
			<e:text value=" #{msgs['DEPARTMENT_SELECTION.ACTION.ADD_ALL']}" />
			<%@include file="_actionResult.jsp"%>
		</h:panelGroup>
	</h:panelGroup>
</f:facet>
<f:facet name="addByFilter">
	<h:panelGroup>
		<h:panelGroup rendered="#{node.evalResult == null or departmentSelectionController.advanced}">
			<t:graphicImage value="/media/images/action-add.png" />
			<%@include file="_actionFor.jsp"%>
			<e:italic value=" #{msgs['DEPARTMENT_SELECTION.ACTION.ADD_BY_FILTER']}" >
				<f:param value="#{node.action.filter == null ? '' : node.action.filter}" />
			</e:italic>
		</h:panelGroup>
		<h:panelGroup rendered="#{node.evalResult != null and not departmentSelectionController.advanced}" >
			<t:graphicImage value="/media/images/action-added.png" />
			<%@include file="_actionFor.jsp"%>
			<e:text value=" #{msgs['DEPARTMENT_SELECTION.ACTION.ADD_BY_FILTER']}" >
				<f:param value="#{node.action.filter}" />
			</e:text>
			<%@include file="_actionResult.jsp"%>
		</h:panelGroup>
	</h:panelGroup>
</f:facet>
<f:facet name="addByLabel">
	<h:panelGroup>
		<h:panelGroup rendered="#{node.evalResult == null or departmentSelectionController.advanced}">
			<t:graphicImage value="/media/images/action-add.png" />
			<%@include file="_actionFor.jsp"%>
			<e:italic value=" #{msgs['DEPARTMENT_SELECTION.ACTION.ADD_BY_LABEL']}" >
				<f:param value="#{node.action.label}" />
			</e:italic>
		</h:panelGroup>
		<h:panelGroup rendered="#{node.evalResult != null and not departmentSelectionController.advanced}" >
			<t:graphicImage value="/media/images/action-added.png" />
			<%@include file="_actionFor.jsp"%>
			<e:text value=" #{msgs['DEPARTMENT_SELECTION.ACTION.ADD_BY_LABEL']}" >
				<f:param value="#{node.action.label}" />
			</e:text>
			<%@include file="_actionResult.jsp"%>
		</h:panelGroup>
	</h:panelGroup>
</f:facet>
<f:facet name="stop">
	<h:panelGroup>
		<h:panelGroup rendered="#{node.evalResult == null or departmentSelectionController.advanced}">
			<t:graphicImage value="/media/images/action-stop.png" />
			<%@include file="_actionFor.jsp"%>
			<e:text value=" #{msgs['DEPARTMENT_SELECTION.ACTION.STOP']}" />
		</h:panelGroup>
		<h:panelGroup rendered="#{node.evalResult != null and not departmentSelectionController.advanced}" >
			<t:graphicImage value="/media/images/action-stopped.png" rendered="#{node.evalResult != null}" />
			<%@include file="_actionFor.jsp"%>
			<e:bold value=" #{msgs['DEPARTMENT_SELECTION.ACTION.STOP']}" />
		</h:panelGroup>
	</h:panelGroup>
</f:facet>
<f:facet name="doNothing">
	<h:panelGroup>
		<t:graphicImage value="/media/images/action-do-nothing.png" />
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.ACTION.DO_NOTHING']}" />
	</h:panelGroup>
</f:facet>
