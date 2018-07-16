<%@include file="_include.jsp"%>
<h:panelGroup rendered="#{administratorsController.paginator.lastPageNumber != 0}">

	<h:panelGroup rendered="#{not administratorsController.paginator.firstPage}">
        <h:panelGroup style="cursor: pointer" styleClass="page-first" onclick="buttonClick('administratorsForm:data:pageFirst');">
            <t:htmlTag value="i" styleClass="fas fa-angle-double-left"/>
        </h:panelGroup>
        <h:panelGroup style="cursor: pointer" styleClass="page-previous" onclick="buttonClick('administratorsForm:data:pagePrevious');">
            <t:htmlTag value="i" styleClass="fas fa-angle-left"/>
        </h:panelGroup>
	</h:panelGroup>

	<t:dataList value="#{administratorsController.paginator.nearPages}" var="page" >

		<e:text value="#{page + 1}"
			rendered="#{page == administratorsController.paginator.currentPage}" styleClass="current-page"/>
		<h:commandLink value="#{page + 1}"
			rendered="#{page != administratorsController.paginator.currentPage}" >
			<t:updateActionListener value="#{page}"
				property="#{administratorsController.paginator.currentPage}" />
		</h:commandLink>

	</t:dataList>

	<h:panelGroup rendered="#{not administratorsController.paginator.lastPage}">
        <h:panelGroup style="cursor: pointer" styleClass="page-next" onclick="buttonClick('administratorsForm:data:pageNext');">
            <t:htmlTag value="i" styleClass="fas fa-angle-right"/>
        </h:panelGroup>
        <h:panelGroup style="cursor: pointer" styleClass="page-last" onclick="buttonClick('administratorsForm:data:pageLast');">
             <t:htmlTag value="i" styleClass="fas fa-angle-double-right"/>
        </h:panelGroup>
	</h:panelGroup>
</h:panelGroup>

