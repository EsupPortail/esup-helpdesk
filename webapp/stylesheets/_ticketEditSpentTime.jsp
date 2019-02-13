<%@include file="_include.jsp"%>

<e:selectOneMenu id="ticketSpentTimeDays"
	value="#{ticketController.ticketSpentTimeDays}" 
	>
	<f:selectItems value="#{ticketController.spentTimeDayItems}" />
</e:selectOneMenu>
<e:text escape="false" value="&nbsp;" />
<e:selectOneMenu id="ticketSpentTimeHours"
	value="#{ticketController.ticketSpentTimeHours}" 
	>
	<f:selectItems value="#{ticketController.spentTimeHourItems}" />
</e:selectOneMenu>
<e:text escape="false" value="&nbsp;" />
<e:selectOneMenu id="ticketSpentTimeMinutes"
	value="#{ticketController.ticketSpentTimeMinutes}" 
	>
	<f:selectItems value="#{ticketController.spentTimeMinuteItems}" />
</e:selectOneMenu>
<e:text escape="false" value="&nbsp;" />
