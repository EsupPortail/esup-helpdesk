<%@include file="_include.jsp"%>

<t:column style="cursor: pointer" 
	onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}" >
	<f:facet name="header">
		<t:aliasBean alias="#{columnName}" value="#{controlPanelController.columnsOrderer[columnIndex]}" >
			<%@include file="_controlPanelColumnFacet.jsp"%>
		</t:aliasBean>
	</f:facet>
	<t:aliasBean alias="#{columnName}" value="#{controlPanelController.columnsOrderer[columnIndex]}" >
		<%@include file="_controlPanelColumnContent.jsp"%>
	</t:aliasBean>
	<f:facet name="footer">
		<t:aliasBean alias="#{columnName}" value="#{controlPanelController.columnsOrderer[columnIndex]}" >
			<%@include file="_controlPanelColumnFacet.jsp"%>
		</t:aliasBean>
	</f:facet>
</t:column>
