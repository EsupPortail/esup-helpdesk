<%@include file="_include.jsp"%>

<t:htmlTag value="hr" />
<e:subSection value="#{msgs['TICKET_ACTION.HISTORY.HEADER']} " >
	<f:param value="#{ticketController.ticket.label}"/>
</e:subSection>
<h:panelGroup >
	<e:italic value="#{msgs['TICKET_ACTION.HISTORY.QUOTE_HELP.1']} " />
	<t:graphicImage value="/media/images/quote.png" />
	<e:italic value="#{msgs['TICKET_ACTION.HISTORY.QUOTE_HELP.2']} " />
</h:panelGroup>
<e:dataTable columnClasses="colCenter,colLeft" id="actionData" width="100%" 
	value="#{ticketController.historyEntries}" rowIndexVar="variable" 
	var="he" border="0" cellspacing="0" cellpadding="0">
	<t:column>
		<t:graphicImage value="/media/images/public.png" rendered="#{he.action.scope == 'DEFAULT'}" />
		<t:graphicImage value="/media/images/protected.png" rendered="#{he.action.scope == 'OWNER'}" />
		<t:graphicImage value="/media/images/private.png" rendered="#{he.action.scope == 'MANAGER'}" />
		<h:panelGroup rendered="#{he.quotedMessage != null}">
			<t:htmlTag value="br" />
			<t:graphicImage value="/media/images/quote.png" style="cursor: pointer" onclick="javascript:{quoteAction#{he.action.id}();}" />
		</h:panelGroup>
	</t:column>
	<t:column>
		<e:bold value="#{actionI18nTitleProvider[he.action]}" />
		<h:panelGroup rendered="#{he.action.message != null}" >
			<t:htmlTag value="br" />
			<e:text 
				escape="false"
				rendered="#{he.canView}"
				value="#{he.action.message}" />
		</h:panelGroup>
	</t:column>
</e:dataTable>				

<h:outputText value="<script type=&quot;text/javascript&quot;>" escape="false" />
<t:dataList 
	id="quoteData"  
	value="#{ticketController.historyEntries}" 
	rowIndexVar="variable" 
	var="he" >
	<h:outputText value="function quoteAction#{he.action.id}() {" escape="false" />
	<h:outputText value="insertTextIntoEditor(&quot;<br />\n" escape="false" />
	<h:outputFormat value="#{msgs['TICKET_ACTION.QUOTE']}<br />\n" escape="false" rendered="#{he.action.user != null}">
		<f:param value="#{userFormatter[he.action.user]}" />
	</h:outputFormat>
	<h:outputText value="#{he.quotedMessage}\n&quot;); return false; }" escape="false" />

</t:dataList>
<h:outputText value="</script>" escape="false" />
