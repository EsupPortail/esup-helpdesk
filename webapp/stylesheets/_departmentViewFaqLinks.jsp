<%@include file="_include.jsp"%>
<t:htmlTag value="div" styleClass="form-block" rendered="#{not empty faqsController.viewTree}">
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
       </h:panelGroup>
   </e:panelGrid>
</t:htmlTag>
   