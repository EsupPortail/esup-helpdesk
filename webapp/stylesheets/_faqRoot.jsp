<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<h:panelGroup rendered="#{faqsController.department == null && faqsController.faq == null}" >
	<e:section value="#{msgs['FAQS.TEXT.ROOT_TITLE']}" />
</h:panelGroup>

