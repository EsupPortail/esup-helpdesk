<%@include file="_include.jsp"%>

 <t:htmlTag value="div" styleClass="form-item  display-flex">
    <e:selectOneMenu id="ticketSpentTimeDays"
        value="#{ticketController.ticketSpentTimeDays}">
        <f:selectItems value="#{ticketController.spentTimeDayItems}" />
    </e:selectOneMenu>

    <e:selectOneMenu id="ticketSpentTimeHours" value="#{ticketController.ticketSpentTimeHours}">
        <f:selectItems value="#{ticketController.spentTimeHourItems}" />
    </e:selectOneMenu>

    <e:selectOneMenu id="ticketSpentTimeMinutes" value="#{ticketController.ticketSpentTimeMinutes}">
        <f:selectItems value="#{ticketController.spentTimeMinuteItems}" />
    </e:selectOneMenu>
</t:htmlTag>
