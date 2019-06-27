<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<h:panelGroup styleClass="faq" rendered="#{faqsController.faq != null}" >
	<e:subSection styleClass="faq-header" value="#{faqsController.faq.label}" />
	<e:text escape="false" value="#{faqsController.faq.content}" rendered="#{not (faqsController.userCanEdit and faqsController.editInterfaceDpt)}" />
	<h:panelGroup styleClass="faq-content" rendered="#{faqsController.userCanEdit and faqsController.editInterfaceDpt}" >
		<e:outputLabel for="faqTitle" value="#{msgs['FAQS.TEXT.FAQ_LABEL_PROMPT']} " />
		<e:inputText id="faqTitle" size="80" value="#{faqsController.faqToUpdate.label}" />
		<fck:editor
			id="faqContent" 
			value="#{faqsController.faqToUpdate.content}" 
			toolbarSet="faqContent" />
		<e:outputLabel for="faqScope" value="#{msgs['FAQS.TEXT.SCOPE_PROMPT']} " />
		<e:panelGrid columns="3" columnClasses="colLeftNowrap,colCenterNowrap,colCenterNowrap" width="100%">
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:updateFaqButton');" >
					<e:bold value=" #{msgs['FAQS.BUTTON.UPDATE_FAQ']} " />
					<t:graphicImage value="/media/images/save.png"
						alt="#{msgs['FAQS.BUTTON.UPDATE_FAQ']}" 
						title="#{msgs['FAQS.BUTTON.UPDATE_FAQ']}" />
				</h:panelGroup>
				<e:commandButton id="updateFaqButton" style="display: none"
					value="#{msgs['FAQS.BUTTON.UPDATE_FAQ']}" 
					action="#{departmentsController.updateFaq}" />
			</h:panelGroup>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:moveFaqButton');" >
					<e:bold value=" #{msgs['FAQS.BUTTON.MOVE_FAQ']} " />
					<t:graphicImage value="/media/images/move.png"
						alt="#{msgs['FAQS.BUTTON.MOVE_FAQ']}" 
						title="#{msgs['FAQS.BUTTON.MOVE_FAQ']}" />
				</h:panelGroup>
				<e:commandButton id="moveFaqButton" style="display: none"
					value="#{msgs['FAQS.BUTTON.MOVE_FAQ']}" immediate="true" 
					action="#{departmentsController.moveFaqDpt}" >
					<t:updateActionListener value="#{faqsController.faq}" property="#{faqsController.faqToUpdate}" />
				</e:commandButton>
			</h:panelGroup>
			<h:panelGroup>
				<h:panelGroup 
					rendered="#{empty faqsController.subFaqs}">
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:deleteFaqButton');" >
						<e:bold value=" #{msgs['FAQS.BUTTON.DELETE_FAQ']} " />
						<t:graphicImage value="/media/images/delete.png"
							alt="#{msgs['FAQS.BUTTON.DELETE_FAQ']}" 
							title="#{msgs['FAQS.BUTTON.DELETE_FAQ']}" />
					</h:panelGroup>
					<e:commandButton id="deleteFaqButton" style="display: none"
						value="#{msgs['FAQS.BUTTON.DELETE_FAQ']}" immediate="true" 
						action="#{faqsController.deleteFaq}" >
						<t:updateActionListener value="#{faqsController.faq}" property="#{faqsController.faqToUpdate}" />
					</e:commandButton>
				</h:panelGroup>
			</h:panelGroup>
		</e:panelGrid>
	</h:panelGroup>
</h:panelGroup>
