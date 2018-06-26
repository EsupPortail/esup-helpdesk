<%@include file="_include.jsp"%>


	<t:htmlTag value="div" styleClass="form-item" rendered="#{ticketController.actionScopeItems != null}" >
		<e:outputLabel for="scope" value="#{msgs['TICKET_ACTION.TEXT.SCOPE_PROMPT']} " />
		<e:selectOneMenu id="scope" 
			value="#{ticketController.actionScope}" >
			<f:selectItems value="#{ticketController.actionScopeItems}" />
		</e:selectOneMenu>
	</t:htmlTag>

