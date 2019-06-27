<%@include file="_include.jsp"%>

<h:panelGroup styleClass="hideme" rendered="#{faqsController.department != null}" >
	<e:section value="#{msgs['FAQS.TEXT.DEPARTMENT_TITLE']}" >
		<f:param value="#{faqsController.department.label}" />
	</e:section>

	<h:panelGroup
		rendered="#{faqsController.userCanEdit and faqsController.editInterface}" >
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



