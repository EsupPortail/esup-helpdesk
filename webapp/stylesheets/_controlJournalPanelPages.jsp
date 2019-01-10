<%@include file="_include.jsp"%>
<h:panelGroup rendered="#{journalController.paginator.lastPageNumber != 0}">

	<h:panelGroup rendered="#{not journalController.paginator.firstPage}">
        <h:panelGroup style="cursor: pointer" styleClass="page-first" onclick="buttonClick('journalForm:data:pageFirst');">
            <t:htmlTag value="i" styleClass="fas fa-angle-double-left"/>
        </h:panelGroup>
        <h:panelGroup style="cursor: pointer" styleClass="page-previous" onclick="buttonClick('journalForm:data:pagePrevious');">
            <t:htmlTag value="i" styleClass="fas fa-angle-left"/>
        </h:panelGroup>
	</h:panelGroup>

	<t:dataList value="#{journalController.paginator.nearPages}" var="page" >
		<e:text value="#{page + 1}"
			rendered="#{page == journalController.paginator.currentPage}" styleClass="current-page"/>
		<h:commandLink value="#{page + 1}"
			rendered="#{page != journalController.paginator.currentPage}" >
			<t:updateActionListener value="#{page}"
				property="#{journalController.paginator.currentPage}" />
		</h:commandLink>
	</t:dataList>

	<h:panelGroup rendered="#{not journalController.paginator.lastPage}">
        <h:panelGroup style="cursor: pointer" styleClass="page-next" onclick="buttonClick('journalForm:data:pageNext');">
            <t:htmlTag value="i" styleClass="fas fa-angle-right"/>
        </h:panelGroup>
        <h:panelGroup style="cursor: pointer" styleClass="page-last" onclick="buttonClick('journalForm:data:pageLast');">
             <t:htmlTag value="i" styleClass="fas fa-angle-double-right"/>
        </h:panelGroup>
	</h:panelGroup>
</h:panelGroup>

