<%@include file="_include.jsp"%>
<h:panelGroup rendered="#{controlPanelController.paginator.lastPageNumber != 0}">

	<h:panelGroup rendered="#{not controlPanelController.paginator.firstPage}">
        <h:panelGroup style="cursor: pointer" styleClass="page-first" onclick="buttonClick('controlPanelForm:data:pageFirst');">
            <t:htmlTag value="i" styleClass="fas fa-angle-double-left"/>
        </h:panelGroup>
        <h:panelGroup style="cursor: pointer" styleClass="page-previous" onclick="buttonClick('controlPanelForm:data:pagePrevious');">
            <t:htmlTag value="i" styleClass="fas fa-angle-left"/>
        </h:panelGroup>
	</h:panelGroup>

	<t:dataList value="#{controlPanelController.paginator.nearPages}" var="page" >
		<e:text value="#{page + 1}"
			rendered="#{page == controlPanelController.paginator.currentPage}" styleClass="current-page"/>
		<h:commandLink value="#{page + 1}"
			rendered="#{page != controlPanelController.paginator.currentPage}" >
			<t:updateActionListener value="#{page}"
				property="#{controlPanelController.paginator.currentPage}" />
		</h:commandLink>
	</t:dataList>

	<h:panelGroup rendered="#{not controlPanelController.paginator.lastPage}">
        <h:panelGroup style="cursor: pointer" styleClass="page-next" onclick="buttonClick('controlPanelForm:data:pageNext');">
            <t:htmlTag value="i" styleClass="fas fa-angle-right"/>
        </h:panelGroup>
        <h:panelGroup style="cursor: pointer" styleClass="page-last" onclick="buttonClick('controlPanelForm:data:pageLast');">
             <t:htmlTag value="i" styleClass="fas fa-angle-double-right"/>
        </h:panelGroup>
	</h:panelGroup>
</h:panelGroup>

