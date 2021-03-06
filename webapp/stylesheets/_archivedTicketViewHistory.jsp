<%@include file="_include.jsp"%>

<e:dataTable columnClasses="colLeft" id="actionData" width="100%" 
	value="#{archivedTicketController.archivedHistoryEntries}" rowIndexVar="variable" 
	var="ahe" border="0" cellspacing="0" cellpadding="0">
	<t:column>
		<t:aliasBean alias="#{archivedAction}" value="#{ahe.archivedAction}" >
			<%@include file="_archivedActionTitle.jsp"%>
		</t:aliasBean>
		<h:panelGroup rendered="#{ahe.archivedAction.message != null}" >
			<t:htmlTag value="br" />
			<t:div
				styleClass="#{ahe.styleClass}" >
				<e:text 
					escape="false"
					rendered="#{ahe.canView}"
					value="#{ahe.archivedAction.message}" />
			</t:div>
		</h:panelGroup>
	</t:column>
</e:dataTable>				
