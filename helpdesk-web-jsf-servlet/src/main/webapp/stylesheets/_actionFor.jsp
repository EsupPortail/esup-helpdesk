<%@include file="_include.jsp"%>
<h:panelGroup rendered="#{not (node.forTicketCreation and node.forTicketView and node.forFaqView)}" >
	<t:graphicImage value="/media/images/action-for-ticket-creation.png" 
		rendered="#{node.forTicketCreation}" />
	<t:graphicImage value="/media/images/action-for-ticket-view.png" 
		rendered="#{node.forTicketView}" />
	<t:graphicImage value="/media/images/action-for-faq-view.png" 
		rendered="#{node.forFaqView}" />
</h:panelGroup>
