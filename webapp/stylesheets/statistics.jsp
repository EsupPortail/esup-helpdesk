<%@include file="_include.jsp"%>
<%@ taglib uri="http://sourceforge.net/projects/jsf-comp" prefix="c" %>
<e:page stringsVar="msgs" menuItem="statistics" locale="#{sessionController.locale}">
    <t:htmlTag id="statistics" value="div" styleClass="page-wrapper statistics">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
               </t:htmlTag>
               <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>
                    <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                        <t:htmlTag value="div" styleClass="content-inner">
                            <h:panelGroup rendered="#{statisticsController.currentUser == null}" >
                                <%@include file="_auth.jsp"%>
                            </h:panelGroup>
                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="statisticsForm" rendered="#{statisticsController.pageAuthorized}" >
                                <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['STATISTICS.TITLE']}"/>
                                                 </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                      </t:htmlTag>
                                <t:htmlTag value="div" styleClass="message">
                                        <e:messages/>
                                </t:htmlTag>
                                <e:panelGrid columns="9" >
                                    <e:outputLabel for="statistics" value="#{msgs['STATISTICS.STATISTICS_FILTER.PROMPT']}" />
                                    <h:panelGroup>
                                        <e:outputLabel
                                            for="creationDepartments" value="#{msgs['STATISTICS.DEPARTMENT_FILTER.PROMPT']}"
                                            rendered="#{statisticsController.statistics != 0}" />
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:outputLabel
                                            for="origins" value="#{msgs['STATISTICS.ORIGIN_FILTER.PROMPT']}"
                                            rendered="#{statisticsController.statistics == 1
                                                    or statisticsController.statistics == 2}" />
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:outputLabel
                                            for="creationSort" value="#{msgs['STATISTICS.SORT_FILTER.PROMPT']}"
                                            rendered="#{
                                                (
                                                    (statisticsController.statistics == 1 or statisticsController.statistics == 2)
                                                    and
                                                    not empty statisticsController.creationSortItems
                                                ) or (
                                                    statisticsController.statistics == 10
                                                    and
                                                    not empty statisticsController.statusSortItems
                                                )}" />
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:outputLabel
                                            for="creationPeriod" value="#{msgs['STATISTICS.PERIOD_FILTER.PROMPT']}"
                                            rendered="#{statisticsController.statistics != 0}" />
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:outputLabel
                                            for="ticketsCreationHistoryInterval" value="#{msgs['STATISTICS.INTERVAL_FILTER.PROMPT']}"
                                            rendered="#{(statisticsController.statistics == 1
                                                    or statisticsController.statistics == 4
                                                    or statisticsController.statistics == 7
                                                    )
                                                    and statisticsController.ticketCreationsPeriodSpec != 'w'
                                                    and statisticsController.ticketCreationsPeriodSpec != 'm'
                                                    and statisticsController.ticketCreationsPeriodSpec != 'y'}" />
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:outputLabel
                                            for="ticketsCreationDistribution" value="#{msgs['STATISTICS.DISTRIBUTION_FILTER.PROMPT']}"
                                            rendered="#{statisticsController.statistics == 2 or statisticsController.statistics == 5}" />
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:outputLabel
                                            for="creatorsMaxNumber" value="#{msgs['STATISTICS.TICKET_CREATORS_MAX_NUMBER_FILTER.PROMPT']}"
                                            rendered="#{statisticsController.statistics == 3}" />
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:outputLabel
                                            for="creatorsMaxNumber" value="#{msgs['STATISTICS.TICKETS_MAX_NUMBER_FILTER.PROMPT']}"
                                            rendered="#{statisticsController.statistics == 6
                                                or statisticsController.statistics == 9}" />
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:selectOneMenu id="statistics" value="#{statisticsController.statistics}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" >
                                            <f:selectItems value="#{statisticsController.statisticsItems}" />
                                        </e:selectOneMenu>
                                        <e:commandButton style="display: none"
                                            value="#{msgs['_.BUTTON.UPDATE']}"
                                            id="changeStatisticsButton"
                                            action="#{statisticsController.computeStatistics}" />
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <h:selectManyListbox size="6"
                                            rendered="#{statisticsController.statistics != 0}"
                                            id="creationDepartments" value="#{statisticsController.creationDepartments}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');"
                                            converter="#{departmentConverter}"
                                            >
                                            <f:selectItems value="#{statisticsController.creationDepartmentItems}" />
                                        </h:selectManyListbox>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <h:selectManyListbox size="6"
                                            rendered="#{statisticsController.statistics == 1
                                                    or statisticsController.statistics == 2}"
                                            id="origins" value="#{statisticsController.origins}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" >
                                            <f:selectItems value="#{statisticsController.originItems}" />
                                        </h:selectManyListbox>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:selectOneMenu
                                            rendered="#{(statisticsController.statistics == 1
                                                    or statisticsController.statistics == 2)
                                                    and statisticsController.creationSortItems != null}"
                                            id="creationSort" value="#{statisticsController.ticketCreationsSort}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" >
                                            <f:selectItems value="#{statisticsController.creationSortItems}" />
                                        </e:selectOneMenu>
                                        <e:selectOneMenu
                                            rendered="#{statisticsController.statistics == 10
                                                    and statisticsController.statusSortItems != null}"
                                            id="statusSort" value="#{statisticsController.statusSort}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" >
                                            <f:selectItems value="#{statisticsController.statusSortItems}" />
                                        </e:selectOneMenu>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:selectOneMenu
                                            rendered="#{statisticsController.statistics != 0}"
                                            id="creationPeriod" value="#{statisticsController.ticketCreationsPeriodSpec}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" >
                                            <f:selectItems value="#{statisticsController.creationPeriodSpecItems}" />
                                        </e:selectOneMenu>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:selectOneMenu
                                            rendered="#{(statisticsController.statistics == 1
                                                or statisticsController.statistics == 4
                                                or statisticsController.statistics == 7)
                                                    and statisticsController.ticketCreationsPeriodSpec != 'w'
                                                    and statisticsController.ticketCreationsPeriodSpec != 'm'
                                                    and statisticsController.ticketCreationsPeriodSpec != 'y'}"
                                            id="ticketsCreationHistoryInterval"
                                            value="#{statisticsController.ticketCreationsHistoryIntervalSpec}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" >
                                            <f:selectItem itemValue="m" itemLabel="#{msgs['STATISTICS.INTERVAL_FILTER.MONTH']}"  />
                                            <f:selectItem itemValue="y" itemLabel="#{msgs['STATISTICS.INTERVAL_FILTER.YEAR']}"  />
                                        </e:selectOneMenu>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:selectOneMenu
                                            rendered="#{statisticsController.statistics == 2
                                                or statisticsController.statistics == 5
                                                or statisticsController.statistics == 8}"
                                            id="ticketsCreationDistribution"
                                            value="#{statisticsController.ticketCreationsDistributionSpec}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" >
                                            <f:selectItem itemValue="how" itemLabel="#{msgs['STATISTICS.DISTRIBUTION_FILTER.HOW']}"  />
                                            <f:selectItem itemValue="hod" itemLabel="#{msgs['STATISTICS.DISTRIBUTION_FILTER.HOD']}"  />
                                            <f:selectItem itemValue="dow" itemLabel="#{msgs['STATISTICS.DISTRIBUTION_FILTER.DOW']}"  />
                                        </e:selectOneMenu>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:selectOneMenu
                                            rendered="#{statisticsController.statistics == 3}"
                                            id="creatorsMaxNumber" value="#{statisticsController.ticketCreatorsMaxNumber}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" >
                                            <f:selectItems value="#{statisticsController.ticketCreatorsMaxNumberItems}" />
                                        </e:selectOneMenu>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <e:selectOneMenu
                                            rendered="#{statisticsController.statistics == 6
                                                or statisticsController.statistics == 9}"
                                            id="ticketsMaxNumber" value="#{statisticsController.ticketsMaxNumber}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" >
                                            <f:selectItems value="#{statisticsController.ticketsMaxNumberItems}" />
                                        </e:selectOneMenu>
                                    </h:panelGroup>
                                </e:panelGrid>
                                <h:panelGroup
                                    rendered="#{statisticsController.statistics == 4
                                        or statisticsController.statistics == 5
                                        or statisticsController.statistics == 7
                                        or statisticsController.statistics == 8}" >
                                    <e:selectBooleanCheckbox id="showMinMax"
                                        value="#{statisticsController.showMinMax}"
                                        onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" />
                                    <e:text value=" #{msgs['STATISTICS.TEXT.SHOW_MIN_MAX']}" />
                                </h:panelGroup>
                                <h:panelGroup
                                    rendered="#{statisticsController.statistics == 10}">
                                    <e:selectBooleanCheckbox id="ignoreArchivedTickets"
                                        value="#{statisticsController.ignoreArchivedTickets}"
                                        onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" />
                                    <e:text value=" #{msgs['STATISTICS.TEXT.IGNORE_ARCHIVED_TICKETS']}" />
                                </h:panelGroup>
                                <e:panelGrid columns="2" columnClasses="colRight,colLeft"
                                    rendered="#{statisticsController.statistics == 1
                                        or statisticsController.statistics == 2
                                        or statisticsController.statistics == 4
                                        or statisticsController.statistics == 5
                                        or statisticsController.statistics == 7
                                        or statisticsController.statistics == 8
                                        or statisticsController.statistics == 10}">
                                    <h:panelGroup>
                                        <h:panelGroup
                                            rendered="#{statisticsController.canDecChartWidth}" >
                                            <t:graphicImage
                                                value="/media/images/dec-chart-width.png"
                                                title="#{msgs['STATISTICS.BUTTON.DEC_WIDTH']}"
                                                style="cursor: pointer"
                                                onclick="simulateLinkClick('statisticsForm:decChartWidthButton');" />
                                            <e:commandButton style="display: none"
                                                value="#{msgs['STATISTICS.BUTTON.DEC_WIDTH']}"
                                                id="decChartWidthButton"
                                                action="#{statisticsController.decChartWidth}" />
                                        </h:panelGroup>
                                        <h:panelGroup
                                            rendered="#{statisticsController.canIncChartWidth}" >
                                            <t:graphicImage
                                                value="/media/images/inc-chart-width.png"
                                                title="#{msgs['STATISTICS.BUTTON.INC_WIDTH']}"
                                                style="cursor: pointer"
                                                onclick="simulateLinkClick('statisticsForm:incChartWidthButton');" />
                                            <e:commandButton style="display: none"
                                                value="#{msgs['STATISTICS.BUTTON.INC_WIDTH']}"
                                                id="incChartWidthButton"
                                                action="#{statisticsController.incChartWidth}" />
                                        </h:panelGroup>
                                    </h:panelGroup>
                                    <h:panelGroup >
                                        <t:graphicImage
                                            style="cursor: pointer"
                                            onclick="simulateLinkClick('statisticsForm:setDefaultChartSizeButton');"
                                            value="/media/images/set-default-chart-size.png"
                                            title="#{msgs['STATISTICS.BUTTON.SET_DEFAULT_SIZE']}" />
                                        <e:commandButton style="display: none"
                                            value="#{msgs['STATISTICS.BUTTON.SET_DEFAULT_SIZE']}"
                                            id="setDefaultChartSizeButton"
                                            action="#{statisticsController.setDefaultChartSize}" />
                                    </h:panelGroup>
                                    <h:panelGroup >
                                        <c:chart id="chart"
                                            datasource="#{statisticsController.chartType == 'pie' ? statisticsController.pieData : statisticsController.chartData}"
                                            type="#{statisticsController.chartType}"
                                            orientation="#{statisticsController.chartOrientation}"
                                            antialias="true"
                                            title="#{statisticsController.chartTitle}"
                                            xlabel="#{statisticsController.chartXtitle}"
                                            ylabel="#{statisticsController.chartYtitle}"
                                            legend="#{statisticsController.chartLegend}"
                                            height="#{statisticsController.chartHeight}"
                                            width="#{statisticsController.chartWidth}"  />
                                    </h:panelGroup>
                                    <e:panelGrid columns="1" columnClasses="colLeft" >
                                        <h:panelGroup>
                                            <h:panelGroup
                                                rendered="#{statisticsController.canIncChartHeight}" >
                                                <t:graphicImage
                                                    value="/media/images/inc-chart-height.png"
                                                    title="#{msgs['STATISTICS.BUTTON.INC_HEIGHT']}"
                                                    style="cursor: pointer"
                                                    onclick="simulateLinkClick('statisticsForm:incChartHeightButton');" />
                                                <e:commandButton style="display: none"
                                                    value="#{msgs['STATISTICS.BUTTON.INC_HEIGHT']}"
                                                    id="incChartHeightButton"
                                                    action="#{statisticsController.incChartHeight}" />
                                            </h:panelGroup>
                                        </h:panelGroup>
                                        <h:panelGroup>
                                            <h:panelGroup
                                                rendered="#{statisticsController.canDecChartHeight}" >
                                                <t:graphicImage
                                                    value="/media/images/dec-chart-height.png"
                                                    title="#{msgs['STATISTICS.BUTTON.DEC_HEIGHT']}"
                                                    style="cursor: pointer"
                                                    onclick="simulateLinkClick('statisticsForm:decChartHeightButton');" />
                                                <e:commandButton style="display: none"
                                                    value="#{msgs['STATISTICS.BUTTON.DEC_HEIGHT']}"
                                                    id="decChartHeightButton"
                                                    action="#{statisticsController.decChartHeight}" />
                                            </h:panelGroup>
                                        </h:panelGroup>
                                    </e:panelGrid>
                                </e:panelGrid>
                                <h:panelGroup rendered="#{statisticsController.statistics == 3}">
                                    <h:panelGroup rendered="#{not empty statisticsController.ticketCreatorsData}">
                                        <e:subSection value="#{statisticsController.chartTitle}" />
                                        <e:dataTable value="#{statisticsController.ticketCreatorsData}" var="entry"
                                            cellspacing="0" cellpadding="0" columnClasses="colLeft,colLeft,colLeft" >
                                            <f:facet name="header">
                                                <t:htmlTag value="hr" />
                                            </f:facet>
                                            <f:facet name="footer">
                                                <t:htmlTag value="hr" />
                                            </f:facet>
                                            <t:column>
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.CHART.TICKET_CREATORS.HEADER.RANK']} " />
                                                </f:facet>
                                                <e:text value="#{msgs['STATISTICS.CHART.TICKET_CREATORS.ENTRY.RANK']}" rendered="#{entry.rank != -1}" >
                                                    <f:param value="#{entry.rank + 1}" />
                                                </e:text>
                                            </t:column>
                                            <t:column>
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.CHART.TICKET_CREATORS.HEADER.TICKETS_NUMBER']} " />
                                                </f:facet>
                                                <e:text value="#{msgs['STATISTICS.CHART.TICKET_CREATORS.ENTRY.TICKETS_NUMBER']}" rendered="#{entry.rank != -1}" >
                                                    <f:param value="#{entry.number}" />
                                                </e:text>
                                            </t:column>
                                            <t:column>
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.CHART.TICKET_CREATORS.HEADER.USER']} " />
                                                </f:facet>
                                                <e:text value="#{msgs['STATISTICS.CHART.TICKET_CREATORS.ENTRY.USER']}" >
                                                    <f:param value="#{userFormatter[entry.user]}" />
                                                </e:text>
                                            </t:column>
                                        </e:dataTable>
                                    </h:panelGroup>
                                    <e:paragraph
                                        value="#{msgs['STATISTICS.CHART.TICKET_CREATORS.NONE']}"
                                        rendered="#{empty statisticsController.ticketCreatorsData}" />
                                </h:panelGroup>
                                <h:panelGroup
                                    rendered="#{statisticsController.statistics == 4
                                        or statisticsController.statistics == 5
                                        or statisticsController.statistics == 7
                                        or statisticsController.statistics == 8}">
                                    <h:panelGroup rendered="#{not empty statisticsController.chartValues}">
                                        <e:dataTable value="#{statisticsController.chartValuesLabels}" var="label"
                                            cellspacing="0" cellpadding="0" columnClasses="colLeft,colLeft,colLeft,colLeft,colLeft" >
                                            <f:facet name="header">
                                                <t:htmlTag value="hr" />
                                            </f:facet>
                                            <f:facet name="footer">
                                                <t:htmlTag value="hr" />
                                            </f:facet>
                                            <t:column>
                                                <e:bold value="#{label}" />
                                            </t:column>
                                            <t:column>
                                                <f:facet name="header">
                                                    <e:bold value="#{msgs['STATISTICS.VALUES.HEADER.AVG']}" />
                                                </f:facet>
                                                <e:bold
                                                    value="#{statisticsController.chartValues[label].number != 0
                                                        ? elapsedTimeI18nFormatter[statisticsController.chartValues[label].avg]
                                                        : '-'}" />
                                            </t:column>
                                            <t:column>
                                                <f:facet name="header">
                                                    <e:bold value="#{msgs['STATISTICS.VALUES.HEADER.MIN']}" />
                                                </f:facet>
                                                <e:text
                                                    value="#{statisticsController.chartValues[label].number != 0
                                                        ? elapsedTimeI18nFormatter[statisticsController.chartValues[label].min]
                                                        : '-'}" />
                                            </t:column>
                                            <t:column>
                                                <f:facet name="header">
                                                    <e:bold value="#{msgs['STATISTICS.VALUES.HEADER.MAX']}" />
                                                </f:facet>
                                                <e:text
                                                    value="#{statisticsController.chartValues[label].number != 0
                                                        ? elapsedTimeI18nFormatter[statisticsController.chartValues[label].max]
                                                        : '-'}" />
                                            </t:column>
                                            <t:column>
                                                <f:facet name="header">
                                                    <e:bold value="#{msgs['STATISTICS.VALUES.HEADER.NUMBER']}" />
                                                </f:facet>
                                                <e:text
                                                    value="#{statisticsController.chartValues[label].number != 0
                                                        ? statisticsController.chartValues[label].number
                                                        : '-'}" />
                                            </t:column>
                                        </e:dataTable>
                                    </h:panelGroup>
                                </h:panelGroup>
                                <h:panelGroup
                                    rendered="#{statisticsController.statistics == 6
                                        or statisticsController.statistics == 9}">
                                    <h:panelGroup
                                        rendered="#{statisticsController.statistics == 6}">
                                        <e:selectBooleanCheckbox id="hideCharged"
                                            value="#{statisticsController.hideCharged}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" />
                                        <e:text value=" #{msgs['STATISTICS.TEXT.HIDE_CHARGED']}" />
                                    </h:panelGroup>
                                    <h:panelGroup
                                        rendered="#{statisticsController.statistics == 9}">
                                        <e:selectBooleanCheckbox id="hideClosed"
                                            value="#{statisticsController.hideClosed}"
                                            onchange="simulateLinkClick('statisticsForm:changeStatisticsButton');" />
                                        <e:text value=" #{msgs['STATISTICS.TEXT.HIDE_CLOSED']}" />
                                    </h:panelGroup>
                                    <h:panelGroup rendered="#{not empty statisticsController.ticketsData}">
                                        <e:subSection value="#{statisticsController.chartTitle}" />
                                        <e:dataTable id="ticketsData" rowStyle="#{ticketEntry.canView ? 'cursor: pointer' : ''}"
                                            value="#{statisticsController.ticketsData}" var="ticketEntry" rowIndexVar="index"
                                            cellspacing="0" cellpadding="0" columnClasses="colLeft,colLeft,colLeft,colLeft,colLeft,colLeft,colLeft"
                                            rowOnMouseOver="javascript:{if (#{ticketEntry.canView}) {previousClass = this.className; this.className = 'portlet-table-selected';}}"
                                            rowOnMouseOut="javascript:{if (#{ticketEntry.canView}) {this.className = previousClass;}}"
                                            >
                                            <f:facet name="header">
                                                <t:htmlTag value="hr" />
                                            </f:facet>
                                            <f:facet name="footer">
                                                <t:htmlTag value="hr" />
                                            </f:facet>
                                            <t:column onclick="javascript:{if (#{ticketEntry.canView}) {selectTicket(#{index});}}" >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.CHART.TICKETS.HEADER.ID']} " />
                                                </f:facet>
                                                <e:text value="#{ticketEntry.ticketId}" />
                                            </t:column>
                                            <t:column onclick="javascript:{if (#{ticketEntry.canView}) {selectTicket(#{index});}}" >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.CHART.TICKETS.HEADER.CREATION_DATE']} " />
                                                </f:facet>
                                                <e:text value="{0}" >
                                                    <f:param value="#{ticketEntry.creationDate}" />
                                                </e:text>
                                            </t:column>
                                            <t:column onclick="javascript:{if (#{ticketEntry.canView}) {selectTicket(#{index});}}" >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.CHART.TICKETS.HEADER.CREATION_DEPARTMENT']} " />
                                                </f:facet>
                                                <e:text value="#{ticketEntry.creationDepartment.label}" />
                                            </t:column>
                                            <t:column onclick="javascript:{if (#{ticketEntry.canView}) {selectTicket(#{index});}}" >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.CHART.TICKETS.HEADER.DEPARTMENT']} " />
                                                </f:facet>
                                                <e:text value="#{ticketEntry.department.label}" />
                                            </t:column>
                                            <t:column onclick="javascript:{if (#{ticketEntry.canView}) {selectTicket(#{index});}}" >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.CHART.TICKETS.HEADER.STATUS']} " />
                                                </f:facet>
                                                <e:text value="#{msgs[ticketStatusI18nKeyProvider[ticketEntry.status]]}" />
                                            </t:column>
                                            <t:column onclick="javascript:{if (#{ticketEntry.canView}) {selectTicket(#{index});}}" >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.CHART.TICKETS.HEADER.LABEL']} " />
                                                </f:facet>
                                                <e:text value="#{ticketEntry.label}" />
                                            </t:column>
                                            <t:column onclick="javascript:{if (#{ticketEntry.canView}) {selectTicket(#{index});}}" >
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <e:text value=" #{msgs['STATISTICS.CHART.TICKETS.HEADER.CHARGE_TIME']} "
                                                            rendered="#{statisticsController.statistics == 6}" />
                                                        <e:text value=" #{msgs['STATISTICS.CHART.TICKETS.HEADER.CLOSURE_TIME']} "
                                                            rendered="#{statisticsController.statistics == 9}" />
                                                    </h:panelGroup>
                                                </f:facet>
                                                <e:text value="#{elapsedTimeI18nFormatter[ticketEntry.chargeTime]}"
                                                    rendered="#{statisticsController.statistics == 6}" />
                                                <e:text value="#{elapsedTimeI18nFormatter[ticketEntry.closureTime]}"
                                                    rendered="#{statisticsController.statistics == 9}" />
                                            </t:column>
                                            <t:column >
                                                <h:panelGroup rendered="#{ticketEntry.canView}" >
                                                    <e:commandButton value="#{msgs['_.BUTTON.VIEW_EDIT']}"
                                                        id="selectTicketButton" style="display: none"
                                                        action="viewTicket" immediate="true"
                                                        rendered="#{not ticketEntry.archived}">
                                                        <t:updateActionListener value="#{ticketEntry.ticket}"
                                                            property="#{ticketController.ticket}" />
                                                        <t:updateActionListener value="statistics"
                                                            property="#{ticketController.backPage}" />
                                                    </e:commandButton>
                                                    <e:commandButton value="#{msgs['_.BUTTON.VIEW_EDIT']}"
                                                        id="selectArchivedTicketButton" style="display: none"
                                                        action="viewArchivedTicket" immediate="true"
                                                        rendered="#{ticketEntry.archived}">
                                                        <t:updateActionListener value="#{ticketEntry.archivedTicket}"
                                                            property="#{archivedTicketController.archivedTicket}" />
                                                        <t:updateActionListener value="statistics"
                                                            property="#{archivedTicketController.backPage}" />
                                                    </e:commandButton>
                                                </h:panelGroup>
                                            </t:column>
                                        </e:dataTable>
                                    </h:panelGroup>
                                    <e:paragraph
                                        value="#{msgs['STATISTICS.CHART.TICKETS.NONE']}"
                                        rendered="#{empty statisticsController.ticketsData}" />
                                </h:panelGroup>
                                <h:panelGroup
                                    rendered="#{statisticsController.statistics == 11}">
                                    <h:panelGroup rendered="#{not empty statisticsController.spentTimeData}">
                                        <e:subSection value="#{statisticsController.chartTitle}" />
                                        <e:dataTable id="ticketsData"
                                            value="#{statisticsController.spentTimeData}" var="entry" rowIndexVar="index"
                                            cellspacing="0" cellpadding="0" columnClasses="colLeft,colLeft,colLeft,colLeft"
                                            >
                                            <f:facet name="header">
                                                <t:htmlTag value="hr" />
                                            </f:facet>
                                            <f:facet name="footer">
                                                <t:htmlTag value="hr" />
                                            </f:facet>
                                            <t:column >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.SPENT_TIME.HEADER.FINAL_DEPARTMENT']} " />
                                                </f:facet>
                                                <e:bold rendered="#{entry.printFinalDepartment}" value="#{entry.finalDepartment.label}" />
                                            </t:column>
                                            <t:column >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.SPENT_TIME.HEADER.CREATION_DEPARTMENT']} " />
                                                </f:facet>
                                                <e:italic value="#{entry.creationDepartment.label}" rendered="#{entry.creationDepartment != null}" />
                                            </t:column>
                                            <t:column >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.SPENT_TIME.HEADER.NUMBER']} " />
                                                </f:facet>
                                                <e:italic value="#{entry.number}"
                                                    rendered="#{entry.creationDepartment != null}" />
                                                <e:bold value="#{entry.number}"
                                                    rendered="#{entry.creationDepartment == null}" />
                                            </t:column>
                                            <t:column >
                                                <f:facet name="header">
                                                    <e:text value=" #{msgs['STATISTICS.SPENT_TIME.HEADER.SPENT_TIME']} " />
                                                </f:facet>
                                                <h:panelGroup
                                                    rendered="#{entry.spentTime != 0}">
                                                    <e:italic value="#{spentTimeI18nFormatter[entry.spentTime]}"
                                                        rendered="#{entry.creationDepartment != null}" />
                                                    <e:bold value="#{spentTimeI18nFormatter[entry.spentTime]}"
                                                        rendered="#{entry.creationDepartment == null}" />
                                                </h:panelGroup>
                                                <h:panelGroup
                                                    rendered="#{entry.spentTime == 0}">
                                                    <e:italic value="-" rendered="#{entry.creationDepartment != null}" />
                                                    <e:bold value="-" rendered="#{entry.creationDepartment == null}" />
                                                </h:panelGroup>
                                            </t:column>
                                        </e:dataTable>
                                    </h:panelGroup>
                                    <e:paragraph
                                        value="#{msgs['STATISTICS.SPENT_TIME.NONE']}"
                                        rendered="#{empty statisticsController.spentTimeData}" />
                                </h:panelGroup>
                            </e:form>
                        </t:htmlTag>
                    </t:htmlTag>
               </t:htmlTag>
               <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
               </t:htmlTag>
    </t:htmlTag>
</e:page>
