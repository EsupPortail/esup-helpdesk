<%@include file="_include.jsp"%>
<h:panelGroup>
	<h:panelGroup rendered="#{ticketController.userCanUseCannedResponses and not empty ticketController.responseItems}" >
		<h:panelGroup id="showResponses" style="cursor: pointer" 
			onclick="showElement('ticketActionForm:responses');hideElement('ticketActionForm:showResponses');" >
			<e:bold value="#{msgs['TICKET_ACTION.TEXT.RESPONSES']} " />
			<t:graphicImage value="/media/images/edit.png" />
		</h:panelGroup>
		<h:panelGroup id="responses" style="display: none" >
			<e:selectOneMenu id="response" onchange="javascript:{insertResponse();hideElement('ticketActionForm:responses');showElement('ticketActionForm:showResponses');}" >
				<f:selectItems
					value="#{ticketController.responseItems}" />
			</e:selectOneMenu>
		</h:panelGroup>
		<h:outputText value="<script type=&quot;text/javascript&quot;>" escape="false" />
		<t:dataList 
			id="responseData"  
			value="#{ticketController.responseEntries}" 
			rowIndexVar="variable" 
			var="re" >
			<h:outputText value="function insertResponse#{re.response.id}() {" escape="false" />
			<h:outputText value="insertTextIntoEditor(&quot;<br />\n#{re.formattedMessage}\n&quot;); return false; }" escape="false" />
		</t:dataList>
		<h:outputText value="</script>" escape="false" />
	</h:panelGroup>
</h:panelGroup>
