<%@include file="_include.jsp"%>

<t:htmlTag value="div" id="categoriesListAllDpt" styleClass="form-block treeview basic-style" >
   <t:htmlTag value="div" styleClass="form-item">
        <t:tree2 id="tree1" value="#{faqsController.viewTreeAllDpt}" rendered="#{faqsController.viewTreeAllDpt != null}"
            var="node" varNodeToggler="t" clientSideToggle="true"
            showRootNode="false" >
            <f:facet name="root">
                <h:panelGroup>
                    <h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('faqsForm:tree1:#{node.identifier}:selectRoot1');">
                        <t:graphicImage value="/media/images/root-opened.png" rendered="#{t.nodeExpanded}" />
                        <t:graphicImage value="/media/images/root-closed.png" rendered="#{!t.nodeExpanded}" />
                        <e:text value=" " />
                        <t:graphicImage value="/media/images/faq-scope-#{domainService.departmentDefaultFaqScope}.png" rendered="#{faqsController.editInterface}" />
                        <e:bold value=" #{msgs['FAQS.TEXT.TREE_ROOT_LABEL']}" rendered="#{t.nodeSelected}" />
                        <e:text value=" #{msgs['FAQS.TEXT.TREE_ROOT_LABEL']}" rendered="#{!t.nodeSelected}" />
                    </h:panelGroup>
                    <e:commandButton value="->" id="selectRoot1" style="display: none" >
                        <t:updateActionListener value="#{node}" property="#{faqsController.node}" />
                    </e:commandButton>
                </h:panelGroup>
            </f:facet>
            <f:facet name="department">
                <h:panelGroup>
                    <h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('faqsForm:tree1:#{node.identifier}:selectDepartment1');">
                        <t:graphicImage value="#{departmentIconUrlProvider[node.department]}" />
                        <e:text value=" " />
                        <t:graphicImage value="/media/images/faq-scope-#{node.department.defaultFaqScope}.png" rendered="#{faqsController.editInterface and node.department.defaultFaqScope != 'DEFAULT'}" />
                        <e:text value=" #{node.department.label}" rendered="#{!t.nodeSelected}" />
                        <e:bold value=" #{node.department.label}" rendered="#{t.nodeSelected}" />
                    </h:panelGroup>
                    <e:commandButton value="->" id="selectDepartment1" style="display: none" >
                        <t:updateActionListener value="#{node}" property="#{faqsController.node}" />
                    </e:commandButton>
                </h:panelGroup>
            </f:facet>
            <f:facet name="faq">
                <h:panelGroup styleClass="faq">
                          <h:panelGroup styleClass="leaf" style="cursor: pointer"  rendered="#{node.leaf}" onclick="simulateLinkClick('faqsForm:tree1:#{node.identifier}:selectFaq1');return false;">
                                    <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
                                    <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
                          </h:panelGroup>
                          <h:panelGroup styleClass="parentLeaf" rendered="#{!node.leaf}">
                                    <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
                                    <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
                          </h:panelGroup>

                    <h:panelGroup style="cursor: pointer; display:none" onclick="simulateLinkClick('faqsForm:tree1:#{node.identifier}:selectFaq1');return false;">
                        <t:graphicImage style="display:none" value="/media/images/faq.png" rendered="#{node.leaf}" />
                        <t:graphicImage style="display:none" value="/media/images/faq-container-#{t.nodeExpanded?'opened':'closed'}.png" rendered="#{not node.leaf}" />
                        <e:text value=" " />
                        <t:graphicImage value="/media/images/faq-scope-#{node.faq.scope}.png" rendered="#{faqsController.editInterface and node.faq.scope != 'DEFAULT'}" />
                        <e:text value=" #{node.faq.label}" rendered="#{!t.nodeSelected}" />
                        <e:bold value=" #{node.faq.label}" rendered="#{t.nodeSelected}" />
                    </h:panelGroup>

                    <e:commandButton value="->" id="selectFaq1" style="display: none" >
                        <t:updateActionListener value="#{node}" property="#{faqsController.nodeAllDpt}" />
                    </e:commandButton>
                </h:panelGroup>
            </f:facet>
        </t:tree2>
   </t:htmlTag>
</t:htmlTag>