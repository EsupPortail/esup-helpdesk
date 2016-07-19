<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="faqs"
	authorized="#{faqsController.pageAuthorized}"
	locale="#{sessionController.locale}" >
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="faqsForm" >
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%"
			cellspacing="0" cellpadding="0" >
			<e:section value="#{msgs['FAQS.TITLE']}" />
			<h:panelGroup >
				<h:panelGroup rendered="#{faqsController.interfaceItems != null}" >
					<e:outputLabel for="interface" value="#{msgs['FAQS.TEXT.INTERFACE_PROMPT']}" />
					<e:selectOneMenu id="interface"  value="#{faqsController.editInterface}" 
						onchange="simulateLinkClick('faqsForm:updateInterfaceButton');" >
						<f:selectItems value="#{faqsController.interfaceItems}" />
					</e:selectOneMenu>
					<e:commandButton id="updateInterfaceButton" style="display: none"
						value="#{msgs['_.BUTTON.REFRESH']}" 
						action="#{faqsController.updateInterface}" />
				</h:panelGroup>
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:panelGrid columns="3" columnClasses="colLeft,colCenter,colLeftMax" width="100%"  >
            <h:panelGroup >
                <t:graphicImage value="/media/images/trans.png" height="1" width="400" />
                <%@include file="_faqsTree.jsp"%>
    		</h:panelGroup>
			<e:text escape="false" value="&nbsp;" style="width: 20px" />
			<h:panelGroup >
				<%@include file="_faqRoot.jsp"%>
				<%@include file="_faqDepartment.jsp"%>
				<%@include file="_faq.jsp"%>
                <%@include file="_faqSubFaqs.jsp"%>
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{faqsController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
