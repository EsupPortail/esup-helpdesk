<%@include file="_include.jsp"%>
    <t:htmlTag value="div" styleClass="form-block form-faqlinks">
        <t:htmlTag id="ticketFaqs" value="div" styleClass=" block accordion accordion-minus">
            <t:htmlTag value="div" styleClass="content">
               <t:tree2 id="faqTree" value="#{ticketController.moveFaqTree}"
					var="node" varNodeToggler="t" clientSideToggle="true"
					showRootNode="false" >
					<f:facet name="root">
						<h:panelGroup>
							<h:panelGroup style="white-space: nowrap" >
								<e:italic value=" #{msgs['TICKET_ACTION.TEXT.ADD.FAQ_LINKS_HELP']}" />
							</h:panelGroup>
						</h:panelGroup>
					</f:facet>
					<f:facet name="faq">
						<h:panelGroup>
							<h:panelGroup style="cursor: pointer; white-space: nowrap"
								onclick="showHideElement('ticketActionForm:faqTree:#{node.identifier}:faqContent');return false;">
								<e:bold value=" #{node.faq.label} " />
							</h:panelGroup>
							<t:htmlTag value="div" id="faqContent"  styleClass="faq-content" style="display: none">
							<e:text  escape="false" value="#{node.faq.content}" />
							</t:htmlTag>
						</h:panelGroup>
					</f:facet>
				</t:tree2>
            </t:htmlTag>
        </t:htmlTag>
    </t:htmlTag>