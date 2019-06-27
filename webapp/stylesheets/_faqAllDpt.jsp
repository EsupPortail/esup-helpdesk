<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<h:panelGroup styleClass="faq" rendered="#{faqsController.faqAllDpt != null}" >
	<e:subSection styleClass="faq-header" value="#{faqsController.faqAllDpt.label}" />
	<e:text escape="false" value="#{faqsController.faqAllDpt.content}" rendered="#{not (faqsController.userCanEdit and faqsController.editInterface)}" />
</h:panelGroup>
