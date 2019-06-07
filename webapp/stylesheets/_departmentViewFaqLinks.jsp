<%@include file="_include.jsp"%>

   <e:panelGrid columns="3" columnClasses="colLeft,colCenter,colLeftMax" width="100%"  >
       <h:panelGroup >
           <t:graphicImage value="/media/images/trans.png" height="1" width="400" />
           <%@include file="_faqsTreeDpt.jsp"%>
       </h:panelGroup>
       <e:text escape="false" value="&nbsp;" style="width: 20px" />
       <h:panelGroup style="display:block">
           <%@include file="_faqRootDpt.jsp"%>
           <%@include file="_faqDpt.jsp"%>
           <%@include file="_faqSubFaqsDpt.jsp"%>
		   <h:panelGroup rendered="#{faqsController.userCanEdit and faqsController.editInterfaceDpt}" > 
				<h:panelGroup style="cursor: pointer" 
					onclick="simulateLinkClick('departmentViewForm:addFaqButton');" >
					<e:bold value="#{msgs['FAQS.BUTTON.ADD_FAQ']} " /> 
					<t:graphicImage value="/media/images/add.png" 
						alt="#{msgs['FAQS.BUTTON.ADD_FAQ']}" 
						title="#{msgs['FAQS.BUTTON.ADD_FAQ']}" 
						/>
				</h:panelGroup>
	               <e:commandButton 
	                   id="addFaqButton" style="display: none"
	                   value="#{msgs['FAQS.BUTTON.ADD_FAQ']}" 
	                   action="#{faqsController.addFaq}" />
		   </h:panelGroup>
       </h:panelGroup>
   </e:panelGrid>