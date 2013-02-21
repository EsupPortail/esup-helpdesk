<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="about" locale="#{sessionController.locale}" >
	<%@include file="_navigation.jsp"%>

	<e:section value="#{msgs['ABOUT.TITLE']}">
		<f:param value="#{applicationService.name}" />
		<f:param value="#{applicationService.version}" />
	</e:section>

	<e:paragraph value="#{msgs['ABOUT.TEXT.SUMMARY']}">
		<f:param value="#{applicationService.name}" />
	</e:paragraph>

	<e:paragraph value="#{msgs['ABOUT.TEXT.AUTHORS']}">
		<f:param value="#{applicationService.name}" />
	</e:paragraph>
	<e:ul>
		<e:li value="#{msgs['ABOUT.TEXT.AUTHORS.PA']}" />
		<e:li value="#{msgs['ABOUT.TEXT.AUTHORS.AB']}" />
	</e:ul>

	<e:subSection value="#{msgs['ABOUT.SUBTITLE.COPYRIGHT']}" />
	<e:paragraph value="#{msgs['ABOUT.TEXT.COPYRIGHT']}">
		<f:param value="#{applicationService.name}" />
		<f:param value="#{applicationService.version}" />
		<f:param value="#{applicationService.copyright}" />
	</e:paragraph>

	<e:subSection value="#{msgs['ABOUT.SUBTITLE.MORE_INFORMATION']}">
		<f:param value="#{applicationService.name}" />
	</e:subSection>
	<e:ul>
		<e:li escape="false" value="#{msgs['ABOUT.TEXT.MORE_INFORMATION.ESUP_PORTAIL']}" />
	</e:ul>
	<t:aliasBean alias="#{controller}" value="#{aboutController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
