<%@include file="_include.jsp"%>
<h:panelGroup
	rendered="#{controlPanelController.paginator.lastPageNumber != 0}">
	<h:panelGroup
		rendered="#{not controlPanelController.paginator.firstPage}">
		<t:graphicImage value="/media/images/page-first.png"
			style="cursor: pointer"
			onclick="simulateLinkClick('controlPanelForm:data:pageFirst');" />
		<e:text value=" " />
		<t:graphicImage value="/media/images/page-previous.png"
			style="cursor: pointer"
			onclick="simulateLinkClick('controlPanelForm:data:pagePrevious');" />
	</h:panelGroup>
	<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
	<t:dataList value="#{controlPanelController.paginator.nearPages}"
		var="page">
		<e:text value=" " />
		<e:italic value="#{page + 1}"
			rendered="#{page == controlPanelController.paginator.currentPage}" />
		<h:commandLink value="#{page + 1}"
			rendered="#{page != controlPanelController.paginator.currentPage}" >
			<t:updateActionListener value="#{page}"
				property="#{controlPanelController.paginator.currentPage}" />
		</h:commandLink>
		<e:text value=" " />
	</t:dataList>
	<h:panelGroup
		rendered="#{not controlPanelController.paginator.lastPage}">
		<t:graphicImage value="/media/images/page-next.png"
			style="cursor: pointer"
			onclick="simulateLinkClick('controlPanelForm:data:pageNext');" />
		<e:text value=" " />
		<t:graphicImage value="/media/images/page-last.png"
			style="cursor: pointer"
			onclick="simulateLinkClick('controlPanelForm:data:pageLast');" />
	</h:panelGroup>
</h:panelGroup>

