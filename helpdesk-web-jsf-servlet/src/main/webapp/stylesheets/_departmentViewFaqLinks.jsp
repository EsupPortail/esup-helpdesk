<%@include file="_include.jsp"%>

<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
	<e:subSection value="#{msgs['DEPARTMENT_VIEW.HEADER.FAQ_LINKS']}" />
	<h:panelGroup>
		<h:panelGroup rendered="#{departmentsController.currentUserCanEditDepartmentProperties}" >
			<e:commandButton id="editFaqLinksButton" action="#{departmentsController.editDepartmentFaqLinks}"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_FAQ_LINKS']}" style="display: none" />
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentViewForm:editFaqLinksButton');" >
				<e:bold value="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_FAQ_LINKS']} " />
				<t:graphicImage value="/media/images/edit.png"
					alt="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_FAQ_LINKS']}" 
					title="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_FAQ_LINKS']}" />
			</h:panelGroup>
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
<t:dataList var="faqLink" value="#{departmentsController.departmentFaqLinks}" rowIndexVar="index" >
	<t:htmlTag value="br" rendered="#{index != 0}" />
    <t:graphicImage value="/media/images/faq#{faqHasChildrenHelper[faqLink.faq]?'-container':''}-link.png" />
	<e:italic value=" #{faqLink.faq.label}" />
</t:dataList>
<e:paragraph 
	rendered="#{departmentsController.department.virtual}" 
	value="#{msgs['DEPARTMENT_VIEW.TEXT.VIRTUAL_HAS_NO_FAQ_LINK']}" />
