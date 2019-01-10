<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="about" locale="#{sessionController.locale}" >
	   <t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper about">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                    <t:htmlTag value="div" styleClass="content-inner">
                              <t:htmlTag value="div" styleClass="region">
                                        <t:htmlTag value="h1">
                                            <t:htmlTag value="span" styleClass="title">
                                                  <h:outputText value="#{msgs['ABOUT.TITLE']}" escape="false" />
                                            </t:htmlTag>
                                            <t:htmlTag value="span" styleClass="subtitle title">
                                                <h:outputText value=" #{applicationService.name}" escape="false" />
                                            </t:htmlTag>
                                        </t:htmlTag>
                              </t:htmlTag>

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
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
                <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
                </t:htmlTag>
        </t:htmlTag>
</e:page>
