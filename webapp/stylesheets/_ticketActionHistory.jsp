<%@include file="_include.jsp"%>

<e:dataTable id="actionData" styleClass="history-container" width="100%"
	value="#{ticketController.historyEntries}" rowIndexVar="variable" 
	var="he" border="0" cellspacing="0" cellpadding="0">


	<t:column styleClass="col-main view--full">
        <t:htmlTag styleClass="action--header" value="div">
            		<h:outputText value="#{actionI18nTitleProvider[he.action]}" />
        </t:htmlTag>
        <t:htmlTag rendered="#{he.action.message != null && he.canView}" styleClass="action--content #{he.action.user == null and he.canView ? 'hideme' : ''}" value="div">
            <t:htmlTag styleClass="action-message" value="div">
                <h:outputText escape="false"  value="#{he.action.message}"/>
            </t:htmlTag>
            <h:panelGroup onclick="javascript:{quoteAction#{he.action.id}();}" rendered="#{he.quotedMessage != null}" styleClass="action--quote">
                 <t:htmlTag style="cursor:pointer" value="i" styleClass="far fa-copy fa-2x"/>
            </h:panelGroup>
        </t:htmlTag>
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
