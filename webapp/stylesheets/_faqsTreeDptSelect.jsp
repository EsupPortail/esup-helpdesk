<%@include file="_include.jsp"%>

<t:htmlTag value="div" styleClass="form-block treeview readonly-style ">
   <t:htmlTag value="div" styleClass="form-item">
        <t:tree2 id="treefaqDpt" value="#{faqsController.viewTree}"
            var="node" varNodeToggler="t" clientSideToggle="true"
            showRootNode="false" >

            
            <f:facet name="faq">
                <h:panelGroup styleClass="faq">
                          <h:panelGroup styleClass="leaf" style="cursor: pointer" onclick="simulateLinkClick('categoryFaqLinksForm:treefaqDpt:#{node.identifier}:selectFaq');return false;">
                                    <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
                                    <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
                          </h:panelGroup>

                    <h:panelGroup style="cursor: pointer; display:none" onclick="simulateLinkClick('categoryFaqLinksForm:treeFaqDpt:#{node.identifier}:selectfaq');return false;">
                        <t:graphicImage style="display:none" value="/media/images/faq.png" rendered="#{node.leaf}" />
                        <t:graphicImage style="display:none" value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" rendered="#{not node.leaf}" />
                        <e:text value=" " />
                        <t:graphicImage value="/media/images/faq-scope-#{node.faq.scope}.png" rendered="#{faqsController.editInterfaceDpt and node.faq.scope != 'DEFAULT'}" />
                        <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
                        <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
                    </h:panelGroup>

					<e:commandButton 
						value="->" id="selectFaq" style="display: none" 
						action="#{departmentsController.addCategoryFaqLink}" >
						<t:updateActionListener value="#{node.faq}" property="#{departmentsController.faqToLink}" />
					</e:commandButton>
                </h:panelGroup>
            </f:facet>
        </t:tree2>
   </t:htmlTag>
</t:htmlTag>