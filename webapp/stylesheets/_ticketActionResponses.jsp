<%@include file="_include.jsp"%>
<t:htmlTag value="div" styleClass="form-block" rendered="#{ticketController.userCanUseCannedResponses and not empty ticketController.responseItems}">
		<t:htmlTag value="div" styleClass="form-select scrollable" id="responses" >
			<e:selectOneMenu id="response" onchange="javascript:{insertResponse();}" >
				<f:selectItems
					value="#{ticketController.responseItems}" />
			</e:selectOneMenu>
		</t:htmlTag>

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

</t:htmlTag>
