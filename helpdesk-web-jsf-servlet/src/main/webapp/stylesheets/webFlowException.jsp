<%@include file="_commons-include.jsp"%>
<e:page stringsVar="msgs">
	<e:emptyMenu />
	<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
		<e:section value="#{msgs['WEB_FLOW_EXCEPTION.TITLE']}" />
		<h:panelGroup>
			<%@include file="_exceptionDetailsButtons.jsp"%>
		</h:panelGroup>
	</e:panelGrid>
	<e:messages />
	<e:paragraph value="#{msgs['WEB_FLOW_EXCEPTION.TEXT.TOP']}" />
	<%@include file="_exceptionForm.jsp"%>
	<%@include file="_exceptionDetailsPanel.jsp"%>
</e:page>
