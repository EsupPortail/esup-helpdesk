<%@include file="_include.jsp"%>

<h:panelGroup styleClass="hideme" rendered="#{faqsController.department != null}" >
	<e:section value="#{msgs['FAQS.TEXT.DEPARTMENT_TITLE']}" >
		<f:param value="#{faqsController.department.label}" />
	</e:section>

	<h:panelGroup
		rendered="#{faqsController.userCanEdit and faqsController.editInterface}" >
		<e:outputLabel for="departmentScope" value="#{msgs['FAQS.TEXT.SCOPE_PROMPT']} " />
		<e:selectOneMenu id="departmentScope" 
			value="#{faqsController.departmentToUpdate.defaultFaqScope}" >
			<f:selectItems value="#{faqsController.scopeItems}" />
		</e:selectOneMenu>
		<e:italic value=" #{msgs['FAQS.TEXT.DEFAULT_FAQ_SCOPE_HELP']}" >
			<f:param value="#{msgs[faqScopeI18nKeyProvider[faqsController.defaultFaqScopeI18nSuffix]]}" />
		</e:italic>
		<e:panelGrid columns="1" columnClasses="colLeft" width="100%">
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('faqsForm:updateDepartmentButton');" >
					<e:bold value="#{msgs['FAQS.BUTTON.UPDATE_DEPARTMENT']} " />
					<t:graphicImage value="/media/images/save.png"
						alt="#{msgs['FAQS.BUTTON.UPDATE_DEPARTMENT']}" 
						title="#{msgs['FAQS.BUTTON.UPDATE_DEPARTMENT']}" />
				</h:panelGroup>
				<e:commandButton id="updateDepartmentButton" style="display: none"
					value="#{msgs['FAQS.BUTTON.UPDATE_DEPARTMENT']}" 
					action="#{faqsController.updateDepartment}" />
			</h:panelGroup>
		</e:panelGrid>
	</h:panelGroup>
</h:panelGroup>



