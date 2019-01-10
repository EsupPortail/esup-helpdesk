<%@include file="_include.jsp"%>

<t:htmlTag value="div" styleClass="form-block">
        <t:htmlTag value="div" styleClass="form-item" >
            <t:dataList var="faqLink" value="#{departmentsController.departmentFaqLinks}" rowIndexVar="index" >
                <t:htmlTag value="div">
                <e:text value="- #{faqLink.faq.label}" />
                 </t:htmlTag>
            </t:dataList>
         </t:htmlTag>
 </t:htmlTag>

<e:paragraph 
	rendered="#{departmentsController.department.virtual}" 
	value="#{msgs['DEPARTMENT_VIEW.TEXT.VIRTUAL_HAS_NO_FAQ_LINK']}" />


 <t:htmlTag value="div" styleClass="form-block form-submit" rendered="#{departmentsController.currentUserCanEditDepartmentProperties}" >
     <t:htmlTag value="div" styleClass="form-item" >
			<e:commandButton id="editFaqLinksButton" action="#{departmentsController.editDepartmentFaqLinks}"
			    styleClass="button--secondary"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_FAQ_LINKS']}"/>
     </t:htmlTag>
 </t:htmlTag>