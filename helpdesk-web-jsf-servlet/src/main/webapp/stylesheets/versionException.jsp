<%@include file="_commons-include.jsp"%>
<e:page stringsVar="msgs">
	<e:emptyMenu />
	<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
		<e:section value="#{msgs['VERSION_EXCEPTION.TITLE']}" />
		<h:panelGroup />
	</e:panelGrid>
	<e:messages />
	<e:paragraph value="#{msgs['VERSION_EXCEPTION.TEXT.1']}" />
	<e:paragraph value="#{msgs['VERSION_EXCEPTION.TEXT.2']}" />
	<%@include file="_exceptionForm.jsp"%>
</e:page>
