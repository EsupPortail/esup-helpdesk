<%@include file="_include.jsp"%>

<t:htmlTag value="div" styleClass="region">
    <t:htmlTag value="div" styleClass="region-inner">
            <t:htmlTag value="div">
                <t:graphicImage value="/media/images/logo-footer-small.png" alt="AMU" title="" />
            </t:htmlTag>
            <t:htmlTag value="div">
            <e:text value="#{msgs['FOOTER.TITLE']}" >
                <f:param value="#{sessionController.footerTitle}" />
            </e:text>
            </t:htmlTag>
        </t:htmlTag>
</t:htmlTag>
