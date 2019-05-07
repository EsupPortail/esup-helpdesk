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
            <t:htmlTag value="div">
            	<t:htmlTag value="div" rendered="#{controller != null}" >
					<e:text 
						value="#{msgs['COMMON.PERM_LINKS.PROMPT']}" 
						escape="false" style="font-size: 75%;" />
					<e:text 
						value=" #{msgs['COMMON.PERM_LINKS.APPLICATION']}" 
						escape="false" style="font-size: 75%;" 
						rendered="#{userStore.applicationAuthAllowed}"
						styleClass="copy" >
						<f:param value="#{controller.applicationPermLink}"/>
					</e:text>
					<e:text 
						value=" #{msgs['COMMON.PERM_LINKS.CAS']}" 
						escape="false" style="font-size: 75%;" 
		                rendered="#{userStore.casAuthAllowed}" 
						styleClass="copy" >
						<f:param value="#{controller.casPermLink}"/>
					</e:text>
					<e:text 
						value=" #{msgs['COMMON.PERM_LINKS.SHIBBOLETH']}" 
						escape="false" style="font-size: 75%;" 
		                rendered="#{userStore.shibbolethAuthAllowed}" 
						styleClass="copy" >
						<f:param value="#{controller.shibbolethPermLink}"/>
					</e:text>
					<e:text 
						value=" #{msgs['COMMON.PERM_LINKS.SPECIFIC']}" 
						escape="false" style="font-size: 75%;" 
		                rendered="#{userStore.specificAuthAllowed}" 
						styleClass="copy" >
						<f:param value="#{controller.specificPermLink}"/>
					</e:text>
				</t:htmlTag>
            </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>
