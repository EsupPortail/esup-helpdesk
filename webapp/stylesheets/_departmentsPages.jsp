<%@include file="_include.jsp"%>
<h:panelGroup rendered="#{departmentsController.departmentPaginator.lastPageNumber != 0}">

	<h:panelGroup rendered="#{not departmentsController.departmentPaginator.firstPage}">
        <h:panelGroup style="cursor: pointer" styleClass="page-first" onclick="buttonClick('departmentsForm:data:pageFirst');">
            <t:htmlTag value="i" styleClass="fas fa-angle-double-left"/>
        </h:panelGroup>
        <h:panelGroup style="cursor: pointer" styleClass="page-previous" onclick="buttonClick('departmentsForm:data:pagePrevious');">
            <t:htmlTag value="i" styleClass="fas fa-angle-left"/>
        </h:panelGroup>
	</h:panelGroup>

	<t:dataList value="#{departmentsController.departmentPaginator.nearPages}" var="page" >
		<e:text value="#{page + 1}"
			rendered="#{page == departmentsController.departmentPaginator.currentPage}" styleClass="current-page"/>
		<h:commandLink value="#{page + 1}"
			rendered="#{page != departmentsController.departmentPaginator.currentPage}" >
			<t:updateActionListener value="#{page}"
				property="#{departmentsController.departmentPaginator.currentPage}" />
		</h:commandLink>
	</t:dataList>

	<h:panelGroup rendered="#{not departmentsController.departmentPaginator.lastPage}">
        <h:panelGroup style="cursor: pointer" styleClass="page-next" onclick="buttonClick('departmentsForm:data:pageNext');">
            <t:htmlTag value="i" styleClass="fas fa-angle-right"/>
        </h:panelGroup>
        <h:panelGroup style="cursor: pointer" styleClass="page-last" onclick="buttonClick('departmentsForm:data:pageLast');">
             <t:htmlTag value="i" styleClass="fas fa-angle-double-right"/>
        </h:panelGroup>
	</h:panelGroup>
</h:panelGroup>

