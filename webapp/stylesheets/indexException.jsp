<%@include file="_include.jsp"%>
<e:page stringsVar="msgs">
	<e:emptyMenu />
	<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
		<e:section value="#{msgs['EXCEPTION.INDEX.TITLE']}" />
		<h:panelGroup>
			<%@include file="_exceptionDetailsButtons.jsp"%>
		</h:panelGroup>
	</e:panelGrid>
	<e:messages />
	<e:paragraph value="#{msgs['EXCEPTION.INDEX.TEXT']}" />
	<%@include file="_exceptionForm.jsp"%>
	<%@include file="_exceptionDetailsPanel.jsp"%>
</e:page>
